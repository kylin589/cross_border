$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'product/freightprice/list',
        datatype: "json",
        colModel: [			
			{ label: 'freightPriceId', name: 'freightPriceId', index: 'freight_price_id', width: 50, key: true },
			{ label: '国家代码', name: 'countryCode', index: 'country_code', width: 80 }, 			
			{ label: '类型（大包、小包）', name: 'type', index: 'type', width: 80 }, 			
			{ label: '单价', name: 'price', index: 'price', width: 80 }, 			
			{ label: '修改时间', name: 'updateTime', index: 'update_time', width: 80 }			
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
		freightPrice: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.freightPrice = {};
		},
		update: function (event) {
			var freightPriceId = getSelectedRow();
			if(freightPriceId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(freightPriceId)
		},
		saveOrUpdate: function (event) {
			var url = vm.freightPrice.freightPriceId == null ? "product/freightprice/save" : "product/freightprice/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.freightPrice),
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
			var freightPriceIds = getSelectedRows();
			if(freightPriceIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "product/freightprice/delete",
                    contentType: "application/json",
				    data: JSON.stringify(freightPriceIds),
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
		getInfo: function(freightPriceId){
			$.get(baseURL + "product/freightprice/info/"+freightPriceId, function(r){
                vm.freightPrice = r.freightPrice;
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