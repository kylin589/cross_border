package io.renren.modules.product.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.modules.product.entity.OrderEntity;
import io.renren.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
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
     * 修改异常状态
     */
    boolean updateAbnormalState(Long[] orderIds, String abnormalStatus, String abnormalState);
    /**
     * 取消订单
     */
    void cancleOrder(String amazonOrderId);
    /**
     * 更新订单列表
     */
    void updateOrder(SysUserEntity user, List<OrderEntity> objList);
}

