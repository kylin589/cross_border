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

import io.renren.modules.order.entity.ProductShipAddressEntity;
import io.renren.modules.order.service.ProductShipAddressService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 订单配送地址表
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-10 10:16:27
 */
@RestController
@RequestMapping("order/productshipaddress")
public class ProductShipAddressController {
    @Autowired
    private ProductShipAddressService productShipAddressService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("order:productshipaddress:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = productShipAddressService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{shipAddressId}")
    @RequiresPermissions("order:productshipaddress:info")
    public R info(@PathVariable("shipAddressId") Long shipAddressId){
        ProductShipAddressEntity productShipAddress = productShipAddressService.selectById(shipAddressId);

        return R.ok().put("productShipAddress", productShipAddress);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("order:productshipaddress:save")
    public R save(@RequestBody ProductShipAddressEntity productShipAddress){
        productShipAddressService.insert(productShipAddress);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("order:productshipaddress:update")
    public R update(@RequestBody ProductShipAddressEntity productShipAddress){
        //ValidatorUtils.validateEntity((productShipAddress);
        productShipAddressService.updateAllColumnById(productShipAddress);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("order:productshipaddress:delete")
    public R delete(@RequestBody Long[] shipAddressIds){
        productShipAddressService.deleteBatchIds(Arrays.asList(shipAddressIds));

        return R.ok();
    }

}
