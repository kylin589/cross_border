package io.renren.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.NoticeEntity;

import java.util.Map;

/**
 * 消息表
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-22 03:22:35
 */
public interface NoticeService extends IService<NoticeEntity> {

    PageUtils queryPage(Map<String, Object> params,Long userId);

}

