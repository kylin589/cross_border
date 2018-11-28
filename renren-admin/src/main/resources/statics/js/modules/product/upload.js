$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'product/upload/list',
        datatype: "json",
        colModel: [			
			{ label: 'uploadId', name: 'uploadId', index: 'upload_id', width: 50, key: true },
			{ label: '产品id', name: 'productId', index: 'product_id', width: 80 }, 			
			{ label: '主图片url', name: 'mainUrl', index: 'main_url', width: 80 },
			{ label: '店铺id', name: 'grantShopId', index: 'grant_shop_id', width: 80 },
			{ label: '店铺', name: 'grantShop', index: 'grant_shop', width: 80 },
			{ label: '操作类型（默认0：上传   1：修改）', name: 'operateType', index: 'operate_type', width: 80 },
			{ label: '操作项（0:产品基本信息1:关系 2:图片3:库存4:价格）以逗号隔开', name: 'operateItem', index: 'operate_item', width: 80 },
			{ label: '亚马逊分类id', name: 'amazonCategoryId', index: 'amazon_category_id', width: 80 },
			{ label: '亚马逊分类', name: 'amazonCategory', index: 'amazon_category', width: 80 },
			{ label: '亚马逊模板id', name: 'amazonTemplateId', index: 'amazon_template_id', width: 80 }, 			
			{ label: '亚马逊模板', name: 'amazonTemplate', index: 'amazon_template', width: 80 },
			{ label: '是否有分类属性（默认0：没有  1：有）', name: 'isAttribute', index: 'is_attribute', width: 80 },
			{ label: '分类属性id', name: 'attributeId', index: 'attribute_id', width: 80 }, 			
			{ label: '(默认0：正在上传1：上传成功2：上传失败)', name: 'uploadState', index: 'upload_state', width: 80 }, 			
			{ label: '返回错误代码（上传失败）', name: 'returnCode', index: 'return_code', width: 80 }, 			
			{ label: '返回错误（上传失败）', name: 'returnError', index: 'return_error', width: 80 }, 			
			{ label: '上传时间', name: 'uploadTime', index: 'upload_time', width: 80 },
			{ label: '更新', name: 'updateTime', index: 'update_time', width: 80 }
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
		upload: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.upload = {};
		},
		update: function (event) {
			var uploadId = getSelectedRow();
			if(uploadId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(uploadId)
		},
		saveOrUpdate: function (event) {
			var url = vm.upload.uploadId == null ? "product/upload/save" : "product/upload/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.upload),
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
			var uploadIds = getSelectedRows();
			if(uploadIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "product/upload/delete",
                    contentType: "application/json",
				    data: JSON.stringify(uploadIds),
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
		getInfo: function(uploadId){
			$.get(baseURL + "product/upload/info/"+uploadId, function(r){
                vm.upload = r.upload;
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