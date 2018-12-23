$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'product/templatefieldvalue/list',
        datatype: "json",
        colModel: [			
			{ label: 'valueId', name: 'valueId', index: 'value_id', width: 50, key: true },
			{ label: '字段id', name: 'fieldId', index: 'field_id', width: 80 }, 			
			{ label: '中国值', name: 'cnValue', index: 'cn_value', width: 80 }, 			
			{ label: '英国值', name: 'ukValue', index: 'uk_value', width: 80 }, 			
			{ label: '法国值', name: 'frValue', index: 'fr_value', width: 80 }, 			
			{ label: '德国值', name: 'deValue', index: 'de_value', width: 80 }, 			
			{ label: '意大利值', name: 'itValue', index: 'it_value', width: 80 }, 			
			{ label: '西班牙值', name: 'esValue', index: 'es_value', width: 80 }, 			
			{ label: '日本值', name: 'jpValue', index: 'jp_value', width: 80 }, 			
			{ label: '澳大利亚值', name: 'auValue', index: 'au_value', width: 80 }, 			
			{ label: '加拿大值', name: 'caValue', index: 'ca_value', width: 80 }, 			
			{ label: '美国值', name: 'usValue', index: 'us_value', width: 80 }, 			
			{ label: '墨西哥值', name: 'mxValue', index: 'mx_value', width: 80 }			
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
		templateFieldValue: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.templateFieldValue = {};
		},
		update: function (event) {
			var valueId = getSelectedRow();
			if(valueId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(valueId)
		},
		saveOrUpdate: function (event) {
			var url = vm.templateFieldValue.valueId == null ? "product/templatefieldvalue/save" : "product/templatefieldvalue/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.templateFieldValue),
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
			var valueIds = getSelectedRows();
			if(valueIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "product/templatefieldvalue/delete",
                    contentType: "application/json",
				    data: JSON.stringify(valueIds),
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
		getInfo: function(valueId){
			$.get(baseURL + "product/templatefieldvalue/info/"+valueId, function(r){
                vm.templateFieldValue = r.templateFieldValue;
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