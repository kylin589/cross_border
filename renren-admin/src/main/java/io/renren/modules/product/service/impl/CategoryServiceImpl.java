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
    public String queryParentByChildId(Long categoryId) {
        //设置一个数组存放查询出的id
        Long[]arr= new Long[3];
        //把传过来的子级id存放到数组
        arr[0] = categoryId;
        int j = 1;
        for (int i = 0; i < arr.length - 1; i++) {
            if (categoryId != 0) {
                CategoryEntity productCategoryEntity = this.selectById(categoryId);
                categoryId = productCategoryEntity.getParentId();
                arr[j] = categoryId;
                j++;
            }
        }
        String idString = "";
        //多数组进行反转，以逗号进行字符串拼接
        for (int i = arr.length - 1; i >= 0; i--) {
            //遍历数组里不为null和0的
            if (!(arr[i] == null || arr[i] == 0)) {
                if (i == 0) {
                    idString += arr[i];
                } else {
                    idString += arr[i];
                    idString += ",";
                }
            }
        }
        return idString;
    }

}
