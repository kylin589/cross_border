package io.renren.modules.product.vm;

import java.math.BigDecimal;

/**
 * @Auther: wdh
 * @Date: 2018/12/15 17:28
 * @Description:
 */
public class OrderVM {
    public Long orderId;
    public Long[] orderIds;
    public String amazonOrderId;
    public String[] amazonOrderIds;//支持批量

    public String[] getAmazonOrderIds() {
        return amazonOrderIds;
    }

    public void setAmazonOrderIds(String[] amazonOrderIds) {
        this.amazonOrderIds = amazonOrderIds;
    }

    public int packageType;//包裹类型 0-（云途小包）1-（三泰大包）
    public String channelName;//物流运输专线名称

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String channelCode;//物流运输专线代码
    public String orderState;
    public String chineseName; //包裹中文名
    public String englishName; //包裹英文名
    public int length;//长
    public int width;//宽
    public int height;//高
    public BigDecimal weight;//
    public String abnormalStatus;
    public String abnormalState;

    public int getPackageType() {
        return packageType;
    }

    public void setPackageType(int packageType) {
        this.packageType = packageType;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getAmazonOrderId() {
        return amazonOrderId;
    }

    public void setAmazonOrderId(String amazonOrderId) {
        this.amazonOrderId = amazonOrderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long[] getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(Long[] orderIds) {
        this.orderIds = orderIds;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getAbnormalStatus() {
        return abnormalStatus;
    }

    public void setAbnormalStatus(String abnormalStatus) {
        this.abnormalStatus = abnormalStatus;
    }

    public String getAbnormalState() {
        return abnormalState;
    }

    public void setAbnormalState(String abnormalState) {
        this.abnormalState = abnormalState;
    }
}
