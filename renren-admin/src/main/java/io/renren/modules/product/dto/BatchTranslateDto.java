package io.renren.modules.product.dto;

/**
 * @Auther: wdh
 * @Date: 2019/1/13 17:55
 * @Description:
 */
public class BatchTranslateDto {
    private String countryCode;
    private String productTitleQ;
    private String productTitleH;
    private String keyWord;
    private String keyPoints;
    private String productDescriptionQ;
    private String productDescriptionH;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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
}
