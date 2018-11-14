package io.renren;

import io.renren.modules.product.entity.DataDictionaryEntity;
import io.renren.modules.product.service.DataDictionaryService;
import io.renren.modules.product.service.ProductsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductDataTest {
   /* @Autowired
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private ProductsService productsService;

    *//**
     * 审核状态分类
     * 入参：无
     *//*
    @Test
    public void auditlist(){
        List<DataDictionaryEntity> auditList=dataDictionaryService.auditList();

        int auditCounts=0;
        for (DataDictionaryEntity dataDictionaryEntity:auditList){
            String number= dataDictionaryEntity.getDataNumber();
            int auditCount=productsService.auditCount(number);
            dataDictionaryEntity.setCount(auditCount);
            auditCounts+=auditCount;
            System.out.println(dataDictionaryEntity.getDataContent()+"  "+dataDictionaryEntity.getCount());
        }
        System.out.println(auditCounts);
    }

    *//**
     * 上架状态分类
     * 入参：无
     *//*
    @Test
    public void putawaylist(){
        List<DataDictionaryEntity> putawayList=dataDictionaryService.putawayList();
        int putawayCounts=0;
        for (DataDictionaryEntity dataDictionaryEntity:putawayList){
            String number= dataDictionaryEntity.getDataNumber();
            int putawayCount=productsService.putawayCount(number);
            dataDictionaryEntity.setCount(putawayCount);
            putawayCounts+=putawayCount;
            System.out.println(dataDictionaryEntity.getDataContent()+"  "+dataDictionaryEntity.getCount());
        }
        System.out.println(putawayCounts);
    }



    *//**
     * 产品类型分类
     * 入参：无
     *//*
    @Test
    public void productlist(){
        List<DataDictionaryEntity> productList=dataDictionaryService.productList();
        int productCounts=0;
        for (DataDictionaryEntity dataDictionaryEntity:productList){
            String number= dataDictionaryEntity.getDataNumber();
            int productCount=productsService.productCount(number);
            dataDictionaryEntity.setCount(productCount);
            productCounts+=productCount;
            System.out.println(dataDictionaryEntity.getDataContent()+"  "+dataDictionaryEntity.getCount());
        }
        System.out.println(productCounts);
    }*/
}
