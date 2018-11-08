package io.renren.modules.product.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.product.dao.AmazonTemplateDao;
import io.renren.modules.product.entity.AmazonTemplateEntity;
import io.renren.modules.product.service.AmazonTemplateService;


@Service("amazonTemplateService")
public class AmazonTemplateServiceImpl extends ServiceImpl<AmazonTemplateDao, AmazonTemplateEntity> implements AmazonTemplateService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<AmazonTemplateEntity> page = this.selectPage(
                new Query<AmazonTemplateEntity>(params).getPage(),
                new EntityWrapper<AmazonTemplateEntity>()
        );

        return new PageUtils(page);
    }

}
