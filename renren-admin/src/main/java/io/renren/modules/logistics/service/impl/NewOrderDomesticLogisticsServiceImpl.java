package io.renren.modules.logistics.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.logistics.dao.NewOrderDomesticLogisticsDao;
import io.renren.modules.logistics.entity.NewOrderDomesticLogisticsEntity;
import io.renren.modules.logistics.service.NewOrderDomesticLogisticsService;


@Service("newOrderDomesticLogisticsService")
public class NewOrderDomesticLogisticsServiceImpl extends ServiceImpl<NewOrderDomesticLogisticsDao, NewOrderDomesticLogisticsEntity> implements NewOrderDomesticLogisticsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<NewOrderDomesticLogisticsEntity> page = this.selectPage(
                new Query<NewOrderDomesticLogisticsEntity>(params).getPage(),
                new EntityWrapper<NewOrderDomesticLogisticsEntity>()
        );

        return new PageUtils(page);
    }

}
