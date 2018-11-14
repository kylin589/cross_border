package io.renren.modules.product.controller;

import java.util.Arrays;
import java.util.Map;

import io.renren.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
public class UploadController {
    @Autowired
    private UploadService uploadService;

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

}
