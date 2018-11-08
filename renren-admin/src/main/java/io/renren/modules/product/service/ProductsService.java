package io.renren.modules.product.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.product.entity.ProductsEntity;

import java.util.Map;

/**
 * 产品
 *
 * @author jhy
 * @email 617493711@qq.com
 * @date 2018-11-08 09:59:27
 */
public interface ProductsService extends IService<ProductsEntity> {

    PageUtils queryPage(Map<String, Object> params);

    int auditCount(String number);

    int putawayCount(String number);

    int productCount(String number);

    int count(Long id);

    int counts(Long id);
}

