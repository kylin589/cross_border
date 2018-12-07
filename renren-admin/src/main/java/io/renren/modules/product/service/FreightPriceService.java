package io.renren.modules.product.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.product.entity.FreightPriceEntity;

import java.util.Map;

/**
 * 物流单价表
 *
 * @author jhy
 * @email 617493711@qq.com
 * @date 2018-12-07 14:47:30
 */
public interface FreightPriceService extends IService<FreightPriceEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

