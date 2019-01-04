package io.renren.modules.order.controller;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.order.entity.RemarkEntity;
import io.renren.modules.order.service.RemarkService;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;



/**
 * 订单备注、操作日志表
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-11 16:32:11
 */
@RestController
@RequestMapping("order/remark")
public class RemarkController extends AbstractController{
    @Autowired
    private RemarkService remarkService;

    /**
     * 添加 添加国内物流操作记录
     */
    @RequestMapping("/addLog")
    public R addLog(@RequestParam Long orderId){
        RemarkEntity remark = new RemarkEntity();
        remark.setOrderId(orderId);
        remark.setType("log");
        remark.setRemark("添加采购信息");
        remark.setUserId(getUserId());
        remark.setUserName(getUser().getDisplayName());
        remark.setUpdateTime(new Date());
        remarkService.insert(remark);
        return R.ok();
    }
    /**
     * 添加 修改国内物流操作记录
     */
    @RequestMapping("/updateLog")
    public R updateLog(@RequestParam Long orderId){
        RemarkEntity remark = new RemarkEntity();
        remark.setOrderId(orderId);
        remark.setType("log");
        remark.setRemark("修改采购信息");
        remark.setUserId(getUserId());
        remark.setUserName(getUser().getDisplayName());
        remark.setUpdateTime(new Date());
        remarkService.insert(remark);
        return R.ok();
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("order:remark:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = remarkService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{remarkId}")
    @RequiresPermissions("order:remark:info")
    public R info(@PathVariable("remarkId") Long remarkId){
        RemarkEntity remark = remarkService.selectById(remarkId);

        return R.ok().put("remark", remark);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("order:remark:save")
    public R save(@RequestBody RemarkEntity remark){
        remark.setType("remark");
        remark.setUserId(getUserId());
        remark.setUserName(getUser().getDisplayName());
        remark.setUpdateTime(new Date());
        remarkService.insert(remark);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("order:remark:update")
    public R update(@RequestBody RemarkEntity remark){
        //ValidatorUtils.validateEntity((remark);
        remarkService.updateAllColumnById(remark);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("order:remark:delete")
    public R delete(@RequestBody Long[] remarkIds){
        remarkService.deleteBatchIds(Arrays.asList(remarkIds));

        return R.ok();
    }
}
