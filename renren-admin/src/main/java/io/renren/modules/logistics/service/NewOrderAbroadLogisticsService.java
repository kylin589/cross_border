package io.renren.modules.logistics.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.logistics.entity.NewOrderAbroadLogisticsEntity;

import java.util.Map;

/**
 * 国际物流
 *
 * @author wangdh
 * @email 594340717@qq.com
 * @date 2019-03-28 19:50:52
 */
public interface NewOrderAbroadLogisticsService extends IService<NewOrderAbroadLogisticsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

