$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'product/claim/list',
        datatype: "json",
        colModel: [			
			{ label: 'productClaimId', name: 'productClaimId', index: 'product_claim_id', width: 50, key: true },
			{ label: '认领产品id', name: 'productId', index: 'product_id', width: 80 }, 			
			{ label: '产品来源公司id', name: 'fromDeptId', index: 'from_dept_id', width: 80 }, 			
			{ label: '产品来源公司', name: 'fromDeptName', index: 'from_dept_name', width: 80 }, 			
			{ label: '产品创建人id', name: 'fromUserId', index: 'from_user_id', width: 80 }, 			
			{ label: '产品创建人', name: 'fromUserName', index: 'from_user_name', width: 80 }, 			
			{ label: '产品来源公司id', name: 'toDeptId', index: 'to_dept_id', width: 80 }, 			
			{ label: '产品来源公司', name: 'toDeptName', index: 'to_dept_name', width: 80 }, 			
			{ label: '产品创建人id', name: 'toUserId', index: 'to_user_id', width: 80 }, 			
			{ label: '产品创建人', name: 'toUserName', index: 'to_user_name', width: 80 }, 			
			{ label: '操作人公司id', name: 'operatorDeptId', index: 'operator_dept_id', width: 80 }, 			
			{ label: '操作人id', name: 'operatorId', index: 'operator_id', width: 80 }, 			
			{ label: '操作人', name: 'operatorName', index: 'operator_name', width: 80 }, 			
			{ label: '操作时间', name: 'operatorTime', index: 'operator_time', width: 80 }			
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
		claim: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.claim = {};
		},
		update: function (event) {
			var productClaimId = getSelectedRow();
			if(productClaimId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(productClaimId)
		},
		saveOrUpdate: function (event) {
			var url = vm.claim.productClaimId == null ? "product/claim/save" : "product/claim/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.claim),
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
			var productClaimIds = getSelectedRows();
			if(productClaimIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "product/claim/delete",
                    contentType: "application/json",
				    data: JSON.stringify(productClaimIds),
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
		getInfo: function(productClaimId){
			$.get(baseURL + "product/claim/info/"+productClaimId, function(r){
                vm.claim = r.claim;
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