
package io.renren.modules.logistics.util.shiprate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>EISOrderProducts complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="EISOrderProducts">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element name="itemNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="transactionId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="shelfCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CNDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customLabel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EISOrderProducts", propOrder = {
    "itemNumber",
    "transactionId",
    "shelfCode",
    "cnDescription",
    "customLabel"
})
public class EISOrderProducts {

    protected String itemNumber;
    protected String transactionId;
    protected String shelfCode;
    @XmlElement(name = "CNDescription")
    protected String cnDescription;
    protected String customLabel;

    /**
     * 获取itemNumber属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemNumber() {
        return itemNumber;
    }

    /**
     * 设置itemNumber属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemNumber(String value) {
        this.itemNumber = value;
    }

    /**
     * 获取transactionId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * 设置transactionId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionId(String value) {
        this.transactionId = value;
    }

    /**
     * 获取shelfCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShelfCode() {
        return shelfCode;
    }

    /**
     * 设置shelfCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShelfCode(String value) {
        this.shelfCode = value;
    }

    /**
     * 获取cnDescription属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCNDescription() {
        return cnDescription;
    }

    /**
     * 设置cnDescription属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCNDescription(String value) {
        this.cnDescription = value;
    }

    /**
     * 获取customLabel属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomLabel() {
        return customLabel;
    }

    /**
     * 设置customLabel属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomLabel(String value) {
        this.customLabel = value;
    }

}
