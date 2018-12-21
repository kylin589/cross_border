package io.renren.modules.product.dto;

import io.renren.modules.product.entity.ProductsEntity;

import java.util.List;

/**
 * @Auther: wdh
 * @Date: 2018/12/19 21:37
 * @Description:
 */
public class UploadProductDTO {
    private String code = "ok";
    private String msg;
    private List<ProductsEntity> productsList;
    private List<Long> ret;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ProductsEntity> getProductsList() {
        return productsList;
    }

    public void setProductsList(List<ProductsEntity> productsList) {
        this.productsList = productsList;
    }

    public List<Long> getRet() {
        return ret;
    }

    public void setRet(List<Long> ret) {
        this.ret = ret;
    }
}
