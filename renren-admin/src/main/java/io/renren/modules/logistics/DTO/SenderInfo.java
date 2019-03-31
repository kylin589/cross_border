package io.renren.modules.logistics.DTO;

public class SenderInfo {
    private String CountryCode;//发件人所在国家，填写国际通用标准2位简码，可通过国家查询服务查询
    private String SenderFirstName;//发件人姓
    private String SenderLastName;//发件人名字
    private String SenderCompany;//发件人公司名称
    private String SenderAddress;//发件人详情地址
    private String SenderCity;//发件人所在城市
    private String SenderState;//发货人省/州
    private String SenderZip;//发货人邮编
    private String SenderPhone;//发货人电话

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    public String getSenderFirstName() {
        return SenderFirstName;
    }

    public void setSenderFirstName(String senderFirstName) {
        SenderFirstName = senderFirstName;
    }

    public String getSenderLastName() {
        return SenderLastName;
    }

    public void setSenderLastName(String senderLastName) {
        SenderLastName = senderLastName;
    }

    public String getSenderCompany() {
        return SenderCompany;
    }

    public void setSenderCompany(String senderCompany) {
        SenderCompany = senderCompany;
    }

    public String getSenderAddress() {
        return SenderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        SenderAddress = senderAddress;
    }

    public String getSenderCity() {
        return SenderCity;
    }

    public void setSenderCity(String senderCity) {
        SenderCity = senderCity;
    }

    public String getSenderState() {
        return SenderState;
    }

    public void setSenderState(String senderState) {
        SenderState = senderState;
    }

    public String getSenderZip() {
        return SenderZip;
    }

    public void setSenderZip(String senderZip) {
        SenderZip = senderZip;
    }

    public String getSenderPhone() {
        return SenderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        SenderPhone = senderPhone;
    }
}
