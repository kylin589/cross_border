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

import io.renren.modules.product.entity.FreightCostEntity;
import io.renren.modules.product.service.FreightCostService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 物流成本
 *
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
@RestController
@RequestMapping("product/freightcost")
public class FreightCostController {
    @Autowired
    private FreightCostService freightCostService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:freightcost:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = freightCostService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{freightCostId}")
    @RequiresPermissions("product:freightcost:info")
    public R info(@PathVariable("freightCostId") Long freightCostId){
        FreightCostEntity freightCost = freightCostService.selectById(freightCostId);

        return R.ok().put("freightCost", freightCost);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:freightcost:save")
    public R save(@RequestBody FreightCostEntity freightCost){
        freightCostService.insert(freightCost);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:freightcost:update")
    public R update(@RequestBody FreightCostEntity freightCost){
        ValidatorUtils.validateEntity(freightCost);
        freightCostService.updateAllColumnById(freightCost);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:freightcost:delete")
    public R delete(@RequestBody Long[] freightCostIds){
        freightCostService.deleteBatchIds(Arrays.asList(freightCostIds));

        return R.ok();
    }

}
