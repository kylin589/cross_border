$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'product/introduction/list',
        datatype: "json",
        colModel: [			
			{ label: 'introductionId', name: 'introductionId', index: 'introduction_id', width: 50, key: true },
			{ label: '国家代码', name: 'country', index: 'country', width: 80 }, 			
			{ label: '产品标题', name: 'productTitle', index: 'product_title', width: 80 }, 			
			{ label: '关键词', name: 'keyWord', index: 'key_word', width: 80 }, 			
			{ label: '要点说明', name: 'keyPoints', index: 'key_points', width: 80 }, 			
			{ label: '产品描述', name: 'productDescription', index: 'product_description', width: 80 }			
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
		introduction: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.introduction = {};
		},
		update: function (event) {
			var introductionId = getSelectedRow();
			if(introductionId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(introductionId)
		},
		saveOrUpdate: function (event) {
			var url = vm.introduction.introductionId == null ? "product/introduction/save" : "product/introduction/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.introduction),
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
			var introductionIds = getSelectedRows();
			if(introductionIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "product/introduction/delete",
                    contentType: "application/json",
				    data: JSON.stringify(introductionIds),
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
		getInfo: function(introductionId){
			$.get(baseURL + "product/introduction/info/"+introductionId, function(r){
                vm.introduction = r.introduction;
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