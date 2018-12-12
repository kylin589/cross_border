package io.renren.modules.order.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.order.dao.RemarkDao;
import io.renren.modules.order.entity.RemarkEntity;
import io.renren.modules.order.service.RemarkService;


@Service("remarkService")
public class RemarkServiceImpl extends ServiceImpl<RemarkDao, RemarkEntity> implements RemarkService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<RemarkEntity> page = this.selectPage(
                new Query<RemarkEntity>(params).getPage(),
                new EntityWrapper<RemarkEntity>()
        );

        return new PageUtils(page);
    }

}
