package io.renren.modules.product.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.product.dao.TemplateCategoryFieldsDao;
import io.renren.modules.product.entity.TemplateCategoryFieldsEntity;
import io.renren.modules.product.service.TemplateCategoryFieldsService;


@Service("templateCategoryFieldsService")
public class TemplateCategoryFieldsServiceImpl extends ServiceImpl<TemplateCategoryFieldsDao, TemplateCategoryFieldsEntity> implements TemplateCategoryFieldsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<TemplateCategoryFieldsEntity> page = this.selectPage(
                new Query<TemplateCategoryFieldsEntity>(params).getPage(),
                new EntityWrapper<TemplateCategoryFieldsEntity>()
        );

        return new PageUtils(page);
    }

}
