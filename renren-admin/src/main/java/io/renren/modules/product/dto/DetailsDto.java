package io.renren.modules.product.dto;

import io.renren.modules.product.entity.FieldMiddleEntity;
import io.renren.modules.product.entity.UploadEntity;

import java.util.List;

public class DetailsDto {

    private UploadEntity uploadEntity;

    private List<FieldMiddleEntity> middleEntitys;

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
