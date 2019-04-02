package io.renren.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.CookieLogEntity;

import java.util.Map;

/**
 * cookie表
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2019-01-11 01:41:22
 */
public interface CookieLogService extends IService<CookieLogEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 更新用户公司信息
     */
    void Calculation(Long userId,Long deptId);
}

