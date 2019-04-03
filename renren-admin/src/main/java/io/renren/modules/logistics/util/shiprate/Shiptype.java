
package io.renren.modules.logistics.util.shiprate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>shiptype complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="shiptype">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="method_code" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="en_name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="cn_name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="deliverytime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="iftracking" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="is_weight" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "shiptype")
public class Shiptype {

    @XmlAttribute(name = "method_code")
    protected String methodCode;
    @XmlAttribute(name = "en_name")
    protected String enName;
    @XmlAttribute(name = "cn_name")
    protected String cnName;
    @XmlAttribute(name = "deliverytime")
    protected String deliverytime;
    @XmlAttribute(name = "iftracking")
    protected String iftracking;
    @XmlAttribute(name = "is_weight")
    protected String isWeight;

    /**
     * 获取methodCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMethodCode() {
        return methodCode;
    }

    /**
     * 设置methodCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMethodCode(String value) {
        this.methodCode = value;
    }

    /**
     * 获取enName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnName() {
        return enName;
    }

    /**
     * 设置enName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnName(String value) {
        this.enName = value;
    }

    /**
     * 获取cnName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCnName() {
        return cnName;
    }

    /**
     * 设置cnName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCnName(String value) {
        this.cnName = value;
    }

    /**
     * 获取deliverytime属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverytime() {
        return deliverytime;
    }

    /**
     * 设置deliverytime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverytime(String value) {
        this.deliverytime = value;
    }

    /**
     * 获取iftracking属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIftracking() {
        return iftracking;
    }

    /**
     * 设置iftracking属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIftracking(String value) {
        this.iftracking = value;
    }

    /**
     * 获取isWeight属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsWeight() {
        return isWeight;
    }

    /**
     * 设置isWeight属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsWeight(String value) {
        this.isWeight = value;
    }

}
