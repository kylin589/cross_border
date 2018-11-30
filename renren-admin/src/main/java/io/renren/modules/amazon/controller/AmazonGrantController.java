package io.renren.modules.amazon.controller;

import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.amazon.entity.AmazonGrantShopEntity;
import io.renren.modules.amazon.entity.AmazonMarketplaceEntity;
import io.renren.modules.amazon.service.AmazonGrantShopService;
import io.renren.modules.amazon.service.AmazonMarketplaceService;
import io.renren.modules.amazon.util.COUNTY;
import io.renren.modules.product.vm.CountryListVM;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.amazon.entity.AmazonGrantEntity;
import io.renren.modules.amazon.service.AmazonGrantService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 * Amazon授权
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-11-27 10:43:39
 */
@RestController
@RequestMapping("amazon/amazongrant")
public class AmazonGrantController extends AbstractController {
    @Autowired
    private AmazonGrantService amazonGrantService;
    @Autowired
    private AmazonGrantShopService amazonGrantShopService;
    @Autowired
    private AmazonMarketplaceService amazonMarketplaceService;
    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("amazon:amazongrant:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = amazonGrantService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{grantId}")
//    @RequiresPermissions("amazon:amazongrant:info")
    public R info(@PathVariable("grantId") Long grantId){
        AmazonGrantEntity amazonGrant = amazonGrantService.selectById(grantId);

        return R.ok().put("amazonGrant", amazonGrant);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("amazon:amazongrant:update")
    public R update(@RequestBody AmazonGrantEntity amazonGrant){
        ValidatorUtils.validateEntity(amazonGrant);
        amazonGrantService.updateAllColumnById(amazonGrant);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("amazon:amazongrant:delete")
    public R delete(@RequestBody Long[] grantIds){
        List<Long> idList = Arrays.asList(grantIds);
        amazonGrantService.deleteBatchIds(idList);
        for(Long id : idList){
            amazonGrantShopService.delete(new EntityWrapper<AmazonGrantShopEntity>().eq("grant_id",id));
        }
        return R.ok();
    }

    /**
     * 添加Amazon授权
     */
    @RequestMapping("/addAmazonGrant")
//    @RequiresPermissions("amazon:amazongrant:save")
    public R addAmazonGrant(@RequestBody AmazonGrantEntity amazonGrant){
        List<AmazonGrantShopEntity> shopList= new ArrayList<AmazonGrantShopEntity>();
        String shopName = amazonGrant.getShopName();
        String amazonAccount = amazonGrant.getAmazonAccount();
        amazonGrant.setUserId(getUserId());
        amazonGrant.setDeptId(getDeptId());
        amazonGrant.setCreateTime(new Date());
        //添加到Amazon授权表
        amazonGrantService.insert(amazonGrant);
        //生成店铺
        if(amazonGrant.getArea() == 0){
            //北美 (墨西哥/加拿大/美国)
            AmazonGrantShopEntity one = new AmazonGrantShopEntity();
            one.setCountryCode(COUNTY.US.toString());
            one.setShopName(shopName + "(美国)");
            one.setShopAccount(amazonAccount);
            shopList.add(one);
            AmazonGrantShopEntity two = new AmazonGrantShopEntity();
            two.setCountryCode(COUNTY.CA.toString());
            two.setShopName(shopName + "(加拿大)");
            two.setShopAccount(amazonAccount);
            shopList.add(two);
            AmazonGrantShopEntity three = new AmazonGrantShopEntity();
            three.setCountryCode(COUNTY.MX.toString());
            three.setShopName(shopName + "(墨西哥)");
            three.setShopAccount(amazonAccount);
            shopList.add(three);
        }else if(amazonGrant.getArea() == 1){
            //欧洲 (意大利/法国/西班牙/英国/德国)
            AmazonGrantShopEntity one = new AmazonGrantShopEntity();
            one.setCountryCode(COUNTY.IT.toString());
            one.setShopName(shopName + "(意大利)");
            one.setShopAccount(amazonAccount);
            shopList.add(one);
            AmazonGrantShopEntity two = new AmazonGrantShopEntity();
            two.setCountryCode(COUNTY.FR.toString());
            two.setShopName(shopName + "(法国)");
            two.setShopAccount(amazonAccount);
            shopList.add(two);
            AmazonGrantShopEntity three = new AmazonGrantShopEntity();
            three.setCountryCode(COUNTY.ES.toString());
            three.setShopName(shopName + "(西班牙)");
            three.setShopAccount(amazonAccount);
            shopList.add(three);
            AmazonGrantShopEntity four = new AmazonGrantShopEntity();
            four.setCountryCode(COUNTY.GB.toString());
            four.setShopName(shopName + "(英国)");
            four.setShopAccount(amazonAccount);
            shopList.add(four);
            AmazonGrantShopEntity five = new AmazonGrantShopEntity();
            five.setCountryCode(COUNTY.DE.toString());
            five.setShopName(shopName + "(德国)");
            five.setShopAccount(amazonAccount);
            shopList.add(five);
        }else if(amazonGrant.getArea() == 2){
            //日本
            AmazonGrantShopEntity one = new AmazonGrantShopEntity();
            one.setCountryCode(COUNTY.JP.toString());
            one.setShopName(shopName + "(日本)");
            one.setShopAccount(amazonAccount);
            shopList.add(one);
        }else if(amazonGrant.getArea() == 3){
            //澳大利亚
            AmazonGrantShopEntity one = new AmazonGrantShopEntity();
            one.setCountryCode(COUNTY.AU.toString());
            one.setShopName(shopName + "(澳大利亚)");
            one.setShopAccount(amazonAccount);
            shopList.add(one);
        }
        //关联站点信息
        relationMwsPoint(amazonGrant.getGrantId(), shopList);
        //添加授权店铺
        amazonGrantShopService.insertBatch(shopList);
        return R.ok();
    }

    private void relationMwsPoint(Long grantId, List<AmazonGrantShopEntity> shopList){
        for(AmazonGrantShopEntity amazonGrantShop : shopList){
            amazonGrantShop.setGrantId(grantId);
            amazonGrantShop.setUserId(getUserId());
            amazonGrantShop.setDeptId(getDeptId());
            AmazonMarketplaceEntity amazonMarketplace = amazonMarketplaceService.selectOne(
                    new EntityWrapper<AmazonMarketplaceEntity>()
                            .eq("country_code",amazonGrantShop.getCountryCode())
                            .eq("is_deleted",0));
            if (amazonMarketplace != null){
                amazonGrantShop.setAmazonSite(amazonMarketplace.getAmazonSite());
                amazonGrantShop.setGrantCounty(amazonMarketplace.getCountry());
                amazonGrantShop.setMarketplaceId(amazonMarketplace.getMarketplaceId());
                amazonGrantShop.setMwsPoint(amazonMarketplace.getMwsPoint());
                amazonGrantShop.setRegion(amazonMarketplace.getRegion());
            }
        }
    }

    /**
     * 查询国家列表
     */
    @RequestMapping("/countryList")
//    @RequiresPermissions("amazon:amazongrant:save")
    public R addAmazonGrant(@RequestParam Long grantId){
        List<CountryListVM> countryList = new ArrayList<CountryListVM>();
        List<AmazonGrantShopEntity> shopList = amazonGrantShopService.selectList(new EntityWrapper<AmazonGrantShopEntity>().eq("grant_id",grantId));
        for(AmazonGrantShopEntity shop : shopList){
            CountryListVM vm = new CountryListVM();
            vm.setShopName(shop.getShopName());
            vm.setCountry(shop.getGrantCounty());
            vm.setAmazonSite(shop.getAmazonSite());
            countryList.add(vm);
        }
        return R.ok().put("countryList",countryList);
    }
}
