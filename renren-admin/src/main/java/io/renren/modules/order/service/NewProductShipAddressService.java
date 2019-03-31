package io.renren.modules.order.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.order.entity.NewProductShipAddressEntity;
import io.renren.modules.order.entity.ProductShipAddressEntity;

import java.util.Map;

/**
 * 订单配送地址表
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-10 10:16:27
 */
public interface NewProductShipAddressService extends IService<NewProductShipAddressEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

