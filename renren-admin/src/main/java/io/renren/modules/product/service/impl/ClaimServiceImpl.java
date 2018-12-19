package io.renren.modules.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.product.dao.ClaimDao;
import io.renren.modules.product.entity.ClaimEntity;
import io.renren.modules.product.service.ClaimService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("claimService")
public class ClaimServiceImpl extends ServiceImpl<ClaimDao, ClaimEntity> implements ClaimService {

    @Override
    public PageUtils queryPage(Map<String, Object> params,Long deptId) {
        Page<ClaimEntity> page = this.selectPage(
                new Query<ClaimEntity>(params).getPage(),
                new EntityWrapper<ClaimEntity>().eq(deptId != 1L,"operator_dept_id",deptId)
        );

        return new PageUtils(page);
    }

}
