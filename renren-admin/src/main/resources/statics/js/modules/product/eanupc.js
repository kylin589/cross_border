$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'product/eanupc/list',
        datatype: "json",
        colModel: [			
			{ label: 'eanUpcId', name: 'eanUpcId', index: 'ean_upc_id', width: 50, key: true },
			{ label: '码', name: 'code', index: 'code', width: 80 }, 			
			{ label: '状态（0：未使用，1：已使用）', name: 'state', index: 'state', width: 80 }, 			
			{ label: '产品id', name: 'productId', index: 'product_id', width: 80 }, 			
			{ label: '添加时间', name: 'createTime', index: 'create_time', width: 80 }			
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
		eanUpc: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.eanUpc = {};
		},
		update: function (event) {
			var eanUpcId = getSelectedRow();
			if(eanUpcId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(eanUpcId)
		},
		saveOrUpdate: function (event) {
			var url = vm.eanUpc.eanUpcId == null ? "product/eanupc/save" : "product/eanupc/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.eanUpc),
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
			var eanUpcIds = getSelectedRows();
			if(eanUpcIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "product/eanupc/delete",
                    contentType: "application/json",
				    data: JSON.stringify(eanUpcIds),
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
		getInfo: function(eanUpcId){
			$.get(baseURL + "product/eanupc/info/"+eanUpcId, function(r){
                vm.eanUpc = r.eanUpc;
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