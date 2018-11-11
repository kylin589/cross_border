package io.renren.modules.product.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.product.entity.CategoryEntity;

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

    List<CategoryEntity> queryCategoryOne();

    List<CategoryEntity> queryCategoryByParentId(Long id);

    String queryParentByChildId(Long id);


}

