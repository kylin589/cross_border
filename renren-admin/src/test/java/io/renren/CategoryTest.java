package io.renren;

import io.renren.modules.product.entity.CategoryEntity;
import io.renren.modules.product.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryTest {
    @Autowired
    private CategoryService categoryService;
    @Test
    public void parentByChindId(){
        Long categoryId=3L;
        Long[]arr= new Long[3];
        //把传过来的子级id存放到数组
        arr[0] = categoryId;
        int j = 1;
        for (int i = 0; i < arr.length - 1; i++) {
            if (categoryId != 0) {
                CategoryEntity productCategoryEntity = categoryService.selectById(categoryId);
                categoryId = productCategoryEntity.getParentId();
                arr[j] = categoryId;
                j++;
            }
        }
        String idString = "";
        //多数组进行反转，以逗号进行字符串拼接
        for (int i = arr.length - 1; i >= 0; i--) {
            //遍历数组里不为null和0的
            if (!(arr[i] == null || arr[i] == 0)) {
                if (i == 0) {
                    idString += arr[i];
                } else {
                    idString += arr[i];
                    idString += ",";
                }
            }
        }
        System.out.println(idString);
    }
}
