$(function () {

})

window.onload = function() {


}


var vm = new Vue({
    el: '#step',
    data: {
        // 产品id
        id:null,
        couDetails:{
            "deptId": null,
            "parentId": null,
            "name": "",
            "parentName": "",
            "delFlag": null,
            "open": null,
            "list": null,
            "accountCount": null,
            "companySku": "",
            "companyAddress": "",
            "companyPerson": "",
            "companyTel": "",
            "companyQq": "",
            "companyPoints": null,
            "balance": 0,
            "availableBalance": 0,
            "unliquidatedNumber": 0,
            "unshippedNumber": 0,
            "estimatedCost": 0,
            "estimatedOrder": 0,
            "updateTime": "",
            "createTime": ""},
        chongzhiList:[],
        proCurr:1,
        // 每页数量限制
        pageLimit:12,
        totalCount:0,

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


                    } else {
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            })
        },

        // 修改保存
        saveCou:function () {

            layer.confirm('确定修改吗？', function(index){

                var index = layer.load();
                var index = layer.load(1); //换了种风格
                var index = layer.load(2, {time: 10*1000}); //又换了种风格，并且设定最长等待10秒
                $.ajax({
                    type: 'post',
                    url: '../../sys/dept/update',
                    contentType: "application/json",
                    data: JSON.stringify(vm.couDetails),
                    success: function (r) {
                        console.log('修改产品');
                        console.log(r);
                        console.log(vm.proDetails);
                        if (r.code == 0) {
                            layer.close(index);

                            window.location.href = document.referrer;

                        } else {
                            alert(r.msg);
                        }
                    }
                });



            });



        },

        // 充值记录
        chongzhiFunc:function () {
            vm.getchongzhiList();
            layer.open({
                type: 1,
                title: false,
                content: $('#chongzhi'), //这里content是一个普通的String
                skin: 'openClass',
                area: ['800px', '420px'],
                shadeClose: true,
                btn: [],

            });
        },
        // 获取充值记录
        getchongzhiList:function () {
            $.ajax({
                url: '../../sys/recharge/list',
                type: 'get',
                // data:vm.id,
                // dataType:'json',
                data:{deptId:vm.id},
                contentType: "application/json",
                success: function (r) {
                    console.log('充值记录');
                    console.log(r);
                    if (r.code === 0) {
                        // layer.msg('修改成功');
                        vm.chongzhiList = r.list;
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
        // 充值记录分页器
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

        // 消费记录
        xiaofFunc:function () {
            window.location.href = ''
        },
        // 返回
        returnFunc:function () {
            layer.confirm('确定返回吗？',function () {
                window.location.href = document.referrer;
            })
        }
    },

    created:function () {

        var url = decodeURI(window.location.href);
        var argsIndex = url.split("?id=");
        var id = argsIndex[1];
        // console.log(id)
        this.id = parseInt(id);
        console.log(this.id);
        this.getCouDetails();

    }

})