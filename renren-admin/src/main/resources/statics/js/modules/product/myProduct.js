window.onload = function () {
    // 筛选区域
    $('.screen>div.audit ul li').click(function () {
        $('.screen>div.audit ul li').removeClass('action');
        $(this).addClass('action');
        vm.auditNumber = $('.screen>div.audit ul li.action').eq(0).attr("data-number");
        vm.getPage(1, 30);
        vm.laypage();
    });
    $('.screen>div.shelve ul li').click(function () {
        $('.screen>div.shelve ul li').removeClass('action');
        $(this).addClass('action');
        vm.shelveNumber = $('.screen>div.shelve ul li.action').eq(0).attr("data-number");
        vm.getPage(1, 30);
        vm.laypage();
    });
    $('.screen>div.type ul li').click(function () {
        $('.screen>div.type ul li').removeClass('action');
        $(this).addClass('action');
        vm.productNumber = $('.screen>div.type ul li.action').eq(0).attr("data-number");
        vm.getPage(1, 30);
        vm.laypage();
    });
    // 全选
    $('.radiobox').click(function () {
        if ($('.radiobox').attr("data-checked") == 'true') {
            $(this).addClass('action');
            $('.pro_list .item').addClass('action');
            $('.radiobox').attr("data-checked", 'false');
        } else {
            $(this).removeClass('action');
            $('.pro_list .item').removeClass('action');
            $('.radiobox').attr("data-checked", 'true');
        }
    })
    // 商品单机选中
    /* $('.pro_list .item').click(function () {
         $(this).toggleClass('action');
     })
     $('.pro_list .item h3 a').click(function () {
         console.log(11111);
     })*/

}


/*window.onload = function () {
    // 批量修改
    $('.updata').click(function () {
        console.log(11111);
        vm.updateFunc();
    })
}*/



