package io.renren.modules.logistics.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.logistics.entity.DomesticLogisticsEntity;

import java.util.Map;

/**
 * 
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-03 11:46:03
 */
public interface DomesticLogisticsService extends IService<DomesticLogisticsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

