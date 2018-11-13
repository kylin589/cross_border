package io.renren.modules.product.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import io.renren.modules.product.entity.ProductsEntity;

import java.util.List;
import java.util.Map;

/**
 * 产品
 *
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
public interface ProductsService extends IService<ProductsEntity> {

    Map<String, Object> queryPage(Map<String, Object> params, Long userId);

    Map<String, Object> queryRecyclingPage(Map<String, Object> params, Long userId);

    int getApprovedCount(String category, String title, String sku, String startDate, String endDate, String shelveNumber, String productNumber, List<Long> ids, int isDeleted);

    int getNumberOfVariants(EntityWrapper<ProductsEntity> wrapper);

    int getWariantsCount(EntityWrapper<ProductsEntity> wrapper);

    Long getNewProductId(Long userId);
    //审核状态每个分类总数
    int auditCount(String number, String del);
    //上架状态每个分类总数
    int putawayCount(String number, String del);
    //产品类型每个分类总数
    int productCount(String number, String del);
    //一级分类产品总数
    int count(Long id, String del);
    //父类查子类的产品总和
    int counts(Long id, String del);

    int getTotalCount(Map<String, Object> params, Long userId,String isDel);
}