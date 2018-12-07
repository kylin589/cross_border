package io.renren.modules.product.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.product.dao.AmazonRateDao;
import io.renren.modules.product.entity.AmazonRateEntity;
import io.renren.modules.product.service.AmazonRateService;


@Service("amazonRateService")
public class AmazonRateServiceImpl extends ServiceImpl<AmazonRateDao, AmazonRateEntity> implements AmazonRateService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<AmazonRateEntity> page = this.selectPage(
                new Query<AmazonRateEntity>(params).getPage(),
                new EntityWrapper<AmazonRateEntity>()
        );

        return new PageUtils(page);
    }

}
