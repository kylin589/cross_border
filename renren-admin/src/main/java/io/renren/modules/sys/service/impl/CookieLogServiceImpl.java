package io.renren.modules.sys.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.sys.dao.CookieLogDao;
import io.renren.modules.sys.entity.CookieLogEntity;
import io.renren.modules.sys.service.CookieLogService;


@Service("cookieLogService")
public class CookieLogServiceImpl extends ServiceImpl<CookieLogDao, CookieLogEntity> implements CookieLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<CookieLogEntity> page = this.selectPage(
                new Query<CookieLogEntity>(params).getPage(),
                new EntityWrapper<CookieLogEntity>()
        );

        return new PageUtils(page);
    }

}
