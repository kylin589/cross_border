window.onload = function (ev) {
    var rows = document.getElementsByTagName('tr');//取得行
    for(var i=0 ;i<rows.length; i++)
    {
        if(i != 0){
            rows[i].onmouseover = function(){//鼠标移上去,添加一个类'hilite'
                this.className += 'hilite';
            }
            rows[i].onmouseout = function(){//鼠标移开,改变该类的名称
                this.className = this.className.replace('hilite','');
            }
        }

    }
}



var vm = new Vue({
    el:'#step',
    data:{
        id:'',
        // 当前页码
        proCurr:1,
        // 每页数量限制
        pageLimit:12,
        xmllist:[],
        // 总页数
        totalCount:'',
        xml:'',
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
                            vm.getMyUploadList();
                        }
                    }
                });
            });
        },
        //xml列表
        getMyUploadList:function () {
            $.ajax({
                url: '../../amazon/resultxml/list',
                type: 'get',
                data: {
                    'uploadId':this.id
                },
                dataType: 'json',
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.xmllist = r.data;
                        // vm.totalCount = r.page.totalCount;
                    } else {
                        layer.alert(r.msg);
                    }


                },
                error: function () {
                    layer.msg("网络故障");
                }
            });
        },
        tankXml:function (d) {
            vm.xml = d;
            layer.open({
                type: 1,
                title: false,
                content: $('#xmlDiv'), //这里content是一个普通的String
                skin: 'openClass',
                area: ['530px', '430px'],
                shadeClose: true,
                btn: [],
                btn1: function (index) {

                },
                btn2: function (index) {


                }
            });
        },
        errorTan:function (code) {

            $.ajax({
                url: '../../amazon/resultxml/list',
                type: 'get',
                data: {
                    'errorCode':code
                },
                dataType: 'json',
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.xmllist = r.data;
                        // vm.totalCount = r.page.totalCount;
                    } else {
                        layer.alert(r.msg);
                    }


                },
                error: function () {
                    layer.msg("网络故障");
                }
            });

            layer.open({
                type: 1,
                title: false,
                content: $('#xmlDiv'), //这里content是一个普通的String
                skin: 'openClass',
                area: ['530px', '430px'],
                shadeClose: true,
                btn: [],
                btn1: function (index) {

                },
                btn2: function (index) {


                }
            });
        }

    },
    created:function () {
        var url = decodeURI(window.location.href);
        var argsIndex = url.split("?id=");
        var id = argsIndex[1];
        // console.log(id)
        this.id = parseInt(id);

        this.getMyUploadList();
        // this.laypage();
    }
})