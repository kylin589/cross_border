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
        sel:0,
        ean:'',
        merchantId:'',
        grantToken:'',
        amazonGrant:{},
        totalCount:'',
        // 总数
        all:'',
        shiyong:'',
        weishuy:'',
        delValue:''
    },
    methods:{
        //添加upc
        addShouq:function () {
            layer.open({
                type: 1,
                title: false,
                content: $('#addShouq'), //这里content是一个普通的String
                skin: 'openClass',
                area: ['400px', '400px'],
                shadeClose: true,
                btn: ['添加','取消'],
                btn1: function (index1) {
                    var eanarry = vm.ean.split('\n');
                    function getTextByJs(arr) {
                        var str = "";
                        for (var i = 0; i < arr.length; i++) {
                        str += arr[i]+ ",";
                        }
                        //去掉最后一个逗号(如果不需要去掉，就不用写)
                        if (str.length > 0) {
                            str = str.substr(0, str.length - 1);
                        }
                        return str;
                    }
                    var ean1 = getTextByJs(eanarry);
                    var ean2 = ean1.split('，');
                    var ean = ean2.join(',');

                    console.log(ean);
                    console.log(vm.sel);
                    var index = layer.load();
                    var index = layer.load(1); //换了种风格
                    var index = layer.load(2, {time: 10*100000}); //又换了种风格，并且设定最长等待10秒
                    $.ajax({
                        url: '../../product/eanupc/batchadd',
                        type: 'post',
                        data: JSON.stringify({
                            type:vm.sel,
                            codes:ean,
                        }),
                        contentType: "application/json",
                        success: function (r) {
                            console.log(r);
                            if (r.code === 0) {
                                vm.getauthorizeList();
                                layer.close(index);
                                layer.close(index1);
                                layer.msg('添加成功')
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
                            vm.getauthorizeList1();
                        }
                    }
                });
            });
        },
        // 获取UPC列表
        getauthorizeList:function () {
            $.ajax({
                url: '../../product/eanupc/list',
                type: 'get',
                data: {
                    limit:this.pageLimit,
                    page:this.proCurr
                },
                dataType: 'json',
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.all = r.count;
                        vm.shiyong = r.YesStateCount;
                        vm.weishuy = r.noStateCount
                        vm.authorizeList=r.page.list;
                        vm.totalCount = r.page.totalCount;
                        vm.laypage();
                    } else {
                        layer.alert(r.message);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            });
        },
        // 获取UPC列表
        getauthorizeList1:function () {
            $.ajax({
                url: '../../product/eanupc/list',
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
                        vm.totalCount = r.page.totalCount;
                        // vm.laypage();
                    } else {
                        layer.alert(r.message);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            });
        },
        // 批量删除
        delPiliang:function () {
            $.ajax({
                url: '../../product/eanupc/batchDelete',
                type: 'get',
                data: {
                    number:this.delValue,

                },
                dataType: 'json',
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        layer.msg('删除成功');
                        vm.getauthorizeList1();
                        // vm.laypage();
                    } else {
                        layer.alert(r.message);
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
        // this.laypage();
    }

})