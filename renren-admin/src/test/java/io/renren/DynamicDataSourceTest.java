package io.renren;


import io.renren.modules.product.entity.ProductsEntity;
import io.renren.modules.product.entity.UploadEntity;
import io.renren.modules.product.service.ProductsService;
import io.renren.modules.product.service.UploadService;
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
