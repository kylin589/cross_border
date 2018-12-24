package io.renren.modules.product.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.product.entity.FieldMiddleEntity;

import java.util.Map;

/**
 * 
 *
 * @author zjr
 * @email 1981763981@qq.com
 * @date 2018-12-24 05:20:03
 */
public interface FieldMiddleService extends IService<FieldMiddleEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

