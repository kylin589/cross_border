package io.renren.modules.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.Constant;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.logistics.util.AbroadLogisticsUtil;
import io.renren.modules.product.dao.ProductsDao;
import io.renren.modules.product.dto.UploadProductDTO;
import io.renren.modules.product.entity.*;
import io.renren.modules.product.service.AmazonRateService;
import io.renren.modules.product.service.FreightCostService;
import io.renren.modules.product.service.ProductsService;
import io.renren.modules.product.service.VariantsInfoService;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysDeptService;
import io.renren.modules.sys.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author zjr
 */
@Service("productsService")
public class ProductsServiceImpl extends ServiceImpl<ProductsDao, ProductsEntity> implements ProductsService {
    @Autowired
    private VariantsInfoService variantsInfoService;

    @Autowired
    private AmazonRateService amazonRateService;

    @Autowired
    private FreightCostService freightCostService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysDeptService sysDeptService;

    private static final String AU = "AU"; // 澳大利亚
    private static final String USD = "USD";//美元
    private static final String CAD = "CAD";//加拿大元
    private static final String MXN = "MXN";// 墨西哥比索
    private static final String GBP = "GBP";// 英镑
    private static final String EUR = "EUR";// 欧元
    private static final String JPY = "JPY";// 日元
    private static final String AUD = "AUD";//澳大利亚元

