package io.renren.modules.product.dto;

import io.renren.modules.product.entity.FieldMiddleEntity;
import io.renren.modules.product.entity.UploadEntity;

import java.util.List;

public class DetailsDto {

    private UploadEntity uploadEntity;

    private List<FieldMiddleEntity> middleEntitys;

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
