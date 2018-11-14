package io.renren.modules.product.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.product.entity.VariantsInfoEntity;

import java.util.Map;

/**
 * 
 *
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
public interface VariantsInfoService extends IService<VariantsInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

