package io.renren.modules.product.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.product.dto.TemplateFieldValueDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author zjr
 * @email 1981763981@qq.com
 * @date 2018-12-23 23:19:40
 */
@TableName("amazon_template_category_fields")
public class TemplateCategoryFieldsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 字段id
     */
    @TableId
    private Long fieldId;
    /**
     * 模板id
     */
    private Long templateId;
    /**
     * 字段名
     */
    private String fieldName;
    /**
     * 显示名
     */
    private String fieldDisplayName;
    /**
     * 是否自定义（0：可以自己输入，1：不可以自己输入）
     */
    private Integer isCustom;

    @TableField(exist = false)
    private List<TemplateFieldValueDto> templateFieldValueDtos;

    public List<TemplateFieldValueDto> getTemplateFieldValueDtos() {
        return templateFieldValueDtos;
    }

    public void setTemplateFieldValueDtos(List<TemplateFieldValueDto> templateFieldValueDtos) {
        this.templateFieldValueDtos = templateFieldValueDtos;
    }

    /**
     * 设置：字段id
     */
    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    /**
     * 获取：字段id
     */
    public Long getFieldId() {
        return fieldId;
    }

    /**
     * 设置：模板id
     */
    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    /**
     * 获取：模板id
     */
    public Long getTemplateId() {
        return templateId;
    }

    /**
     * 设置：字段名
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * 获取：字段名
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * 设置：显示名
     */
    public void setFieldDisplayName(String fieldDisplayName) {
        this.fieldDisplayName = fieldDisplayName;
    }

    /**
     * 获取：显示名
     */
    public String getFieldDisplayName() {
        return fieldDisplayName;
    }

    /**
     * 设置：是否自定义（0：可以自己输入，1：不可以自己输入）
     */
    public void setIsCustom(Integer isCustom) {
        this.isCustom = isCustom;
    }

    /**
     * 获取：是否自定义（0：可以自己输入，1：不可以自己输入）
     */
    public Integer getIsCustom() {
        return isCustom;
    }
}
