$(function () {

})



var vm = new Vue({
    el: '#step',
    data: {
        id:null,
        // 当前页码
        proCurr:1,
        // 每页数量限制
        pageLimit:12,
        xiaofList:[],
        couDetails:{

        },
        type:[{
            id:1,
            name:'服务费'
        },{
            id:2,
            name:'物流费'
        }],
        typeValue:'',
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
                    // console.log(r);
                    if (r.code === 0) {

                        vm.couDetails = r.dept;
                        vm.getauthorizeList();
                        vm.laypage();


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
        // 获取消费记录列表
        getauthorizeList:function () {
            console.log(this.couDetails);
            if(vm.typeValue != ''){
                vm.couDetails.type = vm.typeValue
            }
            $.ajax({
                url: '../../sys/consume/list',
                type: 'get',
                data: this.couDetails,
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