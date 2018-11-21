$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'product/order/list',
        datatype: "json",
        colModel: [			
			{ label: 'orderId', name: 'orderId', index: 'order_id', width: 50, key: true },
			{ label: '亚马逊订单id', name: 'amazonOrderId', index: 'amazon_order_id', width: 80 }, 			
			{ label: '购买日期', name: 'buyDate', index: 'buy_date', width: 80 }, 			
			{ label: 'amazon订单状态', name: 'amazonOrderState', index: 'amazon_order_state', width: 80 }, 			
			{ label: '内部订单标识', name: 'orderStatus', index: 'order_status', width: 80 }, 			
			{ label: '店铺名称(店铺+国家）', name: 'shopName', index: 'shop_name', width: 80 }, 			
			{ label: '产品sku', name: 'productSku', index: 'product_sku', width: 80 }, 			
			{ label: '产品asin码', name: 'productAsin', index: 'product_asin', width: 80 }, 			
			{ label: '订单数量', name: 'orderNumber', index: 'order_number', width: 80 }, 			
			{ label: '订单金额', name: 'orderMoney', index: 'order_money', width: 80 }, 			
			{ label: 'Amazon佣金', name: 'amazonCommission', index: 'amazon_commission', width: 80 }, 			
			{ label: '到账金额', name: 'accountMoney', index: 'account_money', width: 80 }, 			
			{ label: '国际运费', name: 'interFreight', index: 'inter_freight', width: 80 }, 			
			{ label: '平台佣金', name: 'platformCommissions', index: 'platform_commissions', width: 80 }, 			
			{ label: '利润', name: 'orderProfit', index: 'order_profit', width: 80 }, 			
			{ label: '退货费用', name: 'returnCost', index: 'return_cost', width: 80 }			
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
		order: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.order = {};
		},
		update: function (event) {
			var orderId = getSelectedRow();
			if(orderId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(orderId)
		},
		saveOrUpdate: function (event) {
			var url = vm.order.orderId == null ? "product/order/save" : "product/order/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.order),
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
			var orderIds = getSelectedRows();
			if(orderIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "product/order/delete",
                    contentType: "application/json",
				    data: JSON.stringify(orderIds),
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
		getInfo: function(orderId){
			$.get(baseURL + "product/order/info/"+orderId, function(r){
                vm.order = r.order;
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