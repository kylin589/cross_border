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

import io.renren.modules.product.entity.TemplateCategoryFieldsEntity;
import io.renren.modules.product.service.TemplateCategoryFieldsService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author zjr
 * @email 1981763981@qq.com
 * @date 2018-12-23 23:19:40
 */
@RestController
@RequestMapping("product/templatecategoryfields")
public class TemplateCategoryFieldsController {
    @Autowired
    private TemplateCategoryFieldsService templateCategoryFieldsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:templatecategoryfields:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = templateCategoryFieldsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{fieldId}")
    @RequiresPermissions("product:templatecategoryfields:info")
    public R info(@PathVariable("fieldId") Long fieldId){
        TemplateCategoryFieldsEntity templateCategoryFields = templateCategoryFieldsService.selectById(fieldId);

        return R.ok().put("templateCategoryFields", templateCategoryFields);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:templatecategoryfields:save")
    public R save(@RequestBody TemplateCategoryFieldsEntity templateCategoryFields){
        templateCategoryFieldsService.insert(templateCategoryFields);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:templatecategoryfields:update")
    public R update(@RequestBody TemplateCategoryFieldsEntity templateCategoryFields){
        ValidatorUtils.validateEntity(templateCategoryFields);
        templateCategoryFieldsService.updateAllColumnById(templateCategoryFields);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:templatecategoryfields:delete")
    public R delete(@RequestBody Long[] fieldIds){
        templateCategoryFieldsService.deleteBatchIds(Arrays.asList(fieldIds));

        return R.ok();
    }

}
