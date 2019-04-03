
package io.renren.modules.logistics.util.shiprate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>getWarehouseShipTypeRequestInfoArray complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="getWarehouseShipTypeRequestInfoArray">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="countryEnName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="warehouseEnName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getWarehouseShipTypeRequestInfoArray", propOrder = {
    "countryEnName",
    "warehouseEnName"
})
public class GetWarehouseShipTypeRequestInfoArray {

    protected String countryEnName;
    protected String warehouseEnName;

    /**
     * 获取countryEnName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryEnName() {
        return countryEnName;
    }

    /**
     * 设置countryEnName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryEnName(String value) {
        this.countryEnName = value;
    }

    /**
     * 获取warehouseEnName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWarehouseEnName() {
        return warehouseEnName;
    }

    /**
     * 设置warehouseEnName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarehouseEnName(String value) {
        this.warehouseEnName = value;
    }

}
