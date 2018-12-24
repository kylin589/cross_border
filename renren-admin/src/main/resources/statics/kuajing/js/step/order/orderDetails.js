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
    $('.layui-tab-title li').click(function () {
        var _index = $(this).index();
        $('.layui-tab-title li').removeClass('layui-this');
        $(this).addClass('layui-this');
        $('.layui-tab-content').removeClass('action');
        $('.layui-tab-content').eq(_index).addClass('action');
    })

    $('a.logistics').mouseover(function () {
        console.log(1111);
        if($(this).attr('data-ok') == 'true'){
            var _val = $(this).val();
            var _top = $(this).offset().top;
            var _left = $(this).offset().left - 130;
            var _height = $(this).height();
            var top = _top + _height-8;
            $('.logisticsDiv').css({
                'display':'inline-block',
                'top':top+'px',
                'left':_left+'px'
            })
        }

    })

    $('a.logistics').mouseout(function () {
        console.log(444)
        if($(this).attr('data-ok') == 'true'){
            $('.logisticsDiv').css({
                'display':'none',
            })
        }

    })

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
        logistics:{
        },
        remark:'',
        remarkType:'',
        caigou:'',
        wulDanh:''

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
                        for (var i=0;i<vm.orderDetails.domesticLogisticsList.length;i++){
                            vm.waybill.push(vm.orderDetails.domesticLogisticsList[i].waybill);
                            vm.price.push(vm.orderDetails.purchasePrice);
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
        editJijian:function (domesticLogisticsId,index) {
            console.log(index);
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
                $(event.target).val('编辑');
                $(event.target).parent().parent().find('input[type=text]').attr('disabled','true');
                $(event.target).parent().parent().find('textarea').attr('disabled','true');
                $(event.target).parent().parent().find('input[type=text]').css('border','1px solid transparent');
                $(event.target).parent().parent().find('input.noedit').css('display','none');
                if($(event.target).parent().parent().find('.logistics').length!=0){
                    $(event.target).parent().parent().find('.logistics').attr('data-ok','true');
                }
                $.ajax({
                    url: '../../domestic/updateLogistics',
                    type: 'post',
                    data: {
                        orderId:this.orderId,
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
                                    orderId:this.orderId,
                                },
                                dataType: 'json',
                                success: function (r) {
                                    console.log(r);
                                    if (r.code === 0) {

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
        //编辑修改国内物流
        edit:function (domesticLogisticsId,index) {
            console.log(index);
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
                $(event.target).val('编辑');
                $(event.target).parent().parent().find('input[type=text]').attr('disabled','true');
                $(event.target).parent().parent().find('textarea').attr('disabled','true');
                $(event.target).parent().parent().find('input[type=text]').css('border','1px solid transparent');
                $(event.target).parent().parent().find('input.noedit').css('display','none');
                if($(event.target).parent().parent().find('.logistics').length!=0){
                    $(event.target).parent().parent().find('.logistics').attr('data-ok','true');
                }
                $.ajax({
                    url: '../../domestic/updateLogistics',
                    type: 'post',
                    data: {
                        orderId:this.orderId,
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
                                    orderId:this.orderId,
                                },
                                dataType: 'json',
                                success: function (r) {
                                    console.log(r);
                                    if (r.code === 0) {
                                        $.ajax({
                                            url: '../../order/remark/updateLog',
                                            type: 'get',
                                            data: {
                                                orderId:this.orderId,
                                            },
                                            dataType: 'json',
                                            success: function (r) {
                                                console.log(r);
                                                if (r.code === 0) {

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
        noedit:function (event) {
            $(event.target).prev().val('编辑');
            $(event.target).parent().parent().find('input[type=text]').attr('disabled','true');
            $(event.target).parent().parent().find('textarea').attr('disabled','true');
            $(event.target).parent().parent().find('input[type=text]').css('border','1px solid transparent');
            $(event.target).parent().parent().find('input.noedit').css('display','none');
            if($(event.target).parent().parent().find('.logistics').length!=0){
                $(event.target).parent().parent().find('.logistics').attr('data-ok','true');
            }
        },
        //物流信息
        queryLogistic:function (waybill) {
            console.log(waybill);
            $.ajax({
                url: '../../domestic/queryLogistic',
                type: 'get',
                data: {
                    waybill:waybill,
                },
                dataType: 'json',
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.logistics = r.data;
                    } else {
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            });
            if($(event.target).attr('data-ok') == 'true'){
                console.log(111)
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
                btn1: function (index) {

                    $.ajax({
                        url: '../../domestic/getLogisticsCompany',
                        type: 'get',
                        data: {
                            orderId:this.orderId,
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
                                        orderId:this.orderId,
                                    },
                                    dataType: 'json',
                                    success: function (r) {
                                        console.log(r);
                                        if (r.code === 0) {

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
                },
                btn2: function (index) {


                }
            });
        },
        //修改订单状态
        updateState:function (orderState) {
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
                        layer.alert('修改成功');
                        vm.getOrderInfo();
                    } else {
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            });
        },
        //添加备注
        Addnotes:function () {
            console.log(vm.remark)
            if (vm.remark==''){
                layer.alert('请输入备注内容');
            }if (vm.remarkType==''){
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
                            layer.alert('添加成功');
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
    created:function () {
        var url = decodeURI(window.location.href);
        var argsIndex = url.split("?orderid=");
        var orderid = argsIndex[1];
        this.orderid = parseInt(orderid);
        console.log(this.orderid);
        this.getOrderInfo();
    }
})