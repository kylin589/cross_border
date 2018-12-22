package io.renren.modules.amazon.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.amazon.dao.ResultXmlDao;
import io.renren.modules.amazon.entity.ResultXmlEntity;
import io.renren.modules.amazon.service.ResultXmlService;


@Service("resultXmlService")
public class ResultXmlServiceImpl extends ServiceImpl<ResultXmlDao, ResultXmlEntity> implements ResultXmlService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ResultXmlEntity> page = this.selectPage(
                new Query<ResultXmlEntity>(params).getPage(),
                new EntityWrapper<ResultXmlEntity>()
        );

        return new PageUtils(page);
    }

}
