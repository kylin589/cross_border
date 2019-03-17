package io.renren.modules.product.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
@TableName("product_variants_info")
public class VariantsInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long variantId;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 变体排序
     */
    private Integer variantSort;
    /**
     * 变体组合
     */
    private String variantCombination;
    /**
     * 变体sku修正
     */
    private String variantSku;
    /**
     * 加价（人民币）
     */
    private BigDecimal variantAddPrice;
    /**
     * 变体库存
     */
    private Integer variantStock;
    /**
     * ean/puc
     */
    private String eanCode;
    /**
     * 变体图片路径（按顺序以逗号隔开）
     */
    private String imageUrl;

    private Long userId;

    private Long deptId;

    /**
     * 获取：产品ID
     */
    public Long getProductId() {
        return productId;
    }
    /**
     * 设置：产品ID
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * 设置：
     */
    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    /**
     * 获取：
     */
    public Long getVariantId() {
        return variantId;
    }

    /**
     * 设置：变体排序
     */
    public void setVariantSort(Integer variantSort) {
        this.variantSort = variantSort;
    }

    /**
     * 获取：变体排序
     */
    public Integer getVariantSort() {
        return variantSort;
    }

    /**
     * 设置：变体组合
     */
    public void setVariantCombination(String variantCombination) {
        this.variantCombination = variantCombination;
    }

    /**
     * 获取：变体组合
     */
    public String getVariantCombination() {
        return variantCombination;
    }

    /**
     * 设置：变体sku修正
     */
    public void setVariantSku(String variantSku) {
        this.variantSku = variantSku;
    }

    /**
     * 获取：变体sku修正
     */
    public String getVariantSku() {
        return variantSku;
    }

    /**
     * 设置：加价（人民币）
     */
    public void setVariantAddPrice(BigDecimal variantAddPrice) {
        this.variantAddPrice = variantAddPrice;
    }

    /**
     * 获取：加价（人民币）
     */
    public BigDecimal getVariantAddPrice() {
        return variantAddPrice;
    }

    /**
     * 设置：变体库存
     */
    public void setVariantStock(Integer variantStock) {
        this.variantStock = variantStock;
    }

    /**
     * 获取：变体库存
     */
    public Integer getVariantStock() {
        return variantStock;
    }

    /**
     * 设置：ean/puc
     */
    public void setEanCode(String eanCode) {
        this.eanCode = eanCode;
    }

    /**
     * 获取：ean/puc
     */
    public String getEanCode() {
        return eanCode;
    }

    /**
     * 设置：变体图片路径（按顺序以逗号隔开）
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * 获取：变体图片路径（按顺序以逗号隔开）
     */
    public String getImageUrl() {
        return imageUrl;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }
}
