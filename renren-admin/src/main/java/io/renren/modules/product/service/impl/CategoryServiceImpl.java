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
     * @methodname: queryCategoryOne 一级分类，条件父级id为空
     * @param: 无
     * @return: java.util.List<io.renren.modules.product.entity.CategoryEntity>
     * @auther: jhy
     * @date: 2018/11/13 23:31
     */
    @Override
    public List<CategoryEntity> queryCategoryOne() {
        Map<String, Object> map = new HashMap<>();
        map.put("parent_id", 0);
        List<CategoryEntity> parentList = this.selectByMap(map);
        return parentList;
    }

    /**
     * @methodname: queryCategoryByParentId 根据父级id查询出子类的分类信息
     * @param: [id] 父级id
     * @return: java.util.List<io.renren.modules.product.entity.CategoryEntity>
     * @auther: jhy
     * @date: 2018/11/13 23:32
     */
    @Override
    public List<CategoryEntity> queryCategoryByParentId(Long id) {
        List<CategoryEntity> parentLists = this.selectList(new EntityWrapper<CategoryEntity>().eq("parent_id", id));
        return parentLists;
    }

    /**
     * @methodname: queryParentByChildId 三级id查出一级id和二级id
     * @param: [id] 三级id
     * @return: java.lang.String
     * @auther: jhy
     * @date: 2018/11/13 23:33
     */
    @Override
    public String queryParentByChildId(Long id) {
        CategoryEntity productCategoryEntity = this.selectById(id);
        Long twoId = productCategoryEntity.getParentId();
        CategoryEntity productCategoryEntity2 = this.selectById(twoId);
        Long oneId = productCategoryEntity2.getParentId();
        //把一级二级三级id以逗号进行拼接
        String ids = oneId + "," + twoId + "," + id;
        return ids;
    }

}
