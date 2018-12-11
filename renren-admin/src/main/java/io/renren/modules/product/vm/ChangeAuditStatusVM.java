package io.renren.modules.product.vm;

/**
 * @Auther: wdh
 * @Date: 2018/12/10 19:46
 * @Description:
 */
public class ChangeAuditStatusVM {
    private Long[] productIds;
    String number;
    String type;

    public Long[] getProductIds() {
        return productIds;
    }

    public void setProductIds(Long[] productIds) {
        this.productIds = productIds;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
