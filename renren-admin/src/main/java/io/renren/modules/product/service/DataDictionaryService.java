package io.renren.modules.product.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.product.entity.DataDictionaryEntity;

import java.util.List;
import java.util.Map;

/**
 * 数据字典
 *
 * @author jhy
 * @email 617493711@qq.com
 * @date 2018-11-08 09:59:28
 */
public interface DataDictionaryService extends IService<DataDictionaryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<DataDictionaryEntity> auditList();

    List<DataDictionaryEntity> putawayList();

    List<DataDictionaryEntity> productList();

}

