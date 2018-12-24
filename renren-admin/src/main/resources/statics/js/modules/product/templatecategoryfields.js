$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'product/templatecategoryfields/list',
        datatype: "json",
        colModel: [			
			{ label: 'fieldId', name: 'fieldId', index: 'field_id', width: 50, key: true },
			{ label: '模板id', name: 'templateId', index: 'template_id', width: 80 }, 			
			{ label: '字段名', name: 'fieldName', index: 'field_name', width: 80 }, 			
			{ label: '显示名', name: 'fieldDisplayName', index: 'field_display_name', width: 80 }, 			
			{ label: '是否自定义（0：可以自己输入，1：不可以自己输入）', name: 'isCustom', index: 'is_custom', width: 80 }			
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
		templateCategoryFields: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.templateCategoryFields = {};
		},
		update: function (event) {
			var fieldId = getSelectedRow();
			if(fieldId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(fieldId)
		},
		saveOrUpdate: function (event) {
			var url = vm.templateCategoryFields.fieldId == null ? "product/templatecategoryfields/save" : "product/templatecategoryfields/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.templateCategoryFields),
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
			var fieldIds = getSelectedRows();
			if(fieldIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "product/templatecategoryfields/delete",
                    contentType: "application/json",
				    data: JSON.stringify(fieldIds),
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
		getInfo: function(fieldId){
			$.get(baseURL + "product/templatecategoryfields/info/"+fieldId, function(r){
                vm.templateCategoryFields = r.templateCategoryFields;
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