package io.renren.modules.logistics.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.order.entity.RemarkEntity;
import io.renren.modules.order.service.RemarkService;
import io.renren.modules.product.entity.NewOrderEntity;
import io.renren.modules.product.service.NewOrderService;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.logistics.entity.NewOrderDomesticLogisticsEntity;
import io.renren.modules.logistics.service.NewOrderDomesticLogisticsService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 国内物流
 *
 * @author wangdh
 * @email 594340717@qq.com
 * @date 2019-04-01 19:38:40
 */
@RestController
@RequestMapping("amazon/neworderdomesticlogistics")
public class NewOrderDomesticLogisticsController extends AbstractController{
    @Autowired
    private NewOrderDomesticLogisticsService newOrderDomesticLogisticsService;
    @Autowired
    private NewOrderService newOrderService;
    @Autowired
    private RemarkService remarkService;

    /**
     * 新增
     */
    @RequestMapping("/addLogistics")
//    @RequiresPermissions("amazon:neworderdomesticlogistics:save")
    public R addLogistics(@RequestBody NewOrderDomesticLogisticsEntity newOrderDomesticLogistics){
        newOrderDomesticLogistics.setCreateTime(new Date());
        NewOrderEntity orderEntity = newOrderService.selectById(newOrderDomesticLogistics.getOrderId());
        if(StringUtils.isNotBlank(newOrderDomesticLogistics.getWaybill())){
            orderEntity.setDomesticWaybill(newOrderDomesticLogistics.getWaybill());
            newOrderDomesticLogistics.setIssuanceDate(new Date());
            if(orderEntity.getOrderStatus().equals("Shipped") || orderEntity.getOrderStatus().equals("Purchased")){
                orderEntity.setOrderStatus("InShipped");
                orderEntity.setOrderState("国内物流已发货");
            }
        }else{
            if(orderEntity.getOrderStatus().equals("Shipped") || orderEntity.getOrderStatus().equals("Purchased")){
                orderEntity.setOrderStatus("Purchased");
                orderEntity.setOrderState("国内物流已采购");
            }
        }
        newOrderDomesticLogisticsService.insert(newOrderDomesticLogistics);
        orderEntity.setPurchasePrice(orderEntity.getPurchasePrice().add(newOrderDomesticLogistics.getPrice()));
        newOrderService.updateById(orderEntity);
        RemarkEntity remark = new RemarkEntity();
        remark.setOrderId(newOrderDomesticLogistics.getOrderId());
        remark.setType("log");
        remark.setRemark("添加采购信息");
        remark.setUserId(getUserId());
        remark.setUserName(getUser().getDisplayName());
        remark.setUpdateTime(new Date());
        remarkService.insert(remark);
        return R.ok();
    }
    /**
     * 修改
     */
    @RequestMapping("/updateLogistics")
//    @RequiresPermissions("amazon:neworderdomesticlogistics:save")
    public R updateLogistics(@RequestBody NewOrderDomesticLogisticsEntity newOrderDomesticLogistics){
        newOrderDomesticLogistics.setCreateTime(new Date());
        NewOrderEntity orderEntity = newOrderService.selectById(newOrderDomesticLogistics.getOrderId());
        if(StringUtils.isNotBlank(newOrderDomesticLogistics.getWaybill())){
            orderEntity.setDomesticWaybill(newOrderDomesticLogistics.getWaybill());
            newOrderDomesticLogistics.setIssuanceDate(new Date());
            if(orderEntity.getOrderStatus().equals("Shipped") || orderEntity.getOrderStatus().equals("Purchased")){
                orderEntity.setOrderStatus("InShipped");
                orderEntity.setOrderState("国内物流已发货");
            }
        }
        newOrderDomesticLogisticsService.updateById(newOrderDomesticLogistics);
        List<NewOrderDomesticLogisticsEntity> list = newOrderDomesticLogisticsService.selectList(new EntityWrapper<NewOrderDomesticLogisticsEntity>().eq("order_id",newOrderDomesticLogistics.getOrderId()));
        BigDecimal price = new BigDecimal("0");
        for (NewOrderDomesticLogisticsEntity x : list){
            price = price.add(x.getPrice());
        }
        orderEntity.setPurchasePrice(price);
        newOrderService.updateById(orderEntity);
        RemarkEntity remark = new RemarkEntity();
        remark.setOrderId(newOrderDomesticLogistics.getOrderId());
        remark.setType("log");
        remark.setRemark("修改采购信息");
        remark.setUserId(getUserId());
        remark.setUserName(getUser().getDisplayName());
        remark.setUpdateTime(new Date());
        remarkService.insert(remark);
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("amazon:neworderdomesticlogistics:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = newOrderDomesticLogisticsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{domesticLogisticsId}")
    @RequiresPermissions("amazon:neworderdomesticlogistics:info")
    public R info(@PathVariable("domesticLogisticsId") Long domesticLogisticsId){
        NewOrderDomesticLogisticsEntity newOrderDomesticLogistics = newOrderDomesticLogisticsService.selectById(domesticLogisticsId);

        return R.ok().put("newOrderDomesticLogistics", newOrderDomesticLogistics);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("amazon:neworderdomesticlogistics:save")
    public R save(@RequestBody NewOrderDomesticLogisticsEntity newOrderDomesticLogistics){
        newOrderDomesticLogisticsService.insert(newOrderDomesticLogistics);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("amazon:neworderdomesticlogistics:update")
    public R update(@RequestBody NewOrderDomesticLogisticsEntity newOrderDomesticLogistics){
        ValidatorUtils.validateEntity(newOrderDomesticLogistics);
        newOrderDomesticLogisticsService.updateAllColumnById(newOrderDomesticLogistics);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("amazon:neworderdomesticlogistics:delete")
    public R delete(@RequestBody Long[] domesticLogisticsIds){
        newOrderDomesticLogisticsService.deleteBatchIds(Arrays.asList(domesticLogisticsIds));

        return R.ok();
    }

}
