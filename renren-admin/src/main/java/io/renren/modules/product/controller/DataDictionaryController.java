package io.renren.modules.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.product.service.ProductsService;
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
 * @author jhy
 * @email 617493711@qq.com
 * @date 2018-11-08 09:59:28
 */
@RestController
@RequestMapping("product/datadictionary")
public class DataDictionaryController {
    @Autowired
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private ProductsService productsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:datadictionary:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = dataDictionaryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{dataId}")
    @RequiresPermissions("product:datadictionary:info")
    public R info(@PathVariable("dataId") Long dataId) {
        DataDictionaryEntity dataDictionary = dataDictionaryService.selectById(dataId);

        return R.ok().put("dataDictionary", dataDictionary);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:datadictionary:save")
    public R save(@RequestBody DataDictionaryEntity dataDictionary) {
        dataDictionaryService.insert(dataDictionary);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:datadictionary:update")
    public R update(@RequestBody DataDictionaryEntity dataDictionary) {
        ValidatorUtils.validateEntity(dataDictionary);
        dataDictionaryService.updateAllColumnById(dataDictionary);//全部更新

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:datadictionary:delete")
    public R delete(@RequestBody Long[] dataIds) {
        dataDictionaryService.deleteBatchIds(Arrays.asList(dataIds));

        return R.ok();
    }

    /**
     * @methodname: auditlist:审核状态分类
     * @param: []
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/6 10:02
     */
    @RequestMapping("/auditlist")
    public R auditlist() {
        List<DataDictionaryEntity> auditList = dataDictionaryService.auditList();
        //定义一个变量 全部的总和
        int auditCounts = 0;
        for (DataDictionaryEntity dataDictionaryEntity : auditList) {
            String number = dataDictionaryEntity.getDataNumber();
            int auditCount = productsService.auditCount(number);
            dataDictionaryEntity.setCount(auditCount);
            auditCounts += auditCount;
        }
        return R.ok().put("auditlist", auditList).put("auditCounts", auditCounts);
    }


    /**
     * @methodname: putawaylist:上架状态分类
     * @param: []
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/6 10:05
     */
    @RequestMapping("/putawaylist")
    public R putawaylist() {
        List<DataDictionaryEntity> putawayList = dataDictionaryService.putawayList();
        //定义一个变量 全部的总和
        int putawayCounts = 0;
        for (DataDictionaryEntity dataDictionaryEntity : putawayList) {
            String number = dataDictionaryEntity.getDataNumber();
            int putawayCount = productsService.putawayCount(number);
            dataDictionaryEntity.setCount(putawayCount);
            putawayCounts += putawayCount;
        }
        return R.ok().put("putawaylist", putawayList).put("putawayCounts", putawayCounts);
    }


    /**
     * @methodname: productlist产品类型分类
     * @param: []
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/6 9:59
     */
    @RequestMapping("/productlist")
    public R productlist() {
        List<DataDictionaryEntity> productList = dataDictionaryService.productList();
        //定义一个变量 全部的总和
        int productCounts = 0;
        for (DataDictionaryEntity dataDictionaryEntity : productList) {
            String number = dataDictionaryEntity.getDataNumber();
            int productCount = productsService.productCount(number);
            dataDictionaryEntity.setCount(productCount);
            productCounts += productCount;
        }
        return R.ok().put("productlist", productList).put("producCounts", productCounts);
    }
}
