package io.renren.modules.logistics.service.impl;

import io.renren.modules.logistics.dao.LogisticsChannelDao;
import io.renren.modules.logistics.entity.LogisticsChannelEntity;
import io.renren.modules.logistics.service.LogisticsChannelService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;




@Service("logisticsChannelService")
public class LogisticsChannelServiceImpl extends ServiceImpl<LogisticsChannelDao, LogisticsChannelEntity> implements LogisticsChannelService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<LogisticsChannelEntity> page = this.selectPage(
                new Query<LogisticsChannelEntity>(params).getPage(),
                new EntityWrapper<LogisticsChannelEntity>()
        );

        return new PageUtils(page);
    }

}
