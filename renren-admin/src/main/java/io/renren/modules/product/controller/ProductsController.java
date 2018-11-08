package io.renren.modules.product.controller;

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

import io.renren.modules.product.entity.ProductsEntity;
import io.renren.modules.product.service.ProductsService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;


/**
 * 产品
 *
 * @author jhy
 * @email 617493711@qq.com
 * @date 2018-11-08 09:59:27
 */
@RestController
@RequestMapping("product/products")
public class ProductsController {
    @Autowired
    private ProductsService productsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:products:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = productsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{productId}")
    @RequiresPermissions("product:products:info")
    public R info(@PathVariable("productId") Long productId) {
        ProductsEntity products = productsService.selectById(productId);

        return R.ok().put("products", products);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:products:save")
    public R save(@RequestBody ProductsEntity products) {
        productsService.insert(products);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:products:update")
    public R update(@RequestBody ProductsEntity products) {
        ValidatorUtils.validateEntity(products);
        productsService.updateAllColumnById(products);//全部更新

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:products:delete")
    public R delete(@RequestBody Long[] productIds) {
        productsService.deleteBatchIds(Arrays.asList(productIds));

        return R.ok();
    }

}
