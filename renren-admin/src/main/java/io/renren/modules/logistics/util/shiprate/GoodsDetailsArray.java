package io.renren.modules.logistics.util.shiprate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * goodsDetailsArray complex type的 Java 类。
 * 
 * <p>
 * 以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="goodsDetailsArray">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element name="detailDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="detailQuantity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="detailCustomLabel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="detailWorth" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="detailWeight" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="detailEbayTxnId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="detailEbayItemId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="detailEbayUserId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="detailEbayPayDate" type="{http://www.w3.org/2001/XMLSchema}time" minOccurs="0"/>
 *         &lt;element name="detailEbaySoldDate" type="{http://www.w3.org/2001/XMLSchema}time" minOccurs="0"/>
 *         &lt;element name="detailDescriptionCN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="detailName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="hsCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="origin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="enMaterial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cnMaterial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="boxId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "goodsDetailsArray", propOrder = { "detailDescription",
		"detailQuantity", "detailCustomLabel", "detailWorth", "detailWeight",
		"detailEbayTxnId", "detailEbayItemId", "detailEbayUserId",
		"detailEbayPayDate", "detailEbaySoldDate", "detailDescriptionCN",
		"detailName", "hsCode", "origin", "enMaterial", "cnMaterial", "boxId",
		"priceTag" })
public class GoodsDetailsArray {

	protected String detailDescription;
	protected String detailQuantity;
	protected String detailCustomLabel;
	protected String detailWorth;
	protected Float detailWeight;
	protected String detailEbayTxnId;
	protected String detailEbayItemId;
	protected String detailEbayUserId;
	@XmlSchemaType(name = "time")
	protected XMLGregorianCalendar detailEbayPayDate;
	@XmlSchemaType(name = "time")
	protected XMLGregorianCalendar detailEbaySoldDate;
	protected String detailDescriptionCN;
	protected String detailName;
	protected String hsCode;
	protected String origin;
	protected String enMaterial;
	protected String cnMaterial;
	protected Integer boxId;
	protected String priceTag;

	/**
	 * 获取detailDescription属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDetailDescription() {
		return detailDescription;
	}

	/**
	 * 设置detailDescription属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDetailDescription(String value) {
		this.detailDescription = value;
	}

	/**
	 * 获取detailQuantity属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDetailQuantity() {
		return detailQuantity;
	}

	/**
	 * 设置detailQuantity属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDetailQuantity(String value) {
		this.detailQuantity = value;
	}

	/**
	 * 获取detailCustomLabel属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDetailCustomLabel() {
		return detailCustomLabel;
	}

	/**
	 * 设置detailCustomLabel属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDetailCustomLabel(String value) {
		this.detailCustomLabel = value;
	}

	/**
	 * 获取detailWorth属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDetailWorth() {
		return detailWorth;
	}

	/**
	 * 设置detailWorth属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDetailWorth(String value) {
		this.detailWorth = value;
	}

	/**
	 * 获取detailWeight属性的值。
	 * 
	 * @return possible object is {@link Float }
	 * 
	 */
	public Float getDetailWeight() {
		return detailWeight;
	}

	/**
	 * 设置detailWeight属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link Float }
	 * 
	 */
	public void setDetailWeight(Float value) {
		this.detailWeight = value;
	}

	/**
	 * 获取detailEbayTxnId属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDetailEbayTxnId() {
		return detailEbayTxnId;
	}

	/**
	 * 设置detailEbayTxnId属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDetailEbayTxnId(String value) {
		this.detailEbayTxnId = value;
	}

	/**
	 * 获取detailEbayItemId属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDetailEbayItemId() {
		return detailEbayItemId;
	}

	/**
	 * 设置detailEbayItemId属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDetailEbayItemId(String value) {
		this.detailEbayItemId = value;
	}

	/**
	 * 获取detailEbayUserId属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDetailEbayUserId() {
		return detailEbayUserId;
	}

	/**
	 * 设置detailEbayUserId属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDetailEbayUserId(String value) {
		this.detailEbayUserId = value;
	}

	/**
	 * 获取detailEbayPayDate属性的值。
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getDetailEbayPayDate() {
		return detailEbayPayDate;
	}

	/**
	 * 设置detailEbayPayDate属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDetailEbayPayDate(XMLGregorianCalendar value) {
		this.detailEbayPayDate = value;
	}

	/**
	 * 获取detailEbaySoldDate属性的值。
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getDetailEbaySoldDate() {
		return detailEbaySoldDate;
	}

	/**
	 * 设置detailEbaySoldDate属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDetailEbaySoldDate(XMLGregorianCalendar value) {
		this.detailEbaySoldDate = value;
	}

	/**
	 * 获取detailDescriptionCN属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDetailDescriptionCN() {
		return detailDescriptionCN;
	}

	/**
	 * 设置detailDescriptionCN属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDetailDescriptionCN(String value) {
		this.detailDescriptionCN = value;
	}

	/**
	 * 获取detailName属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDetailName() {
		return detailName;
	}

	/**
	 * 设置detailName属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDetailName(String value) {
		this.detailName = value;
	}

	/**
	 * 获取hsCode属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getHsCode() {
		return hsCode;
	}

	/**
	 * 设置hsCode属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setHsCode(String value) {
		this.hsCode = value;
	}

	/**
	 * 获取origin属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * 设置origin属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setOrigin(String value) {
		this.origin = value;
	}

	/**
	 * 获取enMaterial属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getEnMaterial() {
		return enMaterial;
	}

	/**
	 * 设置enMaterial属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setEnMaterial(String value) {
		this.enMaterial = value;
	}

	/**
	 * 获取cnMaterial属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCnMaterial() {
		return cnMaterial;
	}

	/**
	 * 设置cnMaterial属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCnMaterial(String value) {
		this.cnMaterial = value;
	}

	/**
	 * 获取boxId属性的值。
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getBoxId() {
		return boxId;
	}

	/**
	 * 设置boxId属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setBoxId(Integer value) {
		this.boxId = value;
	}

	/**
	 * 设置MRP价值
	 * 
	 * @return
	 */
	public String getPriceTag() {
		return priceTag;
	}

	/**
	 * 获取MRP价值
	 * 
	 * @param priceTag
	 */
	public void setPriceTag(String priceTag) {
		this.priceTag = priceTag;
	}

}
