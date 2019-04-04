package io.renren.modules.logistics.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

        import java.math.BigDecimal;
    import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 国际物流
 *
 * @author wangdh
 * @email 594340717@qq.com
 * @date 2019-03-28 19:50:52
 */
@TableName("new_order_abroad_logistics")
public class NewOrderAbroadLogisticsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

            /**
         * 国外物流
         */
                @TableId
            private Long abroadLogisticsId;
            /**
         * 订单id
         */
            private Long orderId;
        /**
         * 订单国际物流编号
         */
            private String orderNumber;
        /**
        * 国际物流单号
         */
            private String abroadWaybill;
            /**
         * 国内追踪号
         */
            private String domesticTrackWaybill;
            /**
         * 国际追踪号
         */
            private String trackWaybill;
            /**
         * 国际物流状态编码
         */
            private String status;
            /**
         * 国际物流状态
         */
            private String state;
            /**
         * 实际重量
         */
            private String actualWeight;
            /**
         * 国际运费
         */
            private BigDecimal interFreight;
            /**
         * 是否同步（默认0：没有  1：有）
         */
            private Integer isSynchronization;
            /**
         * 国外物流公司
         */
            private String destTransportCompany;
            /**
         * 国外物流渠道
         */
            private String destChannel;
            /**
         * 目的地查询网址
         */
            private String destQueryUrl;
            /**
         * 服务查询网址
         */
            private String serviceQueryUrl;
            /**
         * 电话
         */
            private String mobile;
            /**
         * 发货时间
         */
            private Date shipTime;
            /**
         * 包裹中文名称
         */
            private String chineseName;
            /**
         * 包裹英文名称
         */
            private String englishName;
            /**
         * 包裹长
         */
            private Integer length;
            /**
         * 宽
         */
            private Integer width;
            /**
         * 包裹高
         */
            private Integer height;
            /**
         * 包裹净重
         */
            private BigDecimal weight;

            /**
         * 包裹类型 0-小包(云途)；1-大包(三泰)
         */
            private Integer packageType;
            /**
         * 线路中文名
         */
            private String channelName;
            /**
         * 线路代码
         */
            private String channelCode;
        /**
         * 打印地址
         */
            private String printUrl;
            /**
         * 创建时间
         */
            private Date createTime;
            /**
         * 更新时间
         */
            private Date updateTime;

            private int isDeleted;//是否销毁 0-未销毁；1-已销毁
        @TableField(exist = false)
        private String channeDisplayName;

        @TableField(exist = false)
        private List<NewOrderItemRelationshipEntity> orderItemRelationList;

        public String getChanneDisplayName() {
            return channeDisplayName;
        }

        public void setChanneDisplayName(String channeDisplayName) {
            this.channeDisplayName = channeDisplayName;
        }

        /**
         * 设置：国外物流
         */
        public void setAbroadLogisticsId(Long abroadLogisticsId) {
            this.abroadLogisticsId = abroadLogisticsId;
        }

        /**
         * 获取：国外物流
         */
        public Long getAbroadLogisticsId() {
            return abroadLogisticsId;
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
         * 设置：国内追踪号
         */
        public void setDomesticTrackWaybill(String domesticTrackWaybill) {
            this.domesticTrackWaybill = domesticTrackWaybill;
        }

        /**
         * 获取：国内追踪号
         */
        public String getDomesticTrackWaybill() {
            return domesticTrackWaybill;
        }
            /**
         * 设置：国际追踪号
         */
        public void setTrackWaybill(String trackWaybill) {
            this.trackWaybill = trackWaybill;
        }

        /**
         * 获取：国际追踪号
         */
        public String getTrackWaybill() {
            return trackWaybill;
        }
            /**
         * 设置：国际物流状态编码
         */
        public void setStatus(String status) {
            this.status = status;
        }

        /**
         * 获取：国际物流状态编码
         */
        public String getStatus() {
            return status;
        }
            /**
         * 设置：国际物流状态
         */
        public void setState(String state) {
            this.state = state;
        }

        /**
         * 获取：国际物流状态
         */
        public String getState() {
            return state;
        }
            /**
         * 设置：实际重量
         */
        public void setActualWeight(String actualWeight) {
            this.actualWeight = actualWeight;
        }

        /**
         * 获取：实际重量
         */
        public String getActualWeight() {
            return actualWeight;
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
         * 设置：是否同步（默认0：没有  1：有）
         */
        public void setIsSynchronization(Integer isSynchronization) {
            this.isSynchronization = isSynchronization;
        }

        /**
         * 获取：是否同步（默认0：没有  1：有）
         */
        public Integer getIsSynchronization() {
            return isSynchronization;
        }
            /**
         * 设置：国外物流公司
         */
        public void setDestTransportCompany(String destTransportCompany) {
            this.destTransportCompany = destTransportCompany;
        }

        /**
         * 获取：国外物流公司
         */
        public String getDestTransportCompany() {
            return destTransportCompany;
        }
            /**
         * 设置：国外物流渠道
         */
        public void setDestChannel(String destChannel) {
            this.destChannel = destChannel;
        }

        /**
         * 获取：国外物流渠道
         */
        public String getDestChannel() {
            return destChannel;
        }
            /**
         * 设置：目的地查询网址
         */
        public void setDestQueryUrl(String destQueryUrl) {
            this.destQueryUrl = destQueryUrl;
        }

        /**
         * 获取：目的地查询网址
         */
        public String getDestQueryUrl() {
            return destQueryUrl;
        }
            /**
         * 设置：服务查询网址
         */
        public void setServiceQueryUrl(String serviceQueryUrl) {
            this.serviceQueryUrl = serviceQueryUrl;
        }

        /**
         * 获取：服务查询网址
         */
        public String getServiceQueryUrl() {
            return serviceQueryUrl;
        }
            /**
         * 设置：电话
         */
        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        /**
         * 获取：电话
         */
        public String getMobile() {
            return mobile;
        }
            /**
         * 设置：发货时间
         */
        public void setShipTime(Date shipTime) {
            this.shipTime = shipTime;
        }

        /**
         * 获取：发货时间
         */
        public Date getShipTime() {
            return shipTime;
        }
            /**
         * 设置：包裹中文名称
         */
        public void setChineseName(String chineseName) {
            this.chineseName = chineseName;
        }

        /**
         * 获取：包裹中文名称
         */
        public String getChineseName() {
            return chineseName;
        }
            /**
         * 设置：包裹英文名称
         */
        public void setEnglishName(String englishName) {
            this.englishName = englishName;
        }

        /**
         * 获取：包裹英文名称
         */
        public String getEnglishName() {
            return englishName;
        }
            /**
         * 设置：包裹长
         */
        public void setLength(Integer length) {
            this.length = length;
        }

        /**
         * 获取：包裹长
         */
        public Integer getLength() {
            return length;
        }
            /**
         * 设置：宽
         */
        public void setWidth(Integer width) {
            this.width = width;
        }

        /**
         * 获取：宽
         */
        public Integer getWidth() {
            return width;
        }
            /**
         * 设置：包裹高
         */
        public void setHeight(Integer height) {
            this.height = height;
        }

        /**
         * 获取：包裹高
         */
        public Integer getHeight() {
            return height;
        }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
         * 设置：包裹类型 0-小包；1-大包
         */
        public void setPackageType(Integer packageType) {
            this.packageType = packageType;
        }

        /**
         * 获取：包裹类型 0-小包；1-大包
         */
        public Integer getPackageType() {
            return packageType;
        }
            /**
         * 设置：线路中文名
         */
        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        /**
         * 获取：线路中文名
         */
        public String getChannelName() {
            return channelName;
        }
            /**
         * 设置：线路代码
         */
        public void setChannelCode(String channelCode) {
            this.channelCode = channelCode;
        }

        /**
         * 获取：线路代码
         */
        public String getChannelCode() {
            return channelCode;
        }
            /**
         * 设置：创建时间
         */
        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        /**
         * 获取：创建时间
         */
        public Date getCreateTime() {
            return createTime;
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

    public String getPrintUrl() {
        return printUrl;
    }

    public void setPrintUrl(String printUrl) {
        this.printUrl = printUrl;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public List<NewOrderItemRelationshipEntity> getOrderItemRelationList() {
        return orderItemRelationList;
    }

    public void setOrderItemRelationList(List<NewOrderItemRelationshipEntity> orderItemRelationList) {
        this.orderItemRelationList = orderItemRelationList;
    }
}
