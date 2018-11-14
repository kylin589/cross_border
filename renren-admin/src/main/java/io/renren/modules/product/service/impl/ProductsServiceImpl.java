package io.renren.modules.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.Constant;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.product.dao.ProductsDao;
import io.renren.modules.product.entity.ImageAddressEntity;
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
     * @return Map<String       ,               Object>
     * page 产品page
     * proCount 产品数量
     * approvedCount 审核通过
     * numberOfVariants 包含变体的商品
     * variantsCount 变体总数
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @Override
    public Map<String, Object> queryPage(Map<String, Object> params, Long userId) {

        // 分类传过来的是三级分类的id
        // TODO: 2018/11/11 缺少所选择的员工id
        String category = (String) params.get("category");
        String title = (String) params.get("title");
        String sku = (String) params.get("sku");
        // TODO: 2018/11/13 时间处理 
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");
        String auditNumber = (String) params.get("auditNumber");
        String shelveNumber = (String) params.get("shelveNumber");
        String productNumber = (String) params.get("productNumber");

        // TODO: 2018/10/31  判断是不是管理员，根据管理员id查员工的商品
        // TODO: 2018/11/8  1.判断用户角色 或者 判断用户是否有子级
        // TODO: 2018/11/8  2.如果是管理员，那么就把他手底下的员工id查询出来，生成数组或者list列表
        // TODO: 2018/11/8  3.判断是否是管理员，如果不是，就把当前用户id传入list.
        List<Long> ids = new ArrayList<>();
        ids.add(userId);
        /*
        if (){

        }else {
            ids.add(userId);
        }
        */

        EntityWrapper<ProductsEntity> wrapper = new EntityWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(category), "category_three_id", category)
                .like(StringUtils.isNotBlank(title), "product_title", title)
                .like(StringUtils.isNotBlank(sku), "product_sku", sku)
                .ge(StringUtils.isNotBlank(startDate), "create_time", startDate)
                .le(StringUtils.isNotBlank(endDate), "create_time", endDate)
                .eq(StringUtils.isNotBlank(auditNumber), "audit_status", auditNumber)
                .eq(StringUtils.isNotBlank(shelveNumber), "shelve_status", shelveNumber)
                .eq(StringUtils.isNotBlank(productNumber), "product_type", productNumber)
                .in("create_user_id", ids)
                .eq("is_deleted", 0)
                .orderBy(true, "create_time", false)
                .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER));

        Page<ProductsEntity> page = this.selectPage(
                new Query<ProductsEntity>(params).getPage(),
                wrapper
        );

        PageUtils pageUtils = new PageUtils(page);
        List<ProductsEntity> list = (List<ProductsEntity>) pageUtils.getList();
        for (int i = 0; i < list.size(); i++) {
            ImageAddressEntity imageAddressEntity = new ImageAddressEntity();
            imageAddressEntity.setImageId(list.get(i).getMainImageId());
            imageAddressEntity = imageAddressService.selectById(imageAddressEntity);
            list.get(i).setMainImageUrl(imageAddressEntity.getImageUrl());
        }
        pageUtils.setList(list);

        // 产品数量
        int proCount = this.selectCount(wrapper);

        // 审核通过
        int approvedCount;
        if (AUDIT_KEY.equals(auditNumber)) {
            approvedCount = this.selectCount(wrapper);
        } else {
            approvedCount = getApprovedCount(category, title, sku, startDate, endDate, shelveNumber, productNumber, ids, 0);
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
     * 产品回收站
     *
     * @param params url参数
     * @param userId 用户id
     * @return Map<String       ,               Object>
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
        String value9 = (String) params.get("value9");
        System.out.println("value9:" + value9);
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");
        String auditNumber = (String) params.get("auditNumber");
        String shelveNumber = (String) params.get("shelveNumber");
        String productNumber = (String) params.get("productNumber");

        startDate = "";
        endDate = "";
        // TODO: 2018/10/31  判断是不是管理员，根据管理员id查员工的商品
        // TODO: 2018/11/8  1.判断用户角色 或者 判断用户是否有子级
        // TODO: 2018/11/8  2.如果是管理员，那么就把他手底下的员工id查询出来，生成数组或者list列表
        // TODO: 2018/11/8  3.判断是否是管理员，如果不是，就把当前用户id传入list.
        List<Long> ids = new ArrayList<>();
        ids.add(userId);
        /*
        if (){

        }else {
            ids.add(userId);
        }
        */

        EntityWrapper<ProductsEntity> wrapper = new EntityWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(category), "category_three_id", category)
                .like(StringUtils.isNotBlank(title), "product_title", title)
                .like(StringUtils.isNotBlank(sku), "product_sku", sku)
                .ge(StringUtils.isNotBlank(startDate), "create_time", startDate)
                .le(StringUtils.isNotBlank(endDate), "create_time", endDate)
                .eq(StringUtils.isNotBlank(auditNumber), "audit_status", auditNumber)
                .eq(StringUtils.isNotBlank(shelveNumber), "shelve_status", shelveNumber)
                .eq(StringUtils.isNotBlank(productNumber), "product_type", productNumber)
                .in("create_user_id", ids)
                .eq("is_deleted", 1)
                .orderBy(true, "last_operation_time", false)
                .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER));


        Page<ProductsEntity> page = this.selectPage(
                new Query<ProductsEntity>(params).getPage(), wrapper
        );

        PageUtils pageUtils = new PageUtils(page);
        List<ProductsEntity> list = (List<ProductsEntity>) pageUtils.getList();
        for (int i = 0; i < list.size(); i++) {
            ImageAddressEntity imageAddressEntity = new ImageAddressEntity();
            imageAddressEntity.setImageId(list.get(i).getMainImageId());
            imageAddressEntity = imageAddressService.selectById(imageAddressEntity);
            list.get(i).setMainImageUrl(imageAddressEntity.getImageUrl());
        }
        pageUtils.setList(list);

        // 产品数量
        int proCount = this.selectCount(wrapper);

        // 审核通过
        int approvedCount;
        if (AUDIT_KEY.equals(auditNumber)) {
            approvedCount = this.selectCount(wrapper);
        } else {
            approvedCount = getApprovedCount(category, title, sku, startDate, endDate, shelveNumber, productNumber, ids, 1);
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
     * 审核通过总数
     *
     * @param category      分类
     * @param title         标题
     * @param sku
     * @param startDate     开始时间
     * @param endDate       结束时间
     * @param shelveNumber  上架状态
     * @param productNumber 产品存放类型
     * @param ids           用户ids
     * @param isDeleted     是否删除
     * @return 总数
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @Override
    public int getApprovedCount(String category, String title, String sku, String startDate, String endDate, String shelveNumber, String productNumber, List<Long> ids, int isDeleted) {
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
                .in("create_user_id", ids)
                .eq("is_deleted", isDeleted)
                .orderBy(true, timeType, false);
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
     * @methodname: auditCount 审核类型分类统计每个分类的总数
     * @param: [number, del]
     * @return: int
     * @auther: jhy
     * @date: 2018/11/13 23:43
     */
    @Override
    public int auditCount(String number, String del) {
        int auditCount = this.selectCount(new EntityWrapper<ProductsEntity>().eq("audit_status", number).eq("is_deleted", del));
        return auditCount;
    }


    /**
     * @methodname: putawayCount 上架类型分类统计每个分类的总数
     * @param: [number, del]
     * @return: int
     * @auther: jhy
     * @date: 2018/11/13 23:43
     */
    @Override
    public int putawayCount(String number, String del) {
        int putawayCount = this.selectCount(new EntityWrapper<ProductsEntity>().eq("shelve_status", number).eq("is_deleted", del));
        return putawayCount;
    }


    /**
     * @methodname: productCount 产品类型分类统计每个分类的总数
     * @param: [number, del]
     * @return: int
     * @auther: jhy
     * @date: 2018/11/13 23:44
     */
    @Override
    public int productCount(String number, String del) {
        int productCount = this.selectCount(new EntityWrapper<ProductsEntity>().eq("product_type", number).eq("is_deleted", del));
        return productCount;
    }

    /**
     * @methodname: count 一级分类商品总数
     * @param: [id, del]
     * @return: int
     * @auther: jhy
     * @date: 2018/11/13 23:42
     */
    @Override
    public int count(Long id, String del) {
        int c = this.selectCount(new EntityWrapper<ProductsEntity>().eq("category_one_id", id).eq("is_deleted", del));
        return c;
    }

    /**
     * @methodname: counts 父级查子类产品总和
     * @param: [id, del] 父级id，删除
     * @return: int
     * @auther: jhy
     * @date: 2018/11/13 23:40
     */
    @Override
    public int counts(Long id, String del) {
        int c = this.selectCount(new EntityWrapper<ProductsEntity>().eq("category_two_id", id).or().eq("category_three_id", id).eq("is_deleted", del));
        return c;
    }

    /**
     * 获取商品数量
     *
     * @param params url 参数
     * @param userId 用户id
     * @param isDel  是否删除
     * @return 数量
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @Override
    public int getTotalCount(Map<String, Object> params, Long userId, String isDel) {
        // 分类传过来的是三级分类的id

        String category = (String) params.get("category");
        String title = (String) params.get("title");
        String sku = (String) params.get("sku");
        // 时间是：value9
        String value9 = (String) params.get("value9");
        System.out.println("value9:" + value9);
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");
        String auditNumber = (String) params.get("auditNumber");
        String shelveNumber = (String) params.get("shelveNumber");
        String productNumber = (String) params.get("productNumber");

        startDate = "";
        endDate = "";
        // TODO: 2018/10/31  判断是不是管理员，根据管理员id查员工的商品
        // TODO: 2018/11/8  1.判断用户角色 或者 判断用户是否有子级
        // TODO: 2018/11/8  2.如果是管理员，那么就把他手底下的员工id查询出来，生成数组或者list列表
        // TODO: 2018/11/8  3.判断是否是管理员，如果不是，就把当前用户id传入list.
        List<Long> ids = new ArrayList<>();
        ids.add(userId);
        /*
        if (){

        }else {
            ids.add(userId);
        }
        */
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
                .in("create_user_id", ids)
                .eq("is_deleted", isDel)
                .orderBy(true, timeType, false)
                .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER));

        // 产品数量
        int proCount = this.selectCount(wrapper);
        return proCount;
    }
}
