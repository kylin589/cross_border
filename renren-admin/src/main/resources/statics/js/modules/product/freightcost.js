$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'product/freightcost/list',
        datatype: "json",
        colModel: [			
			{ label: 'freightCostId', name: 'freightCostId', index: 'freight_cost_id', width: 50, key: true },
			{ label: '运费', name: 'freight', index: 'freight', width: 80 }, 			
			{ label: '售价', name: 'price', index: 'price', width: 80 }, 			
			{ label: '外币', name: 'foreignCurrency', index: 'foreign_currency', width: 80 }, 			
			{ label: '优化', name: 'optimization', index: 'optimization', width: 80 }, 			
			{ label: '最终售价', name: 'finalPrice', index: 'final_price', width: 80 }, 			
			{ label: '利润', name: 'profit', index: 'profit', width: 80 }			
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
		freightCost: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.freightCost = {};
		},
		update: function (event) {
			var freightCostId = getSelectedRow();
			if(freightCostId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(freightCostId)
		},
		saveOrUpdate: function (event) {
			var url = vm.freightCost.freightCostId == null ? "product/freightcost/save" : "product/freightcost/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.freightCost),
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
			var freightCostIds = getSelectedRows();
			if(freightCostIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "product/freightcost/delete",
                    contentType: "application/json",
				    data: JSON.stringify(freightCostIds),
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
		getInfo: function(freightCostId){
			$.get(baseURL + "product/freightcost/info/"+freightCostId, function(r){
                vm.freightCost = r.freightCost;
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