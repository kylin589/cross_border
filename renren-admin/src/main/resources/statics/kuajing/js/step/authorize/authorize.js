$(function () {

})



var vm = new Vue({
    el: '#step',
    data: {
        // 当前页码
        proCurr:1,
        // 每页数量限制
        pageLimit:12,
        grantId:null,
        grantIds:[],
        authorizeList:[],
        // 国家列表
        gjList:[],
        shopName:'',
        amazonAccount:'',
        area:'',
        merchantId:'',
        grantToken:''
    },
    methods:{
        addShouq:function () {
            layer.open({
                type: 1,
                title: false,
                content: $('#addShouq'), //这里content是一个普通的String
                skin: 'openClass',
                area: ['400px', '400px'],
                shadeClose: true,
                btn: ['添加','取消'],
                btn1: function (index) {
                    console.log(vm.shopName);
                    console.log(vm.amazonAccount);
                    console.log(vm.area);
                    console.log(vm.merchantId);
                    console.log(vm.grantToken);
                    $.ajax({
                        url: '../../amazon/amazongrant/addAmazonGrant',
                        type: 'post',
                        data: JSON.stringify({
                            shopName:vm.shopName,
                            amazonAccount:vm.amazonAccount,
                            area:vm.area,
                            merchantId:vm.merchantId,
                            grantToken:vm.grantToken
                        }),
                        contentType: "application/json",
                        success: function (r) {
                            console.log(r);
                            if (r.code === 0) {
                                vm.getauthorizeList();
                                vm.shopName='';
                                vm.amazonAccount='';
                                vm.area='' ;
                                vm.merchantId='';
                                vm.grantToken='';
                                layer.close(index)
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
        getGjList:function (grantId) {
            layer.open({
                type: 1,
                title: false,
                content: $('#coList'), //这里content是一个普通的String
                skin: 'openClass',
                area: ['600px', '410px'],
                shadeClose: true,
                btn: [],

            });
            vm.grantId = grantId;
            $.ajax({
                url: '../../amazon/amazongrant/countryList',
                type: 'post',
                data: {
                    grantId:this.grantId
                },
                dataType: 'json',
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                       vm.gjList= r.countryList
                    } else {
                        layer.alert(r.message);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            });
        },
        // 分页器
        laypage: function () {
            // var tempTotalCount;

            // 分页器
            layui.use('laypage', function () {
                var laypage = layui.laypage;
                //执行一个laypage实例
                laypage.render({
                    elem: 'page', //注意，这里的 test1 是 ID，不用加 # 号
                    count: vm.totalCount, //数据总数，从服务端得到
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
                            vm.getauthorizeList();
                        }
                    }
                });
            });
        },
        // 获取授权列表
        getauthorizeList:function () {
            $.ajax({
                url: '../../amazon/amazongrant/list',
                type: 'get',
                data: {
                    limit:this.pageLimit,
                    page:this.proCurr
                },
                dataType: 'json',
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.authorizeList=r.page.list;
                    } else {
                        layer.alert(r.message);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            });
        },
        //删除
        getdelete:function (grantId) {
            vm.grantIds.push(grantId);
            console.log(this.grantIds);
            $.ajax({
                url: '../../amazon/amazongrant/delete',
                type: 'post',
                data: JSON.stringify(this.grantIds),
                contentType: "application/json",
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.getauthorizeList();
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
        this.getauthorizeList();
        this.laypage();
    }

})