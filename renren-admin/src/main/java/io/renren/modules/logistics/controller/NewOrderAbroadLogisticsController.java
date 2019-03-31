package io.renren.modules.logistics.controller;

import java.util.Arrays;
import java.util.Map;

import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.logistics.entity.NewOrderAbroadLogisticsEntity;
import io.renren.modules.logistics.service.NewOrderAbroadLogisticsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 国际物流
 *
 * @author wangdh
 * @email 594340717@qq.com
 * @date 2019-03-28 19:50:52
 */
@RestController
@RequestMapping("amazon/neworderabroadlogistics")
public class NewOrderAbroadLogisticsController {
    @Autowired
    private NewOrderAbroadLogisticsService newOrderAbroadLogisticsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("amazon:neworderabroadlogistics:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = newOrderAbroadLogisticsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{abroadLogisticsId}")
    @RequiresPermissions("amazon:neworderabroadlogistics:info")
    public R info(@PathVariable("abroadLogisticsId") Long abroadLogisticsId){
        NewOrderAbroadLogisticsEntity newOrderAbroadLogistics = newOrderAbroadLogisticsService.selectById(abroadLogisticsId);

        return R.ok().put("newOrderAbroadLogistics", newOrderAbroadLogistics);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("amazon:neworderabroadlogistics:save")
    public R save(@RequestBody NewOrderAbroadLogisticsEntity newOrderAbroadLogistics){
        newOrderAbroadLogisticsService.insert(newOrderAbroadLogistics);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("amazon:neworderabroadlogistics:update")
    public R update(@RequestBody NewOrderAbroadLogisticsEntity newOrderAbroadLogistics){
        ValidatorUtils.validateEntity(newOrderAbroadLogistics);
        newOrderAbroadLogisticsService.updateAllColumnById(newOrderAbroadLogistics);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("amazon:neworderabroadlogistics:delete")
    public R delete(@RequestBody Long[] abroadLogisticsIds){
        newOrderAbroadLogisticsService.deleteBatchIds(Arrays.asList(abroadLogisticsIds));

        return R.ok();
    }

}
