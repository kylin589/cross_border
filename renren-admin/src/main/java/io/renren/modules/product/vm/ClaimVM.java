package io.renren.modules.product.vm;

/**
 * @Auther: wdh
 * @Date: 2018/12/19 00:30
 * @Description:
 */
public class ClaimVM {
    private Long productId;
    private Long deptId;
    private Long userId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
