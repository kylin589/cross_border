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

import io.renren.modules.sys.entity.ConsumeEntity;
import io.renren.modules.sys.service.ConsumeService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 消费记录
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-17 17:36:01
 */
@RestController
@RequestMapping("sys/consume")
public class ConsumeController {
    @Autowired
    private ConsumeService consumeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:consume:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = consumeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{companyConsumeId}")
    @RequiresPermissions("sys:consume:info")
    public R info(@PathVariable("companyConsumeId") Long companyConsumeId){
        ConsumeEntity consume = consumeService.selectById(companyConsumeId);

        return R.ok().put("consume", consume);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:consume:save")
    public R save(@RequestBody ConsumeEntity consume){
        consumeService.insert(consume);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:consume:update")
    public R update(@RequestBody ConsumeEntity consume){
        ValidatorUtils.validateEntity(consume);
        consumeService.updateAllColumnById(consume);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:consume:delete")
    public R delete(@RequestBody Long[] companyConsumeIds){
        consumeService.deleteBatchIds(Arrays.asList(companyConsumeIds));

        return R.ok();
    }

}
