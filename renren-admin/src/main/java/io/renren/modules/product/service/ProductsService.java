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

    /**
     * 我的产品列表
     *
     * @param params url参数
     * @param userId 用户id
     * @return Map<String   ,       Object>
     * page 产品page
     * proCount 产品数量
     * approvedCount 审核通过
     * numberOfVariants 包含变体的商品
     * variantsCount 变体总数
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    Map<String, Object> queryPage(Map<String, Object> params, Long userId);

    /**
     * 产品回收站
     *
     * @param params url参数
     * @param userId 用户id
     * @return Map<String   ,       Object>
     * page 产品page
     * proCount 产品数量
     * approvedCount 审核通过
     * numberOfVariants 包含变体的商品
     * variantsCount 变体总数
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    Map<String, Object> queryRecyclingPage(Map<String, Object> params, Long userId);

    /**
     * 审核通过总数
     *
     * @param category      分类
     * @param title         标题
     * @param sku
     * @param startDate     开始时间
     * @param endDate       结束时间
     * @param shelveNumber  上架状态
     * @param productNumber 产品存放类型
     * @param ids           用户ids
     * @param isDeleted     是否删除
     * @return 总数
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    int getApprovedCount(String category, String title, String sku, String startDate, String endDate, String shelveNumber, String productNumber, List<Long> ids, int isDeleted);

    /**
     * 统计包含变体的商品总数
     *
     * @param wrapper 查询条件
     * @return 总数
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    int getNumberOfVariants(EntityWrapper<ProductsEntity> wrapper);

    /**
     * 统计变体总数
     *
     * @param wrapper 查询条件
     * @return 总数
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    int getWariantsCount(EntityWrapper<ProductsEntity> wrapper);

    /**
     * 获取新商品的编号
     *
     * @param userId 用户id
     * @return 编号
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
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

    /**
     * 获取商品数量
     * @param params url 参数
     * @param userId 用户id
     * @param isDel 是否删除
     * @return 数量
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    int getTotalCount(Map<String, Object> params, Long userId, String isDel);

    boolean relationVariantColor(Long productId, Long variantParameterId);

    boolean relationVariantSize(Long productId, Long variantParameterId);
}