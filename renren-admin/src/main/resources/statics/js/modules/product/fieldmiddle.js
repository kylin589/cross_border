$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'product/fieldmiddle/list',
        datatype: "json",
        colModel: [			
			{ label: 'middleId', name: 'middleId', index: 'middle_id', width: 50, key: true },
			{ label: '上传id', name: 'uploadId', index: 'upload_id', width: 80 }, 			
			{ label: '模板字段id', name: 'fieldId', index: 'field_id', width: 80 }, 			
			{ label: '字段名称', name: 'fieldName', index: 'field_name', width: 80 }, 			
			{ label: '字段显示名称', name: 'fieldDisplayName', index: 'field_display_name', width: 80 }, 			
			{ label: '字段值', name: 'value', index: 'value', width: 80 }			
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
		fieldMiddle: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.fieldMiddle = {};
		},
		update: function (event) {
			var middleId = getSelectedRow();
			if(middleId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(middleId)
		},
		saveOrUpdate: function (event) {
			var url = vm.fieldMiddle.middleId == null ? "product/fieldmiddle/save" : "product/fieldmiddle/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.fieldMiddle),
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
			var middleIds = getSelectedRows();
			if(middleIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "product/fieldmiddle/delete",
                    contentType: "application/json",
				    data: JSON.stringify(middleIds),
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
		getInfo: function(middleId){
			$.get(baseURL + "product/fieldmiddle/info/"+middleId, function(r){
                vm.fieldMiddle = r.fieldMiddle;
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