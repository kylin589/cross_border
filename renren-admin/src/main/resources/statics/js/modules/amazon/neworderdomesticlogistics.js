$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'amazon/neworderdomesticlogistics/list',
        datatype: "json",
        colModel: [			
			{ label: 'domesticLogisticsId', name: 'domesticLogisticsId', index: 'domestic_logistics_id', width: 50, key: true },
			{ label: '订单id', name: 'orderId', index: 'order_id', width: 80 }, 			
			{ label: '订单商品id', name: 'itemId', index: 'item_id', width: 80 }, 			
			{ label: '采购价', name: 'price', index: 'price', width: 80 }, 			
			{ label: '运单号', name: 'waybill', index: 'waybill', width: 80 }, 			
			{ label: '物流公司', name: 'logisticsCompany', index: 'logistics_company', width: 80 }, 			
			{ label: '发货日期', name: 'issuanceDate', index: 'issuance_date', width: 80 }, 			
			{ label: '是否入库', name: 'state', index: 'state', width: 80 }, 			
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
		newOrderDomesticLogistics: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.newOrderDomesticLogistics = {};
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
			var url = vm.newOrderDomesticLogistics.domesticLogisticsId == null ? "amazon/neworderdomesticlogistics/save" : "amazon/neworderdomesticlogistics/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.newOrderDomesticLogistics),
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
				    url: baseURL + "amazon/neworderdomesticlogistics/delete",
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
			$.get(baseURL + "amazon/neworderdomesticlogistics/info/"+domesticLogisticsId, function(r){
                vm.newOrderDomesticLogistics = r.newOrderDomesticLogistics;
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