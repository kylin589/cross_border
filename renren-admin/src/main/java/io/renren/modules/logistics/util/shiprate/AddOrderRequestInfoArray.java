package io.renren.modules.logistics.util.shiprate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * addOrderRequestInfoArray complex type的 Java 类。
 * 
 * <p>
 * 以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="addOrderRequestInfoArray">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="customerOrderNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipperAddressType" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
 *         &lt;element name="recipientOrganization" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="recipientPhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="recipientEmail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="goodsDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="goodsQuantity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="goodsDeclareWorth" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="goodsWeight" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="goodsLength" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="goodsWidth" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="goodsHeight" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="goodsDetails" type="{http://www.example.org/ShipRate/}goodsDetailsArray" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="opDivision" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="orderStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ebayIdentify" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="printSortId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="areaCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="notifyUrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="evaluate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="invNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="taxesNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="taxType" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="isRemoteConfirm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isReturn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="withBattery" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shippingWorth" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="smallLangAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="doorplate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isGetArea" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="matchProduct" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addOrderRequestInfoArray", propOrder = { "customerOrderNo",
		"shipperAddressType", "shipperName", "shipperEmail", "shipperAddress",
		"shipperPhone", "shipperZipCode", "shipperCompanyName",
		"shippingMethod", "recipientCountry", "recipientName",
		"recipientState", "recipientCity", "recipientAddress",
		"recipientZipCode", "recipientOrganization", "recipientPhone",
		"recipientEmail", "goodsDescription", "goodsQuantity",
		"goodsDeclareWorth", "goodsWeight", "goodsLength", "goodsWidth",
		"goodsHeight", "goodsDetails", "opDivision", "orderStatus",
		"ebayIdentify", "printSortId", "areaCode", "notifyUrl", "evaluate",
		"invNo", "taxesNumber", "taxType", "isRemoteConfirm", "isReturn",
		"withBattery", "shippingWorth", "smallLangAddress", "doorplate",
		"isGetArea", "matchProduct", "pieceNumber" })
public class AddOrderRequestInfoArray {

	protected String customerOrderNo;
	protected Integer shipperAddressType;
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
	protected String recipientOrganization;
	protected String recipientPhone;
	protected String recipientEmail;
	protected String goodsDescription;
	protected String goodsQuantity;
	protected String goodsDeclareWorth;
	protected Float goodsWeight;
	protected Float goodsLength;
	protected Float goodsWidth;
	protected Float goodsHeight;
	protected List<GoodsDetailsArray> goodsDetails;
	protected Integer opDivision;
	protected String orderStatus;
	protected String ebayIdentify;
	protected String printSortId;
	protected String areaCode;
	protected String notifyUrl;
	protected String evaluate;
	protected String invNo;
	protected String taxesNumber;
	protected Integer taxType;
	protected String isRemoteConfirm;
	protected String isReturn;
	protected String withBattery;
	protected Float shippingWorth;
	protected String smallLangAddress;
	protected String doorplate;
	protected String isGetArea;
	protected String matchProduct;
	protected String pieceNumber;

