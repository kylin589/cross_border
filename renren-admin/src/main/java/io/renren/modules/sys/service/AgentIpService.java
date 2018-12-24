package io.renren.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.AgentIpEntity;

import java.util.Map;

/**
 * 
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-24 00:29:21
 */
public interface AgentIpService extends IService<AgentIpEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

