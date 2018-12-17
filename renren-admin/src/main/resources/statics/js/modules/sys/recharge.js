$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/recharge/list',
        datatype: "json",
        colModel: [			
			{ label: 'companyRechargeId', name: 'companyRechargeId', index: 'company_recharge_id', width: 50, key: true },
			{ label: '操作人', name: 'userName', index: 'user_name', width: 80 }, 			
			{ label: '类型', name: 'type', index: 'type', width: 80 }, 			
			{ label: '余额', name: 'balance', index: 'balance', width: 80 }, 			
			{ label: '充值时间', name: 'rechargeTime', index: 'recharge_time', width: 80 }, 			
			{ label: '备注', name: 'remark', index: 'remark', width: 80 }, 			
			{ label: '用户id', name: 'userId', index: 'user_id', width: 80 }, 			
			{ label: '公司id', name: 'deptId', index: 'dept_id', width: 80 }			
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

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		recharge: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.recharge = {};
		},
		update: function (event) {
			var companyRechargeId = getSelectedRow();
			if(companyRechargeId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(companyRechargeId)
		},
		saveOrUpdate: function (event) {
			var url = vm.recharge.companyRechargeId == null ? "sys/recharge/save" : "sys/recharge/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.recharge),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		del: function (event) {
			var companyRechargeIds = getSelectedRows();
			if(companyRechargeIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "sys/recharge/delete",
                    contentType: "application/json",
				    data: JSON.stringify(companyRechargeIds),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo: function(companyRechargeId){
			$.get(baseURL + "sys/recharge/info/"+companyRechargeId, function(r){
                vm.recharge = r.recharge;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});