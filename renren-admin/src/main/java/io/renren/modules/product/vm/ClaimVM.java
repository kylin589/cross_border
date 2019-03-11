package io.renren.modules.product.vm;

/**
 * @Auther: wdh
 * @Date: 2018/12/19 00:30
 * @Description:
 */
public class ClaimVM {
    private Long productId;
    private Long userId;
    private Long[] productIds;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long[] getProductIds() {
        return productIds;
    }

    public void setProductIds(Long[] productIds) {
        this.productIds = productIds;
    }
}
