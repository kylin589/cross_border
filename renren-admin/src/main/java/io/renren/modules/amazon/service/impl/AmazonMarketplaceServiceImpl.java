package io.renren.modules.amazon.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.amazon.dao.AmazonMarketplaceDao;
import io.renren.modules.amazon.entity.AmazonMarketplaceEntity;
import io.renren.modules.amazon.service.AmazonMarketplaceService;


@Service("amazonMarketplaceService")
public class AmazonMarketplaceServiceImpl extends ServiceImpl<AmazonMarketplaceDao, AmazonMarketplaceEntity> implements AmazonMarketplaceService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<AmazonMarketplaceEntity> page = this.selectPage(
                new Query<AmazonMarketplaceEntity>(params).getPage(),
                new EntityWrapper<AmazonMarketplaceEntity>()
        );

        return new PageUtils(page);
    }

}
