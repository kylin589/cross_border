package io.renren.modules.logistics.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.logistics.dao.NewOrderItemRelationshipDao;
import io.renren.modules.logistics.entity.NewOrderItemRelationshipEntity;
import io.renren.modules.logistics.service.NewOrderItemRelationshipService;


@Service("newOrderItemRelationshipService")
public class NewOrderItemRelationshipServiceImpl extends ServiceImpl<NewOrderItemRelationshipDao, NewOrderItemRelationshipEntity> implements NewOrderItemRelationshipService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<NewOrderItemRelationshipEntity> page = this.selectPage(
                new Query<NewOrderItemRelationshipEntity>(params).getPage(),
                new EntityWrapper<NewOrderItemRelationshipEntity>()
        );

        return new PageUtils(page);
    }

}
