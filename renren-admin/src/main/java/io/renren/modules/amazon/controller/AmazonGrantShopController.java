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

import io.renren.modules.amazon.entity.AmazonGrantShopEntity;
import io.renren.modules.amazon.service.AmazonGrantShopService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 * Amazon授权店铺列表
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-11-27 11:00:50
 */
@RestController
@RequestMapping("amazon/amazongrantshop")
public class AmazonGrantShopController {
    @Autowired
    private AmazonGrantShopService amazonGrantShopService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("amazon:amazongrantshop:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = amazonGrantShopService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{grantShopId}")
    @RequiresPermissions("amazon:amazongrantshop:info")
    public R info(@PathVariable("grantShopId") Long grantShopId){
        AmazonGrantShopEntity amazonGrantShop = amazonGrantShopService.selectById(grantShopId);

        return R.ok().put("amazonGrantShop", amazonGrantShop);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("amazon:amazongrantshop:save")
    public R save(@RequestBody AmazonGrantShopEntity amazonGrantShop){
        amazonGrantShopService.insert(amazonGrantShop);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("amazon:amazongrantshop:update")
    public R update(@RequestBody AmazonGrantShopEntity amazonGrantShop){
        ValidatorUtils.validateEntity(amazonGrantShop);
        amazonGrantShopService.updateAllColumnById(amazonGrantShop);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("amazon:amazongrantshop:delete")
    public R delete(@RequestBody Long[] grantShopIds){
        amazonGrantShopService.deleteBatchIds(Arrays.asList(grantShopIds));

        return R.ok();
    }

}
