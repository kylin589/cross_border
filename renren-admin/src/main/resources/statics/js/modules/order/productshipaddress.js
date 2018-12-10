$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'order/productshipaddress/list',
        datatype: "json",
        colModel: [			
			{ label: 'shipAddressId', name: 'shipAddressId', index: 'ship_address_id', width: 50, key: true },
			{ label: '订单id', name: 'orderId', index: 'order_id', width: 80 }, 			
			{ label: '收件人', name: 'shipName', index: 'ship_name', width: 80 }, 			
			{ label: '收件人地址（国家）', name: 'shipCountry', index: 'ship_country', width: 80 }, 			
			{ label: '收件人电话', name: 'shipTel', index: 'ship_tel', width: 80 }, 			
			{ label: '收件人邮编', name: 'shipZip', index: 'ship_zip', width: 80 }, 			
			{ label: '州、区域', name: 'shipRegion', index: 'ship_region', width: 80 }, 			
			{ label: '城市', name: 'shipCity', index: 'ship_city', width: 80 }, 			
			{ label: '区县', name: 'shipCounty', index: 'ship_county', width: 80 }, 			
			{ label: '区', name: 'shipDistrict', index: 'ship_district', width: 80 }, 			
			{ label: '街道', name: 'shipAddressLine', index: 'ship_address_line', width: 80 }			
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
		productShipAddress: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.productShipAddress = {};
		},
		update: function (event) {
			var shipAddressId = getSelectedRow();
			if(shipAddressId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(shipAddressId)
		},
		saveOrUpdate: function (event) {
			var url = vm.productShipAddress.shipAddressId == null ? "order/productshipaddress/save" : "order/productshipaddress/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.productShipAddress),
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
			var shipAddressIds = getSelectedRows();
			if(shipAddressIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "order/productshipaddress/delete",
                    contentType: "application/json",
				    data: JSON.stringify(shipAddressIds),
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
		getInfo: function(shipAddressId){
			$.get(baseURL + "order/productshipaddress/info/"+shipAddressId, function(r){
                vm.productShipAddress = r.productShipAddress;
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