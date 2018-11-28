package io.renren.modules.product.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.product.entity.CategoryEntity;
import io.renren.modules.product.entity.ProductsEntity;

import java.util.List;
import java.util.Map;

/**
 * 内部分类表
 *
 * @author jhy
 * @email 617493711@qq.com
 * @date 2018-11-08 09:59:28
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //一级分类
    List<CategoryEntity> queryCategoryOne();
    //父级分类分类的id查出子级分类
    List<CategoryEntity> queryCategoryByParentId(Long id);
    //根据三级id查出一级和二级id
    String queryParentByChildId(Long id);
    //根据三级id查出一级和二级id和注入商品分类
    String queryParentByChildIdAndCategory(Long id,ProductsEntity productsEntity);


}

