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
        orderid:null,
        orderDetails:{
            shipAddress:{},
            abroadLogistics:{}
        },
        waybill:[],
        domesticLogisticsId:null,
        price:[],
        guojilogistics:[{
            value:1,
            name:'中美专线(特惠)[USZXR]'
        },{
            value:2,
            name:'中美专线(标快)[USZMTK]'
        },{
            value:3,
            name:'云途中欧专线挂号[EUDDP]'
        }],
        intLog:{
            num:123123,
            num1:123214,
            circuit:'10 nicolson drive ',
            date:'2018-10-26'
        },
        remark:[{
            title:'2018-10-11',
            name:'小明',
            con:'深刻搭街坊世界杯s'
        }],
        operationLog:[{
            date:'2018-10-28',
            name:'小明',
            con:'时间发货：深刻搭街坊世界杯s'
        },{
            date:'2018-10-28',
            name:'小明',
            con:'时间发货：深刻搭街坊世界杯s'
        },{
            date:'2018-10-28',
            name:'小明',
            con:'时间发货：深刻搭街坊世界杯s'
        }],
        addyundanhao:'123424',
        addzhuizonghao:'34564',
        logistics:[{
                context:'客户已签收',
                time:'2018-11-10 10:00:00'
            },
            {
                context:'客户已签收',
                time:'2018-11-10 10:00:00'
            },
            {
                context:'客户已签收',
                time:'2018-11-10 10:00:00'
            }

        ],
        remark:'',
        remarkType:'',
        caigou:'',
        wulDanh:'',
        isLookWuliu:true,
        quxiaoJijian:null,

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
                url: '../../product/order/getOrderInfo',
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
                        vm.waybill = [];
                        for (var i=0;i<vm.orderDetails.domesticLogisticsList.length;i++){
                            vm.waybill.push(vm.orderDetails.domesticLogisticsList[i].waybill);
                            vm.price.push(vm.orderDetails.domesticLogisticsList[i].price);

                        }
                        // vm.quxiaoJijian = r.orderDTO.shipAddress;
                        // console.log(vm.quxiaoJijian);
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
                // $(event.target).parent().parent().find('textarea').removeAttr("disabled");
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
                    data: JSON.stringify({
                        'shipAddress':vm.orderDetails.shipAddress
                    }),
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
        edit:function (domesticLogisticsId,index,event) {
            console.log(index);
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
                $.ajax({
                    // url: 'http://39.106.131.222:8000/domestic/updateLogistics',
                    url: 'http://www.threeee.cn/domestic/updateLogistics',
                    type: 'get',
                    data: {
                        orderId:vm.orderid,
                        waybill:this.waybill[index],
                        domesticLogisticsId:domesticLogisticsId,
                        price:this.price[index],
                    },
                    dataType: 'json',
                    success: function (r) {
                        console.log(r);
                        if (r.code === 0) {
                            $.ajax({
                                url: '../../order/remark/updateLog',
                                type: 'get',
                                data: {
                                    orderId:vm.orderid,
                                },
                                dataType: 'json',
                                success: function (r) {
                                    console.log(r);
                                    if (r.code === 0) {
                                        layer.msg('修改成功')
                                    } else {
                                        layer.alert(r.msg);
                                    }
                                },
                                error: function () {
                                    layer.msg("网络故障");
                                }
                            });



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
        noedit:function (d,index,event) {
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
        // 删除国内物流
        synchronize:function (item) {
            console.log(item);
            var index = layer.load();
            var index = layer.load(1); //换了种风格
            var index = layer.load(2, {time: 10*1000}); //又换了种风格，并且设定最长等待10秒
            layer.close(index);
            layer.msg("暂无此功能");

            // $.ajax({
            //     // url: 'http://39.106.131.222:8000/domestic/updateLogistics',
            //     url: '../../product/order/deleteLogistic',
            //     type: 'get',
            //     data: {
            //         domesticLogisticsId:item.domesticLogisticsId,
            //
            //     },
            //     dataType: 'json',
            //     success: function (r) {
            //         console.log(r);
            //         vm.getOrderInfo();
            //         layer.close(index);
            //     },
            //     error: function () {
            //         layer.msg("删除失败");
            //         layer.close(index);
            //     }
            // });
        },
        //物流信息
        queryLogistic:function (waybill,event) {
            console.log(waybill);
            console.log($(event.target));
            // console.log($(event.target).attr('data-ok'));
            // console.log($(event.target).find('input').attr('disabled'));
            if(vm.isLookWuliu == true && JSON.stringify(waybill) != 'null'){
                console.log(waybill)
                // var index = layer.load();
                // var index = layer.load(1); //换了种风格
                // var index = layer.load(2, {time: 10*1000}); //又换了种风格，并且设定最长等待10秒
                $.ajax({
                    // url: 'http://39.106.131.222:8000/domestic/queryLogistic',
                    url: 'http://www.threeee.cn/domestic/queryLogistic',
                    type: 'get',
                    data: {
                        waybill:waybill,
                    },
                    dataType: 'json',
                    success: function (r) {
                        console.log(r);
                        if (r.code === 0) {
                            vm.logistics = r.data;
                            // layer.close(index);
                        } else {
                            layer.alert(r.msg);
                            // layer.close(index);
                        }
                    },
                    error: function () {
                        // layer.msg("网络故障");
                        // layer.close(index);
                    }
                });
                var _val = $(event.target).val();
                var _top = $(event.target).offset().top;
                var _left = $(event.target).offset().left - 130;
                var _height = $(event.target).height();
                var top = _top + _height-8;
                $('.logisticsDiv').css({
                    'display':'inline-block',
                    'top':top+'px',
                    'left':_left+'px'
                })
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
        addWuliuFunc:function () {
            layer.open({
                type: 1,
                title: false,
                content: $('#addWul'), //这里content是一个普通的String
                skin: 'openClass',
                area: ['400px', '220px'],
                shadeClose: true,
                btn: ['添加','取消'],
                btn1: function (index1) {
                    var index = layer.load();
                    var index = layer.load(1); //换了种风格
                    var index = layer.load(2, {time: 10*1000}); //又换了种风格，并且设定最长等待10秒
                    $.ajax({
                        // url: 'http://39.106.131.222:8000/domestic/getLogisticsCompany',
                        url: 'http://www.threeee.cn/domestic/getLogisticsCompany',
                        type: 'get',
                        data: {
                            orderId:vm.orderid,
                            price:vm.caigou,
                            waybill:vm.wulDanh
                        },
                        dataType: 'json',
                        success: function (r) {
                            console.log(r);
                            if (r.code === 0) {
                                $.ajax({
                                    url: '../../order/remark/addLog',
                                    type: 'get',
                                    data: {
                                        "orderId":vm.orderid,
                                    },
                                    dataType: 'json',
                                    success: function (r) {
                                        console.log(r);
                                        if (r.code === 0) {
                                            layer.msg("添加成功");
                                            layer.close(index);
                                            layer.close(index1);
                                            vm.getOrderInfo();
;                                        } else {
                                            layer.alert(r.msg);
                                            layer.close(index);
                                        }
                                    },
                                    error: function () {
                                        layer.msg("网络故障");
                                        layer.close(index);

                                    }
                                });
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
        },
        //修改订单状态
        updateState:function (orderState) {
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
                        if(orderState == '待签收'){
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
                        url: '../../order/remark/save',
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
                        vm.orderDetails.shipAddress.abroadWaybill = r.abroadLogistics;
                        layer.msg('生成成功');
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
        // 同步国际运单
        tongbu:function () {
            var index = layer.load();
            var index = layer.load(1); //换了种风格
            var index = layer.load(2, {time: 10*1000}); //又换了种风格，并且设定最长等待10秒
            $.ajax({
                url: '../../product/order/synchronization',
                type: 'post',
                data: {
                    orderId:this.orderid
                },
                dataType: 'json',
                success: function (r) {
                    console.log('订单详情');
                    console.log(r);
                    if (r.code === 0) {
                        // vm.orderDetails = r.orderDTO;
                        // vm.orderDetails.shipAddress.abroadWaybill = r.abroadLogistics;
                        layer.msg('同步成功');
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
        // 选项卡
        tabFcun:function (event) {
            var _index = $(event.target).index();
            $('.layui-tab-title1 li').removeClass('layui-this');
            $(event.target).addClass('layui-this');
            $('.layui-tab-content').removeClass('action');
            $('.layui-tab-content').eq(_index).addClass('action');
        },
        returnFunc:function () {
            window.location.href = 'myOrder.html';
        }
    },
    created:function () {
        var url = decodeURI(window.location.href);
        var argsIndex = url.split("?orderid=");
        var orderid = argsIndex[1];
        this.orderid = parseInt(orderid);
        console.log(this.orderid);
        this.getOrderInfo();
    }
})