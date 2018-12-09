$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'logistics/domesticlogistics/list',
        datatype: "json",
        colModel: [			
			{ label: 'domesticLogisticsId', name: 'domesticLogisticsId', index: 'domestic_logistics_id', width: 50, key: true },
			{ label: '订单id', name: 'orderId', index: 'order_id', width: 80 }, 			
			{ label: '运单号', name: 'waybill', index: 'waybill', width: 80 }, 			
			{ label: '物流公司', name: 'logisticsCompany', index: 'logistics_company', width: 80 }, 			
			{ label: '发货日期', name: 'issuanceDate', index: 'issuance_date', width: 80 }, 			
			{ label: '', name: 'userId', index: 'user_id', width: 80 }, 			
			{ label: '', name: 'deptId', index: 'dept_id', width: 80 }, 			
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80 }			
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
		domesticLogistics: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.domesticLogistics = {};
		},
		update: function (event) {
			var domesticLogisticsId = getSelectedRow();
			if(domesticLogisticsId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(domesticLogisticsId)
		},
		saveOrUpdate: function (event) {
			var url = vm.domesticLogistics.domesticLogisticsId == null ? "logistics/domesticlogistics/save" : "logistics/domesticlogistics/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.domesticLogistics),
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
			var domesticLogisticsIds = getSelectedRows();
			if(domesticLogisticsIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "logistics/domesticlogistics/delete",
                    contentType: "application/json",
				    data: JSON.stringify(domesticLogisticsIds),
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
		getInfo: function(domesticLogisticsId){
			$.get(baseURL + "logistics/domesticlogistics/info/"+domesticLogisticsId, function(r){
                vm.domesticLogistics = r.domesticLogistics;
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