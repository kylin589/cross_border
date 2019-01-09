package io.renren.modules.amazon.controller;

import java.util.Arrays;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.amazon.entity.ErrorCodeEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.amazon.service.ErrorCodeService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;


/**
 * @author zjr
 * @email 1981763981@qq.com
 * @date 2019-01-09 14:18:08
 */
@RestController
@RequestMapping("amazon/errorcode")
public class ErrorCodeController {
    @Autowired
    private ErrorCodeService errorCodeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("amazon:errorcode:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = errorCodeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("amazon:errorcode:info")
    public R info(@PathVariable("id") Long id) {
        ErrorCodeEntity errorCode = errorCodeService.selectById(id);

        return R.ok().put("errorCode", errorCode);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("amazon:errorcode:save")
    public R save(@RequestBody ErrorCodeEntity errorCodeEntity) {
        errorCodeService.insert(errorCodeEntity);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("amazon:errorcode:update")
    public R update(@RequestBody ErrorCodeEntity errorCodeEntity) {
        //ValidatorUtils.validateEntity(errorCodeEntity);
        errorCodeService.updateAllColumnById(errorCodeEntity);//全部更新

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("amazon:errorcode:delete")
    public R delete(@RequestBody Long[] ids) {
        errorCodeService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 根据错误代码查询错误解释
     */
    @RequestMapping("/explanation")
    public R queryExplanationById(String errorCode) {
        ErrorCodeEntity codeEntity = errorCodeService.selectOne(new EntityWrapper<ErrorCodeEntity>().eq("error_code", errorCode));
        return R.ok().put("data", codeEntity);
    }
}
