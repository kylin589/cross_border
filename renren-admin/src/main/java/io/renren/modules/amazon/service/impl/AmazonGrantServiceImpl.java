package io.renren.modules.amazon.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.amazon.dao.AmazonGrantDao;
import io.renren.modules.amazon.entity.AmazonGrantEntity;
import io.renren.modules.amazon.service.AmazonGrantService;


@Service("amazonGrantService")
public class AmazonGrantServiceImpl extends ServiceImpl<AmazonGrantDao, AmazonGrantEntity> implements AmazonGrantService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<AmazonGrantEntity> page = this.selectPage(
                new Query<AmazonGrantEntity>(params).getPage(),
                new EntityWrapper<AmazonGrantEntity>()
        );

        return new PageUtils(page);
    }

}
