package io.renren.modules.logistics.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.logistics.dao.ItemCodeDao;
import io.renren.modules.logistics.entity.ItemCodeEntity;
import io.renren.modules.logistics.service.ItemCodeService;


@Service("itemCodeService")
public class ItemCodeServiceImpl extends ServiceImpl<ItemCodeDao, ItemCodeEntity> implements ItemCodeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ItemCodeEntity> page = this.selectPage(
                new Query<ItemCodeEntity>(params).getPage(),
                new EntityWrapper<ItemCodeEntity>()
        );

        return new PageUtils(page);
    }

}
