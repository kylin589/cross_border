package io.renren.modules.amazon.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.amazon.entity.AmazonCategoryHistoryEntity;

import java.util.Map;

/**
 * 
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-11-27 16:33:14
 */
public interface AmazonCategoryHistoryService extends IService<AmazonCategoryHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    AmazonCategoryHistoryEntity selectByAmazonCategoryId(Long amazonCategoryId);
}

