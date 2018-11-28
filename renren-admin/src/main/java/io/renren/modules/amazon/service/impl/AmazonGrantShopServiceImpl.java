package io.renren.modules.amazon.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.amazon.dao.AmazonGrantShopDao;
import io.renren.modules.amazon.entity.AmazonGrantShopEntity;
import io.renren.modules.amazon.service.AmazonGrantShopService;


@Service("amazonGrantShopService")
public class AmazonGrantShopServiceImpl extends ServiceImpl<AmazonGrantShopDao, AmazonGrantShopEntity> implements AmazonGrantShopService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<AmazonGrantShopEntity> page = this.selectPage(
                new Query<AmazonGrantShopEntity>(params).getPage(),
                new EntityWrapper<AmazonGrantShopEntity>()
        );

        return new PageUtils(page);
    }

}
