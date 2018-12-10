package io.renren.modules.order.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.order.dao.ProductShipAddressDao;
import io.renren.modules.order.entity.ProductShipAddressEntity;
import io.renren.modules.order.service.ProductShipAddressService;


@Service("productShipAddressService")
public class ProductShipAddressServiceImpl extends ServiceImpl<ProductShipAddressDao, ProductShipAddressEntity> implements ProductShipAddressService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ProductShipAddressEntity> page = this.selectPage(
                new Query<ProductShipAddressEntity>(params).getPage(),
                new EntityWrapper<ProductShipAddressEntity>()
        );

        return new PageUtils(page);
    }

}
