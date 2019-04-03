
package io.renren.modules.logistics.util.shiprate;

import javax.xml.bind.annotation.*;


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
 *         &lt;element name="HeaderRequest" type="{http://www.example.org/ShipRate/}HeaderRequest"/>
 *         &lt;element name="reSetLabelInfo" type="{http://www.example.org/ShipRate/}reSetLabelInfoArray"/>
 *         &lt;element name="orderCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "headerRequest",
    "reSetLabelInfo",
    "orderCode"
})
@XmlRootElement(name = "reSetLabel")
public class ReSetLabel {

    @XmlElement(name = "HeaderRequest", required = true)
    protected HeaderRequest headerRequest;
    @XmlElement(required = true)
    protected ReSetLabelInfoArray reSetLabelInfo;
    @XmlElement(required = true)
    protected String orderCode;

    /**
     * 获取headerRequest属性的值。
     * 
     * @return
     *     possible object is
     *     {@link HeaderRequest }
     *     
     */
    public HeaderRequest getHeaderRequest() {
        return headerRequest;
    }

    /**
     * 设置headerRequest属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link HeaderRequest }
     *     
     */
    public void setHeaderRequest(HeaderRequest value) {
        this.headerRequest = value;
    }

    /**
     * 获取reSetLabelInfo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ReSetLabelInfoArray }
     *     
     */
    public ReSetLabelInfoArray getReSetLabelInfo() {
        return reSetLabelInfo;
    }

    /**
     * 设置reSetLabelInfo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ReSetLabelInfoArray }
     *     
     */
    public void setReSetLabelInfo(ReSetLabelInfoArray value) {
        this.reSetLabelInfo = value;
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

}
