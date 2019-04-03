package io.renren.modules.sys.dao;

import io.renren.modules.sys.entity.ConsumeEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 消费记录
 * 
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-17 17:36:01
 */
public interface ConsumeDao extends BaseMapper<ConsumeEntity> {
    BigDecimal consumTotal(@Param("deptId")Long deptId);
}
