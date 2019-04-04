package io.renren.modules.logistics.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

        import java.math.BigDecimal;
    import java.io.Serializable;
import java.util.Date;

/**
 * 
 *
 * @author wangdh
 * @email 594340717@qq.com
 * @date 2019-04-03 15:39:33
 */
@TableName("new_order_item_relationship")
public class NewOrderItemRelationshipEntity implements Serializable {
    private static final long serialVersionUID = 1L;

            /**
         * 关系表id
         */
                @TableId
            private Long relationshipId;
            /**
         * 国外物流id
         */
            private Long abroadLogisticsId;
            /**
         * 关联的货品id
         */
            private Long itemId;
            /**
         * 产品id
         */
            private Long itemCodeId;
            /**
         * 货品数量
         */
            private Integer orderItemNumber;
            /**
         * 关联货品的运输数量
         */
            private String itemQuantity;
            /**
         * 关联货品单价
         */
            private BigDecimal productPrice;
         /**
         *  美元价格
         */
         private BigDecimal usdPrice;

            /**
         * 关联货品的重量
         */
            private BigDecimal itemWeight;
            /**
         * 关联货品的货品编码
         */
            private String itemCode;
            /**
         * 关联货品的中文名称描述
         */
            private String itemChineseName;
            /**
         * 关联货品的英文名称描述
         */
            private String itemEnglishName;
            /**
         * 关联货品的中文材质
         */
            private String itemCnMaterial;
            /**
         * 关联货品的英文材质
         */
            private String itemEnMaterial;
            /**
         * 关联货品的标题
         */
            private String productTitle;

            /**
         * 关联货品的图片地址
         */
            private String procuctImageUrl;
            /**
         * 
         */
            private String productSku;
            /**
         * 创建时间
         */
            private Date createTime;
    
            /**
         * 设置：关系表id
         */
        public void setRelationshipId(Long relationshipId) {
            this.relationshipId = relationshipId;
        }

        /**
         * 获取：关系表id
         */
        public Long getRelationshipId() {
            return relationshipId;
        }
            /**
         * 设置：国外物流id
         */
        public void setAbroadLogisticsId(Long abroadLogisticsId) {
            this.abroadLogisticsId = abroadLogisticsId;
        }

        /**
         * 获取：国外物流id
         */
        public Long getAbroadLogisticsId() {
            return abroadLogisticsId;
        }
            /**
         * 设置：关联的货品id
         */
        public void setItemId(Long itemId) {
            this.itemId = itemId;
        }

        /**
         * 获取：关联的货品id
         */
        public Long getItemId() {
            return itemId;
        }



    /**
         * 设置：货品数量
         */
        public void setOrderItemNumber(Integer orderItemNumber) {
            this.orderItemNumber = orderItemNumber;
        }

        /**
         * 获取：货品数量
         */
        public Integer getOrderItemNumber() {
            return orderItemNumber;
        }
            /**
         * 设置：关联货品的运输数量
         */
        public void setItemQuantity(String itemQuantity) {
            this.itemQuantity = itemQuantity;
        }

        /**
         * 获取：关联货品的运输数量
         */
        public String getItemQuantity() {
            return itemQuantity;
        }
            /**
         * 设置：关联货品的数量
         */
        public void setProductPrice(BigDecimal productPrice) {
            this.productPrice = productPrice;
        }

        /**
         * 获取：关联货品的数量
         */
        public BigDecimal getProductPrice() {
            return productPrice;
        }
            /**
         * 设置：关联货品的重量
         */
        public void setItemWeight(BigDecimal itemWeight) {
            this.itemWeight = itemWeight;
        }

        /**
         * 获取：关联货品的重量
         */
        public BigDecimal getItemWeight() {
            return itemWeight;
        }
            /**
         * 设置：关联货品的货品编码
         */
        public void setItemCode(String itemCode) {
            this.itemCode = itemCode;
        }

        /**
         * 获取：关联货品的货品编码
         */
        public String getItemCode() {
            return itemCode;
        }
            /**
         * 设置：关联货品的中文名称描述
         */
        public void setItemChineseName(String itemChineseName) {
            this.itemChineseName = itemChineseName;
        }

        /**
         * 获取：关联货品的中文名称描述
         */
        public String getItemChineseName() {
            return itemChineseName;
        }
            /**
         * 设置：关联货品的英文名称描述
         */
        public void setItemEnglishName(String itemEnglishName) {
            this.itemEnglishName = itemEnglishName;
        }

        /**
         * 获取：关联货品的英文名称描述
         */
        public String getItemEnglishName() {
            return itemEnglishName;
        }
            /**
         * 设置：关联货品的中文材质
         */
        public void setItemCnMaterial(String itemCnMaterial) {
            this.itemCnMaterial = itemCnMaterial;
        }

        /**
         * 获取：关联货品的中文材质
         */
        public String getItemCnMaterial() {
            return itemCnMaterial;
        }
            /**
         * 设置：关联货品的英文材质
         */
        public void setItemEnMaterial(String itemEnMaterial) {
            this.itemEnMaterial = itemEnMaterial;
        }

        /**
         * 获取：关联货品的英文材质
         */
        public String getItemEnMaterial() {
            return itemEnMaterial;
        }
            /**
         * 设置：关联货品的标题
         */
        public void setProductTitle(String productTitle) {
            this.productTitle = productTitle;
        }

        /**
         * 获取：关联货品的标题
         */
        public String getProductTitle() {
            return productTitle;
        }

            /**
         * 设置：关联货品的图片地址
         */
        public void setProcuctImageUrl(String procuctImageUrl) {
            this.procuctImageUrl = procuctImageUrl;
        }

        /**
         * 获取：关联货品的图片地址
         */
        public String getProcuctImageUrl() {
            return procuctImageUrl;
        }
            /**
         * 设置：
         */
        public void setProductSku(String productSku) {
            this.productSku = productSku;
        }

        /**
         * 获取：
         */
        public String getProductSku() {
            return productSku;
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


    public BigDecimal getUsdPrice() {
        return usdPrice;
    }

    public void setUsdPrice(BigDecimal usdPrice) {
        this.usdPrice = usdPrice;
    }

    public Long getItemCodeId() {
            return itemCodeId;
        }

        public void setItemCodeId(Long itemCodeId) {
            this.itemCodeId = itemCodeId;
        }
    }
