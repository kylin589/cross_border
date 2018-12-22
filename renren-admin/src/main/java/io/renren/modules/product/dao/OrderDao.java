package io.renren.modules.product.dao;

import io.renren.modules.product.entity.OrderStatisticsEntity;
import io.renren.modules.product.entity.OrderEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.sys.dto.UserStatisticsDto;
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
    int updateAbnormalState(@Param("orderIds")Long[] orderIds, @Param("abnormalState")String abnormalState, @Param("abnormalStatus")String abnormalStatus);
    OrderStatisticsEntity statisticsOrderCounts(@Param("params") Map<String, Object> map);
    UserStatisticsDto oneLevelUserStatistics1(@Param("params") Map<String, Object> map);
    String oneLevelUserStatistics2(@Param("params") Map<String, Object> map);
}
