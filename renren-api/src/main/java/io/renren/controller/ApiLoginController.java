package io.renren.controller;


import io.renren.annotation.Login;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.form.LoginForm;
import io.renren.service.TokenService;
import io.renren.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * 登录接口
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:31
 */
@RestController
@RequestMapping("/api")
@Api(tags="登录接口")
public class ApiLoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
//    @Autowired
//    private AmazonGrantShopService amazonGrantShopService;

    @PostMapping("login")
    @ApiOperation("登录")
    public R login(@RequestBody LoginForm form){
        //表单校验
        ValidatorUtils.validateEntity(form);
        //用户登录
        Map<String, Object> map = userService.login(form);
        return R.ok(map);
    }

    @Login
    @PostMapping("logout")
    @ApiOperation("退出")
    public R logout(@ApiIgnore @RequestAttribute("userId") long userId){
        tokenService.expireToken(userId);
        return R.ok();
    }

    /**
     * 测试接口
     */
    @PostMapping("/test")
    public R test(String oofayOrderData, Long userdean, Long timestamp, String asin){
        System.out.println("oofayOrderData:" + oofayOrderData);
        System.out.println("userdean:" + userdean);
        System.out.println("timestamp:" + timestamp);
        System.out.println("asin:" + asin);

        return R.ok();
    }

    /**
     * 测试接口1
     */
    /*@PostMapping("/test1")
    public R test1(){
        List<AmazonGrantShopEntity> a = amazonGrantShopService.selectList(null);
        System.out.println("a:" + a.size());
        return R.ok();
    }*/
}
