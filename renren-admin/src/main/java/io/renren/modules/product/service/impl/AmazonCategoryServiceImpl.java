package io.renren.modules.product.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.product.dao.AmazonCategoryDao;
import io.renren.modules.product.entity.AmazonCategoryEntity;
import io.renren.modules.product.service.AmazonCategoryService;


@Service("amazonCategoryService")
public class AmazonCategoryServiceImpl extends ServiceImpl<AmazonCategoryDao, AmazonCategoryEntity> implements AmazonCategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<AmazonCategoryEntity> page = this.selectPage(
                new Query<AmazonCategoryEntity>(params).getPage(),
                new EntityWrapper<AmazonCategoryEntity>()
        );

        return new PageUtils(page);
    }

}
