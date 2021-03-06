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
        recordList:[],
        // 国家列表
        gjList:[],
        shopName:'',
        amazonAccount:'',
        area:'',
        merchantId:'',
        grantToken:'',
        amazonGrant:{},
        totalCount:'',
    },
    methods:{

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
                            vm.getrecordList1();
                        }
                    }
                });
            });
        },
        // 获取授权列表
        getrecordList:function () {
            $.ajax({
                url: '../../product/claim/list',
                type: 'get',
                data: {
                    limit:this.pageLimit,
                    page:this.proCurr
                },
                dataType: 'json',
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.recordList=r.page.list;
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
        getrecordList1:function () {
            $.ajax({
                url: '../../product/claim/list',
                type: 'get',
                data: {
                    limit:this.pageLimit,
                    page:this.proCurr
                },
                dataType: 'json',
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.recordList=r.page.list;
                        vm.totalCount = r.page.totalCount;
                    } else {
                        layer.alert(r.message);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            });
        },


    },
    created:function () {
        this.getrecordList();
        this.laypage();
    }

})