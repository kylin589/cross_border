package io.renren.modules.product.controller;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.product.entity.NewOrderItemEntity;
import io.renren.modules.product.service.NewOrderItemService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 新子订单表
 *
 * @author wangdh
 * @email 594340717@qq.com
 * @date 2019-03-28 14:50:57
 */
@RestController
@RequestMapping("amazon/neworderitem")
public class NewOrderItemController {
    @Autowired
    private NewOrderItemService newOrderItemService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("amazon:neworderitem:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = newOrderItemService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{itemId}")
    @RequiresPermissions("amazon:neworderitem:info")
    public R info(@PathVariable("itemId") Long itemId){
        NewOrderItemEntity newOrderItem = newOrderItemService.selectById(itemId);

        return R.ok().put("newOrderItem", newOrderItem);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("amazon:neworderitem:save")
    public R save(@RequestBody NewOrderItemEntity newOrderItem){
        newOrderItemService.insert(newOrderItem);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("amazon:neworderitem:update")
    public R update(@RequestBody NewOrderItemEntity newOrderItem){
        ValidatorUtils.validateEntity(newOrderItem);
        newOrderItemService.updateAllColumnById(newOrderItem);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("amazon:neworderitem:delete")
    public R delete(@RequestBody Long[] itemIds){
        newOrderItemService.deleteBatchIds(Arrays.asList(itemIds));

        return R.ok();
    }

}
