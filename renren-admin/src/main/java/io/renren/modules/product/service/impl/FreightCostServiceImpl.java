package io.renren.modules.product.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.product.dao.FreightCostDao;
import io.renren.modules.product.entity.FreightCostEntity;
import io.renren.modules.product.service.FreightCostService;


@Service("freightCostService")
public class FreightCostServiceImpl extends ServiceImpl<FreightCostDao, FreightCostEntity> implements FreightCostService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<FreightCostEntity> page = this.selectPage(
                new Query<FreightCostEntity>(params).getPage(),
                new EntityWrapper<FreightCostEntity>()
        );

        return new PageUtils(page);
    }

}
