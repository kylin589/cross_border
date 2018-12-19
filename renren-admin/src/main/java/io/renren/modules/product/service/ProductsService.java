package io.renren.modules.product.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.product.dto.UploadProductDTO;
import io.renren.modules.product.entity.ProductsEntity;

import java.util.List;
import java.util.Map;

/**
 * 产品
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
public interface ProductsService extends IService<ProductsEntity> {
    /**
     * 我的产品列表
     * @param params url参数
     * @param userId 用户id
     * @return Map<String,Object>
     * page 产品page
     * proCount 产品数量
     * approvedCount 审核通过
     * numberOfVariants 包含变体的商品
     * variantsCount 变体总数
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    Map<String, Object> queryMyPage(Map<String, Object> params, Long userId);

    /**
     * 所有产品列表
     * @param params url参数
     * @param deptId 公司id
     * @return Map<String,Object>
     * page 产品page
     * proCount 产品数量
     * approvedCount 审核通过
     * numberOfVariants 包含变体的商品
     * variantsCount 变体总数
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    Map<String,Object> queryAllPage(Map<String,Object> params, Long deptId);

    /**
     * 认领产品列表
     * @param params url参数
     * @param deptId 公司id
     * @return Map<String,Object>
     * page 产品page
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    PageUtils queryClaimPage(Map<String,Object> params, Long deptId);

    /**
     * 产品回收站
     * @param params url参数
     * @param userId 用户id
     * @return Map<String,Object>
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
     * 我的产品审核通过总数
     * @param category      分类
     * @param title         标题
     * @param sku
     * @param startDate     开始时间
     * @param endDate       结束时间
     * @param shelveNumber  上架状态
     * @param productNumber 产品存放类型
     * @param userId           用户ids
     * @param isDeleted     是否删除
     * @return 总数
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    int getMyApprovedCount(String category, String title, String sku, String startDate, String endDate, String shelveNumber, String productNumber, Long userId, int isDeleted);

    /**
     * 所有产品审核通过总数
     * @param category      分类
     * @param title         标题
     * @param sku
     * @param startDate     开始时间
     * @param endDate       结束时间
     * @param shelveNumber  上架状态
     * @param productNumber 产品存放类型
     * @param deptId          用户ids
     * @param isDeleted     是否删除
     * @return 总数
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    int getAllApprovedCount(String category, String title, String sku, String startDate, String endDate, String shelveNumber, String productNumber, Long deptId, int isDeleted);
    /**
     * 统计包含变体的商品总数
     * @param wrapper 查询条件
     * @return 总数
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    int getNumberOfVariants(EntityWrapper<ProductsEntity> wrapper);

    /**
     * 统计变体总数
     * @param wrapper 查询条件
     * @return 总数
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    int getWariantsCount(EntityWrapper<ProductsEntity> wrapper);

    /**
     * 获取新商品的编号
     * @param userId 用户id
     * @return 编号
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    Long getNewProductId(Long userId);

    //公司审核状态每个分类总数
    int auditCountAll(String number, String del, Long deptId);
    //公司上架状态每个分类总数
    int putawayCountAll(String number, String del, Long deptId);
    //公司产品类型每个分类总数
    int productCountAll(String number, String del, Long deptId);



    /**
     * 获取我的商品数量
     * @param params url 参数
     * @param userId 用户id
     * @param isDel 是否删除
     * @return 数量
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    int getMyTotalCount(Map<String, Object> params, Long userId, String isDel);
    /**
     * 获取所有商品数量
     * @param params url 参数
     * @param deptId 公司id
     * @param isDel 是否删除
     * @return 数量
     * @author zjr
     * @date 2018-11-07 14:54:47
     */
    int getAllTotalCount(Map<String, Object> params, Long deptId, String isDel);

    //变体参数颜色与产品绑定
    boolean relationVariantColor(Long productId, Long variantParameterId);
   //变体参数尺寸与产品绑定
    boolean relationVariantSize(Long productId, Long variantParameterId);

    //筛选属于通过和上架而且属于自己的产品
    UploadProductDTO selectCanUploadProducts(List<Long> idList, Long userId);
}