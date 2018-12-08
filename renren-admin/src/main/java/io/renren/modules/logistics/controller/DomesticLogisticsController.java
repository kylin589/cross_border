package io.renren.modules.logistics.controller;

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

import io.renren.modules.logistics.entity.DomesticLogisticsEntity;
import io.renren.modules.logistics.service.DomesticLogisticsService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 国内物流
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-08 11:07:32
 */
@RestController
@RequestMapping("logistics/domesticlogistics")
public class DomesticLogisticsController {
    @Autowired
    private DomesticLogisticsService domesticLogisticsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("logistics:domesticlogistics:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = domesticLogisticsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{domesticLogisticsId}")
    @RequiresPermissions("logistics:domesticlogistics:info")
    public R info(@PathVariable("domesticLogisticsId") Long domesticLogisticsId){
        DomesticLogisticsEntity domesticLogistics = domesticLogisticsService.selectById(domesticLogisticsId);

        return R.ok().put("domesticLogistics", domesticLogistics);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("logistics:domesticlogistics:save")
    public R save(@RequestBody DomesticLogisticsEntity domesticLogistics){
        domesticLogisticsService.insert(domesticLogistics);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("logistics:domesticlogistics:update")
    public R update(@RequestBody DomesticLogisticsEntity domesticLogistics){
        ValidatorUtils.validateEntity(domesticLogistics);
        domesticLogisticsService.updateAllColumnById(domesticLogistics);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("logistics:domesticlogistics:delete")
    public R delete(@RequestBody Long[] domesticLogisticsIds){
        domesticLogisticsService.deleteBatchIds(Arrays.asList(domesticLogisticsIds));

        return R.ok();
    }

}
