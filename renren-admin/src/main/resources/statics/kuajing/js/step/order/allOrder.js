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
            orderAll:12324,
            orderNum:123,
            allPrice:32784523.2346,
            profit:3442763487,
            freight:21356,
            purchase:238175,
            refund:123,
            refundCost:-2378.23
        },
        yichangList:[],
        yichangListValue:'',
    },
    methods:{
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
                            vm.getOrderlist();
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
            console.log(this.startDate);
            console.log(this.endDate);
            this.orderStatus = orderStatus;
            $.ajax({
                url: '../../product/order/getAllList',
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
                },
                dataType: 'json',
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.prolist = r.page.list;
                        vm.allOrderCount = r.page.totalCount;
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
        // 获取各状态订单数量
        getOrderStatenum:function () {
            $.ajax({
                url: '../../product/datadictionary/allOrderStateList',
                type: 'get',
                data: {},
                dataType: 'json',
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.allOrderCount = r.allOrderCount;
                        for (var i=0;i<r.orderStateList.length;i++){
                            if(r.orderStateList[i].dataContent == "待付款"){
                                r.orderStateList[i].color = 'org';
                            }
                            if(r.orderStateList[i].dataContent == "已付款"){
                                r.orderStateList[i].color = 'blue';
                            }
                            if(r.orderStateList[i].dataContent == "虚发货"){
                                r.orderStateList[i].color = 'org';
                            }
                            if(r.orderStateList[i].dataContent == "已采购"){
                                r.orderStateList[i].color = 'blue';
                            }
                            if(r.orderStateList[i].dataContent == "待发货"){
                                r.orderStateList[i].color = 'blue';
                            }
                            if(r.orderStateList[i].dataContent == "待签收"){
                                r.orderStateList[i].color = 'green';
                            }
                            if(r.orderStateList[i].dataContent == "入库"){
                                r.orderStateList[i].color = 'blue';
                            }
                            if(r.orderStateList[i].dataContent == "国际已发货"){
                                r.orderStateList[i].color = 'blue';
                            }
                            if(r.orderStateList[i].dataContent == "取消"){
                                r.orderStateList[i].color = 'red';
                            }
                            if(r.orderStateList[i].dataContent == "缺货"){
                                r.orderStateList[i].color = 'org';
                            }
                            if(r.orderStateList[i].dataContent == "退货"){
                                r.orderStateList[i].color = 'red';
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
                            userId:'',
                            displayName:'-选择-'
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

        }
    },
    created:function () {
        this.getOrderlist('');
        // this.laypage();
        this.getOrderStatenum();
        this.getYichangList();

    }
})