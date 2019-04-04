window.onload = function (ev) {

    $('.inner-content-div2').slimScroll({
        height: '400px' //设置显示的高度
    });

    $('.okEdit input[type=text]').attr('disabled','true');
    $('.okEdit textarea').attr('disabled','true');
    // $('input.edit').click(function () {
    //     console.log($(this).val());
    //     if($(this).val() == '编辑'){
    //         $(this).val('保存');
    //         $(this).parent().parent().find('input').removeAttr("disabled");
    //         $(this).parent().parent().find('textarea').removeAttr("disabled");
    //         $(this).parent().parent().find('input[type=text]').css('border','1px solid #d8dce5');
    //         $(this).parent().parent().find('input.noedit').css('display','inline-block');
    //         if($(this).parent().parent().find('.logistics').length!=0){
    //             $(this).parent().parent().find('.logistics').attr('data-ok','false');
    //         }
    //     }else {
    //         $(this).val('编辑');
    //         $(this).parent().parent().find('input[type=text]').attr('disabled','true');
    //         $(this).parent().parent().find('textarea').attr('disabled','true');
    //         $(this).parent().parent().find('input[type=text]').css('border','1px solid transparent');
    //         $(this).parent().parent().find('input.noedit').css('display','none');
    //         if($(this).parent().parent().find('.logistics').length!=0){
    //             $(this).parent().parent().find('.logistics').attr('data-ok','true');
    //         }
    //     }
    //
    // })
    // $('input.noedit').click(function () {
    //     $(this).prev().val('编辑');
    //     $(this).parent().parent().find('input[type=text]').attr('disabled','true');
    //     $(this).parent().parent().find('textarea').attr('disabled','true');
    //     $(this).parent().parent().find('input[type=text]').css('border','1px solid transparent');
    //     $(this).parent().parent().find('input.noedit').css('display','none');
    //     if($(this).parent().parent().find('.logistics').length!=0){
    //         $(this).parent().parent().find('.logistics').attr('data-ok','true');
    //     }
    // })

    // 选项卡
    console.log($('.layui-tab-title li'));
    $('.layui-tab-title li').click(function () {
        console.log($('.layui-tab-title li'));
        var _index = $(this).index();
        $('.layui-tab-title li').removeClass('layui-this');
        $(this).addClass('layui-this');
        $('.layui-tab-content').removeClass('action');
        $('.layui-tab-content').eq(_index).addClass('action');
    })

    // $('a.logistics').mouseover(function () {
    //     console.log(1111);
    //     if($(this).attr('data-ok') == 'true'){
    //         var _val = $(this).val();
    //         var _top = $(this).offset().top;
    //         var _left = $(this).offset().left - 130;
    //         var _height = $(this).height();
    //         var top = _top + _height-8;
    //         $('.logisticsDiv').css({
    //             'display':'inline-block',
    //             'top':top+'px',
    //             'left':_left+'px'
    //         })
    //     }
    //
    // })
    //
    // $('a.logistics').mouseout(function () {
    //     console.log(444)
    //     if($(this).attr('data-ok') == 'true'){
    //         $('.logisticsDiv').css({
    //             'display':'none',
    //         })
    //     }
    //
    // })

    // // 添加国际运单
    // $('.addguojiyundan').click(function () {
    //     vm.addorder()
    // })
    // // 国际运单明细
    // $('.detailsOrderA').click(function () {
    //     vm.detailsorder();
    // })


}


