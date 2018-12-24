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

import io.renren.modules.product.entity.TemplateFieldValueEntity;
import io.renren.modules.product.service.TemplateFieldValueService;
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
@RequestMapping("product/templatefieldvalue")
public class TemplateFieldValueController {
    @Autowired
    private TemplateFieldValueService templateFieldValueService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:templatefieldvalue:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = templateFieldValueService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{valueId}")
    @RequiresPermissions("product:templatefieldvalue:info")
    public R info(@PathVariable("valueId") Long valueId){
        TemplateFieldValueEntity templateFieldValue = templateFieldValueService.selectById(valueId);

        return R.ok().put("templateFieldValue", templateFieldValue);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:templatefieldvalue:save")
    public R save(@RequestBody TemplateFieldValueEntity templateFieldValue){
        templateFieldValueService.insert(templateFieldValue);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:templatefieldvalue:update")
    public R update(@RequestBody TemplateFieldValueEntity templateFieldValue){
        ValidatorUtils.validateEntity(templateFieldValue);
        templateFieldValueService.updateAllColumnById(templateFieldValue);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:templatefieldvalue:delete")
    public R delete(@RequestBody Long[] valueIds){
        templateFieldValueService.deleteBatchIds(Arrays.asList(valueIds));

        return R.ok();
    }

}
