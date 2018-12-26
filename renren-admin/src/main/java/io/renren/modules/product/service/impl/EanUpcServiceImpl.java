package io.renren.modules.product.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.product.dao.EanUpcDao;
import io.renren.modules.product.entity.EanUpcEntity;
import io.renren.modules.product.service.EanUpcService;


@Service("eanUpcService")
public class EanUpcServiceImpl extends ServiceImpl<EanUpcDao, EanUpcEntity> implements EanUpcService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<EanUpcEntity> page = this.selectPage(
                new Query<EanUpcEntity>(params).getPage(),
                new EntityWrapper<EanUpcEntity>().orderBy(true,"state",true).orderBy(true,"create_time",false)
        );

        return new PageUtils(page);
    }

}
