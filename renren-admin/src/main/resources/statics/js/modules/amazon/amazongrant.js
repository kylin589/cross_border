$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'amazon/amazongrant/list',
        datatype: "json",
        colModel: [			
			{ label: 'grantId', name: 'grantId', index: 'grant_id', width: 50, key: true },
			{ label: '用户id', name: 'userId', index: 'user_id', width: 80 }, 			
			{ label: '店铺名称', name: 'shopName', index: 'shop_name', width: 80 }, 			
			{ label: 'Amazon账号', name: 'amazonAccount', index: 'amazon_account', width: 80 }, 			
			{ label: '开户区域(0：北美、1：欧洲、2：日本、3：澳大利亚)', name: 'area', index: 'area', width: 80 }, 			
			{ label: '卖家编号', name: 'merchantId', index: 'merchant_id', width: 80 }, 			
			{ label: '授权令牌', name: 'grantToken', index: 'grant_token', width: 80 }			
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
		amazonGrant: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.amazonGrant = {};
		},
		update: function (event) {
			var grantId = getSelectedRow();
			if(grantId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(grantId)
		},
		saveOrUpdate: function (event) {
			var url = vm.amazonGrant.grantId == null ? "amazon/amazongrant/save" : "amazon/amazongrant/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.amazonGrant),
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
			var grantIds = getSelectedRows();
			if(grantIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "amazon/amazongrant/delete",
                    contentType: "application/json",
				    data: JSON.stringify(grantIds),
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
		getInfo: function(grantId){
			$.get(baseURL + "amazon/amazongrant/info/"+grantId, function(r){
                vm.amazonGrant = r.amazonGrant;
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