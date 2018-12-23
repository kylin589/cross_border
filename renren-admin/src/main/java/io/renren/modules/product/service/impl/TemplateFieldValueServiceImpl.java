package io.renren.modules.product.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.product.dao.TemplateFieldValueDao;
import io.renren.modules.product.entity.TemplateFieldValueEntity;
import io.renren.modules.product.service.TemplateFieldValueService;


@Service("templateFieldValueService")
public class TemplateFieldValueServiceImpl extends ServiceImpl<TemplateFieldValueDao, TemplateFieldValueEntity> implements TemplateFieldValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<TemplateFieldValueEntity> page = this.selectPage(
                new Query<TemplateFieldValueEntity>(params).getPage(),
                new EntityWrapper<TemplateFieldValueEntity>()
        );

        return new PageUtils(page);
    }

}
