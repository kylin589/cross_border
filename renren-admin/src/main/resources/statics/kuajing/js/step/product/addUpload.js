$(function () {
    // $('.inner-content-div2').slimScroll({
    //     height: '270px' //设置显示的高度
    // });
})

window.onload = function () {


}




var vm = new Vue({
    el:'#step',
    data:{
        value9:null,
        startId: null,
        endId: null,
        uploadIdsstr:'',
        uploadIds:[],
        grantShopId: 0,
        isAttribute: 0,
        grantShop:null,
        amazonCategoryId: 0,
        amazonCategory: null,
        amazonAllCategory:'',
        amazonAllArr:[],
        amazonTemplateId: 0,
        amazonTemplate: null,
        inputche:[],
        operateItem: [],
        marketplace:[],
        shopinfo:'',
        // shopinfo:{},
        leven:[],
        arr:[],
        arr2:[],
        // 转换时间
        changeTime:'',
        lishiList:[],
        // 分类模版
        flModleList:[],
        flModleValue:'',
        // 模版属性
        modelAttr:[],
    },
    methods:{
        fenleiTankuang:function () {
            // var con = $('.fenleiCon');
            // $('#fenleiTankuang div.con').append(con);
            // $('.fenleiCon ul li').click(function () {
            //     var id = $(this).attr('data-id');
            //     var bol = $(this).attr('data-ifTwo');
            //     vm.fenlei(bol,id);
            //     vm.fenlei();
            // })
            // vm.amazonOneCategory();



            // 分类弹框
            layer.open({
                type: 1,
                title: false,
                content: $('#fenleiTankuang'), //这里content是一个普通的String
                skin: 'openClass',
                area: ['800px', '400px'],
                shadeClose: true,
                btn: ['确定','取消'],
                btn1: function (index) {
                    $('#fenleiTankuang div.con li').removeClass('active');
                    layer.close(index);

                },
                btn2: function (index) {

                    // $('#fenleiTankuang div.con>div.qita').remove();
                    $('#fenleiTankuang div.con li').removeClass('active');
                }
            });
            console.log('打印');
            console.log($('.inner-content-div2'))
            $('.inner-content-div2').slimScroll({
                height: '270px' //设置显示的高度
            });
        },

        // 选择店铺更改
        chanDianFunc:function () {
            vm.flModleList = [];
            vm.flModleValue = '';
            vm.modelAttr = [];

        },
        // 定时上传
        timeUpFunc:function () {
            if (this.shopinfo.region!=undefined) {
                layer.open({
                    type: 1,
                    title: false,
                    content: $('#timeUp'), //这里content是一个普通的String
                    skin: 'openClass',
                    area: ['530px', '330px'],
                    shadeClose: true,
                    btn: ['上传', '取消'],
                    btn1: function (index) {
                        console.log(vm.inputche)
                        vm.uploadIds = vm.uploadIdsstr.split(',');
                        console.log(typeof(vm.uploadIds));
                        if (vm.inputche[0]==true){

                            vm.operateItem = [0,1,2,3,4];
                        }else {
                            for (var i=0;i<vm.inputche.length;i++){
                                if (vm.inputche[i]==true){
                                    console.log('2222');
                                    vm.operateItem.push(i-1);
                                }
                            }
                        }
                        console.log(vm.operateItem);
                        vm.grantShopId = vm.shopinfo.grantShopId;
                        vm.grantShop = vm.shopinfo.shopName;
                        console.log(vm);
                        $.ajax({
                            url: '../../product/upload/addUpload',
                            type: 'post',
                            data: {
                                'startId': vm.startId,
                                'endId': vm.endId,
                                'uploadIds': vm.uploadIds,
                                'grantShopId': vm.grantShopId,
                                'isAttribute': vm.isAttribute,
                                'grantShop':vm.grantShop,
                                'amazonCategoryId': vm.amazonCategoryId,
                                'amazonCategory': vm.amazonCategory,
                                'amazonTemplateId': vm.amazonTemplateId,
                                'amazonTemplate': vm.amazonTemplate,
                                'operateItem': vm.operateItem,
                                'time':vm.changeTime,
                                'countryCode':vm.shopinfo.countryCode,
                            },
                            dataType: 'json',
                            success: function (r) {
                                console.log(r);
                                if (r.code === 0) {
                                    layer.close(index)
                                } else {
                                    layer.alert(r.message);
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
            }else {
                layer.msg("请选择店铺");
            }
        },
        // 全选
        allSelFunc:function () {

            var _if = $(event.target).prop('checked');
            console.log(vm.inputche);
            $('#operateItem input').prop('checked',_if);
        },
        //立即上传
        addUpload:function () {

            layer.confirm('确定上传吗？',function (index) {
                // console.log(vm.shopinfo);
                // vm.uploadIds = vm.uploadIdsstr;
                vm.uploadIds = vm.uploadIdsstr.split(',');
                // console.log(vm.uploadIds);
                if (vm.inputche[0]==true){

                    vm.operateItem = [0,1,2,3,4];
                }else {
                    for (var i=0;i<vm.inputche.length;i++){
                        if (vm.inputche[i]==true){
                            console.log('2222');
                            vm.operateItem.push(i-1);
                        }
                    }
                }
                console.log(vm.shopinfo);
                var grantShop = '';
                vm.marketplace.forEach(function (t) {
                    if(t.grantShopId == vm.shopinfo){
                        grantShop = t.shopName
                    }
                })

                var templateDisplayName = '';
                vm.flModleList.forEach(function (t) {
                    if(t.templateId = vm.flModleValue){
                        templateDisplayName = t.templateDisplayName;
                    }
                })
                console.log(grantShop)
                console.log(templateDisplayName)

                // vm.grantShopId = vm.shopinfo.grantShopId;
                // vm.grantShop = vm.shopinfo.shopName;
                // console.log(vm.grantShopId);
                $.ajax({
                    url: '../../product/upload/addUpload',
                    type: 'post',
                    data: JSON.stringify({
                        'startId': parseInt(vm.startId),
                        'endId': parseInt(vm.endId),
                        'uploadIds': vm.uploadIds,
                        'grantShopId': parseInt(vm.shopinfo),
                        // 'grantShopId': parseInt(vm.grantShopId),
                        'isAttribute': vm.isAttribute,
                        'grantShop':grantShop,
                        'amazonCategoryId': vm.amazonCategoryId,
                        'amazonCategory': vm.amazonCategory,
                        'amazonTemplateId': vm.flModleValue,
                        'amazonTemplate': templateDisplayName,
                        'operateItem': vm.operateItem,
                        'fieldsEntityList':vm.modelAttr,
                    }),
                    contentType: "application/json",
                    // dataType: 'json',
                    success: function (r) {
                        console.log(r);
                        if (r.code === 0) {

                            layer.msg("上传成功");
                            layer.close(index)

                        } else {
                            layer.alert(r.msg);
                        }


                    },
                    error: function () {
                        layer.msg("网络故障");
                    }
                });
            });


        },
        //授权店铺
        getmarketplace:function () {

            $.ajax({
                url: '../../amazon/amazongrantshop/myShopList',
                type: 'get',
                data: {
                },
                dataType: 'json',
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.marketplace = r.shopList;
                    } else {
                        layer.alert(r.message);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            });
        },
        //亚马逊一级分类
        amazonOneCategory:function () {
            // console.log(this.shopinfo.region);
            if (vm.shopinfo!='') {
                console.log(this.shopinfo.countryCode);
                var countryCode;
                vm.marketplace.forEach(function (t) {
                    if(t.grantShopId == vm.shopinfo){
                        countryCode = t.countryCode
                    }
                })

                $.ajax({
                    url: '../../product/amazoncategory/amazonOneCategory',
                    type: 'get',
                    data: {
                        countryCode:countryCode
                    },
                    dataType: 'json',
                    success: function (r) {
                        console.log(r);
                        if (r.code === 0) {
                            vm.leven = [];
                            vm.leven.push(r.amazonCategoryEntityList);
                            console.log(vm.leven);

                            vm.fenleiTankuang();
                            setTimeout(function () {
                                $('.inner-content-div2').slimScroll({
                                    height: '270px' //设置显示的高度
                                });
                            },1000)

                        } else {
                            layer.alert(r.msg);
                        }
                    },
                    error: function () {
                        layer.msg("网络故障");
                    }
                });
            }else {
                layer.msg("请选择店铺");
            }

        },
        // 子级分类
        amazonItemCategory:function (list) {
            $('.inner-content-div2').slimScroll({
                height: '270px' //设置显示的高度
            });
            // console.log($(event))
            $(event.target).siblings().removeClass('active');
            $(event.target).addClass('active');
            //
            // var id = $(event.target).attr('id');

            var _index = $(event.target).attr('data-index');
            var index = parseInt(_index) + 1;
            vm.leven.splice(index);
            vm.amazonAllArr.splice(_index);
            console.log(list);
            // if (list.ifNext=='true') {
                $.ajax({
                    url: '../../product/amazoncategory/childCategoryList',
                    type: 'get',
                    data: {
                        amazonCategoryId:list.amazonCategoryId
                    },
                    dataType: 'json',
                    success: function (r) {
                        console.log('子集分类')
                        console.log(r);
                        if (r.code === 0) {
                            if(r.amazonCategoryEntityChildList.length != 0){
                                vm.leven.push(r.amazonCategoryEntityChildList);
                                console.log(vm.leven);
                                vm.amazonCategoryId = list.amazonCategoryId;
                                vm.amazonCategory = list.displayName;
                                vm.amazonAllArr.push(list.displayName);
                                console.log(vm.amazonAllArr);
                            }else {
                                vm.amazonCategoryId = list.amazonCategoryId;
                                vm.amazonCategory = list.displayName;
                                // vm.amazonAllArr.push(list.displayName);
                                console.log(vm.amazonAllArr);
                                vm.amazonAllArr.forEach(function (t) {
                                    vm.amazonAllCategory+=t+'/'
                                })
                                vm.amazonAllCategory+=list.displayName;
                                console.log(vm.amazonAllCategory);
                                // amazonAllCategory =
                            }

                        } else {
                            layer.alert(r.message);
                        }
                    },
                    error: function () {
                        layer.msg("网络故障");
                    }
                });
            // }else {

            // }

        },
        //时区转换
        timeZoneConversion:function () {
            console.log(vm.value9);
            console.log(vm.shopinfo.countryCode);
            $.ajax({
                url: '../../product/upload/timeZoneConversion',
                type: 'get',
                data: {
                    countryCode:vm.shopinfo.countryCode,
                    // countryCode:vm.value9,
                    countryTime:vm.value9
                },
                // contentType: "application/json",
                dataType: 'json',
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.changeTime = r.CNTimeString;
                    } else {
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            });
        },
        // 历史选择
        lishiFunc:function () {

            $.ajax({
                url: '../../amazon/amazoncategoryhistory/getMyList',
                type: 'get',
                data: '',
                dataType: 'json',
                success: function (r) {
                    console.log('历史选择')
                    console.log(r);
                    if (r.code === 0) {
                        vm.lishiList = r.list;
                        layer.open({
                            type: 1,
                            title: false,
                            content: $('#lishi'), //这里content是一个普通的String
                            skin: 'openClass',
                            area: ['800px', '400px'],
                            shadeClose: true,
                            btn: ['确定','取消'],
                            btn1: function (index) {
                                // $('#fenleiTankuang div.con li').removeClass('active');
                                layer.close(index);

                            },
                            btn2: function (index) {

                                // $('#fenleiTankuang div.con>div.qita').remove();
                                // $('#fenleiTankuang div.con li').removeClass('active');
                            }
                        });
                        $('.inner-content-div2').slimScroll({
                            height: '300px' //设置显示的高度
                        });
                        setTimeout(function () {
                            $('.inner-content-div2').slimScroll({
                                height: '300px' //设置显示的高度
                            });
                        },1000)


                    } else {
                        layer.alert(r.message);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            });
        },
        lishiSelFunc:function () {
            vm.amazonCategory = $(event.target).attr('data-val');
            vm.amazonCategoryId = $(event.target).attr('id');
            vm.amazonAllCategory = $(event.target).attr('data-allV');
        },
        // 选择模版
        selFlFunc:function () {
            if (this.shopinfo!='') {
                // vm.flModleList = [];
                // vm.flModleValue = '';
                // vm.modelAttr = [];
                $.ajax({
                    url: '../../product/template/list',
                    type: 'get',
                    data: {
                    },
                    dataType: 'json',
                    success: function (r) {
                        console.log('分类模版');
                        console.log(r);
                        if (r.code === 0) {
                            vm.flModleList = r.templates;




                        } else {
                            layer.alert(r.message);
                        }
                    },
                    error: function () {
                        layer.msg("网络故障");
                    }
                });
            }else {
                layer.msg("请选择分类");
            }

        },
        // 模版选择后获取分类属性
        chanFunc:function () {
            var countryCode;
            vm.marketplace.forEach(function (t) {
                if(t.grantShopId == vm.shopinfo){
                    countryCode = t.countryCode
                }
            })
            console.log('countryCode' ,countryCode);
            $.ajax({
                url: '../../product/template/getOptionalValues',
                type: 'get',
                data: {
                    'templateId':vm.flModleValue,
                    'countryCode':countryCode
                },
                dataType: 'json',
                success: function (r) {
                    console.log('模版可选');
                    console.log(r);
                    if (r.code === 0) {
                        vm.modelAttr = r.data;


                    } else {
                        layer.alert(r.message);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            });
        },
        // 点击模版分类属性可选值选中
        clickValActive:function (v) {
            // $(event.target).siblings().removeClass('active');
            // $(event.target).addClass('active');
            // console.log(v);
            // console.log($(event.target).attr('data-index'));
            // console.log($(event.target).text());
            v.value = $(event.target).attr('data-index');

        }
    },
    created:function () {
        this.getmarketplace();

    }
})