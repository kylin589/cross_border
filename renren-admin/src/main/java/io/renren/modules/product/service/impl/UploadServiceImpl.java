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
    public PageUtils queryMyUploadPage(Map<String, Object> params) {
        Long userId = (Long) params.get("userId");
        Page<UploadEntity> page = this.selectPage(
                new Query<UploadEntity>(params).getPage(),
                new EntityWrapper<UploadEntity>().eq("user_id", userId).orderBy("update_time", false)
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryAllUploadPage(Map<String, Object> params) {
        Long deptId = (Long) params.get("userId");
        EntityWrapper<UploadEntity> wrapper = new EntityWrapper<UploadEntity>();
        if (deptId != 1) {
            wrapper.eq("dept_id", deptId).orderBy("update_time", false);
        }
        Page<UploadEntity> page = this.selectPage(
                new Query<UploadEntity>(params).getPage(),
                wrapper
        );

        return new PageUtils(page);
    }
}
