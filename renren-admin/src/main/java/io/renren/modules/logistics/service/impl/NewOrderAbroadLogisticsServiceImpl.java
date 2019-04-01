package io.renren.modules.logistics.service.impl;

import io.renren.modules.logistics.dao.NewOrderAbroadLogisticsDao;
import io.renren.modules.logistics.entity.NewOrderAbroadLogisticsEntity;
import io.renren.modules.logistics.service.NewOrderAbroadLogisticsService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;



@Service("newOrderAbroadLogisticsService")
public class NewOrderAbroadLogisticsServiceImpl extends ServiceImpl<NewOrderAbroadLogisticsDao, NewOrderAbroadLogisticsEntity> implements NewOrderAbroadLogisticsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<NewOrderAbroadLogisticsEntity> page = this.selectPage(
                new Query<NewOrderAbroadLogisticsEntity>(params).getPage(),
                new EntityWrapper<NewOrderAbroadLogisticsEntity>()
        );

        return new PageUtils(page);
    }

}
