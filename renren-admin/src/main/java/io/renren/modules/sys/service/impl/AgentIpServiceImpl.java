package io.renren.modules.sys.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.sys.dao.AgentIpDao;
import io.renren.modules.sys.entity.AgentIpEntity;
import io.renren.modules.sys.service.AgentIpService;


@Service("agentIpService")
public class AgentIpServiceImpl extends ServiceImpl<AgentIpDao, AgentIpEntity> implements AgentIpService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<AgentIpEntity> page = this.selectPage(
                new Query<AgentIpEntity>(params).getPage(),
                new EntityWrapper<AgentIpEntity>()
        );

        return new PageUtils(page);
    }

}
