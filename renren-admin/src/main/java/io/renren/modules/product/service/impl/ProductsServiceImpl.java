package io.renren.modules.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.Constant;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.product.dao.ProductsDao;
import io.renren.modules.product.entity.ProductsEntity;
import io.renren.modules.product.entity.VariantsInfoEntity;
import io.renren.modules.product.service.ProductsService;
import io.renren.modules.product.service.VariantsInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zjr
 */
@Service("productsService")
public class ProductsServiceImpl extends ServiceImpl<ProductsDao, ProductsEntity> implements ProductsService {

    static String AUDIT_KEY = "001";

    @Autowired
    private VariantsInfoService variantsInfoService;

    @Override
    public Map<String, Object> queryPage(Map<String, Object> params, Long userId) {

        // 分类传过来的是三级分类的id
        String category = (String) params.get("category");
        String title = (String) params.get("title");
        String sku = (String) params.get("sku");
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
        wrapper.eq("category_three_id", category)
                .like(StringUtils.isNotBlank(title), "product_title", title)
                .like(StringUtils.isNotBlank(sku), "product_sku", sku)
                .ge("create_time", startDate)
                .le("create_time", endDate)
                .eq("audit_status", auditNumber)
                .eq("shelve_status", shelveNumber)
                .eq("product_type", productNumber)
                .in("create_user_id", ids)
                .eq("is_deleted", 0)
                .orderBy(true, "create_time", false)
                .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER));

        Page<ProductsEntity> page = this.selectPage(
                new Query<ProductsEntity>(params).getPage(),
                wrapper
        );

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
        map.put("page", new PageUtils(page));
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
        wrapper.eq("category_three_id", category)
                .like(StringUtils.isNotBlank(title), "product_title", title)
                .like(StringUtils.isNotBlank(sku), "product_sku", sku)
                .ge("create_time", startDate)
                .le("create_time", endDate)
                .eq("audit_status", auditNumber)
                .eq("shelve_status", shelveNumber)
                .eq("product_type", productNumber)
                .in("create_user_id", ids)
                .eq("is_deleted", 1)
                .orderBy(true, "last_operation_time", false)
                .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER));


        Page<ProductsEntity> page = this.selectPage(
                new Query<ProductsEntity>(params).getPage(), wrapper
        );

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
        map.put("page", new PageUtils(page));
        map.put("proCount", proCount);
        map.put("approvedCount", approvedCount);
        map.put("numberOfVariants", numberOfVariants);
        map.put("variantsCount", variantsCount);
        return map;
    }

    @Override
    public int getApprovedCount(String category, String title, String sku, String startDate, String endDate, String shelveNumber, String productNumber, List<Long> ids, int isDeleted) {
        EntityWrapper<ProductsEntity> approvedCountWrapper = new EntityWrapper<>();
        approvedCountWrapper.eq("category_three_id", category)
                .like(StringUtils.isNotBlank(title), "product_title", title)
                .like(StringUtils.isNotBlank(sku), "product_sku", sku)
                .ge("create_time", startDate)
                .le("create_time", endDate)
                .eq("audit_status", "001")
                .eq("shelve_status", shelveNumber)
                .eq("product_type", productNumber)
                .in("create_user_id", ids)
                .eq("is_deleted", isDeleted)
                .orderBy(true, "create_time", false);
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

}
