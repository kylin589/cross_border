
package io.renren.modules.logistics.util.shiprate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>addEISOrderInfo complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="addEISOrderInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ebayAccount" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="refCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="buyerFullName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="buyerPhoneNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="buyerAddress1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="buyerAddress2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="buyerCity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="buyerState" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="buyerZipCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="labelSize" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="EISOrderProducts" type="{http://www.example.org/ShipRate/}EISOrderProducts" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addEISOrderInfo", propOrder = {
    "ebayAccount",
    "refCode",
    "buyerFullName",
    "buyerPhoneNumber",
    "buyerAddress1",
    "buyerAddress2",
    "buyerCity",
    "buyerState",
    "buyerZipCode",
    "labelSize",
    "eisOrderProducts"
})
public class AddEISOrderInfo {

    @XmlElement(required = true)
    protected String ebayAccount;
    protected String refCode;
    protected String buyerFullName;
    protected String buyerPhoneNumber;
    protected String buyerAddress1;
    protected String buyerAddress2;
    protected String buyerCity;
    protected String buyerState;
    protected String buyerZipCode;
    protected Integer labelSize;
    @XmlElement(name = "EISOrderProducts")
    protected List<EISOrderProducts> eisOrderProducts;

    /**
     * 获取ebayAccount属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEbayAccount() {
        return ebayAccount;
    }

    /**
     * 设置ebayAccount属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEbayAccount(String value) {
        this.ebayAccount = value;
    }

    /**
     * 获取refCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefCode() {
        return refCode;
    }

    /**
     * 设置refCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefCode(String value) {
        this.refCode = value;
    }

    /**
     * 获取buyerFullName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuyerFullName() {
        return buyerFullName;
    }

    /**
     * 设置buyerFullName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuyerFullName(String value) {
        this.buyerFullName = value;
    }

    /**
     * 获取buyerPhoneNumber属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuyerPhoneNumber() {
        return buyerPhoneNumber;
    }

    /**
     * 设置buyerPhoneNumber属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuyerPhoneNumber(String value) {
        this.buyerPhoneNumber = value;
    }

    /**
     * 获取buyerAddress1属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuyerAddress1() {
        return buyerAddress1;
    }

    /**
     * 设置buyerAddress1属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuyerAddress1(String value) {
        this.buyerAddress1 = value;
    }

    /**
     * 获取buyerAddress2属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuyerAddress2() {
        return buyerAddress2;
    }

    /**
     * 设置buyerAddress2属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuyerAddress2(String value) {
        this.buyerAddress2 = value;
    }

    /**
     * 获取buyerCity属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuyerCity() {
        return buyerCity;
    }

    /**
     * 设置buyerCity属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuyerCity(String value) {
        this.buyerCity = value;
    }

    /**
     * 获取buyerState属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuyerState() {
        return buyerState;
    }

    /**
     * 设置buyerState属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuyerState(String value) {
        this.buyerState = value;
    }

    /**
     * 获取buyerZipCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuyerZipCode() {
        return buyerZipCode;
    }

    /**
     * 设置buyerZipCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuyerZipCode(String value) {
        this.buyerZipCode = value;
    }

    /**
     * 获取labelSize属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLabelSize() {
        return labelSize;
    }

    /**
     * 设置labelSize属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLabelSize(Integer value) {
        this.labelSize = value;
    }

    /**
     * Gets the value of the eisOrderProducts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the eisOrderProducts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEISOrderProducts().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EISOrderProducts }
     * 
     * 
     */
    public List<EISOrderProducts> getEISOrderProducts() {
        if (eisOrderProducts == null) {
            eisOrderProducts = new ArrayList<EISOrderProducts>();
        }
        return this.eisOrderProducts;
    }

}
