package io.renren.modules.sys.controller;

import io.renren.common.utils.R;
import io.renren.modules.product.service.OrderService;
import io.renren.modules.sys.dto.FranchiseeStatisticsDto;
import io.renren.modules.sys.dto.PlatformStatisticsDto;
import io.renren.modules.sys.dto.StatisticsDto;
import io.renren.modules.sys.dto.UserStatisticsDto;
import io.renren.modules.sys.vm.StatisticsVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: wdh
 * 财务管理
 * 统计
 * @Date: 2018/12/23 00:49
 * @Description:
 */

@RestController
@RequestMapping("/sys/finance")
public class FinanceController extends AbstractController{
    @Autowired
    private OrderService orderService;


    @RequestMapping("/oneLevelStatisticsDefault")
    public R oneLevelStatisticsDefault(){
        //总部员工统计
        StatisticsVM vm = new StatisticsVM();
        vm.setType("day");
        vm.setDeptId("1");
        StatisticsDto dto = new StatisticsDto();
        //总部员工统计数据模型
        UserStatisticsDto userStatisticsDto = orderService.oneLevelUserStatistics(vm);
        dto.setUserStatisticsDto(userStatisticsDto);
        //加盟商统计数据模型
        FranchiseeStatisticsDto franchiseeStatisticsDto = orderService.oneLevelFranchiseeStatistics(vm);
        dto.setFranchiseeStatisticsDto(franchiseeStatisticsDto);
        //平台利润统计
        PlatformStatisticsDto platformStatisticsDto = orderService.platformStatistics(vm);
        dto.setPlatformStatisticsDto(platformStatisticsDto);
        return R.ok().put("dto",dto);
    }
    @RequestMapping("/oneLevelQueryUser")
    public R oneLevelQueryUser(@RequestBody StatisticsVM vm){
        //总部员工统计数据模型
        UserStatisticsDto userStatisticsDto = orderService.oneLevelUserStatistics(vm);
        return R.ok().put("userStatisticsDto",userStatisticsDto);
    }
    @RequestMapping("/oneLevelQueryFranchisee")
    public R oneLevelQueryFranchisee(@RequestBody StatisticsVM vm){
        //加盟商统计数据模型
        FranchiseeStatisticsDto franchiseeStatisticsDto = orderService.oneLevelFranchiseeStatistics(vm);
        return R.ok().put("franchiseeStatisticsDto",franchiseeStatisticsDto);
    }
    @RequestMapping("/oneLevelQueryPlatform")
    public R oneLevelQueryPlatform(@RequestBody StatisticsVM vm){
        //平台利润统计
        PlatformStatisticsDto platformStatisticsDto = orderService.platformStatistics(vm);
        return R.ok().put("platformStatisticsDto",platformStatisticsDto);
    }
}
