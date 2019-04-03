package io.renren.modules.logistics.controller;

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

import io.renren.modules.logistics.entity.ItemCodeEntity;
import io.renren.modules.logistics.service.ItemCodeService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author wangdh
 * @email 594340717@qq.com
 * @date 2019-04-03 15:39:33
 */
@RestController
@RequestMapping("logistics/itemcode")
public class ItemCodeController {
    @Autowired
    private ItemCodeService itemCodeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("logistics:itemcode:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = itemCodeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{itemCodeId}")
    @RequiresPermissions("logistics:itemcode:info")
    public R info(@PathVariable("itemCodeId") Long itemCodeId){
        ItemCodeEntity itemCode = itemCodeService.selectById(itemCodeId);

        return R.ok().put("itemCode", itemCode);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("logistics:itemcode:save")
    public R save(@RequestBody ItemCodeEntity itemCode){
        itemCodeService.insert(itemCode);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("logistics:itemcode:update")
    public R update(@RequestBody ItemCodeEntity itemCode){
        ValidatorUtils.validateEntity(itemCode);
        itemCodeService.updateAllColumnById(itemCode);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("logistics:itemcode:delete")
    public R delete(@RequestBody Long[] itemCodeIds){
        itemCodeService.deleteBatchIds(Arrays.asList(itemCodeIds));

        return R.ok();
    }

}
