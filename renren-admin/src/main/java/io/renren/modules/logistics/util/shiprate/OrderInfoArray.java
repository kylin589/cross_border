
package io.renren.modules.logistics.util.shiprate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>orderInfoArray complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="orderInfoArray">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="orderCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerOrderNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipperName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipperEmail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipperAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipperPhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipperZipCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipperCompanyName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shippingMethod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="recipientCountry" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="recipientName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="recipientState" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="recipientCity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="recipientAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="recipientZipCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="recipientPhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="recipientEmail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="goodsDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="goodsQuantity" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="goodsDeclareWorth" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="goodsWeight" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="goodsLength" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="goodsWidth" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="goodsHeight" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="orderStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orderStatusCn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="addTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deliveryTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sendTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="trackNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subShipType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="goodsDetails" type="{http://www.example.org/ShipRate/}goodsDetailsArray" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "orderInfoArray", propOrder = {
    "orderCode",
    "customerOrderNo",
    "shipperName",
    "shipperEmail",
    "shipperAddress",
    "shipperPhone",
    "shipperZipCode",
    "shipperCompanyName",
    "shippingMethod",
    "recipientCountry",
    "recipientName",
    "recipientState",
    "recipientCity",
    "recipientAddress",
    "recipientZipCode",
    "recipientPhone",
    "recipientEmail",
    "goodsDescription",
    "goodsQuantity",
    "goodsDeclareWorth",
    "goodsWeight",
    "goodsLength",
    "goodsWidth",
    "goodsHeight",
    "orderStatus",
    "orderStatusCn",
    "addTime",
    "deliveryTime",
    "sendTime",
    "trackNumber",
    "subShipType",
    "goodsDetails"
})
public class OrderInfoArray {

    protected String orderCode;
    protected String customerOrderNo;
    protected String shipperName;
    protected String shipperEmail;
    protected String shipperAddress;
    protected String shipperPhone;
    protected String shipperZipCode;
    protected String shipperCompanyName;
    protected String shippingMethod;
    protected String recipientCountry;
    protected String recipientName;
    protected String recipientState;
    protected String recipientCity;
    protected String recipientAddress;
    protected String recipientZipCode;
    protected String recipientPhone;
    protected String recipientEmail;
    protected String goodsDescription;
    protected Integer goodsQuantity;
    protected Float goodsDeclareWorth;
    protected Float goodsWeight;
    protected Float goodsLength;
    protected Float goodsWidth;
    protected Float goodsHeight;
    protected String orderStatus;
    protected String orderStatusCn;
    protected String addTime;
    protected String deliveryTime;
    protected String sendTime;
    protected String trackNumber;
    protected String subShipType;
    protected List<GoodsDetailsArray> goodsDetails;

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
     * 获取customerOrderNo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerOrderNo() {
        return customerOrderNo;
    }

