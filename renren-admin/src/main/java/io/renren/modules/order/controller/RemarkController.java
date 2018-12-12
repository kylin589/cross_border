package io.renren.modules.order.controller;

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

import io.renren.modules.order.entity.RemarkEntity;
import io.renren.modules.order.service.RemarkService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 订单备注、操作日志表
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-11 16:32:11
 */
@RestController
@RequestMapping("order/remark")
public class RemarkController {
    @Autowired
    private RemarkService remarkService;

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
    @RequiresPermissions("order:remark:save")
    public R save(@RequestBody RemarkEntity remark){
        remarkService.insert(remark);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("order:remark:update")
    public R update(@RequestBody RemarkEntity remark){
        ValidatorUtils.validateEntity(remark);
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
