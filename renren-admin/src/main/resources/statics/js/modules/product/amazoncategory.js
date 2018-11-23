$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'product/amazoncategory/list',
        datatype: "json",
        colModel: [			
			{ label: 'amazonCategoryId', name: 'amazonCategoryId', index: 'amazon_category_id', width: 50, key: true },
			{ label: '上级id', name: 'parentId', index: 'parent_id', width: 80 }, 			
			{ label: '类目名称', name: 'categoryName', index: 'category_name', width: 80 }, 			
			{ label: '展示名称（中文+英文）', name: 'displayName', index: 'display_name', width: 80 }, 			
			{ label: '英国节点id', name: 'nodeIdUk', index: 'node_id_uk', width: 80 }, 			
			{ label: '德国节点id', name: 'nodeIdDe', index: 'node_id_de', width: 80 }, 			
			{ label: '法国节点id', name: 'nodeIdFr', index: 'node_id_fr', width: 80 }, 			
			{ label: '意大利节点id', name: 'nodeIdIt', index: 'node_id_it', width: 80 }, 			
			{ label: '西班牙节点id', name: 'nodeIdEs', index: 'node_id_es', width: 80 }, 			
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
		amazonCategory: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.amazonCategory = {};
		},
		update: function (event) {
			var amazonCategoryId = getSelectedRow();
			if(amazonCategoryId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(amazonCategoryId)
		},
		saveOrUpdate: function (event) {
			var url = vm.amazonCategory.amazonCategoryId == null ? "product/amazoncategory/save" : "product/amazoncategory/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.amazonCategory),
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
			var amazonCategoryIds = getSelectedRows();
			if(amazonCategoryIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "product/amazoncategory/delete",
                    contentType: "application/json",
				    data: JSON.stringify(amazonCategoryIds),
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
		getInfo: function(amazonCategoryId){
			$.get(baseURL + "product/amazoncategory/info/"+amazonCategoryId, function(r){
                vm.amazonCategory = r.amazonCategory;
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