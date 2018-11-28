package io.renren.modules.product.controller;

import java.util.*;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.amazon.entity.AmazonCategoryHistoryEntity;
import io.renren.modules.amazon.service.AmazonCategoryHistoryService;
import io.renren.modules.product.entity.ProductsEntity;
import io.renren.modules.product.service.ProductsService;
import io.renren.modules.product.vm.AddUploadVM;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.product.entity.UploadEntity;
import io.renren.modules.product.service.UploadService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 产品上传
 *
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
@RestController
@RequestMapping("product/upload")
public class UploadController extends AbstractController {
    @Autowired
    private UploadService uploadService;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private AmazonCategoryHistoryService amazonCategoryHistoryService;
    /**
     * @methodname: list 信息
     * @param: [params] 接受参数
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/8 21:22
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:upload:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = uploadService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * @methodname: info 信息
     * @param: [uploadId] 下载id
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/8 21:22
     */
    @RequestMapping("/info/{uploadId}")
    @RequiresPermissions("product:upload:info")
    public R info(@PathVariable("uploadId") Long uploadId){
        UploadEntity upload = uploadService.selectById(uploadId);

        return R.ok().put("upload", upload);
    }

    /**
     * @methodname: save 保存
     * @param: [upload] 下载实体
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/8 21:22
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:upload:save")
    public R save(@RequestBody UploadEntity upload){
        uploadService.insert(upload);

        return R.ok();
    }

    /**
     * @methodname: update 更新
     * @param: [upload] 下载实体
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/8 21:22
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:upload:update")
    public R update(@RequestBody UploadEntity upload){
        ValidatorUtils.validateEntity(upload);
        uploadService.updateAllColumnById(upload);//全部更新
        
        return R.ok();
    }

    /**
     * @methodname: delete 删除
     * @param: [uploadIds] 下载id数组
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/8 21:22
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:upload:delete")
    public R delete(@RequestBody Long[] uploadIds){
        uploadService.deleteBatchIds(Arrays.asList(uploadIds));

        return R.ok();
    }

    /**
     * addUpload:立即上传
     * @param: [addUploadVM]
     *
     * @return: io.renren.common.utils.R
     * @auther: wdh
     * @date: 2018/11/27 16:17
     */
    @RequestMapping("/addUpload")
//    @RequiresPermissions("product:upload:addupload")
    public R addUpload(@RequestBody AddUploadVM addUploadVM){
        List<UploadEntity> uploadList = new ArrayList<UploadEntity>();
        Set<Long> ret = new LinkedHashSet<>(0);
        if(addUploadVM.getStartId() != null && addUploadVM.getEndId() != null){
            Long index = addUploadVM.getStartId();
            while (index <= addUploadVM.getEndId()){
                ret.add(index);
                index++;
            }
        }
        if(addUploadVM.getUploadIds() != null){
            ret.addAll(Arrays.asList(addUploadVM.getUploadIds()));
        }
        System.out.println("ret:" + ret);
        //迭代
        Iterator i = ret.iterator();
        //遍历
        while(i.hasNext()){
            UploadEntity upload = new UploadEntity();
            //获取产品
            ProductsEntity product = productsService.selectById(Long.valueOf(i.next().toString()));
            //设置产品id
            upload.setProductId(product.getProductId());
            //设置主图片
            upload.setMainUrl(product.getMainImageUrl());
            //设置授权账户
            upload.setGrantShopId(addUploadVM.getGrantShopId());
            upload.setGrantShop(addUploadVM.getGrantShop());
            //设置分类
            upload.setAmazonCategoryId(addUploadVM.getAmazonCategoryId());
            upload.setAmazonCategory(addUploadVM.getAmazonCategory());
            //设置模板
            upload.setAmazonTemplateId(addUploadVM.getAmazonTemplateId());
            upload.setAmazonTemplate(addUploadVM.getAmazonTemplate());
            //设置操作类型（0：上传   1：修改）
            upload.setOperateType(0);
            //数组转','号隔开的字符串
            String operateItem = StringUtils.join(addUploadVM.getOperateItem(),",");
            //设置操作项
            upload.setOperateItem(operateItem);
            //设置是否有分类属性
            upload.setIsAttribute(addUploadVM.getIsAttribute());
            // TODO: 2018/11/27 分类属性
            //设置状态(0：正在上传1：上传成功2：上传失败)
            upload.setUploadState(0);
            //设置常用属性
            upload.setUploadTime(new Date());
            upload.setUpdateTime(new Date());
            upload.setUserId(getUserId());
            upload.setDeptId(getDeptId());
            //添加到list
            uploadList.add(upload);
        }
        //批量添加到上传表
        uploadService.insertBatch(uploadList);
        //添加到分类历史记录表
        AmazonCategoryHistoryEntity categoryHistory = amazonCategoryHistoryService.selectByAmazonCategoryId(addUploadVM.getAmazonCategoryId());
        //如果有历史数据，则累加数量1
        if(categoryHistory != null){
            int count = categoryHistory.getCount() + 1;
            categoryHistory.setCount(count);
            amazonCategoryHistoryService.updateAllColumnById(categoryHistory);
        }else{
            //如果没有历史数据，则新增历史数据
            AmazonCategoryHistoryEntity categoryHistoryNew = new AmazonCategoryHistoryEntity();
            categoryHistoryNew.setAmazonCategoryId(addUploadVM.getAmazonCategoryId());
            categoryHistoryNew.setAmazonCategory(addUploadVM.getAmazonCategory());
            categoryHistoryNew.setCount(1);
            categoryHistoryNew.setUserId(getUserId());
            categoryHistoryNew.setUserId(getDeptId());
            amazonCategoryHistoryService.insert(categoryHistoryNew);
        }
        return R.ok();
    }

}
