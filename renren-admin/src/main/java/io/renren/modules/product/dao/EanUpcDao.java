package io.renren.modules.product.dao;

import io.renren.modules.product.entity.EanUpcEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * 
 * 
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-23 16:58:48
 */
public interface EanUpcDao extends BaseMapper<EanUpcEntity> {

    List<EanUpcEntity> selectByLimit(EanUpcEntity eanUpcEntity);

    //批量删除
    int selectMaxId();
}
