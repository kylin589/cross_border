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

import io.renren.modules.logistics.entity.AbroadLogisticsEntity;
import io.renren.modules.logistics.service.AbroadLogisticsService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 国际物流
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-18 23:28:12
 */
@RestController
@RequestMapping("logistics/abroadlogistics")
public class AbroadLogisticsController {
    @Autowired
    private AbroadLogisticsService abroadLogisticsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("logistics:abroadlogistics:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = abroadLogisticsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{abroadLogisticsId}")
    @RequiresPermissions("logistics:abroadlogistics:info")
    public R info(@PathVariable("abroadLogisticsId") Long abroadLogisticsId){
        AbroadLogisticsEntity abroadLogistics = abroadLogisticsService.selectById(abroadLogisticsId);

        return R.ok().put("abroadLogistics", abroadLogistics);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("logistics:abroadlogistics:save")
    public R save(@RequestBody AbroadLogisticsEntity abroadLogistics){
        abroadLogisticsService.insert(abroadLogistics);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("logistics:abroadlogistics:update")
    public R update(@RequestBody AbroadLogisticsEntity abroadLogistics){
        //ValidatorUtils.validateEntity((abroadLogistics);
        abroadLogisticsService.updateAllColumnById(abroadLogistics);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("logistics:abroadlogistics:delete")
    public R delete(@RequestBody Long[] abroadLogisticsIds){
        abroadLogisticsService.deleteBatchIds(Arrays.asList(abroadLogisticsIds));

        return R.ok();
    }

}
