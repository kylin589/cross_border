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

import io.renren.modules.sys.entity.RechargeEntity;
import io.renren.modules.sys.service.RechargeService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-17 11:09:16
 */
@RestController
@RequestMapping("sys/recharge")
public class RechargeController {
    @Autowired
    private RechargeService rechargeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:recharge:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = rechargeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{companyRechargeId}")
    @RequiresPermissions("sys:recharge:info")
    public R info(@PathVariable("companyRechargeId") Long companyRechargeId){
        RechargeEntity recharge = rechargeService.selectById(companyRechargeId);

        return R.ok().put("recharge", recharge);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:recharge:save")
    public R save(@RequestBody RechargeEntity recharge){
        rechargeService.insert(recharge);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:recharge:update")
    public R update(@RequestBody RechargeEntity recharge){
        ValidatorUtils.validateEntity(recharge);
        rechargeService.updateAllColumnById(recharge);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:recharge:delete")
    public R delete(@RequestBody Long[] companyRechargeIds){
        rechargeService.deleteBatchIds(Arrays.asList(companyRechargeIds));

        return R.ok();
    }

}
