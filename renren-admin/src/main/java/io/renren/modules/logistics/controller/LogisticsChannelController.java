package io.renren.modules.logistics.controller;

import java.util.Arrays;
import java.util.Map;

import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.logistics.entity.LogisticsChannelEntity;
import io.renren.modules.logistics.service.LogisticsChannelService;
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
 * 
 *
 * @author wangdh
 * @email 594340717@qq.com
 * @date 2019-03-31 09:38:56
 */
@RestController
@RequestMapping("amazon/logisticschannel")
public class LogisticsChannelController {
    @Autowired
    private LogisticsChannelService logisticsChannelService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("amazon:logisticschannel:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = logisticsChannelService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{channelId}")
    @RequiresPermissions("amazon:logisticschannel:info")
    public R info(@PathVariable("channelId") Long channelId){
        LogisticsChannelEntity logisticsChannel = logisticsChannelService.selectById(channelId);

        return R.ok().put("logisticsChannel", logisticsChannel);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("amazon:logisticschannel:save")
    public R save(@RequestBody LogisticsChannelEntity logisticsChannel){
        logisticsChannelService.insert(logisticsChannel);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("amazon:logisticschannel:update")
    public R update(@RequestBody LogisticsChannelEntity logisticsChannel){
        ValidatorUtils.validateEntity(logisticsChannel);
        logisticsChannelService.updateAllColumnById(logisticsChannel);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("amazon:logisticschannel:delete")
    public R delete(@RequestBody Long[] channelIds){
        logisticsChannelService.deleteBatchIds(Arrays.asList(channelIds));

        return R.ok();
    }

}
