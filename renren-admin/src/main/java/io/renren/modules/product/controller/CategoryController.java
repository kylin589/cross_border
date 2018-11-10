package io.renren.modules.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:category:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = categoryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{categoryId}")
    @RequiresPermissions("product:category:info")
    public R info(@PathVariable("categoryId") Long categoryId) {
        CategoryEntity category = categoryService.selectById(categoryId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:category:save")
    public R save(@RequestBody CategoryEntity category) {
        categoryService.insert(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category) {
        ValidatorUtils.validateEntity(category);
        categoryService.updateAllColumnById(category);//全部更新

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:category:delete")
    public R delete(@RequestBody Long[] categoryIds) {
        categoryService.deleteBatchIds(Arrays.asList(categoryIds));

        return R.ok();
    }

    /**
     * @methodname: parent:一级分类
     * @param: []
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/6 10:29
     */
    @RequestMapping("/parentlist")
    public R parentList() {
        List<CategoryEntity> parentList = categoryService.parent();
        for (CategoryEntity categoryEntity : parentList) {
            Long id = categoryEntity.getCategoryId();
            int c = productsService.count(id);

            categoryEntity.setCount(c);
        }
        return R.ok().put("parentList", parentList);
    }

    /**
     * @methodname: parentId:父id查询
     * @param: [categoryId]
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/6 10:28
     */
    @RequestMapping("/childlist")
    public R parentId(Long categoryId) {
        List<CategoryEntity> parentLists = categoryService.parentId(categoryId);
        for (CategoryEntity categoryEntity : parentLists) {
            Long id = categoryEntity.getCategoryId();
            int count = productsService.counts(id);
            categoryEntity.setCount(count);
        }
        return R.ok().put("parentLists", parentLists);
    }

    /**
     * @methodname: parentids:根据子id查出父类的id
     * @param: [categoryId]
     * @return: java.lang.String
     * @auther: jhy
     * @date: 2018/11/6 10:27
     */
    @RequestMapping("/parentids")
    public String parentids(Long categoryId) {
        String ids = categoryService.parentIds(categoryId);
        return ids;
    }

}
