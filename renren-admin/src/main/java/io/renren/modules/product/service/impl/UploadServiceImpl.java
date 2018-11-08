package io.renren.modules.product.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.product.dao.UploadDao;
import io.renren.modules.product.entity.UploadEntity;
import io.renren.modules.product.service.UploadService;


@Service("uploadService")
public class UploadServiceImpl extends ServiceImpl<UploadDao, UploadEntity> implements UploadService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<UploadEntity> page = this.selectPage(
                new Query<UploadEntity>(params).getPage(),
                new EntityWrapper<UploadEntity>()
        );

        return new PageUtils(page);
    }

}