	/**
	 * 获取customerOrderNo属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCustomerOrderNo() {
		return customerOrderNo;
	}

	/**
	 * 设置customerOrderNo属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCustomerOrderNo(String value) {
		this.customerOrderNo = value;
	}

	/**
	 * 获取shipperAddressType属性的值。
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getShipperAddressType() {
		return shipperAddressType;
	}

	/**
	 * 设置shipperAddressType属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setShipperAddressType(Integer value) {
		this.shipperAddressType = value;
	}

	/**
	 * 获取shipperName属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getShipperName() {
		return shipperName;
	}

	/**
	 * 设置shipperName属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setShipperName(String value) {
		this.shipperName = value;
	}

	/**
	 * 获取shipperEmail属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getShipperEmail() {
		return shipperEmail;
	}

	/**
	 * 设置shipperEmail属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setShipperEmail(String value) {
		this.shipperEmail = value;
	}

	/**
	 * 获取shipperAddress属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getShipperAddress() {
		return shipperAddress;
	}

	/**
	 * 设置shipperAddress属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setShipperAddress(String value) {
		this.shipperAddress = value;
	}

	/**
	 * 获取shipperPhone属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getShipperPhone() {
		return shipperPhone;
	}

	/**
	 * 设置shipperPhone属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setShipperPhone(String value) {
		this.shipperPhone = value;
	}

	/**
	 * 获取shipperZipCode属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getShipperZipCode() {
		return shipperZipCode;
	}

	/**
	 * 设置shipperZipCode属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setShipperZipCode(String value) {
		this.shipperZipCode = value;
	}

	/**
	 * 获取shipperCompanyName属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getShipperCompanyName() {
		return shipperCompanyName;
	}

	/**
	 * 设置shipperCompanyName属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setShipperCompanyName(String value) {
		this.shipperCompanyName = value;
	}

	/**
	 * 获取shippingMethod属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getShippingMethod() {
		return shippingMethod;
	}

	/**
	 * 设置shippingMethod属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setShippingMethod(String value) {
		this.shippingMethod = value;
	}

	/**
	 * 获取recipientCountry属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRecipientCountry() {
		return recipientCountry;
	}

	/**
	 * 设置recipientCountry属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRecipientCountry(String value) {
		this.recipientCountry = value;
	}

	/**
	 * 获取recipientName属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRecipientName() {
		return recipientName;
	}

	/**
	 * 设置recipientName属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRecipientName(String value) {
		this.recipientName = value;
	}

	/**
	 * 获取recipientState属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRecipientState() {
		return recipientState;
	}

	/**
	 * 设置recipientState属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRecipientState(String value) {
		this.recipientState = value;
	}

	/**
	 * 获取recipientCity属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRecipientCity() {
		return recipientCity;
	}

	/**
	 * 设置recipientCity属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRecipientCity(String value) {
		this.recipientCity = value;
	}

	/**
	 * 获取recipientAddress属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRecipientAddress() {
		return recipientAddress;
	}

	/**
	 * 设置recipientAddress属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRecipientAddress(String value) {
		this.recipientAddress = value;
	}

	/**
	 * 获取recipientZipCode属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRecipientZipCode() {
		return recipientZipCode;
	}

	/**
	 * 设置recipientZipCode属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRecipientZipCode(String value) {
		this.recipientZipCode = value;
	}

	/**
	 * 获取recipientOrganization属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRecipientOrganization() {
		return recipientOrganization;
	}

	/**
	 * 设置recipientOrganization属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRecipientOrganization(String value) {
		this.recipientOrganization = value;
	}

	/**
	 * 获取recipientPhone属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRecipientPhone() {
		return recipientPhone;
	}

	/**
	 * 设置recipientPhone属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRecipientPhone(String value) {
		this.recipientPhone = value;
	}

	/**
	 * 获取recipientEmail属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRecipientEmail() {
		return recipientEmail;
	}

	/**
	 * 设置recipientEmail属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRecipientEmail(String value) {
		this.recipientEmail = value;
	}

	/**
	 * 获取goodsDescription属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getGoodsDescription() {
		return goodsDescription;
	}

	/**
	 * 设置goodsDescription属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setGoodsDescription(String value) {
		this.goodsDescription = value;
	}

	/**
	 * 获取goodsQuantity属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getGoodsQuantity() {
		return goodsQuantity;
	}

	/**
	 * 设置goodsQuantity属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setGoodsQuantity(String value) {
		this.goodsQuantity = value;
	}

	/**
	 * 获取goodsDeclareWorth属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getGoodsDeclareWorth() {
		return goodsDeclareWorth;
	}

	/**
	 * 设置goodsDeclareWorth属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setGoodsDeclareWorth(String value) {
		this.goodsDeclareWorth = value;
	}

	/**
	 * 获取goodsWeight属性的值。
	 * 
	 * @return possible object is {@link Float }
	 * 
	 */
	public Float getGoodsWeight() {
		return goodsWeight;
	}

	/**
	 * 设置goodsWeight属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link Float }
	 * 
	 */
	public void setGoodsWeight(Float value) {
		this.goodsWeight = value;
	}

	/**
	 * 获取goodsLength属性的值。
	 * 
	 * @return possible object is {@link Float }
	 * 
	 */
	public Float getGoodsLength() {
		return goodsLength;
	}

	/**
	 * 设置goodsLength属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link Float }
	 * 
	 */
	public void setGoodsLength(Float value) {
		this.goodsLength = value;
	}

	/**
	 * 获取goodsWidth属性的值。
	 * 
	 * @return possible object is {@link Float }
	 * 
	 */
	public Float getGoodsWidth() {
		return goodsWidth;
	}

	/**
	 * 设置goodsWidth属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link Float }
	 * 
	 */
	public void setGoodsWidth(Float value) {
		this.goodsWidth = value;
	}

	/**
	 * 获取goodsHeight属性的值。
	 * 
	 * @return possible object is {@link Float }
	 * 
	 */
	public Float getGoodsHeight() {
		return goodsHeight;
	}

