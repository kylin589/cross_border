$(function () {

})

// 分页器
layui.use('laypage', function(){
    var laypage = layui.laypage;

    //执行一个laypage实例
    laypage.render({
        elem: 'page', //注意，这里的 test1 是 ID，不用加 # 号
        count: 50, //数据总数，从服务端得到
        prev:'<i class="layui-icon layui-icon-left"></i>',
        next:'<i class="layui-icon layui-icon-right"></i>',
        layout:['prev', 'page', 'next','limit','skip'],
        jump: function(obj, first){
            //obj包含了当前分页的所有参数，比如：
            console.log(obj.curr); //得到当前页，以便向服务端请求对应页的数据。
            console.log(obj.limit); //得到每页显示的条数

            //首次不执行
            if(!first){
                //do something
            }
        }
    });
});

var vm = new Vue({
    el: '#step',
    data: {
        authorizeList:[{
            shopName:'安徽省宣杭食品',
            account:'****idouye@yeah.net',
            country:'法国/英国/德国/西班牙/意大利',
            date:'2018/10/15 11:25',
        },{
            shopName:'安徽省宣杭食品',
            account:'****idouye@yeah.net',
            country:'法国/英国/德国/西班牙/意大利',
            date:'2018/10/15 11:25',
        },{
            shopName:'安徽省宣杭食品',
            account:'****idouye@yeah.net',
            country:'法国/英国/德国/西班牙/意大利',
            date:'2018/10/15 11:25',
        },{
            shopName:'安徽省宣杭食品',
            account:'****idouye@yeah.net',
            country:'法国/英国/德国/西班牙/意大利',
            date:'2018/10/15 11:25',
        },{
            shopName:'安徽省宣杭食品',
            account:'****idouye@yeah.net',
            country:'法国/英国/德国/西班牙/意大利',
            date:'2018/10/15 11:25',
        }],
        // 国家列表
        gjList:[{
            shopName:'111',
            country:'1111',
            amazonSite:'111'
        },{
            shopName:'222',
            country:'2',
            amazonSite:'2'
        },{
            shopName:'3',
            country:'3',
            amazonSite:'3'
        }]
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
                    // console.log(vm.xiugaiData);
                    // $.ajax({
                    //     url: '../../product/products/batchmodify',
                    //     type: 'post',
                    //     // data:vm.xiugaiData,
                    //     data:JSON.stringify(vm.xiugaiData),
                    //     contentType: "application/json",
                    //     success: function (r) {
                    //         console.log(r);
                    //         if (r.code === 0) {
                    //             layer.alert('操作成功');
                    //
                    //         } else {
                    //             layer.alert(r.msg);
                    //         }
                    //     },
                    //     error: function () {
                    //         layer.msg("网络故障");
                    //     }
                    // })

                },
                btn2: function (index) {


                }
            });
        },
        getGjList:function () {
            layer.open({
                type: 1,
                title: false,
                content: $('#coList'), //这里content是一个普通的String
                skin: 'openClass',
                area: ['600px', '410px'],
                shadeClose: true,
                btn: [],

            });
        }


    }

})