package io.renren.modules.product.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.product.dao.IntroductionDao;
import io.renren.modules.product.entity.IntroductionEntity;
import io.renren.modules.product.service.IntroductionService;


@Service("introductionService")
public class IntroductionServiceImpl extends ServiceImpl<IntroductionDao, IntroductionEntity> implements IntroductionService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<IntroductionEntity> page = this.selectPage(
                new Query<IntroductionEntity>(params).getPage(),
                new EntityWrapper<IntroductionEntity>()
        );

        return new PageUtils(page);
    }

}
