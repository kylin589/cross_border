package io.renren.modules.amazon.controller;

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

import io.renren.modules.amazon.entity.AmazonGrantEntity;
import io.renren.modules.amazon.service.AmazonGrantService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 * Amazon授权
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-11-27 10:43:39
 */
@RestController
@RequestMapping("amazon/amazongrant")
public class AmazonGrantController {
    @Autowired
    private AmazonGrantService amazonGrantService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("amazon:amazongrant:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = amazonGrantService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{grantId}")
    @RequiresPermissions("amazon:amazongrant:info")
    public R info(@PathVariable("grantId") Long grantId){
        AmazonGrantEntity amazonGrant = amazonGrantService.selectById(grantId);

        return R.ok().put("amazonGrant", amazonGrant);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("amazon:amazongrant:save")
    public R save(@RequestBody AmazonGrantEntity amazonGrant){
        amazonGrantService.insert(amazonGrant);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("amazon:amazongrant:update")
    public R update(@RequestBody AmazonGrantEntity amazonGrant){
        ValidatorUtils.validateEntity(amazonGrant);
        amazonGrantService.updateAllColumnById(amazonGrant);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("amazon:amazongrant:delete")
    public R delete(@RequestBody Long[] grantIds){
        amazonGrantService.deleteBatchIds(Arrays.asList(grantIds));

        return R.ok();
    }

}
