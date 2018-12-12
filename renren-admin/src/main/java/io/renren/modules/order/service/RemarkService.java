package io.renren.modules.order.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.order.entity.RemarkEntity;

import java.util.Map;

/**
 * 订单备注、操作日志表
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-11 16:32:11
 */
public interface RemarkService extends IService<RemarkEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

