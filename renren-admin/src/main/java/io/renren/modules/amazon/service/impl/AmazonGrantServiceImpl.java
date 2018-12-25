package io.renren.modules.amazon.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.amazon.dao.AmazonGrantDao;
import io.renren.modules.amazon.entity.AmazonGrantEntity;
import io.renren.modules.amazon.service.AmazonGrantService;


@Service("amazonGrantService")
public class AmazonGrantServiceImpl extends ServiceImpl<AmazonGrantDao, AmazonGrantEntity> implements AmazonGrantService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<AmazonGrantEntity> page = this.selectPage(
                new Query<AmazonGrantEntity>(params).getPage(),
                new EntityWrapper<AmazonGrantEntity>().eq("user_id",params.get("userId"))
        );
        List<AmazonGrantEntity> list = page.getRecords();
        for(AmazonGrantEntity amazonGrantEntity : list){
            //开户区域(0：北美、1：欧洲、2：日本、3：澳大利亚)
            if(amazonGrantEntity.getRegion() == 0){
                amazonGrantEntity.setGrantCountry("美国/加拿大/墨西哥");
            }else if(amazonGrantEntity.getRegion() == 1){
                amazonGrantEntity.setGrantCountry("英国/法国/德国/西班牙/意大利");
            }else if(amazonGrantEntity.getRegion() == 2){
                amazonGrantEntity.setGrantCountry("日本");
            }else{
                amazonGrantEntity.setGrantCountry("澳大利亚");
            }
        }
        return new PageUtils(page);
    }

}
