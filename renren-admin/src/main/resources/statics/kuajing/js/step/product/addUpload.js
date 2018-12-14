$(function () {
    $('.inner-content-div2').slimScroll({
        height: '270px' //设置显示的高度
    });
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
        amazonTemplateId: 10,
        amazonTemplate: 'test模板',
        inputche:[],
        operateItem: [],
        marketplace:[],
        shopinfo:{
        },
        leven:[],
        arr:[],
        arr2:[],
        // 转换时间
        changeTime:''
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
                    layer.close(index);

                },
                btn2: function (index) {

                    $('#fenleiTankuang div.con>div.qita').remove();
                }
            });
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
                        console.log(vm.uploadIds);
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
        //立即上传
        addUpload:function () {
            console.log(vm.inputche)
            vm.uploadIds = vm.uploadIdsstr.split(',');
            console.log(vm.uploadIds);
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
                data: JSON.stringify({
                    'startId': parseInt(vm.startId),
                    'endId': parseInt(vm.endId),
                    'uploadIds': vm.uploadIds,
                    'grantShopId': parseInt(vm.grantShopId),
                    'isAttribute': vm.isAttribute,
                    'grantShop':vm.grantShop,
                    'amazonCategoryId': vm.amazonCategoryId,
                    'amazonCategory': vm.amazonCategory,
                    'amazonTemplateId': vm.amazonTemplateId,
                    'amazonTemplate': vm.amazonTemplate,
                    'operateItem': vm.operateItem,
                }),
                contentType: "application/json",
                // dataType: 'json',
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
            if (this.shopinfo.region!=undefined) {

                $.ajax({
                    url: '../../product/amazoncategory/amazonOneCategory',
                    type: 'post',
                    data: {
                        region:this.shopinfo.region
                    },
                    dataType: 'json',
                    success: function (r) {
                        console.log(r);
                        if (r.code === 0) {
                            vm.leven = [];
                            vm.leven.push(r.amazonCategoryEntityList);
                            console.log(vm.leven);
                            vm.fenleiTankuang();
                        } else {
                            layer.alert(r.message);
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
            var _index = $(event.target).attr('data-index');
            var index = parseInt(_index) + 1;
            vm.leven.splice(index);
            console.log(list);
            if (list.ifNext=='true') {
                $.ajax({
                    url: '../../product/amazoncategory/childCategoryList',
                    type: 'post',
                    data: {
                        amazonCategoryId:list.amazonCategoryId
                    },
                    dataType: 'json',
                    success: function (r) {
                        console.log(r);
                        if (r.code === 0) {
                            vm.leven.push(r.amazonCategoryEntityChildList);
                        } else {
                            layer.alert(r.message);
                        }
                    },
                    error: function () {
                        layer.msg("网络故障");
                    }
                });
            }else {
                vm.amazonCategoryId = list.amazonCategoryId;
                vm.amazonCategory = list.displayName;
            }

        },
        //时区转换
        timeZoneConversion:function () {
            console.log(vm.value9);
            console.log(vm.shopinfo.countryCode);
            $.ajax({
                url: '../../product/upload/timeZoneConversion',
                type: 'get',
                data: {
                    countryCode:vm.value9,
                    countryTime:vm.shopinfo.countryCode
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
        }
    },
    created:function () {
        this.getmarketplace();

    }
})