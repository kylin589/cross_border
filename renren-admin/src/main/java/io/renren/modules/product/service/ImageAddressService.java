package io.renren.modules.product.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.product.entity.ImageAddressEntity;

import java.util.Map;

/**
 * 产品图片表
 *
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
public interface ImageAddressService extends IService<ImageAddressEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

