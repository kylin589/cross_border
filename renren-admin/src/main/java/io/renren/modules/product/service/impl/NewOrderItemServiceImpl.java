package io.renren.modules.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.product.dao.NewOrderItemDao;
import io.renren.modules.product.entity.NewOrderItemEntity;
import io.renren.modules.product.service.NewOrderItemService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("newOrderItemService")
public class NewOrderItemServiceImpl extends ServiceImpl<NewOrderItemDao, NewOrderItemEntity> implements NewOrderItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<NewOrderItemEntity> page = this.selectPage(
                new Query<NewOrderItemEntity>(params).getPage(),
                new EntityWrapper<NewOrderItemEntity>()
        );

        return new PageUtils(page);
    }

}
