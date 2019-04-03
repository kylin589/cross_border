
package io.renren.modules.logistics.util.shiprate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>productDetailsArray complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="productDetailsArray">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="productNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="productQuantity" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "productDetailsArray", propOrder = {
    "productNumber",
    "productQuantity"
})
public class ProductDetailsArray {

    @XmlElement(required = true)
    protected String productNumber;
    protected int productQuantity;

    /**
     * 获取productNumber属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductNumber() {
        return productNumber;
    }

    /**
     * 设置productNumber属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductNumber(String value) {
        this.productNumber = value;
    }

    /**
     * 获取productQuantity属性的值。
     * 
     */
    public int getProductQuantity() {
        return productQuantity;
    }

    /**
     * 设置productQuantity属性的值。
     * 
     */
    public void setProductQuantity(int value) {
        this.productQuantity = value;
    }

}
