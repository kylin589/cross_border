package io.renren.modules.amazon.controller;

import java.util.Arrays;
import java.util.Map;

import io.renren.common.utils.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.amazon.entity.AmazonMarketplaceEntity;
import io.renren.modules.amazon.service.AmazonMarketplaceService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-19 11:02:06
 */
@RestController
@RequestMapping("amazon/amazonmarketplace")
public class AmazonMarketplaceController {
    @Autowired
    private AmazonMarketplaceService amazonMarketplaceService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("amazon:amazonmarketplace:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = amazonMarketplaceService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{pointId}")
    @RequiresPermissions("amazon:amazonmarketplace:info")
    public R info(@PathVariable("pointId") Integer pointId){
        AmazonMarketplaceEntity amazonMarketplace = amazonMarketplaceService.selectById(pointId);

        return R.ok().put("amazonMarketplace", amazonMarketplace);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("amazon:amazonmarketplace:save")
    public R save(@RequestBody AmazonMarketplaceEntity amazonMarketplace){
        amazonMarketplaceService.insert(amazonMarketplace);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("amazon:amazonmarketplace:update")
    public R update(@RequestBody AmazonMarketplaceEntity amazonMarketplace){
        //ValidatorUtils.validateEntity((amazonMarketplace);
        amazonMarketplaceService.updateAllColumnById(amazonMarketplace);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("amazon:amazonmarketplace:delete")
    public R delete(@RequestBody Integer[] pointIds){
        amazonMarketplaceService.deleteBatchIds(Arrays.asList(pointIds));

        return R.ok();
    }

}
