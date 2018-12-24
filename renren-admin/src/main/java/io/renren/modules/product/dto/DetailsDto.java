package io.renren.modules.product.dto;

import io.renren.modules.product.entity.FieldMiddleEntity;
import io.renren.modules.product.entity.TemplateCategoryFieldsEntity;
import io.renren.modules.product.entity.UploadEntity;

import java.util.List;

public class DetailsDto {

    private UploadEntity uploadEntity;

    private List<FieldMiddleEntity> middleEntitys;

    private List<TemplateCategoryFieldsEntity> templateCategoryFieldsEntities;

    public List<TemplateCategoryFieldsEntity> getTemplateCategoryFieldsEntities() {
        return templateCategoryFieldsEntities;
    }

    public void setTemplateCategoryFieldsEntities(List<TemplateCategoryFieldsEntity> templateCategoryFieldsEntities) {
        this.templateCategoryFieldsEntities = templateCategoryFieldsEntities;
    }

    private String AllCategories;

    public String getAllCategories() {
        return AllCategories;
    }

    public void setAllCategories(String allCategories) {
        AllCategories = allCategories;
    }

    public UploadEntity getUploadEntity() {
        return uploadEntity;
    }

    public void setUploadEntity(UploadEntity uploadEntity) {
        this.uploadEntity = uploadEntity;
    }

    public List<FieldMiddleEntity> getMiddleEntitys() {
        return middleEntitys;
    }

    public void setMiddleEntitys(List<FieldMiddleEntity> middleEntitys) {
        this.middleEntitys = middleEntitys;
    }
}
