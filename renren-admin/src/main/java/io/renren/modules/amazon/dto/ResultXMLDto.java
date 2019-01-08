package io.renren.modules.amazon.dto;

/**
 * @author zjrit
 */
public class ResultXMLDto {
    private String messageID;
    private String resultCode;
    private String resultMessageCode;
    private String resultDescription;
    private String sku;

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessageCode() {
        return resultMessageCode;
    }

    public void setResultMessageCode(String resultMessageCode) {
        this.resultMessageCode = resultMessageCode;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @Override
    public String toString() {
        return "ResultXMLDto{" +
                "messageID='" + messageID + '\'' +
                ", resultCode='" + resultCode + '\'' +
                ", resultMessageCode='" + resultMessageCode + '\'' +
                ", resultDescription='" + resultDescription + '\'' +
                ", sku='" + sku + '\'' +
                '}';
    }
}
