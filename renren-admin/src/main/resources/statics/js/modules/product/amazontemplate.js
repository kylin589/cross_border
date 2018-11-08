$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'product/amazontemplate/list',
        datatype: "json",
        colModel: [			
			{ label: 'amazonTemplateId', name: 'amazonTemplateId', index: 'amazon_template_id', width: 50, key: true },
			{ label: '上级id', name: 'parentId', index: 'parent_id', width: 80 }, 			
			{ label: '模板名称', name: 'categoryName', index: 'category_name', width: 80 }, 			
			{ label: '展示名称（中文+英文）', name: 'displayName', index: 'display_name', width: 80 }			
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
		amazonTemplate: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.amazonTemplate = {};
		},
		update: function (event) {
			var amazonTemplateId = getSelectedRow();
			if(amazonTemplateId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(amazonTemplateId)
		},
		saveOrUpdate: function (event) {
			var url = vm.amazonTemplate.amazonTemplateId == null ? "product/amazontemplate/save" : "product/amazontemplate/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.amazonTemplate),
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
			var amazonTemplateIds = getSelectedRows();
			if(amazonTemplateIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "product/amazontemplate/delete",
                    contentType: "application/json",
				    data: JSON.stringify(amazonTemplateIds),
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
		getInfo: function(amazonTemplateId){
			$.get(baseURL + "product/amazontemplate/info/"+amazonTemplateId, function(r){
                vm.amazonTemplate = r.amazonTemplate;
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