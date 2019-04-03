package io.renren.modules.sys.service.impl;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.sys.dao.RechargeDao;
import io.renren.modules.sys.entity.RechargeEntity;
import io.renren.modules.sys.service.RechargeService;


@Service("rechargeService")
public class RechargeServiceImpl extends ServiceImpl<RechargeDao, RechargeEntity> implements RechargeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String deptId = (String) params.get("deptId");
        Page<RechargeEntity> page = this.selectPage(
                new Query<RechargeEntity>(params).getPage(),
                new EntityWrapper<RechargeEntity>().eq("dept_id",deptId)
        );
        return new PageUtils(page);
    }

    @Override
    public BigDecimal rechargeTotle(Long deptId) {
        return baseMapper.rechargeTotle(deptId);
    }

}
