package io.renren;

import io.renren.modules.product.entity.CategoryEntity;
import io.renren.modules.product.entity.FreightCostEntity;
import io.renren.modules.product.entity.IntroductionEntity;
import io.renren.modules.product.entity.ProductsEntity;
import io.renren.modules.product.service.CategoryService;
import io.renren.modules.product.service.FreightCostService;
import io.renren.modules.product.service.IntroductionService;
import io.renren.modules.product.service.ProductsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductControllerTest {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private FreightCostService freightCostService;
    @Autowired
    private IntroductionService introductionService;
/*
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductsService productsService;

    *//**
     * 一级分类
     * 入参：无
     *//*
    @Test
    public void parentList() {
        List<CategoryEntity> parentList = categoryService.parent();
        for (CategoryEntity categoryEntity : parentList) {
            Long id = categoryEntity.getCategoryId();
            int c = productsService.count(id);

            categoryEntity.setCount(c);
            System.out.println(categoryEntity.getCategoryName() + "  " + categoryEntity.getCount());
        }

    }


    *//**
     * 父级查子级
     * 入参：父级id
     *//*
    @Test
    public void parentId() {
        List<CategoryEntity> parentLists = categoryService.parentId(1L);
        for (CategoryEntity categoryEntity : parentLists) {
            Long id = categoryEntity.getCategoryId();
            int count = productsService.counts(id);
            categoryEntity.setCount(count);
            System.out.println(categoryEntity.getCategoryName() + "  " + categoryEntity.getCount());
        }


    }

    *//**
     * 子类id查父类id
     * 入参：子类id
     *//*
    @Test
    public void parentIds() {
        String ids = categoryService.parentIds(81L);
        System.out.println(ids);
    }*/
    @Test
    public void save (){
        ProductsEntity products= new ProductsEntity();
        products.setCategoryThreeId(8L);
        products.setMainImageId(1L);
        products.setAuditStatus("001");
        products.setShelveStatus("001");
        products.setProductType("001");
        products.setProducerName("年后");
        products.setBrandName("苹果");
        products.setManufacturerNumber("001");
        products.setProductSource("diqiu");
        products.setSellerLink("33");
        products.setProductRemark("bizhu");
        products.setUpcCode("dddddd");
        products.setPurchasePrice(new BigDecimal(3.00));

        Long threeId = products.getCategoryThreeId();
        //根据三级id查出一级二级三级字符串
        String idString = categoryService.queryParentByChildId(threeId);
        String[] id = idString.split(",");
        products.setCategoryOneId(Long.parseLong(id[0]));
        products.setCategoryTwoId(Long.parseLong(id[1]));
        products.setCategoryThreeId(Long.parseLong(id[2]));


        //中文介绍
        IntroductionEntity chinesePRE=new IntroductionEntity();
        chinesePRE.setProductTitle("nihao");
        introductionService.insert(chinesePRE);
        products.setChineseIntroduction(chinesePRE.getIntroductionId());
        productsService.insert(products);

    }
}
