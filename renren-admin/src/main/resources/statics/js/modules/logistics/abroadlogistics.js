$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'logistics/abroadlogistics/list',
        datatype: "json",
        colModel: [			
			{ label: 'abroadLogisticsId', name: 'abroadLogisticsId', index: 'abroad_logistics_id', width: 50, key: true },
			{ label: '订单id', name: 'orderId', index: 'order_id', width: 80 }, 			
			{ label: '国际物流单号', name: 'abroadWaybill', index: 'abroad_waybill', width: 80 }, 			
			{ label: '国际追踪号', name: 'trackWaybill', index: 'track_waybill', width: 80 }, 			
			{ label: '国际物流状态编码', name: 'status', index: 'status', width: 80 }, 			
			{ label: '国际物流状态', name: 'state', index: 'state', width: 80 }, 			
			{ label: '国际运费', name: 'interFreight', index: 'inter_freight', width: 80 }, 			
			{ label: '是否同步（默认0：没有  1：有）', name: 'isSynchronization', index: 'is_synchronization', width: 80 }			
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
		abroadLogistics: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.abroadLogistics = {};
		},
		update: function (event) {
			var abroadLogisticsId = getSelectedRow();
			if(abroadLogisticsId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(abroadLogisticsId)
		},
		saveOrUpdate: function (event) {
			var url = vm.abroadLogistics.abroadLogisticsId == null ? "logistics/abroadlogistics/save" : "logistics/abroadlogistics/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.abroadLogistics),
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
			var abroadLogisticsIds = getSelectedRows();
			if(abroadLogisticsIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "logistics/abroadlogistics/delete",
                    contentType: "application/json",
				    data: JSON.stringify(abroadLogisticsIds),
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
		getInfo: function(abroadLogisticsId){
			$.get(baseURL + "logistics/abroadlogistics/info/"+abroadLogisticsId, function(r){
                vm.abroadLogistics = r.abroadLogistics;
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