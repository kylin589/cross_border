package io.renren.modules.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.product.service.ProductsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.product.entity.CategoryEntity;
import io.renren.modules.product.service.CategoryService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;


/**
 * 内部分类表
 *
 * @author jhy
 * @email 617493711@qq.com
 * @date 2018-11-08 09:59:28
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductsService productsService;

    /**
     * 列表
     *
     * @param params url参数
     * @return R.ok()
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:category:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = categoryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     *
     * @param categoryId id
     * @return R.ok()
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @RequestMapping("/info/{categoryId}")
    @RequiresPermissions("product:category:info")
    public R info(@PathVariable("categoryId") Long categoryId) {
        CategoryEntity category = categoryService.selectById(categoryId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     *
     * @param category 实体
     * @return R.ok()
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:category:save")
    public R save(@RequestBody CategoryEntity category) {
        categoryService.insert(category);

        return R.ok();
    }

    /**
     * 修改
     *
     * @param category 实体
     * @return R.ok()
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category) {
        ValidatorUtils.validateEntity(category);
        //全部更新
        categoryService.updateAllColumnById(category);

        return R.ok();
    }

    /**
     * 删除
     *
     * @param categoryIds id数组
     * @return R.ok()
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:category:delete")
    public R delete(@RequestBody Long[] categoryIds) {
        categoryService.deleteBatchIds(Arrays.asList(categoryIds));

        return R.ok();
    }

    /**
     * @methodname: parent:一级分类
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/6 10:29
     */
    @RequestMapping("/querycategoryone")
    public R queryCategoryOne(@RequestParam(value = "del", required = false, defaultValue = "0") String del) {
        List<CategoryEntity> parentList = categoryService.queryCategoryOne();
        for (CategoryEntity categoryEntity : parentList) {
            Long id = categoryEntity.getCategoryId();
            //根据分类id查出一级分类产品总数
            int c = productsService.count(id, del);
            categoryEntity.setCount(c);
            categoryEntity.setIfNext("true");
        }
        return R.ok().put("categoryOneList", parentList);
    }

    /**
     * @methodname: parentId:根据父id查询
     * @param: categoryId 父级id
     * @return: io.renren.common.utils.R 分类的信息
     * @auther: jhy
     * @date: 2018/11/6 10:28
     */
    @RequestMapping("/querycategorybyparentid")
    public R queryCategoryByParentId(Long categoryId, @RequestParam(value = "del", required = false, defaultValue = "0") String del) {
        //根据分类的父级id查出分类的信息
        List<CategoryEntity> parentLists = categoryService.queryCategoryByParentId(categoryId);
        for (CategoryEntity categoryEntity : parentLists) {
            Long id = categoryEntity.getCategoryId();
            int temp = categoryService.selectCount(new EntityWrapper<CategoryEntity>().eq("parent_id", categoryEntity.getCategoryId()));
            System.out.println(temp);
            if (temp==0){
                categoryEntity.setIfNext("false");
            }else{
                categoryEntity.setIfNext("true");
            }
            //父类分类id查子类的产品总和
            int count = productsService.counts(id, del);
            categoryEntity.setCount(count);
        }
        return R.ok().put("categoryList", parentLists);
    }

    /**
     * @methodname: queryParentByChildId 根据子id查出父类的id
     * @param: [categoryId] 三级分类的id
     * @return: java.lang.String  字符串的形式返回一级二级三级id，以逗号进行的拼接
     * @auther: jhy
     * @date: 2018/11/6 10:27
     */
    @RequestMapping("/queryparentbychildid")
    public String queryParentByChildId(Long categoryId) {
        String ids = categoryService.queryParentByChildId(categoryId);
        return ids;
    }

}
