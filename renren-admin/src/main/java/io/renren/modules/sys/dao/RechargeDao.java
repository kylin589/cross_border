package io.renren.modules.sys.dao;

import io.renren.modules.sys.entity.RechargeEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 
 * 
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-17 11:09:16
 */
public interface RechargeDao extends BaseMapper<RechargeEntity> {
    BigDecimal rechargeTotle(@Param("deptId")Long deptId);
}
