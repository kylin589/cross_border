package io.renren.modules.amazon.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.amazon.entity.AmazonCategoryHistoryEntity;
import io.renren.modules.amazon.service.AmazonCategoryHistoryService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-11-27 16:33:14
 */
@RestController
@RequestMapping("amazon/amazoncategoryhistory")
public class AmazonCategoryHistoryController extends AbstractController{
    @Autowired
    private AmazonCategoryHistoryService amazonCategoryHistoryService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("amazon:amazoncategoryhistory:list")
    public R list(@RequestParam Map<String, Object> params){
        params.put("userId",getUserId());
        PageUtils page = amazonCategoryHistoryService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/getMyList")
//    @RequiresPermissions("amazon:amazoncategoryhistory:list")
    public R list(String countryCode){
        List<AmazonCategoryHistoryEntity> list = new ArrayList<AmazonCategoryHistoryEntity>();
        list = amazonCategoryHistoryService.selectList(new EntityWrapper<AmazonCategoryHistoryEntity>().eq("user_id",getUserId()).eq("country_code",countryCode));
        return R.ok().put("list", list);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{categoryHistoryId}")
    @RequiresPermissions("amazon:amazoncategoryhistory:info")
    public R info(@PathVariable("categoryHistoryId") Long categoryHistoryId){
        AmazonCategoryHistoryEntity amazonCategoryHistory = amazonCategoryHistoryService.selectById(categoryHistoryId);

        return R.ok().put("amazonCategoryHistory", amazonCategoryHistory);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("amazon:amazoncategoryhistory:save")
    public R save(@RequestBody AmazonCategoryHistoryEntity amazonCategoryHistory){
        amazonCategoryHistoryService.insert(amazonCategoryHistory);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("amazon:amazoncategoryhistory:update")
    public R update(@RequestBody AmazonCategoryHistoryEntity amazonCategoryHistory){
        ValidatorUtils.validateEntity(amazonCategoryHistory);
        amazonCategoryHistoryService.updateAllColumnById(amazonCategoryHistory);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("amazon:amazoncategoryhistory:delete")
    public R delete(@RequestBody Long[] categoryHistoryIds){
        amazonCategoryHistoryService.deleteBatchIds(Arrays.asList(categoryHistoryIds));

        return R.ok();
    }

}
