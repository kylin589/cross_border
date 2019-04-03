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

import io.renren.modules.logistics.entity.NewOrderItemRelationshipEntity;
import io.renren.modules.logistics.service.NewOrderItemRelationshipService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author wangdh
 * @email 594340717@qq.com
 * @date 2019-04-03 15:39:33
 */
@RestController
@RequestMapping("logistics/neworderitemrelationship")
public class NewOrderItemRelationshipController {
    @Autowired
    private NewOrderItemRelationshipService newOrderItemRelationshipService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("logistics:neworderitemrelationship:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = newOrderItemRelationshipService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{relationshipId}")
    @RequiresPermissions("logistics:neworderitemrelationship:info")
    public R info(@PathVariable("relationshipId") Long relationshipId){
        NewOrderItemRelationshipEntity newOrderItemRelationship = newOrderItemRelationshipService.selectById(relationshipId);

        return R.ok().put("newOrderItemRelationship", newOrderItemRelationship);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("logistics:neworderitemrelationship:save")
    public R save(@RequestBody NewOrderItemRelationshipEntity newOrderItemRelationship){
        newOrderItemRelationshipService.insert(newOrderItemRelationship);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("logistics:neworderitemrelationship:update")
    public R update(@RequestBody NewOrderItemRelationshipEntity newOrderItemRelationship){
        ValidatorUtils.validateEntity(newOrderItemRelationship);
        newOrderItemRelationshipService.updateAllColumnById(newOrderItemRelationship);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("logistics:neworderitemrelationship:delete")
    public R delete(@RequestBody Long[] relationshipIds){
        newOrderItemRelationshipService.deleteBatchIds(Arrays.asList(relationshipIds));

        return R.ok();
    }

}
