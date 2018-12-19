package io.renren.modules.product.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.product.entity.AmazonCategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 亚马逊分类表
 *
 * @author jhy
 * @email 617493711@qq.com
 * @date 2018-11-16 11:11:30
 */
public interface AmazonCategoryService extends IService<AmazonCategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @methodname:  根据区域查出所有一级分类
     * @param: [region]
     * @return: List<AmazonCategoryEntity>
     * @auther: jhy
     * @date: 2018/11/16 14:39
     */
    List<AmazonCategoryEntity> queryByAreaOneClassify(String countryCode);
    /**
     * @methodname: queryByChindIdParentId 根据子级id查出所有的父级id以逗号进行拼接成字符串
     * @param: [amazonCategoryId] 子级id
     * @return: String
     * @auther: jhy
     * @date: 2018/11/16 15:46
     */
    String queryByChindIdParentId(Long amazonCategoryId);
}

