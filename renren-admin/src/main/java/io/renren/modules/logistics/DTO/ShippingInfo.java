package io.renren.modules.logistics.DTO;

public class ShippingInfo {
    private String ShippingTaxId;//收件人企业税号，欧盟可以填EORI，巴西可以填CPF等，非必填
    private String CountryCode;//收件人所在国家，填写国际通用标准2位简码，可通过国家查询服务查询
    private String ShippingFirstName;//收件人姓 备注（荷兰邮政小包挂号长度限制收件人姓和收件人名 总长度60）
    private String ShippingLastName;//收件人名字
    private String ShippingCompany;//收件人公司名称
    private String ShippingAddress;//收件人详情地址 备注(发中美专线长度限制35个字符、荷兰邮政小包挂号限制200，不能包含中文)
    private String ShippingAddress1;//收件人详情地址1备注(发中美专线长度限制35个字符)
    private String ShippingAddress2;//收件人详情地址2备注(发中美专线长度限制35个字符)
    private String ShippingCity;//收件人所在城市 备注（荷兰邮政小包限制30个字符）
    private String ShippingState;//收货人省/州 备注（荷兰邮政小包挂号非必填，但限制30个字符）
    private String ShippingZip;//收货人邮编 备注（荷兰邮政小包挂号非必填，但限制30个字符）
    private String ShippingPhone;//收货人电话 备注（荷兰邮政小包挂号非必填，但限制20个字符，只能是数字）

    public String getShippingTaxId() {
        return ShippingTaxId;
    }

    public void setShippingTaxId(String shippingTaxId) {
        ShippingTaxId = shippingTaxId;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    public String getShippingFirstName() {
        return ShippingFirstName;
    }

    public void setShippingFirstName(String shippingFirstName) {
        ShippingFirstName = shippingFirstName;
    }

    public String getShippingLastName() {
        return ShippingLastName;
    }

    public void setShippingLastName(String shippingLastName) {
        ShippingLastName = shippingLastName;
    }

    public String getShippingCompany() {
        return ShippingCompany;
    }

    public void setShippingCompany(String shippingCompany) {
        ShippingCompany = shippingCompany;
    }

    public String getShippingAddress() {
        return ShippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        ShippingAddress = shippingAddress;
    }

    public String getShippingAddress1() {
        return ShippingAddress1;
    }

    public void setShippingAddress1(String shippingAddress1) {
        ShippingAddress1 = shippingAddress1;
    }

    public String getShippingAddress2() {
        return ShippingAddress2;
    }

    public void setShippingAddress2(String shippingAddress2) {
        ShippingAddress2 = shippingAddress2;
    }

    public String getShippingCity() {
        return ShippingCity;
    }

    public void setShippingCity(String shippingCity) {
        ShippingCity = shippingCity;
    }

    public String getShippingState() {
        return ShippingState;
    }

    public void setShippingState(String shippingState) {
        ShippingState = shippingState;
    }

    public String getShippingZip() {
        return ShippingZip;
    }

    public void setShippingZip(String shippingZip) {
        ShippingZip = shippingZip;
    }

    public String getShippingPhone() {
        return ShippingPhone;
    }

    public void setShippingPhone(String shippingPhone) {
        ShippingPhone = shippingPhone;
    }
}
