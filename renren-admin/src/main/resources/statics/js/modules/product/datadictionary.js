$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'product/datadictionary/list',
        datatype: "json",
        colModel: [			
			{ label: 'dataId', name: 'dataId', index: 'data_id', width: 50, key: true },
			{ label: '数据分类', name: 'dataType', index: 'data_type', width: 80 }, 			
			{ label: '数据标识', name: 'dataNumber', index: 'data_number', width: 80 }, 			
			{ label: '数据内容', name: 'dataContent', index: 'data_content', width: 80 }, 			
			{ label: '数据排序', name: 'dataSort', index: 'data_sort', width: 80 }			
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
		dataDictionary: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.dataDictionary = {};
		},
		update: function (event) {
			var dataId = getSelectedRow();
			if(dataId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(dataId)
		},
		saveOrUpdate: function (event) {
			var url = vm.dataDictionary.dataId == null ? "product/datadictionary/save" : "product/datadictionary/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.dataDictionary),
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
			var dataIds = getSelectedRows();
			if(dataIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "product/datadictionary/delete",
                    contentType: "application/json",
				    data: JSON.stringify(dataIds),
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
		getInfo: function(dataId){
			$.get(baseURL + "product/datadictionary/info/"+dataId, function(r){
                vm.dataDictionary = r.dataDictionary;
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