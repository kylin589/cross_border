package io.renren.modules.amazon.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.amazon.entity.ResultXmlEntity;
import io.renren.modules.amazon.service.ResultXmlService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 亚马逊商品上传结果表
 *
 * @author zjr
 * @email 1981763981@qq.com
 * @date 2018-12-24 07:26:40
 */
@RestController
@RequestMapping("amazon/resultxml")
public class ResultXmlController {
    @Autowired
    private ResultXmlService resultXmlService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("amazon:resultxml:list")
    public R list(Long uploadId){
        EntityWrapper<ResultXmlEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("upload_id",uploadId);
        wrapper.ne("state",2);
        List<ResultXmlEntity> resultXmlEntities = resultXmlService.selectList(wrapper);
        return R.ok().put("data", resultXmlEntities);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("amazon:resultxml:info")
    public R info(@PathVariable("id") Long id){
        ResultXmlEntity resultXml = resultXmlService.selectById(id);

        return R.ok().put("resultXml", resultXml);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("amazon:resultxml:save")
    public R save(@RequestBody ResultXmlEntity resultXml){
        resultXmlService.insert(resultXml);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("amazon:resultxml:update")
    public R update(@RequestBody ResultXmlEntity resultXml){
        ValidatorUtils.validateEntity(resultXml);
        resultXmlService.updateAllColumnById(resultXml);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("amazon:resultxml:delete")
    public R delete(@RequestBody Long[] ids){
        resultXmlService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
