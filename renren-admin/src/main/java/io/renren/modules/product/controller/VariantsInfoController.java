package io.renren.modules.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.product.entity.VariantsInfoEntity;
import io.renren.modules.product.service.VariantsInfoService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
@RestController
@RequestMapping("product/variantsinfo")
public class VariantsInfoController {
    @Autowired
    private VariantsInfoService variantsInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:variantsinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = variantsInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{variantId}")
    @RequiresPermissions("product:variantsinfo:info")
    public R info(@PathVariable("variantId") Long variantId){
        VariantsInfoEntity variantsInfo = variantsInfoService.selectById(variantId);

        return R.ok().put("variantsInfo", variantsInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:variantsinfo:save")
    public R save(@RequestBody VariantsInfoEntity variantsInfo){
        variantsInfoService.insert(variantsInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:variantsinfo:update")
    public R update(@RequestBody VariantsInfoEntity variantsInfo){
        ValidatorUtils.validateEntity(variantsInfo);
        variantsInfoService.updateAllColumnById(variantsInfo);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:variantsinfo:delete")
    public R delete(@RequestBody Long[] variantIds){
        variantsInfoService.deleteBatchIds(Arrays.asList(variantIds));

        return R.ok();
    }
    /**
     * @methodname: variantsinfoList:根据产品id查出变体参数信息并进行排序
     * @param: [productId] 产品的id
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/8 21:22
     */
    @RequestMapping("variantsinfolist")
    public R variantsinfoList(Long productId) {
        List<VariantsInfoEntity> variantsInfoEntities = variantsInfoService.selectList(new EntityWrapper<VariantsInfoEntity>().eq("product_id", productId).orderBy("variant_sort"));
        return R.ok().put("variantsInfoEntities", variantsInfoEntities);
    }
}
