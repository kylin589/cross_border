window.onload = function () {
    // 筛选区域
    // $('.screen>div.audit ul li').click(function () {
    //     $('.screen>div.audit ul li').removeClass('action');
    //     $(this).addClass('action');
    //     vm.auditNumber = $('.screen>div.audit ul li.action').eq(0).attr("data-number");
    //     vm.getPage(1, 30);
    //     vm.laypage();
    // });
    // $('.screen>div.shelve ul li').click(function () {
    //     $('.screen>div.shelve ul li').removeClass('action');
    //     $(this).addClass('action');
    //     vm.shelveNumber = $('.screen>div.shelve ul li.action').eq(0).attr("data-number");
    //     vm.getPage(1, 30);
    //     vm.laypage();
    // });
    // $('.screen>div.type ul li').click(function () {
    //     $('.screen>div.type ul li').removeClass('action');
    //     $(this).addClass('action');
    //     vm.productNumber = $('.screen>div.type ul li.action').eq(0).attr("data-number");
    //     vm.getPage(1, 30);
    //     vm.laypage();
    // });
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

    // 点击分类框元素外部隐藏元素
    $(document).click(function(){
        $(".sousuoArea").hide();
    });
    // 点击分类框元素时阻止事件冒泡
    $(".sousuoArea").click(function(event){
        event.stopPropagation();
    });
    // 点击分类输入框时阻止事件冒泡
    $('.sousuoAreaInput').click(function(event){
        event.stopPropagation();
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
        // 产品列表
        proList: [],
        // 当前页码
        proCurr:1,
        // 每页数量限制
        pageLimit:12,
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
        // categoryOneList: [],
        // 总产品数
        totalCount: null,
        // currPage: null,
        // category: '',
        title: '',
        sku: '',
        value9: null,
        // 审核状态个数
        auditNumber: '',
        // 上架状态个数
        shelveNumber: '',
        // 产品类型个数
        productNumber: '',
        // 一级产品分类
        categoryOneList:[],
        // 二级产品分类
        categoryTwoList:[],
        // 三级产品分类
        categoryThreeList:[],
        // 当前所选分类
        nowproTypearr:[],
        nowProType:'',
        nowProTypeId:'',
        // 选中产品ip数组
        activeProlist:[],
        // 批量修改产品分类
        pilTypePro:[],
        // 批量修改对象
        xiugaiData:{
            "productIds":[],
            "auditStatus": "",
            "shelveStatus": "",
            "productType": "",
            "categoryThreeId": null,
            "producerName": "",
            "manufacturerNumber": "",
            "brandName": "",
            "productWeight": null,
            "productLength": null,
            "productWide": null,
            "productHeight": null,
            "productTitleQ": "",
            "productTitleH": "",
            "keyWord": "",
            "keyPoints": "",
            "keyPointsadd": "",
            "keywordsadd": "",
            "productDescriptionQ": "",
            "productDescriptionH": "",
            "productCategory":"",
        },
        // 采集产品分类
        caijiTypeList:[],
        caijiNowType:'',
        caijiThreeId:null,
        caijiUrl:'',
        caijiProgress:0,
        caijiProgressIf:false

    },
    methods: {
        // 筛选
        // 审核状态筛选
        screenfunAudit:function(event){
            var _index = $(event.target).attr('data-index');
            $('.screen>div.audit ul li').removeClass('action');
            $('.screen>div.audit ul li').eq(_index).addClass('action');
            vm.auditNumber = $('.screen>div.audit ul li.action').eq(0).attr("data-number");
            vm.getPage();
            // vm.laypage();
        },
        // 上架状态筛选
        screenfunShelve:function(event){
            var _index = $(event.target).attr('data-index');
            $('.screen>div.shelve ul li').removeClass('action');
            $('.screen>div.shelve ul li').eq(_index).addClass('action');
            vm.shelveNumber = $('.screen>div.shelve ul li.action').eq(0).attr("data-number");
            vm.getPage();
            // vm.laypage();
        },
        // 产品类型筛选
        screenfunType:function(event){
            var _index = $(event.target).attr('data-index');
            $('.screen>div.type ul li').removeClass('action');
            $('.screen>div.type ul li').eq(_index).addClass('action');
            vm.productNumber = $('.screen>div.type ul li.action').eq(0).attr("data-number");
            vm.getPage();
            // vm.laypage();
        },
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
        // getQueryCategoryOne: function () {
        //     $.get('../../product/category/querycategoryone', function (r) {
        //         vm.categoryOneList = r.categoryOneList;
        //     });
        // },
        // 分页器
        laypage: function () {
            // var tempTotalCount;

            // 分页器
            layui.use('laypage', function () {
                var laypage = layui.laypage;
                //执行一个laypage实例
                laypage.render({
                    elem: 'page', //注意，这里的 test1 是 ID，不用加 # 号
                    count: vm.totalCount, //数据总数，从服务端得到
                    prev: '<i class="layui-icon layui-icon-left"></i>',
                    next: '<i class="layui-icon layui-icon-right"></i>',
                    limits: [12, 24, 30],
                    limit: 12,
                    layout: ['prev', 'page', 'next', 'limit', 'skip'],
                    jump: function (obj, first) {
                        //obj包含了当前分页的所有参数，比如：
                        // console.log(obj.curr); //得到当前页，以便向服务端请求对应页的数据。
                        // console.log(obj.limit); //得到每页显示的条数
                        vm.pageLimit = obj.limit;
                        vm.proCurr = obj.curr;
                        $('.pro_list .item').removeClass('action');
                        //首次不执行
                        if (!first) {
                            //do something
                            vm.getPage1();
                        }
                    }
                });
            });
        },
        // 获取产品列表
        getPage: function () {

            var s , e;
            console.log(this.value9)
            if(JSON.stringify(this.value9) != 'null'){
                console.log(true);
                s = this.value9[0] + ' 00:00:00';
                e = this.value9[1] + ' 23:59:59';
            }
            $.ajax({
                url: '../../product/products/mylist',
                type: 'post',
                data: {
                    // '_search': false,
                    'page': this.proCurr,
                    'limit': this.pageLimit,
                    // 'sidx': "",
                    // 'order': "asc",
                    'category': this.nowProTypeId,
                    'title': this.title,
                    'sku': this.sku,
                    'startDate':s,
                    'endDate': e,
                    'auditNumber': this.auditNumber,
                    'shelveNumber': this.shelveNumber,
                    'productNumber': this.productNumber,
                    // '_': $.now()
                },
                dataType: 'json',
                success: function (r) {
                    if (r.code === 0) {
                        console.log('订单列表');
                        console.log(r)
                        vm.statistics.proNum = r.proNum;
                        vm.statistics.via = r.via;
                        vm.statistics.variant = r.variant;
                        vm.statistics.allVariant = r.allVariant;
                        vm.proList = r.page.list;
                        vm.totalCount = r.page.totalCount;
                        console.log(vm.proList);
                        // vm.proCurr = r.page.currPage;
                        // vm.limit = r.page.pageSize;
                        // vm.limit = lmt;
                        vm.laypage();
                    } else {
                        layer.alert(r.message);
                    }


                },
                error: function () {
                    layer.msg("网络故障");
                }
            });


        },
        getPage1: function () {

            var s , e;
            console.log(this.value9)
            if(JSON.stringify(this.value9) != 'null'){
                console.log(true);
                s = this.value9[0] + ' 00:00:00';
                e = this.value9[1] + ' 23:59:59';
            }
            $.ajax({
                url: '../../product/products/mylist',
                type: 'post',
                data: {
                    // '_search': false,
                    'page': this.proCurr,
                    'limit': this.pageLimit,
                    // 'sidx': "",
                    // 'order': "asc",
                    'category': this.nowProTypeId,
                    'title': this.title,
                    'sku': this.sku,
                    'startDate':s,
                    'endDate': e,
                    'auditNumber': this.auditNumber,
                    'shelveNumber': this.shelveNumber,
                    'productNumber': this.productNumber,
                    // '_': $.now()
                },
                dataType: 'json',
                success: function (r) {
                    if (r.code === 0) {
                        // console.log(r)
                        vm.statistics.proNum = r.proNum;
                        vm.statistics.via = r.via;
                        vm.statistics.variant = r.variant;
                        vm.statistics.allVariant = r.allVariant;
                        vm.proList = r.page.list;
                        vm.totalCount = r.page.totalCount;
                        // vm.proCurr = r.page.currPage;
                        // vm.limit = r.page.pageSize;
                        // vm.limit = lmt;
                        // vm.laypage();
                    } else {
                        layer.alert(r.message);
                    }


                },
                error: function () {
                    layer.msg("网络故障");
                }
            });


        },
        selPage:function () {
            vm.getPage();
            // vm.laypage();
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
        },
        // 获取产品一级分类
        getProTypeOne:function () {
            $.ajax({
                type: 'post',
                url: '../../product/category/querycategoryone',
                contentType: "application/json",
                data: '',
                success: function (r) {
                    console.log('产品一级分类');
                    console.log(r);
                    if (r.code == 0) {
                        vm.categoryOneList = r.categoryOneList;

                        console.log(vm.categoryOneList)
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        // 点击分类输入框展示一级分类
        typeClickINput:function (event) {
            // var _top = $(event.target)
            console.log($('.sousuoArea'));
            $('.sousuoArea').css({
                'display':'flex',
            })
            vm.getProTypeOne();
            vm.categoryTwoList=[];
            vm.categoryThreeList=[];

            // 点击分类框元素外部隐藏元素
            $(document).click(function(){
                $(".sousuoArea").hide();
            });
            // 点击分类框元素时阻止事件冒泡
            $(".sousuoArea").click(function(event){
                event.stopPropagation();
            });
            // 点击分类输入框时阻止事件冒泡
            $('.sousuoAreaInput').click(function(event){
                event.stopPropagation();
            })

        },
        // 点击每个分类展示下一级或者直接选中
        clickTypeItem:function (event) {
            // $(event.target)
            var pId = $(event.target).attr('data-pid');
            var id = $(event.target).attr('data-id');

            console.log(parseInt($(event.target).attr('data-pid')));
            console.log(vm.categoryOneList[vm.categoryOneList.length-1]);
            console.log(parseInt($(event.target).attr('data-pid')) > vm.categoryOneList[vm.categoryOneList.length-1].categoryId);

            // if($(event.target).attr('data-if') == 'true'){
                if($(event.target).attr('data-pid') == '0'){
                    vm.categoryThreeList = [];
                    $.ajax({
                        type: 'get',
                        url: '../../product/category/querycategorybyparentid',
                        contentType: "application/json",
                        data: {categoryId:id},
                        success: function (r) {
                            if (r.code == 0) {

                                if(r.categoryList.length != 0){
                                    vm.categoryTwoList = r.categoryList;
                                    vm.nowproTypearr[0] = $(event.target).attr('data-val');
                                }else {
                                    console.log('&&&&');
                                    vm.nowproTypearr[0] = $(event.target).attr('data-val');
                                    vm.nowProTypeId = id;
                                    vm.nowProType = vm.nowproTypearr[0];
                                    $('.sousuoArea').css('display','none');
                                }
                                // vm.nowproTypearr[0] = $(event.target).attr('data-val');

                                console.log(r.categoryList)
                            } else {
                                alert(r.msg);
                            }
                        }
                    });
                // }else if($(event.target).attr('data-pid') == '0' && vm.categoryThreeList){
                }else if($(event.target).attr('data-pid') != 0){
                    if($(event.target).attr('data-index') == 2){
                        vm.categoryThreeList = [];
                        $.ajax({
                            type: 'get',
                            url: '../../product/category/querycategorybyparentid',
                            contentType: "application/json",
                            data: {categoryId:id},
                            success: function (r) {
                                if (r.code == 0) {
                                    // vm.categoryThreeList = r.categoryList;
                                    if(r.categoryList.length != 0){
                                        vm.categoryThreeList = r.categoryList;
                                        vm.nowproTypearr[1] = $(event.target).attr('data-val');
                                    }else {
                                        vm.nowproTypearr[1] = $(event.target).attr('data-val');
                                        vm.nowProTypeId = id;
                                        vm.nowProType = vm.nowproTypearr[0] + '/' + vm.nowproTypearr[1];
                                        $('.sousuoArea').css('display','none');
                                        // vm.nowproTypearr.forEach(function (t) {
                                        //     vm.nowProType+=t+'/'
                                        // })
                                        // vm.nowProType.slice(0,vm.nowProType.length-1);
                                    }

                                    console.log(vm.categoryThreeList)
                                } else {
                                    alert(r.msg);
                                }
                            }
                        });
                    }else {
                        vm.nowproTypearr[2] = $(event.target).attr('data-val');
                        vm.nowProTypeId = id;

                        vm.nowProType = vm.nowproTypearr[0] + '/' + vm.nowproTypearr[1] + '/' + vm.nowproTypearr[2];
                        $('.sousuoArea').css('display','none');
                    }

                }
            // }else {
            //     vm.nowproTypearr[2] = $(event.target).attr('data-val');
            //     vm.nowProTypeId = id;
            //
            //     vm.nowProType = vm.nowproTypearr[0] + '/' + vm.nowproTypearr[1] + '/' + vm.nowproTypearr[2];
            //     $('.sousuoArea').css('display','none');
            // }


        },
        // 获取选中产品id
        getProIDs:function () {
            var activeProList = $('.pro_list .item.action');
            console.log($('.pro_list .item'));
            console.log(activeProList);
            for(var i = 0;i<activeProList.length;i++){
                // vm.activeProlist.push(activeProList.eq(i).attr('data-id'));
                vm.activeProlist.push(parseInt(activeProList.eq(i).attr('data-id')));
            }
        },
        // 批量删除
        pilDel:function () {
            vm.getProIDs();
            console.log(JSON.stringify(vm.activeProlist));
            if(vm.activeProlist.length == 0){
                layer.alert('请选择要删除的产品');
            }else {
                layer.confirm('确定删除吗？', function(index){
                    $.ajax({
                        url: '../../product/products/falsedeletion',
                        type: 'post',
                        data:JSON.stringify(vm.activeProlist)
                        // 'productIds': JSON.stringify(vm.activeProlist)
                        ,
                        contentType: "application/json",
                        success: function (r) {
                            console.log(r);
                            if (r.code === 0) {
                                layer.msg('删除成功');
                                vm.getPage();

                            } else {
                                layer.alert(r.msg);
                            }
                        },
                        error: function () {
                            layer.msg("网络故障");
                        }
                    })

                });

            }

        },
        // 批量修改
        pilUp:function () {
            vm.getProIDs();
            console.log(JSON.stringify(vm.activeProlist));
            vm.xiugaiData.productIds = vm.activeProlist;
            console.log(vm.xiugaiData);
            if(vm.activeProlist.length == 0){
                layer.alert('请选择要修改的产品');
            }else {

                layer.open({
                    type: 1,
                    title: false,
                    content: $('#pilUp'), //这里content是一个普通的String
                    skin: 'openClass',
                    area: ['800px', '520px'],
                    shadeClose: true,
                    btn: ['修改','取消'],
                    btn1: function (index) {
                        console.log(vm.xiugaiData);
                        layer.confirm('确定修改吗？',function (index1) {
                            $.ajax({
                                url: '../../product/products/batchmodify',
                                type: 'post',
                                // data:vm.xiugaiData,
                                data:JSON.stringify(vm.xiugaiData),
                                contentType: "application/json",
                                success: function (r) {
                                    console.log(r);
                                    if (r.code === 0) {
                                        layer.msg('修改成功');
                                        layer.close(index1);
                                        layer.close(index);

                                    } else {
                                        layer.alert(r.msg);
                                    }
                                },
                                error: function () {
                                    layer.msg("网络故障");
                                }
                            })
                        })


                    },
                    btn2: function (index) {


                    }
                });


            }

        },
        // 点击批量修改弹框分类输入框展示一级分类
        typeClickINput1:function (event) {
            // var _top = $(event.target)
            console.log($('.sousuoArea'));
            $('.sousuoArea1').css({
                'display':'flex',
            })
            vm.getProTypeOne();
            vm.categoryTwoList=[];
            vm.categoryThreeList=[];

            // 点击分类框元素外部隐藏元素
            $(document).click(function(){
                $(".sousuoArea").hide();
            });
            // 点击分类框元素时阻止事件冒泡
            $(".sousuoArea").click(function(event){
                event.stopPropagation();
            });
            // 点击分类输入框时阻止事件冒泡
            $('.sousuoAreaInput').click(function(event){
                event.stopPropagation();
            })

        },
        // 点击每个分类展示下一级或者直接选中1
        clickTypeItem1:function (event) {
            // $(event.target)
            var pId = $(event.target).attr('data-pid');
            var id = $(event.target).attr('data-id');

            if($(event.target).attr('data-pid') == '0'){
                vm.categoryThreeList = [];
                $.ajax({
                    type: 'get',
                    url: '../../product/category/querycategorybyparentid',
                    contentType: "application/json",
                    data: {categoryId:id},
                    success: function (r) {
                        if (r.code == 0) {

                            if(r.categoryList.length != 0){
                                vm.categoryTwoList = r.categoryList;
                                vm.pilTypePro[0] = $(event.target).attr('data-val');
                            }else {
                                console.log('&&&&');
                                vm.pilTypePro[0] = $(event.target).attr('data-val');
                                vm.xiugaiData.categoryThreeId = id;
                                vm.xiugaiData.productCategory = vm.nowproTypearr[0];
                                $('.sousuoArea').css('display','none');
                            }
                            // vm.nowproTypearr[0] = $(event.target).attr('data-val');

                            console.log(r.categoryList)
                        } else {
                            alert(r.msg);
                        }
                    }
                });
                // }else if($(event.target).attr('data-pid') == '0' && vm.categoryThreeList){
            }else if($(event.target).attr('data-pid') != 0){
                if($(event.target).attr('data-index') == 2){
                    vm.categoryThreeList = [];
                    $.ajax({
                        type: 'get',
                        url: '../../product/category/querycategorybyparentid',
                        contentType: "application/json",
                        data: {categoryId:id},
                        success: function (r) {
                            if (r.code == 0) {
                                // vm.categoryThreeList = r.categoryList;
                                if(r.categoryList.length != 0){
                                    vm.categoryThreeList = r.categoryList;
                                    vm.pilTypePro[1] = $(event.target).attr('data-val');
                                }else {
                                    vm.pilTypePro[1] = $(event.target).attr('data-val');
                                    vm.xiugaiData.categoryThreeId = id;
                                    vm.xiugaiData.productCategory = vm.nowproTypearr[0] + '/' + vm.nowproTypearr[1];
                                    $('.sousuoArea').css('display','none');
                                    // vm.nowproTypearr.forEach(function (t) {
                                    //     vm.nowProType+=t+'/'
                                    // })
                                    // vm.nowProType.slice(0,vm.nowProType.length-1);
                                }

                                console.log(vm.categoryThreeList)
                            } else {
                                alert(r.msg);
                            }
                        }
                    });
                }else {
                    vm.pilTypePro[2] = $(event.target).attr('data-val');
                    vm.xiugaiData.productCategory = vm.pilTypePro[0] + '/' + vm.pilTypePro[1] + '/' + vm.pilTypePro[2];
                    vm.xiugaiData.categoryThreeId = parseInt(id);
                    $('.sousuoArea').css('display','none');

                    // vm.nowproTypearr[2] = $(event.target).attr('data-val');
                    // vm.nowProTypeId = id;
                    //
                    // vm.nowProType = vm.nowproTypearr[0] + '/' + vm.nowproTypearr[1] + '/' + vm.nowproTypearr[2];
                    // $('.sousuoArea').css('display','none');
                }

            }



        },
        // 上架、下架、审核通过、待审核
        changeauditstatusFunc:function (n,t) {
            vm.getProIDs();
            // console.log(JSON.stringify(vm.activeProlist));
            // console.log(n);
            // console.log(t);
            layer.confirm('确定修改改状态吗？',function () {
                $.ajax({
                    url: '../../product/products/changeauditstatus',
                    type: 'post',
                    data:JSON.stringify({
                        'productIds':vm.activeProlist,
                        'number':n,
                        'type':t
                    }),
                    // dataType: 'json',
                    contentType: "application/json",
                    success: function (r) {
                        console.log(r);
                        if (r.code === 0) {
                            layer.msg('操作成功');

                        } else {
                            layer.alert(r.msg);
                        }
                    },
                    error: function () {
                        layer.msg("网络故障");
                    }
                })
            })



        },
        // 点击采集弹框分类输入框展示一级分类
        typeClickINput2:function (event) {
            // var _top = $(event.target)
            console.log($('.sousuoArea'));
            $('.sousuoArea2').css({
                'display':'flex',
            })
            vm.getProTypeOne();

            vm.categoryTwoList=[];
            vm.categoryThreeList=[];

            // 点击分类框元素外部隐藏元素
            $(document).click(function(){
                $(".sousuoArea").hide();
            });
            // 点击分类框元素时阻止事件冒泡
            $(".sousuoArea").click(function(event){
                event.stopPropagation();
            });
            // 点击分类输入框时阻止事件冒泡
            $('.sousuoAreaInput').click(function(event){
                event.stopPropagation();
            })

        },
        // 点击每个分类展示下一级或者直接选中1
        clickTypeItem2:function (event) {
            // $(event.target)
            var pId = $(event.target).attr('data-pid');
            var id = $(event.target).attr('data-id');

            if($(event.target).attr('data-pid') == '0'){
                vm.categoryThreeList = [];
                $.ajax({
                    type: 'get',
                    url: '../../product/category/querycategorybyparentid',
                    contentType: "application/json",
                    data: {categoryId:id},
                    success: function (r) {
                        if (r.code == 0) {

                            if(r.categoryList.length != 0){
                                vm.categoryTwoList = r.categoryList;
                                vm.caijiTypeList[0] = $(event.target).attr('data-val');
                            }else {
                                console.log('&&&&');
                                vm.caijiTypeList[0] = $(event.target).attr('data-val');
                                vm.caijiThreeId = id;
                                vm.caijiNowType = vm.caijiTypeList[0];
                                $('.sousuoArea').css('display','none');
                            }
                            // vm.nowproTypearr[0] = $(event.target).attr('data-val');

                            console.log(r.categoryList)
                        } else {
                            alert(r.msg);
                        }
                    }
                });
                // }else if($(event.target).attr('data-pid') == '0' && vm.categoryThreeList){
            }else if($(event.target).attr('data-pid') != 0){
                if($(event.target).attr('data-index') == 2){
                    vm.categoryThreeList = [];
                    $.ajax({
                        type: 'get',
                        url: '../../product/category/querycategorybyparentid',
                        contentType: "application/json",
                        data: {categoryId:id},
                        success: function (r) {
                            if (r.code == 0) {
                                // vm.categoryThreeList = r.categoryList;
                                if(r.categoryList.length != 0){
                                    vm.categoryThreeList = r.categoryList;
                                    vm.caijiTypeList[1] = $(event.target).attr('data-val');
                                }else {
                                    vm.caijiTypeList[1] = $(event.target).attr('data-val');
                                    vm.caijiThreeId = id;
                                    vm.caijiNowType = vm.caijiTypeList[0] + '/' + vm.caijiTypeList[1];
                                    $('.sousuoArea').css('display','none');
                                    // vm.nowproTypearr.forEach(function (t) {
                                    //     vm.nowProType+=t+'/'
                                    // })
                                    // vm.nowProType.slice(0,vm.nowProType.length-1);
                                }

                                console.log(vm.categoryThreeList)
                            } else {
                                alert(r.msg);
                            }
                        }
                    });
                }else {
                    // vm.pilTypePro[2] = $(event.target).attr('data-val');
                    // vm.xiugaiData.productCategory = vm.pilTypePro[0] + '/' + vm.pilTypePro[1] + '/' + vm.pilTypePro[2];
                    // vm.xiugaiData.categoryThreeId = parseInt(id);
                    // $('.sousuoArea').css('display','none');

                    vm.caijiTypeList[2] = $(event.target).attr('data-val');
                    vm.caijiNowType = vm.caijiTypeList[0] + '/' + vm.caijiTypeList[1] + '/' + vm.caijiTypeList[2];
                    vm.caijiThreeId = parseInt(id);
                    $('.sousuoArea').css('display','none');

                    // vm.nowproTypearr[2] = $(event.target).attr('data-val');
                    // vm.nowProTypeId = id;
                    //
                    // vm.nowProType = vm.nowproTypearr[0] + '/' + vm.nowproTypearr[1] + '/' + vm.nowproTypearr[2];
                    // $('.sousuoArea').css('display','none');
                }

            }

            // if($(event.target).attr('data-if') == 'true'){
            //     if($(event.target).attr('data-pid') == '0'){
            //         vm.categoryThreeList = [];
            //         $.ajax({
            //             type: 'get',
            //             url: '../../product/category/querycategorybyparentid',
            //             contentType: "application/json",
            //             data: {categoryId:id},
            //             success: function (r) {
            //                 if (r.code == 0) {
            //                     vm.categoryTwoList = r.categoryList;
            //                     vm.caijiTypeList[0] = $(event.target).attr('data-val');
            //                     console.log(vm.categoryTwoList)
            //                 } else {
            //                     alert(r.msg);
            //                 }
            //             }
            //         });
            //     }else if($(event.target).attr('data-pid') != '0'){
            //         vm.categoryThreeList = [];
            //         $.ajax({
            //             type: 'get',
            //             url: '../../product/category/querycategorybyparentid',
            //             contentType: "application/json",
            //             data: {categoryId:id},
            //             success: function (r) {
            //                 if (r.code == 0) {
            //                     vm.categoryThreeList = r.categoryList;
            //                     vm.caijiTypeList[1] = $(event.target).attr('data-val');
            //                     console.log(vm.categoryThreeList)
            //                 } else {
            //                     alert(r.msg);
            //                 }
            //             }
            //         });
            //     }
            // }else {
            //
            //     vm.caijiTypeList[2] = $(event.target).attr('data-val');
            //     vm.caijiNowType = vm.caijiTypeList[0] + '/' + vm.caijiTypeList[1] + '/' + vm.caijiTypeList[2];
            //     vm.caijiThreeId = parseInt(id);
            //     $('.sousuoArea').css('display','none');
            // }


        },
        // 采集产品弹框
        caijiProFunc:function () {
            layer.open({
                type: 1,
                title: false,
                content: $('#caijiCreate'), //这里content是一个普通的String
                skin: 'openClass',
                area: ['400px', '160px'],
                shadeClose: true,
                btn: ['采集','取消'],
                btn1: function (index) {
                    // console.log(vm.xiugaiData);
                    console.log(vm.caijiUrl)
                    console.log(vm.caijiThreeId);
                    vm.caijiProgressIf = true;
                    var timer = setInterval(function () {
                        if(vm.caijiProgress<= 70){
                            vm.caijiProgress += 10;
                        }else {

                            setTimeout(timer);
                            // vm.caijiProgress = '';
                        }

                    },1000);
                    $.ajax({
                        url: 'http://127.0.0.1:5000/getCollectionInfo',
                        type: 'post',
                        // data:vm.xiugaiData,
                        data:{
                            url:vm.caijiUrl,
                            // category_three_id:vm.caijiThreeId
                        },
                        // contentType: "application/json",
                        success: function (r) {
                            // console.log(r);
                            if (r.code === 0) {
                                console.log(r);
                                vm.caijiProgress = 100;
                                vm.caijiProgressIf = false;
                                vm.caijiProgress = 0;
                                console.log("productId:" + r.product_id);
                                $.ajax({
                                    url: '../../product/products/collectproduct',
                                    type: 'get',
                                    // data:vm.xiugaiData,
                                    data:{
                                        productId:r.product_id
                                    }
                                    ,
                                    dataType: 'json',
                                    // contentType: "application/json",
                                    success: function (r) {
                                        // console.log(r);
                                        if (r.code === 0) {
                                            console.log(r);
                                            layer.alert('操作成功');
                                            vm.getPage();
                                            layer.close(index);
                                        } else {


                                            layer.alert(r.msg);
                                        }
                                    },
                                    error: function () {

                                        layer.msg("网络故障");
                                    }
                                })

                                // layer.alert('操作成功');

                            } else {
                                setTimeout(timer);
                                vm.caijiProgressIf = false;
                                vm.caijiProgress = 0;
                                layer.alert(r.msg);
                            }
                        },
                        error: function () {
                            setTimeout(timer);
                            vm.caijiProgressIf = false;
                            vm.caijiProgress = 0;
                            layer.msg("网络故障1111");
                        }
                    })

                },
                btn2: function (index) {


                }
            });
        },
        // 原创
        createPro:function () {
            window.location.href="createPro.html";
        }


    },
    created: function () {
        // $.ajax({
        //     url: '../../product/products/gettotalcount',
        //     type: 'post',
        //     data: {
        //         'category': this.category,
        //         'title': this.title,
        //         'sku': this.sku,
        //         'value9': this.value9,
        //         'auditNumber': this.auditNumber,
        //         'shelveNumber': this.shelveNumber,
        //         'productNumber': this.productNumber
        //     },
        //     dataType: 'json',
        //     success: function (r) {
        //         if (r.code === 0) {
        //             vm.totalCount = r.totalCount;
        //             // console.log(tempTotalCount);
        //         } else {
        //             layer.alert(r.message);
        //         }
        //     },
        //     error: function () {
        //         layer.msg("网络故障");
        //     }
        // });
        // this.laypage();
        this.getMyStatusList();

        /*this.getAuditList();
        this.getPutawayList();
        this.getProductTypeList();*/
        // this.getQueryCategoryOne();
        this.getPage();


    },
    updated:function () {
        $('.pro_list').css('display','block');
        var _width = $('.pro_list .item img').width();
        $('.pro_list .item img').css('height',_width+'px');
    }
})