
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
 *         &lt;element name="updateOrderVolumeWeight" type="{http://www.example.org/ShipRate/}updateOrderVolumeWeight"/>
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
    "updateOrderVolumeWeight"
})
@XmlRootElement(name = "updateOrderVolumeWeightRequest")
public class UpdateOrderVolumeWeightRequest {

    @XmlElement(name = "HeaderRequest", required = true)
    protected HeaderRequest headerRequest;
    @XmlElement(required = true)
    protected UpdateOrderVolumeWeight updateOrderVolumeWeight;

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
     * 获取updateOrderVolumeWeight属性的值。
     * 
     * @return
     *     possible object is
     *     {@link UpdateOrderVolumeWeight }
     *     
     */
    public UpdateOrderVolumeWeight getUpdateOrderVolumeWeight() {
        return updateOrderVolumeWeight;
    }

    /**
     * 设置updateOrderVolumeWeight属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link UpdateOrderVolumeWeight }
     *     
     */
    public void setUpdateOrderVolumeWeight(UpdateOrderVolumeWeight value) {
        this.updateOrderVolumeWeight = value;
    }

}
