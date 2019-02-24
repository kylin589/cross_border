package io.renren.modules.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.product.dao.ProductOrderItemDao;
import io.renren.modules.product.entity.ProductOrderItemEntity;
import io.renren.modules.product.service.ProductOrderItemService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("productOrderItemService")
public class ProductOrderItemServiceImpl extends ServiceImpl<ProductOrderItemDao, ProductOrderItemEntity> implements ProductOrderItemService {


    public PageUtils queryPage(Map<String, Object> params) {
        Page<ProductOrderItemEntity> page = this.selectPage(
                new Query<ProductOrderItemEntity>(params).getPage(),
                new EntityWrapper<ProductOrderItemEntity>()
        );

        return new PageUtils(page);
    }

}
