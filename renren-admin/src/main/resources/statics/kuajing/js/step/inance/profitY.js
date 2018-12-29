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

        timetype1:'',
        timetype2:'',
        timetype3:'',
        zongbuTime:false,
        jiamTime:false,
        pingtTime:false,
        // 所有公司
        allGongsi:[],
        // 所选公司的value
        allGongsiValue:''
    },
    methods:{
        // 获取公司列表
        getCouList:function(){
            $.ajax({
                url: '../../sys/dept/select',
                type: 'get',
                data:''
                // 'productIds': JSON.stringify(vm.activeProlist)
                ,
                // contentType: "application/json",
                dataType: 'json',
                success: function (r) {
                    console.log('获取公司');
                    console.log(r);
                    if (r.code === 0) {
                        // layer.msg('操作成功');
                        vm.allGongsi = r.deptList;
                        vm.allGongsi.unshift({
                            deptId:'',
                            name:'全部'
                        })
                        if(vm.allGongsi.length == 2){
                            vm.allGongsiValue = vm.allGongsi[1].deptId;
                        }
                        console.log(r.deptList.deptId);
                        vm.selectOneLevelUserList(r.deptList.deptId);
                        console.log(vm.allGongsi)
                        // vm.getPage();

                    } else {
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            })
        },
        //默认盈利明细
        oneLevelStatisticsDefault:function () {
            $.ajax({
                url: '../../sys/finance/userStatistics',
                type: 'get',
                data:  {
                },
                dataType: 'json',
                success: function (r) {
                    if (r.code === 0) {
                        console.log(r);
                        vm.statistics = r.userStatisticsDto;

                        // vm.allChart1Data[0] = vm.statistics.addProductsCounts;
                        // vm.allChart1Data[1] = vm.statistics.addOrderCounts;
                        // vm.allChart1Data[2] = vm.statistics.returnCounts;
                        vm.allChart2Data[0] = vm.statistics.salesVolume;
                        vm.allChart2Data[1] = vm.statistics.cost + vm.statistics.orderFreight;
                        vm.allChart2Data[2] = vm.statistics.profit;
                        vm.allChart3Data = vm.statistics.profitRate;

                        // allChartY1(vm.allChart1Data);
                        allChartY2(vm.allChart2Data);
                        allChartY3(vm.allChart3Data);

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
        selectOneLevelUserList:function (id) {
            $.ajax({
                url: '../../sys/user/getUserList',
                type: 'get',
                data:  {
                    'deptId':id
                },
                dataType: 'json',
                success: function (r) {
                    console.log(r)
                    if (r.code === 0) {
                        vm.staff = r.userList;
                        vm.staff.unshift({
                            userId:'',
                            displayName:'全部'
                        })
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
            vm.timetype2=type;
            if(type == 'time'){
                vm.zongbuTime = true;
            }else {
                vm.zongbuTime = false;
                $.ajax({
                    url: '../../sys/finance/franchiseeQueryStatistics',
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
                            vm.statistics = r.franchiseeStatisticsDto;
                            // vm.allChart1Data[0] = vm.statistics.addProductsCounts;
                            // vm.allChart1Data[1] = vm.statistics.addOrderCounts;
                            // vm.allChart1Data[2] = vm.statistics.returnCounts;
                            vm.allChart2Data[0] = vm.statistics.salesVolume;
                            vm.allChart2Data[1] = vm.statistics.cost + vm.statistics.orderFreight;
                            vm.allChart2Data[2] = vm.statistics.profit;
                            vm.allChart3Data = vm.statistics.profitRate;
                            // allChartY1(vm.allChart1Data);
                            allChartY2(vm.allChart2Data);
                            allChartY3(vm.allChart3Data);
                        } else {
                            layer.alert(r.msg);
                        }
                    },
                    error: function () {
                        layer.msg("网络故障");
                    }
                });
            }
        },
        //点击查询2
        oneLevelQueryUser1:function(){
            $.ajax({
                url: '../../sys/finance/franchiseeQueryStatistics',
                type: 'post',
                data:  JSON.stringify({
                    type:vm.timetype2,
                    startDate:vm.value1,
                    endDate:vm.value2,
                    userId:vm.yuangonguserid
                }),
                contentType: "application/json",
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        // vm.statisticsProfit = r.platformStatisticsDto;
                        vm.statistics = r.franchiseeStatisticsDto;
                        // vm.allChart1Data[0] = vm.statistics.addProductsCounts;
                        // vm.allChart1Data[1] = vm.statistics.addOrderCounts;
                        // vm.allChart1Data[2] = vm.statistics.returnCounts;
                        vm.allChart2Data[0] = vm.statistics.salesVolume;
                        vm.allChart2Data[1] = vm.statistics.cost + vm.statistics.orderFreight;
                        vm.allChart2Data[2] = vm.statistics.profit;
                        vm.allChart3Data = vm.statistics.profitRate;
                        allChart1(vm.allChart1Data,vm.statistics.name);
                        allChartY2(vm.allChart2Data,vm.statistics.name);
                        allChartY3(vm.allChart3Data);
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
        this.getCouList();
        this.oneLevelStatisticsDefault();


    },
    mounted:function () {
        $('.chart2>div').css('color','#fff');
        $('.statistics>span span').css('color','#00a3e8');
        $('.statistics>span span').css('display','inline-block');
        // $('.statistics>span').css('color','#666');
    },
    updated:function () {
        $('.chart2>div').css('color','#fff');
        $('.statistics>span span').css('color','#00a3e8');
        $('.statistics>span span').css('display','inline-block');
        // $('.statistics>span').css('color','#666');
    }

})