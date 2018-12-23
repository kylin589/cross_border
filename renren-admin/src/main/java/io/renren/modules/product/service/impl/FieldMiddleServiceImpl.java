package io.renren.modules.product.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.product.dao.FieldMiddleDao;
import io.renren.modules.product.entity.FieldMiddleEntity;
import io.renren.modules.product.service.FieldMiddleService;


@Service("fieldMiddleService")
public class FieldMiddleServiceImpl extends ServiceImpl<FieldMiddleDao, FieldMiddleEntity> implements FieldMiddleService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<FieldMiddleEntity> page = this.selectPage(
                new Query<FieldMiddleEntity>(params).getPage(),
                new EntityWrapper<FieldMiddleEntity>()
        );

        return new PageUtils(page);
    }

}
