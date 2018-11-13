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
            imageAddressService.selectById(imageAddressEntity);
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

    @Override
    public int getApprovedCount(String category, String title, String sku, String startDate, String endDate, String shelveNumber, String productNumber, List<Long> ids, int isDeleted) {
        String timeType ="create_time";
        if ("1".equals(isDeleted)){
            timeType  = "last_operation_time";
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

    @Override
    public int getNumberOfVariants(EntityWrapper<ProductsEntity> wrapper) {
        wrapper.isNotNull("color_id").or().isNotNull("size_id");
        return this.selectCount(wrapper);
    }

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
     * 审核类型分类统计
     */
    @Override
    public int auditCount(String number, String del) {
        int auditCount = this.selectCount(new EntityWrapper<ProductsEntity>().eq("audit_status", number).eq("is_deleted", del));
        return auditCount;
    }


    /**
     * 上架类型分类统计
     */
    @Override
    public int putawayCount(String number, String del) {
        int putawayCount = this.selectCount(new EntityWrapper<ProductsEntity>().eq("shelve_status", number).eq("is_deleted", del));
        return putawayCount;
    }


    /**
     * 产品类型分类统计
     */
    @Override
    public int productCount(String number, String del) {
        int productCount = this.selectCount(new EntityWrapper<ProductsEntity>().eq("product_type", number).eq("is_deleted", del));
        return productCount;
    }

    /**
     * 一级分类商品总数
     *
     * @param id
     * @return
     */
    @Override
    public int count(Long id, String del) {
        int c = this.selectCount(new EntityWrapper<ProductsEntity>().eq("category_one_id", id).eq("is_deleted", del));
        return c;
    }

    /**
     * 父类查子类
     */
    @Override
    public int counts(Long id, String del) {
        int c = this.selectCount(new EntityWrapper<ProductsEntity>().eq("category_two_id", id).or().eq("category_three_id", id).eq("is_deleted", del));
        return c;
    }

    @Override
    public int getTotalCount(Map<String, Object> params, Long userId,String isDel) {
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
        String timeType ="create_time";
        if ("1".equals(isDel)){
            timeType  = "last_operation_time";
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
