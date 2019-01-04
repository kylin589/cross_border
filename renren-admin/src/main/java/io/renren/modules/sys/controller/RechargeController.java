package io.renren.modules.sys.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.service.SysDeptService;
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
 * 充值
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-17 11:09:16
 */
@RestController
@RequestMapping("sys/recharge")
public class RechargeController extends AbstractController {
    @Autowired
    private RechargeService rechargeService;
    @Autowired
    private SysDeptService deptService;

    /**
     * 充值
     */
    @RequestMapping("/recharge")
//    @RequiresPermissions("sys:recharge:recharge")
    public R recharge(@RequestBody RechargeEntity recharge){
        try{
            recharge.setUserId(getUserId());
            recharge.setUserName(getUser().getDisplayName());
            recharge.setRechargeTime(new Date());
            //获取充值的公司
            SysDeptEntity dept = deptService.selectById(recharge.getDeptId());
            //设置余额
            dept.setBalance(dept.getBalance().add(recharge.getMoney()));
            //设置可用余额
            BigDecimal availableBalance = dept.getAvailableBalance().add(recharge.getMoney());
            dept.setAvailableBalance(availableBalance);
            //设置预计还可生成单数 充值金额除以50 加 之前预计数
            int estimatedOrder = recharge.getMoney().divide(new BigDecimal(50),0,BigDecimal.ROUND_HALF_DOWN).intValue();
            dept.setEstimatedOrder(dept.getEstimatedOrder() + estimatedOrder);
            deptService.updateById(dept);
            rechargeService.insert(recharge);
            return R.ok();
        }catch (Exception e){
            return R.error("充值失败，请联系管理员");
        }
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("sys:recharge:list")
    public R list(@RequestParam Long deptId){
//        PageUtils page = rechargeService.queryPage(params);
        List<RechargeEntity> list = rechargeService.selectList(new EntityWrapper<RechargeEntity>().eq("dept_id",deptId));
        return R.ok().put("list", list);
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
//        //ValidatorUtils.validateEntity((recharge);
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
