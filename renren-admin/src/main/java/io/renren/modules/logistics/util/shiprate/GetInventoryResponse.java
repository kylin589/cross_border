
package io.renren.modules.logistics.util.shiprate;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="productNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="productName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="systemSerialNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="declaredValue" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="productStatus" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="quantity" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="warehouse" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="weight" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="length" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="width" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="height" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="shortDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="addTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
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
    "productNumber",
    "productName",
    "systemSerialNumber",
    "declaredValue",
    "productStatus",
    "quantity",
    "warehouse",
    "weight",
    "length",
    "width",
    "height",
    "shortDescription",
    "description",
    "addTime"
})
@XmlRootElement(name = "getInventoryResponse")
public class GetInventoryResponse {

    @XmlElement(required = true)
    protected String productNumber;
    @XmlElement(required = true)
    protected String productName;
    @XmlElement(required = true)
    protected String systemSerialNumber;
    protected float declaredValue;
    @XmlElement(required = true)
    protected String productStatus;
    protected int quantity;
    @XmlElement(required = true)
    protected String warehouse;
    protected float weight;
    protected float length;
    protected float width;
    protected float height;
    @XmlElement(required = true)
    protected String shortDescription;
    @XmlElement(required = true)
    protected String description;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar addTime;

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
     * 获取productName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductName() {
        return productName;
    }

    /**
     * 设置productName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductName(String value) {
        this.productName = value;
    }

    /**
     * 获取systemSerialNumber属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSystemSerialNumber() {
        return systemSerialNumber;
    }

    /**
     * 设置systemSerialNumber属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSystemSerialNumber(String value) {
        this.systemSerialNumber = value;
    }

    /**
     * 获取declaredValue属性的值。
     * 
     */
    public float getDeclaredValue() {
        return declaredValue;
    }

    /**
     * 设置declaredValue属性的值。
     * 
     */
    public void setDeclaredValue(float value) {
        this.declaredValue = value;
    }

    /**
     * 获取productStatus属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductStatus() {
        return productStatus;
    }

    /**
     * 设置productStatus属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductStatus(String value) {
        this.productStatus = value;
    }

    /**
     * 获取quantity属性的值。
     * 
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * 设置quantity属性的值。
     * 
     */
    public void setQuantity(int value) {
        this.quantity = value;
    }

    /**
     * 获取warehouse属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWarehouse() {
        return warehouse;
    }

    /**
     * 设置warehouse属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarehouse(String value) {
        this.warehouse = value;
    }

    /**
     * 获取weight属性的值。
     * 
     */
    public float getWeight() {
        return weight;
    }

    /**
     * 设置weight属性的值。
     * 
     */
    public void setWeight(float value) {
        this.weight = value;
    }

    /**
     * 获取length属性的值。
     * 
     */
    public float getLength() {
        return length;
    }

    /**
     * 设置length属性的值。
     * 
     */
    public void setLength(float value) {
        this.length = value;
    }

    /**
     * 获取width属性的值。
     * 
     */
    public float getWidth() {
        return width;
    }

    /**
     * 设置width属性的值。
     * 
     */
    public void setWidth(float value) {
        this.width = value;
    }

    /**
     * 获取height属性的值。
     * 
     */
    public float getHeight() {
        return height;
    }

    /**
     * 设置height属性的值。
     * 
     */
    public void setHeight(float value) {
        this.height = value;
    }

    /**
     * 获取shortDescription属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * 设置shortDescription属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShortDescription(String value) {
        this.shortDescription = value;
    }

    /**
     * 获取description属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置description属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * 获取addTime属性的值。
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getAddTime() {
        return addTime;
    }

    /**
     * 设置addTime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setAddTime(XMLGregorianCalendar value) {
        this.addTime = value;
    }

}
