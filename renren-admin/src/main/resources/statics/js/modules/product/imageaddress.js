$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'product/imageaddress/list',
        datatype: "json",
        colModel: [			
			{ label: 'imageId', name: 'imageId', index: 'image_id', width: 50, key: true },
			{ label: '图片路径', name: 'imageUrl', index: 'image_url', width: 80 }, 			
			{ label: '关联产品id', name: 'productId', index: 'product_id', width: 80 }, 			
			{ label: '软删（1：删除）', name: 'isDeleted', index: 'is_deleted', width: 80 }, 			
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80 }, 			
			{ label: '创建用户id', name: 'createUserId', index: 'create_user_id', width: 80 }, 			
			{ label: '最后操作时间', name: 'lastOperationTime', index: 'last_operation_time', width: 80 }, 			
			{ label: '最后操作人id', name: 'lastOperationUserId', index: 'last_operation_user_id', width: 80 }			
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
		imageAddress: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.imageAddress = {};
		},
		update: function (event) {
			var imageId = getSelectedRow();
			if(imageId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(imageId)
		},
		saveOrUpdate: function (event) {
			var url = vm.imageAddress.imageId == null ? "product/imageaddress/save" : "product/imageaddress/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.imageAddress),
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
			var imageIds = getSelectedRows();
			if(imageIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "product/imageaddress/delete",
                    contentType: "application/json",
				    data: JSON.stringify(imageIds),
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
		getInfo: function(imageId){
			$.get(baseURL + "product/imageaddress/info/"+imageId, function(r){
                vm.imageAddress = r.imageAddress;
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