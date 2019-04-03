
package io.renren.modules.logistics.util.shiprate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>addWarehouseOrderRequestInfoArray complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="addWarehouseOrderRequestInfoArray">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="recipientName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="recipientCountry" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="recipientZipCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="recipientState" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="recipientCity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="recipientAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="recipientPhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerOrderNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="warehouseEnName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="shippingMethod" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="goodsDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="goodsQuantity" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="goodsDeclareWorth" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="goodsWeight" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="productDetails" type="{http://www.example.org/ShipRate/}productDetailsArray" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addWarehouseOrderRequestInfoArray", propOrder = {
    "recipientName",
    "recipientCountry",
    "recipientZipCode",
    "recipientState",
    "recipientCity",
    "recipientAddress",
    "recipientPhone",
    "customerOrderNo",
    "warehouseEnName",
    "shippingMethod",
    "goodsDescription",
    "goodsQuantity",
    "goodsDeclareWorth",
    "goodsWeight",
    "productDetails"
})
public class AddWarehouseOrderRequestInfoArray {

    @XmlElement(required = true)
    protected String recipientName;
    @XmlElement(required = true)
    protected String recipientCountry;
    @XmlElement(required = true)
    protected String recipientZipCode;
    protected String recipientState;
    protected String recipientCity;
    @XmlElement(required = true)
    protected String recipientAddress;
    protected String recipientPhone;
    protected String customerOrderNo;
    @XmlElement(required = true)
    protected String warehouseEnName;
    @XmlElement(required = true)
    protected String shippingMethod;
    @XmlElement(required = true)
    protected String goodsDescription;
    protected int goodsQuantity;
    protected float goodsDeclareWorth;
    protected Float goodsWeight;
    @XmlElement(required = true)
    protected List<ProductDetailsArray> productDetails;

    /**
     * 获取recipientName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecipientName() {
        return recipientName;
    }

    /**
     * 设置recipientName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecipientName(String value) {
        this.recipientName = value;
    }

    /**
     * 获取recipientCountry属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecipientCountry() {
        return recipientCountry;
    }

    /**
     * 设置recipientCountry属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecipientCountry(String value) {
        this.recipientCountry = value;
    }

    /**
     * 获取recipientZipCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecipientZipCode() {
        return recipientZipCode;
    }

    /**
     * 设置recipientZipCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecipientZipCode(String value) {
        this.recipientZipCode = value;
    }

    /**
     * 获取recipientState属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecipientState() {
        return recipientState;
    }

    /**
     * 设置recipientState属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecipientState(String value) {
        this.recipientState = value;
    }

    /**
     * 获取recipientCity属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecipientCity() {
        return recipientCity;
    }

    /**
     * 设置recipientCity属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecipientCity(String value) {
        this.recipientCity = value;
    }

    /**
     * 获取recipientAddress属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecipientAddress() {
        return recipientAddress;
    }

    /**
     * 设置recipientAddress属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecipientAddress(String value) {
        this.recipientAddress = value;
    }

    /**
     * 获取recipientPhone属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecipientPhone() {
        return recipientPhone;
    }

    /**
     * 设置recipientPhone属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecipientPhone(String value) {
        this.recipientPhone = value;
    }

    /**
     * 获取customerOrderNo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerOrderNo() {
        return customerOrderNo;
    }

    /**
     * 设置customerOrderNo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerOrderNo(String value) {
        this.customerOrderNo = value;
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

    /**
     * 获取shippingMethod属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShippingMethod() {
        return shippingMethod;
    }

    /**
     * 设置shippingMethod属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShippingMethod(String value) {
        this.shippingMethod = value;
    }

    /**
     * 获取goodsDescription属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGoodsDescription() {
        return goodsDescription;
    }

    /**
     * 设置goodsDescription属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGoodsDescription(String value) {
        this.goodsDescription = value;
    }

    /**
     * 获取goodsQuantity属性的值。
     * 
     */
    public int getGoodsQuantity() {
        return goodsQuantity;
    }

    /**
     * 设置goodsQuantity属性的值。
     * 
     */
    public void setGoodsQuantity(int value) {
        this.goodsQuantity = value;
    }

    /**
     * 获取goodsDeclareWorth属性的值。
     * 
     */
    public float getGoodsDeclareWorth() {
        return goodsDeclareWorth;
    }

    /**
     * 设置goodsDeclareWorth属性的值。
     * 
     */
    public void setGoodsDeclareWorth(float value) {
        this.goodsDeclareWorth = value;
    }

    /**
     * 获取goodsWeight属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getGoodsWeight() {
        return goodsWeight;
    }

    /**
     * 设置goodsWeight属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setGoodsWeight(Float value) {
        this.goodsWeight = value;
    }

    /**
     * Gets the value of the productDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the productDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProductDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProductDetailsArray }
     * 
     * 
     */
    public List<ProductDetailsArray> getProductDetails() {
        if (productDetails == null) {
            productDetails = new ArrayList<ProductDetailsArray>();
        }
        return this.productDetails;
    }

}
