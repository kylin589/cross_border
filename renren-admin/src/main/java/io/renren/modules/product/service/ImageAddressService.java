package io.renren.modules.product.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.product.entity.ImageAddressEntity;

import java.util.Map;

/**
 * 产品图片表
 *
 * @author jhy
 * @email 617493711@qq.com
 * @date 2018-11-08 09:59:28
 */
public interface ImageAddressService extends IService<ImageAddressEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

