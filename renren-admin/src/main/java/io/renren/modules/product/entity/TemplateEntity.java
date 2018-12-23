package io.renren.modules.product.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author zjr
 * @email 1981763981@qq.com
 * @date 2018-12-23 23:19:41
 */
@TableName("amazon_template")
public class TemplateEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 模板id
     */
    @TableId
    private Long templateId;
    /**
     * 模板名称
     */
    private String templateName;
    /**
     * 显示名称
     */
    private String templateDisplayName;

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
     * 设置：模板名称
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * 获取：模板名称
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * 设置：显示名称
     */
    public void setTemplateDisplayName(String templateDisplayName) {
        this.templateDisplayName = templateDisplayName;
    }

    /**
     * 获取：显示名称
     */
    public String getTemplateDisplayName() {
        return templateDisplayName;
    }
}
