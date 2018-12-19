var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "deptId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url:"nourl"
        }
    }
};
var ztree;

var vm = new Vue({
    el:'#rrapp',
    data:{
        showList: true,
        title: null,
        dept:{
            parentName:null,
            parentId:0,
            orderNum:0
        },
        chongzhiData:{
            money:'',
            remark:'',
            deptId:null
        }
    },
    methods: {
        getDept: function(){
            //加载公司树
            $.get(baseURL + "sys/dept/select", function(r){
                ztree = $.fn.zTree.init($("#deptTree"), setting, r.deptList);
                var node = ztree.getNodeByParam("deptId", vm.dept.parentId);
                ztree.selectNode(node);
                console.log('公司');
                console.log(node);

                vm.dept.parentName = node.name;
            })
        },
        add: function(){
            window.location.href = 'deptCreate.html';
        },
        update: function () {
            var deptId = getDeptId();
            if(deptId == null){
                return ;
            }

            window.location.href = 'deptDetails.html?id=' + deptId;

            // $.get(baseURL + "sys/dept/info/"+deptId, function(r){
            //     vm.showList = false;
            //     vm.title = "修改";
            //     vm.dept = r.dept;
            //
            //     vm.getDept();
            // });
        },
        del: function () {
            var deptId = getDeptId();
            if(deptId == null){
                return ;
            }

            confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/dept/delete",
                    data: "deptId=" + deptId,
                    success: function(r){
                        if(r.code === 0){
                            alert('操作成功', function(){
                                vm.reload();
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        saveOrUpdate: function (event) {
            var url = vm.dept.deptId == null ? "sys/dept/save" : "sys/dept/update";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.dept),
                success: function(r){
                    if(r.code === 0){
                        alert('操作成功', function(){
                            vm.reload();
                        });
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },
        deptTree: function(){
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择公司",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#deptLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    //选择上级公司
                    vm.dept.parentId = node[0].deptId;
                    vm.dept.parentName = node[0].name;

                    layer.close(index);
                }
            });
        },
        reload: function () {
            vm.showList = true;
            Dept.table.refresh();
        },
        // 充值弹框
        chongzhiTank:function (id) {
            vm.chongzhiData.deptId = id;
            layer.open({
                type: 1,
                title: false,
                content: $('#chongzhi'), //这里content是一个普通的String
                skin: 'openClass',
                area: ['400px', '220px'],
                shadeClose: true,
                btn: ['充值','取消'],
                btn1: function (index) {
                    console.log(vm.chongzhiData);
                    $.ajax({
                        type: "POST",
                        url: baseURL + "sys/recharge/recharge",
                        data: JSON.stringify(vm.chongzhiData),
                        contentType: "application/json",
                        success: function(r){
                            if(r.code === 0){
                                layer.msg('充值成功');

                            }else{
                                alert(r.msg);
                            }
                        }
                    });
                    layer.close(index);

                },
                btn2: function (index) {


                }
            });
        }
    }
});

var Dept = {
    id: "deptTable",
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Dept.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: '编号', field: 'deptId', visible: false, align: 'center', valign: 'middle', width: '80px'},
        {title: '公司名称', field: 'name', align: 'center', valign: 'middle', sortable: true, width: '180px'},
        {title: '上级公司', field: 'parentName', align: 'center', valign: 'middle', sortable: true, width: '100px'},
        {title: 'SKU', field: 'companySku', align: 'center', valign: 'middle', sortable: true, width: '100px'},
        {title: '联系人', field: 'companyPerson', align: 'center', valign: 'middle', sortable: true, width: '100px'},
        {title: '联系方式', field: 'companyTel', align: 'center', valign: 'middle', sortable: true, width: '100px'},
        {title: '余额', field: 'balance', align: 'center', valign: 'middle', sortable: true, width: '100px'},
        // {title: '新余额', field: 'parentName', align: 'center', valign: 'middle', sortable: true, width: '100px'},
        {title: '最近更新', field: 'updateTime', align: 'center', valign: 'middle', sortable: true, width: '100px'},
        {title: '创建时间', field: 'createTime', align: 'center', valign: 'middle', sortable: true, width: '100px'},
        {title: '操作', field: 'deptId', align: 'center', valign: 'middle', sortable: true, width: '100px',formatter: function(value, options, row){
            console.log(value)
            return '<span class="label label-success" onclick="aa('+value.deptId+')" style="cursor:pointer;">查看</span> <span class="label label-success1" onclick="bb('+value.deptId+')" style="cursor:pointer;">充值</span>'

        }}
        ]
    return columns;
};

// 查看详情
function aa(a) {
    window.location.href = 'deptDetailsL.html?id=' + a;
}
// 充值
function bb(id) {
    vm.chongzhiTank(id)
}
function getDeptId () {
    var selected = $('#deptTable').bootstrapTreeTable('getSelections');
    if (selected.length == 0) {
        alert("请选择一条记录");
        return null;
    } else {
        return selected[0].id;
    }
}


$(function () {
    $.get(baseURL + "sys/dept/info", function(r){
        var colunms = Dept.initColumn();
        var table = new TreeTable(Dept.id, baseURL + "sys/dept/list", colunms);

        table.setRootCodeValue(r.deptId);
        table.setExpandColumn(2);
        table.setIdField("deptId");
        table.setCodeField("deptId");
        table.setParentCodeField("parentId");
        table.setExpandAll(false);
        table.init();
        Dept.table = table;
        console.log(table);
    });
});
