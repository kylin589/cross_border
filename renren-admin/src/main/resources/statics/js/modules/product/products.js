$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'product/products/list',
        datatype: "json",
        colModel: [			
			{ label: 'productId', name: 'productId', index: 'product_id', width: 50, key: true },
			{ label: '一级分类id', name: 'categoryOneId', index: 'category_one_id', width: 80 }, 			
			{ label: '二级分类id', name: 'categoryTwoId', index: 'category_two_id', width: 80 }, 			
			{ label: '三级分类id', name: 'categoryThreeId', index: 'category_three_id', width: 80 }, 			
			{ label: '产品内部分类', name: 'productCategory', index: 'product_category', width: 80 }, 			
			{ label: '审核状态标识', name: 'auditStatus', index: 'audit_status', width: 80 }, 			
			{ label: '上架状态标识', name: 'shelveStatus', index: 'shelve_status', width: 80 }, 			
			{ label: '产品类型标识', name: 'productType', index: 'product_type', width: 80 }, 			
			{ label: '主图片id', name: 'mainImageId', index: 'main_image_id', width: 80 }, 			
			{ label: '厂商名称', name: 'producerName', index: 'producer_name', width: 80 }, 			
			{ label: '品牌名称', name: 'brandName', index: 'brand_name', width: 80 }, 			
			{ label: '厂商编号', name: 'manufacturerNumber', index: 'manufacturer_number', width: 80 }, 			
			{ label: '内部sku', name: 'productSku', index: 'product_sku', width: 80 }, 			
			{ label: '产品来源', name: 'productSource', index: 'product_source', width: 80 }, 			
			{ label: '来源地址', name: 'sellerLink', index: 'seller_link', width: 80 }, 			
			{ label: '附加备注', name: 'productRemark', index: 'product_remark', width: 80 }, 			
			{ label: 'ean码', name: 'eanCode', index: 'ean_code', width: 80 }, 			
			{ label: 'upc码', name: 'upcCode', index: 'upc_code', width: 80 }, 			
			{ label: '采购价格', name: 'purchasePrice', index: 'purchase_price', width: 80 }, 			
			{ label: '产品重量', name: 'productWeight', index: 'product_weight', width: 80 }, 			
			{ label: '产品长度', name: 'productLength', index: 'product_length', width: 80 }, 			
			{ label: '产品宽度', name: 'productWide', index: 'product_wide', width: 80 }, 			
			{ label: '产品高度', name: 'productHeight', index: 'product_height', width: 80 }, 			
			{ label: '国内运费', name: 'domesticFreight', index: 'domestic_freight', width: 80 }, 			
			{ label: '折扣系数', name: 'discount', index: 'discount', width: 80 }, 			
			{ label: '美国运费id', name: 'americanFreight', index: 'american_freight', width: 80 }, 			
			{ label: '加拿大运费id', name: 'canadaFreight', index: 'canada_freight', width: 80 }, 			
			{ label: '墨西哥运费id', name: 'mexicoFreight', index: 'mexico_freight', width: 80 }, 			
			{ label: '英国运费id', name: 'britainFreight', index: 'britain_freight', width: 80 }, 			
			{ label: '法国运费id', name: 'franceFreight', index: 'france_freight', width: 80 }, 			
			{ label: '德国运费id', name: 'germanyFreight', index: 'germany_freight', width: 80 }, 			
			{ label: '意大利运费id', name: 'italyFreight', index: 'italy_freight', width: 80 }, 			
			{ label: '西班牙运费id', name: 'spainFreight', index: 'spain_freight', width: 80 }, 			
			{ label: '日本运费id', name: 'japanFreight', index: 'japan_freight', width: 80 }, 			
			{ label: '澳大利亚运费id', name: 'australiaFreight', index: 'australia_freight', width: 80 }, 			
			{ label: '库存', name: 'stock', index: 'stock', width: 80 }, 			
			{ label: '预处理时间(天)', name: 'pretreatmentDate', index: 'pretreatment_date', width: 80 }, 			
			{ label: '产品简称（英文）', name: 'productAbbreviations', index: 'product_abbreviations', width: 80 }, 			
			{ label: '产品标题', name: 'productTitle', index: 'product_title', width: 80 }, 			
			{ label: '中文介绍', name: 'chineseIntroduction', index: 'chinese_introduction', width: 80 }, 			
			{ label: '英语介绍', name: 'britainIntroduction', index: 'britain_introduction', width: 80 }, 			
			{ label: '法语介绍', name: 'franceIntroduction', index: 'france_introduction', width: 80 }, 			
			{ label: '德语介绍', name: 'germanyIntroduction', index: 'germany_introduction', width: 80 }, 			
			{ label: '意大利语介绍', name: 'italyIntroduction', index: 'italy_introduction', width: 80 }, 			
			{ label: '西班牙语介绍', name: 'spainIntroduction', index: 'spain_introduction', width: 80 }, 			
			{ label: '日语介绍', name: 'japanIntroduction', index: 'japan_introduction', width: 80 }, 			
			{ label: '（变体参数）颜色的id', name: 'colorId', index: 'color_id', width: 80 }, 			
			{ label: '（变体参数）尺寸id', name: 'sizeId', index: 'size_id', width: 80 }, 			
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
		products: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.products = {};
		},
		update: function (event) {
			var productId = getSelectedRow();
			if(productId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(productId)
		},
		saveOrUpdate: function (event) {
			var url = vm.products.productId == null ? "product/products/save" : "product/products/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.products),
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
			var productIds = getSelectedRows();
			if(productIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "product/products/delete",
                    contentType: "application/json",
				    data: JSON.stringify(productIds),
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
		getInfo: function(productId){
			$.get(baseURL + "product/products/info/"+productId, function(r){
                vm.products = r.products;
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