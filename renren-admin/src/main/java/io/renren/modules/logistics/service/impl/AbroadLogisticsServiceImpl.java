package io.renren.modules.logistics.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.logistics.dao.AbroadLogisticsDao;
import io.renren.modules.logistics.entity.AbroadLogisticsEntity;
import io.renren.modules.logistics.service.AbroadLogisticsService;


@Service("abroadLogisticsService")
public class AbroadLogisticsServiceImpl extends ServiceImpl<AbroadLogisticsDao, AbroadLogisticsEntity> implements AbroadLogisticsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<AbroadLogisticsEntity> page = this.selectPage(
                new Query<AbroadLogisticsEntity>(params).getPage(),
                new EntityWrapper<AbroadLogisticsEntity>()
        );

        return new PageUtils(page);
    }



}
