$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'amazon/amazongrantshop/list',
        datatype: "json",
        colModel: [			
			{ label: 'grantShopId', name: 'grantShopId', index: 'grant_shop_id', width: 50, key: true },
			{ label: '店铺名称', name: 'shopName', index: 'shop_name', width: 80 }, 			
			{ label: '店铺账户', name: 'shopAccount', index: 'shop_account', width: 80 }, 			
			{ label: '授权国家', name: 'grantCounty', index: 'grant_county', width: 80 }, 			
			{ label: '国家标识', name: 'countryCode', index: 'country_code', width: 80 }, 			
			{ label: '亚马逊MWS端点', name: 'mwsPoint', index: 'mws_point', width: 80 }, 			
			{ label: 'MarketplaceId', name: 'marketplaceId', index: 'marketplace_id', width: 80 }, 			
			{ label: '所属区域(欧洲0、北美1、远东2）', name: 'region', index: 'region', width: 80 }			
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
		amazonGrantShop: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.amazonGrantShop = {};
		},
		update: function (event) {
			var grantShopId = getSelectedRow();
			if(grantShopId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(grantShopId)
		},
		saveOrUpdate: function (event) {
			var url = vm.amazonGrantShop.grantShopId == null ? "amazon/amazongrantshop/save" : "amazon/amazongrantshop/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.amazonGrantShop),
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
			var grantShopIds = getSelectedRows();
			if(grantShopIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "amazon/amazongrantshop/delete",
                    contentType: "application/json",
				    data: JSON.stringify(grantShopIds),
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
		getInfo: function(grantShopId){
			$.get(baseURL + "amazon/amazongrantshop/info/"+grantShopId, function(r){
                vm.amazonGrantShop = r.amazonGrantShop;
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