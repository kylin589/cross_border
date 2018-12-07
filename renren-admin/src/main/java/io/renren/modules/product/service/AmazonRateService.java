package io.renren.modules.product.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.product.entity.AmazonRateEntity;

import java.util.Map;

/**
 * 汇率表
 *
 * @author jhy
 * @email 617493711@qq.com
 * @date 2018-12-07 14:47:29
 */
public interface AmazonRateService extends IService<AmazonRateEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

