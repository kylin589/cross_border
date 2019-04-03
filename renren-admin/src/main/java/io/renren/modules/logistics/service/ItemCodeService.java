package io.renren.modules.logistics.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.logistics.entity.ItemCodeEntity;

import java.util.Map;

/**
 * 
 *
 * @author wangdh
 * @email 594340717@qq.com
 * @date 2019-04-03 15:39:33
 */
public interface ItemCodeService extends IService<ItemCodeEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

