package io.renren.modules.product.controller;

import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.product.dto.TemplateFieldValueDto;
import io.renren.modules.product.entity.TemplateCategoryFieldsEntity;
import io.renren.modules.product.entity.TemplateFieldValueEntity;
import io.renren.modules.product.service.TemplateCategoryFieldsService;
import io.renren.modules.product.service.TemplateFieldValueService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.product.entity.TemplateEntity;
import io.renren.modules.product.service.TemplateService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;


/**
 * 上传模板
 *
 * @author zjr
 * @email 1981763981@qq.com
 * @date 2018-12-23 23:19:41
 */
@RestController
@RequestMapping("product/template")
public class TemplateController {
    @Autowired
    private TemplateService templateService;

    @Autowired
    private TemplateCategoryFieldsService templateCategoryFieldsService;

    @Autowired
    private TemplateFieldValueService templateFieldValueService;
    /**
     * 列表
     */
    @RequestMapping("/list")
    /* @RequiresPermissions("product:template:list")*/
    public R list() {
        EntityWrapper<TemplateEntity> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("*");
        List<TemplateEntity> templates = templateService.selectList(wrapper);
        return R.ok().put("templates", templates);
    }

    /**
     * 根据分类模板和国家编码获取所有分类字段和可选值
     */
    @RequestMapping("/getOptionalValues")
    public R getOptionalValues(String templateId, String countryCode) {
        List<TemplateCategoryFieldsEntity> templateCategoryFieldsEntities = templateService.getOptionalValues(templateId, countryCode);
        return R.ok().put("data", templateCategoryFieldsEntities);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{templateId}")
    @RequiresPermissions("product:template:info")
    public R info(@PathVariable("templateId") Long templateId) {
        TemplateEntity template = templateService.selectById(templateId);

        return R.ok().put("template", template);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:template:save")
    public R save(@RequestBody TemplateEntity template) {
        templateService.insert(template);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:template:update")
    public R update(@RequestBody TemplateEntity template) {
        //ValidatorUtils.validateEntity((template);
        templateService.updateAllColumnById(template);//全部更新

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:template:delete")
    public R delete(@RequestBody Long[] templateIds) {
        templateService.deleteBatchIds(Arrays.asList(templateIds));

        return R.ok();
    }

}
