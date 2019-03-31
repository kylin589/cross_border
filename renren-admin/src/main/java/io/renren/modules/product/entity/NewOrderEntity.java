package io.renren.modules.product.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 新订单表
 *
 * @author wangdh
 * @email 594340717@qq.com
 * @date 2019-03-28 14:50:57
 */
@TableName("new_order")
public class NewOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

            /**
         * 订单id
         */
                @TableId
            private Long orderId;
            /**
         * 亚马逊订单id
         */
            private String amazonOrderId;
            /**
         * 订单商品编号
         */
            private String orderItemId;
            /**
         * 购买日期
         */
            private Date buyDate;
            /**
         * 订单状态标识
         */
            private String orderStatus;
            /**
         * 订单状态
         */
            private String orderState;
            /**
         * 异常状态标识
         */
            private String abnormalStatus;
            /**
         * 异常状态
         */
            private String abnormalState;
            /**
         * 国家代码
         */
            private String countryCode;
            /**
         * 币种
         */
            private String rateCode;
            /**
         * 店铺id
         */
            private Long shopId;
            /**
         * 店铺名称(店铺+国家）
         */
            private String shopName;
            /**
         * 关联产品id
         */
            private Long productId;
            /**
         * 产品标题
         */
            private String productTitle;
            /**
         * 产品sku
         */
            private String productSku;
            /**
         * 产品图片url
         */
            private String productImageUrl;
            /**
         * 产品asin码
         */
            private String productAsin;
            /**
         * 订单数量
         */
            private Integer orderNumber;
            /**
         * 当时汇率
         */
            private BigDecimal momentRate;
            /**
         * 采购价
         */
            private BigDecimal purchasePrice;
            /**
         * 订单金额
         */
            private BigDecimal orderMoney;
            /**
         * 订单金额（人民币）
         */
            private BigDecimal orderMoneyCny;
            /**
         * Amazon佣金
         */
            private BigDecimal amazonCommission;
            /**
         * Amazon佣金（人民币）
         */
            private BigDecimal amazonCommissionCny;
            /**
         * 到账金额
         */
            private BigDecimal accountMoney;
            /**
         * 到账金额（人民币）
         */
            private BigDecimal accountMoneyCny;
            /**
         * 国际运费
         */
            private BigDecimal interFreight;
            /**
         * 平台佣金
         */
            private BigDecimal platformCommissions;
            /**
         * 利润
         */
            private BigDecimal orderProfit;
            /**
         * 退货费用
         */
            private BigDecimal returnCost;
            /**
         * 利润率
         */
            private BigDecimal profitRate;
            /**
         * 国内物流单号
         */
            private String domesticWaybill;
            /**
         * 国际物流单号
         */
            private String abroadWaybill;
            /**
         * 用户id
         */
            private Long userId;
            /**
         * 公司id
         */
            private Long deptId;
            /**
         * 更新时间
         */
            private Date updateTime;
            /**
         * 是否为旧数据（0：新，1：旧）
         */
            private Integer isOld;

    /**
     * 用户名
     */
    @TableField(exist = false)
    private String userName;

    /*
     * 公司名称
     */
    @TableField(exist = false)
    private String deptName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    /**
         * 设置：订单id
         */
        public void setOrderId(Long orderId) {
            this.orderId = orderId;
        }

        /**
         * 获取：订单id
         */
        public Long getOrderId() {
            return orderId;
        }
            /**
         * 设置：亚马逊订单id
         */
        public void setAmazonOrderId(String amazonOrderId) {
            this.amazonOrderId = amazonOrderId;
        }

        /**
         * 获取：亚马逊订单id
         */
        public String getAmazonOrderId() {
            return amazonOrderId;
        }
            /**
         * 设置：订单商品编号
         */
        public void setOrderItemId(String orderItemId) {
            this.orderItemId = orderItemId;
        }

        /**
         * 获取：订单商品编号
         */
        public String getOrderItemId() {
            return orderItemId;
        }
            /**
         * 设置：购买日期
         */
        public void setBuyDate(Date buyDate) {
            this.buyDate = buyDate;
        }

        /**
         * 获取：购买日期
         */
        public Date getBuyDate() {
            return buyDate;
        }
            /**
         * 设置：订单状态标识
         */
        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        /**
         * 获取：订单状态标识
         */
        public String getOrderStatus() {
            return orderStatus;
        }
            /**
         * 设置：订单状态
         */
        public void setOrderState(String orderState) {
            this.orderState = orderState;
        }

        /**
         * 获取：订单状态
         */
        public String getOrderState() {
            return orderState;
        }
            /**
         * 设置：异常状态标识
         */
        public void setAbnormalStatus(String abnormalStatus) {
            this.abnormalStatus = abnormalStatus;
        }

        /**
         * 获取：异常状态标识
         */
        public String getAbnormalStatus() {
            return abnormalStatus;
        }
            /**
         * 设置：异常状态
         */
        public void setAbnormalState(String abnormalState) {
            this.abnormalState = abnormalState;
        }

        /**
         * 获取：异常状态
         */
        public String getAbnormalState() {
            return abnormalState;
        }
            /**
         * 设置：国家代码
         */
        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        /**
         * 获取：国家代码
         */
        public String getCountryCode() {
            return countryCode;
        }
            /**
         * 设置：币种
         */
        public void setRateCode(String rateCode) {
            this.rateCode = rateCode;
        }

        /**
         * 获取：币种
         */
        public String getRateCode() {
            return rateCode;
        }
            /**
         * 设置：店铺id
         */
        public void setShopId(Long shopId) {
            this.shopId = shopId;
        }

        /**
         * 获取：店铺id
         */
        public Long getShopId() {
            return shopId;
        }
            /**
         * 设置：店铺名称(店铺+国家）
         */
        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        /**
         * 获取：店铺名称(店铺+国家）
         */
        public String getShopName() {
            return shopName;
        }
            /**
         * 设置：关联产品id
         */
        public void setProductId(Long productId) {
            this.productId = productId;
        }

        /**
         * 获取：关联产品id
         */
        public Long getProductId() {
            return productId;
        }
            /**
         * 设置：产品标题
         */
        public void setProductTitle(String productTitle) {
            this.productTitle = productTitle;
        }

        /**
         * 获取：产品标题
         */
        public String getProductTitle() {
            return productTitle;
        }
            /**
         * 设置：产品sku
         */
        public void setProductSku(String productSku) {
            this.productSku = productSku;
        }

        /**
         * 获取：产品sku
         */
        public String getProductSku() {
            return productSku;
        }
            /**
         * 设置：产品图片url
         */
        public void setProductImageUrl(String productImageUrl) {
            this.productImageUrl = productImageUrl;
        }

        /**
         * 获取：产品图片url
         */
        public String getProductImageUrl() {
            return productImageUrl;
        }
            /**
         * 设置：产品asin码
         */
        public void setProductAsin(String productAsin) {
            this.productAsin = productAsin;
        }

        /**
         * 获取：产品asin码
         */
        public String getProductAsin() {
            return productAsin;
        }
            /**
         * 设置：订单数量
         */
        public void setOrderNumber(Integer orderNumber) {
            this.orderNumber = orderNumber;
        }

        /**
         * 获取：订单数量
         */
        public Integer getOrderNumber() {
            return orderNumber;
        }
            /**
         * 设置：当时汇率
         */
        public void setMomentRate(BigDecimal momentRate) {
            this.momentRate = momentRate;
        }

        /**
         * 获取：当时汇率
         */
        public BigDecimal getMomentRate() {
            return momentRate;
        }
            /**
         * 设置：采购价
         */
        public void setPurchasePrice(BigDecimal purchasePrice) {
            this.purchasePrice = purchasePrice;
        }

        /**
         * 获取：采购价
         */
        public BigDecimal getPurchasePrice() {
            return purchasePrice;
        }
            /**
         * 设置：订单金额
         */
        public void setOrderMoney(BigDecimal orderMoney) {
            this.orderMoney = orderMoney;
        }

        /**
         * 获取：订单金额
         */
        public BigDecimal getOrderMoney() {
            return orderMoney;
        }
            /**
         * 设置：订单金额（人民币）
         */
        public void setOrderMoneyCny(BigDecimal orderMoneyCny) {
            this.orderMoneyCny = orderMoneyCny;
        }

        /**
         * 获取：订单金额（人民币）
         */
        public BigDecimal getOrderMoneyCny() {
            return orderMoneyCny;
        }
            /**
         * 设置：Amazon佣金
         */
        public void setAmazonCommission(BigDecimal amazonCommission) {
            this.amazonCommission = amazonCommission;
        }

        /**
         * 获取：Amazon佣金
         */
        public BigDecimal getAmazonCommission() {
            return amazonCommission;
        }

    public BigDecimal getAmazonCommissionCny() {
        return amazonCommissionCny;
    }

    public void setAmazonCommissionCny(BigDecimal amazonCommissionCny) {
        this.amazonCommissionCny = amazonCommissionCny;
    }

    /**
         * 设置：到账金额
         */
        public void setAccountMoney(BigDecimal accountMoney) {
            this.accountMoney = accountMoney;
        }

        /**
         * 获取：到账金额
         */
        public BigDecimal getAccountMoney() {
            return accountMoney;
        }

    public BigDecimal getAccountMoneyCny() {
        return accountMoneyCny;
    }

    public void setAccountMoneyCny(BigDecimal accountMoneyCny) {
        this.accountMoneyCny = accountMoneyCny;
    }

    /**
         * 设置：国际运费
         */
        public void setInterFreight(BigDecimal interFreight) {
            this.interFreight = interFreight;
        }

        /**
         * 获取：国际运费
         */
        public BigDecimal getInterFreight() {
            return interFreight;
        }
            /**
         * 设置：平台佣金
         */
        public void setPlatformCommissions(BigDecimal platformCommissions) {
            this.platformCommissions = platformCommissions;
        }

        /**
         * 获取：平台佣金
         */
        public BigDecimal getPlatformCommissions() {
            return platformCommissions;
        }
            /**
         * 设置：利润
         */
        public void setOrderProfit(BigDecimal orderProfit) {
            this.orderProfit = orderProfit;
        }

        /**
         * 获取：利润
         */
        public BigDecimal getOrderProfit() {
            return orderProfit;
        }
            /**
         * 设置：退货费用
         */
        public void setReturnCost(BigDecimal returnCost) {
            this.returnCost = returnCost;
        }

        /**
         * 获取：退货费用
         */
        public BigDecimal getReturnCost() {
            return returnCost;
        }
            /**
         * 设置：利润率
         */
        public void setProfitRate(BigDecimal profitRate) {
            this.profitRate = profitRate;
        }

        /**
         * 获取：利润率
         */
        public BigDecimal getProfitRate() {
            return profitRate;
        }
            /**
         * 设置：国内物流单号
         */
        public void setDomesticWaybill(String domesticWaybill) {
            this.domesticWaybill = domesticWaybill;
        }

        /**
         * 获取：国内物流单号
         */
        public String getDomesticWaybill() {
            return domesticWaybill;
        }
            /**
         * 设置：国际物流单号
         */
        public void setAbroadWaybill(String abroadWaybill) {
            this.abroadWaybill = abroadWaybill;
        }

        /**
         * 获取：国际物流单号
         */
        public String getAbroadWaybill() {
            return abroadWaybill;
        }
            /**
         * 设置：用户id
         */
        public void setUserId(Long userId) {
            this.userId = userId;
        }

        /**
         * 获取：用户id
         */
        public Long getUserId() {
            return userId;
        }
            /**
         * 设置：公司id
         */
        public void setDeptId(Long deptId) {
            this.deptId = deptId;
        }

        /**
         * 获取：公司id
         */
        public Long getDeptId() {
            return deptId;
        }
            /**
         * 设置：更新时间
         */
        public void setUpdateTime(Date updateTime) {
            this.updateTime = updateTime;
        }

        /**
         * 获取：更新时间
         */
        public Date getUpdateTime() {
            return updateTime;
        }
            /**
         * 设置：是否为旧数据（0：新，1：旧）
         */
        public void setIsOld(Integer isOld) {
            this.isOld = isOld;
        }

        /**
         * 获取：是否为旧数据（0：新，1：旧）
         */
        public Integer getIsOld() {
            return isOld;
        }
    }
