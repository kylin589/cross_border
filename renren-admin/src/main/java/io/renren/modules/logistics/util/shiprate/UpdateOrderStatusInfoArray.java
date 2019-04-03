package io.renren.modules.logistics.util.shiprate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * UpdateOrderStatusInfoArray complex type的 Java 类。
 * 
 * <p>
 * 以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="UpdateOrderStatusInfoArray">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="orderStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sendDateTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="authenticate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="trackingNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateOrderStatusInfoArray", propOrder = { "orderStatus",
		"sendDateTime", "authenticate", "trackingNumber" })
public class UpdateOrderStatusInfoArray {

	protected String orderStatus;
	protected String sendDateTime;
	protected String authenticate;
	protected String trackingNumber;

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
	 * 获取sendDateTime属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSendDateTime() {
		return sendDateTime;
	}

	/**
	 * 设置sendDateTime属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSendDateTime(String value) {
		this.sendDateTime = value;
	}

	/**
	 * 获取authenticate属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAuthenticate() {
		return authenticate;
	}

	/**
	 * 设置authenticate属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAuthenticate(String value) {
		this.authenticate = value;
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
