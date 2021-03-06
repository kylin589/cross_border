package io.renren.modules.product.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.product.entity.NewOrderEntity;
import io.renren.modules.product.entity.OrderStatisticsEntity;
import io.renren.modules.sys.dto.FranchiseeStatisticsDto;
import io.renren.modules.sys.dto.UserStatisticsDto;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 新订单表
 *
 * @author wangdh
 * @email 594340717@qq.com
 * @date 2019-03-28 14:50:57
 */
public interface NewOrderDao extends BaseMapper<NewOrderEntity> {
    /**
     * 修改状态
     * @param orderId
     * @param orderState
     * @param orderStatus
     * @return
     */
    int updateState(@Param("orderId")Long orderId, @Param("orderState")String orderState, @Param("orderStatus")String orderStatus);

    /**
     * 修改异常状态
     * @param orderIds
     * @param abnormalState
     * @param abnormalStatus
     * @return
     */
    int updateAbnormalState(@Param("orderIds")Long[] orderIds, @Param("abnormalState")String abnormalState, @Param("abnormalStatus")String abnormalStatus);

    /**
     * 订单统计
     * @param map
     * @return
     */
    OrderStatisticsEntity statisticsOrderCounts(@Param("params") Map<String, Object> map);

    /**
     * 总部员工统计
     * @param map
     * @return
     */
    UserStatisticsDto userStatistics(@Param("params") Map<String, Object> map);

    /**
     * 总销售额统计
     * @param map
     * @return
     */
    String salesVolumeStatistics(@Param("params") Map<String, Object> map);

    /**
     * 加盟商统计
     * @param map
     * @return
     */
    FranchiseeStatisticsDto franchiseeStatistics(@Param("params") Map<String, Object> map);

    /**
     * 平台利润统计——所有加盟商抽成统计
     * @param map
     * @return
     */
    String chouchengStatistics(@Param("params") Map<String, Object> map);

    /**
     * 平台利润统计——总部员工销售利润
     * @param map
     * @return
     */
    String hqUserProfit(@Param("params") Map<String, Object> map);
}
