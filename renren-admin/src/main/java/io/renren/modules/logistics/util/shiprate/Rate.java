
package io.renren.modules.logistics.util.shiprate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>rate complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="rate">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="totalfee" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="costfee" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="base_fee" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="dealfee" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="regfee" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="addons" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="deliverytime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="isweight" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="iftracking" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="classtype" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="classtypecode" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="shiptypecode" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="shiptypename" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="shiptypecnname" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rate")
public class Rate {

    @XmlAttribute(name = "totalfee")
    protected Float totalfee;
    @XmlAttribute(name = "costfee")
    protected Float costfee;
    @XmlAttribute(name = "base_fee")
    protected Float baseFee;
    @XmlAttribute(name = "dealfee")
    protected Float dealfee;
    @XmlAttribute(name = "regfee")
    protected Float regfee;
    @XmlAttribute(name = "addons")
    protected Float addons;
    @XmlAttribute(name = "deliverytime")
    protected String deliverytime;
    @XmlAttribute(name = "isweight")
    protected String isweight;
    @XmlAttribute(name = "iftracking")
    protected String iftracking;
    @XmlAttribute(name = "classtype")
    protected String classtype;
    @XmlAttribute(name = "classtypecode")
    protected String classtypecode;
    @XmlAttribute(name = "shiptypecode")
    protected String shiptypecode;
    @XmlAttribute(name = "shiptypename")
    protected String shiptypename;
    @XmlAttribute(name = "shiptypecnname")
    protected String shiptypecnname;

    /**
     * 获取totalfee属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getTotalfee() {
        return totalfee;
    }

    /**
     * 设置totalfee属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setTotalfee(Float value) {
        this.totalfee = value;
    }

    /**
     * 获取costfee属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getCostfee() {
        return costfee;
    }

    /**
     * 设置costfee属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setCostfee(Float value) {
        this.costfee = value;
    }

    /**
     * 获取baseFee属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getBaseFee() {
        return baseFee;
    }

    /**
     * 设置baseFee属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setBaseFee(Float value) {
        this.baseFee = value;
    }

    /**
     * 获取dealfee属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getDealfee() {
        return dealfee;
    }

    /**
     * 设置dealfee属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setDealfee(Float value) {
        this.dealfee = value;
    }

    /**
     * 获取regfee属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getRegfee() {
        return regfee;
    }

    /**
     * 设置regfee属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setRegfee(Float value) {
        this.regfee = value;
    }

    /**
     * 获取addons属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getAddons() {
        return addons;
    }

    /**
     * 设置addons属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setAddons(Float value) {
        this.addons = value;
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
     * 获取isweight属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsweight() {
        return isweight;
    }

    /**
     * 设置isweight属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsweight(String value) {
        this.isweight = value;
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
     * 获取classtype属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClasstype() {
        return classtype;
    }

    /**
     * 设置classtype属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClasstype(String value) {
        this.classtype = value;
    }

    /**
     * 获取classtypecode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClasstypecode() {
        return classtypecode;
    }

    /**
     * 设置classtypecode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClasstypecode(String value) {
        this.classtypecode = value;
    }

    /**
     * 获取shiptypecode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShiptypecode() {
        return shiptypecode;
    }

    /**
     * 设置shiptypecode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShiptypecode(String value) {
        this.shiptypecode = value;
    }

    /**
     * 获取shiptypename属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShiptypename() {
        return shiptypename;
    }

    /**
     * 设置shiptypename属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShiptypename(String value) {
        this.shiptypename = value;
    }

    /**
     * 获取shiptypecnname属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShiptypecnname() {
        return shiptypecnname;
    }

    /**
     * 设置shiptypecnname属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShiptypecnname(String value) {
        this.shiptypecnname = value;
    }

}
