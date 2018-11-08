$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'product/variantsinfo/list',
        datatype: "json",
        colModel: [			
			{ label: 'variantId', name: 'variantId', index: 'variant_id', width: 50, key: true },
			{ label: '变体排序', name: 'variantSort', index: 'variant_sort', width: 80 }, 			
			{ label: '变体组合', name: 'variantCombination', index: 'variant_combination', width: 80 }, 			
			{ label: '变体sku修正', name: 'variantSku', index: 'variant_sku', width: 80 }, 			
			{ label: '加价（人民币）', name: 'variantAddPrice', index: 'variant_add_price', width: 80 }, 			
			{ label: '变体库存', name: 'variantStock', index: 'variant_stock', width: 80 }, 			
			{ label: 'ean/puc', name: 'eanCode', index: 'ean_code', width: 80 }, 			
			{ label: '变体图片路径（按顺序以逗号隔开）', name: 'imageUrl', index: 'image_url', width: 80 }			
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
		variantsInfo: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.variantsInfo = {};
		},
		update: function (event) {
			var variantId = getSelectedRow();
			if(variantId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(variantId)
		},
		saveOrUpdate: function (event) {
			var url = vm.variantsInfo.variantId == null ? "product/variantsinfo/save" : "product/variantsinfo/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.variantsInfo),
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
			var variantIds = getSelectedRows();
			if(variantIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "product/variantsinfo/delete",
                    contentType: "application/json",
				    data: JSON.stringify(variantIds),
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
		getInfo: function(variantId){
			$.get(baseURL + "product/variantsinfo/info/"+variantId, function(r){
                vm.variantsInfo = r.variantsInfo;
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