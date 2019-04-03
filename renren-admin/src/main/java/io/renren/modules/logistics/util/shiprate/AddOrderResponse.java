package io.renren.modules.logistics.util.shiprate;

import javax.xml.bind.annotation.*;

/**
 * <p>
 * anonymous complex type的 Java 类。
 * 
 * <p>
 * 以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="orderCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerOrderNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="operatingTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="orderActionStatus" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="exceptionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="notifyUrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="note" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="alert" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="area" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="trackingNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="trackingNumberUsps" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ae_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "orderCode", "customerOrderNo",
		"operatingTime", "orderActionStatus", "exceptionCode", "notifyUrl",
		"note", "alert", "area", "trackingNumber", "trackingNumberUsps",
		"ae_code" })
@XmlRootElement(name = "addOrderResponse")
public class AddOrderResponse {

	protected String orderCode;
	@XmlElement(required = true)
	protected String customerOrderNo;
	@XmlElement(required = true)
	protected String operatingTime;
	@XmlElement(required = true)
	protected String orderActionStatus;
	protected String exceptionCode;
	protected String notifyUrl;
	protected String note;
	protected String alert;
	protected String area;
	protected String trackingNumber;
	protected String trackingNumberUsps;
	protected String ae_code;

	public String getTrackingNumberUsps() {
		return trackingNumberUsps;
	}

	public void setTrackingNumberUsps(String trackingNumberUsps) {
		this.trackingNumberUsps = trackingNumberUsps;
	}

	public String getAe_code() {
		return ae_code;
	}

	public void setAe_code(String ae_code) {
		this.ae_code = ae_code;
	}

	/**
	 * 获取orderCode属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOrderCode() {
		return orderCode;
	}

	/**
	 * 设置orderCode属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setOrderCode(String value) {
		this.orderCode = value;
	}

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
	 * 获取operatingTime属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOperatingTime() {
		return operatingTime;
	}

	/**
	 * 设置operatingTime属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setOperatingTime(String value) {
		this.operatingTime = value;
	}

	/**
	 * 获取orderActionStatus属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOrderActionStatus() {
		return orderActionStatus;
	}

	/**
	 * 设置orderActionStatus属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setOrderActionStatus(String value) {
		this.orderActionStatus = value;
	}

	/**
	 * 获取exceptionCode属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getExceptionCode() {
		return exceptionCode;
	}

	/**
	 * 设置exceptionCode属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setExceptionCode(String value) {
		this.exceptionCode = value;
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
	 * 获取note属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNote() {
		return note;
	}

	/**
	 * 设置note属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setNote(String value) {
		this.note = value;
	}

	/**
	 * 获取alert属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAlert() {
		return alert;
	}

	/**
	 * 设置alert属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAlert(String value) {
		this.alert = value;
	}

	/**
	 * 获取area属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getArea() {
		return area;
	}

	/**
	 * 设置area属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setArea(String value) {
		this.area = value;
	}

	/**
	 * 获取trackingNumber属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTrackingNumber() {
		return trackingNumber;
	}

	/**
	 * 设置trackingNumber属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTrackingNumber(String value) {
		this.trackingNumber = value;
	}

}
