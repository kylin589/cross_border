package io.renren.modules.logistics.DTO;

import org.apache.poi.hpsf.Decimal;

import java.math.BigDecimal;

public class ApplicationInfos {
    private String ApplicationName;//包裹中货品(英文) 申报名称,必填一项,打印CN22用, 备注（荷兰邮政小包挂号限制50个字符，且只能为英文）
    private String HsCode;//包裹中货品 申报编码,打印CN22用(欧洲专线必填；荷兰邮政小包挂号不必填，限制10个字符，只能为数字)
    private int Qty;//包裹中货品 申报数量,必填一项,打印CN22用
    private BigDecimal UnitPrice;//包裹中货品 申报价格,单位USD,必填一项,打印CN22用
    private BigDecimal UnitWeight;//包裹中货品 申报重量，单位kg,打印CN22用
    private String PickingName;//包裹的申报中文名称
    private String Remark;//用于打印配货单，（欧洲专线必填）
    private String ProductUrl;//产品链接地址
    private String Sku;//用于填写商品SKU

    public String getApplicationName() {
        return ApplicationName;
    }

    public void setApplicationName(String applicationName) {
        ApplicationName = applicationName;
    }

    public String getHsCode() {
        return HsCode;
    }

    public void setHsCode(String hsCode) {
        HsCode = hsCode;
    }

    public String getSku() {
        return Sku;
    }

    public void setSku(String sku) {
        Sku = sku;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public BigDecimal getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        UnitPrice = unitPrice;
    }

    public BigDecimal getUnitWeight() {
        return UnitWeight;
    }

    public void setUnitWeight(BigDecimal unitWeight) {
        UnitWeight = unitWeight;
    }

    public String getPickingName() {
        return PickingName;
    }

    public void setPickingName(String pickingName) {
        PickingName = pickingName;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getProductUrl() {
        return ProductUrl;
    }

    public void setProductUrl(String productUrl) {
        ProductUrl = productUrl;
    }


}
