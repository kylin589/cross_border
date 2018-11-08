package io.renren.modules.product.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.product.entity.IntroductionEntity;

import java.util.Map;

/**
 * 国家介绍
 *
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
public interface IntroductionService extends IService<IntroductionEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

