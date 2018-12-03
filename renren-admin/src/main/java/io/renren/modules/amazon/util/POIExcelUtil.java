package io.renren.modules.amazon.util;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.swjtu.lang.LANG;
import com.swjtu.querier.Querier;
import com.swjtu.trans.AbstractTranslator;
import com.swjtu.trans.impl.BaiduTranslator;
import io.renren.modules.product.entity.AmazonCategoryEntity;
import io.renren.modules.product.service.AmazonCategoryService;
import io.renren.modules.product.service.impl.AmazonCategoryServiceImpl;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class POIExcelUtil {
    @Autowired
    private static AmazonCategoryService amazonCategoryService;
    /**
     * @methodname: insetCategory 解析excel表格
     * @param: [pathname, region] excel的路径，所属区域(欧洲0、北美1、远东2）
     * @return: void
     * @auther: jhy
     * @date: 2018/12/1 14:41
     */
    public static void insetCategory(String pathname, int region) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(new File(pathname)));
        //获取第一个标签页  根据标签页名称获取
        HSSFSheet sheet = workbook.getSheet("MAPPINGS");
        //遍历标签页
        for (Row row : sheet) {
            //获取单元格
            //如果第一行直接跳过
            if (row.getRowNum() == 0) {
                continue;
            }
            //获取Node Path的数据，以"/"进行拆分
            String categoryNameString = row.getCell(2).getStringCellValue();
            String[] categoryNameArr = categoryNameString.split("/");
            Long parentId = 0L;
            for (int i = 1; i < categoryNameArr.length; i++) {
                //定义亚马逊分类实体
                AmazonCategoryEntity amazonCategoryEntity = new AmazonCategoryEntity();
                //根据分类名称查找判断数据库中是否有此条数据
                Long id = queryByNameId(categoryNameArr[i]);
                if (id == 0L) {
                    //第一次循环
                    if (i == 1) {
                        parentId = 0L;
                        amazonCategoryEntity.setParentId(parentId);
                        amazonCategoryEntity.setRegion(region);
                        amazonCategoryEntity.setDisplayName(EntoZh(categoryNameArr[i]) + categoryNameArr[i]);
                        amazonCategoryEntity.setCategoryName(categoryNameArr[i]);
                        //是否为最后一个分类
                    } else if (categoryNameArr.length - 1 == i) {
                        row.getCell(1).setCellType(CellType.STRING);
                        amazonCategoryEntity.setNodeIdUk(row.getCell(1).getStringCellValue());
                        row.getCell(3).setCellType(CellType.STRING);
                        amazonCategoryEntity.setNodeIdDe(row.getCell(3).getStringCellValue());
                        row.getCell(4).setCellType(CellType.STRING);
                        amazonCategoryEntity.setNodeIdFr(row.getCell(4).getStringCellValue());
                        row.getCell(5).setCellType(CellType.STRING);
                        amazonCategoryEntity.setNodeIdIt(row.getCell(5).getStringCellValue());
                        row.getCell(6).setCellType(CellType.STRING);
                        amazonCategoryEntity.setNodeIdEs(row.getCell(6).getStringCellValue());
                        amazonCategoryEntity.setParentId(parentId);
                        amazonCategoryEntity.setRegion(region);
                        amazonCategoryEntity.setDisplayName(EntoZh(categoryNameArr[i]) + categoryNameArr[i]);
                        amazonCategoryEntity.setCategoryName(categoryNameArr[i]);
                    } else {
                        amazonCategoryEntity.setParentId(parentId);
                        amazonCategoryEntity.setRegion(region);
                        amazonCategoryEntity.setDisplayName(EntoZh(categoryNameArr[i]) + categoryNameArr[i]);
                        amazonCategoryEntity.setCategoryName(categoryNameArr[i]);
                    }
                    parentId = insert(amazonCategoryEntity);
                } else {
                    parentId = id;
                }
            }
        }
    }

    //根据分类名称获取Id
    private static Long queryByNameId(String name) {
        AmazonCategoryEntity amazonCategoryEntity = amazonCategoryService.selectOne(new EntityWrapper<AmazonCategoryEntity>().eq("category_name", name));
        if (amazonCategoryEntity == null) {
            return 0L;
        } else {
            Long id = amazonCategoryEntity.getAmazonCategoryId();
            return id;
        }
    }

    //封装insert插入方法返回插入数据的id
    private static Long insert(AmazonCategoryEntity amazonCategoryEntity) {
        amazonCategoryService.insert(amazonCategoryEntity);
        return amazonCategoryEntity.getAmazonCategoryId();
    }

    //英文翻译成中文
    private static String EntoZh(String name) {
        // 获取查询器
        Querier<AbstractTranslator> querierTrans = new Querier<>();
        // 设置参数：传过来的参数
        querierTrans.setParams(LANG.EN, LANG.ZH, name);
        // 向查询器中添加 Google 翻译器
        // querierTrans.attach(new GoogleTranslator());
        // 向查询器中添加 Baidu 翻译器
        querierTrans.attach(new BaiduTranslator());
        // 执行查询并接收查询结果
        //翻译
        List<String> nameList = querierTrans.execute();
        if (nameList.get(0) != "" && nameList.get(0) != null) {
            return nameList.get(0);
        } else {
            return nameList.get(1);
        }
    }
}