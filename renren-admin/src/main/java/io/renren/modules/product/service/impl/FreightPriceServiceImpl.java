package io.renren.modules.product.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.product.dao.FreightPriceDao;
import io.renren.modules.product.entity.FreightPriceEntity;
import io.renren.modules.product.service.FreightPriceService;


@Service("freightPriceService")
public class FreightPriceServiceImpl extends ServiceImpl<FreightPriceDao, FreightPriceEntity> implements FreightPriceService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<FreightPriceEntity> page = this.selectPage(
                new Query<FreightPriceEntity>(params).getPage(),
                new EntityWrapper<FreightPriceEntity>()
        );

        return new PageUtils(page);
    }

}
