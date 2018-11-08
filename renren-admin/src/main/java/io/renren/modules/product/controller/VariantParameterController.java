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

import io.renren.modules.product.entity.VariantParameterEntity;
import io.renren.modules.product.service.VariantParameterService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 变体参数
 *
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
@RestController
@RequestMapping("product/variantparameter")
public class VariantParameterController {
    @Autowired
    private VariantParameterService variantParameterService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:variantparameter:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = variantParameterService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{paramsId}")
    @RequiresPermissions("product:variantparameter:info")
    public R info(@PathVariable("paramsId") Long paramsId){
        VariantParameterEntity variantParameter = variantParameterService.selectById(paramsId);

        return R.ok().put("variantParameter", variantParameter);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:variantparameter:save")
    public R save(@RequestBody VariantParameterEntity variantParameter){
        variantParameterService.insert(variantParameter);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:variantparameter:update")
    public R update(@RequestBody VariantParameterEntity variantParameter){
        ValidatorUtils.validateEntity(variantParameter);
        variantParameterService.updateAllColumnById(variantParameter);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:variantparameter:delete")
    public R delete(@RequestBody Long[] paramsIds){
        variantParameterService.deleteBatchIds(Arrays.asList(paramsIds));

        return R.ok();
    }

}
