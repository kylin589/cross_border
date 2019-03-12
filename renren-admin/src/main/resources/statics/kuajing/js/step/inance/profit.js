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
                contentType: "application/json",
                // dataType: 'json',
                success: function (r) {
                    // console.log('获取公司');
                    // console.log(r);
                    if (r.code === 0) {
                        // layer.msg('操作成功');
                        vm.allGongsi = r.deptList;
                        vm.allGongsi.unshift({
                            deptId:'',
                            name:'全部'
                        })
                        // console.log(vm.allGongsi)
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
                url: '../../sys/finance/oneLevelStatisticsDefault',
                type: 'get',
                data:  {
                },
                dataType: 'json',
                success: function (r) {
                    console.log('moren');
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
                        allChart1(vm.allChart1Data);
                        allChart2(vm.allChart2Data);
                        allChart3(vm.allChart3Data);
                        shopChart3(vm.shopChart3Data);
                        shopChart2(vm.shopChart2Data);
                        // shopChart1(vm.shopChart1Data);
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
                    // console.log(r)
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
        //平台利润查询
        oneLevelQueryPlatform:function (type) {
            this.timetype1=type;
            if(type == 'time'){
                this.pingtTime = true;
            }else {
                this.pingtTime = false;
                $.ajax({
                    url: '../../sys/finance/oneLevelQueryPlatform',
                    type: 'post',
                    data:  JSON.stringify({
                        type:type,
                        startDate:this.value5,
                        endDate:this.value6
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
            }
        },
        //点击查询1
        oneLevelQueryPlatform1:function(){
            console.log(vm.value5);
            console.log(vm.timetype1);
            $.ajax({
                url: '../../sys/finance/oneLevelQueryPlatform',
                type: 'post',
                data:  JSON.stringify({
                    type:vm.timetype1,
                    startDate:vm.value5,
                    endDate:vm.value6
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
            this.timetype2=type;
            if(type == 'time'){
                this.zongbuTime = true;
            }else {
                this.zongbuTime = false;
                $.ajax({
                    url: '../../sys/finance/oneLevelQueryUser',
                    type: 'post',
                    data:  JSON.stringify({
                        type:type,
                        startDate:this.value1,
                        endDate:this.value2,
                        userId:this.yuangonguserid
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
            }
        },
        //点击查询2
        oneLevelQueryUser1:function(){
            $.ajax({
                url: '../../sys/finance/oneLevelQueryUser',
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
            this.timetype3=type;
            if(type == 'time'){
                this.jiamTime = true;
            }else {
                this.jiamTime = false;
                $.ajax({
                    url: '../../sys/finance/oneLevelQueryFranchisee',
                    type: 'post',
                    data:  JSON.stringify({
                        type:type,
                        startDate:this.value3,
                        endDate:this.value4,
                        deptId:this.allGongsiValue
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
                            // shopChart1(vm.shopChart1Data);
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
        //点击查询3
        oneLevelQueryFranchisee1:function () {
            $.ajax({
                url: '../../sys/finance/oneLevelQueryFranchisee',
                type: 'post',
                data:  JSON.stringify({
                    type:vm.timetype3,
                    startDate:vm.value3,
                    endDate:vm.value4,
                    deptId:vm.allGongsiValue
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
                        // shopChart1(vm.shopChart1Data);
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
    created:function(){
        this.getCouList();
        // this.oneLevelStatisticsDefault();
        this.selectOneLevelUserList();
        this.oneLevelQueryPlatform('day');
        this.oneLevelQueryUser('day');
        this.oneLevelQueryFranchisee('day');

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