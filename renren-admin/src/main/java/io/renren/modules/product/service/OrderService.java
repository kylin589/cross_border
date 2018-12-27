package io.renren.modules.product.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.modules.product.entity.OrderEntity;
import io.renren.modules.product.vm.OrderModel;
import io.renren.modules.sys.dto.FranchiseeStatisticsDto;
import io.renren.modules.sys.dto.PlatformStatisticsDto;
import io.renren.modules.sys.dto.StatisticsDto;
import io.renren.modules.sys.dto.UserStatisticsDto;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.vm.StatisticsVM;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author wdh
 * @email wang-dehai@baizesoft.com
 * @date 2018-11-19 14:59:13
 */
public interface OrderService extends IService<OrderEntity> {


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

    /**
     * 修改状态
     */
    boolean updateState(Long orderId, String orderState);
    /**
     * 推送订单
     */
    Map<String,String> pushOrder(Long orderId);
    /**
     * 修改异常状态
     */
    boolean updateAbnormalState(Long[] orderIds, String abnormalStatus, String abnormalState);
    /**
     * 更新订单列表
     */
    void updateOrder(List<OrderModel> orderModelList);
    /**
     * 国际已发货
     * 扣款
     * 生成运费记录、服务费记录
     */
    void internationalShipments(OrderEntity order);
    /**
     * 扣款，并生成运费记录和服务费记录
     * @param order
     */
    void deduction(OrderEntity order);
    /**
     * 查询总部员工统计
     */
    UserStatisticsDto oneLevelUserStatistics(StatisticsVM vm);
    /**
     * 查询总部加盟商统计
     */
    FranchiseeStatisticsDto oneLevelFranchiseeStatistics(StatisticsVM vm);
    /**
     * 查询平台利润统计
     */
    PlatformStatisticsDto platformStatistics(StatisticsVM vm);

    /**
     * 根据订单id进行更新订单Amazon状态
     * @param AmazonOrderId
     * @return
     */
    OrderModel updateOrderAmazonStatus(String AmazonOrderId);
}

