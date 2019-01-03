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

import io.renren.modules.product.entity.FieldMiddleEntity;
import io.renren.modules.product.service.FieldMiddleService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author zjr
 * @email 1981763981@qq.com
 * @date 2018-12-24 05:20:03
 */
@RestController
@RequestMapping("product/fieldmiddle")
public class FieldMiddleController {
    @Autowired
    private FieldMiddleService fieldMiddleService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:fieldmiddle:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = fieldMiddleService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{middleId}")
    @RequiresPermissions("product:fieldmiddle:info")
    public R info(@PathVariable("middleId") Long middleId){
        FieldMiddleEntity fieldMiddle = fieldMiddleService.selectById(middleId);

        return R.ok().put("fieldMiddle", fieldMiddle);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:fieldmiddle:save")
    public R save(@RequestBody FieldMiddleEntity fieldMiddle){
        fieldMiddleService.insert(fieldMiddle);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:fieldmiddle:update")
    public R update(@RequestBody FieldMiddleEntity fieldMiddle){
        //ValidatorUtils.validateEntity((fieldMiddle);
        fieldMiddleService.updateAllColumnById(fieldMiddle);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:fieldmiddle:delete")
    public R delete(@RequestBody Long[] middleIds){
        fieldMiddleService.deleteBatchIds(Arrays.asList(middleIds));

        return R.ok();
    }

}
