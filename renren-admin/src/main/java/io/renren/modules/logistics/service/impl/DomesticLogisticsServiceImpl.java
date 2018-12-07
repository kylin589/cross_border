package io.renren.modules.logistics.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.logistics.dao.DomesticLogisticsDao;
import io.renren.modules.logistics.entity.DomesticLogisticsEntity;
import io.renren.modules.logistics.service.DomesticLogisticsService;


@Service("domesticLogisticsService")
public class DomesticLogisticsServiceImpl extends ServiceImpl<DomesticLogisticsDao, DomesticLogisticsEntity> implements DomesticLogisticsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DomesticLogisticsEntity> page = this.selectPage(
                new Query<DomesticLogisticsEntity>(params).getPage(),
                new EntityWrapper<DomesticLogisticsEntity>()
        );

        return new PageUtils(page);
    }

}
