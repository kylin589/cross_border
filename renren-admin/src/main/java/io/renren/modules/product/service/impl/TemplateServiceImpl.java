package io.renren.modules.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.product.dao.TemplateDao;
import io.renren.modules.product.entity.TemplateEntity;
import io.renren.modules.product.service.TemplateService;


@Service("templateService")
public class TemplateServiceImpl extends ServiceImpl<TemplateDao, TemplateEntity> implements TemplateService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<TemplateEntity> page = this.selectPage(
                new Query<TemplateEntity>(params).getPage(),
                new EntityWrapper<TemplateEntity>()
        );

        return new PageUtils(page);
    }

}
