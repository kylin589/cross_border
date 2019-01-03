package io.renren.modules.amazon.controller;

import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.utils.ValidatorUtils;
import io.renren.modules.sys.controller.AbstractController;
import io.renren.modules.sys.service.SysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.amazon.entity.AmazonGrantShopEntity;
import io.renren.modules.amazon.service.AmazonGrantShopService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 * Amazon授权店铺列表
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-11-27 11:00:50
 */
@RestController
@RequestMapping("amazon/amazongrantshop")
public class AmazonGrantShopController extends AbstractController{
    @Autowired
    private AmazonGrantShopService amazonGrantShopService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 我的店铺列表
     */
    @RequestMapping("/myShopList")
//    @RequiresPermissions("amazon:amazongrantshop:list")
    public R myShopList(@RequestParam Map<String, Object> params){
        List shopList = amazonGrantShopService.selectList(new EntityWrapper<AmazonGrantShopEntity>().eq("user_id",getUserId()));
        return R.ok().put("shopList", shopList);
    }
    /**
     * 所有店铺列表
     */
    @RequestMapping("/allShopList")
//    @RequiresPermissions("amazon:amazongrantshop:list")
    public R allShopList(@RequestParam Map<String, Object> params){
        List<AmazonGrantShopEntity> shopList = new ArrayList<AmazonGrantShopEntity>();
        if(getDeptId() == 1){
            shopList = amazonGrantShopService.selectList(null);
        }else{
            shopList = amazonGrantShopService.selectList(new EntityWrapper<AmazonGrantShopEntity>().eq("dept_id",getDeptId()));
        }
        return R.ok().put("shopList", shopList);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{grantShopId}")
    @RequiresPermissions("amazon:amazongrantshop:info")
    public R info(@PathVariable("grantShopId") Long grantShopId){
        AmazonGrantShopEntity amazonGrantShop = amazonGrantShopService.selectById(grantShopId);

        return R.ok().put("amazonGrantShop", amazonGrantShop);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("amazon:amazongrantshop:save")
    public R save(@RequestBody AmazonGrantShopEntity amazonGrantShop){
        amazonGrantShopService.insert(amazonGrantShop);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("amazon:amazongrantshop:update")
    public R update(@RequestBody AmazonGrantShopEntity amazonGrantShop){
        //ValidatorUtils.validateEntity((amazonGrantShop);
        amazonGrantShopService.updateAllColumnById(amazonGrantShop);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("amazon:amazongrantshop:delete")
    public R delete(@RequestBody Long[] grantShopIds){
        amazonGrantShopService.deleteBatchIds(Arrays.asList(grantShopIds));

        return R.ok();
    }

}
