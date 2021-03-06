package io.renren;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.exception.RRException;
import io.renren.modules.product.entity.ImageAddressEntity;
import io.renren.modules.product.service.ImageAddressService;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.apache.http.entity.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.awt.Image;
@SpringBootTest
@RunWith(SpringRunner.class)
public class ImageDemo extends AbstractController {
    @Autowired
    private ImageAddressService imageAddressService;

    /**
     * 图片上传
     * 入参：file 产品id
     */
    @Test
    public void upload() {
        File file = new File("C:\\Users\\asus\\Desktop\\9cc804b86b2aa5c5349983c8b803963a_U4P1T52D145F1033DT20131105102009.jpg");
        try {
            MultipartFile mulFile = new MockMultipartFile(
                    "9cc804b86b2aa5c5349983c8b803963a_U4P1T52D145F1033DT20131105102009.jpg", //文件名
                    "9cc804b86b2aa5c5349983c8b803963a_U4P1T52D145F1033DT20131105102009.jpg", //originalName 相当于上传文件在客户机上的文件名
                    ContentType.APPLICATION_OCTET_STREAM.toString(), //文件类型
                    new FileInputStream(file)//文件流
            );
            //判断文件是否为空
            if (mulFile.isEmpty()) {
                //自定义异常
                throw new RRException("上传文件不能为空");
            }
            // 上传文件夹获取文件名
            String fileName = mulFile.getOriginalFilename();
            // 获取上传文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            // 文件上传后是保存在本地的路径
//            String filePath = "D:/test/";
            String filePath = "/usr/test/";
            String url = 2 + "/" + fileName;
            //文件上传的位置
            File dest = new File(filePath + 2 + "/" + fileName);
            // 检测是否存在目录不存在创建一个文件
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            //把MultipartFile转成File
            CommonsMultipartFile cf= (CommonsMultipartFile)mulFile;
            DiskFileItem fi = (DiskFileItem)cf.getFileItem();
            File fileone = fi.getStoreLocation();

            Image srcImg=ImageIO.read(file);
            BufferedImage buffImg = null;
            buffImg = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
            buffImg.getGraphics().drawImage(
                    srcImg.getScaledInstance(1000, 1000, Image.SCALE_SMOOTH), 0,
                    0, null);

            ImageIO.write(buffImg, suffixName,dest);

            try {
                ImageAddressEntity imageAddressEntity = new ImageAddressEntity();
                imageAddressEntity.setImageUrl(url);
                imageAddressEntity.setProductId(2L);
                imageAddressEntity.setIsDeleted("0");
                imageAddressEntity.setCreateTime(new Date());

                imageAddressService.insert(imageAddressEntity);

            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据产品id查图片信息
     * 入参：产品id
     */
    @Test
    public void imageinfo() {
        List<ImageAddressEntity> imageInfo = imageAddressService.selectList(new EntityWrapper<ImageAddressEntity>().eq("product_id", 1L).eq("is_deleted", "0"));
        for (ImageAddressEntity addressEntity : imageInfo) {
            String id = addressEntity.getIsDeleted();
            System.out.println(id);
        }
    }

    /**
     * 图片进行批量删除
     * 入参：图片id
     */
    @Test
    public void updateimage() {
        Long[] imageIds = {16L, 17L};
        for (int i = 0; i < imageIds.length; i++) {
            ImageAddressEntity addressEntity = imageAddressService.selectById(imageIds[i]);
            addressEntity.setIsDeleted("1");
            imageAddressService.updateById(addressEntity);
        }
    }

    /**
     * 根据产品id查出被逻辑删除的图片
     * 入参：产品id
     */
    @Test
    public void isdeleteList() {
        List<ImageAddressEntity> isdeleteList = imageAddressService.selectList(new EntityWrapper<ImageAddressEntity>().eq("product_id", 1L).eq("is_deleted", "1"));
        for (ImageAddressEntity addressEntity : isdeleteList) {
            String id = addressEntity.getIsDeleted();
            System.out.println(id);
        }
    }

    /**
     * 恢复逻辑删除的图片
     * 入参：图片id数组
     */
    @Test
    public void recoverdelete() {
        Long[] imageIds = {16L, 17L};
        for (int i = 0; i < imageIds.length; i++) {
            ImageAddressEntity addressEntity = imageAddressService.selectById(imageIds[i]);
            addressEntity.setIsDeleted("0");
            imageAddressService.updateById(addressEntity);
        }
    }

    /**
     * 彻底删除图片信息及图片文件
     */
    @Test
    public void deleteimage() {
        Long[] imageIds = {16L, 17L};
        for (int i = 0; i < imageIds.length; i++) {
            ImageAddressEntity imageAddressEntity = imageAddressService.selectById(imageIds[i]);
            String url = imageAddressEntity.getImageUrl();
//            File file = new File("D:/test/" + url);
            File file = new File("/usr/test/" + url);
            file.delete();
            imageAddressService.deleteById(imageIds[i]);
        }

    }
}
