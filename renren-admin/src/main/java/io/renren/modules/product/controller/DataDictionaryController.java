package io.renren.modules.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.product.service.ProductsService;
import io.renren.modules.sys.controller.AbstractController;
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
public class DataDictionaryController extends AbstractController {
    @Autowired
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private ProductsService productsService;

    /**
     * 列表
     * @param params url列表
     * @return R.ok()
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:datadictionary:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = dataDictionaryService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     * @param dataId id
     * @return R.ok()
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @RequestMapping("/info/{dataId}")
    @RequiresPermissions("product:datadictionary:info")
    public R info(@PathVariable("dataId") Long dataId) {
        DataDictionaryEntity dataDictionary = dataDictionaryService.selectById(dataId);

        return R.ok().put("dataDictionary", dataDictionary);
    }

    /**
     * 保存
     * @param dataDictionary 实体
     * @return R.ok()
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:datadictionary:save")
    public R save(@RequestBody DataDictionaryEntity dataDictionary) {
        dataDictionaryService.insert(dataDictionary);

        return R.ok();
    }

    /**
     * 修改
     * @param dataDictionary 实体
     * @return R.ok()
     * @author zjr
     * @date 2018-11-07 14:54:47
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
     * @param dataIds id数组
     * @return R.ok()
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:datadictionary:delete")
    public R delete(@RequestBody Long[] dataIds) {
        dataDictionaryService.deleteBatchIds(Arrays.asList(dataIds));

        return R.ok();
    }

    /**
     * @methodname: auditlist 我的产品状态分类
     * @param: del 删除标识
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/6 10:02
     */
    @RequestMapping("/mystatuslist")
    public R myStatusList(@RequestParam(value = "del", required = false, defaultValue = "0") String del) {
        List<DataDictionaryEntity> auditList = dataDictionaryService.auditList();
        //定义一个变量 全部的总和
        int auditCounts = 0;
        for (DataDictionaryEntity dataDictionaryEntity : auditList) {
            String number = dataDictionaryEntity.getDataNumber();
            int auditCount = productsService.auditCount(number, del, getUserId());
            dataDictionaryEntity.setCount(auditCount);
            //审核状态分类每个状态的总数进行相加
            auditCounts += auditCount;
        }
        List<DataDictionaryEntity> putawayList = dataDictionaryService.putawayList();
        //定义一个变量 全部的总和
        int putawayCounts = 0;
        for (DataDictionaryEntity dataDictionaryEntity : putawayList) {
            String number = dataDictionaryEntity.getDataNumber();
            int putawayCount = productsService.putawayCount(number, del,getUserId());
            dataDictionaryEntity.setCount(putawayCount);
            //上架状态分类每个状态的总数进行相加
            putawayCounts += putawayCount;
        }
        List<DataDictionaryEntity> productList = dataDictionaryService.productTypeList();
        //定义一个变量 全部的总和
        int productCounts = 0;
        for (DataDictionaryEntity dataDictionaryEntity : productList) {
            String number = dataDictionaryEntity.getDataNumber();
            int productCount = productsService.productCount(number, del,getUserId());
            dataDictionaryEntity.setCount(productCount);
            //产品类型分类每个类型的总数进行相加
            productCounts += productCount;
        }
        return R.ok().put("auditList", auditList).put("auditCounts", auditCounts).put("putawayList", putawayList).put("putawayCounts", putawayCounts).put("productTypeList", productList).put("productTypeCounts", productCounts);
    }

    /**
     * @methodname: allstatuslist 所有产品状态分类
     * @param: del 删除标识
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/6 10:02
     */
    @RequestMapping("/allstatuslist")
    public R allStatusList(@RequestParam(value = "del", required = false, defaultValue = "0") String del) {
        List<DataDictionaryEntity> auditList = dataDictionaryService.auditList();
        //定义一个变量 全部的总和
        int auditCounts = 0;
        for (DataDictionaryEntity dataDictionaryEntity : auditList) {
            String number = dataDictionaryEntity.getDataNumber();
            int auditCount = productsService.auditCountAll(number, del, getDeptId());
            dataDictionaryEntity.setCount(auditCount);
            //审核状态分类每个状态的总数进行相加
            auditCounts += auditCount;
        }
        List<DataDictionaryEntity> putawayList = dataDictionaryService.putawayList();
        //定义一个变量 全部的总和
        int putawayCounts = 0;
        for (DataDictionaryEntity dataDictionaryEntity : putawayList) {
            String number = dataDictionaryEntity.getDataNumber();
            int putawayCount = productsService.putawayCountAll(number, del,getDeptId());
            dataDictionaryEntity.setCount(putawayCount);
            //上架状态分类每个状态的总数进行相加
            putawayCounts += putawayCount;
        }
        List<DataDictionaryEntity> productList = dataDictionaryService.productTypeList();
        //定义一个变量 全部的总和
        int productCounts = 0;
        for (DataDictionaryEntity dataDictionaryEntity : productList) {
            String number = dataDictionaryEntity.getDataNumber();
            int productCount = productsService.productCountAll(number, del,getDeptId());
            dataDictionaryEntity.setCount(productCount);
            //产品类型分类每个类型的总数进行相加
            productCounts += productCount;
        }
        return R.ok().put("auditList", auditList).put("auditCounts", auditCounts).put("putawayList", putawayList).put("putawayCounts", putawayCounts).put("productTypeList", productList).put("productTypeCounts", productCounts);
    }


}