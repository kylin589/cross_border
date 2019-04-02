package io.renren.modules.product.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.amazon.util.ConstantDictionary;
import io.renren.modules.product.entity.DataDictionaryEntity;
import io.renren.modules.product.entity.NewOrderEntity;
import io.renren.modules.product.entity.OrderEntity;
import io.renren.modules.product.entity.ProductsEntity;
import io.renren.modules.product.service.DataDictionaryService;
import io.renren.modules.product.service.NewOrderService;
import io.renren.modules.product.service.OrderService;
import io.renren.modules.product.service.ProductsService;
import io.renren.modules.sys.controller.AbstractController;
import io.renren.modules.sys.service.SysUserRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    @Autowired
    @Lazy
    private OrderService orderService;
    @Autowired
    @Lazy
    private NewOrderService newOrderService;
    @Autowired
    private SysUserRoleService userRoleService;
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
        //ValidatorUtils.validateEntity((dataDictionary);
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
        List<DataDictionaryEntity> auditList = dataDictionaryService.selectList(new EntityWrapper<DataDictionaryEntity>().eq("data_type", "AUDIT_STATE").orderBy(true, "data_sort", true));
        //定义一个变量 全部的总和
        int auditCounts = 0;
        for (DataDictionaryEntity dataDictionaryEntity : auditList) {
            int auditCount = productsService.selectCount(new EntityWrapper<ProductsEntity>().eq("audit_status", dataDictionaryEntity.getDataNumber()).eq("is_deleted", del).eq("create_user_id", getUserId()));
            dataDictionaryEntity.setCount(auditCount);
            //审核状态分类每个状态的总数进行相加
            auditCounts += auditCount;
        }
        List<DataDictionaryEntity> putawayList = dataDictionaryService.selectList(new EntityWrapper<DataDictionaryEntity>().eq("data_type", "SHELVE_STATE").orderBy(true, "data_sort", true));
        //定义一个变量 全部的总和
        int putawayCounts = 0;
        for (DataDictionaryEntity dataDictionaryEntity : putawayList) {
            int putawayCount = productsService.selectCount(new EntityWrapper<ProductsEntity>().eq("shelve_status", dataDictionaryEntity.getDataNumber()).eq("is_deleted", del).eq("create_user_id", getUserId()));
            dataDictionaryEntity.setCount(putawayCount);
            //上架状态分类每个状态的总数进行相加
            putawayCounts += putawayCount;
        }
        List<DataDictionaryEntity> productList = dataDictionaryService.selectList(new EntityWrapper<DataDictionaryEntity>().eq("data_type", "PRODUCT_TYPE").orderBy(true, "data_sort", true));
        //定义一个变量 全部的总和
        int productCounts = 0;
        for (DataDictionaryEntity dataDictionaryEntity : productList) {
            int productCount = productsService.selectCount(new EntityWrapper<ProductsEntity>().eq("product_type", dataDictionaryEntity.getDataNumber()).eq("is_deleted", del).eq("create_user_id", getUserId()));
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
        List<DataDictionaryEntity> auditList = dataDictionaryService.selectList(new EntityWrapper<DataDictionaryEntity>().eq("data_type", "AUDIT_STATE").orderBy(true, "data_sort", true));
        //定义一个变量 全部的总和
        int auditCounts = 0;
        for (DataDictionaryEntity dataDictionaryEntity : auditList) {
            String number = dataDictionaryEntity.getDataNumber();
            int auditCount = productsService.auditCountAll(number, del, getDeptId());
            dataDictionaryEntity.setCount(auditCount);
            //审核状态分类每个状态的总数进行相加
            auditCounts += auditCount;
        }
        List<DataDictionaryEntity> putawayList = dataDictionaryService.selectList(new EntityWrapper<DataDictionaryEntity>().eq("data_type", "SHELVE_STATE").orderBy(true, "data_sort", true));
        //定义一个变量 全部的总和
        int putawayCounts = 0;
        for (DataDictionaryEntity dataDictionaryEntity : putawayList) {
            String number = dataDictionaryEntity.getDataNumber();
            int putawayCount = productsService.putawayCountAll(number, del,getDeptId());
            dataDictionaryEntity.setCount(putawayCount);
            //上架状态分类每个状态的总数进行相加
            putawayCounts += putawayCount;
        }
        List<DataDictionaryEntity> productList = dataDictionaryService.selectList(new EntityWrapper<DataDictionaryEntity>().eq("data_type", "PRODUCT_TYPE").orderBy(true, "data_sort", true));
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

    /**
     * @methodname: myOrderStateList 我的订单状态获取
     * @return: io.renren.common.utils.R
     * @auther: wdh
     * @date: 2018/12/3 10:02
     */
    @RequestMapping("/myOrderStateList")
    public R myOrderStateList() {
        List<DataDictionaryEntity> orderStateList = dataDictionaryService.selectList(new EntityWrapper<DataDictionaryEntity>().eq("data_type", "AMAZON_ORDER_STATE").orderBy(true, "data_sort", true));
        List<DataDictionaryEntity> abnormalStateList = dataDictionaryService.selectList(new EntityWrapper<DataDictionaryEntity>().eq("data_type", "ORDER_ABNORMAL_STATE").orderBy(true, "data_sort", true));
        //定义一个变量 全部的总和
        int allOrderCount = orderService.selectCount(new EntityWrapper<OrderEntity>().eq("user_id",getUserId()).eq("is_old",0));
        for (DataDictionaryEntity orderState : orderStateList) {
            int orderCount = orderService.selectCount(new EntityWrapper<OrderEntity>().eq("order_status",orderState.getDataNumber()).eq("user_id",getUserId()).eq("is_old",0));
            orderState.setCount(orderCount);
        }
        for(DataDictionaryEntity abnormalState : abnormalStateList){
            int orderCount = orderService.selectCount(new EntityWrapper<OrderEntity>().eq("abnormal_status",abnormalState.getDataNumber()).eq("user_id",getUserId()).eq("is_old",0));
            abnormalState.setCount(orderCount);
        }
        orderStateList.addAll(abnormalStateList);
        return R.ok().put("orderStateList", orderStateList).put("allOrderCount", allOrderCount);
    }
    /**
     * @methodname: myOrderStateList 我的订单状态获取
     * @return: io.renren.common.utils.R
     * @auther: wdh
     * @date: 2018/12/3 10:02
     */
    @RequestMapping("/myNewOrderStateList")
    public R myNewOrderStateList() {
        List<DataDictionaryEntity> orderStateList = dataDictionaryService.selectList(new EntityWrapper<DataDictionaryEntity>().eq("data_type", "NEW_AMAZON_ORDER_STATE").orderBy(true, "data_sort", true));
        List<DataDictionaryEntity> abnormalStateList = dataDictionaryService.selectList(new EntityWrapper<DataDictionaryEntity>().eq("data_type", "ORDER_ABNORMAL_STATE").orderBy(true, "data_sort", true));
        //定义一个变量 全部的总和
        int allOrderCount = newOrderService.selectCount(new EntityWrapper<NewOrderEntity>().eq("user_id",getUserId()));
        for (DataDictionaryEntity orderState : orderStateList) {
            int orderCount = newOrderService.selectCount(new EntityWrapper<NewOrderEntity>().eq("order_status",orderState.getDataNumber()).eq("user_id",getUserId()));
            orderState.setCount(orderCount);
        }
        for(DataDictionaryEntity abnormalState : abnormalStateList){
            int orderCount = newOrderService.selectCount(new EntityWrapper<NewOrderEntity>().eq("abnormal_status",abnormalState.getDataNumber()).eq("user_id",getUserId()));
            abnormalState.setCount(orderCount);
        }
        orderStateList.addAll(abnormalStateList);
        return R.ok().put("orderStateList", orderStateList).put("allOrderCount", allOrderCount);
    }
    /**
     * @methodname: myOrderStateList 我的订单状态获取(旧)
     * @return: io.renren.common.utils.R
     * @auther: wdh
     * @date: 2018/12/3 10:02
     */
    @RequestMapping("/myOldOrderStateList")
    public R myOldOrderStateList() {
        List<DataDictionaryEntity> orderStateList = dataDictionaryService.selectList(new EntityWrapper<DataDictionaryEntity>().eq("data_type", "AMAZON_ORDER_STATE").orderBy(true, "data_sort", true));
        List<DataDictionaryEntity> abnormalStateList = dataDictionaryService.selectList(new EntityWrapper<DataDictionaryEntity>().eq("data_type", "ORDER_ABNORMAL_STATE").orderBy(true, "data_sort", true));
        //定义一个变量 全部的总和
        int allOrderCount = orderService.selectCount(new EntityWrapper<OrderEntity>().eq("user_id",getUserId()).eq("is_old",1));
        for (DataDictionaryEntity orderState : orderStateList) {
            int orderCount = orderService.selectCount(new EntityWrapper<OrderEntity>().eq("order_status",orderState.getDataNumber()).eq("user_id",getUserId()).eq("is_old",1));
            orderState.setCount(orderCount);
        }
        for(DataDictionaryEntity abnormalState : abnormalStateList){
            int orderCount = orderService.selectCount(new EntityWrapper<OrderEntity>().eq("abnormal_status",abnormalState.getDataNumber()).eq("user_id",getUserId()).eq("is_old",1));
            abnormalState.setCount(orderCount);
        }
        orderStateList.addAll(abnormalStateList);
        return R.ok().put("orderStateList", orderStateList).put("allOrderCount", allOrderCount);
    }
    /**
     * @methodname: myOrderStateList 所有订单状态获取
     * @return: io.renren.common.utils.R
     * @auther: wdh
     * @date: 2018/12/3 10:02
     */
    @RequestMapping("/allOrderStateList")
    public R allOrderStateList() {
        List<DataDictionaryEntity> orderStateList = dataDictionaryService.selectList(new EntityWrapper<DataDictionaryEntity>().eq("data_type", "AMAZON_ORDER_STATE").orderBy(true, "data_sort", true));
        List<DataDictionaryEntity> abnormalStateList = dataDictionaryService.selectList(new EntityWrapper<DataDictionaryEntity>().eq("data_type", "ORDER_ABNORMAL_STATE").orderBy(true, "data_sort", true));
        //定义一个变量 全部的总和
        int allOrderCount = orderService.selectCount(new EntityWrapper<OrderEntity>().eq(getDeptId()!=1L,"dept_id",getDeptId()).eq("is_old",0));
        for (DataDictionaryEntity orderState : orderStateList) {
            int orderCount = orderService.selectCount(new EntityWrapper<OrderEntity>().eq("order_status",orderState.getDataNumber()).eq(getDeptId()!=1L,"dept_id",getDeptId()).eq("is_old",0));
            orderState.setCount(orderCount);
        }
        for (DataDictionaryEntity abnormalState : abnormalStateList) {
            int orderCount = orderService.selectCount(new EntityWrapper<OrderEntity>().eq("abnormal_status",abnormalState.getDataNumber()).eq(getDeptId()!=1L,"dept_id",getDeptId()).eq("is_old",0));
            abnormalState.setCount(orderCount);
        }
        orderStateList.addAll(abnormalStateList);
        return R.ok().put("orderStateList", orderStateList).put("allOrderCount", allOrderCount);
    }
    /**
     * @methodname: myOrderStateList 所有订单状态获取
     * @return: io.renren.common.utils.R
     * @auther: wdh
     * @date: 2018/12/3 10:02
     */
    @RequestMapping("/allNewOrderStateList")
    public R allNewOrderStateList() {
        List<DataDictionaryEntity> orderStateList = dataDictionaryService.selectList(new EntityWrapper<DataDictionaryEntity>().eq("data_type", "NEW_AMAZON_ORDER_STATE").orderBy(true, "data_sort", true));
        List<DataDictionaryEntity> abnormalStateList = dataDictionaryService.selectList(new EntityWrapper<DataDictionaryEntity>().eq("data_type", "ORDER_ABNORMAL_STATE").orderBy(true, "data_sort", true));
        //定义一个变量 全部的总和
        int allOrderCount = newOrderService.selectCount(new EntityWrapper<NewOrderEntity>().eq(getDeptId()!=1L,"dept_id",getDeptId()));
        for (DataDictionaryEntity orderState : orderStateList) {
            int orderCount = newOrderService.selectCount(new EntityWrapper<NewOrderEntity>().eq("order_status",orderState.getDataNumber()).eq(getDeptId()!=1L,"dept_id",getDeptId()).eq("is_old",0));
            orderState.setCount(orderCount);
        }
        for (DataDictionaryEntity abnormalState : abnormalStateList) {
            int orderCount = newOrderService.selectCount(new EntityWrapper<NewOrderEntity>().eq("abnormal_status",abnormalState.getDataNumber()).eq(getDeptId()!=1L,"dept_id",getDeptId()).eq("is_old",0));
            abnormalState.setCount(orderCount);
        }
        orderStateList.addAll(abnormalStateList);
        return R.ok().put("orderStateList", orderStateList).put("allOrderCount", allOrderCount);
    }
    /**
     * @methodname: myOrderStateList 所有订单状态获取（旧）
     * @return: io.renren.common.utils.R
     * @auther: wdh
     * @date: 2018/12/3 10:02
     */
    @RequestMapping("/allOldOrderStateList")
    public R allOldOrderStateList() {
        List<DataDictionaryEntity> orderStateList = dataDictionaryService.selectList(new EntityWrapper<DataDictionaryEntity>().eq("data_type", "AMAZON_ORDER_STATE").orderBy(true, "data_sort", true));
        List<DataDictionaryEntity> abnormalStateList = dataDictionaryService.selectList(new EntityWrapper<DataDictionaryEntity>().eq("data_type", "ORDER_ABNORMAL_STATE").orderBy(true, "data_sort", true));
        //定义一个变量 全部的总和
        int allOrderCount = orderService.selectCount(new EntityWrapper<OrderEntity>().eq(getDeptId()!=1L,"dept_id",getDeptId()).eq("is_old",1));
        for (DataDictionaryEntity orderState : orderStateList) {
            int orderCount = orderService.selectCount(new EntityWrapper<OrderEntity>().eq("order_status",orderState.getDataNumber()).eq(getDeptId()!=1L,"dept_id",getDeptId()).eq("is_old",1));
            orderState.setCount(orderCount);
        }
        for (DataDictionaryEntity abnormalState : abnormalStateList) {
            int orderCount = orderService.selectCount(new EntityWrapper<OrderEntity>().eq("abnormal_status",abnormalState.getDataNumber()).eq(getDeptId()!=1L,"dept_id",getDeptId()).eq("is_old",1));
            abnormalState.setCount(orderCount);
        }
        orderStateList.addAll(abnormalStateList);
        return R.ok().put("orderStateList", orderStateList).put("allOrderCount", allOrderCount);
    }
    /**
     * @methodname: myOrderStateList 所有订单异常状态获取
     * @return: io.renren.common.utils.R
     * @auther: wdh
     * @date: 2018/12/3 10:02
     */
    @RequestMapping("/getAbnormalStateList")
    public R getAbnormalStateList() {
        DataDictionaryEntity normal = new DataDictionaryEntity();
        normal.setDataNumber("Normal");
        normal.setDataContent("正常");
        normal.setDataSort(10);
        List<DataDictionaryEntity> abnormalStateList = new ArrayList<>();
        abnormalStateList.add(normal);
        List<DataDictionaryEntity> abnormalStateList1 = dataDictionaryService.selectList(new EntityWrapper<DataDictionaryEntity>().eq("data_type", "ORDER_ABNORMAL_STATE").orderBy(true, "data_sort", true));
        if(getDeptId() != 1L || !userRoleService.isNotManager(getUserId()) ){
            for (int i = 0; i < abnormalStateList1.size(); i++){
                if(ConstantDictionary.OrderStateCode.ORDER_STATE_RETURN.equals(abnormalStateList1.get(i).getDataNumber())){
                    abnormalStateList1.remove(i);
                }
            }
        }
        abnormalStateList.addAll(abnormalStateList1);
        return R.ok().put("abnormalStateList", abnormalStateList);
    }
}