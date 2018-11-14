package io.renren.modules.product.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.product.dao.VariantsInfoDao;
import io.renren.modules.product.entity.VariantsInfoEntity;
import io.renren.modules.product.service.VariantsInfoService;


@Service("variantsInfoService")
public class VariantsInfoServiceImpl extends ServiceImpl<VariantsInfoDao, VariantsInfoEntity> implements VariantsInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<VariantsInfoEntity> page = this.selectPage(
                new Query<VariantsInfoEntity>(params).getPage(),
                new EntityWrapper<VariantsInfoEntity>()
        );

        return new PageUtils(page);
    }

}
