package io.renren.modules.product.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.product.dao.VariantParameterDao;
import io.renren.modules.product.entity.VariantParameterEntity;
import io.renren.modules.product.service.VariantParameterService;


@Service("variantParameterService")
public class VariantParameterServiceImpl extends ServiceImpl<VariantParameterDao, VariantParameterEntity> implements VariantParameterService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<VariantParameterEntity> page = this.selectPage(
                new Query<VariantParameterEntity>(params).getPage(),
                new EntityWrapper<VariantParameterEntity>()
        );

        return new PageUtils(page);
    }

}