var vm = new Vue({
    el:'#step',
    data:{
        wuliuType:null,
        value8:null,
        wuliuDetails:{
            chineseName:'',
            englishName:'',
            weight:null,
            length:1,
            width:1,
            height:1,
            addyundanhao:'',
            addzhuizonghao:'',
        },
        getWlDetails:{},
        orderid:null,
        page:null,
        orderDetails:{
            shipAddress:{},
            abroadLogistics:{},
            domesticLogisticsList:[],
            logList:[],
            remarkList:[],
            orderProductList:[]
        },
        waybill:[],
        domesticLogisticsId:null,
        price:[],
        guojilogistics:[

        ],
        intLog:{
            num:123123,
            num1:123214,
            circuit:'10 nicolson drive ',
            date:'2018-10-26'
        },
        remark:[],
        operationLog:[],
        logistics:[],
        remark:'',
        remarkType:'',
        caigou:'',
        wulDanh:'',
        wuliuGongsi:'',
        isLookWuliu:true,
        quxiaoJijian:null,
        islogistics:false,
        logisticsNum:'',
        orderProductList:[],
        itemCodelist:[],
        itemCodeId:null,
    },
    methods:{
        addorder:function () {

            layer.open({
                type: 1,
                title: false,
                content: $('#addorder'), //这里content是一个普通的String
                skin: 'openClass',
                area: ['900px', '500px'],
                shadeClose: true,
                btn: ['添加','取消'],
                btn1: function (index) {


                },
                btn2: function (index) {


                }
            });

        },
        detailsorder:function () {
            layer.open({
                type: 1,
                title: false,
                content: $('#detailsorder'), //这里content是一个普通的String
                skin: 'openClass',
                area: ['900px', '500px'],
                shadeClose: true,
                btn: ['保存备注','取消'],
                btn1: function (index) {


                },
                btn2: function (index) {


                }
            });
        },
        //获取订单详情
        getOrderInfo:function () {
            console.log(this.orderid);
            $.ajax({
                url: '../../amazon/neworder/getOrderInfo',
                type: 'get',
                data: {
                    orderId:this.orderid
                },
                dataType: 'json',
                success: function (r) {
                    console.log('订单详情');
                    console.log(r);
                    if (r.code === 0) {
                        vm.orderDetails = r.orderDTO;
                        vm.price = [];
                        // vm.waybill = [];
                        // for (var i=0;i<vm.orderDetails.domesticLogisticsList.length;i++){
                        //     vm.waybill.push(vm.orderDetails.domesticLogisticsList[i].waybill);
                        //     vm.price.push(vm.orderDetails.domesticLogisticsList[i].price);
                        //
                        // }
                        // vm.quxiaoJijian = r.orderDTO.shipAddress;
                        // console.log(vm.quxiaoJijian);
                        vm.orderProductList = [];
                        for (var i = 0;i<vm.orderDetails.orderProductList.length;i++){
                            var numberList = [];
                            for (var j = 0;j<=vm.orderDetails.orderProductList[i].orderItemNumber;j++){
                                numberList.push(j);
                            }
                            vm.orderProductList.push({
                                productImageUrl:vm.orderDetails.orderProductList[i].productImageUrl,
                                productTitle:vm.orderDetails.orderProductList[i].productTitle,
                                productSku:vm.orderDetails.orderProductList[i].productSku,
                                itemQuantity:vm.orderDetails.orderProductList[i].orderItemNumber,
                                productPrice:vm.orderDetails.orderProductList[i].productPrice,
                                numberList:numberList,
                                itemId:vm.orderDetails.orderProductList[i].itemId,
                                orderItemNumber:vm.orderDetails.orderProductList[i].orderItemNumber,
                                itemCodeId:null,

                            })
                            console.log(numberList);
                        }

                    } else {
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            });
        },
        //编辑修改寄件信息
        editJijian:function (event) {
            // vm.quxiaoJijian = vm.orderDetails.shipAddress;
            if($(event.target).val() == '编辑'){
                $(event.target).val('保存');
                $(event.target).parent().parent().find('input').removeAttr("disabled");
                $(event.target).parent().parent().find('textarea').removeAttr("disabled");
                $(event.target).parent().parent().find('input[type=text]').css('border','1px solid #d8dce5');
                $(event.target).parent().parent().find('input.noedit').css('display','inline-block');
                if($(event.target).parent().parent().find('.logistics').length!=0){
                    $(event.target).parent().parent().find('.logistics').attr('data-ok','false');
                }
            }else {
                console.log(vm.orderDetails.shipAddress);

                $.ajax({
                    url: '../../order/productshipaddress/update',
                    type: 'post',
                    data: JSON.stringify(vm.orderDetails.shipAddress),
                    contentType: "application/json",
                    // dataType: 'json',
                    success: function (r) {
                        console.log(r);
                        if (r.code === 0) {
                            $(event.target).val('编辑');
                            $(event.target).parent().parent().find('input[type=text]').attr('disabled','true');
                            $(event.target).parent().parent().find('textarea').attr('disabled','true');
                            $(event.target).parent().parent().find('input[type=text]').css('border','1px solid transparent');
                            $(event.target).parent().parent().find('input.noedit').css('display','none');
                            if($(event.target).parent().parent().find('.logistics').length!=0){
                                $(event.target).parent().parent().find('.logistics').attr('data-ok','true');
                            }

                            layer.msg('修改成功');



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
        //编辑修改国内物流
        edit:function (d,event) {
            // console.log(index);
            if($(event.target).val() == '编辑'){
                vm.isLookWuliu = false;
                $(event.target).val('保存');
                $(event.target).parent().parent().find('input').removeAttr("disabled");
                $(event.target).parent().parent().find('textarea').removeAttr("disabled");
                $(event.target).parent().parent().find('input[type=text]').css('border','1px solid #d8dce5');
                $(event.target).parent().parent().find('input.noedit').css('display','inline-block');
                if($(event.target).parent().parent().find('.logistics').length!=0){
                    $(event.target).parent().parent().find('.logistics').attr('data-ok','false');
                }
            }else {
                $(event.target).val('编辑');
                vm.isLookWuliu = true;
                $(event.target).parent().parent().find('input[type=text]').attr('disabled','true');
                $(event.target).parent().parent().find('textarea').attr('disabled','true');
                $(event.target).parent().parent().find('input[type=text]').css('border','1px solid transparent');
                $(event.target).parent().parent().find('input.noedit').css('display','none');
                if($(event.target).parent().parent().find('.logistics').length!=0){
                    $(event.target).parent().parent().find('.logistics').attr('data-ok','true');
                }
                ;
                $.ajax({
                    // url: 'http://39.106.131.222:8000/domestic/updateLogistics',
                    url: '../../amazon/neworderdomesticlogistics/updateLogistics',
                    type: 'post',
                    data: JSON.stringify({
                        orderId:vm.orderid,
                        waybill:$.trim(d.waybill),
                        domesticLogisticsId:d.domesticLogisticsId,
                        price:d.price,
                        logisticsCompany:d.logisticsCompany
                    }),
                    // dataType: 'json',
                    contentType: "application/json",
                    success: function (r) {
                        console.log(r);
                        if (r.code === 0) {
                            layer.msg('修改成功');
                            vm.getOrderInfo();
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
        //取消修改
        noedit:function (d,event) {
            // d = vm.orderDetails.domesticLogisticsList[index].waybill;
            // console.log(d);
            // $(event.target).parent().parent().find('.logistics').find('input').val(d);
            vm.getOrderInfo();
            $(event.target).prev().val('编辑');
            $(event.target).parent().parent().find('input[type=text]').attr('disabled','true');
            $(event.target).parent().parent().find('textarea').attr('disabled','true');
            $(event.target).parent().parent().find('input[type=text]').css('border','1px solid transparent');
            $(event.target).parent().parent().find('input.noedit').css('display','none');
            if($(event.target).parent().parent().find('.logistics').length!=0){
                $(event.target).parent().parent().find('.logistics').attr('data-ok','true');
            }
            vm.isLookWuliu = true;
        },
        noedit1:function (event) {
            // d = vm.orderDetails.domesticLogisticsList[index].waybill;
            // console.log(d);
            // $(event.target).parent().parent().find('.logistics').find('input').val(d);
            // vm.orderDetails.shipAddress = vm.quxiaoJijian;
            // console.log(vm.quxiaoJijian);
            vm.getOrderInfo();
            // $(event.target).parent().parent().find('input[type=text]').eq(0).val(vm.orderDetails.shipAddress.shipName);
            // $(event.target).parent().parent().find('input[type=text]').eq(1).val(vm.orderDetails.shipAddress.shipTel);
            // $(event.target).parent().parent().find('input[type=text]').eq(2).val(vm.orderDetails.shipAddress.shipCountry);
            // $(event.target).parent().parent().find('input[type=text]').eq(3).val(vm.orderDetails.shipAddress.shipZip);
            // $(event.target).parent().parent().find('input[type=text]').eq(4).val(vm.orderDetails.shipAddress.shipRegion);
            // $(event.target).parent().parent().find('input[type=text]').eq(5).val(vm.orderDetails.shipAddress.shipCity);
            // $(event.target).parent().parent().find('input[type=text]').eq(6).val(vm.orderDetails.shipAddress.shipAddressLine1);
            $(event.target).prev().val('编辑');
            $(event.target).parent().parent().find('input[type=text]').attr('disabled','true');
            $(event.target).parent().parent().find('textarea').attr('disabled','true');
            $(event.target).parent().parent().find('input[type=text]').css('border','1px solid transparent');
            $(event.target).parent().parent().find('input.noedit').css('display','none');
            if($(event.target).parent().parent().find('.logistics').length!=0){
                $(event.target).parent().parent().find('.logistics').attr('data-ok','true');
            }
        },
        // 同步国内物流单号
        synchronize:function (item) {
            console.log(item);
            var index = layer.load();
            var index = layer.load(1); //换了种风格
            var index = layer.load(2, {time: 10*1000}); //又换了种风格，并且设定最长等待10秒
            $.ajax({
                // url: 'http://39.106.131.222:8000/domestic/updateLogistics',
                url: '../../product/order/synchronizeWaybill',
                type: 'get',
                data: {
                    orderId:vm.orderid,

                },
                dataType: 'json',
                success: function (r) {
                    console.log(r);
                    vm.getOrderInfo();
                    layer.close(index);
                },
                error: function () {
                    layer.msg("同步失败");
                    layer.close(index);
                }
            });
        },
        //物流信息
        queryLogistic:function (waybill,event) {
            console.log()

            if(vm.isLookWuliu == true && JSON.stringify(waybill) != 'null'){
                console.log(waybill);
                vm.logisticsNum = waybill;


                layer.open({
                    type: 1,
                    title: '物流信息',
                    content: $('#logisticsDiv'), //这里content是一个普通的String
                    skin: 'openClass',
                    area: ['300px', '420px'],
                    shadeClose: true,
                    btn: [],

                });
                var index = layer.load();
                var index = layer.load(1); //换了种风格
                var index = layer.load(2, {time: 10*1000}); //又换了种风格，并且设定最长等待10秒

                $.ajax({
                    // url: 'http://39.106.131.222:8000/domestic/queryLogistic',
                    url: 'http://www.threeee.cn/domestic/queryLogistic',
                    // url: 'http://127.0.0.1:8000/domestic/queryLogistic',
                    type: 'get',
                    data: {
                        waybill:$.trim(waybill),
                    },
                    dataType: 'json',
                    success: function (r) {
                        console.log(r);
                        if (r.code === 0) {
                            vm.logistics = r.data;
                            layer.close(index);
                            if(vm.logistics.length == 0){
                                vm.islogistics = true;
                            }else {
                                vm.islogistics = false;
                            }

                        } else {
                            layer.alert(r.msg);
                            layer.close(index);
                        }
                    },
                    error: function () {
                        // layer.msg("网络故障");
                        // layer.close(index);
                    }
                });
                // var _val = $(event.target).val();
                // var _top = $(event.target).offset().top;
                // var _left = $(event.target).offset().left - 130;
                // var _height = $(event.target).height();
                // var top = _top + _height-8;
                // $('.logisticsDiv').css({
                //     'display':'inline-block',
                //     'top':top+'px',
                //     'left':_left+'px'
                // })
            }

            // if($(event.target).attr('data-ok') == 'true'){
            //     console.log(111)
            //     var _val = $(event.target).val();
            //     var _top = $(event.target).offset().top;
            //     var _left = $(event.target).offset().left - 130;
            //     var _height = $(event.target).height();
            //     var top = _top + _height-8;
            //     $('.logisticsDiv').css({
            //         'display':'inline-block',
            //         'top':top+'px',
            //         'left':_left+'px'
            //     })
            // }
        },
        // 取消单击事件
        closeClick:function () {

        },
        // 鼠标一处物流消失
        xiaoshiFcun:function (event) {
            $('.logisticsDiv').css({
                'display':'none',
            })
            // if($(event.target).attr('data-ok') == 'true'){
            //
            // }
        },
        // 添加国内物流
        addWuliuFunc:function (id) {
            if(vm.orderDetails.orderState == '虚发货' || vm.orderDetails.orderState == '国内物流已采购' || vm.orderDetails.orderState == '国内物流已发货'){

                layer.open({
                    type: 1,
                    title: false,
                    content: $('#addWul'), //这里content是一个普通的String
                    skin: 'openClass',
                    area: ['400px', '280px'],
                    shadeClose: true,
                    btn: ['添加','取消'],
                    btn1: function (index1) {
                        var index = layer.load();
                        var index = layer.load(1); //换了种风格
                        var index = layer.load(2, {time: 10*1000}); //又换了种风格，并且设定最长等待10秒
                        $.ajax({
                            // url: 'http://39.106.131.222:8000/domestic/getLogisticsCompany',
                            url: '../../amazon/neworderdomesticlogistics/addLogistics',
                            type: 'post',
                            data: JSON.stringify({
                                orderId:vm.orderid,
                                price:parseInt(vm.caigou),
                                waybill:$.trim(vm.wulDanh),
                                itemId:id,
                                logisticsCompany:vm.wuliuGongsi
                            }),
                            // dataType: 'json',
                            contentType: "application/json",
                            success: function (r) {
                                console.log(r);
                                if (r.code === 0) {
                                    layer.msg("添加成功");
                                    layer.close(index);
                                    layer.close(index1);
                                    vm.getOrderInfo();
                                } else {
                                    layer.alert(r.msg);
                                    layer.close(index);

                                }
                            },
                            error: function () {
                                layer.msg("网络故障");
                                layer.close(index);
                                // layer.close(index1);
                            }
                        });
                    },
                    btn2: function (index1) {


                    }
                });
            }else {
                layer.msg('订单还未同步，请同步后再添加物流信息')
            }

        },
        //修改订单状态
        updateState:function (orderState) {
            console.log(11111);
            var index = layer.load();
            var index = layer.load(1); //换了种风格
            var index = layer.load(2, {time: 10*1000}); //又换了种风格，并且设定最长等待10秒
            $.ajax({
                url: '../../product/order/updateState',
                type: 'post',
                data: JSON.stringify({
                    orderId:this.orderid,
                    orderState:orderState
                }),
                contentType: "application/json",
                success: function (r) {
                    console.log('订单详情');
                    console.log(r);
                    if (r.code === 0) {
                        if(orderState == '物流仓库未签收'){
                            layer.alert('推送成功');
                        }else{
                            layer.alert('修改成功');
                        }
                        vm.getOrderInfo();
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
        //添加备注
        Addnotes:function () {
            console.log(vm.remark)
            if (vm.remark==''){
                layer.alert('请输入备注内容');
            }else {
                if (vm.remarkType==''){
                    layer.alert('请输入备注类型');
                }else {
                    $.ajax({
                        url: '../../order/remark/saveNew',
                        type: 'post',
                        data: JSON.stringify({
                            orderId:this.orderid,
                            remark:vm.remark,
                            remarkType:vm.remarkType
                        }),
                        contentType: "application/json",
                        success: function (r) {
                            console.log(r);
                            if (r.code === 0) {
                                layer.msg('添加成功');
                                console.log(r);
                                vm.getOrderInfo();
                            } else {
                                layer.alert(r.msg);
                            }
                        },
                        error: function () {
                            layer.msg("网络故障");
                        }
                    });
                }
            }
        },
        // 生成国际运单号
        getorderID:function () {
            var index = layer.load();
            var index = layer.load(1); //换了种风格
            var index = layer.load(2, {time: 10*1000}); //又换了种风格，并且设定最长等待10秒
            $.ajax({
                url: '../../product/order/createAbroadWaybill',
                type: 'post',
                data: JSON.stringify({
                    orderId:this.orderid
                }),
                contentType: "application/json",
                success: function (r) {
                    // console.log('订单详情');
                    console.log(r);
                    if (r.code === 0) {

                        // vm.orderDetails = r.orderDTO;

                        layer.msg('操作成功');
                        layer.close(index);
                        vm.getOrderInfo();
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
        // 同步国际运单
        tongbu:function (id) {
            var index = layer.load();
            var index = layer.load(1); //换了种风格
            var index = layer.load(2, {time: 10*1000}); //又换了种风格，并且设定最长等待10秒
            $.ajax({
                url: '../../amazon/neworder/synchronization',
                type: 'get',
                data: {
                    orderId:this.orderid,
                    abroadLogisticsId:id
                },
                dataType: 'json',
                success: function (r) {
                    console.log('订单详情');
                    console.log(r);
                    if (r.code === 0) {
                        // vm.orderDetails = r.orderDTO;
                        // vm.orderDetails.shipAddress.abroadWaybill = r.abroadLogistics;

                        layer.close(index);
                        layer.msg('操作成功');
                        vm.getOrderInfo();
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
        // 选项卡
        tabFcun:function (event) {
            var _index = $(event.target).index();
            $('.layui-tab-title1 li').removeClass('layui-this');
            $(event.target).addClass('layui-this');
            $('.layui-tab-content').removeClass('action');
            $('.layui-tab-content').eq(_index).addClass('action');
        },
        returnFunc:function () {
            window.location.href = 'myOrder.html?page='+vm.page;
        },
        magSku:function () {
            layer.msg('暂无该产品')
        },
        // 添加国际物流
        addguojiWul:function () {
            layer.open({
                type: 1,
                title: false,
                content: $('#addorder'), //这里content是一个普通的String
                skin: 'openClass',
                area: ['75%', '80%'],
                shadeClose: true,
                btn: [],

            });
        },
        // 国际物流明细
        detailsguojiWul:function (i) {
            vm.getWlDetails = vm.orderDetails.abroadLogisticsList[i];
            layer.open({
                type: 1,
                title: false,
                content: $('#detailsorder'), //这里content是一个普通的String
                skin: 'openClass',
                area: ['75%', '80%'],
                shadeClose: true,
                btn: ['添加','取消'],
                btn1: function (index1) {


                },
                btn2: function (index1) {


                }
            });
        },
        // 生成运单号
        createdNum:function () {
            var index = layer.load();
            var index = layer.load(1); //换了种风格
            var index = layer.load(2, {time: 10*100000}); //又换了种风格，并且设定最长等待10秒
            // console.log(vm.orderProductList);
            $.ajax({
                url: '../../amazon/neworder/createAbroadWaybill',
                type: 'post',
                data: JSON.stringify({
                    orderItemRelationList:vm.orderProductList,
                    amazonOrderId:vm.orderDetails.amazonOrderId,
                    packageType:parseInt(vm.wuliuType),
                    channelId:vm.value8,
                    // channelName:vm.wuliuLuxian,
                    chineseName:vm.wuliuDetails.chineseName,
                    englishName:vm.wuliuDetails.englishName,
                    length:parseInt(vm.wuliuDetails.length),
                    width:parseInt(vm.wuliuDetails.width),
                    height:parseInt(vm.wuliuDetails.height),
                    weight:parseInt(vm.wuliuDetails.weight),
                }),
                contentType: "application/json",
                success: function (r) {
                    console.log('生成单号');
                    console.log(r);
                    if (r.code == '0') {
                        layer.msg('操作成功');
                        layer.close(index);
                        vm.getOrderInfo();
                        vm.wuliuDetails.addyundanhao = r.newabroadLogistics.abroadWaybill;
                        vm.wuliuDetails.addzhuizonghao = r.newabroadLogistics.trackWaybill;
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
        // 获取物流线路的下拉
        getWuliuXianl:function () {
            // if(vm.wuliuType != 0 || vm.wuliuType != 1){
            //     vm.wuliuType = 0;
            // }
            if(vm.wuliuType){
                $.ajax({
                    url: '../../amazon/neworder/getShippingMethodCode',
                    type: 'get',
                    data: {
                        type:vm.wuliuType
                    },
                    dataType: 'json',
                    // contentType: "application/json",
                    success: function (r) {
                        console.log('线路下拉');
                        console.log(r);
                        if (r.code == '0') {
                            // layer.msg('操作成功');
                            // layer.close(index);
                            // vm.getOrderInfo();
                            vm.guojilogistics = r.channelilist;
                            vm.wuliuLuxian = vm.guojilogistics[0].channelName;
                            console.log(vm.guojilogistics);
                        } else {
                            layer.alert(r.msg);
                            // layer.close(index);
                        }
                    },
                    error: function () {
                        layer.msg("网络故障");
                        // layer.close(index);
                    }
                });
            }else {
                layer.msg('请先选择订单包裹类型');
            }

        },
        // 物流明细
        getWuliuDetails:function(){

            $.ajax({
                url: '../../amazon/neworder/getShippingFeeDetail',
                type: 'get',
                data: {
                    amazonOrderId:vm.orderDetails.amazonOrderId,
                },
                dataType: 'json',
                // contentType: "application/json",
                success: function (r) {
                    console.log('线路下拉');
                    console.log(r);
                    if (r.code === '0') {
                        // layer.msg('操作成功');
                        // layer.close(index);
                        // vm.getOrderInfo();
                        vm.getWlDetails
                    } else {
                        layer.alert(r.msg);
                        // layer.close(index);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                    // layer.close(index);
                }
            });
        },
        // 物流作废
        deleWuliu:function (id) {
            var index = layer.load();
            var index = layer.load(1); //换了种风格
            var index = layer.load(2, {time: 10*100000}); //又换了种风格，并且设定最长等待10秒
            $.ajax({
                url: '../../amazon/neworder/deleteLogisticAbroad',
                type: 'get',
                data: {
                    abroadLogisticsId:id,
                },
                dataType: 'json',
                // contentType: "application/json",
                success: function (r) {
                    console.log('作废');
                    console.log(r);
                    if (r.code == '0') {
                        layer.msg('操作成功');
                        layer.close(index);
                        vm.getOrderInfo();
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
        // 打印
        dayin:function (i,id) {
            var url = vm.orderDetails.abroadLogisticsList[i].printUrl;
            if(!url){
                var index = layer.load();
                var index = layer.load(1); //换了种风格
                var index = layer.load(2, {time: 10*100000}); //又换了种风格，并且设定最长等待10秒
                $.ajax({
                    url: '../../amazon/neworder/printLogisticAbroad',
                    type: 'get',
                    data: {
                        abroadLogisticsId:id,
                    },
                    dataType: 'json',
                    // contentType: "application/json",
                    success: function (r) {
                        console.log('打印');
                        console.log(r);
                        if (r.code == '0') {
                            // layer.msg('操作成功');
                            layer.close(index);
                            // vm.getOrderInfo();
                            // vm.getWlDetails
                            window.open(r.url);

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
            }else {
                window.open(url);
            }
        },
        // 获取淮关编码
        getHGBM:function () {
            $.ajax({
                url: '../../amazon/neworder/getItemCode',
                type: 'get',
                data: {},
                dataType: 'json',
                // contentType: "application/json",
                success: function (r) {
                    console.log('海关编码');
                    console.log(r);
                    if (r.code == '0') {
                        // vm.itemCodelist = r.itemCodelist;
                        r.itemCodelist.forEach(function (t) {
                            vm.itemCodelist.push({
                                itemCodeId:t.itemCodeId,
                                name:t.itemCode + t.itemCnMaterial,
                                itemCnMaterial:t.itemCnMaterial,
                                itemEnMaterial:t.itemEnMaterial,

                            })
                        })
                        // layer.msg('操作成功');
                        // layer.close(index);
                        // vm.getOrderInfo();
                        // vm.getWlDetails
                    } else {
                        layer.alert(r.msg);
                        // layer.close(index);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                    // layer.close(index);
                }
            });
        },
        // 海关编码修改
        changeHG:function (value) {
            console.log(value);
            vm.itemCodelist.forEach(function (t,i) {
                if(t.itemCodeId == value){
                    vm.wuliuDetails.chineseName = vm.itemCodelist[i].itemCnMaterial;
                    vm.wuliuDetails.englishName = vm.itemCodelist[i].itemEnMaterial;
                }
            })

        },
        // 入库
        ruku:function (id) {
            $.ajax({
                url: '../../amazon/neworder/formruku',
                type: 'get',
                data: {
                    domesticLogisticsId:id
                },
                dataType: 'json',
                // contentType: "application/json",
                success: function (r) {
                    console.log('海关编码');
                    console.log(r);
                    if (r.code == '0') {

                        // layer.msg('操作成功');
                        // layer.close(index);
                        // vm.getOrderInfo();
                        // vm.getWlDetails
                    } else {
                        layer.alert(r.msg);
                        // layer.close(index);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                    // layer.close(index);
                }
            });
        },
        // 出库
        chuku:function (id) {
            $.ajax({
                url: '../../amazon/neworder/formchuku',
                type: 'get',
                data: {
                    orderId:id
                },
                dataType: 'json',
                // contentType: "application/json",
                success: function (r) {
                    console.log('海关编码');
                    console.log(r);
                    if (r.code == '0') {

                        // layer.msg('操作成功');
                        // layer.close(index);
                        // vm.getOrderInfo();
                        // vm.getWlDetails
                    } else {
                        layer.alert(r.msg);
                        // layer.close(index);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                    // layer.close(index);
                }
            });
        }
    },
    created:function () {
        var url = decodeURI(window.location.href);
        var argsIndex = url.split("?orderid=");
        var orderid = argsIndex[1].split('page=');
        this.orderid = parseInt(orderid[0]);
        this.page = orderid[1];
        console.log(this.page);
        this.getOrderInfo();
    }
})