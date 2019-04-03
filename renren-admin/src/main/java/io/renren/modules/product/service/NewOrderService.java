package io.renren.modules.product.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.logistics.entity.NewOrderAbroadLogisticsEntity;
import io.renren.modules.logistics.entity.SendDataMoedl;
import io.renren.modules.logistics.util.NewAbroadLogisticsUtil;
import io.renren.modules.product.entity.NewOrderEntity;
import io.renren.modules.product.entity.OrderEntity;
import io.renren.modules.product.vm.OrderModel;
import io.renren.modules.sys.dto.FranchiseeStatisticsDto;
import io.renren.modules.sys.dto.PlatformStatisticsDto;
import io.renren.modules.sys.dto.UserStatisticsDto;
import io.renren.modules.sys.vm.StatisticsVM;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 新订单表
 *
 * @author wangdh
 * @email 594340717@qq.com
 * @date 2019-03-28 14:50:57
 */
public interface NewOrderService extends IService<NewOrderEntity> {
    /**
     * 我的订单
     * @param params
     * @param userId 用户id
     * @return
     */
    Map<String, Object> queryMyPage(Map<String, Object> params, Long userId);

    /**
     * 所有订单
     * @param params
     * @param deptId 公司id
     * @return
     */
    Map<String, Object> queryAllPage(Map<String, Object> params, Long deptId);

    PageUtils queryNewAllPage(Map<String, Object> params);
    /**
     * 修改状态
     */
    boolean updateState(Long orderId, String orderState);


    /**
     * 修改异常状态
     */
    boolean updateAbnormalState(Long[] orderIds, String abnormalStatus, String abnormalState);

    /**
     * 更新订单列表
     */
//    void updateOrder(List<OrderModel> orderModelList);
    /**
     * 获取以往订单
     */
//    void updateOldOrder(List<OrderModel> orderModelList);
    /**
     * 国际已发货
     * 扣款
     * 生成运费记录、服务费记录
     */
    void internationalShipments(NewOrderEntity neworderEntity);

    /**
     * 生成服务费记录
     * @param
     */
    void deductionFW(NewOrderEntity neworderEntity);
    /**
     * 生成服务费记录
     * @param
     */
    void deductionYF(NewOrderAbroadLogisticsEntity newOrderAbroadLogisticsEntity);

    /**
     * 计算服务费
     */
    void serviceFee(NewOrderEntity newOrderEntity);

    /**
     * 查询总部员工统计
     */
//    UserStatisticsDto oneLevelUserStatistics(StatisticsVM vm);

    /**
     * 查询总部加盟商统计
     */
//    FranchiseeStatisticsDto oneLevelFranchiseeStatistics(StatisticsVM vm);

    /**
     * 查询平台利润统计
     */
//    PlatformStatisticsDto platformStatistics(StatisticsVM vm);

    /**
     * 根据订单id进行更新订单Amazon状态
     * @param AmazonOrderId
     * @return
     */
//    OrderModel updateOrderAmazonStatus(String AmazonOrderId, NewOrderEntity neworderEntity);

    /**
     * 同步物流订单号的方法
     * @param sendDataMoedl
     * @param orderId
     */
/*
    void amazonUpdateLogistics(SendDataMoedl sendDataMoedl, Long orderId);
*/

    /**
     * amazon状态更新
     */
//    void RefreshAmazonState(NewOrderEntity neworderEntity, OrderModel orderModel);

    /**
     * 国际物流更新
     */
//    void RefreshOrder(Long orderId);


    /**
     * 推送订单
     */
    Map<String,String> pushOrder(String orderNumber,String  amazonOrderId, int packageType, String channelCode, String channelName, String englishName, int length, int width, int height, BigDecimal weight);

    /**
     * 删除订单
     */
    Map<String,String> DeleteOrder(String type,String wayBill);

    /**
     * 打印快递单号
     */
    Map<String,String> printOrder(String orderNumber);


    /**
     * 获取保存推送订单信息
     */
    Map<String,String>  getShippingFeeDetail(String orderNumber);

    /*
     * 获取运输代码
     */
    List<String>  getShippingMethodCode(int type);

    /**
     * 三态推送订单
     */
     Map<String,String> pushOrder(String  customerOrderNo,String amazonOrderId,int shipperAddressType,String shippingMethod);


    /**
     * 三态订单状态修改（包含作废）
     */
    Map<String,String> updateOrder(String orderCode,String orderStatus);

    String print(String orderID,int printType,String print_type,int printSize,int printSort);

}

