package io.renren.modules.order.controller;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.order.entity.NewProductShipAddressEntity;
import io.renren.modules.order.entity.ProductShipAddressEntity;
import io.renren.modules.order.service.NewProductShipAddressService;
import io.renren.modules.order.service.ProductShipAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 订单配送地址表
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-10 10:16:27
 */
@RestController
@RequestMapping("order/newproductshipaddress")
public class NewProductShipAddressController {
    @Autowired
    private NewProductShipAddressService newproductShipAddressService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("order:productshipaddress:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = newproductShipAddressService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{shipAddressId}")
//    @RequiresPermissions("order:productshipaddress:info")
    public R info(@PathVariable("shipAddressId") Long shipAddressId){
        NewProductShipAddressEntity newproductShipAddress = newproductShipAddressService.selectById(shipAddressId);

        return R.ok().put("productShipAddress", newproductShipAddress);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("order:productshipaddress:save")
    public R save(@RequestBody NewProductShipAddressEntity productShipAddress){
        newproductShipAddressService.insert(productShipAddress);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("order:productshipaddress:update")
    public R update(@RequestBody NewProductShipAddressEntity productShipAddress){
        //ValidatorUtils.validateEntity((productShipAddress);
        newproductShipAddressService.updateAllColumnById(productShipAddress);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("order:productshipaddress:delete")
    public R delete(@RequestBody Long[] shipAddressIds){
        newproductShipAddressService.deleteBatchIds(Arrays.asList(shipAddressIds));

        return R.ok();
    }

}
