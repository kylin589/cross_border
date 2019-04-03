
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
 *         &lt;element name="getInventoryRequestInfo" type="{http://www.example.org/ShipRate/}getInventoryRequestInfoArray"/>
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
    "getInventoryRequestInfo"
})
@XmlRootElement(name = "getInventoryRequest")
public class GetInventoryRequest {

    @XmlElement(name = "HeaderRequest", required = true)
    protected HeaderRequest headerRequest;
    @XmlElement(required = true)
    protected GetInventoryRequestInfoArray getInventoryRequestInfo;

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
     * 获取getInventoryRequestInfo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link GetInventoryRequestInfoArray }
     *     
     */
    public GetInventoryRequestInfoArray getGetInventoryRequestInfo() {
        return getInventoryRequestInfo;
    }

    /**
     * 设置getInventoryRequestInfo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link GetInventoryRequestInfoArray }
     *     
     */
    public void setGetInventoryRequestInfo(GetInventoryRequestInfoArray value) {
        this.getInventoryRequestInfo = value;
    }

}
