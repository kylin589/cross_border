package io.renren.modules.logistics.entity;

/**
 * @Auther: wdh
 * @Date: 2018/12/29 14:23
 * @Description:
 */
/**
 * 头部类
 */
public class Header {

    private String DocumentVersion;//文档版本


    private String MerchantIdentifier;//商家标识


    public String getDocumentVersion() {
        return DocumentVersion;
    }


    public void setDocumentVersion(String documentVersion) {
        DocumentVersion = documentVersion;
    }


    public String getMerchantIdentifier() {
        return MerchantIdentifier;
    }


    public void setMerchantIdentifier(String merchantIdentifier) {
        MerchantIdentifier = merchantIdentifier;
    }


}