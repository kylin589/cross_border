package io.renren.modules.product.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.product.entity.EanUpcEntity;

import java.util.Map;

/**
 * 
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-23 16:58:48
 */
public interface EanUpcService extends IService<EanUpcEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

