window.onload = function (ev) {
    var rows = document.getElementsByTagName('tr');//取得行
    for(var i=0 ;i<rows.length; i++)
    {
        if(i != 0){
            rows[i].onmouseover = function(){//鼠标移上去,添加一个类'hilite'
                this.className += 'hilite';
            }
            rows[i].onmouseout = function(){//鼠标移开,改变该类的名称
                this.className = this.className.replace('hilite','');
            }
        }

    }

    // 添加订单
    $('.addBtn').click(function () {
        console.log(1111);
        vm.addOrder();
    })

    // $('.layui-tab-title li').click(function () {
    //     // console.log(111111);
    //     $('.layui-tab-title li').removeClass('layui-this');
    //     $(this).addClass('layui-this');
    //     $('.layui-tab-content ul li').removeClass('active');
    //     $('.layui-tab-content ul li').eq($(this).index()).addClass('active');
    // })

    // // 鼠标移入图片放大
    // $('table tr td.imgtd img').mouseover(function () {
    //     console.log(111111);
    //     var _src = $(this).attr('src');
    //     console.log(_src);
    //     var _top = $(this).offset().top + 25 - 150;
    //     var _left = $(this).offset().left;
    //     var _width = $(this).width();
    //     var left = _left + _width;
    //     var img = $('<img class="bigImg" >');
    //     img.attr('src',_src);
    //     $('.bigImgDiv').css({
    //         'display':'inline-block',
    //         'top': _top+'px',
    //         'left':left+'px',
    //     });
    //     $('.bigImgDiv').append(img);
    // })
    // $('table tr td.imgtd img').mouseout(function () {
    //     $('img.bigImg').remove();
    //     $('.bigImgDiv').css('display','none');
    // })
}


