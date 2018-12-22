package io.renren.modules.product.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.product.entity.ImageAddressEntity;
import io.renren.modules.product.service.ImageAddressService;
import io.renren.modules.sys.controller.AbstractController;
import io.renren.modules.util.FtpUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


/**
 * 产品图片表
 *
 * @author jhy
 * @email 617493711@qq.com
 * @date 2018-11-08 09:59:28
 */
@RestController
@RequestMapping("product/imageaddress")
public class ImageAddressController extends AbstractController {
    @Autowired
    private ImageAddressService imageAddressService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:imageaddress:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = imageAddressService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{imageId}")
    @RequiresPermissions("product:imageaddress:info")
    public R info(@PathVariable("imageId") Long imageId) {
        ImageAddressEntity imageAddress = imageAddressService.selectById(imageId);

        return R.ok().put("imageAddress", imageAddress);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:imageaddress:save")
    public R save(@RequestBody ImageAddressEntity imageAddress) {
        imageAddressService.insert(imageAddress);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:imageaddress:update")
    public R update(@RequestBody ImageAddressEntity imageAddress) {
        ValidatorUtils.validateEntity(imageAddress);
        imageAddressService.updateAllColumnById(imageAddress);//全部更新

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:imageaddress:delete")
    public R delete(@RequestBody Long[] imageIds) {
        imageAddressService.deleteBatchIds(Arrays.asList(imageIds));

        return R.ok();
    }

    /**
     * @methodname: upload 上传图片
     * @param: [file, productId] 上传的图片文件，产品id
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/6 15:54
     */
    //保存到数据库的Url前缀
    private static final String fileUrl = "http://hgjfsdg.top/image/";
    @RequestMapping("/upload")
    public R upload(@RequestParam("file") MultipartFile file, Long productId) throws Exception {
        //判断文件是否为空
        try {
            if (file.isEmpty()) {
                return R.error("上传文件不能为空");
            }
            // 上传文件夹获取文件名
            String fileName = file.getOriginalFilename();
            // 获取上传文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            if (!suffixName.equalsIgnoreCase(".jpg")){
                return R.error(1,"图片格式不正确，请上传jpg格式的图片");
            }
            //获取图片的输入流
            InputStream inputStream = file.getInputStream();
            //获取年月日以年月日来分目录
            Calendar calendar = Calendar.getInstance();
            String year = String.valueOf(calendar.get(Calendar.YEAR));
            String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
            String date = String.valueOf(calendar.get(Calendar.DATE));
           // 文件上传到ftp的地址上
            String filePath = "/image/" + year + "/" + month + "/" + date + "/" + productId + "/";
            //调用FtpUtil的方法，连接ftp服务器，并把图片上传到服务器上
            Boolean flag = FtpUtil.uploadFile(fileName, inputStream, filePath);
            if (flag == true) {
                ImageAddressEntity imageAddressEntity = new ImageAddressEntity();
                String url = year + "/" + month + "/" + date + "/" + productId + "/" + fileName;
                String urlOne = fileUrl + url;
                imageAddressEntity.setImageUrl(urlOne);
                imageAddressEntity.setProductId(productId);
                imageAddressEntity.setIsDeleted("0");
                imageAddressEntity.setCreateTime(new Date());
                imageAddressEntity.setCreateUserId(getUserId());
                imageAddressEntity.setLastOperationUserId(getUserId());
                imageAddressEntity.setLastOperationTime(new Date());
                imageAddressService.insert(imageAddressEntity);
                Long id = imageAddressEntity.getImageId();
                return R.ok().put("url", urlOne).put("id", id);}
            } catch(IllegalStateException e){
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();
            }
        return R.error(1, "上传失败");
    }

    /**
     * @methodname: imageinfo:根据产品id查图片
     * @param: [productId] 产品的id
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/6 15:54
     */
    @RequestMapping("/imageinfo")
    public R imageinfo(@RequestParam Long productId) throws Exception {
        List<ImageAddressEntity> imageInfo = imageAddressService.selectList(new EntityWrapper<ImageAddressEntity>().eq("product_id", productId).eq("is_deleted", "0"));
        return R.ok().put("imageInfo", imageInfo);
    }

    /**
     * @methodname: deleteimage 图片批量删除
     * @param: [imageIds]
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/6 15:53
     */
    @RequestMapping("/updateimage")
    public R updateimage(@RequestBody Long[] imageIds) {
        for (int i = 0; i < imageIds.length; i++) {
            ImageAddressEntity addressEntity = imageAddressService.selectById(imageIds[i]);
            addressEntity.setIsDeleted("1");
            addressEntity.setLastOperationTime(new Date());
            addressEntity.setLastOperationUserId(getUserId());
            imageAddressService.updateById(addressEntity);
        }
        return R.ok();
    }

    /**
     * @methodname: isdeleteList 根据产品id查出被逻辑删除的图片
     * @param: [productId] 产品的id
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/7 14:32
     */
    @RequestMapping("/querydeleteimage")
    public R isdeleteList(@RequestParam Long productId) {
        List<ImageAddressEntity> isdeleteList = imageAddressService.selectList(new EntityWrapper<ImageAddressEntity>().eq("product_id", productId).eq("is_deleted", "1"));
        return R.ok().put("isdeleteList", isdeleteList);
    }

    /**
     * @methodname: recoverdelete 恢复逻辑删除的图片
     * @param: [imageIds] 图片id数组
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/7 14:32
     */
    @RequestMapping("/recoverdelete")
    public R recoverdelete(@RequestBody Long[] imageIds) {
        for (int i = 0; i < imageIds.length; i++) {
            ImageAddressEntity addressEntity = imageAddressService.selectById(imageIds[i]);
            addressEntity.setIsDeleted("0");
            addressEntity.setLastOperationTime(new Date());
            addressEntity.setLastOperationUserId(getUserId());
            imageAddressService.updateById(addressEntity);
        }
        return R.ok();
    }

    /**
     * @methodname: deleteimage:根据图片id彻底删除图片信息及图片文件
     * @param: [imageIds] 图片的id数组
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/7 15:21
     */
    @RequestMapping("/deleteimage")
    public R deleteimage(@RequestBody Long[] imageIds) {
        for (int i = 0; i < imageIds.length; i++) {
            ImageAddressEntity addressEntity = imageAddressService.selectById(imageIds[i]);
            // 文件上传后是保存在本地的路径
            String filePath = "E:/test/";
            String url = filePath + addressEntity.getImageUrl();
            File file = new File(url);
            file.delete();
            imageAddressService.deleteById(imageIds[i]);
        }
        return R.ok();
    }
}
