package io.renren.modules.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.Constant;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.product.dao.ProductsDao;
import io.renren.modules.product.dto.UploadProductDTO;
import io.renren.modules.product.entity.ProductsEntity;
import io.renren.modules.product.entity.VariantsInfoEntity;
import io.renren.modules.product.service.ImageAddressService;
import io.renren.modules.product.service.ProductsService;
import io.renren.modules.product.service.VariantsInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author zjr
 */
@Service("productsService")
public class ProductsServiceImpl extends ServiceImpl<ProductsDao, ProductsEntity> implements ProductsService {

    static String AUDIT_KEY = "001";

    @Autowired
    private VariantsInfoService variantsInfoService;
    @Autowired
    private ImageAddressService imageAddressService;

    /**
     * 我的产品列表
     *
     * @param params url参数
     * @param userId 用户id
     * @return Map<String                               ,                                                               O                               bject>
     * page 产品page
     * proCount 产品数量
     * approvedCount 审核通过
     * numberOfVariants 包含变体的商品
     * variantsCount 变体总数
     * @author zjr
     * @date 2018-11-07 14:54:47
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
                .orderBy(true, "create_time", false)//时间排序
                .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER));
        Page<ProductsEntity> page = this.selectPage(new Query<ProductsEntity>(params).getPage(), wrapper);
        PageUtils pageUtils = new PageUtils(page);
        List<ProductsEntity> list = (List<ProductsEntity>) pageUtils.getList();
        /*for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getMainImageId() != null){
                ImageAddressEntity imageAddressEntity  = imageAddressService.selectById(list.get(i).getMainImageId());
                list.get(i).setMainImageUrl(imageAddressEntity.getImageUrl());
            }
        }*/
        pageUtils.setList(list);
        // 产品数量
        int proCount = this.selectCount(wrapper);
        // 审核通过
        int approvedCount;
        if (AUDIT_KEY.equals(auditNumber)) {
            approvedCount = this.selectCount(wrapper);
        } else {
            //我的产品审核通过
            approvedCount = getMyApprovedCount(category, title, sku, startDate, endDate, shelveNumber, productNumber, userId, 0);
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
     * 所有产品列表
     *
     * @param params url参数
     * @param deptId 公司id
     * @return Map<String,Object>
     * page 产品page
     * proCount 产品数量
     * approvedCount 审核通过
     * numberOfVariants 包含变体的商品
     * variantsCount 变体总数
     * @author zjr
     * @date 2018-11-07 14:54:47
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
        String userId = (String)params.get("userId");
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
                    .eq(StringUtils.isNotBlank(userId),"create_user_id",userId)
                    .eq("is_deleted", 0)
                    .orderBy(true, "create_time", false)//时间排序
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
                    .eq(StringUtils.isNotBlank(userId),"create_user_id",userId)
                    .eq("dept_id", deptId)
                    .eq("is_deleted", 0)
                    .orderBy(true, "create_time", false)//时间排序
                    .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER));
        }
        Page<ProductsEntity> page = this.selectPage(new Query<ProductsEntity>(params).getPage(), wrapper);
        PageUtils pageUtils = new PageUtils(page);
        List<ProductsEntity> list = (List<ProductsEntity>) pageUtils.getList();
        /*for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getMainImageId() != null){
                ImageAddressEntity imageAddressEntity  = imageAddressService.selectById(list.get(i).getMainImageId());
                list.get(i).setMainImageUrl(imageAddressEntity.getImageUrl());
            }
        }*/
        pageUtils.setList(list);
        // 产品数量
        int proCount = this.selectCount(wrapper);
        // 审核通过
        int approvedCount;
        if (AUDIT_KEY.equals(auditNumber)) {
            approvedCount = this.selectCount(wrapper);
        } else {
            //所有产品审核通过
            approvedCount = getAllApprovedCount(category, title, sku, startDate, endDate, shelveNumber, productNumber, deptId, 0);
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
     * 认领产品列表
     *
     * @param params url参数
     * @param deptId 公司id
     * @return Map<String,Object>
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
        String userId = (String)params.get("userId");
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
                    .eq(StringUtils.isNotBlank(userId),"create_user_id",userId)
                    .ne("product_type","007")
                    .eq("is_deleted", 0)
                    .orderBy(true, "create_time", false)//时间排序
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
                    .eq(StringUtils.isNotBlank(userId),"create_user_id",userId)
                    .ne("product_type","007")
                    .eq("dept_id", deptId)
                    .eq("is_deleted", 0)
                    .orderBy(true, "create_time", false)//时间排序
                    .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER));
        }
        Page<ProductsEntity> page = this.selectPage(new Query<ProductsEntity>(params).getPage(), wrapper);
        return new PageUtils(page);
    }
    /**
     * 产品回收站
     *
     * @param params url参数
     * @param userId 用户id
     * @return Map<String                               ,                               Object>
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
        // 时间是：value9
        //String value9 = (String) params.get("value9");
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
                .orderBy(true, "last_operation_time", false)
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
        if (AUDIT_KEY.equals(auditNumber)) {
            approvedCount = this.selectCount(wrapper);
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
        String timeType = "create_time";
        if ("1".equals(isDeleted)) {
            timeType = "last_operation_time";
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
        if ("1".equals(isDeleted)) {
            timeType = "last_operation_time";
        }
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
        wrapper.isNotNull("color_id").or().isNotNull("size_id");
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
        Long[] proIds = new Long[list.size()];
        for (int i = 0; i < list.size(); i++) {
            proIds[i] = list.get(i).getProductId();
        }
        EntityWrapper<VariantsInfoEntity> variantsCountWrapper = new EntityWrapper<>();
        variantsCountWrapper.in("product_id", proIds);
        return variantsInfoService.selectCount(variantsCountWrapper);
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
    public Long getNewProductId(Long userId) {
        ProductsEntity product = new ProductsEntity();
        product.setCreateTime(new Date());
        product.setCreateUserId(userId);
        product.setIsDeleted(0);
        this.insert(product);
        return product.getProductId();
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

        startDate = "";
        endDate = "";

        String timeType = "create_time";
        if ("1".equals(isDel)) {
            timeType = "last_operation_time";
        }

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

        startDate = "";
        endDate = "";

        String timeType = "create_time";
        if ("1".equals(isDel)) {
            timeType = "last_operation_time";
        }

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
        for(int i=0; i < list.size(); i++){
            ProductsEntity product = list.get(i);
            if(product.getCreateUserId().longValue() == userId.longValue()){
                if("001".equals(product.getAuditStatus()) && "001".equals(product.getShelveStatus())){
                        productsList.add(product);
                }else{
                    dto.setCode("error");
                    if((!"001".equals(product.getAuditStatus()))){
                        System.out.println("id==" + product.getProductId());
                        dto.setMsg("有产品没有通过审核");
                    } else if((!"001".equals(product.getShelveStatus()))){
                        dto.setMsg("有产品没有上架");
                    }else{
                        dto.setMsg("有产品没有通过审核/上架");
                    }
                    return dto;
                }
            }
        }
        dto.setProductsList(productsList);
        return dto;
    }
}