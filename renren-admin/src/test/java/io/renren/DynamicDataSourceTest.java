package io.renren;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.modules.logistics.entity.DomesticLogisticsEntity;
import io.renren.modules.logistics.service.DomesticLogisticsService;
import io.renren.modules.product.entity.*;
import io.renren.modules.product.service.*;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysDeptService;
import io.renren.modules.sys.service.SysUserService;
import io.renren.service.DataSourceTestService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamicDataSourceTest {
    @Autowired
    private DataSourceTestService dataSourceTestService;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductOrderItemService productOrderItemService;
    @Autowired
    private DomesticLogisticsService domesticLogisticsService;
    @Autowired
    private EanUpcService eanUpcService;
    @Autowired
    private VariantsInfoService variantsInfoService;

    @Test
    public void tongbuOrder(){
        List<VariantsInfoEntity> list = variantsInfoService.selectList(null);
        for(VariantsInfoEntity variantsInfoEntity : list){
            ProductsEntity product = productsService.selectById(variantsInfoEntity.getProductId());
            if(product != null && product.getCreateUserId() != null && product.getDeptId() != null){
                variantsInfoEntity.setDeptId(product.getDeptId());
                variantsInfoEntity.setUserId(product.getCreateUserId());
            }
        }
        variantsInfoService.updateBatchById(list);
//        List<OrderEntity> orderEntityList = orderService.selectList(null);
//        List<ProductOrderItemEntity> list = new ArrayList<ProductOrderItemEntity>();
//        for(OrderEntity orderEntity : orderEntityList){
//            ProductOrderItemEntity orderItem = new ProductOrderItemEntity();
//            orderItem.setAmazonOrderId(orderEntity.getAmazonOrderId());
//            orderItem.setProductImageUrl(orderEntity.getProductImageUrl());
//            orderItem.setProductAsin(orderEntity.getProductAsin());
//            orderItem.setProductSku(orderEntity.getProductSku());
//            orderItem.setProductPrice(orderEntity.getOrderMoney());
//            orderItem.setProductTitle(orderEntity.getProductTitle());
//            orderItem.setProductId(orderEntity.getProductId());
//            orderItem.setUpdatetime(new Date());
//            orderItem.setOrderItemId(orderEntity.getOrderItemId());
//            orderItem.setOrderItemNumber(orderEntity.getOrderNumber());
//            list.add(orderItem);
//        }
//        productOrderItemService.insertBatch(list);
    }
    @Test
    public void tongbuwuliu(){
        List<DomesticLogisticsEntity> domesticLogisticsEntityList = domesticLogisticsService.selectList(null);
        for(DomesticLogisticsEntity dom : domesticLogisticsEntityList){
            OrderEntity order = orderService.selectById(dom.getOrderId());
            ProductOrderItemEntity orderItem = productOrderItemService.selectOne(new EntityWrapper<ProductOrderItemEntity>().eq("order_item_id",order.getOrderItemId()));
            dom.setItemId(orderItem.getItemId());
        }
        domesticLogisticsService.updateBatchById(domesticLogisticsEntityList);
    }
    @Test
    public void test(){
        //数据源1
        SysUserEntity user1 = dataSourceTestService.queryUser(1L);
        System.out.println(ToStringBuilder.reflectionToString(user1));

        //数据源2
        SysUserEntity user2 = dataSourceTestService.queryUser2(1L);
        System.out.println(ToStringBuilder.reflectionToString(user2));

        //数据源1
        SysUserEntity user3 = dataSourceTestService.queryUser(1L);
        System.out.println(ToStringBuilder.reflectionToString(user3));
    }

    @Test
    public void test1(){
        //1
        List<UploadEntity> uploadEntityList = uploadService.selectList(null);
        for(UploadEntity uploadEntity : uploadEntityList){
            List<String> ids = Arrays.asList(uploadEntity.getUploadProductsIds().split(","));
            List<ProductsEntity> productsEntityList = new ArrayList<ProductsEntity>();
            productsEntityList = productsService.selectBatchIds(ids);
            for(ProductsEntity productsEntity : productsEntityList){
                productsEntity.setIsUpload(1);
            }
            if(productsEntityList.size() > 0){
                productsService.updateBatchById(productsEntityList);
            }
        }
        //2
        List<ProductsEntity> productsEntityList = productsService.selectList(null);
        for(ProductsEntity productsEntity : productsEntityList){
            if(productsEntity.getDeptId() == null){
                SysUserEntity user = userService.selectById(productsEntity.getCreateUserId());
                if(user != null){
                    productsEntity.setDeptId(user.getDeptId());
                }
            }
        }
        productsService.updateBatchById(productsEntityList);
    }
}
