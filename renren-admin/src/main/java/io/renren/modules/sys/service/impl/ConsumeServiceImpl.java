package io.renren.modules.sys.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.sys.dao.ConsumeDao;
import io.renren.modules.sys.entity.ConsumeEntity;
import io.renren.modules.sys.service.ConsumeService;


@Service("consumeService")
public class ConsumeServiceImpl extends ServiceImpl<ConsumeDao, ConsumeEntity> implements ConsumeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ConsumeEntity> page = this.selectPage(
                new Query<ConsumeEntity>(params).getPage(),
                new EntityWrapper<ConsumeEntity>()
        );

        return new PageUtils(page);
    }

}
