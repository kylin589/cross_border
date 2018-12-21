package io.renren.modules.util;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.swjtu.lang.LANG;
import com.swjtu.querier.Querier;
import com.swjtu.trans.AbstractTranslator;
import com.swjtu.trans.impl.BaiduTranslator;
import com.swjtu.trans.impl.GoogleTranslator;
import io.renren.modules.product.entity.AmazonCategoryEntity;
import io.renren.modules.product.service.AmazonCategoryService;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Level;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UKExcel {
    @Autowired
    private AmazonCategoryService amazonCategoryService;

    /**
     * @methodname 解析英国商品分类Excel
     * @auther: jhy
     * @date: 2018/12/19 10:32
     */
    @Test
    public void poiExcel() throws Exception {
        File file = new File("C:\\Users\\asus\\Desktop\\法国商品分类FR");
        File[] files = file.listFiles();
        //System.out.println(files.length);
        for (int i = 0; i <files.length ; i++) {
            //System.out.println(files[i]);
            this.insertCategory(files[i]);
        }
        //String pathname = "C:\\Users\\asus\\Desktop\\uk\\uk_drugstore_browse_tree_guide._TTH_.xls";//文件的地址
    }

    //根据分类名称获取IdString nodeIdUk,.eq("node_id_uk",nodeIdUk)
    private Long queryByNameId(String categoryName,String countryCode,String categoryQ) {
        AmazonCategoryEntity amazonCategoryEntity = amazonCategoryService.selectOne(new EntityWrapper<AmazonCategoryEntity>().eq("category_name",categoryName).eq("country_code",countryCode).eq("category_q",categoryQ));
        if (amazonCategoryEntity == null) {
            return 0L;
        } else {
            Long id = amazonCategoryEntity.getAmazonCategoryId();
            return id;
        }
    }

    //封装insert插入方法返回插入数据的id
    private Long insert(AmazonCategoryEntity amazonCategoryEntity) {
        amazonCategoryService.insert(amazonCategoryEntity);
        return amazonCategoryEntity.getAmazonCategoryId();
    }

    //英文翻译成中文
    private String EntoZh(String name) {
        Querier<AbstractTranslator> querierTrans = new Querier<>(); // 获取查询器
        querierTrans.setParams(LANG.EN, LANG.ZH, name);// 设置参数：传过来的参数
        //querierTrans.attach(new GoogleTranslator());// 向查询器中添加 Google 翻译器
        querierTrans.attach(new BaiduTranslator());// 向查询器中添加 Baidu 翻译器
        // 执行查询并接收查询结果
        List<String> nameList = querierTrans.execute();//翻译
        if (nameList.get(0) != "" && nameList.get(0) != null) {
            return nameList.get(0);
        } else {
            return nameList.get(1);
        }
    }

    //excel的分类方法插入到数据库
    public  void  insertCategory(File fileName) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(fileName));//创建Excel文档对象HSSFWorkbook
        HSSFSheet sheet = workbook.getSheet("sheet");//获取第一个标签页  根据标签页名称获取
        for (Row row : sheet) {//遍历标签页
            if (row.getRowNum() == 0) {//获取单元格，如果第一行直接跳过 就是总的
                continue;
            }
            String categoryNameString = row.getCell(1).getStringCellValue();//获取Node Path的数据，以"/"进行拆分
            /*row.getCell(0).setCellType(CellType.STRING);
            String nodeIdUk = row.getCell(0).getStringCellValue();*/
            String[] categoryNameArr = categoryNameString.split("/");
            AmazonCategoryEntity amazonCategoryEntity = new AmazonCategoryEntity();//定义亚马逊分类实体
            if(categoryNameArr.length == 1){
                //一级类目
                amazonCategoryEntity.setCountryCode("FR");
                amazonCategoryEntity.setCategoryQ("");
                String categoryNameChina = this.EntoZh(categoryNameArr[0]);
                String categoryNameChinaReplace = categoryNameChina.replace("\"", "").replace("\"", "");//去掉双引号
                amazonCategoryEntity.setDisplayName(categoryNameChinaReplace + "/" + categoryNameArr[0]);
                amazonCategoryEntity.setParentId(0L);
                amazonCategoryEntity.setCategoryName(categoryNameArr[0]);
                row.getCell(0).setCellType(CellType.STRING);
                amazonCategoryEntity.setNodeIdFr(row.getCell(0).getStringCellValue());
                this.insert(amazonCategoryEntity);
            }else{
                //非一级类目
                //倒数第二个
                String a1 = categoryNameArr[categoryNameArr.length-2];
                //倒数第一个
                String a2 = categoryNameArr[categoryNameArr.length-1];
                //以倒数第二个为条件拆分字符串
                String before1 = categoryNameString.split(a1)[0];
                //根据分类名称查找判断数据库中是否有此条数据
                Long id1 = this.queryByNameId(a1,"FR",before1);
                //以倒数第一个为条件拆分字符串
                String before2 = categoryNameString.split(a2)[0];
                //插入最后一个
                amazonCategoryEntity.setCountryCode("FR");
                amazonCategoryEntity.setCategoryQ(before2);
                String categoryNameChina = this.EntoZh(a2);
                String categoryNameChinaReplace = categoryNameChina.replace("\"", "").replace("\"", "");//去掉双引号
                amazonCategoryEntity.setDisplayName(categoryNameChinaReplace + "/" + a2);
                amazonCategoryEntity.setParentId(id1);
                amazonCategoryEntity.setCategoryName(a2);
                row.getCell(0).setCellType(CellType.STRING);
                amazonCategoryEntity.setNodeIdFr(row.getCell(0).getStringCellValue());
                this.insert(amazonCategoryEntity);
            }
        }
    }
}

