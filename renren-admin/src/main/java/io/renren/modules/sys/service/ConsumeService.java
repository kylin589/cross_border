package io.renren.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.ConsumeEntity;

import java.util.Map;

/**
 * 消费记录
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-17 17:36:01
 */
public interface ConsumeService extends IService<ConsumeEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

