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
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
        for (int i = 12; i <files.length ; i++) {
        this.insertCategory(file);
         }
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
    //法语翻译成中文
    private String FrtoZh(String name) {
        Querier<AbstractTranslator> querierTrans = new Querier<>(); // 获取查询器
        querierTrans.setParams(LANG.FRA, LANG.ZH, name);// 设置参数：传过来的参数
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
    //德语翻译成中文
    private String DetoZh(String name) {
        Querier<AbstractTranslator> querierTrans = new Querier<>(); // 获取查询器
        querierTrans.setParams(LANG.DE, LANG.ZH, name);// 设置参数：传过来的参数
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
    //意大利翻译成中文
    private String IttoZh(String name) {
        Querier<AbstractTranslator> querierTrans = new Querier<>(); // 获取查询器
        querierTrans.setParams(LANG.IT, LANG.ZH, name);// 设置参数：传过来的参数
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
    //西班牙翻译成中文
    private String EstoZh(String name) {
        Querier<AbstractTranslator> querierTrans = new Querier<>(); // 获取查询器
        querierTrans.setParams(LANG.SPA, LANG.ZH, name);// 设置参数：传过来的参数
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
    //日语翻译成中文
    private String JptoZh(String name) {
        Querier<AbstractTranslator> querierTrans = new Querier<>(); // 获取查询器
        querierTrans.setParams(LANG.JP, LANG.ZH, name);// 设置参数：传过来的参数
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
        Workbook workbook=null;
        String[] split = fileName.getName().split("\\.");
        if (split[split.length-1].equals("xlsx")){
            //创建Excel文档对象XSSFWorkbook
            workbook = new XSSFWorkbook(new FileInputStream(fileName));
        }else if(split[split.length-1].equals("xls")){
            //创建Excel文档对象HSSFWorkbook
            workbook = new HSSFWorkbook(new FileInputStream(fileName));
        }
        //获取第一个标签页  根据标签页名称获取
        Sheet sheet = workbook.getSheet("sheet");
        //遍历标签页
        for (Row row : sheet) {
            //获取单元格，如果第一行直接跳过 就是总的
            if (row.getRowNum() == 0) {
                continue;
            }
            //获取Node Path的数据，以"/"进行拆分
            String categoryNameString = row.getCell(1).getStringCellValue();
            String[] categoryNameArr = categoryNameString.split("/");
            //定义父id为零
            Long parentId = 0L;
            for (int i = 0; i < categoryNameArr.length; i++) {
                //定义亚马逊分类实体
                AmazonCategoryEntity amazonCategoryEntity = new AmazonCategoryEntity();
                String[] categoryNameQArr = categoryNameString.split(categoryNameArr[i]);
                Long id =null;
                if (categoryNameQArr.length !=0){
                    amazonCategoryEntity.setCategoryQ(categoryNameQArr[0]);
                    //根据分类名称查找判断数据库中是否有此条数据
                    id = this.queryByNameId(categoryNameArr[i],"FR",categoryNameQArr[0]);
                }else {
                    //根据分类名称查找判断数据库中是否有此条数据
                    id = this.queryByNameId(categoryNameArr[i],"FR","");
                }

                if (id == 0L) {
                    //第一次循环
                    if (i == 0) {
                        parentId = 0L;
                        amazonCategoryEntity.setParentId(parentId);
                        amazonCategoryEntity.setCountryCode("FR");//英国
                        String categoryNameChina = this.EntoZh(categoryNameArr[i]);
                        String categoryNameChinaReplace = categoryNameChina.replace("\"", "").replace("\"", "");//去掉双引号
                        amazonCategoryEntity.setDisplayName(categoryNameChinaReplace + "/" + categoryNameArr[i]);
                        amazonCategoryEntity.setCategoryName(categoryNameArr[i]);
                        row.getCell(0).setCellType(CellType.STRING);
                        amazonCategoryEntity.setNodeIdFr(row.getCell(0).getStringCellValue());
                    } else {
                        amazonCategoryEntity.setParentId(parentId);
                        amazonCategoryEntity.setCountryCode("FR");//英国
                        String categoryNameChina = this.EntoZh(categoryNameArr[i]);
                        String categoryNameChinaReplace = categoryNameChina.replace("\"", "").replace("\"", "");//去掉双引号
                        amazonCategoryEntity.setDisplayName(categoryNameChinaReplace + "/" + categoryNameArr[i]);
                        amazonCategoryEntity.setCategoryName(categoryNameArr[i]);
                        row.getCell(0).setCellType(CellType.STRING);
                        amazonCategoryEntity.setNodeIdFr(row.getCell(0).getStringCellValue());
                    }
                    parentId = this.insert(amazonCategoryEntity);
                } else {
                    parentId = id;
                }
            }
        }
    }
}

