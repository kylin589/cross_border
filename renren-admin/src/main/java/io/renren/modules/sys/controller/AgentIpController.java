package io.renren.modules.sys.controller;

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

import io.renren.modules.sys.entity.AgentIpEntity;
import io.renren.modules.sys.service.AgentIpService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-24 00:29:21
 */
@RestController
@RequestMapping("sys/agentip")
public class AgentIpController {
    @Autowired
    private AgentIpService agentIpService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:agentip:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = agentIpService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{agentId}")
    @RequiresPermissions("sys:agentip:info")
    public R info(@PathVariable("agentId") Integer agentId){
        AgentIpEntity agentIp = agentIpService.selectById(agentId);

        return R.ok().put("agentIp", agentIp);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:agentip:save")
    public R save(@RequestBody AgentIpEntity agentIp){
        agentIpService.insert(agentIp);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:agentip:update")
    public R update(@RequestBody AgentIpEntity agentIp){
        ValidatorUtils.validateEntity(agentIp);
        agentIpService.updateAllColumnById(agentIp);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:agentip:delete")
    public R delete(@RequestBody Integer[] agentIds){
        agentIpService.deleteBatchIds(Arrays.asList(agentIds));

        return R.ok();
    }

}
