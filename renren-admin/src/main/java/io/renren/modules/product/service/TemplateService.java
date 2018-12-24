package io.renren.modules.product.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.product.entity.TemplateEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zjr
 * @email 1981763981@qq.com
 * @date 2018-12-23 23:19:41
 */
public interface TemplateService extends IService<TemplateEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

