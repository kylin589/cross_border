$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/user/list',
        datatype: "json",
        colModel: [			
			{ label: '账户ID', name: 'userId', index: "user_id", width: 45, key: true },
            { label: '昵称', name: 'displayName', width: 65 },
			{ label: '账户', name: 'username', width: 55 },
            { label: '所属公司', name: 'deptName', sortable: false, width: 75 },
			{ label: '邮箱', name: 'email', width: 90 },
			{ label: '手机号', name: 'mobile', width: 100 },
			{ label: '状态', name: 'status', width: 60, formatter: function(value, options, row){
				return value === 0 ? 
					'<span class="label label-danger">禁用</span>' : 
					'<span class="label label-success">正常</span>';
			}},
			{ label: '创建时间', name: 'createTime', index: "create_time", width: 85}
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});
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
        q:{
            username: null
        },
        showList: true,
        showList1:true,
        title:null,
        roleList:{},
        user:{
            status:1,
            deptId:null,
            deptName:null,
            roleIdList:[]
        },
        chanUserNameMsg:'',
        chanUserNameMsgE:'',
        chanUserNameMsgE1:'',
        erroMsg1:'',
        erroMsg2:'',
        erroMsg3:'',
        erroMsg4:'',
        erroMsg5:'',
        erroMsg6:'',
        erroMsg7:'',
        erroMsg9:'',
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function(){
            vm.showList = false;
            vm.showList1 = true;
            vm.title = "新增";
            vm.roleList = {};
            vm.user = {deptName:null, deptId:null, status:1, roleIdList:[]};
            $('#user_password').show();
            //获取角色信息
            this.getRoleList();

            vm.getDept();
        },
        getDept: function(){
            //加载公司树
            $.get(baseURL + "sys/dept/list", function(r){
                ztree = $.fn.zTree.init($("#deptTree"), setting, r);
                var node = ztree.getNodeByParam("deptId", vm.user.deptId);
                if(node != null){
                    ztree.selectNode(node);

                    vm.user.deptName = node.name;
                }
            })
        },
        chanUserNameE:function () {
            // console.log(vm.user.username)
            var reg =/\s/;
            console.log(reg.test(vm.user.enName));
            if(reg.test(vm.user.enName)){
                console.log(11111);
                vm.chanUserNameMsgE = '英文名称中不能有空格'
            }else {
                vm.chanUserNameMsgE = ''
            }
        },
        chanUserNameE1:function () {
            // console.log(vm.user.username)
            var reg =/\s/;
            if(reg.test(vm.user.enBrand)){
                vm.chanUserNameMsgE1 = '英文品牌中不能有空格'
            }else {
                vm.chanUserNameMsgE1 = ''
            }
        },
        chanUserName:function () {
            console.log(vm.user.username)
            if(vm.user.username.length>20 || vm.user.username.length<6){
                vm.chanUserNameMsg = '账号长度必须在6到20个字符之间'
            }else {
                vm.chanUserNameMsg = ''
            }
        },
        okPassword:function () {
            if(vm.user.password1 != vm.user.password){
                vm.erroMsg5 = '确认密码错误'
            }else {
                vm.erroMsg5 = '';
            }
        },
        chanPassword:function () {
            var reg = /^(?![0-9\x21-\x2f\x3a-\x40\x5b-\x60\x7B-\x7F]+$)(?![0-9]+$)(?![0-90-9A-Za-z]+$)(?![\x21-\x2f\x3a-\x40\x5b-\x60\x7B-\x7F]+$)(?![A-Za-z]+$)(?![a-zA-Z\x21-\x2f\x3a-\x40\x5b-\x60\x7B-\x7F]+$)[0-9A-Za-z\x21-\x2f\x3a-\x40\x5b-\x60\x7B-\x7F]{8,}$/;
            console.log(vm.user.password);
            console.log(reg.test(vm.user.password));
            // console.log(reg.match(vm.user.password));
            if(reg.test(vm.user.password)){
                var reg1 = /^[\x21-\x2f\x3a-\x40\x5b-\x60\x7B-\x7F]/;
                if(reg1.test(vm.user.password)){
                    // console.log(111111);
                    vm.erroMsg4 = '密码不能以字符开头'
                }else {
                    vm.erroMsg4 = ''
                }

            }else {
                vm.erroMsg4 = '密码至少8位,要求必须字母、数字加英文符号(不包含空格)';
            }
        },
        update: function () {
            var userId = getSelectedRow();
            if(userId == null){
                return ;
            }
            $('#user_password').hide();
            vm.showList = false;
            vm.showList1 = false;
            vm.title = "修改";

            vm.getUser(userId);
            //获取角色信息
            this.getRoleList();
        },
        del: function () {
            var userIds = getSelectedRows();
            if(userIds == null){
                return ;
            }

            confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/user/delete",
                    contentType: "application/json",
                    data: JSON.stringify(userIds),
                    success: function(r){
                        if(r.code == 0){
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
        resetPassword: function () {
            var userIds = getSelectedRows();
            if(userIds == null){
                return ;
            }

            confirm('确定要重置选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/user/resetPassword",
                    contentType: "application/json",
                    data: JSON.stringify(userIds),
                    success: function(r){
                        if(r.code == 0){
                            alert('重置成功', function(){
                                vm.reload();
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        saveOrUpdate: function () {
            console.log(vm.user.enBrand);
            vm.erroMsg1 = '';
            vm.erroMsg3 = '';
            vm.erroMsg4 = '';
            vm.erroMsg5 = '';
            vm.erroMsg6 = '';
            vm.erroMsg7 = '';
            vm.erroMsg9 = '';
            var reg =/\s/;
            var reg2 = /^[\x21-\x2f\x3a-\x40\x5b-\x60\x7B-\x7F]/;
            // if(reg1.test(vm.user.password)){
            //     console.log(111111);
            //     vm.erroMsg4 = '不能以字符开头'
            // }else {
            //     vm.erroMsg4 = ''
            // }
            var reg1 = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,}$/;
            if(!vm.user.username){
                vm.chanUserNameMsg = '账号不能为空'
            }else if(vm.user.username.length>20 || vm.user.username.length<6){
                vm.chanUserNameMsg = '账号长度必须在6到20个字符之间'
            }else if(!vm.user.deptName){
                vm.erroMsg1 = '请选择所属公司'
            }else if(!vm.user.enName){
                vm.chanUserNameMsgE = '英文品牌不能为空'
            }else if(!vm.user.enBrand){
                vm.chanUserNameMsgE1 = '英文品牌不能为空'
            }else if(reg.test(vm.user.enName)){
                vm.chanUserNameMsgE = '英文名称中不能有空格'
            }else if(reg.test(vm.user.enBrand)){
                vm.chanUserNameMsgE1 = '英文品牌中不能有空格'
            }else if(!vm.user.displayName){
                vm.erroMsg3 = '昵称不能为空'
            }else if(!vm.user.password){
                vm.erroMsg4 = '密码不能为空'
            }else if(reg1.test(vm.user.password)){
                vm.erroMsg4 = '密码至少8位，要求必须字母、数字加英文符号（不包含空格）'
            }else if(reg2.test(vm.user.password)){
                // console.log(111111);
                vm.erroMsg4 = '密码不能以字符开头'
            }else if(!vm.user.password1){
                vm.erroMsg5 = '确认密码不能为空'
            }else if(!vm.user.email){
                vm.erroMsg6 = '邮箱不能为空'
            }else if(!vm.user.mobile){
                vm.erroMsg7 = '手机号不能为空'
            }else if(vm.user.roleIdList.length == 0){
                vm.erroMsg9 = '请选择角色'
            }else if(vm.user.password1 != vm.user.password){
                vm.erroMsg5 = '确认密码错误'
            }else {
                var url = vm.user.userId == null ? "sys/user/save" : "sys/user/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.user),
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
            }


        },
        saveOrUpdate1: function () {
            console.log(vm.user.enBrand);
            vm.erroMsg1 = '';
            vm.erroMsg3 = '';
            vm.erroMsg4 = '';
            vm.erroMsg5 = '';
            vm.erroMsg6 = '';
            vm.erroMsg7 = '';
            vm.erroMsg9 = '';
            var reg =/\s/;
            var reg2 = /^[\x21-\x2f\x3a-\x40\x5b-\x60\x7B-\x7F]/;
            // if(reg1.test(vm.user.password)){
            //     console.log(111111);
            //     vm.erroMsg4 = '不能以字符开头'
            // }else {
            //     vm.erroMsg4 = ''
            // }
            var reg1 = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,}$/;
            if(!vm.user.enName){
                vm.chanUserNameMsgE = '英文品牌不能为空'
            }else if(!vm.user.enBrand){
                vm.chanUserNameMsgE1 = '英文品牌不能为空'
            }else if(reg.test(vm.user.enName)){
                vm.chanUserNameMsgE = '英文名称中不能有空格'
            }else if(reg.test(vm.user.enBrand)){
                vm.chanUserNameMsgE1 = '英文品牌中不能有空格'
            }else if(!vm.user.displayName){
                vm.erroMsg3 = '昵称不能为空'
            }else if(!vm.user.email){
                vm.erroMsg6 = '邮箱不能为空'
            }else if(!vm.user.mobile){
                vm.erroMsg7 = '手机号不能为空'
            }else if(vm.user.roleIdList.length == 0){
                vm.erroMsg9 = '请选择角色'
            }else {
                var url = vm.user.userId == null ? "sys/user/save" : "sys/user/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.user),
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
            }


        },
        getUser: function(userId){
            $.get(baseURL + "sys/user/info/"+userId, function(r){
                vm.user = r.user;
                vm.user.password = null;
                vm.getDept();
            });
        },
        getRoleList: function(){
            $.get(baseURL + "sys/role/select", function(r){
                vm.roleList = r.list;
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
                    vm.user.deptId = node[0].deptId;
                    vm.user.deptName = node[0].name;

                    layer.close(index);
                }
            });
        },
        reload: function () {
            vm.showList = true;
            vm.showList1 = true;
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{'username': vm.q.username,'displayName':vm.q.displayName},
                page:page
            }).trigger("reloadGrid");
        }
    }
});