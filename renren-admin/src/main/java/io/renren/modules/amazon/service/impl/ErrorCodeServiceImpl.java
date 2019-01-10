package io.renren.modules.amazon.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.amazon.dao.ErrorCodeDao;
import io.renren.modules.amazon.entity.ErrorCodeEntity;
import io.renren.modules.amazon.service.ErrorCodeService;


@Service("errorCodeService")
public class ErrorCodeServiceImpl extends ServiceImpl<ErrorCodeDao, ErrorCodeEntity> implements ErrorCodeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String errorCode = (String) params.get("errorCode");
        Page<ErrorCodeEntity> page = this.selectPage(
                new Query<ErrorCodeEntity>(params).getPage(),
                new EntityWrapper<ErrorCodeEntity>().like("error_code",errorCode)
        );

        return new PageUtils(page);
    }

}
