package io.renren.modules.amazon.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

/**
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-11-27 16:33:14
 */
@TableName("amazon_category_history")
public class AmazonCategoryHistoryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long categoryHistoryId;
    /**
     *
     */
    private Long amazonCategoryId;
    /**
     * 亚马逊分类
     */
    private String amazonAllCategory;
    /**
     * 亚马逊分类
     */
    private String amazonCategory;
    /**
     * 使用次数
     */
    private Integer count;

    private Long userId;
    private Long deptId;

    private String nodeId;
    private String countryCode;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * 设置：
     */
    public void setCategoryHistoryId(Long categoryHistoryId) {
        this.categoryHistoryId = categoryHistoryId;
    }

    /**
     * 获取：
     */
    public Long getCategoryHistoryId() {
        return categoryHistoryId;
    }

    /**
     * 设置：
     */
    public void setAmazonCategoryId(Long amazonCategoryId) {
        this.amazonCategoryId = amazonCategoryId;
    }

    /**
     * 获取：
     */
    public Long getAmazonCategoryId() {
        return amazonCategoryId;
    }

    public String getAmazonAllCategory() {
        return amazonAllCategory;
    }

    public void setAmazonAllCategory(String amazonAllCategory) {
        this.amazonAllCategory = amazonAllCategory;
    }

    /**
     * 设置：亚马逊分类
     */
    public void setAmazonCategory(String amazonCategory) {
        this.amazonCategory = amazonCategory;
    }

    /**
     * 获取：亚马逊分类
     */
    public String getAmazonCategory() {
        return amazonCategory;
    }

    /**
     * 设置：使用次数
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 获取：使用次数
     */
    public Integer getCount() {
        return count;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }
}
