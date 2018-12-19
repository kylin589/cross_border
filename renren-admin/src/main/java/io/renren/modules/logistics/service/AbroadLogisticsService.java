package io.renren.modules.logistics.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.logistics.entity.AbroadLogisticsEntity;

import java.util.Map;

/**
 * 国际物流
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-18 23:28:12
 */
public interface AbroadLogisticsService extends IService<AbroadLogisticsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

