package io.renren.modules.product.controller;

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

import io.renren.modules.product.entity.AmazonCategoryEntity;
import io.renren.modules.product.service.AmazonCategoryService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;


/**
 * 亚马逊分类表
 *
 * @author jhy
 * @email 617493711@qq.com
 * @date 2018-11-16 11:11:30
 */
@RestController
@RequestMapping("product/amazoncategory")
public class AmazonCategoryController {
    @Autowired
    private AmazonCategoryService amazonCategoryService;

    /**
     * @methodname: list 列表
     * @param: [params] 接受参数
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/16 11:23
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:amazoncategory:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = amazonCategoryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * @methodname: info 信息
     * @param: [amazonCategoryId] 分类id
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/16 11:24
     */
    @RequestMapping("/info/{amazonCategoryId}")
    @RequiresPermissions("product:amazoncategory:info")
    public R info(@PathVariable("amazonCategoryId") Long amazonCategoryId) {

        AmazonCategoryEntity amazonCategory = amazonCategoryService.selectById(amazonCategoryId);

        return R.ok().put("amazonCategory", amazonCategory);
    }

    /**
     * @methodname: save 保存
     * @param: [amazonCategory] 分类实体
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/16 11:25
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:amazoncategory:save")
    public R save(@RequestBody AmazonCategoryEntity amazonCategory) {


        amazonCategoryService.insert(amazonCategory);

        return R.ok();
    }

    /**
     * @methodname: update 修改
     * @param: [amazonCategory] 分类实体
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/16 11:25
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:amazoncategory:update")
    public R update(@RequestBody AmazonCategoryEntity amazonCategory) {

        ValidatorUtils.validateEntity(amazonCategory);
        amazonCategoryService.updateAllColumnById(amazonCategory);//全部更新

        return R.ok();
    }

    /**
     * @methodname: delete 删除
     * @param: [amazonCategoryIds] 分类的数组ids
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/16 11:26
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:amazoncategory:delete")
    public R delete(@RequestBody Long[] amazonCategoryIds) {

        amazonCategoryService.deleteBatchIds(Arrays.asList(amazonCategoryIds));

        return R.ok();
    }

    /**
     * @methodname: queryByAreaOneClassify 根据区域查出所有一级分类
     * @param: [region]
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/16 11:51
     */
    @RequestMapping("/amazon one category")
    public R queryByAreaOneClassify(Integer region){
        List<AmazonCategoryEntity>amazonCategoryEntityList=amazonCategoryService.queryByAreaOneClassify(region);
        return R.ok().put("amazonCategoryEntityList",amazonCategoryEntityList);
    }

    /**
     * @methodname: queryByParentIdChildList 根据父级id查出子级的列表
     * @param: [amazonCategoryId] 亚马逊分类id
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/16 15:02
     */
    @RequestMapping("/child category list")
    public R queryByParentIdChildList(Long amazonCategoryId){
        List<AmazonCategoryEntity>amazonCategoryEntityChildList=amazonCategoryService.selectList(new EntityWrapper<AmazonCategoryEntity>().eq("parent_id",amazonCategoryId));
        for (AmazonCategoryEntity amazonCategoryEntity : amazonCategoryEntityChildList) {
            int temp=amazonCategoryService.selectCount(new EntityWrapper<AmazonCategoryEntity>().eq("parent_id",amazonCategoryEntity.getAmazonCategoryId()));
            //判断出是否有下一级分类
            if (temp==0){
                amazonCategoryEntity.setIfNext("false");
            }else{
                amazonCategoryEntity.setIfNext("true");
            }
        }
        return R.ok().put("amazonCategoryEntityChildList",amazonCategoryEntityChildList);
    }

    /**
     * @methodname: queryByChindIdParentId 根据子级id查出所有的父级id以逗号进行拼接成字符串
     * @param: [amazonCategoryId] 子级id
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/16 15:46
     */
    @RequestMapping()
    public R queryByChindIdParentId(Long amazonCategoryId){
        String ids=amazonCategoryService.queryByChindIdParentId(amazonCategoryId);
        return R.ok().put("ids",ids);
    }
}