    /**
     * 设置customerOrderNo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerOrderNo(String value) {
        this.customerOrderNo = value;
    }

    /**
     * 获取shipperName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipperName() {
        return shipperName;
    }

    /**
     * 设置shipperName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipperName(String value) {
        this.shipperName = value;
    }

    /**
     * 获取shipperEmail属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipperEmail() {
        return shipperEmail;
    }

    /**
     * 设置shipperEmail属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipperEmail(String value) {
        this.shipperEmail = value;
    }

    /**
     * 获取shipperAddress属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipperAddress() {
        return shipperAddress;
    }

    /**
     * 设置shipperAddress属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipperAddress(String value) {
        this.shipperAddress = value;
    }

    /**
     * 获取shipperPhone属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipperPhone() {
        return shipperPhone;
    }

    /**
     * 设置shipperPhone属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipperPhone(String value) {
        this.shipperPhone = value;
    }

    /**
     * 获取shipperZipCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipperZipCode() {
        return shipperZipCode;
    }

    /**
     * 设置shipperZipCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipperZipCode(String value) {
        this.shipperZipCode = value;
    }

    /**
     * 获取shipperCompanyName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipperCompanyName() {
        return shipperCompanyName;
    }

    /**
     * 设置shipperCompanyName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipperCompanyName(String value) {
        this.shipperCompanyName = value;
    }

    /**
     * 获取shippingMethod属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShippingMethod() {
        return shippingMethod;
    }

    /**
     * 设置shippingMethod属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShippingMethod(String value) {
        this.shippingMethod = value;
    }

    /**
     * 获取recipientCountry属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecipientCountry() {
        return recipientCountry;
    }

    /**
     * 设置recipientCountry属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecipientCountry(String value) {
        this.recipientCountry = value;
    }

    /**
     * 获取recipientName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecipientName() {
        return recipientName;
    }

    /**
     * 设置recipientName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecipientName(String value) {
        this.recipientName = value;
    }

    /**
     * 获取recipientState属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecipientState() {
        return recipientState;
    }

    /**
     * 设置recipientState属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecipientState(String value) {
        this.recipientState = value;
    }

    /**
     * 获取recipientCity属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecipientCity() {
        return recipientCity;
    }

    /**
     * 设置recipientCity属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecipientCity(String value) {
        this.recipientCity = value;
    }

    /**
     * 获取recipientAddress属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecipientAddress() {
        return recipientAddress;
    }

    /**
     * 设置recipientAddress属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecipientAddress(String value) {
        this.recipientAddress = value;
    }

    /**
     * 获取recipientZipCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecipientZipCode() {
        return recipientZipCode;
    }

    /**
     * 设置recipientZipCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecipientZipCode(String value) {
        this.recipientZipCode = value;
    }

    /**
     * 获取recipientPhone属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecipientPhone() {
        return recipientPhone;
    }

    /**
     * 设置recipientPhone属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecipientPhone(String value) {
        this.recipientPhone = value;
    }

    /**
     * 获取recipientEmail属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecipientEmail() {
        return recipientEmail;
    }

    /**
     * 设置recipientEmail属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecipientEmail(String value) {
        this.recipientEmail = value;
    }

    /**
     * 获取goodsDescription属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGoodsDescription() {
        return goodsDescription;
    }

    /**
     * 设置goodsDescription属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGoodsDescription(String value) {
        this.goodsDescription = value;
    }

    /**
     * 获取goodsQuantity属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getGoodsQuantity() {
        return goodsQuantity;
    }

    /**
     * 设置goodsQuantity属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setGoodsQuantity(Integer value) {
        this.goodsQuantity = value;
    }

    /**
     * 获取goodsDeclareWorth属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getGoodsDeclareWorth() {
        return goodsDeclareWorth;
    }

    /**
     * 设置goodsDeclareWorth属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setGoodsDeclareWorth(Float value) {
        this.goodsDeclareWorth = value;
    }

    /**
     * 获取goodsWeight属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getGoodsWeight() {
        return goodsWeight;
    }

    /**
     * 设置goodsWeight属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setGoodsWeight(Float value) {
        this.goodsWeight = value;
    }

    /**
     * 获取goodsLength属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getGoodsLength() {
        return goodsLength;
    }

    /**
     * 设置goodsLength属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setGoodsLength(Float value) {
        this.goodsLength = value;
    }

    /**
     * 获取goodsWidth属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getGoodsWidth() {
        return goodsWidth;
    }

    /**
     * 设置goodsWidth属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setGoodsWidth(Float value) {
        this.goodsWidth = value;
    }

    /**
     * 获取goodsHeight属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getGoodsHeight() {
        return goodsHeight;
    }

    /**
     * 设置goodsHeight属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setGoodsHeight(Float value) {
        this.goodsHeight = value;
    }

    /**
     * 获取orderStatus属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderStatus() {
        return orderStatus;
    }

    /**
     * 设置orderStatus属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderStatus(String value) {
        this.orderStatus = value;
    }

    /**
     * 获取orderStatusCn属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderStatusCn() {
        return orderStatusCn;
    }

    /**
     * 设置orderStatusCn属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderStatusCn(String value) {
        this.orderStatusCn = value;
    }

    /**
     * 获取addTime属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddTime() {
        return addTime;
    }

    /**
     * 设置addTime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddTime(String value) {
        this.addTime = value;
    }

    /**
     * 获取deliveryTime属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryTime() {
        return deliveryTime;
    }

    /**
     * 设置deliveryTime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryTime(String value) {
        this.deliveryTime = value;
    }

    /**
     * 获取sendTime属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSendTime() {
        return sendTime;
    }

    /**
     * 设置sendTime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSendTime(String value) {
        this.sendTime = value;
    }

    /**
     * 获取trackNumber属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrackNumber() {
        return trackNumber;
    }

    /**
     * 设置trackNumber属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrackNumber(String value) {
        this.trackNumber = value;
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
     * Gets the value of the goodsDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the goodsDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGoodsDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GoodsDetailsArray }
     * 
     * 
     */
    public List<GoodsDetailsArray> getGoodsDetails() {
        if (goodsDetails == null) {
            goodsDetails = new ArrayList<GoodsDetailsArray>();
        }
        return this.goodsDetails;
    }

}