	/**
	 * 设置goodsHeight属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link Float }
	 * 
	 */
	public void setGoodsHeight(Float value) {
		this.goodsHeight = value;
	}

	/**
	 * Gets the value of the goodsDetails property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the goodsDetails property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getGoodsDetails().add(newItem);
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

	public void setGoodsDetails(List<GoodsDetailsArray> goodsDetails) {
		this.goodsDetails = goodsDetails;
	}

	/**
	 * 获取opDivision属性的值。
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getOpDivision() {
		return opDivision;
	}

	/**
	 * 设置opDivision属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setOpDivision(Integer value) {
		this.opDivision = value;
	}

	/**
	 * 获取orderStatus属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOrderStatus() {
		return orderStatus;
	}

	/**
	 * 设置orderStatus属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setOrderStatus(String value) {
		this.orderStatus = value;
	}

	/**
	 * 获取ebayIdentify属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getEbayIdentify() {
		return ebayIdentify;
	}

	/**
	 * 设置ebayIdentify属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setEbayIdentify(String value) {
		this.ebayIdentify = value;
	}

	/**
	 * 获取printSortId属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPrintSortId() {
		return printSortId;
	}

	/**
	 * 设置printSortId属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPrintSortId(String value) {
		this.printSortId = value;
	}

	/**
	 * 获取areaCode属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAreaCode() {
		return areaCode;
	}

	/**
	 * 设置areaCode属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAreaCode(String value) {
		this.areaCode = value;
	}

	/**
	 * 获取notifyUrl属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNotifyUrl() {
		return notifyUrl;
	}

	/**
	 * 设置notifyUrl属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setNotifyUrl(String value) {
		this.notifyUrl = value;
	}

	/**
	 * 获取evaluate属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getEvaluate() {
		return evaluate;
	}

	/**
	 * 设置evaluate属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setEvaluate(String value) {
		this.evaluate = value;
	}

	/**
	 * 获取invNo属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getInvNo() {
		return invNo;
	}

	/**
	 * 设置invNo属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setInvNo(String value) {
		this.invNo = value;
	}

	/**
	 * 获取taxesNumber属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTaxesNumber() {
		return taxesNumber;
	}

	/**
	 * 设置taxesNumber属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTaxesNumber(String value) {
		this.taxesNumber = value;
	}

	/**
	 * 获取taxType属性的值。
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getTaxType() {
		return taxType;
	}

	/**
	 * 设置taxType属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setTaxType(Integer value) {
		this.taxType = value;
	}

	/**
	 * 获取isRemoteConfirm属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIsRemoteConfirm() {
		return isRemoteConfirm;
	}

	/**
	 * 设置isRemoteConfirm属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setIsRemoteConfirm(String value) {
		this.isRemoteConfirm = value;
	}

	/**
	 * 获取isReturn属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIsReturn() {
		return isReturn;
	}

	/**
	 * 设置isReturn属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setIsReturn(String value) {
		this.isReturn = value;
	}

	/**
	 * 获取withBattery属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getWithBattery() {
		return withBattery;
	}

	/**
	 * 设置withBattery属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setWithBattery(String value) {
		this.withBattery = value;
	}

	/**
	 * 获取shippingWorth属性的值。
	 * 
	 * @return possible object is {@link Float }
	 * 
	 */
	public Float getShippingWorth() {
		return shippingWorth;
	}

	/**
	 * 设置shippingWorth属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link Float }
	 * 
	 */
	public void setShippingWorth(Float value) {
		this.shippingWorth = value;
	}

	/**
	 * 获取smallLangAddress属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSmallLangAddress() {
		return smallLangAddress;
	}

	/**
	 * 设置smallLangAddress属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSmallLangAddress(String value) {
		this.smallLangAddress = value;
	}

	/**
	 * 获取doorplate属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDoorplate() {
		return doorplate;
	}

	/**
	 * 设置doorplate属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDoorplate(String value) {
		this.doorplate = value;
	}

	/**
	 * 获取isGetArea属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIsGetArea() {
		return isGetArea;
	}

	/**
	 * 设置isGetArea属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setIsGetArea(String value) {
		this.isGetArea = value;
	}

	/**
	 * 获取matchProduct属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMatchProduct() {
		return matchProduct;
	}

	/**
	 * 设置matchProduct属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMatchProduct(String value) {
		this.matchProduct = value;
	}

	public String getPieceNumber() {
		return pieceNumber;
	}

	public void setPieceNumber(String pieceNumber) {
		this.pieceNumber = pieceNumber;
	}

}
