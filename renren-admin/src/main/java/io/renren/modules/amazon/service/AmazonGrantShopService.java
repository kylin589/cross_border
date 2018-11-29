package io.renren.modules.amazon.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.amazon.entity.AmazonGrantShopEntity;

import java.util.Map;

/**
 * 
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-11-27 11:00:50
 */
public interface AmazonGrantShopService extends IService<AmazonGrantShopEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

