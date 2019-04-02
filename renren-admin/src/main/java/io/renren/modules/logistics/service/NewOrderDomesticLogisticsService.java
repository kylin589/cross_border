package io.renren.modules.logistics.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.logistics.entity.NewOrderDomesticLogisticsEntity;

import java.util.Map;

/**
 * 国内物流
 *
 * @author wangdh
 * @email 594340717@qq.com
 * @date 2019-04-01 19:38:40
 */
public interface NewOrderDomesticLogisticsService extends IService<NewOrderDomesticLogisticsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

