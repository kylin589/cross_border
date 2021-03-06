package io.renren.modules.product.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.product.entity.AmazonTemplateEntity;

import java.util.Map;

/**
 * 亚马逊分类表
 *
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
public interface AmazonTemplateService extends IService<AmazonTemplateEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

