package io.renren.modules.amazon.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.amazon.entity.AmazonMarketplaceEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-19 11:02:06
 */
public interface AmazonMarketplaceService extends IService<AmazonMarketplaceEntity> {

    PageUtils queryPage(Map<String, Object> params);

}

