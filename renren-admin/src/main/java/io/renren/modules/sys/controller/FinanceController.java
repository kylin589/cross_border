package io.renren.modules.sys.controller;

import com.baomidou.mybatisplus.plugins.Page;
import io.renren.common.utils.R;
import io.renren.modules.product.service.OrderService;
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
        UserStatisticsDto userStatisticsDto = orderService.oneLevelUserStatistics(vm);
        dto.setUserStatisticsDto(userStatisticsDto);
//        System.out.println("userSt:" + userStatisticsDto.toString());
        return R.ok().put("dto",dto);
    }
    @RequestMapping("/oneLevelStatisticsQuery")
    public R oneLevelStatisticsQuery(@RequestBody StatisticsVM vm){
        String type = vm.getType();
        String startDate = vm.getStartDate();
        String endDate = vm.getEndDate();
        if("year".equals(type)){
            //查询年

        }else if("month".equals(type)){
            //查询月
        }else if("day".equals(type)){
            //查询日
        }else{
            //按时间查询
        }
        return R.ok();
    }
}