var vm = new Vue({
    el:'#step',
    data:{
        value9:'',
        //全部订单数量
        allOrderCount:0,
        allOrderCount1:0,
        //各状态订单个数
        orderStateList:[],
        //异常订单
        orderStateListyc:[],
        //订单列表
        prolist:[],
        // 当前页码
        proCurr:1,
        // 每页数量限制
        pageLimit:12,
        //店铺名称
        shopName:'',
        //订单状态
        orderStatus:'',
        //订单id
        orderId:null,
        //亚马逊订单id
        amazonOrderId:null,
        //产品id
        productId:null,
        productSku:'',
        productAsin:'',
        domesticWaybill:'',
        abroadWaybill:'',
        startDate:'',
        endDate:'',
        statistics:{
            // orderAll:12324,
            // orderNum:123,
            // allPrice:32784523.2346,
            // profit:3442763487,
            // freight:21356,
            // purchase:238175,
            // refund:123,
            // refundCost:-2378.23
        },
        yichangList:[],
        yichangListValue:'',
        isok:false,  //判断CheckBox是否选中
        orderIds:[],
        allcheck:[],
        abnormalStatus:'',
        abnormalState:'',
        shopList:[],
        // 所有员工
        allYUanG:[{
            userId:'',
            displayName:'所有员工'
        }],
        // 所选员工的value
        allYUanGValue:'',
        // 所有公司
        allGongsi:[],
        // 所选公司的value
        allGongsiValue:''
    },
    methods:{
        // 获取员工列表
        getManList:function () {
            console.log('11111');


            if(vm.allGongsiValue != ''){
                console.log('@@@@@@');
                console.log(vm.allYUanGValue);
                $.ajax({
                    url: '../../sys/user/getUserList',
                    type: 'get',
                    data:{deptId:vm.allGongsiValue}
                    // 'productIds': JSON.stringify(vm.activeProlist)
                    ,
                    // contentType: "application/json",
                    dataType: 'json',
                    success: function (r) {
                        console.log('获取员工');
                        console.log(r);
                        if (r.code === 0) {
                            vm.allYUanG= r.userList;
                            vm.allYUanG.unshift({
                                userId:'',
                                displayName:'所有员工'
                            })
                            // vm.getPage();

                        } else {
                            layer.alert(r.msg);
                        }
                    },
                    error: function () {
                        layer.msg("网络故障");
                    }
                })
            }else {
                $.ajax({
                    url: '../../sys/user/getUserList',
                    type: 'get',
                    data:{deptId:'0'}
                    // 'productIds': JSON.stringify(vm.activeProlist)
                    ,
                    // contentType: "application/json",
                    dataType: 'json',
                    success: function (r) {
                        console.log('获取员工');
                        console.log(r);
                        if (r.code === 0) {
                            vm.allYUanG= r.userList;
                            vm.allYUanG.unshift({
                                userId:'',
                                username:'所有员工'
                            })
                            // vm.getPage();

                        } else {
                            layer.alert(r.msg);
                        }
                    },
                    error: function () {
                        layer.msg("网络故障");
                    }
                })
            }



        },
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
                    console.log('获取公司');
                    console.log(r);
                    if (r.code === 0) {
                        // layer.msg('操作成功');
                        vm.allGongsi = r.deptList;
                        vm.allGongsi.unshift({
                            deptId:'',
                            name:'全部'
                        })
                        console.log(vm.allGongsi);
                        if(vm.allGongsi.length == 2){
                            vm.allGongsiValue = vm.allGongsi[1].deptId;
                        }
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
        // 选择公司后获取员工
        chanGongsiFunc:function () {
            vm.allYUanG = [{
                userId:'',
                displayName:'所有员工'
            }];
            vm.allYUanGValue = '';
        },
        // addOrder:function () {
        //     layer.open({
        //         type: 1,
        //         title: false,
        //         content: $('#addOrder'), //这里content是一个普通的String
        //         skin: 'openClass',
        //         area: ['400px', '220px'],
        //         shadeClose: true,
        //         btn: ['添加','取消'],
        //         btn1: function (index) {
        //
        //
        //         },
        //         btn2: function (index) {
        //
        //
        //         }
        //     });
        // },
        // 分页器
        laypage: function () {
            // var tempTotalCount;

            // console.log(vm.allOrderCount);

            // 分页器
            layui.use('laypage', function () {
                var laypage = layui.laypage;
                //执行一个laypage实例
                laypage.render({
                    elem: 'page', //注意，这里的 test1 是 ID，不用加 # 号
                    count: vm.allOrderCount, //数据总数，从服务端得到
                    prev: '<i class="layui-icon layui-icon-left"></i>',
                    next: '<i class="layui-icon layui-icon-right"></i>',
                    limits: [12, 24, 30],
                    curr:vm.proCurr,
                    limit: 12,
                    layout: ['prev', 'page', 'next', 'limit', 'skip'],
                    jump: function (obj, first) {
                        //obj包含了当前分页的所有参数，比如：
                        // console.log(obj.curr); //得到当前页，以便向服务端请求对应页的数据。
                        // console.log(obj.limit); //得到每页显示的条数
                        vm.pageLimit = obj.limit;
                        vm.proCurr = obj.curr;
                        $('.pro_list .item').removeClass('action');
                        //首次不执行
                        if (!first) {
                            //do something
                            vm.getOrderlist1();
                        }
                    }
                });
            });
        },
        // 获取订单列表
        getOrderlist:function (orderStatus) {
            console.log(orderStatus);
            this.startDate = this.value9[0];
            this.endDate = this.value9[1];
            console.log(this.shopName);
            console.log(this.endDate);
            this.orderStatus = orderStatus;
            this.proCurr = 1;
            $.ajax({
                url: '../../amazon/neworder/depotOrderList',
                type: 'post',
                data: {
                    'page': this.proCurr,
                    'limit': this.pageLimit,
                    'shopName': this.shopName,
                    'orderStatus':this.orderStatus,
                    'orderId':this.orderId,
                    'amazonOrderId':this.amazonOrderId,
                    //产品id
                    'productId':this.productId,
                    'productSku':this.productSku,
                    'productAsin':this.productAsin,
                    'domesticWaybill':this.domesticWaybill,
                    'abroadWaybill':this.abroadWaybill,
                    'startDate':this.startDate,
                    'endDate':this.endDate,
                    'deptId':this.allGongsiValue,
                    'userId':this.allYUanGValue,
                    'abnormalStatus':this.orderStatus1
                },
                dataType: 'json',
                success: function (r) {
                    console.log('订单列表')
                    console.log(r);
                    if (r.code === 0) {
                        vm.prolist = r.page.list;
                        vm.allOrderCount = r.page.totalCount;
                        vm.statistics = r.orderCounts;
                        console.log(vm.allOrderCount)
                        vm.laypage();
                    } else {
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            });
        },
        getOrderlist2:function (orderStatus,index) {


            $('.zhuangtaiUl li').removeClass('action');
            $('.zhuangtaiUl li').eq(index).addClass('action');

            // console.log(orderStatus);
            this.startDate = this.value9[0];
            this.endDate = this.value9[1];
            // console.log(this.shopName);
            // console.log(this.endDate);
            this.orderStatus = orderStatus;

            $.ajax({
                url: '../../amazon/neworder/depotOrderList',
                type: 'post',
                data: {
                    'page': this.proCurr,
                    'limit': this.pageLimit,
                    'shopName': this.shopName,
                    'orderStatus':this.orderStatus,
                    'orderId':this.orderId,
                    'amazonOrderId':this.amazonOrderId,
                    //产品id
                    'productId':this.productId,
                    'productSku':this.productSku,
                    'productAsin':this.productAsin,
                    'domesticWaybill':this.domesticWaybill,
                    'abroadWaybill':this.abroadWaybill,
                    'startDate':this.startDate,
                    'endDate':this.endDate,
                    'deptId':this.allGongsiValue,
                    'userId':this.allYUanGValue,
                    'abnormalStatus':this.orderStatus1
                },
                dataType: 'json',
                success: function (r) {
                    console.log('订单列表')
                    console.log(r);
                    if (r.code === 0) {
                        vm.prolist = r.page.list;
                        vm.allOrderCount = r.page.totalCount;
                        vm.statistics = r.orderCounts;
                        console.log(vm.allOrderCount)
                        vm.laypage();
                    } else {
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            });
        },
        getOrderlist3:function (orderStatus,index) {


            $('.yichangUl li').removeClass('action');
            $('.yichangUl li').eq(index).addClass('action');

            // console.log(orderStatus);
            this.startDate = this.value9[0];
            this.endDate = this.value9[1];
            // console.log(this.shopName);
            // console.log(this.endDate);
            this.orderStatus1 = orderStatus;
            $.ajax({
                url: '../../amazon/neworder/depotOrderList',
                type: 'post',
                data: {
                    'page': this.proCurr,
                    'limit': this.pageLimit,
                    'shopName': this.shopName,
                    'orderStatus':this.orderStatus,
                    'orderId':this.orderId,
                    'amazonOrderId':this.amazonOrderId,
                    //产品id
                    'productId':this.productId,
                    'productSku':this.productSku,
                    'productAsin':this.productAsin,
                    'domesticWaybill':this.domesticWaybill,
                    'abroadWaybill':this.abroadWaybill,
                    'startDate':this.startDate,
                    'endDate':this.endDate,
                    'deptId':this.allGongsiValue,
                    'userId':this.allYUanGValue,
                    'abnormalStatus':this.orderStatus1
                },
                dataType: 'json',
                success: function (r) {
                    console.log('订单列表')
                    console.log(r);
                    if (r.code === 0) {
                        vm.prolist = r.page.list;
                        vm.allOrderCount = r.page.totalCount;
                        vm.statistics = r.orderCounts;
                        console.log(vm.allOrderCount)
                        vm.laypage();
                    } else {
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            });

        },
        getOrderlist1:function () {
            console.log('订单状态');
            console.log(this.orderStatus);
            console.log(this.orderStatus1);
            this.startDate = this.value9[0];
            this.endDate = this.value9[1];
            // console.log(this.shopName);
            // console.log(this.endDate);
            // this.orderStatus = orderStatus;
            $.ajax({
                url: '../../amazon/neworder/depotOrderList',
                type: 'post',
                data: {
                    'page': this.proCurr,
                    'limit': this.pageLimit,
                    'shopName': this.shopName,
                    'orderStatus':this.orderStatus,
                    'orderId':this.orderId,
                    'amazonOrderId':this.amazonOrderId,
                    //产品id
                    'productId':this.productId,
                    'productSku':this.productSku,
                    'productAsin':this.productAsin,
                    'domesticWaybill':this.domesticWaybill,
                    'abroadWaybill':this.abroadWaybill,
                    'startDate':this.startDate,
                    'endDate':this.endDate,
                    'deptId':this.allGongsiValue,
                    'userId':this.allYUanGValue,
                    'abnormalStatus':this.orderStatus1
                },
                dataType: 'json',
                success: function (r) {
                    console.log('订单列表')
                    console.log(r);
                    if (r.code === 0) {
                        vm.prolist = r.page.list;
                        vm.allOrderCount = r.page.totalCount;
                        console.log(vm.allOrderCount)
                        // vm.laypage();
                    } else {
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            });
        },
        // 获取各状态订单数量
        getOrderStatenum:function () {
            $.ajax({
                url: '../../product/datadictionary/allCKNewOrderStateList',
                type: 'get',
                data: {},
                dataType: 'json',
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.allOrderCount1 = r.allOrderCount;
                        vm.orderStateListyc = [];
                        for (var i=0;i<r.orderStateList.length;i++){
                            if(r.orderStateList[i].dataContent == "待付款"){
                                r.orderStateList[i].color = 'org1';
                            }
                            if(r.orderStateList[i].dataContent == "已付款"){
                                r.orderStateList[i].color = 'blue1';
                            }
                            if(r.orderStateList[i].dataContent == "虚发货"){
                                r.orderStateList[i].color = 'org';
                            }
                            if(r.orderStateList[i].dataContent == "国内物流已采购"){
                                r.orderStateList[i].color = 'blue2';
                            }
                            if(r.orderStateList[i].dataContent == "国内物流待发货"){
                                r.orderStateList[i].color = 'blue3';
                            }
                            if(r.orderStateList[i].dataContent == "物流仓库未签收"){
                                r.orderStateList[i].color = 'green';
                            }
                            if(r.orderStateList[i].dataContent == "仓库已入库"){
                                r.orderStateList[i].color = 'blue4';
                            }
                            if(r.orderStateList[i].dataContent == "国际已发货"){
                                r.orderStateList[i].color = 'blue';
                            }
                            if(r.orderStateList[i].dataContent == "取消"){
                                r.orderStateList[i].color = 'red';
                            }
                            if(r.orderStateList[i].dataContent == "缺货"){
                                r.orderStateList[i].color = 'org1';
                            }
                            if(r.orderStateList[i].dataContent == "退货"){
                                r.orderStateList[i].color = 'red1';
                            }
                            if(r.orderStateList[i].dataContent == "补发"){
                                r.orderStateList[i].color = 'green';
                            }
                            if(r.orderStateList[i].dataContent == "问题"){
                                r.orderStateList[i].color = 'red';
                            }

                        }
                        for (var i=0;i<r.orderStateList.length;i++){
                            if(r.orderStateList[i].dataType == "ORDER_ABNORMAL_STATE"){
                                vm.orderStateListyc.push(r.orderStateList[i]);
                            }else {
                                vm.orderStateList.push(r.orderStateList[i]);
                            }
                        }
                        console.log('状态列表');
                        console.log(vm.orderStateList);
                    } else {
                        layer.alert(r.message);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            });
        },
        // 鼠标移入图片放大
        imgMouover:function (event) {
            console.log(111111);
            var _src = $(event.target).attr('src');
            console.log(_src);
            var _top = $(event.target).offset().top + 25 - 150;
            var _left = $(event.target).offset().left;
            var _width = $(event.target).width();
            var left = _left + _width;
            var img = $('<img class="bigImg" >');
            img.attr('src',_src);
            $('.bigImgDiv').css({
                'display':'inline-block',
                'top': _top+'px',
                'left':left+'px',
            });
            $('.bigImgDiv').append(img);
        },
        imgMouout:function () {
            $('img.bigImg').remove();
            $('.bigImgDiv').css('display','none');
        },
        // 获取异常状态列表
        getYichangList:function () {
            $.ajax({
                url: '../../product/datadictionary/getAbnormalStateList',
                type: 'post',
                data: '',
                dataType: 'json',
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.yichangList = r.abnormalStateList;
                        vm.yichangList.unshift({
                            dataNumber:'',
                            dataContent:'-选择-'
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
        allSel:function () {

        },
        demo:function(){   //全选/反选
            console.log(this.isok);
            this.orderIds=[];
            this.allcheck=[];
            if(this.isok){ //全选中 所有CheckBox
                this.prolist.forEach(function (fruit) {
                    this.orderIds.push(fruit.orderId);
                    this.allcheck.push(true)
                }, this);
            }else{   //反选清楚所有CheckBox选中
                this.orderIds=[];
                this.allcheck=[];
            }
            console.log(this.orderIds);
            console.log(this.allcheck)
        },
        demo2:function(orderId){
            this.orderIds.push(orderId);
            if(this.prolist.length==this.orderIds.length){ //判断每一个CheckBox是否选中   全选中让全选反选按钮选中
                this.isok=true;
            }else{  // 不选中 让全选反选按钮不选中
                this.isok=false;
            }
            console.log(this.orderIds)
        },
        //标记为异常状态
        updateAbnormalState:function () {
            console.log(vm.yichangListValue);
            for (var i =0;i<vm.yichangList.length;i++){
                if (vm.yichangListValue == vm.yichangList[i].dataNumber) {
                    vm.abnormalStatus = vm.yichangList[i].dataNumber;
                    vm.abnormalState = vm.yichangList[i].dataContent;
                }
            }
            console.log(vm.orderIds)
            console.log(vm.abnormalStatus)
            console.log(vm.abnormalState)
            $.ajax({
                url: '../../product/order/updateAbnormalState',
                type: 'post',
                data:  JSON.stringify({
                    orderIds:vm.orderIds,
                    abnormalStatus:vm.abnormalStatus,
                    abnormalState:vm.abnormalState
                }),
                contentType: "application/json",
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.isok=false;
                        vm.orderIds=[];
                        vm.allcheck=[];
                        vm.getOrderlist();
                        layer.alert('修改成功');
                    } else {
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            });
        },
        //选择店铺
        allShopList:function () {
            $.ajax({
                url: '../../amazon/amazongrantshop/allShopList',
                type: 'get',
                data: {
                },
                dataType: 'json',
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.shopList = r.shopList;
                        vm.shopList.unshift({
                            // userId:'1-1',
                            shopName:''
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
        // 刷新
        shuaxin:function (id) {
            var index = layer.load();
            var index = layer.load(1); //换了种风格
            var index = layer.load(2, {time: 10*1000}); //又换了种风格，并且设定最长等待10秒
            $.ajax({
                url: '../../product/order/manualUpdateOrder',
                type: 'post',
                data: JSON.stringify({
                    'orderId':id
                }),
                // dataType: 'json',
                contentType: "application/json",
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        layer.msg('刷新成功');
                        layer.close(index);
                    } else {
                        layer.alert(r.msg);
                        layer.close(index);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                    layer.close(index);
                }
            });
        },
        // 入库
        ruku:function (id) {
            var index = layer.load();
            var index = layer.load(1); //换了种风格
            var index = layer.load(2, {time: 10*1000}); //又换了种风格，并且设定最长等待10秒
            $.ajax({
                url: '../../amazon/neworder/listruku',
                type: 'get',
                data: {
                    orderId:id
                },
                dataType: 'json',
                // contentType: "application/json",
                success: function (r) {
                    // console.log('海关编码');
                    // console.log(r);
                    if (r.code == '0') {

                        layer.msg('操作成功');
                        layer.close(index);
                        vm.getOrderlist('Warehousing');
                        // this.laypage();
                        vm.getOrderStatenum();
                        // vm.getOrderInfo();
                        // vm.getWlDetails
                    } else {
                        layer.alert(r.msg);
                        layer.close(index);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                    layer.close(index);
                }
            });
        },
        // 出库
        chuku:function (id) {
            var index = layer.load();
            var index = layer.load(1); //换了种风格
            var index = layer.load(2, {time: 10*1000}); //又换了种风格，并且设定最长等待10秒
            $.ajax({
                url: '../../amazon/neworder/listchuku',
                type: 'get',
                data: {
                    orderId:id
                },
                dataType: 'json',
                // contentType: "application/json",
                success: function (r) {
                    // console.log('海关编码');
                    // console.log(r);
                    if (r.code == '0') {

                        layer.msg('操作成功');
                        layer.close(index);
                        vm.getOrderlist('InShipped');
                        // this.laypage();
                        vm.getOrderStatenum();
                        // vm.getOrderInfo();
                        // vm.getWlDetails
                    } else {
                        layer.alert(r.msg);
                        layer.close(index);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                    layer.close(index);
                }
            });
        },

    },
    created:function () {
        var url = decodeURI(window.location.href);
        var argsIndex = url.split("?page=");
        if(argsIndex[1]){
            this.proCurr = argsIndex[1];
            console.log(argsIndex[1]);
        }


        this.getOrderlist('Purchased');
        // this.laypage();
        this.getOrderStatenum();
        // this.getYichangList();
        // this.allShopList();
        // this.getCouList();

    }
})