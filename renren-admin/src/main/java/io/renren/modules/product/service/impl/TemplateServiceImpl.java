package io.renren.modules.product.service.impl;

import io.renren.modules.product.dto.TemplateFieldValueDto;
import io.renren.modules.product.entity.TemplateCategoryFieldsEntity;
import io.renren.modules.product.entity.TemplateFieldValueEntity;
import io.renren.modules.product.service.TemplateCategoryFieldsService;
import io.renren.modules.product.service.TemplateFieldValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.product.dao.TemplateDao;
import io.renren.modules.product.entity.TemplateEntity;
import io.renren.modules.product.service.TemplateService;


@Service("templateService")
public class TemplateServiceImpl extends ServiceImpl<TemplateDao, TemplateEntity> implements TemplateService {

    @Autowired
    private TemplateCategoryFieldsService templateCategoryFieldsService;

    @Autowired
    private TemplateFieldValueService templateFieldValueService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<TemplateEntity> page = this.selectPage(
                new Query<TemplateEntity>(params).getPage(),
                new EntityWrapper<TemplateEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<TemplateCategoryFieldsEntity> getOptionalValues(String templateId, String countryCode) {
        Map<String, Object> templateCategoryFieldMap = new HashMap<>();
        templateCategoryFieldMap.put("template_id", templateId);
        List<TemplateCategoryFieldsEntity> templateCategoryFieldsEntities = templateCategoryFieldsService.selectByMap(templateCategoryFieldMap);

        for (TemplateCategoryFieldsEntity templateCategoryFieldsEntity : templateCategoryFieldsEntities) {
            Map<String, Object> tfvesMap = new HashMap<>();
            tfvesMap.put("field_id", templateCategoryFieldsEntity.getFieldId());
            List<TemplateFieldValueEntity> tfves = templateFieldValueService.selectByMap(tfvesMap);

            List<TemplateFieldValueDto> templateFieldValueDtos = new ArrayList<>();
            for (int i = 0; i < tfves.size(); i++) {
                TemplateFieldValueDto templateFieldValueDto = new TemplateFieldValueDto();
                templateFieldValueDto.setValueId(tfves.get(i).getValueId());
                templateFieldValueDto.setCnValue(tfves.get(i).getCnValue());
                templateFieldValueDto.setFieldId(tfves.get(i).getFieldId());
                switch (countryCode) {
                    // 加拿大
                    case "CA":
                        templateFieldValueDto.setValue(tfves.get(i).getCaValue());
                        break;
                    // 墨西哥
                    case "MX":
                        templateFieldValueDto.setValue(tfves.get(i).getMxValue());
                        break;
                    // 美国
                    case "US":
                        templateFieldValueDto.setValue(tfves.get(i).getUsValue());
                        break;
                    // 德国
                    case "DE":
                        templateFieldValueDto.setValue(tfves.get(i).getDeValue());
                        break;
                    // 西班牙
                    case "ES":
                        templateFieldValueDto.setValue(tfves.get(i).getEsValue());
                        break;
                    // 法国
                    case "FR":
                        templateFieldValueDto.setValue(tfves.get(i).getFrValue());
                        break;
                    // 英国
                    case "GB":
                        templateFieldValueDto.setValue(tfves.get(i).getUkValue());
                        break;
                    // 意大利
                    case "IT":
                        templateFieldValueDto.setValue(tfves.get(i).getItValue());
                        break;
                    // 澳大利亚
                    case "AU":
                        templateFieldValueDto.setValue(tfves.get(i).getAuValue());
                        break;
                    // 日本
                    case "JP":
                        templateFieldValueDto.setValue(tfves.get(i).getJpValue());
                        break;
                    default:
                        break;
                }
                templateFieldValueDtos.add(templateFieldValueDto);
            }
            templateCategoryFieldsEntity.setTemplateFieldValueDtos(templateFieldValueDtos);
        }
        return templateCategoryFieldsEntities;
    }

}
