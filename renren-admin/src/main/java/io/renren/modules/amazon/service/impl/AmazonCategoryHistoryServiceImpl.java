package io.renren.modules.amazon.service.impl;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.amazon.dao.AmazonCategoryHistoryDao;
import io.renren.modules.amazon.entity.AmazonCategoryHistoryEntity;
import io.renren.modules.amazon.service.AmazonCategoryHistoryService;


@Service("amazonCategoryHistoryService")
public class AmazonCategoryHistoryServiceImpl extends ServiceImpl<AmazonCategoryHistoryDao, AmazonCategoryHistoryEntity> implements AmazonCategoryHistoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Long userId = (Long)params.get("userId");
        Page<AmazonCategoryHistoryEntity> page = this.selectPage(
                new Query<AmazonCategoryHistoryEntity>(params).getPage(),
                new EntityWrapper<AmazonCategoryHistoryEntity>().eq("user_id",userId)
        );

        return new PageUtils(page);
    }

    @Override
    public AmazonCategoryHistoryEntity selectByAmazonCategoryId(Long amazonCategoryId) {
        List<AmazonCategoryHistoryEntity> list = new ArrayList<AmazonCategoryHistoryEntity>();
        list = this.selectList(new EntityWrapper<AmazonCategoryHistoryEntity>().eq("amazon_category_id",amazonCategoryId));
        if(list.size() != 0){
            return list.get(0);
        }
        return null;
    }

}
