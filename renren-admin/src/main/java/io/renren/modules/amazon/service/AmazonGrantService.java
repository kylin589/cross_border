package io.renren.modules.amazon.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.amazon.entity.AmazonGrantEntity;

import java.util.Map;

/**
 * 
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-11-27 10:43:39
 */
public interface AmazonGrantService extends IService<AmazonGrantEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

