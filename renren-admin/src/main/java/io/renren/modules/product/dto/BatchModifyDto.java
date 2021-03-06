package io.renren.modules.product.dto;

/**
 * 批量修改实体
 */
public class BatchModifyDto {

    private Long[] productIds;
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
     * 产品重量
     */
    private Double productWeight;

    /**
     * 产品长度
     */
    private Double productLength;
    /**
     * 产品宽度
     */
    private Double productWide;
    /**
     * 产品高度
     */
    private Double productHeight;

    /**
     * 产品标题前加
     */
    private String productTitleQ;
    /**
     * 产品标题后加
     */
    private String productTitleH;
    /**
     * 关键词
     */
    private String keyWord;
    /**
     * 要点说明
     */
    private String keyPoints;
    /**
     * 产品描述前加
     */
    private String productDescriptionQ;
    /**
     * 产品描述后加
     */
    private String productDescriptionH;

    /**
     * 买点追加
     */
    private boolean keyPointsadd;

    /**
     * 关键字追加
     */
    private boolean  keywordsadd;
    /**
     * 翻译标识（0英,1法,2意,3西,4德，5日）
     */
    private int[] translates;

    //产品的内部分类
    private String productCategory;

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public Long[] getProductIds() {
        return productIds;
    }

    public void setProductIds(Long[] productIds) {
        this.productIds = productIds;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getShelveStatus() {
        return shelveStatus;
    }

    public void setShelveStatus(String shelveStatus) {
        this.shelveStatus = shelveStatus;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Long getCategoryOneId() {
        return categoryOneId;
    }

    public void setCategoryOneId(Long categoryOneId) {
        this.categoryOneId = categoryOneId;
    }

    public Long getCategoryTwoId() {
        return categoryTwoId;
    }

    public void setCategoryTwoId(Long categoryTwoId) {
        this.categoryTwoId = categoryTwoId;
    }

    public Long getCategoryThreeId() {
        return categoryThreeId;
    }

    public void setCategoryThreeId(Long categoryThreeId) {
        this.categoryThreeId = categoryThreeId;
    }

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getManufacturerNumber() {
        return manufacturerNumber;
    }

    public void setManufacturerNumber(String manufacturerNumber) {
        this.manufacturerNumber = manufacturerNumber;
    }

    public Double getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(Double productWeight) {
        this.productWeight = productWeight;
    }

    public Double getProductLength() {
        return productLength;
    }

    public void setProductLength(Double productLength) {
        this.productLength = productLength;
    }

    public Double getProductWide() {
        return productWide;
    }

    public void setProductWide(Double productWide) {
        this.productWide = productWide;
    }

    public Double getProductHeight() {
        return productHeight;
    }

    public void setProductHeight(Double productHeight) {
        this.productHeight = productHeight;
    }

    public String getProductTitleQ() {
        return productTitleQ;
    }

    public void setProductTitleQ(String productTitleQ) {
        this.productTitleQ = productTitleQ;
    }

    public String getProductTitleH() {
        return productTitleH;
    }

    public void setProductTitleH(String productTitleH) {
        this.productTitleH = productTitleH;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getKeyPoints() {
        return keyPoints;
    }

    public void setKeyPoints(String keyPoints) {
        this.keyPoints = keyPoints;
    }

    public String getProductDescriptionQ() {
        return productDescriptionQ;
    }

    public void setProductDescriptionQ(String productDescriptionQ) {
        this.productDescriptionQ = productDescriptionQ;
    }

    public String getProductDescriptionH() {
        return productDescriptionH;
    }

    public void setProductDescriptionH(String productDescriptionH) {
        this.productDescriptionH = productDescriptionH;
    }

    public boolean getKeyPointsadd() {
        return keyPointsadd;
    }

    public void setKeyPointsadd(boolean keyPointsadd) {
        this.keyPointsadd = keyPointsadd;
    }

    public boolean getKeywordsadd() {
        return keywordsadd;
    }

    public void setKeywordsadd(boolean keywordsadd) {
        this.keywordsadd = keywordsadd;
    }

    public int[] getTranslates() {
        return translates;
    }

    public void setTranslates(int[] translates) {
        this.translates = translates;
    }

    @Override
    public String toString() {
        return "BatchModifyDto{" +
                "auditStatus='" + auditStatus + '\'' +
                ", shelveStatus='" + shelveStatus + '\'' +
                ", productType='" + productType + '\'' +
                ", categoryOneId=" + categoryOneId +
                ", categoryTwoId=" + categoryTwoId +
                ", categoryThreeId=" + categoryThreeId +
                ", producerName='" + producerName + '\'' +
                ", brandName='" + brandName + '\'' +
                ", manufacturerNumber='" + manufacturerNumber + '\'' +
                ", productWeight=" + productWeight +
                ", productLength=" + productLength +
                ", productWide=" + productWide +
                ", productHeight=" + productHeight +
                ", productTitleQ='" + productTitleQ + '\'' +
                ", productTitleH='" + productTitleH + '\'' +
                ", keyWord='" + keyWord + '\'' +
                ", keyPoints='" + keyPoints + '\'' +
                ", productDescriptionQ='" + productDescriptionQ + '\'' +
                ", productDescriptionH='" + productDescriptionH + '\'' +
                ", keyPointsadd='" + keyPointsadd + '\'' +
                ", keywordsadd='" + keywordsadd + '\'' +
                '}';
    }
}
