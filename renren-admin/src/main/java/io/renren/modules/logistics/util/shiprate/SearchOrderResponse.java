
package io.renren.modules.logistics.util.shiprate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="orderInfo" type="{http://www.example.org/ShipRate/}orderInfoArray" minOccurs="0"/>
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
    "orderInfo"
})
@XmlRootElement(name = "searchOrderResponse")
public class SearchOrderResponse {

    protected OrderInfoArray orderInfo;

    /**
     * 获取orderInfo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link OrderInfoArray }
     *     
     */
    public OrderInfoArray getOrderInfo() {
        return orderInfo;
    }

    /**
     * 设置orderInfo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link OrderInfoArray }
     *     
     */
    public void setOrderInfo(OrderInfoArray value) {
        this.orderInfo = value;
    }

}
