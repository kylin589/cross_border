package io.renren.modules.logistics.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.logistics.entity.DomesticLogisticsEntity;

import java.util.Map;

/**
 * 国内物流
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-08 11:07:32
 */
public interface DomesticLogisticsService extends IService<DomesticLogisticsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

