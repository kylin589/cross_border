package io.renren.modules.product.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.product.entity.OrderEntity;
import io.renren.modules.product.service.OrderService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-19 14:59:13
 */
@RestController
@RequestMapping("product/order")
public class OrderController extends AbstractController{
    @Autowired
    private OrderService orderService;
    /**
     * 我的订单
     */
    @RequestMapping("/getMyList")
//    @RequiresPermissions("product:order:mylist")
    public R getMyList(@RequestParam Map<String, Object> params){
        Map<String, Object> map = orderService.queryMyPage(params, getUserId());
        return R.ok().put("page", map.get("page")).put("orderCounts",map.get("orderCounts"));
    }

    /**
     * 所有订单
     */
    @RequestMapping("/getAllList")
//    @RequiresPermissions("product:order:alllist")
    public R getAllList(@RequestParam Map<String, Object> params){
        Map<String, Object> map = orderService.queryAllPage(params, getUserId());
        return R.ok().put("page", map.get("page")).put("orderCounts",map.get("orderCounts"));
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{orderId}")
    @RequiresPermissions("product:order:info")
    public R info(@PathVariable("orderId") Long orderId){
        OrderEntity order = orderService.selectById(orderId);

        return R.ok().put("order", order);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:order:save")
    public R save(@RequestBody OrderEntity order){
        orderService.insert(order);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:order:update")
    public R update(@RequestBody OrderEntity order){
        ValidatorUtils.validateEntity(order);
        orderService.updateAllColumnById(order);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:order:delete")
    public R delete(@RequestBody Long[] orderIds){
        orderService.deleteBatchIds(Arrays.asList(orderIds));

        return R.ok();
    }

    /**
     * 修改状态
     */
    @RequestMapping("/updateState")
    public R updateState(@RequestParam Long orderId, @RequestParam String orderState){
        boolean flag = orderService.updateState(orderId,orderState);
        if(flag){
            return R.ok();
        }
        return R.error();
    }

}
