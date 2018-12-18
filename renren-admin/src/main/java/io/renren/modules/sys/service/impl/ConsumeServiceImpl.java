package io.renren.modules.sys.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.sys.dao.ConsumeDao;
import io.renren.modules.sys.entity.ConsumeEntity;
import io.renren.modules.sys.service.ConsumeService;


@Service("consumeService")
public class ConsumeServiceImpl extends ServiceImpl<ConsumeDao, ConsumeEntity> implements ConsumeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String deptId = (String) params.get("deptId");
        String userId = (String)params.get("userId");
        String type = (String)params.get("type");
        String orderId = (String)params.get("orderId");
        String abroadWaybill = (String)params.get("abroadWaybill");
        Page<ConsumeEntity> page = this.selectPage(
                new Query<ConsumeEntity>(params).getPage(),
                new EntityWrapper<ConsumeEntity>().eq("dept_id",deptId)
                .eq(StringUtils.isNotBlank(userId),"user_id",userId)
                .eq(StringUtils.isNotBlank(orderId),"order_id",orderId)
                .eq(StringUtils.isNotBlank(type),"type",type)
                .eq(StringUtils.isNotBlank(abroadWaybill),"abroad_waybill",abroadWaybill)
        );

        return new PageUtils(page);
    }
}
