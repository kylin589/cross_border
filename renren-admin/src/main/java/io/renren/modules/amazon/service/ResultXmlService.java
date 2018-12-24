package io.renren.modules.amazon.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.amazon.entity.ResultXmlEntity;

import java.util.Map;

/**
 * 亚马逊商品上传结果表
 *
 * @author zjr
 * @email 1981763981@qq.com
 * @date 2018-12-24 07:26:40
 */
public interface ResultXmlService extends IService<ResultXmlEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

