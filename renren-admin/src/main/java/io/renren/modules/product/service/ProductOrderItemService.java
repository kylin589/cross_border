package io.renren.modules.product.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.product.entity.ProductOrderItemEntity;

import java.util.Map;

/**
 * 
 *
 * @author wangdh
 * @email 594340717@qq.com
 * @date 2019-02-23 09:12:26
 */
public interface ProductOrderItemService extends IService<ProductOrderItemEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

