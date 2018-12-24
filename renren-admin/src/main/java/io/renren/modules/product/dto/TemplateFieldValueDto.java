package io.renren.modules.product.dto;

public class TemplateFieldValueDto {

    private Long valueId;

    private Long fieldId;

    private String cnValue;

    private String value;

    private String returnValue;

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public Long getValueId() {
        return valueId;
    }

    public void setValueId(Long valueId) {
        this.valueId = valueId;
    }

    public String getCnValue() {
        return cnValue;
    }

    public void setCnValue(String cnValue) {
        this.cnValue = cnValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
