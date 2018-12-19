package io.renren.modules.product.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.product.entity.ClaimEntity;

import java.util.Map;

/**
 * 产品认领记录表
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-19 00:26:46
 */
public interface ClaimService extends IService<ClaimEntity> {

    PageUtils queryPage(Map<String, Object> params,Long deptId);
}

