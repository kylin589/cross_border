package io.renren.modules.product.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.product.entity.VariantParameterEntity;

import java.util.Map;

/**
 * 变体参数
 *
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
public interface VariantParameterService extends IService<VariantParameterEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

