package io.renren.modules.product.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.product.dao.ImageAddressDao;
import io.renren.modules.product.entity.ImageAddressEntity;
import io.renren.modules.product.service.ImageAddressService;


@Service("imageAddressService")
public class ImageAddressServiceImpl extends ServiceImpl<ImageAddressDao, ImageAddressEntity> implements ImageAddressService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ImageAddressEntity> page = this.selectPage(
                new Query<ImageAddressEntity>(params).getPage(),
                new EntityWrapper<ImageAddressEntity>()
        );

        return new PageUtils(page);
    }

}
