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
        // 当前页码
        proCurr:1,
        // 每页数量限制
        pageLimit:12,
        prolist:[],
        // 总页数
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
                            vm.getAllUploadList1();
                        }
                    }
                });
            });
        },
        //上传列表
        getAllUploadList:function () {
            console.log(this.proCurr);
            console.log(this.pageLimit);
            $.ajax({
                url: '../../product/upload/getAllUploadList',
                type: 'post',
                data: {
                    page:this.proCurr,
                    limit:this.pageLimit
                },
                dataType: 'json',
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.prolist = r.page.list;
                        vm.totalCount = r.page.totalCount;
                        vm.laypage();
                    } else {
                        layer.alert(r.msg);
                    }


                },
                error: function () {
                    layer.msg("网络故障");
                }
            });
        },

        getAllUploadList1:function () {
            console.log(vm.proCurr);
            console.log(vm.pageLimit);
            $.ajax({
                url: '../../product/upload/getAllUploadList',
                type: 'post',
                data: {
                    page:vm.proCurr,
                    limit:vm.pageLimit
                },
                dataType: 'json',
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.prolist = r.page.list;
                        vm.totalCount = r.page.totalCount;
                        // this.laypage();
                    } else {
                        layer.alert(r.msg);
                    }


                },
                error: function () {
                    layer.msg("网络故障");
                }
            });
        },
        // 编辑
        upFunc:function (id) {
            window.location.href="detailsUpload.html?id="+id;
        },
        // 删除
        delFunc:function (id) {
            layer.confirm('确定删除吗？',function (index) {
                $.ajax({
                    url: '../../product/upload/delete',
                    type: 'get',
                    data: {
                        uploadId:id
                    },
                    dataType: 'json',
                    success: function (r) {
                        console.log(r);
                        if (r.code === 0) {
                            layer.close(index);
                            layer.msg('删除成功');
                            vm.getAllUploadList();
                        } else {
                            layer.alert(r.msg);
                        }


                    },
                    error: function () {
                        layer.msg("网络故障");
                    }
                });
            })
        },
        // 重新上传
        reUpFunc:function (id) {
            layer.confirm('确定重新上传吗？',function (index) {
                console.log(id);
                $.ajax({
                    url: '../../product/upload/againUploadByButton',
                    type: 'post',
                    data: {
                        uploadId:id
                    },
                    dataType: 'json',
                    success: function (r) {
                        console.log(r);
                        if (r.code === 0) {

                            layer.close(index);
                            layer.msg('重新上传成功');
                        } else {
                            layer.alert(r.msg);
                        }


                    },
                    error: function () {
                        layer.msg("网络故障");
                    }
                });
            })
        },
        // xml列表
        xmlFunc:function (id) {
            window.location.href="xmlListUP.html?id="+id;
        },
        // xml1列表
        xmlFunc1:function (id) {
            window.location.href="xmlListUP1.html?id="+id;
        },

    },
    created:function () {
        this.getAllUploadList();

    }
})