    /**
     * 我的产品列表
     *
     * @param params url参数
     * @param userId 用户id
     * @return Map<Stringb j e c t> * page  产 品 p a g e * proCo u n t   产 品 数 量 * appro v e d C o u n t   审 核 通 过 * numbe r O f V a r i a n t s   包 含 变 体 的 商 品 * varia n t s C o u n t   变 体 总 数 * @auth o r zj r * @date 20 1 8 - 1 1 - 0 7 14:54:47
     */
    @Override
    public Map<String, Object> queryMyPage(Map<String, Object> params, Long userId) {
        // 分类传过来的是三级分类的id
        String category = (String) params.get("category");
        String title = (String) params.get("title");
        String sku = (String) params.get("sku");
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");
        String auditNumber = (String) params.get("auditNumber");
        String shelveNumber = (String) params.get("shelveNumber");
        String productNumber = (String) params.get("productNumber");
        //条件构造器拼接条件
        EntityWrapper<ProductsEntity> wrapper = new EntityWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(category), "category_three_id", category)
                .like(StringUtils.isNotBlank(title), "product_title", title)
                .like(StringUtils.isNotBlank(sku), "product_sku", sku)
                .ge(StringUtils.isNotBlank(startDate), "create_time", startDate)
                .le(StringUtils.isNotBlank(endDate), "create_time", endDate)
                .eq(StringUtils.isNotBlank(auditNumber), "audit_status", auditNumber)
                .eq(StringUtils.isNotBlank(shelveNumber), "shelve_status", shelveNumber)
                .eq(StringUtils.isNotBlank(productNumber), "product_type", productNumber)
                .eq("create_user_id", userId)//当前用户
                .eq("is_deleted", 0)
                .orderBy(true, "product_id", false);//时间排序
        Page<ProductsEntity> page = this.selectPage(new Query<ProductsEntity>(params).getPage(), wrapper);
        PageUtils pageUtils = new PageUtils(page);
        // 产品数量
        int proCount = this.selectCount(wrapper);
        // 审核通过
        int approvedCount;
        if ("001".equals(auditNumber)) {
            approvedCount = this.selectCount(wrapper);
        } else if ("002".equals(auditNumber)) {
            approvedCount = 0;
            //我的产品审核通过
        } else if ("003".equals(auditNumber)) {
            approvedCount = 0;
        } else {
            approvedCount = getMyApprovedCount(category, title, sku, startDate, endDate, shelveNumber, productNumber, userId, 0);
        }
        // 包含变体的商品
        int numberOfVariants = getNumberOfVariants(wrapper);
        // 变体总数
        int variantsCount = variantsInfoService.selectCount(new EntityWrapper<VariantsInfoEntity>().eq("user_id",userId));
        Map<String, Object> map = new HashMap<>(5);
        map.put("page", pageUtils);
        map.put("proCount", proCount);
        map.put("approvedCount", approvedCount);
        map.put("numberOfVariants", numberOfVariants);
        map.put("variantsCount", variantsCount);
        return map;
    }

    /**
     * 所有产品列表
     *
     * @param params url参数
     * @param deptId 公司id
     * @return Map<String , Object>
     * page 产品page
     * proCount 产品数量
     * approvedCount 审核通过
     * numberOfVariants 包含变体的商品
     * variantsCount 变体总数
     * @author zjr
     * @date 2018- 1 1 - 07 14:54:47
     */
    @Override
    public Map<String, Object> queryAllPage(Map<String, Object> params, Long deptId) {
        // 分类传过来的是三级分类的id
        String category = (String) params.get("category");
        String title = (String) params.get("title");
        String sku = (String) params.get("sku");
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");
        String auditNumber = (String) params.get("auditNumber");
        String shelveNumber = (String) params.get("shelveNumber");
        String productNumber = (String) params.get("productNumber");
        String qDeptId = (String) params.get("deptId");
        String userId = (String) params.get("userId");
        // 变体总数
        int variantsCount = 0;
        //条件构造器拼接条件
        EntityWrapper<ProductsEntity> wrapper = new EntityWrapper<>();
        //管理员
        if (deptId == 1) {
            wrapper.eq(StringUtils.isNotBlank(category), "category_three_id", category)
                    .like(StringUtils.isNotBlank(title), "product_title", title)
                    .like(StringUtils.isNotBlank(sku), "product_sku", sku)
                    .ge(StringUtils.isNotBlank(startDate), "create_time", startDate)
                    .le(StringUtils.isNotBlank(endDate), "create_time", endDate)
                    .eq(StringUtils.isNotBlank(auditNumber), "audit_status", auditNumber)
                    .eq(StringUtils.isNotBlank(shelveNumber), "shelve_status", shelveNumber)
                    .eq(StringUtils.isNotBlank(productNumber), "product_type", productNumber)
                    .eq(StringUtils.isNotBlank(userId), "create_user_id", userId)
                    .eq(StringUtils.isNotBlank(qDeptId), "dept_id", qDeptId)
                    .eq("is_deleted", 0)
                    .orderBy(true, "product_id", false);//时间排序
            variantsCount = variantsInfoService.selectCount(null);
        } else {
            //加盟商
            wrapper.eq(StringUtils.isNotBlank(category), "category_three_id", category)
                    .like(StringUtils.isNotBlank(title), "product_title", title)
                    .like(StringUtils.isNotBlank(sku), "product_sku", sku)
                    .ge(StringUtils.isNotBlank(startDate), "create_time", startDate)
                    .le(StringUtils.isNotBlank(endDate), "create_time", endDate)
                    .eq(StringUtils.isNotBlank(auditNumber), "audit_status", auditNumber)
                    .eq(StringUtils.isNotBlank(shelveNumber), "shelve_status", shelveNumber)
                    .eq(StringUtils.isNotBlank(productNumber), "product_type", productNumber)
                    .eq(StringUtils.isNotBlank(userId), "create_user_id", userId)
                    .eq("dept_id", deptId)
                    .eq("is_deleted", 0)
                    .orderBy(true, "product_id", false);//时间排序
            variantsCount = variantsInfoService.selectCount(new EntityWrapper<VariantsInfoEntity>().eq("dept_id",deptId));
        }
        Page<ProductsEntity> page = this.selectPage(new Query<ProductsEntity>(params).getPage(), wrapper);
        PageUtils pageUtils = new PageUtils(page);
        List<ProductsEntity> productList = page.getRecords();
        for(ProductsEntity productsEntity : productList){
            SysUserEntity sysUserEntity = sysUserService.selectById(productsEntity.getCreateUserId());
            SysDeptEntity sysDeptEntity = sysDeptService.selectById(productsEntity.getDeptId());
            if(sysDeptEntity != null){
                productsEntity.setDeptName(sysDeptEntity.getName());
            }
            if(sysUserEntity != null){
                productsEntity.setUserName(sysUserEntity.getDisplayName());
            }
        }
        // 产品数量
        int proCount = this.selectCount(wrapper);
        // 审核通过
        int approvedCount;
        if ("001".equals(auditNumber)) {
            approvedCount = this.selectCount(wrapper);
        } else if ("002".equals(auditNumber)) {
            approvedCount = 0;
        } else if ("003".equals(auditNumber)) {
            approvedCount = 0;
        } else {
            //所有产品审核通过
            approvedCount = getAllApprovedCount(category, title, sku, startDate, endDate, shelveNumber, productNumber, deptId, 0);
        }

    // 包含变体的商品
    int numberOfVariants = getNumberOfVariants(wrapper);

    Map<String, Object> map = new
            HashMap<>(5);
        map.put("page",pageUtils);
        map.put("proCount",proCount);
        map.put("approvedCount",approvedCount);
        map.put("numberOfVariants",numberOfVariants);
        map.put("variantsCount",variantsCount);
        return map;
}

    /**
     * 认领产品列表
     *
     * @param params url参数
     * @param deptId 公司id
     * @return Map<String , Object>
     * page 产品page
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @Override
    public PageUtils queryClaimPage(Map<String, Object> params, Long deptId) {
        // 分类传过来的是三级分类的id
        String category = (String) params.get("category");
        String title = (String) params.get("title");
        String sku = (String) params.get("sku");
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");
        String auditNumber = (String) params.get("auditNumber");
        String shelveNumber = (String) params.get("shelveNumber");
        String productNumber = (String) params.get("productNumber");
        String userId = (String) params.get("userId");
        //条件构造器拼接条件
        EntityWrapper<ProductsEntity> wrapper = new EntityWrapper<>();
        //管理员
        if (deptId == 1) {
            wrapper.eq(StringUtils.isNotBlank(category), "category_three_id", category)
                    .like(StringUtils.isNotBlank(title), "product_title", title)
                    .like(StringUtils.isNotBlank(sku), "product_sku", sku)
                    .ge(StringUtils.isNotBlank(startDate), "create_time", startDate)
                    .le(StringUtils.isNotBlank(endDate), "create_time", endDate)
                    .eq(StringUtils.isNotBlank(auditNumber), "audit_status", auditNumber)
                    .eq(StringUtils.isNotBlank(shelveNumber), "shelve_status", shelveNumber)
                    .eq(StringUtils.isNotBlank(productNumber), "product_type", productNumber)
                    .eq(StringUtils.isNotBlank(userId), "create_user_id", userId)
                    .ne("product_type", "007")
                    .eq("is_deleted", 0)
                    .orderBy(true, "product_id", false)//时间排序
                    .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER));
        } else {
            //加盟商
            wrapper.eq(StringUtils.isNotBlank(category), "category_three_id", category)
                    .like(StringUtils.isNotBlank(title), "product_title", title)
                    .like(StringUtils.isNotBlank(sku), "product_sku", sku)
                    .ge(StringUtils.isNotBlank(startDate), "create_time", startDate)
                    .le(StringUtils.isNotBlank(endDate), "create_time", endDate)
                    .eq(StringUtils.isNotBlank(auditNumber), "audit_status", auditNumber)
                    .eq(StringUtils.isNotBlank(shelveNumber), "shelve_status", shelveNumber)
                    .eq(StringUtils.isNotBlank(productNumber), "product_type", productNumber)
                    .eq(StringUtils.isNotBlank(userId), "create_user_id", userId)
                    .ne("product_type", "007")
                    .eq("dept_id", deptId)
                    .eq("is_deleted", 0)
                    .orderBy(true, "product_id", false)//时间排序
                    .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER));
        }
        Page<ProductsEntity> page = this.selectPage(new Query<ProductsEntity>(params).getPage(), wrapper);
        List<ProductsEntity> productList = page.getRecords();
        for(ProductsEntity productsEntity : productList){
            SysUserEntity sysUserEntity = sysUserService.selectById(productsEntity.getCreateUserId());
            SysDeptEntity sysDeptEntity = sysDeptService.selectById(productsEntity.getDeptId());
            if(sysDeptEntity != null){
                productsEntity.setDeptName(sysDeptEntity.getName());
            }
            if(sysUserEntity != null){
                productsEntity.setUserName(sysUserEntity.getDisplayName());
            }
        }
        return new PageUtils(page);
    }

    /**
     * 产品回收站
     *
     * @param params url参数
     * @param userId 用户id
     * @return Map<String ,Object>
     * page 产品page
     * proCount 产品数量
     * approvedCount 审核通过
     * numberOfVariants 包含变体的商品
     * variantsCount 变体总数
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @Override
    public Map<String, Object> queryRecyclingPage(Map<String, Object> params, Long userId) {

        // 分类传过来的是三级分类的id
        String category = (String) params.get("category");
        String title = (String) params.get("title");
        String sku = (String) params.get("sku");
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");
        String auditNumber = (String) params.get("auditNumber");
        String shelveNumber = (String) params.get("shelveNumber");
        String productNumber = (String) params.get("productNumber");
        EntityWrapper<ProductsEntity> wrapper = new EntityWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(category), "category_three_id", category)
                .like(StringUtils.isNotBlank(title), "product_title", title)
                .like(StringUtils.isNotBlank(sku), "product_sku", sku)
                .ge(StringUtils.isNotBlank(startDate), "create_time", startDate)
                .le(StringUtils.isNotBlank(endDate), "create_time", endDate)
                .eq(StringUtils.isNotBlank(auditNumber), "audit_status", auditNumber)
                .eq(StringUtils.isNotBlank(shelveNumber), "shelve_status", shelveNumber)
                .eq(StringUtils.isNotBlank(productNumber), "product_type", productNumber)
                .eq("create_user_id", userId)
                .eq("is_deleted", 1)
                .orderBy(true, "product_id", false)
                .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER));
        Page<ProductsEntity> page = this.selectPage(new Query<ProductsEntity>(params).getPage(), wrapper);
        PageUtils pageUtils = new PageUtils(page);
        List<ProductsEntity> list = (List<ProductsEntity>) pageUtils.getList();
       /* for (int i = 0; i < list.size(); i++) {
            ImageAddressEntity imageAddressEntity = new ImageAddressEntity();
            imageAddressEntity.setImageId(list.get(i).getMainImageId());
            imageAddressEntity = imageAddressService.selectById(imageAddressEntity);
            list.get(i).setMainImageUrl(imageAddressEntity.getImageUrl());
        }*/
        pageUtils.setList(list);
        // 产品数量
        int proCount = this.selectCount(wrapper);
        // 审核通过
        int approvedCount;
        if ("001".equals(auditNumber)) {
            approvedCount = this.selectCount(wrapper);
        } else if("002".equals(auditNumber)){
            approvedCount=0;
        }else if("003".equals(auditNumber)){
            approvedCount=0;
        } else {
            approvedCount = getMyApprovedCount(category, title, sku, startDate, endDate, shelveNumber, productNumber, userId, 1);
        }
        // 包含变体的商品
        int numberOfVariants = getNumberOfVariants(wrapper);
        // 变体总数
        int variantsCount = getWariantsCount(wrapper);
        Map<String, Object> map = new HashMap<>(5);
        map.put("page", pageUtils);
        map.put("proCount", proCount);
        map.put("approvedCount", approvedCount);
        map.put("numberOfVariants", numberOfVariants);
        map.put("variantsCount", variantsCount);
        return map;
    }

    /**
     * 我的产品审核通过总数
     *
     * @param category      分类
     * @param title         标题
     * @param sku
     * @param startDate     开始时间
     * @param endDate       结束时间
     * @param shelveNumber  上架状态
     * @param productNumber 产品存放类型
     * @param userId        用户ids
     * @param isDeleted     是否删除
     * @return 总数
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @Override
    public int getMyApprovedCount(String category, String title, String sku, String startDate, String endDate, String shelveNumber, String productNumber, Long userId, int isDeleted) {
        String timeType = "product_id";
        if ("1".equals(isDeleted)) {
            timeType = "product_id";
        }
        EntityWrapper<ProductsEntity> approvedCountWrapper = new EntityWrapper<>();
        approvedCountWrapper.eq(StringUtils.isNotBlank(category), "category_three_id", category)
                .like(StringUtils.isNotBlank(title), "product_title", title)
                .like(StringUtils.isNotBlank(sku), "product_sku", sku)
                .ge(StringUtils.isNotBlank(startDate), "create_time", startDate)
                .le(StringUtils.isNotBlank(endDate), "create_time", endDate)
                .eq("audit_status", "001")
                .eq(StringUtils.isNotBlank(shelveNumber), "shelve_status", shelveNumber)
                .eq(StringUtils.isNotBlank(productNumber), "product_type", productNumber)
                .eq("create_user_id", userId)
                .eq("is_deleted", isDeleted)
                .orderBy(true, timeType, false);
        return this.selectCount(approvedCountWrapper);
    }

    /**
     * 所有产品审核通过总数
     *
     * @param category      分类
     * @param title         标题
     * @param sku
     * @param startDate     开始时间
     * @param endDate       结束时间
     * @param shelveNumber  上架状态
     * @param productNumber 产品存放类型
     * @param deptId        用户ids
     * @param isDeleted     是否删除
     * @return 总数
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @Override
    public int getAllApprovedCount(String category, String title, String sku, String startDate, String endDate, String shelveNumber, String productNumber, Long deptId, int isDeleted) {
        String timeType = "create_time";
//        if ("1".equals(isDeleted)) {
//            timeType = "create_time";
//        }
        EntityWrapper<ProductsEntity> approvedCountWrapper = new EntityWrapper<>();
        if (deptId == 1) {
            approvedCountWrapper.eq(StringUtils.isNotBlank(category), "category_three_id", category)
                    .like(StringUtils.isNotBlank(title), "product_title", title)
                    .like(StringUtils.isNotBlank(sku), "product_sku", sku)
                    .ge(StringUtils.isNotBlank(startDate), "create_time", startDate)
                    .le(StringUtils.isNotBlank(endDate), "create_time", endDate)
                    .eq("audit_status", "001")
                    .eq(StringUtils.isNotBlank(shelveNumber), "shelve_status", shelveNumber)
                    .eq(StringUtils.isNotBlank(productNumber), "product_type", productNumber)
                    .eq("is_deleted", isDeleted)
                    .orderBy(true, timeType, false);
        } else {
            approvedCountWrapper.eq(StringUtils.isNotBlank(category), "category_three_id", category)
                    .like(StringUtils.isNotBlank(title), "product_title", title)
                    .like(StringUtils.isNotBlank(sku), "product_sku", sku)
                    .ge(StringUtils.isNotBlank(startDate), "create_time", startDate)
                    .le(StringUtils.isNotBlank(endDate), "create_time", endDate)
                    .eq("audit_status", "001")
                    .eq(StringUtils.isNotBlank(shelveNumber), "shelve_status", shelveNumber)
                    .eq(StringUtils.isNotBlank(productNumber), "product_type", productNumber)
                    .eq("dept_id", deptId)
                    .eq("is_deleted", isDeleted)
                    .orderBy(true, timeType, false);
        }
        return this.selectCount(approvedCountWrapper);
    }

    /**
     * 统计包含变体的商品总数
     *
     * @param wrapper 查询条件
     * @return 总数
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @Override
    public int getNumberOfVariants(EntityWrapper<ProductsEntity> wrapper) {
        wrapper.andNew().isNotNull("color_id").or().isNotNull("size_id");
        return this.selectCount(wrapper);
    }

    /**
     * 统计变体总数
     *
     * @param wrapper 查询条件
     * @return 总数
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @Override
    public int getWariantsCount(EntityWrapper<ProductsEntity> wrapper) {
        List<ProductsEntity> list = this.selectList(wrapper);
        if(list.size() > 0 ){
            Long[] proIds = new Long[list.size()];
            for (int i = 0; i < list.size(); i++) {
                proIds[i] = list.get(i).getProductId();
            }
            EntityWrapper<VariantsInfoEntity> variantsCountWrapper = new EntityWrapper<VariantsInfoEntity>();
            variantsCountWrapper.in("product_id", proIds);
            return variantsInfoService.selectCount(variantsCountWrapper);
        }else{
            return 0;
        }
    }

    /**
     * 获取新商品的编号
     *
     * @param userId 用户id
     * @return 编号
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @Override
    public ProductsEntity getNewProductId(Long userId) {
        ProductsEntity productsEntity = new ProductsEntity();
        //美国运费
        FreightCostEntity americanFC = new FreightCostEntity();
        productsEntity.setAmericanFC(americanFC);
        // 加拿大运费
        FreightCostEntity canadaFC = new FreightCostEntity();
        productsEntity.setCanadaFC(canadaFC);
        // 墨西哥运费
        FreightCostEntity mexicoFC = new FreightCostEntity();
        productsEntity.setMexicoFC(mexicoFC);
        //英国运费
        FreightCostEntity britainFC = new FreightCostEntity();
        productsEntity.setBritainFC(britainFC);
        // 法国运费
        FreightCostEntity franceFC = new FreightCostEntity();
        productsEntity.setFranceFC(franceFC);
        // 德国运费
        FreightCostEntity germanyFC = new FreightCostEntity();
        productsEntity.setGermanyFC(germanyFC);
        //意大利运费
        FreightCostEntity italyFC = new FreightCostEntity();
        productsEntity.setItalyFC(italyFC);

        //西班牙运费
        FreightCostEntity spainFC = new FreightCostEntity();
        productsEntity.setSpainFC(spainFC);

        // 日本运费
        FreightCostEntity japanFC = new FreightCostEntity();
        productsEntity.setJapanFC(japanFC);

        //澳大利亚运费
        FreightCostEntity australiaFC = new FreightCostEntity();
        productsEntity.setAustraliaFC(australiaFC);
        //各个国家的介绍
        //中文介绍
        IntroductionEntity chinesePRE = new IntroductionEntity();
        productsEntity.setChinesePRE(chinesePRE);
        //英文介绍
        IntroductionEntity britainPRE = new IntroductionEntity();
        productsEntity.setBritainPRE(britainPRE);
        //法语介绍
        IntroductionEntity francePRE = new IntroductionEntity();
        productsEntity.setFrancePRE(francePRE);
        //德语介绍
        IntroductionEntity germanyPRE = new IntroductionEntity();
        productsEntity.setGermanyPRE(germanyPRE);
        //意大利语介绍
        IntroductionEntity italyPRE = new IntroductionEntity();
        productsEntity.setItalyPRE(italyPRE);
        //西班牙语介绍
        IntroductionEntity spainPRE = new IntroductionEntity();
        productsEntity.setSpainPRE(spainPRE);
        //日语介绍
        IntroductionEntity japanPRE = new IntroductionEntity();
        productsEntity.setJapanPRE(japanPRE);
        productsEntity.setCreateTime(new Date());
        productsEntity.setCreateUserId(userId);
        productsEntity.setIsDeleted(0);
        this.insert(productsEntity);
        return productsEntity;
    }


    /**
     * 所有产品
     *
     * @methodname: auditCountAll 审核类型分类统计每个分类的总数
     * @param: [number, del]
     * @return: int
     * @auther: jhy
     * @date: 2018/11/13 23:43
     */
    @Override
    public int auditCountAll(String number, String del, Long deptId) {
        int auditCount = 0;
        if (deptId == 1) {
            auditCount = this.selectCount(new EntityWrapper<ProductsEntity>().eq("audit_status", number).eq("is_deleted", del));
        } else {
            auditCount = this.selectCount(new EntityWrapper<ProductsEntity>().eq("audit_status", number).eq("is_deleted", del).eq("dept_id", deptId));
        }
        return auditCount;
    }

    /**
     * 所有产品
     *
     * @methodname: putawayCountAll 上架类型分类统计每个分类的总数
     * @param: [number, del]
     * @return: int
     * @auther: jhy
     * @date: 2018/11/13 23:43
     */
    @Override
    public int putawayCountAll(String number, String del, Long deptId) {
        int putawayCount = 0;
        if (deptId == 1) {
            putawayCount = this.selectCount(new EntityWrapper<ProductsEntity>().eq("shelve_status", number).eq("is_deleted", del));
        } else {
            putawayCount = this.selectCount(new EntityWrapper<ProductsEntity>().eq("shelve_status", number).eq("is_deleted", del).eq("dept_id", deptId));
        }
        return putawayCount;
    }

    /**
     * 所有产品
     *
     * @methodname: productCountAll 产品类型分类统计每个分类的总数
     * @param: [number, del]
     * @return: int
     * @auther: jhy
     * @date: 2018/11/13 23:44
     */
    @Override
    public int productCountAll(String number, String del, Long deptId) {
        int productCount = 0;
        if (deptId == 1) {
            productCount = this.selectCount(new EntityWrapper<ProductsEntity>().eq("product_type", number).eq("is_deleted", del));
        } else {
            productCount = this.selectCount(new EntityWrapper<ProductsEntity>().eq("product_type", number).eq("is_deleted", del).eq("dept_id", deptId));
        }

        return productCount;
    }

    /**
     * 获取我的产品数量
     *
     * @param params url 参数
     * @param userId 用户id
     * @param isDel  是否删除
     * @return 数量
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @Override
    public int getMyTotalCount(Map<String, Object> params, Long userId, String isDel) {
        // 分类传过来的是三级分类的id
        String category = (String) params.get("category");
        String title = (String) params.get("title");
        String sku = (String) params.get("sku");
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");
        String auditNumber = (String) params.get("auditNumber");
        String shelveNumber = (String) params.get("shelveNumber");
        String productNumber = (String) params.get("productNumber");

        /*startDate = "";
        endDate = "";*/

        String timeType = "create_time";
        /*if ("1".equals(isDel)) {
            timeType = "create_time";
        }*/

        EntityWrapper<ProductsEntity> wrapper = new EntityWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(category), "category_three_id", category)
                .like(StringUtils.isNotBlank(title), "product_title", title)
                .like(StringUtils.isNotBlank(sku), "product_sku", sku)
                .ge(StringUtils.isNotBlank(startDate), "create_time", startDate)
                .le(StringUtils.isNotBlank(endDate), "create_time", endDate)
                .eq(StringUtils.isNotBlank(auditNumber), "audit_status", auditNumber)
                .eq(StringUtils.isNotBlank(shelveNumber), "shelve_status", shelveNumber)
                .eq(StringUtils.isNotBlank(productNumber), "product_type", productNumber)
                .eq("create_user_id", userId)
                .eq("is_deleted", isDel)
                .orderBy(true, timeType, false)
                .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER));
        // 产品数量
        int proCount = this.selectCount(wrapper);
        return proCount;
    }

    /**
     * 获取所有产品数量
     *
     * @param params url 参数
     * @param deptId 用户id
     * @param isDel  是否删除
     * @return 数量
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @Override
    public int getAllTotalCount(Map<String, Object> params, Long deptId, String isDel) {
        // 分类传过来的是三级分类的id
        String category = (String) params.get("category");
        String title = (String) params.get("title");
        String sku = (String) params.get("sku");
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");
        String auditNumber = (String) params.get("auditNumber");
        String shelveNumber = (String) params.get("shelveNumber");
        String productNumber = (String) params.get("productNumber");

        /*startDate = "";
        endDate = "";*/

        String timeType = "product_id";
        /*if ("1".equals(isDel)) {
            timeType = "create_time";
        }*/

        EntityWrapper<ProductsEntity> wrapper = new EntityWrapper<>();
        if (deptId == 1) {
            wrapper.eq(StringUtils.isNotBlank(category), "category_three_id", category)
                    .like(StringUtils.isNotBlank(title), "product_title", title)
                    .like(StringUtils.isNotBlank(sku), "product_sku", sku)
                    .ge(StringUtils.isNotBlank(startDate), "create_time", startDate)
                    .le(StringUtils.isNotBlank(endDate), "create_time", endDate)
                    .eq(StringUtils.isNotBlank(auditNumber), "audit_status", auditNumber)
                    .eq(StringUtils.isNotBlank(shelveNumber), "shelve_status", shelveNumber)
                    .eq(StringUtils.isNotBlank(productNumber), "product_type", productNumber)
                    .eq("is_deleted", isDel)
                    .orderBy(true, timeType, false)
                    .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER));
        } else {
            wrapper.eq(StringUtils.isNotBlank(category), "category_three_id", category)
                    .like(StringUtils.isNotBlank(title), "product_title", title)
                    .like(StringUtils.isNotBlank(sku), "product_sku", sku)
                    .ge(StringUtils.isNotBlank(startDate), "create_time", startDate)
                    .le(StringUtils.isNotBlank(endDate), "create_time", endDate)
                    .eq(StringUtils.isNotBlank(auditNumber), "audit_status", auditNumber)
                    .eq(StringUtils.isNotBlank(shelveNumber), "shelve_status", shelveNumber)
                    .eq(StringUtils.isNotBlank(productNumber), "product_type", productNumber)
                    .eq("dept_id", deptId)
                    .eq("is_deleted", isDel)
                    .orderBy(true, timeType, false)
                    .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER));
        }

        // 产品数量
        int proCount = this.selectCount(wrapper);
        return proCount;
    }

    /**
     * @methodname: relationVariantColor 变体颜色与产品绑定
     * @param: [productId, variantParameterId]
     * @return: boolean
     * @auther: jhy
     * @date: 2018/11/30 14:18
     */
    @Override
    public boolean relationVariantColor(Long productId, Long variantParameterId) {
        int flag = baseMapper.relationVariantColor(productId, variantParameterId);
        if (flag != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @methodname: relationVariantSize 变体尺寸与产品绑定
     * @param: [productId, variantParameterId]
     * @return: boolean
     * @auther: jhy
     * @date: 2018/11/30 14:19
     */
    @Override
    public boolean relationVariantSize(Long productId, Long variantParameterId) {
        int flag = baseMapper.relationVariantSize(productId, variantParameterId);
        if (flag != 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public UploadProductDTO selectCanUploadProducts(List<Long> idList, Long userId) {
        UploadProductDTO dto = new UploadProductDTO();
        List<ProductsEntity> list = this.selectBatchIds(idList);
        List<ProductsEntity> productsList = new ArrayList<>();
        List<Long> ret = new ArrayList<Long>();
        for (int i = 0; i < list.size(); i++) {
            ProductsEntity product = list.get(i);
            if(product != null && product.getCreateUserId() != null){
                if (product.getCreateUserId().longValue() == userId.longValue()) {
                    if ("001".equals(product.getAuditStatus()) && "001".equals(product.getShelveStatus())) {
                        productsList.add(product);
                        ret.add(product.getProductId());
                    } else {
                        dto.setCode("error");
                        if ((!"001".equals(product.getAuditStatus()))) {
                            System.out.println("id==" + product.getProductId());
                            dto.setMsg("有产品没有通过审核");
                        } else if ((!"001".equals(product.getShelveStatus()))) {
                            dto.setMsg("有产品没有上架");
                        } else {
                            dto.setMsg("有产品没有通过审核/上架");
                        }
                        return dto;
                    }
                }
            }
        }
        dto.setProductsList(productsList);
        dto.setRet(ret);
        return dto;
    }

    @Override
    public UploadProductDTO isNotCanUpload(Long startId, Long endId, Long userId) {
        UploadProductDTO dto = new UploadProductDTO();
        List<ProductsEntity> list = this.selectList(new EntityWrapper<ProductsEntity>().ge("product_id",startId).and().le("product_id",endId));
        List<ProductsEntity> productsList = new ArrayList<>();
        List<Long> ret = new ArrayList<Long>();
        for (int i = 0; i < list.size(); i++) {
            ProductsEntity product = list.get(i);
            if(product != null && product.getCreateUserId() != null){
                if (product.getCreateUserId().longValue() == userId.longValue()) {
                    if ("001".equals(product.getAuditStatus()) && "001".equals(product.getShelveStatus())) {
                        productsList.add(product);
                        ret.add(product.getProductId());
                    } else {
                        dto.setCode("error");
                        if ((!"001".equals(product.getAuditStatus()))) {
                            System.out.println("id==" + product.getProductId());
                            dto.setMsg("有产品没有通过审核");
                        } else if ((!"001".equals(product.getShelveStatus()))) {
                            dto.setMsg("有产品没有上架");
                        } else {
                            dto.setMsg("有产品没有通过审核/上架");
                        }
                        return dto;
                    }
                }
            }
        }
        dto.setProductsList(productsList);
        dto.setRet(ret);
        return dto;
    }

    @Override
    public Long queryIdBySku(String sku) {
        VariantsInfoEntity variantsInfoEntity =variantsInfoService.selectOne(new EntityWrapper<VariantsInfoEntity>().eq("variant_sku",sku));
        if (variantsInfoEntity!=null){
            return variantsInfoEntity.getProductId();
        }else{
            // 变体表查不到，去商品表查
            ProductsEntity productsEntity = this.selectOne(new EntityWrapper<ProductsEntity>().eq("product_sku",sku));
            if (productsEntity!=null){
                return productsEntity.getProductId();
            }
        }
        return null;
    }

    @Override
    public ProductsEntity costFreight(ProductsEntity productsEntity) {
        //产品重量
        Double productWeight = productsEntity.getProductWeight();
//        //产品长
//        Double productLength = productsEntity.getProductLength();
//        //产品宽
//        Double productWide = productsEntity.getProductWide();
//        //产品高
//        Double productHeight = productsEntity.getProductHeight();
        //产品采购价格
        BigDecimal purchasePrice = productsEntity.getPurchasePrice();
        //国内运费
        BigDecimal domesticFreight = productsEntity.getDomesticFreight();
        //折扣系数
        BigDecimal discount = productsEntity.getDiscount();
        //计算成本计算先把产品采购价格和国内运费相加
        BigDecimal sum = purchasePrice.add(domesticFreight);

        //获取各个国家国际运费
        Map<String, String> freightMap = AbroadLogisticsUtil.getSaleDetail(new BigDecimal(productWeight));
        //美国运费
        FreightCostEntity americanFC = productsEntity.getAmericanFC();
        if (americanFC == null) {
            americanFC = new FreightCostEntity();
        }
        //美国运费
        BigDecimal americanFreight = new BigDecimal(freightMap.get("americanFreight"));
        americanFC.setFreight(americanFreight);
        //美国成本价格=采购价格+国内物流+国际物流价格
        BigDecimal americanCost = americanFreight.add(sum);
        //计算出美国售价，外币，优化信息
        americanFC = priceInfo(americanCost, americanFC, USD, discount);

        productsEntity.setAmericanFC(americanFC);


        // 加拿大运费
        FreightCostEntity canadaFC = productsEntity.getCanadaFC();
        if (canadaFC == null) {
            canadaFC = new FreightCostEntity();
        }
        //加拿大运费
        BigDecimal canadaFreight = new BigDecimal(freightMap.get("canadaFreight"));
        canadaFC.setFreight(canadaFreight);
        BigDecimal canadaCost = canadaFreight.add(sum);
        canadaFC = priceInfo(canadaCost, canadaFC, CAD, discount);
        productsEntity.setCanadaFC(canadaFC);

        // 墨西哥运费
        FreightCostEntity mexicoFC = productsEntity.getMexicoFC();
        if (mexicoFC == null) {
            mexicoFC = new FreightCostEntity();
        }
        BigDecimal mexicoFreight = new BigDecimal(freightMap.get("mexicoFreight"));
        mexicoFC.setFreight(mexicoFreight);
        BigDecimal mexicoCost = mexicoFreight.add(sum);
        mexicoFC = priceInfo(mexicoCost, mexicoFC, MXN, discount);
        productsEntity.setMexicoFC(mexicoFC);

        //英国运费
        FreightCostEntity britainFC = productsEntity.getBritainFC();
        if (britainFC == null) {
            britainFC = new FreightCostEntity();
        }
        BigDecimal britainFreight = new BigDecimal(freightMap.get("britainFreight"));
        britainFC.setFreight(britainFreight);
        BigDecimal britainCost = britainFreight.add(sum);
        britainFC = priceInfo(britainCost, britainFC, GBP, discount);
        productsEntity.setBritainFC(britainFC);

        // 法国运费
        FreightCostEntity franceFC = productsEntity.getFranceFC();
        if (franceFC == null) {
            franceFC = new FreightCostEntity();
        }
        BigDecimal franceFreight = new BigDecimal(freightMap.get("franceFreight"));
        franceFC.setFreight(franceFreight);
        BigDecimal franceCost = franceFreight.add(sum);
        franceFC = priceInfo(franceCost, franceFC, EUR, discount);
        productsEntity.setFranceFC(franceFC);

        // 德国运费
        FreightCostEntity germanyFC = productsEntity.getGermanyFC();
        if (germanyFC == null) {
            germanyFC = new FreightCostEntity();
        }
        BigDecimal germanyFreight = new BigDecimal(freightMap.get("germanyFreight"));
        germanyFC.setFreight(germanyFreight);
        BigDecimal germanyCost = germanyFreight.add(sum);
        germanyFC = priceInfo(germanyCost, germanyFC, EUR, discount);
        productsEntity.setGermanyFC(germanyFC);

        //意大利运费
        FreightCostEntity italyFC = productsEntity.getItalyFC();
        if (italyFC == null) {
            italyFC = new FreightCostEntity();
        }
        BigDecimal italyFreight = new BigDecimal(freightMap.get("italyFreight"));
        italyFC.setFreight(italyFreight);
        BigDecimal italyCost = italyFreight.add(sum);
        italyFC = priceInfo(italyCost, italyFC, EUR, discount);
        productsEntity.setItalyFC(italyFC);

        //西班牙运费
        FreightCostEntity spainFC = productsEntity.getSpainFC();
        if (spainFC == null) {
            spainFC = new FreightCostEntity();

        }
        BigDecimal spainFreight = new BigDecimal(freightMap.get("spainFreight"));
        spainFC.setFreight(spainFreight);
        BigDecimal spainCost = spainFreight.add(sum);
        spainFC = priceInfo(spainCost, spainFC, EUR, discount);
        productsEntity.setSpainFC(spainFC);

        // 日本运费
        FreightCostEntity japanFC = productsEntity.getJapanFC();
        if (japanFC == null) {
            japanFC = new FreightCostEntity();
        }
        BigDecimal japanFreight = new BigDecimal(freightMap.get("japanFreight"));
        japanFC.setFreight(japanFreight);
        BigDecimal japanCost = japanFreight.add(sum);
        japanFC = priceInfo(japanCost, japanFC, JPY, discount);
        productsEntity.setJapanFC(japanFC);


        //澳大利亚运费
        FreightCostEntity australiaFC = productsEntity.getAustraliaFC();
        if (australiaFC == null) {
            australiaFC = new FreightCostEntity();
        }
        BigDecimal australiaFreight = new BigDecimal(freightMap.get("australiaFreight"));
        australiaFC.setFreight(australiaFreight);
        BigDecimal australiaCost = australiaFreight.add(sum);
        australiaFC = priceInfo(australiaCost, australiaFC, AUD, discount);
        productsEntity.setAustraliaFC(australiaFC);
        return productsEntity;
    }
    @Override
    public ProductsEntity costFreightSave(ProductsEntity productsEntity) {
        //产品重量
        Double productWeight = productsEntity.getProductWeight();
//        //产品长
//        Double productLength = productsEntity.getProductLength();
//        //产品宽
//        Double productWide = productsEntity.getProductWide();
//        //产品高
//        Double productHeight = productsEntity.getProductHeight();
        //产品采购价格
        BigDecimal purchasePrice = productsEntity.getPurchasePrice();
        //国内运费
        BigDecimal domesticFreight = productsEntity.getDomesticFreight();
        //折扣系数
        BigDecimal discount = productsEntity.getDiscount();
        //计算成本计算先把产品采购价格和国内运费相加
        BigDecimal sum = purchasePrice.add(domesticFreight);

        //获取各个国家国际运费
        Map<String, String> freightMap = AbroadLogisticsUtil.getSaleDetail(new BigDecimal(productWeight));
        //美国运费
        FreightCostEntity americanFC = productsEntity.getAmericanFC();
        if (americanFC == null) {
            americanFC = new FreightCostEntity();
        }
        //美国运费
        BigDecimal americanFreight = new BigDecimal(freightMap.get("americanFreight"));
        americanFC.setFreight(americanFreight);
        //美国成本价格=采购价格+国内物流+国际物流价格
        BigDecimal americanCost = americanFreight.add(sum);
        //计算出美国售价，外币，优化信息
        americanFC = priceInfo(americanCost, americanFC, USD, discount);
        if(americanFC.getFreightCostId() != null){
            freightCostService.updateById(americanFC);
        }else{
            freightCostService.insert(americanFC);
        }
        productsEntity.setAmericanFreight(americanFC.getFreightCostId());
        productsEntity.setAmericanFC(americanFC);

        // 加拿大运费
        FreightCostEntity canadaFC = productsEntity.getCanadaFC();
        if (canadaFC == null) {
            canadaFC = new FreightCostEntity();
        }
        //加拿大运费
        BigDecimal canadaFreight = new BigDecimal(freightMap.get("canadaFreight"));
        canadaFC.setFreight(canadaFreight);
        BigDecimal canadaCost = canadaFreight.add(sum);
        canadaFC = priceInfo(canadaCost, canadaFC, CAD, discount);
        if(canadaFC.getFreightCostId() != null){
            freightCostService.updateById(canadaFC);
        }else{
            freightCostService.insert(canadaFC);
        }
        productsEntity.setCanadaFreight(canadaFC.getFreightCostId());
        productsEntity.setCanadaFC(canadaFC);

        // 墨西哥运费
        FreightCostEntity mexicoFC = productsEntity.getMexicoFC();
        if (mexicoFC == null) {
            mexicoFC = new FreightCostEntity();
        }
        BigDecimal mexicoFreight = new BigDecimal(freightMap.get("mexicoFreight"));
        mexicoFC.setFreight(mexicoFreight);
        BigDecimal mexicoCost = mexicoFreight.add(sum);
        mexicoFC = priceInfo(mexicoCost, mexicoFC, MXN, discount);
        if(mexicoFC.getFreightCostId() != null){
            freightCostService.updateById(mexicoFC);
        }else {
            freightCostService.insert(mexicoFC);
        }
        productsEntity.setMexicoFreight(mexicoFC.getFreightCostId());
        productsEntity.setMexicoFC(mexicoFC);
        //英国运费
        FreightCostEntity britainFC = productsEntity.getBritainFC();
        if (britainFC == null) {
            britainFC = new FreightCostEntity();
        }
        BigDecimal britainFreight = new BigDecimal(freightMap.get("britainFreight"));
        britainFC.setFreight(britainFreight);
        BigDecimal britainCost = britainFreight.add(sum);
        britainFC = priceInfo(britainCost, britainFC, GBP, discount);
        if(britainFC.getFreightCostId() != null){
            freightCostService.updateById(britainFC);
        }else{
            freightCostService.insert(britainFC);
        }
        productsEntity.setBritainFreight(britainFC.getFreightCostId());
        productsEntity.setBritainFC(britainFC);

        // 法国运费
        FreightCostEntity franceFC = productsEntity.getFranceFC();
        if (franceFC == null) {
            franceFC = new FreightCostEntity();
        }
        BigDecimal franceFreight = new BigDecimal(freightMap.get("franceFreight"));
        franceFC.setFreight(franceFreight);
        BigDecimal franceCost = franceFreight.add(sum);
        franceFC = priceInfo(franceCost, franceFC, EUR, discount);
        if(franceFC.getFreightCostId() != null){
            freightCostService.updateById(franceFC);
        }else {
            freightCostService.insert(franceFC);
        }
        productsEntity.setFranceFreight(franceFC.getFreightCostId());
        productsEntity.setFranceFC(franceFC);

        // 德国运费
        FreightCostEntity germanyFC = productsEntity.getGermanyFC();
        if (germanyFC == null) {
            germanyFC = new FreightCostEntity();
        }
        BigDecimal germanyFreight = new BigDecimal(freightMap.get("germanyFreight"));
        germanyFC.setFreight(germanyFreight);
        BigDecimal germanyCost = germanyFreight.add(sum);
        germanyFC = priceInfo(germanyCost, germanyFC, EUR, discount);
        if(germanyFC.getFreightCostId() != null){
            freightCostService.updateById(germanyFC);
        }else {
            freightCostService.insert(germanyFC);
        }
        productsEntity.setGermanyFreight(germanyFC.getFreightCostId());
        productsEntity.setGermanyFC(germanyFC);

        //意大利运费
        FreightCostEntity italyFC = productsEntity.getItalyFC();
        if (italyFC == null) {
            italyFC = new FreightCostEntity();
        }
        BigDecimal italyFreight = new BigDecimal(freightMap.get("italyFreight"));
        italyFC.setFreight(italyFreight);
        BigDecimal italyCost = italyFreight.add(sum);
        italyFC = priceInfo(italyCost, italyFC, EUR, discount);
        if(italyFC.getFreightCostId() != null){
            freightCostService.updateById(italyFC);
        }else {
            freightCostService.insert(italyFC);
        }
        productsEntity.setItalyFreight(italyFC.getFreightCostId());
        productsEntity.setItalyFC(italyFC);

        //西班牙运费
        FreightCostEntity spainFC = productsEntity.getSpainFC();
        if (spainFC == null) {
            spainFC = new FreightCostEntity();
        }
        BigDecimal spainFreight = new BigDecimal(freightMap.get("spainFreight"));
        spainFC.setFreight(spainFreight);
        BigDecimal spainCost = spainFreight.add(sum);
        spainFC = priceInfo(spainCost, spainFC, EUR, discount);
        if(spainFC.getFreightCostId() != null){
            freightCostService.updateById(spainFC);
        }else{
            freightCostService.insert(spainFC);
        }
        productsEntity.setSpainFreight(spainFC.getFreightCostId());
        productsEntity.setSpainFC(spainFC);

        // 日本运费
        FreightCostEntity japanFC = productsEntity.getJapanFC();
        if (japanFC == null) {
            japanFC = new FreightCostEntity();
        }
        BigDecimal japanFreight = new BigDecimal(freightMap.get("japanFreight"));
        japanFC.setFreight(japanFreight);
        BigDecimal japanCost = japanFreight.add(sum);
        japanFC = priceInfo(japanCost, japanFC, JPY, discount);
        if(japanFC.getFreightCostId() != null){
            freightCostService.updateById(japanFC);
        }else{
            freightCostService.insert(japanFC);
        }
        productsEntity.setJapanFreight(japanFC.getFreightCostId());
        productsEntity.setJapanFC(japanFC);

        //澳大利亚运费
        FreightCostEntity australiaFC = productsEntity.getAustraliaFC();
        if (australiaFC == null) {
            australiaFC = new FreightCostEntity();
        }
        BigDecimal australiaFreight = new BigDecimal(freightMap.get("australiaFreight"));
        australiaFC.setFreight(australiaFreight);
        BigDecimal australiaCost = australiaFreight.add(sum);
        australiaFC = priceInfo(australiaCost, australiaFC, AUD, discount);
        if(australiaFC.getFreightCostId() != null){
            freightCostService.updateById(australiaFC);
        }else {
            freightCostService.insert(australiaFC);
        }
        productsEntity.setAustraliaFreight(australiaFC.getFreightCostId());
        productsEntity.setAustraliaFC(australiaFC);
        return productsEntity;
    }
    @Override
    public ProductsEntity refresh(ProductsEntity productsEntity) {
        //减去亚马逊15%
        BigDecimal temp = new BigDecimal("0.85");
        //美国汇率
        BigDecimal americanRate = getRate(USD);
        //产品采购价格
        BigDecimal purchasePrice = productsEntity.getPurchasePrice();
        //国内运费
        BigDecimal domesticFreight = productsEntity.getDomesticFreight();
        //产品采购价格和国内运费的和
        BigDecimal sum = purchasePrice.add(domesticFreight);
        //美国运费信息
        FreightCostEntity americanFC = productsEntity.getAmericanFC();
        //把利润率格式转化为0.00%格式的
        DecimalFormat format = new DecimalFormat("0.00%");
        if (americanFC != null) {
            BigDecimal americanOptimization = americanFC.getOptimization();
            BigDecimal americanFinalPrice = americanFC.getFinalPrice();
            //最终
            americanFinalPrice = americanOptimization;
            americanFC.setFinalPrice(americanFinalPrice);
            //美国运费
            BigDecimal americanFreight = americanFC.getFreight();
            //美国成本
            BigDecimal americanCost = sum.add(americanFreight);
            //美国利润
            BigDecimal americanProfit = ((temp.multiply(americanRate).multiply(americanFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(americanCost);
            americanFC.setProfit(americanProfit);
            //美国利润率 divide(BigDecimal)相除  multiply(BigDecimal)相乘
            BigDecimal americanProfitRate = americanProfit.divide(americanRate.multiply(americanFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            //把利润率转换成%格式
            String americanProfitRateString = format.format(americanProfitRate);
            americanFC.setProfitRate(americanProfitRateString);
            productsEntity.setAmericanFC(americanFC);
        }

        FreightCostEntity canadaFC = productsEntity.getCanadaFC();
        if (canadaFC != null) {
            BigDecimal canadaOptimization = canadaFC.getOptimization();
            BigDecimal canadaFinalPrice = canadaFC.getFinalPrice();
            canadaFinalPrice = canadaOptimization;
            canadaFC.setFinalPrice(canadaFinalPrice);
            BigDecimal canadaRate = getRate(CAD);
            BigDecimal canadaFreight = canadaFC.getFreight();
            BigDecimal canadaCost = sum.add(canadaFreight);
            BigDecimal canadaProfit = ((temp.multiply(canadaRate).multiply(canadaFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(canadaCost);
            canadaFC.setProfit(canadaProfit);
            BigDecimal canadaProfitRate = canadaProfit.divide(canadaRate.multiply(canadaFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            String canadaProfitRateString = format.format(canadaProfitRate);
            canadaFC.setProfitRate(canadaProfitRateString);
            productsEntity.setCanadaFC(canadaFC);
        }

        FreightCostEntity mexicoFC = productsEntity.getMexicoFC();
        if (mexicoFC != null) {
            BigDecimal mexicoOptimization = mexicoFC.getOptimization();
            BigDecimal mexicoFinalPrice = mexicoFC.getFinalPrice();
            mexicoFinalPrice = mexicoOptimization;
            mexicoFC.setFinalPrice(mexicoFinalPrice);
            BigDecimal mexicoRate = getRate(MXN);
            BigDecimal mexicoFreight = mexicoFC.getFreight();
            BigDecimal mexicoCost = sum.add(mexicoFreight);
            BigDecimal mexicoProfit = ((temp.multiply(mexicoRate).multiply(mexicoFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(mexicoCost);
            mexicoFC.setProfit(mexicoProfit);
            BigDecimal mexicoProfitRate = mexicoProfit.divide(mexicoRate.multiply(mexicoFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            String mexicoProfitRateString = format.format(mexicoProfitRate);
            mexicoFC.setProfitRate(mexicoProfitRateString);
            productsEntity.setMexicoFC(mexicoFC);
        }

        FreightCostEntity britainFC = productsEntity.getBritainFC();
        if (britainFC != null) {
            BigDecimal britainOptimization = britainFC.getOptimization();
            BigDecimal britainFinalPrice = britainFC.getFinalPrice();
            britainFinalPrice = britainOptimization;
            britainFC.setFinalPrice(britainFinalPrice);
            BigDecimal britainRate = getRate(GBP);
            BigDecimal britainFreight = britainFC.getFreight();
            BigDecimal britainCost = sum.add(britainFreight);
            BigDecimal britainProfit = ((temp.multiply(britainRate).multiply(britainFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(britainCost);
            britainFC.setProfit(britainProfit);
            BigDecimal britainProfitRate = britainProfit.divide(britainRate.multiply(britainFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            String britainProfitRateString = format.format(britainProfitRate);
            britainFC.setProfitRate(britainProfitRateString);
            productsEntity.setBritainFC(britainFC);
        }

        FreightCostEntity franceFC = productsEntity.getFranceFC();
        if (franceFC != null) {
            BigDecimal franceOptimization = franceFC.getOptimization();
            BigDecimal franceFinalPrice = franceFC.getFinalPrice();
            franceFinalPrice = franceOptimization;
            franceFC.setFinalPrice(franceFinalPrice);
            BigDecimal franceRate = getRate(EUR);
            BigDecimal franceFreight = franceFC.getFreight();
            BigDecimal franceCost = sum.add(franceFreight);
            BigDecimal franceProfit = ((temp.multiply(franceRate).multiply(franceFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(franceCost);
            franceFC.setProfit(franceProfit);
            BigDecimal franceProfitRate = franceProfit.divide(franceRate.multiply(franceFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            String franceProfitRateString = format.format(franceProfitRate);
            franceFC.setProfitRate(franceProfitRateString);
            productsEntity.setFranceFC(franceFC);

        }

        FreightCostEntity germanyFC = productsEntity.getGermanyFC();
        if (germanyFC != null) {
            BigDecimal germanyOptimization = germanyFC.getOptimization();
            BigDecimal germanyFinalPrice = germanyFC.getFinalPrice();
            germanyFinalPrice = germanyOptimization;
            germanyFC.setFinalPrice(germanyFinalPrice);
            BigDecimal germanyRate = getRate(EUR);
            BigDecimal germanyFreight = germanyFC.getFreight();
            BigDecimal germanyCost = sum.add(germanyFreight);
            BigDecimal germanyProfit = ((temp.multiply(germanyRate).multiply(germanyFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(germanyCost);
            germanyFC.setProfit(germanyProfit);
            BigDecimal germanyProfitRate = germanyProfit.divide(germanyRate.multiply(germanyFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            String germanyProfitRateString = format.format(germanyProfitRate);
            germanyFC.setProfitRate(germanyProfitRateString);
            productsEntity.setGermanyFC(germanyFC);
        }

        FreightCostEntity italyFC = productsEntity.getItalyFC();
        if (italyFC != null) {
            BigDecimal italyOptimization = italyFC.getOptimization();
            BigDecimal italyFinalPrice = italyFC.getFinalPrice();
            italyFinalPrice = italyOptimization;
            italyFC.setFinalPrice(italyFinalPrice);
            BigDecimal italyRate = getRate(EUR);
            BigDecimal italyFreight = italyFC.getFreight();
            BigDecimal italyCost = sum.add(italyFreight);
            BigDecimal italyProfit = ((temp.multiply(italyRate).multiply(italyFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(italyCost);
            italyFC.setProfit(italyProfit);
            BigDecimal italyProfitRate = italyProfit.divide(italyRate.multiply(italyFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            String italyProfitRateString = format.format(italyProfitRate);
            italyFC.setProfitRate(italyProfitRateString);
            productsEntity.setItalyFC(italyFC);
        }

        FreightCostEntity spainFC = productsEntity.getSpainFC();
        if (spainFC != null) {
            BigDecimal spainOptimization = spainFC.getOptimization();
            BigDecimal spainFinalPrice = spainFC.getFinalPrice();
            spainFinalPrice = spainOptimization;
            spainFC.setFinalPrice(spainFinalPrice);
            BigDecimal spainRate = getRate(EUR);
            BigDecimal spainFreight = spainFC.getFreight();
            BigDecimal spainCost = sum.add(spainFreight);
            BigDecimal spainProfit = ((temp.multiply(spainRate).multiply(spainFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(spainCost);
            spainFC.setProfit(spainProfit);
            BigDecimal spainProfitRate = spainProfit.divide(spainRate.multiply(spainFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            String spainProfitRateString = format.format(spainProfitRate);
            spainFC.setProfitRate(spainProfitRateString);
            productsEntity.setSpainFC(spainFC);
        }

        FreightCostEntity japanFC = productsEntity.getJapanFC();
        if (japanFC != null) {
            BigDecimal japanOptimization = japanFC.getOptimization();
            BigDecimal japanFinalPrice = japanFC.getFinalPrice();
            japanFinalPrice = japanOptimization;
            japanFC.setFinalPrice(japanFinalPrice);
            BigDecimal japanRate = getRate(JPY);
            BigDecimal japanFreight = japanFC.getFreight();
            BigDecimal japanCost = sum.add(japanFreight);
            BigDecimal japanProfit = ((temp.multiply(japanRate).multiply(japanFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(japanCost);
            japanFC.setProfit(japanProfit);
            BigDecimal japanProfitRate = japanProfit.divide(japanRate.multiply(japanFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            String japanProfitRateString = format.format(japanProfitRate);
            japanFC.setProfitRate(japanProfitRateString);
            productsEntity.setJapanFC(japanFC);
        }


        FreightCostEntity australiaFC = productsEntity.getAustraliaFC();
        if (australiaFC != null) {
            BigDecimal australiaOptimization = australiaFC.getOptimization();
            BigDecimal australiaFinalPrice = australiaFC.getFinalPrice();
            australiaFinalPrice = australiaOptimization;
            australiaFC.setFinalPrice(australiaFinalPrice);
            BigDecimal australiaRate = getRate(AUD);
            BigDecimal australiaFreight = australiaFC.getFreight();
            BigDecimal australiaCost = sum.add(australiaFreight);
            BigDecimal australiaProfit = ((temp.multiply(australiaRate).multiply(australiaFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(australiaCost);
            australiaFC.setProfit(australiaProfit);
            BigDecimal australiaProfitRate = australiaProfit.divide(australiaRate.multiply(australiaFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            String australiaProfitRateString = format.format(australiaProfitRate);
            australiaFC.setProfitRate(australiaProfitRateString);
            productsEntity.setAustraliaFC(australiaFC);
        }
        return productsEntity;
    }

    @Override
    public ProductsEntity refreshSave(ProductsEntity productsEntity) {
        //减去亚马逊15%
        BigDecimal temp = new BigDecimal("0.85");
        //美国汇率
        BigDecimal americanRate = getRate(USD);
        //产品采购价格
        BigDecimal purchasePrice = productsEntity.getPurchasePrice();
        //国内运费
        BigDecimal domesticFreight = productsEntity.getDomesticFreight();
        //产品采购价格和国内运费的和
        BigDecimal sum = purchasePrice.add(domesticFreight);
        //美国运费信息
        FreightCostEntity americanFC = productsEntity.getAmericanFC();
        //把利润率格式转化为0.00%格式的
        DecimalFormat format = new DecimalFormat("0.00%");
        if (americanFC != null) {
            BigDecimal americanOptimization = americanFC.getOptimization();
            BigDecimal americanFinalPrice = americanFC.getFinalPrice();
            //最终
            americanFinalPrice = americanOptimization;
            americanFC.setFinalPrice(americanFinalPrice);
            //美国运费
            BigDecimal americanFreight = americanFC.getFreight();
            //美国成本
            BigDecimal americanCost = sum.add(americanFreight);
            //美国利润
            BigDecimal americanProfit = ((temp.multiply(americanRate).multiply(americanFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(americanCost);
            americanFC.setProfit(americanProfit);
            //美国利润率 divide(BigDecimal)相除  multiply(BigDecimal)相乘
            BigDecimal americanProfitRate = americanProfit.divide(americanRate.multiply(americanFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            //把利润率转换成%格式
            String americanProfitRateString = format.format(americanProfitRate);
            americanFC.setProfitRate(americanProfitRateString);
            freightCostService.updateById(americanFC);
        }

        FreightCostEntity canadaFC = productsEntity.getCanadaFC();
        if (canadaFC != null) {
            BigDecimal canadaOptimization = canadaFC.getOptimization();
            BigDecimal canadaFinalPrice = canadaFC.getFinalPrice();
            canadaFinalPrice = canadaOptimization;
            canadaFC.setFinalPrice(canadaFinalPrice);
            BigDecimal canadaRate = getRate(CAD);
            BigDecimal canadaFreight = canadaFC.getFreight();
            BigDecimal canadaCost = sum.add(canadaFreight);
            BigDecimal canadaProfit = ((temp.multiply(canadaRate).multiply(canadaFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(canadaCost);
            canadaFC.setProfit(canadaProfit);
            BigDecimal canadaProfitRate = canadaProfit.divide(canadaRate.multiply(canadaFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            String canadaProfitRateString = format.format(canadaProfitRate);
            canadaFC.setProfitRate(canadaProfitRateString);
            freightCostService.updateById(canadaFC);
        }

        FreightCostEntity mexicoFC = productsEntity.getMexicoFC();
        if (mexicoFC != null) {
            BigDecimal mexicoOptimization = mexicoFC.getOptimization();
            BigDecimal mexicoFinalPrice = mexicoFC.getFinalPrice();
            mexicoFinalPrice = mexicoOptimization;
            mexicoFC.setFinalPrice(mexicoFinalPrice);
            BigDecimal mexicoRate = getRate(MXN);
            BigDecimal mexicoFreight = mexicoFC.getFreight();
            BigDecimal mexicoCost = sum.add(mexicoFreight);
            BigDecimal mexicoProfit = ((temp.multiply(mexicoRate).multiply(mexicoFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(mexicoCost);
            mexicoFC.setProfit(mexicoProfit);
            BigDecimal mexicoProfitRate = mexicoProfit.divide(mexicoRate.multiply(mexicoFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            String mexicoProfitRateString = format.format(mexicoProfitRate);
            mexicoFC.setProfitRate(mexicoProfitRateString);
            freightCostService.updateById(mexicoFC);
        }

        FreightCostEntity britainFC = productsEntity.getBritainFC();
        if (britainFC != null) {
            BigDecimal britainOptimization = britainFC.getOptimization();
            BigDecimal britainFinalPrice = britainFC.getFinalPrice();
            britainFinalPrice = britainOptimization;
            britainFC.setFinalPrice(britainFinalPrice);
            BigDecimal britainRate = getRate(GBP);
            BigDecimal britainFreight = britainFC.getFreight();
            BigDecimal britainCost = sum.add(britainFreight);
            BigDecimal britainProfit = ((temp.multiply(britainRate).multiply(britainFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(britainCost);
            britainFC.setProfit(britainProfit);
            BigDecimal britainProfitRate = britainProfit.divide(britainRate.multiply(britainFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            String britainProfitRateString = format.format(britainProfitRate);
            britainFC.setProfitRate(britainProfitRateString);
            freightCostService.updateById(britainFC);
        }

        FreightCostEntity franceFC = productsEntity.getFranceFC();
        if (franceFC != null) {
            BigDecimal franceOptimization = franceFC.getOptimization();
            BigDecimal franceFinalPrice = franceFC.getFinalPrice();
            franceFinalPrice = franceOptimization;
            franceFC.setFinalPrice(franceFinalPrice);
            BigDecimal franceRate = getRate(EUR);
            BigDecimal franceFreight = franceFC.getFreight();
            BigDecimal franceCost = sum.add(franceFreight);
            BigDecimal franceProfit = ((temp.multiply(franceRate).multiply(franceFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(franceCost);
            franceFC.setProfit(franceProfit);
            BigDecimal franceProfitRate = franceProfit.divide(franceRate.multiply(franceFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            String franceProfitRateString = format.format(franceProfitRate);
            franceFC.setProfitRate(franceProfitRateString);
            freightCostService.updateById(franceFC);
        }

        FreightCostEntity germanyFC = productsEntity.getGermanyFC();
        if (germanyFC != null) {
            BigDecimal germanyOptimization = germanyFC.getOptimization();
            BigDecimal germanyFinalPrice = germanyFC.getFinalPrice();
            germanyFinalPrice = germanyOptimization;
            germanyFC.setFinalPrice(germanyFinalPrice);
            BigDecimal germanyRate = getRate(EUR);
            BigDecimal germanyFreight = germanyFC.getFreight();
            BigDecimal germanyCost = sum.add(germanyFreight);
            BigDecimal germanyProfit = ((temp.multiply(germanyRate).multiply(germanyFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(germanyCost);
            germanyFC.setProfit(germanyProfit);
            BigDecimal germanyProfitRate = germanyProfit.divide(germanyRate.multiply(germanyFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            String germanyProfitRateString = format.format(germanyProfitRate);
            germanyFC.setProfitRate(germanyProfitRateString);
            freightCostService.updateById(germanyFC);
        }

        FreightCostEntity italyFC = productsEntity.getItalyFC();
        if (italyFC != null) {
            BigDecimal italyOptimization = italyFC.getOptimization();
            BigDecimal italyFinalPrice = italyFC.getFinalPrice();
            italyFinalPrice = italyOptimization;
            italyFC.setFinalPrice(italyFinalPrice);
            BigDecimal italyRate = getRate(EUR);
            BigDecimal italyFreight = italyFC.getFreight();
            BigDecimal italyCost = sum.add(italyFreight);
            BigDecimal italyProfit = ((temp.multiply(italyRate).multiply(italyFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(italyCost);
            italyFC.setProfit(italyProfit);
            BigDecimal italyProfitRate = italyProfit.divide(italyRate.multiply(italyFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            String italyProfitRateString = format.format(italyProfitRate);
            italyFC.setProfitRate(italyProfitRateString);
            freightCostService.updateById(italyFC);
        }

        FreightCostEntity spainFC = productsEntity.getSpainFC();
        if (spainFC != null) {
            BigDecimal spainOptimization = spainFC.getOptimization();
            BigDecimal spainFinalPrice = spainFC.getFinalPrice();
            spainFinalPrice = spainOptimization;
            spainFC.setFinalPrice(spainFinalPrice);
            BigDecimal spainRate = getRate(EUR);
            BigDecimal spainFreight = spainFC.getFreight();
            BigDecimal spainCost = sum.add(spainFreight);
            BigDecimal spainProfit = ((temp.multiply(spainRate).multiply(spainFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(spainCost);
            spainFC.setProfit(spainProfit);
            BigDecimal spainProfitRate = spainProfit.divide(spainRate.multiply(spainFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            String spainProfitRateString = format.format(spainProfitRate);
            spainFC.setProfitRate(spainProfitRateString);
            freightCostService.updateById(spainFC);
        }

        FreightCostEntity japanFC = productsEntity.getJapanFC();
        if (japanFC != null) {
            BigDecimal japanOptimization = japanFC.getOptimization();
            BigDecimal japanFinalPrice = japanFC.getFinalPrice();
            japanFinalPrice = japanOptimization;
            japanFC.setFinalPrice(japanFinalPrice);
            BigDecimal japanRate = getRate(JPY);
            BigDecimal japanFreight = japanFC.getFreight();
            BigDecimal japanCost = sum.add(japanFreight);
            BigDecimal japanProfit = ((temp.multiply(japanRate).multiply(japanFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(japanCost);
            japanFC.setProfit(japanProfit);
            BigDecimal japanProfitRate = japanProfit.divide(japanRate.multiply(japanFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            String japanProfitRateString = format.format(japanProfitRate);
            japanFC.setProfitRate(japanProfitRateString);
            freightCostService.updateById(japanFC);
        }


        FreightCostEntity australiaFC = productsEntity.getAustraliaFC();
        if (australiaFC != null) {
            BigDecimal australiaOptimization = australiaFC.getOptimization();
            BigDecimal australiaFinalPrice = australiaFC.getFinalPrice();
            australiaFinalPrice = australiaOptimization;
            australiaFC.setFinalPrice(australiaFinalPrice);
            BigDecimal australiaRate = getRate(AUD);
            BigDecimal australiaFreight = australiaFC.getFreight();
            BigDecimal australiaCost = sum.add(australiaFreight);
            BigDecimal australiaProfit = ((temp.multiply(australiaRate).multiply(australiaFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(australiaCost);
            australiaFC.setProfit(australiaProfit);
            BigDecimal australiaProfitRate = australiaProfit.divide(australiaRate.multiply(australiaFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            String australiaProfitRateString = format.format(australiaProfitRate);
            australiaFC.setProfitRate(australiaProfitRateString);
            freightCostService.updateById(australiaFC);
        }
        return productsEntity;
    }

    //计算售价的信息
    private FreightCostEntity priceInfo(BigDecimal cost, FreightCostEntity freightCostEntity, String rateCode, BigDecimal discount) {
        BigDecimal bigDecimal = new BigDecimal("0.45");
        //计算出售价
        BigDecimal sellingPrice = cost.divide(bigDecimal, 2, BigDecimal.ROUND_HALF_UP);//四舍五入
        //售价乘折扣系数
        BigDecimal sellingPriceX = sellingPrice.multiply(discount);
        freightCostEntity.setPrice(sellingPriceX);
        //获取国家的汇率
        AmazonRateEntity amazonRateEntity = amazonRateService.selectOne(new EntityWrapper<AmazonRateEntity>().eq("rate_code", rateCode));
        BigDecimal rate = amazonRateEntity.getRate();
        BigDecimal multiply = rate.multiply(bigDecimal);
        //外币
        BigDecimal foreignCurrency = cost.divide(multiply, 2, BigDecimal.ROUND_HALF_UP);//四舍五入
        //外币乘折扣系数
        BigDecimal foreignCurrencyX = foreignCurrency.multiply(discount).setScale(2, BigDecimal.ROUND_HALF_UP);
        freightCostEntity.setForeignCurrency(foreignCurrencyX);
        //优化
        String foreignCurrencyString = foreignCurrency.toString();//外币变成字符串
        String[] foreignCurrencyStringArray = foreignCurrencyString.split("\\.");//字符串以"."进行分割
        String optimizationString = foreignCurrencyStringArray[0] + "." + "99";//把后两位变成99
        BigDecimal optimization = new BigDecimal(optimizationString);
        freightCostEntity.setOptimization(optimization);
        return freightCostEntity;
    }
    //获取国家的汇率
    private BigDecimal getRate(String rateCode) {
        //获取国家的汇率
        AmazonRateEntity amazonRateEntity = amazonRateService.selectOne(new EntityWrapper<AmazonRateEntity>().eq("rate_code", rateCode));
        BigDecimal rate = amazonRateEntity.getRate();
        return rate;
    }
}