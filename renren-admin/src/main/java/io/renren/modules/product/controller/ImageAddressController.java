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

import io.renren.modules.product.entity.ImageAddressEntity;
import io.renren.modules.product.service.ImageAddressService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 产品图片表
 *
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
@RestController
@RequestMapping("product/imageaddress")
public class ImageAddressController {
    @Autowired
    private ImageAddressService imageAddressService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:imageaddress:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = imageAddressService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{imageId}")
    @RequiresPermissions("product:imageaddress:info")
    public R info(@PathVariable("imageId") Long imageId){
        ImageAddressEntity imageAddress = imageAddressService.selectById(imageId);

        return R.ok().put("imageAddress", imageAddress);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:imageaddress:save")
    public R save(@RequestBody ImageAddressEntity imageAddress){
        imageAddressService.insert(imageAddress);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:imageaddress:update")
    public R update(@RequestBody ImageAddressEntity imageAddress){
        ValidatorUtils.validateEntity(imageAddress);
        imageAddressService.updateAllColumnById(imageAddress);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:imageaddress:delete")
    public R delete(@RequestBody Long[] imageIds){
        imageAddressService.deleteBatchIds(Arrays.asList(imageIds));

        return R.ok();
    }

}
