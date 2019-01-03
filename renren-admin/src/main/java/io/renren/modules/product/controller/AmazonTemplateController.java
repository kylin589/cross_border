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

import io.renren.modules.product.entity.AmazonTemplateEntity;
import io.renren.modules.product.service.AmazonTemplateService;
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
@RequestMapping("product/amazontemplate")
public class AmazonTemplateController {
    @Autowired
    private AmazonTemplateService amazonTemplateService;

    /**
     * 列表
     * @param params url参数
     * @return R.ok()
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:amazontemplate:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = amazonTemplateService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     * @param amazonTemplateId id
     * @return R.ok()
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @RequestMapping("/info/{amazonTemplateId}")
    @RequiresPermissions("product:amazontemplate:info")
    public R info(@PathVariable("amazonTemplateId") Long amazonTemplateId){
        AmazonTemplateEntity amazonTemplate = amazonTemplateService.selectById(amazonTemplateId);

        return R.ok().put("amazonTemplate", amazonTemplate);
    }

    /**
     * 保存
     * @param amazonTemplate 实体类
     * @return R.ok()
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:amazontemplate:save")
    public R save(@RequestBody AmazonTemplateEntity amazonTemplate){
        amazonTemplateService.insert(amazonTemplate);

        return R.ok();
    }

    /**
     * 修改
     * @param amazonTemplate 实体
     * @return R.ok()
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:amazontemplate:update")
    public R update(@RequestBody AmazonTemplateEntity amazonTemplate){
        //ValidatorUtils.validateEntity((amazonTemplate);
        amazonTemplateService.updateAllColumnById(amazonTemplate);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     * @param amazonTemplateIds id数组
     * @return R.ok()
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:amazontemplate:delete")
    public R delete(@RequestBody Long[] amazonTemplateIds){
        amazonTemplateService.deleteBatchIds(Arrays.asList(amazonTemplateIds));

        return R.ok();
    }

}
