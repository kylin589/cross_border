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

import io.renren.modules.product.entity.FreightPriceEntity;
import io.renren.modules.product.service.FreightPriceService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 物流单价表
 *
 * @author jhy
 * @email 617493711@qq.com
 * @date 2018-12-07 14:47:30
 */
@RestController
@RequestMapping("product/freightprice")
public class FreightPriceController {
    @Autowired
    private FreightPriceService freightPriceService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:freightprice:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = freightPriceService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{freightPriceId}")
    @RequiresPermissions("product:freightprice:info")
    public R info(@PathVariable("freightPriceId") Long freightPriceId){
        FreightPriceEntity freightPrice = freightPriceService.selectById(freightPriceId);

        return R.ok().put("freightPrice", freightPrice);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:freightprice:save")
    public R save(@RequestBody FreightPriceEntity freightPrice){
        freightPriceService.insert(freightPrice);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:freightprice:update")
    public R update(@RequestBody FreightPriceEntity freightPrice){
        ValidatorUtils.validateEntity(freightPrice);
        freightPriceService.updateAllColumnById(freightPrice);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:freightprice:delete")
    public R delete(@RequestBody Long[] freightPriceIds){
        freightPriceService.deleteBatchIds(Arrays.asList(freightPriceIds));

        return R.ok();
    }

}
