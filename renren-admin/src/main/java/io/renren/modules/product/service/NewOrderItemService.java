package io.renren.modules.product.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.product.entity.NewOrderItemEntity;

import java.util.Map;

/**
 * 新子订单表
 *
 * @author wangdh
 * @email 594340717@qq.com
 * @date 2019-03-28 14:50:57
 */
public interface NewOrderItemService extends IService<NewOrderItemEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

