package io.renren.modules.product.controller;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.product.entity.IntroductionEntity;
import io.renren.modules.product.service.IntroductionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * 国家介绍
 *
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
@RestController
@RequestMapping("product/introduction")
public class IntroductionController {
    @Autowired
    private IntroductionService introductionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:introduction:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = introductionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{introductionId}")
    @RequiresPermissions("product:introduction:info")
    public R info(@PathVariable("introductionId") Long introductionId){
        IntroductionEntity introduction = introductionService.selectById(introductionId);

        return R.ok().put("introduction", introduction);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:introduction:save")
    public R save(@RequestBody IntroductionEntity introduction){
        introductionService.insert(introduction);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:introduction:update")
    public R update(@RequestBody IntroductionEntity introduction){
        ValidatorUtils.validateEntity(introduction);
        introductionService.updateAllColumnById(introduction);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:introduction:delete")
    public R delete(@RequestBody Long[] introductionIds){
        introductionService.deleteBatchIds(Arrays.asList(introductionIds));

        return R.ok();
    }

}
