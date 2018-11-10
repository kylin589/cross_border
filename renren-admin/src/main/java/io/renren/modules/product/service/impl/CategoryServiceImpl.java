package io.renren.modules.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.product.dao.CategoryDao;
import io.renren.modules.product.entity.CategoryEntity;
import io.renren.modules.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<CategoryEntity> page = this.selectPage(
                new Query<CategoryEntity>(params).getPage(),
                new EntityWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 一级分类
     */
    public List<CategoryEntity> parent() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("parent_id", 0);
        List<CategoryEntity> parentList = this.selectByMap(map);
        return parentList;
    }

    /**
     * 父类查子类
     */

    public List<CategoryEntity> parentId(Long id) {

        List<CategoryEntity> parentLists = this.selectList(new EntityWrapper<CategoryEntity>().eq("parent_id", id));
        return parentLists;
    }

    /**
     * 子类id查出父类id
     */
    public String parentIds(Long id) {
        CategoryEntity productCategoryEntity = this.selectById(id);
        Long p1 = productCategoryEntity.getParentId();
        CategoryEntity productCategoryEntity2 = this.selectById(p1);
        Long p = productCategoryEntity2.getParentId();
        String a = p + "," + p1 + "," + id;
        return a;
    }

}
