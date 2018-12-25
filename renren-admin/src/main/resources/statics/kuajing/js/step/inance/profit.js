$(function () {
    $(function () {
        $('.inner-content-div2').slimScroll({
            height: '400px' //设置显示的高度
        });
    })

    $('.nav_title ul li').click(function () {
        $(this).siblings().removeClass('action');
        $(this).addClass('action');

    })


})


var vm = new Vue({
    el: '#step',
    data: {
        staff:[],
        statistics:{},
        statisticsShop:{},
        statisticsProfit:{},
        allChart1Data:[],
        allChart2Data:[],
        allChart3Data:null,
        shopChart1Data:null,
        shopChart2Data:null,
        shopChart3Data:[],
        yuangonguserid:'',
        jiamenguserid:'',
        value1:'',
        value2:'',
        value3:'',
        value4:'',
        value5:'',
        value6:'',
        zongbuTime:false,
        jiamTime:false,
        pingtTime:false,
    },
    methods:{
        //默认盈利明细
        oneLevelStatisticsDefault:function () {
            $.ajax({
                url: '../../sys/finance/oneLevelStatisticsDefault',
                type: 'get',
                data:  {
                },
                dataType: 'json',
                success: function (r) {
                    if (r.code === 0) {
                        vm.statisticsProfit = r.dto.platformStatisticsDto;
                        vm.statistics = r.dto.userStatisticsDto;
                        vm.statisticsShop = r.dto.franchiseeStatisticsDto;
                        vm.shopChart3Data[0] = vm.statisticsShop.salesVolume;
                        vm.shopChart3Data[1] = vm.statisticsShop.allCost;
                        vm.shopChart3Data[2] = vm.statisticsShop.profit;
                        vm.shopChart2Data = vm.statisticsShop.profitRate;
                        vm.allChart1Data[0] = vm.statistics.addProductsCounts;
                        vm.allChart1Data[1] = vm.statistics.addOrderCounts;
                        vm.allChart1Data[2] = vm.statistics.returnCounts;
                        vm.allChart2Data[0] = vm.statistics.salesVolume;
                        vm.allChart2Data[1] = vm.statistics.cost + vm.statistics.orderFreight;
                        vm.allChart2Data[2] = vm.statistics.profit;
                        vm.allChart3Data = vm.statistics.profitRate;
                        console.log(r);
                        allChart1(vm.allChart1Data,vm.statistics.name);
                        allChart2(vm.allChart2Data,vm.statistics.name);
                        allChart3(vm.allChart3Data);
                        shopChart3(vm.shopChart3Data,vm.statistics.name);
                        shopChart2(vm.shopChart2Data);
                        shopChart1(vm.shopChart1Data);
                    } else {
                        layer.alert(r.msg);
                    }

                },
                error: function () {
                    layer.msg("网络故障");
                }
            });
        },
        //选择员工
        selectOneLevelUserList:function () {
            $.ajax({
                url: '../../sys/user/selectOneLevelUserList',
                type: 'get',
                data:  {
                },
                dataType: 'json',
                success: function (r) {
                    console.log(r)
                    if (r.code === 0) {
                        vm.staff = r.userList;
                    } else {
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            });
        },
        //平台利润查询
        oneLevelQueryPlatform:function (type) {
            if(type == 'time'){
                vm.pingtTime = true;
            }else {
                vm.pingtTime = false;
            }
            $.ajax({
                url: '../../sys/finance/oneLevelQueryPlatform',
                type: 'post',
                data:  JSON.stringify({
                    type:type,
                    startDate:'',
                    endDate:''
                }),
                contentType: "application/json",
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.statisticsProfit = r.platformStatisticsDto;
                    } else {
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            });
        },
        //总部员工查询
        oneLevelQueryUser:function (type) {
            if(type == 'time'){
                vm.zongbuTime = true;
            }else {
                vm.zongbuTime = false;
            }
            $.ajax({
                url: '../../sys/finance/oneLevelQueryUser',
                type: 'post',
                data:  JSON.stringify({
                    type:type,
                    startDate:vm.value1,
                    endDate:vm.value2,
                    userId:vm.yuangonguserid
                }),
                contentType: "application/json",
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        // vm.statisticsProfit = r.platformStatisticsDto;
                        vm.statistics = r.userStatisticsDto;
                        vm.allChart1Data[0] = vm.statistics.addProductsCounts;
                        vm.allChart1Data[1] = vm.statistics.addOrderCounts;
                        vm.allChart1Data[2] = vm.statistics.returnCounts;
                        vm.allChart2Data[0] = vm.statistics.salesVolume;
                        vm.allChart2Data[1] = vm.statistics.cost + vm.statistics.orderFreight;
                        vm.allChart2Data[2] = vm.statistics.profit;
                        vm.allChart3Data = vm.statistics.profitRate;
                        allChart1(vm.allChart1Data,vm.statistics.name);
                        allChart2(vm.allChart2Data,vm.statistics.name);
                        allChart3(vm.allChart3Data);
                    } else {
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            });
        },
        //加盟商查询
        oneLevelQueryFranchisee:function (type) {
            if(type == 'time'){
                vm.jiamTime = true;
            }else {
                vm.jiamTime = false;
            }
            $.ajax({
                url: '../../sys/finance/oneLevelQueryFranchisee',
                type: 'post',
                data:  JSON.stringify({
                    type:type,
                    startDate:vm.value3,
                    endDate:vm.value3,
                    userId:vm.jiamenguserid
                }),
                contentType: "application/json",
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                       // vm.statisticsProfit = r.platformStatisticsDto;
                        vm.statisticsShop = r.franchiseeStatisticsDto;
                        vm.shopChart3Data[0] = vm.statisticsShop.salesVolume;
                        vm.shopChart3Data[1] = vm.statisticsShop.allCost;
                        vm.shopChart3Data[2] = vm.statisticsShop.profit;
                        vm.shopChart2Data = vm.statisticsShop.profitRate;
                        shopChart3(vm.shopChart3Data,vm.statistics.name);
                        shopChart2(vm.shopChart2Data);
                        shopChart1(vm.shopChart1Data);
                    } else {
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            });
        },

    },
    created:function(){
        this.oneLevelStatisticsDefault();
        this.selectOneLevelUserList();
    },
    mounted:function () {
        $('.chart2>div').css('color','#fff');
    },
    updated:function () {
        $('.chart2>div').css('color','#fff');
    }

})