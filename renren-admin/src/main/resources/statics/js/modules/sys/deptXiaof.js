$(function () {

})



var vm = new Vue({
    el: '#step',
    data: {
        id:null,
        // 当前页码
        proCurr:1,
        // 每页数量限制
        pageLimit:10,
        totalCount:0,
        xiaofList:[],
        couDetails:{

        },
        type:[{
            id:1,
            name:'全部'
        },{
            id:2,
            name:'服务费'
        },{
            id:3,
            name:'物流费'
        }],
        typeValue:'全部',
        // 所有员工
        allYUanG:[{
            userId:'1-1',
            displayName:'所有员工'
        }],
        // 所选员工的value
        allYUanGValue:'1-1',
        orderId:'',
    },
    methods:{
        // 获取公司详情
        getCouDetails:function () {
            $.ajax({
                url: '../../sys/dept/info/'+this.id,
                type: 'get',
                data: '',
                dataType: 'json',
                success: function (r) {
                    console.log('公司详情');
                    console.log(r);
                    if (r.code === 0) {

                        vm.couDetails = r.dept;
                        vm.getauthorizeList();
                        // vm.laypage();
                        vm.getManList();


                    } else {
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            })
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
                    limits: [10, 20, 30],
                    limit: vm.pageLimit,
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
        // 获取员工列表
        getManList:function () {
            console.log('11111');
            $.ajax({
                url: '../../sys/user/getUserList',
                type: 'get',
                data:{deptId:vm.id}
                // 'productIds': JSON.stringify(vm.activeProlist)
                ,
                // contentType: "application/json",
                dataType: 'json',
                success: function (r) {
                    console.log('获取员工');
                    console.log(r);
                    if (r.code === 0) {
                        // layer.msg('操作成功');
                        // vm.getPage();
                        vm.allYUanG= r.userList;
                        vm.allYUanG.unshift({
                            userId:'1-1',
                            displayName:'所有员工'
                        })
                        console.log('员工')
                        console.log(vm.allYUanG)

                    } else {
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            })
        },
        // 获取消费记录列表
        getauthorizeList:function () {
            console.log(this.couDetails);
            if(vm.typeValue != '全部'){
                vm.couDetails.type = vm.typeValue
            }else {
                vm.couDetails.type = '';
            }
            if(vm.allYUanGValue != '1-1'){
                vm.couDetails.userId = vm.allYUanGValue
            }else {
                vm.couDetails.userId = '';
            }
            // vm.couDetails.page = vm.page;
            // vm.couDetails.limit = vm.limit;
            $.ajax({
                url: '../../sys/consume/list',
                type: 'get',
                // data: this.couDetails,
                data:{
                    deptId:vm.couDetails.deptId,
                    userId:vm.couDetails.userId,
                    orderId:vm.orderId,
                    abroadWaybill:'',
                    type:vm.couDetails.type,
                    page:vm.proCurr,
                    limit:vm.pageLimit,
                },
                dataType: 'json',
                success: function (r) {
                    console.log('消费记录')
                    console.log(r);
                    if (r.code === 0) {
                        vm.xiaofList=r.page.list;
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
        getauthorizeList1:function () {
            // console.log(this.couDetails);
            if(vm.typeValue != '全部'){
                vm.couDetails.type = vm.typeValue
            }else {
                vm.couDetails.type = '';
            }
            if(vm.allYUanGValue != '1-1'){
                vm.couDetails.userId = vm.allYUanGValue
            }else {
                vm.couDetails.userId = '';
            }
            // vm.couDetails.page = vm.page;
            // vm.couDetails.limit = vm.limit;
            $.ajax({
                url: '../../sys/consume/list',
                type: 'get',
                data: {
                    deptId:vm.couDetails.deptId,
                    userId:vm.couDetails.userId,
                    orderId:vm.couDetails.orderId,
                    abroadWaybill:'',
                    type:vm.couDetails.type,
                    page:vm.proCurr,
                    limit:vm.pageLimit,
                },
                dataType: 'json',
                success: function (r) {
                    console.log('消费记录')
                    console.log(r);
                    if (r.code === 0) {
                        vm.xiaofList=r.page.list;
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

        var url = decodeURI(window.location.href);
        var argsIndex = url.split("?id=");
        var id = argsIndex[1];

        // console.log(data);

        this.id = parseInt(id);
        this.getCouDetails();

        // this.getauthorizeList();
        // this.laypage();

    }

})