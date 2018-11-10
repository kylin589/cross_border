package io.renren;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.modules.product.entity.ProductsEntity;
import io.renren.modules.product.service.ProductsService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductsServiceTest {
    @Autowired
    private ProductsService productsService;

    @Ignore
    @Test
    public void selectCountTest(){
        EntityWrapper<ProductsEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("is_deleted",1);
        System.out.println(productsService.selectCount(wrapper));
    }

    @Test
    public void getNewProductIdTest(){
        System.out.println(productsService.getNewProductId(1L));
    }
}
