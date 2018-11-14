package io.renren.modules.product.controller;

import java.util.Arrays;
import java.util.Map;

import io.renren.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.product.entity.AmazonCategoryEntity;
import io.renren.modules.product.service.AmazonCategoryService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 亚马逊分类表
 *
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
@RestController
@RequestMapping("product/amazoncategory")
public class AmazonCategoryController {
    @Autowired
    private AmazonCategoryService amazonCategoryService;

    /**
     * 列表
     * @param params url 参数
     * @return R.ok()
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:amazoncategory:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = amazonCategoryService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     * @param amazonCategoryId id
     * @return R.ok()
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @RequestMapping("/info/{amazonCategoryId}")
    @RequiresPermissions("product:amazoncategory:info")
    public R info(@PathVariable("amazonCategoryId") Long amazonCategoryId){
        AmazonCategoryEntity amazonCategory = amazonCategoryService.selectById(amazonCategoryId);

        return R.ok().put("amazonCategory", amazonCategory);
    }

    /**
     * 保存
     * @param amazonCategory 分类实体
     * @return R.ok()
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:amazoncategory:save")
    public R save(@RequestBody AmazonCategoryEntity amazonCategory){
        amazonCategoryService.insert(amazonCategory);

        return R.ok();
    }

    /**
     * 修改
     * @param amazonCategory 分类实体
     * @return R.ok()
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:amazoncategory:update")
    public R update(@RequestBody AmazonCategoryEntity amazonCategory){
        ValidatorUtils.validateEntity(amazonCategory);
        amazonCategoryService.updateAllColumnById(amazonCategory);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     * @param amazonCategoryIds id数组
     * @return R.ok()
     * @return zjr
     * @date 2018-11-07 14:54:47
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:amazoncategory:delete")
    public R delete(@RequestBody Long[] amazonCategoryIds){
        amazonCategoryService.deleteBatchIds(Arrays.asList(amazonCategoryIds));

        return R.ok();
    }

}
