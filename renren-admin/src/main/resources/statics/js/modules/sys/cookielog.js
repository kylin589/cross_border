$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/cookielog/list',
        datatype: "json",
        colModel: [			
			{ label: 'cookieId', name: 'cookieId', index: 'cookie_id', width: 50, key: true },
			{ label: '用户id', name: 'userId', index: 'user_id', width: 80 }, 			
			{ label: 'cookie', name: 'cookie', index: 'cookie', width: 80 }, 			
			{ label: 'cookie类型', name: 'type', index: 'type', width: 80 }, 			
			{ label: '更新时间', name: 'updateTime', index: 'update_time', width: 80 }			
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
		cookieLog: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.cookieLog = {};
		},
		update: function (event) {
			var cookieId = getSelectedRow();
			if(cookieId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(cookieId)
		},
		saveOrUpdate: function (event) {
			var url = vm.cookieLog.cookieId == null ? "sys/cookielog/save" : "sys/cookielog/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.cookieLog),
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
			var cookieIds = getSelectedRows();
			if(cookieIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "sys/cookielog/delete",
                    contentType: "application/json",
				    data: JSON.stringify(cookieIds),
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
		getInfo: function(cookieId){
			$.get(baseURL + "sys/cookielog/info/"+cookieId, function(r){
                vm.cookieLog = r.cookieLog;
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