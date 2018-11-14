package io.renren.modules.product.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.product.entity.FreightCostEntity;

import java.util.Map;

/**
 * 物流成本
 *
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
public interface FreightCostService extends IService<FreightCostEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

