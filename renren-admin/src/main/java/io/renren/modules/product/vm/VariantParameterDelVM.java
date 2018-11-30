package io.renren.modules.product.vm;

import io.renren.modules.product.entity.VariantParameterEntity;

/**
 * @Auther: wdh
 * @Date: 2018/11/27 10:36
 * @Description:
 */
public class VariantParameterDelVM {
    //产品id
    private Long productId;
    //变体参数
    private VariantParameterEntity variantParameter;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public VariantParameterEntity getVariantParameter() {
        return variantParameter;
    }

    public void setVariantParameter(VariantParameterEntity variantParameter) {
        this.variantParameter = variantParameter;
    }
}
