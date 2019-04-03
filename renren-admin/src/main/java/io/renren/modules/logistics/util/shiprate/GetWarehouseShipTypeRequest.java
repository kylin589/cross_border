
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
 *         &lt;element name="getWarehouseShipTypeRequestInfo" type="{http://www.example.org/ShipRate/}getWarehouseShipTypeRequestInfoArray"/>
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
    "getWarehouseShipTypeRequestInfo"
})
@XmlRootElement(name = "getWarehouseShipTypeRequest")
public class GetWarehouseShipTypeRequest {

    @XmlElement(name = "HeaderRequest", required = true)
    protected HeaderRequest headerRequest;
    @XmlElement(required = true)
    protected GetWarehouseShipTypeRequestInfoArray getWarehouseShipTypeRequestInfo;

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
     * 获取getWarehouseShipTypeRequestInfo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link GetWarehouseShipTypeRequestInfoArray }
     *     
     */
    public GetWarehouseShipTypeRequestInfoArray getGetWarehouseShipTypeRequestInfo() {
        return getWarehouseShipTypeRequestInfo;
    }

    /**
     * 设置getWarehouseShipTypeRequestInfo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link GetWarehouseShipTypeRequestInfoArray }
     *     
     */
    public void setGetWarehouseShipTypeRequestInfo(GetWarehouseShipTypeRequestInfoArray value) {
        this.getWarehouseShipTypeRequestInfo = value;
    }

}
