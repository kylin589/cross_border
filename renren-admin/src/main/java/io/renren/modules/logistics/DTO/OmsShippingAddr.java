package io.renren.modules.logistics.DTO;

/**
 * @Auther: wdh
 * 收件人地址
 * @Date: 2018/12/13 14:18
 * @Description:
 */
public class OmsShippingAddr {
    /**
    * 顾客-收件人
    */
    public String customer;
    /**
    * 收货公司
    */
    public String custcompany;
    /**
    * 销售国家
    */
    public String custcountry;
    /**
    * 州名
    */
    public String custstate;
    /**
    * 城市
    */
    public String custcity;
    /**
    * 邮编
    */
    public String custzipcode;
    /**
    * 电话
    */
    public String custphone;
    /**
    * 详细地址
    */
    public String custaddress;
    public String address_line1;
    public String address_line2 = null;
    public String address_line3 = null;

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCustcompany() {
        return custcompany;
    }

    public void setCustcompany(String custcompany) {
        this.custcompany = custcompany;
    }

    public String getCustcountry() {
        return custcountry;
    }

    public void setCustcountry(String custcountry) {
        this.custcountry = custcountry;
    }

    public String getCuststate() {
        return custstate;
    }

    public void setCuststate(String custstate) {
        this.custstate = custstate;
    }

    public String getCustcity() {
        return custcity;
    }

    public void setCustcity(String custcity) {
        this.custcity = custcity;
    }

    public String getCustzipcode() {
        return custzipcode;
    }

    public void setCustzipcode(String custzipcode) {
        this.custzipcode = custzipcode;
    }

    public String getCustphone() {
        return custphone;
    }

    public void setCustphone(String custphone) {
        this.custphone = custphone;
    }

    public String getCustaddress() {
        return custaddress;
    }

    public void setCustaddress(String custaddress) {
        this.custaddress = custaddress;
    }

    public String getAddress_line1() {
        return address_line1;
    }

    public void setAddress_line1(String address_line1) {
        this.address_line1 = address_line1;
    }

    public String getAddress_line2() {
        return address_line2;
    }

    public void setAddress_line2(String address_line2) {
        this.address_line2 = address_line2;
    }

    public String getAddress_line3() {
        return address_line3;
    }

    public void setAddress_line3(String address_line3) {
        this.address_line3 = address_line3;
    }
}
