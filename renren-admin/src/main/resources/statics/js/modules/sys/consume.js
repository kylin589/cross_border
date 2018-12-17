$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/consume/list',
        datatype: "json",
        colModel: [			
			{ label: 'companyConsumeId', name: 'companyConsumeId', index: 'company_consume_id', width: 50, key: true },
			{ label: '公司id', name: 'deptId', index: 'dept_id', width: 80 }, 			
			{ label: '公司名称', name: 'deptName', index: 'dept_name', width: 80 }, 			
			{ label: '操作人id', name: 'userId', index: 'user_id', width: 80 }, 			
			{ label: '操作人', name: 'userName', index: 'user_name', width: 80 }, 			
			{ label: '类型（服务费、物流费）', name: 'type', index: 'type', width: 80 }, 			
			{ label: '订单id', name: 'orderId', index: 'order_id', width: 80 }, 			
			{ label: '金额', name: 'money', index: 'money', width: 80 }, 			
			{ label: '消费前余额', name: 'beforeBalance', index: 'before_balance', width: 80 }, 			
			{ label: '消费后余额', name: 'afterBalance', index: 'after_balance', width: 80 }, 			
			{ label: '运单号', name: 'abroadWaybill', index: 'abroad_waybill', width: 80 }, 			
			{ label: '操作时间', name: 'createTime', index: 'create_time', width: 80 }			
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
		consume: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.consume = {};
		},
		update: function (event) {
			var companyConsumeId = getSelectedRow();
			if(companyConsumeId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(companyConsumeId)
		},
		saveOrUpdate: function (event) {
			var url = vm.consume.companyConsumeId == null ? "sys/consume/save" : "sys/consume/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.consume),
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
			var companyConsumeIds = getSelectedRows();
			if(companyConsumeIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "sys/consume/delete",
                    contentType: "application/json",
				    data: JSON.stringify(companyConsumeIds),
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
		getInfo: function(companyConsumeId){
			$.get(baseURL + "sys/consume/info/"+companyConsumeId, function(r){
                vm.consume = r.consume;
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