package io.renren.modules.amazon.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.amazon.entity.ErrorCodeEntity;

import java.util.Map;

/**
 * 
 *
 * @author zjr
 * @email 1981763981@qq.com
 * @date 2019-01-09 14:18:08
 */
public interface ErrorCodeService extends IService<ErrorCodeEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

