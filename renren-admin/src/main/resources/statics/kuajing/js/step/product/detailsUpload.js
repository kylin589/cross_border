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
        id:'',
        // 上传产品详情
        upProDetails:{
            uploadEntity:{}
        },
        value9:null,
        startId: null,
        endId: null,
        uploadIdsstr:'',
        uploadIds:[],
        // uploadIds:[],
        grantShopId: 0,
        isAttribute: 0,
        grantShop:null,
        amazonCategoryId: 0,
        amazonCategory: null,
        amazonAllCategory:'',
        amazonAllArr:[],
        amazonTemplateId: 0,
        amazonTemplate: null,
        inputche1:'',
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
        nodeId:'',
        grantCounty:''
    },
    methods:{
        // 获取详情
        getDetails:function () {
            $.ajax({
                url: '../../product/upload/details',
                type: 'get',
                data: {
                    'uploadId':this.id
                },
                dataType: 'json',
                success: function (r) {
                    console.log('详情');
                    console.log(r);
                    if (r.code === 0) {


                        vm.upProDetails = r.data;
                        vm.startId = r.data.uploadEntity.startId;
                        vm.endId = r.data.uploadEntity.endId;
                        vm.uploadIdsstr = r.data.uploadEntity.uploadIds;
                        vm.shopinfo = r.data.uploadEntity.grantShopId;
                        vm.amazonAllCategory = r.data.allCategories;
                        vm.amazonCategoryId = r.data.uploadEntity.amazonCategoryId;
                        vm.amazonCategory = r.data.uploadEntity.amazonCategory;
                        vm.flModleValue = r.data.uploadEntity.amazonTemplateId;
                        // vm.flModleValue = r.data.uploadEntity.amazonCategoryId;
                        vm.nodeId = r.data.uploadEntity.amazonCategoryNodeId;
                        vm.modelAttr = r.data.middleEntitys;
                        console.log('111');
                        if(JSON.stringify(r.data.uploadEntity.operateItem) != 'null'){
                            console.log(r.data.uploadEntity.operateItem.split(','));
                            if(r.data.uploadEntity.operateItem.split(',').length == 5){
                                vm.inputche1 = 'true';
                                vm.inputche = ['0','1','2','3','4'];
                                // $('#operateItem input').prop('checked',true)
                            }else {
                                var arr = r.data.uploadEntity.operateItem.split(',');
                                console.log(arr);
                                arr.forEach(function (t) {
                                    if(t == 0){
                                        vm.inputche.push('0');
                                        console.log(t);
                                        // $('#operateItem input').eq(1).prop('checked',true);
                                        // console.log($('#operateItem input').eq(1));
                                    }else if(t == 1){
                                        vm.inputche.push('1');
                                        // $('#operateItem input').eq(2).prop('checked',true);
                                    }else if(t == 2){
                                        vm.inputche.push('2');
                                        // $('#operateItem input').eq(3).prop('checked',true);
                                    }else if(t == 3){
                                        vm.inputche.push('3');
                                        // $('#operateItem input').eq(4).prop('checked',true);
                                    }else if(t == 4){
                                        vm.inputche.push('4');
                                        // $('#operateItem input').eq(5).prop('checked',true);
                                    }
                                })
                            }
                        }


                        vm.selFlFunc();
                        console.log(vm.inputche);
                    } else {
                        layer.alert(r.message);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            });
        },


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

                    vm.amazonAllCategory = '';
                    vm.amazonAllArr.forEach(function (t) {
                        vm.amazonAllCategory+=t+'/'
                    })
                    vm.amazonAllCategory.substr(0, vm.amazonAllCategory.length - 1);
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
        // 定时上传
        timeUpFunc:function () {

            if(vm.shopinfo == '' || vm.inputche.length == 0 || vm.nodeId == '' || vm.amazonCategory == '' || vm.flModleValue == ''){
                layer.msg('授权店铺、更新选项、分类节点、分类节点id、分类模版不能为空！！')
            }else {
                var grantShop = '';
                vm.marketplace.forEach(function (t) {
                    if(t.grantShopId == vm.shopinfo){
                        vm.grantCounty = t.grantCounty;
                        vm.countryCode = t.countryCode;
                        grantShop = t.shopName;
                    }
                })
                var templateDisplayName = '';
                vm.flModleList.forEach(function (t) {
                    if(t.templateId == vm.flModleValue){
                        templateDisplayName = t.templateDisplayName;
                    }
                })

                layer.open({
                    type: 1,
                    title: false,
                    content: $('#timeUp'), //这里content是一个普通的String
                    skin: 'openClass',
                    area: ['530px', '330px'],
                    shadeClose: true,
                    btn: ['上传', '取消'],
                    btn1: function (index1) {

                        if(vm.changeTime == ''){
                            layer.msg('请转换时间')
                        }else {

                            var d=new Date(Date.parse(vm.changeTime.replace(/-/g,"/")));
                            var curDate=new Date();
                            if(d <=curDate){
                                layer.msg('定时上传时间小于当前时间，请重新选择')
                            }else {
                                var index = layer.load();
                                var index = layer.load(1); //换了种风格
                                var index = layer.load(2, {time: 10*1000}); //又换了种风格，并且设定最长等待10秒

                                vm.operateItem = [];
                                // console.log(vm.inputche)
                                vm.uploadIds = vm.uploadIdsstr.split(',');

                                $.ajax({
                                    url: '../../product/upload/renewTimingUpload',
                                    type: 'post',
                                    data: JSON.stringify({
                                        'uploadId':vm.id,
                                        'startId': vm.startId,
                                        'endId': vm.endId,
                                        'uploadIds': vm.uploadIds,
                                        'grantShopId': parseInt(vm.shopinfo),
                                        'isAttribute': vm.isAttribute,
                                        'grantShop':grantShop,
                                        'amazonCategoryId': vm.amazonCategoryId,
                                        'amazonCategory': vm.amazonCategory,
                                        'amazonTemplateId': vm.flModleValue,
                                        'amazonTemplate': templateDisplayName,
                                        'operateItem': vm.inputche,
                                        'time':vm.changeTime,
                                        'countryCode':vm.countryCode,
                                        'fieldsEntityList':vm.modelAttr,
                                        'amazonNodeId':vm.nodeId,
                                    }),
                                    // dataType: 'json',
                                    contentType: "application/json",
                                    success: function (r) {
                                        console.log(r);
                                        if (r.code === 0) {

                                            layer.close(index);
                                            layer.close(index1);
                                            window.location.href="upProduct.html";
                                        } else {
                                            layer.close(index);
                                            // layer.alert(r.message);
                                            // window.location.href="upProduct.html";
                                        }


                                    },
                                    error: function () {
                                        layer.msg("网络故障");
                                        layer.close(index);
                                    }
                                });
                            }


                        }


                    },
                    btn2: function (index) {


                    }
                });
            }

        },
        // 全选
        allSelFunc:function (event) {

            var _if = $(event.target).prop('checked');
            // vm.inputche[1] = 0;
            // vm.inputche[2] = 1;
            // vm.inputche[3] = 2;
            // vm.inputche[4] = 3;
            // vm.inputche[5] = 4;
            if(vm.inputche1){
                vm.inputche = ['0','1','2','3','4'];
            }else {
                vm.inputche = [];
            }


            // $('#operateItem input').prop('checked',_if);
            console.log(vm.inputche1);
        },
        aaa:function () {
            if(vm.inputche.length != 5){
                vm.inputche1 = false;
            }else {
                vm.inputche1 = true;
            }
        },
        //立即上传
        addUpload:function () {
            if(vm.shopinfo == '' || vm.inputche.length == 0 || vm.nodeId == '' || vm.amazonCategory == '' || vm.flModleValue == ''){
                layer.msg('授权店铺、更新选项、分类节点、分类节点id、分类模版不能为空！！')
            }else {
                layer.confirm('确定上传吗？',function (index1) {


                    var index = layer.load();
                    var index = layer.load(1); //换了种风格
                    var index = layer.load(2, {time: 10*1000}); //又换了种风格，并且设定最长等待10秒

                    console.log(vm.shopinfo);
                    // vm.uploadIds = vm.uploadIdsstr;
                    vm.uploadIds = vm.uploadIdsstr.split(',');
                    console.log(vm.uploadIds);
                    // if (vm.inputche[0]==true){
                    //
                    //     vm.operateItem = [0,1,2,3,4];
                    // }else {
                    //     for (var i=0;i<vm.inputche.length;i++){
                    //         if (vm.inputche[i]==true){
                    //             console.log('2222');
                    //             vm.operateItem.push(i-1);
                    //         }
                    //     }
                    // }
                    console.log(vm.shopinfo);

                    var grantShop = '';
                    vm.marketplace.forEach(function (t) {
                        if(t.grantShopId == vm.shopinfo){
                            grantShop = t.shopName
                        }
                    })

                    var templateDisplayName = '';
                    vm.flModleList.forEach(function (t) {
                        if(t.templateId == vm.flModleValue){
                            console.log(t.templateId);
                            templateDisplayName = t.templateDisplayName;
                        }
                    })
                    console.log(vm.flModleValue);
                    console.log(templateDisplayName);

                    // vm.grantShopId = vm.shopinfo.grantShopId;
                    // vm.grantShop = vm.shopinfo.shopName;
                    // console.log(vm.grantShopId);
                    $.ajax({
                        url: '../../product/upload/againUploadByForm',
                        type: 'post',
                        data: JSON.stringify({
                            'uploadId':parseInt(vm.id),
                            'startId': parseInt(vm.startId),
                            'endId': parseInt(vm.endId),
                            'uploadIds': vm.uploadIds,
                            'grantShopId': parseInt(vm.shopinfo),
                            // 'grantShopId': parseInt(vm.grantShopId),
                            'isAttribute': '',
                            'grantShop':grantShop,
                            'amazonCategoryId': vm.amazonCategoryId,
                            'amazonCategory': vm.amazonCategory,
                            'amazonTemplateId': parseInt(vm.flModleValue),
                            'amazonTemplate': templateDisplayName,
                            'operateItem': vm.inputche,
                            'fieldsEntityList':vm.modelAttr,
                            'amazonNodeId':vm.nodeId,
                        }),
                        contentType: "application/json",
                        // dataType: 'json',
                        success: function (r) {
                            console.log(r);
                            if (r.code === 0) {

                                layer.msg("上传成功");
                                layer.close(index);
                                layer.close(index1);
                                window.location.href="upProduct.html";

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
                });
            }




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
                // console.log(this.shopinfo.countryCode);
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
        amazonItemCategory:function (list,event) {
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
                    amazonCategoryId:list.id
                },
                dataType: 'json',
                success: function (r) {
                    console.log(list.id);
                    console.log('子集分类')
                    console.log(r);
                    if (r.code === 0) {
                        if(r.amazonCategoryEntityChildList.length != 0){
                            vm.leven.push(r.amazonCategoryEntityChildList);
                            console.log(vm.leven);
                            vm.amazonCategoryId = list.id;
                            vm.amazonCategory = list.displayName;
                            vm.amazonAllArr.push(list.displayName);
                            // vm.amazonAllArr.forEach(function (t) {
                            //     vm.amazonAllCategory+=t+'/'
                            // })
                            // vm.amazonAllCategory.substr(0, vm.amazonAllCategory.length - 1);
                            console.log(vm.amazonAllArr);
                            vm.nodeId = list.nodeId;
                        }else {
                            vm.amazonCategoryId = list.id;
                            vm.amazonCategory = list.displayName;
                            // vm.amazonAllArr.push(list.displayName);
                            console.log(vm.amazonAllArr);
                            vm.amazonAllCategory='',
                                // vm.amazonAllArr.forEach(function (t) {
                                //     vm.amazonAllCategory+=t+'/'
                                // })
                                // vm.amazonAllCategory+=list.displayName;
                                vm.amazonAllArr.push(list.displayName)

                            console.log(vm.amazonAllCategory);
                            vm.nodeId = list.nodeId;
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
                    countryCode:vm.countryCode,
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

            if(vm.shopinfo!=''){
                var countryCode;
                vm.marketplace.forEach(function (t) {
                    if(t.grantShopId == vm.shopinfo){
                        countryCode = t.countryCode
                    }
                })
                $.ajax({
                    url: '../../amazon/amazoncategoryhistory/getMyList',
                    type: 'get',
                    data: {
                        'countryCode':countryCode
                    },
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
            }else {
                layer.msg('请选择店铺');
            }
        },
        lishiSelFunc:function (event) {
            vm.amazonCategory = $(event.target).attr('data-val');
            vm.amazonCategoryId = $(event.target).attr('id');
            vm.amazonAllCategory = $(event.target).attr('data-allV');
        },
        // 选择模版
        selFlFunc:function () {
            if (this.shopinfo!='') {
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
        clickValActive:function (v,event) {
            // $(event.target).siblings().removeClass('active');
            // $(event.target).addClass('active');
            v.value = $(event.target).attr('data-val');

        }
    },
    created:function () {
        // this.selFlFunc();
        var url = decodeURI(window.location.href);
        var argsIndex = url.split("?id=");
        var id = argsIndex[1];
        // console.log(id)
        this.id = parseInt(id);
        console.log(this.id);
        this.getDetails();
        this.getmarketplace();
        // this.selFlFunc();

    }
})