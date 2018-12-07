package io.renren.modules.product.dao;

import io.renren.modules.product.entity.OrderStatisticsEneity;
import io.renren.modules.product.entity.OrderEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 
 * 
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-19 14:59:13
 */
public interface OrderDao extends BaseMapper<OrderEntity> {
    int updateState(@Param("orderId")Long orderId, @Param("orderState")String orderState, @Param("orderStatus")String orderStatus);
    OrderStatisticsEneity statisticsOrderCounts(@Param("params") Map<String, Object> map);
}
