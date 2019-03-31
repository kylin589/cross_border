package io.renren.modules.order.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.order.dao.NewProductShipAddressDao;
import io.renren.modules.order.dao.ProductShipAddressDao;
import io.renren.modules.order.entity.NewProductShipAddressEntity;
import io.renren.modules.order.entity.ProductShipAddressEntity;
import io.renren.modules.order.service.NewProductShipAddressService;
import io.renren.modules.order.service.ProductShipAddressService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("newproductShipAddressService")
public class NewProductShipAddressServiceImpl extends ServiceImpl<NewProductShipAddressDao, NewProductShipAddressEntity> implements NewProductShipAddressService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<NewProductShipAddressEntity> page = this.selectPage(
                new Query<NewProductShipAddressEntity>(params).getPage(),
                new EntityWrapper<NewProductShipAddressEntity>()
        );

        return new PageUtils(page);
    }

}
