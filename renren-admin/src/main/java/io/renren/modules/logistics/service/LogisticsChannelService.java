package io.renren.modules.logistics.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.logistics.entity.LogisticsChannelEntity;

import java.util.Map;

/**
 * 
 *
 * @author wangdh
 * @email 594340717@qq.com
 * @date 2019-03-31 09:38:56
 */
public interface LogisticsChannelService extends IService<LogisticsChannelEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

