package io.renren.modules.product.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

/**
 * 亚马逊分类表
 *
 * @author jhy
 * @email 617493711@qq.com
 * @date 2018-11-16 11:11:30
 */
@TableName("product_amazon_category")
public class AmazonCategoryEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId
    private Long id;
    /**
     *
     */
    private Long parentId;
    /**
     *
     */
    private String categoryName;
    /**
     *
     */
    private String displayName;
    /**
     *
     */
    private String nodeId;
    /**
     *
     */
    private String countryCode;
    /**
     *
     */
    private String categoryQ;

    /**
     * 设置：
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置：
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取：
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置：
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * 获取：
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * 设置：
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 获取：
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 设置：
     */
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    /**
     * 获取：
     */
    public String getNodeId() {
        return nodeId;
    }

    /**
     * 设置：
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * 获取：
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * 设置：
     */
    public void setCategoryQ(String categoryQ) {
        this.categoryQ = categoryQ;
    }

    /**
     * 获取：
     */
    public String getCategoryQ() {
        return categoryQ;
    }


}
