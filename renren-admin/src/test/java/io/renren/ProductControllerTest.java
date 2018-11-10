package io.renren;

import io.renren.modules.product.entity.CategoryEntity;
import io.renren.modules.product.service.CategoryService;
import io.renren.modules.product.service.ProductsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductControllerTest {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductsService productsService;

    /**
     * 一级分类
     * 入参：无
     */
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


    /**
     * 父级查子级
     * 入参：父级id
     */
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

    /**
     * 子类id查父类id
     * 入参：子类id
     */
    @Test
    public void parentIds() {
        String ids = categoryService.parentIds(81L);
        System.out.println(ids);
    }

}
