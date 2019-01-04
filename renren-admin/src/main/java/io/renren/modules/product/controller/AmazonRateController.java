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

import io.renren.modules.product.entity.AmazonRateEntity;
import io.renren.modules.product.service.AmazonRateService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 汇率表
 *
 * @author jhy
 * @email 617493711@qq.com
 * @date 2018-12-07 14:47:29
 */
@RestController
@RequestMapping("product/amazonrate")
public class AmazonRateController {
    @Autowired
    private AmazonRateService amazonRateService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:amazonrate:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = amazonRateService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{rateId}")
    @RequiresPermissions("product:amazonrate:info")
    public R info(@PathVariable("rateId") Long rateId){
        AmazonRateEntity amazonRate = amazonRateService.selectById(rateId);

        return R.ok().put("amazonRate", amazonRate);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:amazonrate:save")
    public R save(@RequestBody AmazonRateEntity amazonRate){
        amazonRateService.insert(amazonRate);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:amazonrate:update")
    public R update(@RequestBody AmazonRateEntity amazonRate){
        //ValidatorUtils.validateEntity((amazonRate);
        amazonRateService.updateAllColumnById(amazonRate);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:amazonrate:delete")
    public R delete(@RequestBody Long[] rateIds){
        amazonRateService.deleteBatchIds(Arrays.asList(rateIds));

        return R.ok();
    }

}
