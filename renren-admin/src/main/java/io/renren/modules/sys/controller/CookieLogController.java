package io.renren.modules.sys.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.shiro.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.sys.entity.CookieLogEntity;
import io.renren.modules.sys.service.CookieLogService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * cookie表
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2019-01-11 01:41:22
 */
@RestController
@RequestMapping("sys/cookielog")
public class CookieLogController {
    @Autowired
    private CookieLogService cookieLogService;

    @RequestMapping(value = "/set1688Cookie" )
    public R set1688Cookie(@RequestParam String cookie) {
        SysUserEntity userEntity = ShiroUtils.getUserEntity();
        CookieLogEntity cookieLogEntity = cookieLogService.selectOne(new EntityWrapper<CookieLogEntity>().eq("user_id",userEntity.getUserId()));
        if(cookieLogEntity != null){
            cookieLogEntity.setCookie(cookie);
            cookieLogEntity.setType("1688");
            cookieLogEntity.setUpdateTime(new Date());
            cookieLogService.updateById(cookieLogEntity);
        }else{
            cookieLogEntity = new CookieLogEntity();
            cookieLogEntity.setCookie(cookie);
            cookieLogEntity.setType("1688");
            cookieLogEntity.setUpdateTime(new Date());
            cookieLogEntity.setUserId(userEntity.getUserId());
            cookieLogService.insert(cookieLogEntity);
        }
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:cookielog:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = cookieLogService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{cookieId}")
    @RequiresPermissions("sys:cookielog:info")
    public R info(@PathVariable("cookieId") Long cookieId){
        CookieLogEntity cookieLog = cookieLogService.selectById(cookieId);

        return R.ok().put("cookieLog", cookieLog);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:cookielog:save")
    public R save(@RequestBody CookieLogEntity cookieLog){
        cookieLogService.insert(cookieLog);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:cookielog:update")
    public R update(@RequestBody CookieLogEntity cookieLog){
        ValidatorUtils.validateEntity(cookieLog);
        cookieLogService.updateAllColumnById(cookieLog);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:cookielog:delete")
    public R delete(@RequestBody Long[] cookieIds){
        cookieLogService.deleteBatchIds(Arrays.asList(cookieIds));

        return R.ok();
    }

}
