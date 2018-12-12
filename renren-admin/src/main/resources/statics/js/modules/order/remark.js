$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'order/remark/list',
        datatype: "json",
        colModel: [			
			{ label: 'remarkId', name: 'remarkId', index: 'remark_id', width: 50, key: true },
			{ label: '订单id', name: 'orderId', index: 'order_id', width: 80 }, 			
			{ label: '备注', name: 'remark', index: 'remark', width: 80 }, 			
			{ label: '操作人', name: 'userId', index: 'user_id', width: 80 }, 			
			{ label: '操作时间', name: 'updateTime', index: 'update_time', width: 80 }, 			
			{ label: '类型（remark，log）', name: 'type', index: 'type', width: 80 }, 			
			{ label: '备注类型（内部备注、收件人地址异常.......）', name: 'remarkType', index: 'remark_type', width: 80 }			
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
		remark: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.remark = {};
		},
		update: function (event) {
			var remarkId = getSelectedRow();
			if(remarkId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(remarkId)
		},
		saveOrUpdate: function (event) {
			var url = vm.remark.remarkId == null ? "order/remark/save" : "order/remark/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.remark),
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
			var remarkIds = getSelectedRows();
			if(remarkIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "order/remark/delete",
                    contentType: "application/json",
				    data: JSON.stringify(remarkIds),
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
		getInfo: function(remarkId){
			$.get(baseURL + "order/remark/info/"+remarkId, function(r){
                vm.remark = r.remark;
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