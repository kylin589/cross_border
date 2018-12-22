var vm = new Vue({
    el:'#LAY_app',
    data:{
        // 当前页码
        proCurr:1,
        // 每页数量限制
        pageLimit:12,
        totalCount:'',
        informList:[{
            id:1,
            type:'订单消息',
            con:'订单消息订单消息订单消息订单消息',
            date:'2018-10-11 10:00:00'
        },{
            id:2,
            type:'产品消息',
            con:'订单消息订单消息订单消息订单消息',
            date:'2018-11-10 12:10:10'
        },{
            id:3,
            type:'订单消息',
            con:'订单消息订单消息订单消息订单消息',
            date:'2018-10-11 11:00:20'
        },{
            id:4,
            type:'产品消息',
            con:'订单消息订单消息订单消息订单消息',
            date:'2018-11-10 10:30:30'
        }],


    },
    methods: {

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

        // 获取消息列表
        getInformList: function (event) {
            $.ajax({
                url: '../../sys/notice/list',
                type: 'get',
                data: '',
                dataType: 'json',
                success: function (r) {
                    console.log('消息列表');
                    console.log(r);
                    if (r.code === 0) {
                        vm.informList = r.page.list;
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
        // 标记为已读
        noticeStateFunc:function (id) {
            layer.confirm('确定标记为已读吗？',function () {
                $.ajax({
                    url: '../../sys/notice/sign',
                    type: 'get',
                    data: {
                        'noticeId':id
                    },
                    dataType: 'json',
                    success: function (r) {
                        console.log('标记为已读');
                        console.log(r);
                        if (r.code === 0) {
                            vm.getInformList();
                            layer.close();
                        } else {
                            layer.alert(r.message);
                        }


                    },
                    error: function () {
                        layer.msg("网络故障");
                    }
                });
            })

        },
        // 标记全部为已读
        allnoticeStateFunc:function () {
            layer.confirm('确定标记全部为已读吗？',function () {
                $.ajax({
                    url: '../../sys/notice/signAll',
                    type: 'get',
                    data: '',
                    dataType: 'json',
                    success: function (r) {
                        console.log('标记为已读');
                        console.log(r);
                        if (r.code === 0) {
                            vm.getInformList();
                            layer.close();
                        } else {
                            layer.alert(r.msg);
                        }


                    },
                    error: function () {
                        layer.msg("网络故障");
                    }
                });
            })
        }
    },
    created: function(){
        this.getInformList();
        this.laypage();
    },
    updated: function(){
        //路由
        // var router = new Router();
        // routerList(router, vm.menuList);
        // router.start();
        // console.log(vm.main);
        // if(vm.main == 'modules/sys/inform.html'){
        // 	$('.layui-badge-dot1').css('display','none');
        // }
    }
});