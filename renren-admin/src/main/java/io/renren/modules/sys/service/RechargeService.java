package io.renren.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.RechargeEntity;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-17 11:09:16
 */
public interface RechargeService extends IService<RechargeEntity> {

    PageUtils queryPage(Map<String, Object> params);

    BigDecimal rechargeTotle(Long deptId);
}

