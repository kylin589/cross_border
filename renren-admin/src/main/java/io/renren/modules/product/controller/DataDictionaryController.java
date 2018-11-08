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

import io.renren.modules.product.entity.DataDictionaryEntity;
import io.renren.modules.product.service.DataDictionaryService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 数据字典
 *
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:46
 */
@RestController
@RequestMapping("product/datadictionary")
public class DataDictionaryController {
    @Autowired
    private DataDictionaryService dataDictionaryService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:datadictionary:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = dataDictionaryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{dataId}")
    @RequiresPermissions("product:datadictionary:info")
    public R info(@PathVariable("dataId") Long dataId){
        DataDictionaryEntity dataDictionary = dataDictionaryService.selectById(dataId);

        return R.ok().put("dataDictionary", dataDictionary);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:datadictionary:save")
    public R save(@RequestBody DataDictionaryEntity dataDictionary){
        dataDictionaryService.insert(dataDictionary);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:datadictionary:update")
    public R update(@RequestBody DataDictionaryEntity dataDictionary){
        ValidatorUtils.validateEntity(dataDictionary);
        dataDictionaryService.updateAllColumnById(dataDictionary);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:datadictionary:delete")
    public R delete(@RequestBody Long[] dataIds){
        dataDictionaryService.deleteBatchIds(Arrays.asList(dataIds));

        return R.ok();
    }

}
