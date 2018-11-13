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
    //审核状态分类
    List<DataDictionaryEntity> auditList();
    //上架状态分类
    List<DataDictionaryEntity> putawayList();
    //产品类型分类
    List<DataDictionaryEntity> productTypeList();

}

