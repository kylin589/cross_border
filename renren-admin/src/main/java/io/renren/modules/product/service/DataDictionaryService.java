package io.renren.modules.product.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.product.entity.DataDictionaryEntity;

import java.util.Map;

/**
 * 数据字典
 *
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:46
 */
public interface DataDictionaryService extends IService<DataDictionaryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

