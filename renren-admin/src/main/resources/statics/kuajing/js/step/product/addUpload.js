$(function () {
    $('.inner-content-div2').slimScroll({
        height: '270px' //设置显示的高度
    });
})

window.onload = function () {


}
var html1 = $('<div class="qita"></div>');
var html2 = $('<div class="some-content-related-div" style="width: 100%;margin: 0 auto;"></div>');
var html3 = $('<div class="inner-content-div2"></div>');
var ul = $('<ul></ul>');
//分类item
var fenlItem = Vue.extend({
    name: 'fenl-item',
    props:{item:{}},
    template:[
        '<div class="qita">',
        '<div class="some-content-related-div" style="width: 100%;margin: 0 auto;">',
        '<div class="inner-content-div2">',
        '<ul>',
        '<li v-for="item in leven1" :id="item.id">{{ item.value }}</li>',
        '</ul>',
        '</div>',
        '</div>',
        '</div>',
    ].join('')

});
//注册菜单组件
Vue.component('fenlItem',fenlItem);



var vm = new Vue({
    el:'#step',
    data:{
        value9:null,
        startId: null,
        endId: null,
        uploadIds:[],
        grantShopId: 0,
        isAttribute: 0,
        grantShop:null,
        amazonCategoryId: 0,
        amazonCategory: null,
        amazonTemplateId: 0,
        amazonTemplate: null,
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


                },
                btn2: function (index) {

                    $('#fenleiTankuang div.con>div.qita').remove();
                }
            });
        },

        // 定时上传
        timeUpFunc:function () {
            layer.open({
                type: 1,
                title: false,
                content: $('#timeUp'), //这里content是一个普通的String
                skin: 'openClass',
                area: ['530px', '330px'],
                shadeClose: true,
                btn: ['上传','取消'],
                btn1: function (index) {

                },
                btn2: function (index) {


                }
            });
        },
        //立即上传
        addUpload:function () {
            $.ajax({
                url: '../../product/upload/addUpload',
                type: 'post',
                data: {
                    'startId': this.startId,
                    'endId': this.endId,
                    'uploadIds': this.uploadIds,
                    'grantShopId': this.grantShopId,
                    'isAttribute': this.isAttribute,
                    'grantShop':this.grantShop,
                    'amazonCategoryId': this.amazonCategoryId,
                    'amazonCategory': this.amazonCategory,
                    'amazonTemplateId': this.amazonTemplateId,
                    'amazonTemplate': this.amazonTemplate,
                    'operateItem': this.operateItem,
                },
                dataType: 'json',
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                    } else {
                        layer.alert(r.message);
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
        amazonItemCategory:function (event) {
            var _index = $(event.target).attr('data-index');
            var index = parseInt(_index) + 1;
            vm.leven.splice(index);

            vm.leven.push([{
                'amazonCategoryId':0,
                'ifNext':true,
                'displayName':'分类1111'
            },{
                'amazonCategoryId':0,
                'ifNext':true,
                'displayName':'分类222'
            },{
                'amazonCategoryId':0,
                'ifNext':true,
                'displayName':'分类333'
            }])
        }
    },
    created:function () {
        this.getmarketplace();

    }
})