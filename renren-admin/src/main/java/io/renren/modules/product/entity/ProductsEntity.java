package io.renren.modules.product.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 产品
 *
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
@TableName("product_products")
public class ProductsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 产品id
     */
    @TableId
    private Long productId;
    /**
     * 一级分类id
     */
    private Long categoryOneId;
    /**
     * 二级分类id
     */
    private Long categoryTwoId;
    /**
     * 三级分类id
     */
    private Long categoryThreeId;
    /**
     * 产品内部分类
     */
    private String productCategory;
    /**
     * 审核状态标识
     */
    private String auditStatus;
    /**
     * 上架状态标识
     */
    private String shelveStatus;
    /**
     * 产品类型标识
     */
    private String productType;
    /**
     * 主图片id
     */
    private Long mainImageId;
    /**
     * 厂商名称
     */
    private String producerName;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 厂商编号
     */
    private String manufacturerNumber;
    /**
     * 内部sku
     */
    private String productSku;
    /**
     * 产品来源
     */
    private String productSource;
    /**
     * 来源地址
     */
    private String sellerLink;
    /**
     * 附加备注
     */
    private String productRemark;
    /**
     * ean码
     */
    private String eanCode;
    /**
     * upc码
     */
    private String upcCode;
    /**
     * 采购价格
     */
    private BigDecimal purchasePrice=new BigDecimal("0.00");
    /**
     * 产品重量
     */
    private Double productWeight = 0.00;
    /**
     * 产品长度
     */
    private Double productLength = 0.00;
    /**
     * 产品宽度
     */
    private Double productWide = 0.00;
    /**
     * 产品高度
     */
    private Double productHeight = 0.00;
    /**
     * 国内运费
     */
    private BigDecimal domesticFreight=new BigDecimal("0.00");
    /**
     * 折扣系数
     */
    private BigDecimal discount = new BigDecimal("1.00");
    /**
     * 美国运费id
     */
    private Long americanFreight;
    /**
     * 加拿大运费id
     */
    private Long canadaFreight;
    /**
     * 墨西哥运费id
     */
    private Long mexicoFreight;
    /**
     * 英国运费id
     */
    private Long britainFreight;
    /**
     * 法国运费id
     */
    private Long franceFreight;
    /**
     * 德国运费id
     */
    private Long germanyFreight;
    /**
     * 意大利运费id
     */
    private Long italyFreight;
    /**
     * 西班牙运费id
     */
    private Long spainFreight;
    /**
     * 日本运费id
     */
    private Long japanFreight;
    /**
     * 澳大利亚运费id
     */
    private Long australiaFreight;
    /**
     * 库存
     */
    private BigDecimal stock;
    /**
     * 预处理时间(天)
     */
    private Integer pretreatmentDate;
    /**
     * 产品简称（英文）
     */
    private String productAbbreviations;
    /**
     * 产品标题
     */
    private String productTitle;
    /**
     * 中文介绍
     */
    private Long chineseIntroduction;
    /**
     * 英语介绍
     */
    private Long britainIntroduction;
    /**
     * 法语介绍
     */
    private Long franceIntroduction;
    /**
     * 德语介绍
     */
    private Long germanyIntroduction;
    /**
     * 意大利语介绍
     */
    private Long italyIntroduction;
    /**
     * 西班牙语介绍
     */
    private Long spainIntroduction;
    /**
     * 日语介绍
     */
    private Long japanIntroduction;
    /**
     * （变体参数）颜色的id
     */
    private Long colorId;
    /**
     * （变体参数）尺寸id
     */
    private Long sizeId;
    /**
     * 软删（1：删除）
     */
    private Integer isDeleted;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建用户id
     */
    private Long createUserId;
    /**
     * 最后操作时间
     */
    private Date lastOperationTime;
    /**
     * 最后操作人id
     */
    private Long lastOperationUserId;
    /**
     * 公司id
     */
   private Long deptId;

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }
    /**
     *修正
     */
    private String correction;

    public String getCorrection() {
        return correction;
    }

    public void setCorrection(String correction) {
        this.correction = correction;
    }

    /**
     * 美国运费
     */
    @TableField(exist = false)
    private FreightCostEntity americanFC;

    /**
     * 加拿大运费
     */
    @TableField(exist = false)
    private FreightCostEntity canadaFC;

    /**
     * 墨西哥运费
     */
    @TableField(exist = false)
    private FreightCostEntity mexicoFC;

    /**
     * 英国运费
     */
    @TableField(exist = false)
    private FreightCostEntity britainFC;

    /**
     * 法国运费
     */
    @TableField(exist = false)
    private FreightCostEntity franceFC;

    /**
     * 德国运费
     */
    @TableField(exist = false)
    private FreightCostEntity germanyFC;

    /**
     * 意大利运费
     */
    @TableField(exist = false)
    private FreightCostEntity italyFC;

    /**
     * 西班牙运费
     */
    @TableField(exist = false)
    private FreightCostEntity spainFC;

    /**
     * 日本运费
     */
    @TableField(exist = false)
    private FreightCostEntity japanFC;

    /**
     * 澳大利亚运费
     */
    @TableField(exist = false)
    private FreightCostEntity australiaFC;

    /**
     * 中文介绍
     */
    @TableField(exist = false)
    private IntroductionEntity chinesePRE;

    /**
     * 英文介绍
     */
    @TableField(exist = false)
    private IntroductionEntity britainPRE;

    /**
     * 法语介绍
     */
    @TableField(exist = false)
    private IntroductionEntity francePRE;

    /**
     * 德语介绍
     */
    @TableField(exist = false)
    private IntroductionEntity germanyPRE;

    /**
     * 意大利语介绍
     */
    @TableField(exist = false)
    private IntroductionEntity italyPRE;

    /**
     * 西班牙语介绍
     */
    @TableField(exist = false)
    private IntroductionEntity spainPRE;

    /**
     * 日语介绍
     */
    @TableField(exist = false)
    private IntroductionEntity japanPRE;

    /**
     * 颜色变体参数
     */
    @TableField(exist = false)
    private VariantParameterEntity colorVP;

    /**
     * 尺寸变体参数
     */
    @TableField(exist = false)
    private VariantParameterEntity sizeVP;

    /**
     * 图片信息
     */
    @TableField(exist = false)
    private List<ImageAddressEntity> images;

    /**
     * 变体信息
     */
    @TableField(exist = false)
    private List<VariantsInfoEntity> variantsInfos;

    /**
     * 主图片信息
     */
    private String mainImageUrl;


    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }

    public List<ImageAddressEntity> getImages() {
        return images;
    }

    public void setImages(List<ImageAddressEntity> images) {
        this.images = images;
    }

    public List<VariantsInfoEntity> getVariantsInfos() {
        return variantsInfos;
    }

    public void setVariantsInfos(List<VariantsInfoEntity> variantsInfos) {
        this.variantsInfos = variantsInfos;
    }

    public FreightCostEntity getAmericanFC() {
        return americanFC;
    }

    public void setAmericanFC(FreightCostEntity americanFC) {
        this.americanFC = americanFC;
    }

    public FreightCostEntity getCanadaFC() {
        return canadaFC;
    }

    public void setCanadaFC(FreightCostEntity canadaFC) {
        this.canadaFC = canadaFC;
    }

    public FreightCostEntity getMexicoFC() {
        return mexicoFC;
    }

    public void setMexicoFC(FreightCostEntity mexicoFC) {
        this.mexicoFC = mexicoFC;
    }

    public FreightCostEntity getBritainFC() {
        return britainFC;
    }

    public void setBritainFC(FreightCostEntity britainFC) {
        this.britainFC = britainFC;
    }

    public FreightCostEntity getFranceFC() {
        return franceFC;
    }

    public void setFranceFC(FreightCostEntity franceFC) {
        this.franceFC = franceFC;
    }

    public FreightCostEntity getGermanyFC() {
        return germanyFC;
    }

    public void setGermanyFC(FreightCostEntity germanyFC) {
        this.germanyFC = germanyFC;
    }

    public FreightCostEntity getItalyFC() {
        return italyFC;
    }

    public void setItalyFC(FreightCostEntity italyFC) {
        this.italyFC = italyFC;
    }

    public FreightCostEntity getSpainFC() {
        return spainFC;
    }

    public void setSpainFC(FreightCostEntity spainFC) {
        this.spainFC = spainFC;
    }

    public FreightCostEntity getJapanFC() {
        return japanFC;
    }

    public void setJapanFC(FreightCostEntity japanFC) {
        this.japanFC = japanFC;
    }

    public FreightCostEntity getAustraliaFC() {
        return australiaFC;
    }

    public void setAustraliaFC(FreightCostEntity australiaFC) {
        this.australiaFC = australiaFC;
    }

    public IntroductionEntity getChinesePRE() {
        return chinesePRE;
    }

    public void setChinesePRE(IntroductionEntity chinesePRE) {
        this.chinesePRE = chinesePRE;
    }

    public IntroductionEntity getBritainPRE() {
        return britainPRE;
    }

    public void setBritainPRE(IntroductionEntity britainPRE) {
        this.britainPRE = britainPRE;
    }

    public IntroductionEntity getFrancePRE() {
        return francePRE;
    }

    public void setFrancePRE(IntroductionEntity francePRE) {
        this.francePRE = francePRE;
    }

    public IntroductionEntity getGermanyPRE() {
        return germanyPRE;
    }

    public void setGermanyPRE(IntroductionEntity germanyPRE) {
        this.germanyPRE = germanyPRE;
    }

    public IntroductionEntity getItalyPRE() {
        return italyPRE;
    }

    public void setItalyPRE(IntroductionEntity italyPRE) {
        this.italyPRE = italyPRE;
    }

    public IntroductionEntity getSpainPRE() {
        return spainPRE;
    }

    public void setSpainPRE(IntroductionEntity spainPRE) {
        this.spainPRE = spainPRE;
    }

    public IntroductionEntity getJapanPRE() {
        return japanPRE;
    }

    public void setJapanPRE(IntroductionEntity japanPRE) {
        this.japanPRE = japanPRE;
    }

    public VariantParameterEntity getColorVP() {
        return colorVP;
    }

    public void setColorVP(VariantParameterEntity colorVP) {
        this.colorVP = colorVP;
    }

    public VariantParameterEntity getSizeVP() {
        return sizeVP;
    }

    public void setSizeVP(VariantParameterEntity sizeVP) {
        this.sizeVP = sizeVP;
    }

    /**
     * 设置：产品id
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * 获取：产品id
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * 设置：一级分类id
     */
    public void setCategoryOneId(Long categoryOneId) {
        this.categoryOneId = categoryOneId;
    }

    /**
     * 获取：一级分类id
     */
    public Long getCategoryOneId() {
        return categoryOneId;
    }

    /**
     * 设置：二级分类id
     */
    public void setCategoryTwoId(Long categoryTwoId) {
        this.categoryTwoId = categoryTwoId;
    }

    /**
     * 获取：二级分类id
     */
    public Long getCategoryTwoId() {
        return categoryTwoId;
    }

    /**
     * 设置：三级分类id
     */
    public void setCategoryThreeId(Long categoryThreeId) {
        this.categoryThreeId = categoryThreeId;
    }

    /**
     * 获取：三级分类id
     */
    public Long getCategoryThreeId() {
        return categoryThreeId;
    }

    /**
     * 设置：产品内部分类
     */
    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    /**
     * 获取：产品内部分类
     */
    public String getProductCategory() {
        return productCategory;
    }

    /**
     * 设置：审核状态标识
     */
    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    /**
     * 获取：审核状态标识
     */
    public String getAuditStatus() {
        return auditStatus;
    }

    /**
     * 设置：上架状态标识
     */
    public void setShelveStatus(String shelveStatus) {
        this.shelveStatus = shelveStatus;
    }

    /**
     * 获取：上架状态标识
     */
    public String getShelveStatus() {
        return shelveStatus;
    }

    /**
     * 设置：产品类型标识
     */
    public void setProductType(String productType) {
        this.productType = productType;
    }

    /**
     * 获取：产品类型标识
     */
    public String getProductType() {
        return productType;
    }

    /**
     * 设置：主图片id
     */
    public void setMainImageId(Long mainImageId) {
        this.mainImageId = mainImageId;
    }

    /**
     * 获取：主图片id
     */
    public Long getMainImageId() {
        return mainImageId;
    }

    /**
     * 设置：厂商名称
     */
    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    /**
     * 获取：厂商名称
     */
    public String getProducerName() {
        return producerName;
    }

    /**
     * 设置：品牌名称
     */
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    /**
     * 获取：品牌名称
     */
    public String getBrandName() {
        return brandName;
    }

    /**
     * 设置：厂商编号
     */
    public void setManufacturerNumber(String manufacturerNumber) {
        this.manufacturerNumber = manufacturerNumber;
    }

    /**
     * 获取：厂商编号
     */
    public String getManufacturerNumber() {
        return manufacturerNumber;
    }

    /**
     * 设置：内部sku
     */
    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    /**
     * 获取：内部sku
     */
    public String getProductSku() {
        return productSku;
    }

    /**
     * 设置：产品来源
     */
    public void setProductSource(String productSource) {
        this.productSource = productSource;
    }

    /**
     * 获取：产品来源
     */
    public String getProductSource() {
        return productSource;
    }

    /**
     * 设置：来源地址
     */
    public void setSellerLink(String sellerLink) {
        this.sellerLink = sellerLink;
    }

    /**
     * 获取：来源地址
     */
    public String getSellerLink() {
        return sellerLink;
    }

    /**
     * 设置：附加备注
     */
    public void setProductRemark(String productRemark) {
        this.productRemark = productRemark;
    }

    /**
     * 获取：附加备注
     */
    public String getProductRemark() {
        return productRemark;
    }

    /**
     * 设置：ean码
     */
    public void setEanCode(String eanCode) {
        this.eanCode = eanCode;
    }

    /**
     * 获取：ean码
     */
    public String getEanCode() {
        return eanCode;
    }

    /**
     * 设置：upc码
     */
    public void setUpcCode(String upcCode) {
        this.upcCode = upcCode;
    }

    /**
     * 获取：upc码
     */
    public String getUpcCode() {
        return upcCode;
    }

    /**
     * 设置：采购价格
     */
    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    /**
     * 获取：采购价格
     */
    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * 设置：产品重量
     */
    public void setProductWeight(Double productWeight) {
        this.productWeight = productWeight;
    }

    /**
     * 获取：产品重量
     */
    public Double getProductWeight() {
        return productWeight;
    }

    /**
     * 设置：产品长度
     */
    public void setProductLength(Double productLength) {
        this.productLength = productLength;
    }

    /**
     * 获取：产品长度
     */
    public Double getProductLength() {
        return productLength;
    }

    /**
     * 设置：产品宽度
     */
    public void setProductWide(Double productWide) {
        this.productWide = productWide;
    }

    /**
     * 获取：产品宽度
     */
    public Double getProductWide() {
        return productWide;
    }

    /**
     * 设置：产品高度
     */
    public void setProductHeight(Double productHeight) {
        this.productHeight = productHeight;
    }

    /**
     * 获取：产品高度
     */
    public Double getProductHeight() {
        return productHeight;
    }

    /**
     * 设置：国内运费
     */
    public void setDomesticFreight(BigDecimal domesticFreight) {
        this.domesticFreight = domesticFreight;
    }

    /**
     * 获取：国内运费
     */
    public BigDecimal getDomesticFreight() {
        return domesticFreight;
    }

    /**
     * 设置：折扣系数
     */
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    /**
     * 获取：折扣系数
     */
    public BigDecimal getDiscount() {
        return discount;
    }

    /**
     * 设置：美国运费id
     */
    public void setAmericanFreight(Long americanFreight) {
        this.americanFreight = americanFreight;
    }

    /**
     * 获取：美国运费id
     */
    public Long getAmericanFreight() {
        return americanFreight;
    }

    /**
     * 设置：加拿大运费id
     */
    public void setCanadaFreight(Long canadaFreight) {
        this.canadaFreight = canadaFreight;
    }

    /**
     * 获取：加拿大运费id
     */
    public Long getCanadaFreight() {
        return canadaFreight;
    }

    /**
     * 设置：墨西哥运费id
     */
    public void setMexicoFreight(Long mexicoFreight) {
        this.mexicoFreight = mexicoFreight;
    }

    /**
     * 获取：墨西哥运费id
     */
    public Long getMexicoFreight() {
        return mexicoFreight;
    }

    /**
     * 设置：英国运费id
     */
    public void setBritainFreight(Long britainFreight) {
        this.britainFreight = britainFreight;
    }

    /**
     * 获取：英国运费id
     */
    public Long getBritainFreight() {
        return britainFreight;
    }

    /**
     * 设置：法国运费id
     */
    public void setFranceFreight(Long franceFreight) {
        this.franceFreight = franceFreight;
    }

    /**
     * 获取：法国运费id
     */
    public Long getFranceFreight() {
        return franceFreight;
    }

    /**
     * 设置：德国运费id
     */
    public void setGermanyFreight(Long germanyFreight) {
        this.germanyFreight = germanyFreight;
    }

    /**
     * 获取：德国运费id
     */
    public Long getGermanyFreight() {
        return germanyFreight;
    }

    /**
     * 设置：意大利运费id
     */
    public void setItalyFreight(Long italyFreight) {
        this.italyFreight = italyFreight;
    }

    /**
     * 获取：意大利运费id
     */
    public Long getItalyFreight() {
        return italyFreight;
    }

    /**
     * 设置：西班牙运费id
     */
    public void setSpainFreight(Long spainFreight) {
        this.spainFreight = spainFreight;
    }

    /**
     * 获取：西班牙运费id
     */
    public Long getSpainFreight() {
        return spainFreight;
    }

    /**
     * 设置：日本运费id
     */
    public void setJapanFreight(Long japanFreight) {
        this.japanFreight = japanFreight;
    }

    /**
     * 获取：日本运费id
     */
    public Long getJapanFreight() {
        return japanFreight;
    }

    /**
     * 设置：澳大利亚运费id
     */
    public void setAustraliaFreight(Long australiaFreight) {
        this.australiaFreight = australiaFreight;
    }

    /**
     * 获取：澳大利亚运费id
     */
    public Long getAustraliaFreight() {
        return australiaFreight;
    }

    /**
     * 设置：库存
     */
    public void setStock(BigDecimal stock) {
        this.stock = stock;
    }

    /**
     * 获取：库存
     */
    public BigDecimal getStock() {
        return stock;
    }

    /**
     * 设置：预处理时间(天)
     */
    public void setPretreatmentDate(Integer pretreatmentDate) {
        this.pretreatmentDate = pretreatmentDate;
    }

    /**
     * 获取：预处理时间(天)
     */
    public Integer getPretreatmentDate() {
        return pretreatmentDate;
    }

    /**
     * 设置：产品简称（英文）
     */
    public void setProductAbbreviations(String productAbbreviations) {
        this.productAbbreviations = productAbbreviations;
    }

    /**
     * 获取：产品简称（英文）
     */
    public String getProductAbbreviations() {
        return productAbbreviations;
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
     * 设置：中文介绍
     */
    public void setChineseIntroduction(Long chineseIntroduction) {
        this.chineseIntroduction = chineseIntroduction;
    }

    /**
     * 获取：中文介绍
     */
    public Long getChineseIntroduction() {
        return chineseIntroduction;
    }

    /**
     * 设置：英语介绍
     */
    public void setBritainIntroduction(Long britainIntroduction) {
        this.britainIntroduction = britainIntroduction;
    }

    /**
     * 获取：英语介绍
     */
    public Long getBritainIntroduction() {
        return britainIntroduction;
    }

    /**
     * 设置：法语介绍
     */
    public void setFranceIntroduction(Long franceIntroduction) {
        this.franceIntroduction = franceIntroduction;
    }

    /**
     * 获取：法语介绍
     */
    public Long getFranceIntroduction() {
        return franceIntroduction;
    }

    /**
     * 设置：德语介绍
     */
    public void setGermanyIntroduction(Long germanyIntroduction) {
        this.germanyIntroduction = germanyIntroduction;
    }

    /**
     * 获取：德语介绍
     */
    public Long getGermanyIntroduction() {
        return germanyIntroduction;
    }

    /**
     * 设置：意大利语介绍
     */
    public void setItalyIntroduction(Long italyIntroduction) {
        this.italyIntroduction = italyIntroduction;
    }

    /**
     * 获取：意大利语介绍
     */
    public Long getItalyIntroduction() {
        return italyIntroduction;
    }

    /**
     * 设置：西班牙语介绍
     */
    public void setSpainIntroduction(Long spainIntroduction) {
        this.spainIntroduction = spainIntroduction;
    }

    /**
     * 获取：西班牙语介绍
     */
    public Long getSpainIntroduction() {
        return spainIntroduction;
    }

    /**
     * 设置：日语介绍
     */
    public void setJapanIntroduction(Long japanIntroduction) {
        this.japanIntroduction = japanIntroduction;
    }

    /**
     * 获取：日语介绍
     */
    public Long getJapanIntroduction() {
        return japanIntroduction;
    }

    /**
     * 设置：（变体参数）颜色的id
     */
    public void setColorId(Long colorId) {
        this.colorId = colorId;
    }

    /**
     * 获取：（变体参数）颜色的id
     */
    public Long getColorId() {
        return colorId;
    }

    /**
     * 设置：（变体参数）尺寸id
     */
    public void setSizeId(Long sizeId) {
        this.sizeId = sizeId;
    }

    /**
     * 获取：（变体参数）尺寸id
     */
    public Long getSizeId() {
        return sizeId;
    }

    /**
     * 设置：软删（1：删除）
     */
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 获取：软删（1：删除）
     */
    public Integer getIsDeleted() {
        return isDeleted;
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
     * 设置：创建用户id
     */
    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * 获取：创建用户id
     */
    public Long getCreateUserId() {
        return createUserId;
    }

    /**
     * 设置：最后操作时间
     */
    public void setLastOperationTime(Date lastOperationTime) {
        this.lastOperationTime = lastOperationTime;
    }

    /**
     * 获取：最后操作时间
     */
    public Date getLastOperationTime() {
        return lastOperationTime;
    }

    /**
     * 设置：最后操作人id
     */
    public void setLastOperationUserId(Long lastOperationUserId) {
        this.lastOperationUserId = lastOperationUserId;
    }

    /**
     * 获取：最后操作人id
     */
    public Long getLastOperationUserId() {
        return lastOperationUserId;
    }

    @Override
    public String toString() {
        return "ProductsEntity{" +
                "productId=" + productId +
                ", categoryOneId=" + categoryOneId +
                ", categoryTwoId=" + categoryTwoId +
                ", categoryThreeId=" + categoryThreeId +
                ", productCategory='" + productCategory + '\'' +
                ", auditStatus='" + auditStatus + '\'' +
                ", shelveStatus='" + shelveStatus + '\'' +
                ", productType='" + productType + '\'' +
                ", mainImageId=" + mainImageId +
                ", producerName='" + producerName + '\'' +
                ", brandName='" + brandName + '\'' +
                ", manufacturerNumber='" + manufacturerNumber + '\'' +
                ", productSku='" + productSku + '\'' +
                ", productSource='" + productSource + '\'' +
                ", sellerLink='" + sellerLink + '\'' +
                ", productRemark='" + productRemark + '\'' +
                ", eanCode='" + eanCode + '\'' +
                ", upcCode='" + upcCode + '\'' +
                ", purchasePrice=" + purchasePrice +
                ", productWeight=" + productWeight +
                ", productLength=" + productLength +
                ", productWide=" + productWide +
                ", productHeight=" + productHeight +
                ", domesticFreight=" + domesticFreight +
                ", discount=" + discount +
                ", americanFreight=" + americanFreight +
                ", canadaFreight=" + canadaFreight +
                ", mexicoFreight=" + mexicoFreight +
                ", britainFreight=" + britainFreight +
                ", franceFreight=" + franceFreight +
                ", germanyFreight=" + germanyFreight +
                ", italyFreight=" + italyFreight +
                ", spainFreight=" + spainFreight +
                ", japanFreight=" + japanFreight +
                ", australiaFreight=" + australiaFreight +
                ", stock=" + stock +
                ", pretreatmentDate=" + pretreatmentDate +
                ", productAbbreviations='" + productAbbreviations + '\'' +
                ", productTitle='" + productTitle + '\'' +
                ", chineseIntroduction=" + chineseIntroduction +
                ", britainIntroduction=" + britainIntroduction +
                ", franceIntroduction=" + franceIntroduction +
                ", germanyIntroduction=" + germanyIntroduction +
                ", italyIntroduction=" + italyIntroduction +
                ", spainIntroduction=" + spainIntroduction +
                ", japanIntroduction=" + japanIntroduction +
                ", colorId=" + colorId +
                ", sizeId=" + sizeId +
                ", isDeleted=" + isDeleted +
                ", createTime=" + createTime +
                ", createUserId=" + createUserId +
                ", lastOperationTime=" + lastOperationTime +
                ", lastOperationUserId=" + lastOperationUserId +
                ", deptId=" + deptId +
                ", americanFC=" + americanFC +
                ", canadaFC=" + canadaFC +
                ", mexicoFC=" + mexicoFC +
                ", britainFC=" + britainFC +
                ", franceFC=" + franceFC +
                ", germanyFC=" + germanyFC +
                ", italyFC=" + italyFC +
                ", spainFC=" + spainFC +
                ", japanFC=" + japanFC +
                ", australiaFC=" + australiaFC +
                ", chinesePRE=" + chinesePRE +
                ", britainPRE=" + britainPRE +
                ", francePRE=" + francePRE +
                ", germanyPRE=" + germanyPRE +
                ", italyPRE=" + italyPRE +
                ", spainPRE=" + spainPRE +
                ", japanPRE=" + japanPRE +
                ", colorVP=" + colorVP +
                ", sizeVP=" + sizeVP +
                ", images=" + images +
                ", variantsInfos=" + variantsInfos +
                ", mainImageUrl='" + mainImageUrl + '\'' +
                '}';
    }
}
