package io.renren;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.modules.product.entity.AmazonCategoryEntity;
import io.renren.modules.product.service.AmazonCategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import javax.sound.midi.Soundbank;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AmazonCategoryTest {
    @Autowired
    private AmazonCategoryService amazonCategoryService;

    //亚马逊的一级分类
    @Test
    public void amazonOneCategory() {
        List<AmazonCategoryEntity> amazonCategoryEntityList = amazonCategoryService.queryByAreaOneClassify(0);
        for (AmazonCategoryEntity amazonCategoryEntity : amazonCategoryEntityList) {
            String categoryName = amazonCategoryEntity.getCategoryName();
            System.out.println(categoryName);
        }
    }

    //根据父级id查出亚马逊子级id
    @Test
    public void amazonChindCategory() {
        List<AmazonCategoryEntity> amazonCategoryEntityChildList = amazonCategoryService.selectList(new EntityWrapper<AmazonCategoryEntity>().eq("parent_id", 5));
        for (AmazonCategoryEntity amazonCategoryEntity : amazonCategoryEntityChildList) {
            String categoryName = amazonCategoryEntity.getCategoryName();
            System.out.println(categoryName);
        }
    }

    //子级id查询父级的所有id
    @Test
    public void queryByParentId() {
       /* Long chanshu = 5L;
        Long[] arr = new Long[5];
        arr[0] = chanshu;
        int j = 1;
        for (int i = 1; i < arr.length; i++) {
            if (chanshu != 0) {
                AmazonCategoryEntity amazonCategoryEntity = amazonCategoryService.selectById(chanshu);
                chanshu = amazonCategoryEntity.getParentId();
                arr[j] = chanshu;
                j++;
            }
        }
        for (int i = arr.length-1;i>=0;i--){
            if (!(arr[i]==null || arr[i]==0)){

            }
        }
*/
        Long amazonCategoryId = 20L;
        Long[] arr = new Long[5];
        arr[0] = amazonCategoryId;
        int j = 1;
        for (int i = 0; i < arr.length - 1; i++) {
            if (amazonCategoryId != 0) {
                AmazonCategoryEntity amazonCategoryEntity = amazonCategoryService.selectById(amazonCategoryId);
                amazonCategoryId = amazonCategoryEntity.getParentId();
                arr[j] = amazonCategoryId;
                j++;
            }
        }
        String idString = "";
        for (int i = arr.length - 1; i >= 0; i--) {
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


        /*for(int i=arr.length-1;i>=0;i--){
            arr[i]=amazonCategoryId;
        }
        for (int i = 0; i <arr.length-1 ; i++) {
            System.out.println(arr[i]);
        }*/
    /*    String idString="";
        for(int i=arr.length-1;i>=0;i--){
            if(!(arr[i]==null||arr[i]==0)){
                if (i!=arr.length-1){
                    idString+=arr[i];
                    idString+=",";
                }else {
                    idString+=arr[i];

                }
            }
        }*/

        /*AmazonCategoryEntity amazonCategoryEntity = amazonCategoryService.selectById(18);
        Long id = amazonCategoryEntity.getParentId();
        for (int i = 0; i < arr.length; i++) {
            if (id != 0) {
                AmazonCategoryEntity amazonCategoryEntityParent = amazonCategoryService.selectById(id);
                id = amazonCategoryEntityParent.getParentId();
            }
            if (id != 0) {
                arr[i] = id;
            }
        }
        for (int i = 0; i <= arr.length / 2 - 1; i++) {
            Long temp1 = arr[i];
            Long temp2 = arr[arr.length - i - 1];
            arr[i] = temp2;
            arr[arr.length - i - 1] = temp1;

        }*/


