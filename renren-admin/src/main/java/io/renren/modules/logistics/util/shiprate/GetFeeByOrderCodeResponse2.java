
package io.renren.modules.logistics.util.shiprate;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ask" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sysTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orderCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="msg" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="baseFee" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="regFee" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dealFee" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="insurance" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="totalFee" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="currencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="chargebackTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="chargebackWorkDay" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipTypeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subShipType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="waybillCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="discount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="otherFee" type="{http://www.example.org/ShipRate/}otherFee" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="originBaseFee" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="originAddons" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="stDealFee" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="stRegFee" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="feeWeight" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "ask",
    "sysTime",
    "orderCode",
    "msg",
    "baseFee",
    "regFee",
    "dealFee",
    "insurance",
    "totalFee",
    "currencyCode",
    "chargebackTime",
    "chargebackWorkDay",
    "shipTypeCode",
    "subShipType",
    "waybillCode",
    "discount",
    "otherFee",
    "originBaseFee",
    "originAddons",
    "stDealFee",
    "stRegFee",
    "feeWeight"
})
@XmlRootElement(name = "getFeeByOrderCodeResponse")
public class GetFeeByOrderCodeResponse2 {

    @XmlElement(required = true)
    protected String ask;
    protected String sysTime;
    @XmlElement(required = true)
    protected String orderCode;
    @XmlElement(required = true)
    protected String msg;
    protected String baseFee;
    protected String regFee;
    protected String dealFee;
    protected String insurance;
    protected String totalFee;
    protected String currencyCode;
    protected String chargebackTime;
    protected String chargebackWorkDay;
    protected String shipTypeCode;
    protected String subShipType;
    protected String waybillCode;
    protected String discount;
    protected List<OtherFee> otherFee;
    protected String originBaseFee;
    protected String originAddons;
    protected String stDealFee;
    protected String stRegFee;
    protected String feeWeight;

    /**
     * 获取ask属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAsk() {
        return ask;
    }

    /**
     * 设置ask属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAsk(String value) {
        this.ask = value;
    }

    /**
     * 获取sysTime属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSysTime() {
        return sysTime;
    }

    /**
     * 设置sysTime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSysTime(String value) {
        this.sysTime = value;
    }

    /**
     * 获取orderCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderCode() {
        return orderCode;
    }

    /**
     * 设置orderCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderCode(String value) {
        this.orderCode = value;
    }

    /**
     * 获取msg属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置msg属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsg(String value) {
        this.msg = value;
    }

    /**
     * 获取baseFee属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBaseFee() {
        return baseFee;
    }

    /**
     * 设置baseFee属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBaseFee(String value) {
        this.baseFee = value;
    }

    /**
     * 获取regFee属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegFee() {
        return regFee;
    }

    /**
     * 设置regFee属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegFee(String value) {
        this.regFee = value;
    }

    /**
     * 获取dealFee属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDealFee() {
        return dealFee;
    }

    /**
     * 设置dealFee属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDealFee(String value) {
        this.dealFee = value;
    }

    /**
     * 获取insurance属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInsurance() {
        return insurance;
    }

    /**
     * 设置insurance属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInsurance(String value) {
        this.insurance = value;
    }

    /**
     * 获取totalFee属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalFee() {
        return totalFee;
    }

    /**
     * 设置totalFee属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalFee(String value) {
        this.totalFee = value;
    }

    /**
     * 获取currencyCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * 设置currencyCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyCode(String value) {
        this.currencyCode = value;
    }

    /**
     * 获取chargebackTime属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChargebackTime() {
        return chargebackTime;
    }

    /**
     * 设置chargebackTime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChargebackTime(String value) {
        this.chargebackTime = value;
    }

    /**
     * 获取chargebackWorkDay属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChargebackWorkDay() {
        return chargebackWorkDay;
    }

    /**
     * 设置chargebackWorkDay属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChargebackWorkDay(String value) {
        this.chargebackWorkDay = value;
    }

    /**
     * 获取shipTypeCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipTypeCode() {
        return shipTypeCode;
    }

    /**
     * 设置shipTypeCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipTypeCode(String value) {
        this.shipTypeCode = value;
    }

    /**
     * 获取subShipType属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubShipType() {
        return subShipType;
    }

    /**
     * 设置subShipType属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubShipType(String value) {
        this.subShipType = value;
    }

    /**
     * 获取waybillCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWaybillCode() {
        return waybillCode;
    }

    /**
     * 设置waybillCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWaybillCode(String value) {
        this.waybillCode = value;
    }

    /**
     * 获取discount属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDiscount() {
        return discount;
    }

    /**
     * 设置discount属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiscount(String value) {
        this.discount = value;
    }

    /**
     * Gets the value of the otherFee property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the otherFee property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOtherFee().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OtherFee }
     * 
     * 
     */
    public List<OtherFee> getOtherFee() {
        if (otherFee == null) {
            otherFee = new ArrayList<OtherFee>();
        }
        return this.otherFee;
    }

    /**
     * 获取originBaseFee属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginBaseFee() {
        return originBaseFee;
    }

    /**
     * 设置originBaseFee属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginBaseFee(String value) {
        this.originBaseFee = value;
    }

    /**
     * 获取originAddons属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginAddons() {
        return originAddons;
    }

    /**
     * 设置originAddons属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginAddons(String value) {
        this.originAddons = value;
    }

    /**
     * 获取stDealFee属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStDealFee() {
        return stDealFee;
    }

    /**
     * 设置stDealFee属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStDealFee(String value) {
        this.stDealFee = value;
    }

    /**
     * 获取stRegFee属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStRegFee() {
        return stRegFee;
    }

    /**
     * 设置stRegFee属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStRegFee(String value) {
        this.stRegFee = value;
    }

    /**
     * 获取feeWeight属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFeeWeight() {
        return feeWeight;
    }

    /**
     * 设置feeWeight属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFeeWeight(String value) {
        this.feeWeight = value;
    }

}