var vm = new Vue({
    el: '#step',
    data: {
        proList: [],
        screenData: {
            auditAll: 234,
            shelveAll: 33,
            typeAll: 55
        },
        statistics: {
            proNum: 0,
            via: 0,
            variant: 0,
            allVariant: 0
        }, audit: {
            auditList: [],
            auditCounts: null
        }, putaway: {
            putawayList: [],
            putawayCounts: null
        }
        , productType: {
            productTypeList: [],
            productTypeCounts: null
        },
        categoryOneList: [],
        totalCount: null,
        currPage: null,
        limit: null,
        category: '',
        title: '',
        sku: '',
        value9: '',
        auditNumber: '',
        shelveNumber: '',
        productNumber: ''
    },
    methods: {
        getMyStatusList:function(){
            $.get('../../product/datadictionary/mystatuslist',function (r) {
                vm.audit.auditList = r.auditList;
                vm.audit.auditCounts = r.auditCounts;
                vm.putaway.putawayList = r.putawayList;
                vm.putaway.putawayCounts = r.putawayCounts;
                vm.productType.productTypeList = r.productTypeList;
                vm.productType.productTypeCounts = r.productTypeCounts;
            });
        },


        /*getAuditList: function () {
            $.get('../../product/datadictionary/auditlist', function (r) {
                vm.audit.auditList = r.auditList;
                vm.audit.auditCounts = r.auditCounts;
            });
        },
        getPutawayList: function () {
            $.get('../../product/datadictionary/putawaylist', function (r) {
                vm.putaway.putawayList = r.putawayList;
                vm.putaway.putawayCounts = r.putawayCounts;
            });
        },
        getProductTypeList: function () {
            $.get('../../product/datadictionary/producttypelist', function (r) {
                vm.productType.productTypeList = r.productTypeList;
                vm.productType.productTypeCounts = r.productTypeCounts;
            });
        },*/
        getQueryCategoryOne: function () {
            $.get('../../product/category/querycategoryone', function (r) {
                vm.categoryOneList = r.categoryOneList;
            });
        },
        laypage: function () {
            var tempTotalCount;
            $.ajax({
                url: '../../product/products/gettotalcount',
                type: 'post',
                data: {
                    'category': this.category,
                    'title': this.title,
                    'sku': this.sku,
                    'value9': this.value9,
                    'auditNumber': this.auditNumber,
                    'shelveNumber': this.shelveNumber,
                    'productNumber': this.productNumber
                },
                dataType: 'json',
                success: function (r) {
                    if (r.code === 0) {
                        tempTotalCount = r.totalCount;
                        console.log(tempTotalCount);
                    } else {
                        layer.alert(r.message);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            })
            // 分页器
            layui.use('laypage', function () {
                var laypage = layui.laypage;

                //执行一个laypage实例
                laypage.render({
                    elem: 'page', //注意，这里的 test1 是 ID，不用加 # 号
                    count: tempTotalCount, //数据总数，从服务端得到
                    prev: '<i class="layui-icon layui-icon-left"></i>',
                    next: '<i class="layui-icon layui-icon-right"></i>',
                    limits: [30, 60, 90],
                    limit: 30,
                    layout: ['prev', 'page', 'next', 'limit', 'skip'],
                    jump: function (obj, first) {
                        //obj包含了当前分页的所有参数，比如：
                        console.log(obj.curr); //得到当前页，以便向服务端请求对应页的数据。
                        console.log(obj.limit); //得到每页显示的条数
                        $('.pro_list .item').removeClass('action');
                        //首次不执行
                        if (!first) {
                            //do something
                            vm.getPage(obj.curr, obj.limit);
                        }
                    }
                });
            });
        },
        getPage: function (crr, lmt) {
            $.ajax({
                url: '../../product/products/mylist',
                type: 'post',
                data: {
                    '_search': false,
                    'page': crr,
                    'limit': lmt,
                    'sidx': "",
                    'order': "asc",
                    'category': this.category,
                    'title': this.title,
                    'sku': this.sku,
                    'value9': this.value9,
                    'auditNumber': this.auditNumber,
                    'shelveNumber': this.shelveNumber,
                    'productNumber': this.productNumber,
                    '_': $.now()
                },
                dataType: 'json',
                success: function (r) {
                    if (r.code === 0) {
                        vm.statistics.proNum = r.proNum;
                        vm.statistics.via = r.via;
                        vm.statistics.variant = r.variant;
                        vm.statistics.allVariant = r.allVariant;
                        vm.proList = r.page.list;
                        vm.totalCount = r.page.totalCount;
                        vm.currPage = r.page.currPage;
                        vm.limit = r.page.pageSize;
                        vm.limit = lmt;
                    } else {
                        layer.alert(r.message);
                    }


                },
                error: function () {
                    layer.msg("网络故障");
                }
            });

        },
        activePro: function (index) {
            $('.pro_list .item').eq(index).toggleClass('action');
        },
        restore: function (event) {
            var productIds = vm.getSelectedRows();
            if (productIds.length == 0) {
                layer.alert("请选择要恢复的产品！");
                return;
            }

            var r = confirm('确定要恢复选中的产品？');
            if (r == true) {
                $.ajax({
                    type: 'post',
                    url: '../../product/productrecycling/restore',
                    contentType: "application/json",
                    data: JSON.stringify(productIds),
                    success: function (r) {
                        if (r.code == 0) {
                            layer.alert('操作成功', function (index) {
                                layer.close(index);
                                vm.getPage(1, 30);
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            }
        },
        getSelectedRows: function () {
            var count = $('div.item.action').length;
            var productIds = new Array(length);
            for (var i = 0; i < count; i++) {
                productIds[i] = $('div.item.action').eq(i).attr("data-id");
            }
            return productIds;
        }
    },
    created: function () {
        this.getMyStatusList();
        /*this.getAuditList();
        this.getPutawayList();
        this.getProductTypeList();*/
        this.getQueryCategoryOne();
        this.getPage(1, 30);
        this.laypage();
    }
})