package io.renren.modules.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.product.dao.ProductsDao;
import io.renren.modules.product.entity.ProductsEntity;
import io.renren.modules.product.service.ProductsService;


@Service("productsService")
public class ProductsServiceImpl extends ServiceImpl<ProductsDao, ProductsEntity> implements ProductsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ProductsEntity> page = this.selectPage(
                new Query<ProductsEntity>(params).getPage(),
                new EntityWrapper<ProductsEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 审核类型分类总和
     */
    public int auditCount(String number) {
        int auditCount = this.selectCount(new EntityWrapper<ProductsEntity>().eq("audit_status", number).eq("is_deleted", 0));
        return auditCount;
    }


    /**
     * 上架类型分类总和
     */
    public int putawayCount(String number) {
        int putawayCount = this.selectCount(new EntityWrapper<ProductsEntity>().eq("shelve_status", number).eq("is_deleted", 0));
        return putawayCount;
    }


    /**
     * 产品类型分类总和
     */
    public int productCount(String number) {
        int productCount = this.selectCount(new EntityWrapper<ProductsEntity>().eq("product_type", number).eq("is_deleted", 0));
        return productCount;
    }

    /**
     * 一级分类商品总数
     *
     * @param id
     * @return
     */
    public int count(Long id) {
        int c = this.selectCount(new EntityWrapper<ProductsEntity>().eq("category_one_id", id).eq("is_deleted", 0));
        return c;
    }

    /**
     * 父类查子类
     */
    public int counts(Long id) {
        int c = this.selectCount(new EntityWrapper<ProductsEntity>().eq("category_two_id", id).or().eq("category_three_id", id).eq("is_deleted", 0));
        return c;
    }

}
