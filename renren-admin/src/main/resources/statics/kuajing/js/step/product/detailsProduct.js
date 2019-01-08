$(function () {
    $(function () {
        $('.inner-content-div2').slimScroll({
            height: '400px' //设置显示的高度
        });
    })


    // 语言选项卡
    $('.layui-tab-title li').click(function () {
        // console.log(111111);
        $('.layui-tab-title li').removeClass('layui-this');
        $(this).addClass('layui-this');
        $('.layui-tab-content').removeClass('active');
        $('.layui-tab-content').eq($(this).index()).addClass('active');
    })

    // 产品相册图片鼠标移动上去
    // $('.imgItem').mouseover(function () {
    //     $(this).css('opacity','.5');
    //     $(this).parent().find('i').css('display','inline-block');
    //     $('i.imgDel').mouseover(function () {
    //         $(this).prev().css('opacity','.5');
    //         $(this).css('display','inline-block');
    //     })
    //     $('i.imgDel').click(function () {
    //         var _index = $(this).parent().attr('data-index');
    //         vm.proAlbum.splice(_index,1)
    //         // $(this).parent().remove();
    //         console.log(vm.proAlbum)
    //     })
    // })
    // $('.imgItem').mouseout(function () {
    //     $(this).css('opacity','1');
    //     $(this).parent().find('i').css('display','none');
    // })

    // 产品回收站
    // $('.proStation').click(function () {
    //     vm.prostation();
    // })

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





})

window.onload = function() {
    // var oUl = $("ul1");
    // var oUl = document.getElementById("ul1");

    // var aLi = oUl.getElementsByTagName("li");
//     for(var nn = 0;nn<$('.ul1').length;nn++){
//         //      console.log(nn);
//         var _index = nn;
//         var aLi = $(".ul1").eq(_index).find('li');
//         var aLiLast = $(".ul1").eq(_index).find('li:last-child');
//         // console.log(aLiLast);
//         var disX = 0;
//         var disY = 0;
//         var minZindex = 1;
//         var aPos = [];
//         for(var i = 0; i < aLi.length; i++) {
//             var t = aLi[i].offsetTop;
//             var l = aLi[i].offsetLeft;
//             aLi[i].style.top = t + "px";
//             aLi[i].style.left = l + "px";
//             aPos[i] = {
//                 left: l,
//                 top: t
//             };
//             aLi[i].index = i;
//         }
//         for(var i = 0; i < aLi.length; i++) {
//             aLi[i].style.position = "absolute";
//             aLi[i].style.margin = 0;
//             setDrag(aLi[i],aLi);
//         }
//         var _height = aLiLast[0].offsetTop+60;
//         // console.log(_height);
//         aLiLast.parent().css('height',_height+'px');
//         aLiLast.parent().parent().siblings().css('line-height',_height+'px')
//
//         aLi.mouseover(function () {
//             $(this).find('i').css('display','inline-block');
//             $(this).find('i').mouseover(function () {
//                 $(this).css('display','inline-block');
//             })
//             $(this).find('i').mouseout(function () {
//                 $(this).css('display','none');
//             })
//         })
//         aLi.mouseout(function () {
//             $(this).find('i').css('display','none');
//             $(this).find('i').mouseover(function () {
//                 $(this).css('display','inline-block');
//             })
//             $(this).find('i').mouseout(function () {
//                 $(this).css('display','none');
//             })
//         })
//     }
//     //拖拽
//     function setDrag(obj,all) {
//         obj.onmouseover = function() {
//             obj.style.cursor = "move";
//         }
//         obj.onmousedown = function(event) {
// //						获取滚动条位置
//             var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
//             var scrollLeft = document.documentElement.scrollLeft || document.body.scrollLeft;
// //						设置当前拖拽元素位于最上方
//             obj.style.zIndex = minZindex++;
//             //当鼠标按下时计算鼠标与拖拽对象的距离
//             disX = event.clientX + scrollLeft - obj.offsetLeft;
//             disY = event.clientY + scrollTop - obj.offsetTop;
//             document.onmousemove = function(event) {
//                 all.removeClass('active');
//                 //当鼠标拖动时计算div的位置
//                 var l = event.clientX - disX + scrollLeft;
//                 var t = event.clientY - disY + scrollTop;
//                 obj.style.left = l + "px";
//                 obj.style.top = t + "px";
//                 /*for(var i=0;i<aLi.length;i++){
//                     aLi[i].className = "";
//                     if(obj==aLi[i])continue;//如果是自己则跳过自己不加红色虚线
//                     if(colTest(obj,aLi[i])){
//                         aLi[i].className = "active";
//                     }
//                 }*/
//                 for(var i = 0; i < aLi.length; i++) {
//                     aLi[i].className = "";
//                 }
// //							找到距离最近的元素
//                 var oNear = findMin(obj,all);
// //							给距离最近的元素添加选中类名active
//                 if(oNear) {
//                     oNear.className = "active";
//                 }
//             }
//             document.onmouseup = function() {
//                 document.onmousemove = null; //当鼠标弹起时移出移动事件
//                 document.onmouseup = null; //移出up事件，清空内存
//                 //检测是否普碰上，在交换位置
//                 var oNear = findMin(obj,all);
//                 if(oNear) {
//                     oNear.className = "";
//                     oNear.style.zIndex = minZindex++;
//                     obj.style.zIndex = minZindex++;
// //								互换位置
//                     startMove(oNear, aPos[obj.index]);
//                     startMove(obj, aPos[oNear.index]);
//                     //交换index
//                     oNear.index += obj.index;
//                     obj.index = oNear.index - obj.index;
//                     oNear.index = oNear.index - obj.index;
// //								var _indexN = $(oNear).attr('data-index');
//                     var _indexN = oNear.getAttribute('data-index');
// //								var _index = $(obj).attr('data-index');
//                     var _index = obj.getAttribute('data-index');
//                     oNear.setAttribute('data-index',_index);
// //								$(oNear).attr('data-index',_index);
//                     obj.setAttribute('data-index',_indexN);
// //								$(obj).attr('data-index',_indexN);
//                 } else {
// //								没有选中的最近元素,则拖拽元素返回自己原本的位置
//                     startMove(obj, aPos[obj.index]);
//                 }
//             }
//             clearInterval(obj.timer);
//             return false; //低版本出现禁止符号
//         }
//     }
//     //碰撞检测
//     function colTest(obj1, obj2) {
//         var t1 = obj1.offsetTop;
//         var r1 = obj1.offsetWidth + obj1.offsetLeft;
//         var b1 = obj1.offsetHeight + obj1.offsetTop;
//         var l1 = obj1.offsetLeft;
//
//         var t2 = obj2.offsetTop;
//         var r2 = obj2.offsetWidth + obj2.offsetLeft;
//         var b2 = obj2.offsetHeight + obj2.offsetTop;
//         var l2 = obj2.offsetLeft;
//
//         if(t1 > b2 || r1 < l2 || b1 < t2 || l1 > r2) {
//             return false;
//         } else {
//             return true;
//         }
//     }
//     //勾股定理求距离
//     function getDis(obj1, obj2) {
//         var a = obj1.offsetLeft - obj2.offsetLeft;
//         var b = obj1.offsetTop - obj2.offsetTop;
//         return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
//     }
//     //找到距离最近的
//     function findMin(obj,all) {
//         var minDis = 999999999;
//         var minIndex = -1;
//         for(var i = 0; i < all.length; i++) {
//             if(obj == all[i]) continue;
//             if(colTest(obj, all[i])) {
//                 var dis = getDis(obj, all[i]);
//                 if(dis < minDis) {
//                     minDis = dis;
//                     minIndex = i;
//                 }
//             }
//         }
//         if(minIndex == -1) {
//             return null;
//         } else {
//             return all[minIndex];
//         }
//     }
//
//     //通过class获取元素
//     function getClass(cls){
//         var ret = [];
//         var els = document.getElementsByTagName("*");
//         for (var i = 0; i < els.length; i++){
//             //判断els[i]中是否存在cls这个className;.indexOf("cls")判断cls存在的下标，如果下标>=0则存在;
//             if(els[i].className === cls || els[i].className.indexOf("cls")>=0 || els[i].className.indexOf(" cls")>=0 || els[i].className.indexOf(" cls ")>0){
//                 ret.push(els[i]);
//             }
//         }
//         return ret;
//     }
//     function getStyle(obj,attr){//解决JS兼容问题获取正确的属性值
//         return obj.currentStyle?obj.currentStyle[attr]:getComputedStyle(obj,false)[attr];
//     }
//     function startMove(obj,json,fun){
//         clearInterval(obj.timer);
//         obj.timer = setInterval(function(){
//             var isStop = true;
//             for(var attr in json){
//                 var iCur = 0;
//                 //判断运动的是不是透明度值
//                 if(attr=="opacity"){
//                     iCur = parseInt(parseFloat(getStyle(obj,attr))*100);
//                 }else{
//                     iCur = parseInt(getStyle(obj,attr));
//                 }
//                 var ispeed = (json[attr]-iCur)/3;
//                 //运动速度如果大于0则向下取整，如果小于0想上取整；
//                 ispeed = ispeed>0?Math.ceil(ispeed):Math.floor(ispeed);
//                 //判断所有运动是否全部完成
//                 if(iCur!=json[attr]){
//                     isStop = false;
//                 }
//                 //运动开始
//                 if(attr=="opacity"){
//                     obj.style.filter = "alpha:(opacity:"+(json[attr]+ispeed)+")";
//                     obj.style.opacity = (json[attr]+ispeed)/100;
//                 }else{
//                     obj.style[attr] = iCur+ispeed+"px";
//                 }
//             }
//             //判断是否全部完成
//             if(isStop){
//                 clearInterval(obj.timer);
//                 if(fun){
//                     fun();
//                 }
//             }
//         },30);
//     };
    vm.drapImg();

    $('.selImg').change(function(){
        console.log($('.selImg')[0].files)
    })







}


var vm = new Vue({
    el: '#step',
    data: {
        // 产品id
        id:null,
        // 产品详情
        proDetails:{
            "americanFC": {
                "freight": "",
                "price": "",
                "foreignCurrency": "",
                "optimization": "",
                "finalPrice": "",
                'type':'',
                'profitRate':''
            },
            "canadaFC": {
                "freight": "",
                "price": "",
                "foreignCurrency": "",
                "optimization": "",
                "finalPrice": "",
                'type':'',
                'profitRate':''
            },
            "mexicoFC": {
                "freight": "",
                "price": "",
                "foreignCurrency": "",
                "optimization": "",
                "finalPrice": "",
                'type':'',
                'profitRate':''
            },
            "britainFC": {
                "freight": "",
                "price": "",
                "foreignCurrency": "",
                "optimization": "",
                "finalPrice": "",
                'type':'',
                'profitRate':''
            },
            "franceFC": {
                "freight": "",
                "price": "",
                "foreignCurrency": "",
                "optimization": "",
                "finalPrice": "",
                'type':'',
                'profitRate':''
            },
            "germanyFC": {
                "freight": "",
                "price": "",
                "foreignCurrency": "",
                "optimization": "",
                "finalPrice": "",
                'type':'',
                'profitRate':''
            },
            "italyFC": {
                "freight": "",
                "price": "",
                "foreignCurrency": "",
                "optimization": "",
                "finalPrice": "",
                'type':'',
                'profitRate':''
            },
            "spainFC": {
                "freight": "",
                "price": "",
                "foreignCurrency": "",
                "optimization": "",
                "finalPrice": "",
                'type':'',
                'profitRate':''
            },
            "japanFC": {
                "freight": "",
                "price": "",
                "foreignCurrency": "",
                "optimization": "",
                "finalPrice": "",
                'type':'',
                'profitRate':''
            },
            "australiaFC": {
                "freight": "",
                "price": "",
                "foreignCurrency": "",
                "optimization": "",
                "finalPrice": "",
                'profitRate':''
            },
            "chinesePRE": {
                "productTitle": "",
                "keyWord": "",
                "keyPoints": "",
                "productDescription": ""
            },
            "britainPRE": {
                "productTitle": "",
                "keyWord": "",
                "keyPoints": "",
                "productDescription": ""
            },
            "francePRE": {
                "productTitle": "",
                "keyWord": "",
                "keyPoints": "",
                "productDescription": ""
            },
            "germanyPRE": {
                "productTitle": "",
                "keyWord": "",
                "keyPoints": "",
                "productDescription": ""
            },
            "italyPRE": {
                "productTitle": "",
                "keyWord": "",
                "keyPoints": "",
                "productDescription": ""
            },
            "spainPRE": {
                "productTitle": "",
                "keyWord": "",
                "keyPoints": "",
                "productDescription": ""
            },
            "japanPRE": {
                "productTitle": "",
                "keyWord": "",
                "keyPoints": "",
                "productDescription": ""
            },
        },
        // 成本运费
        costFreight:{},
        // 产品相册
        proAlbum:[],
        // 产品回收站
        proStation:[
            {
                url:'../../statics/kuajing/img/img1.jpg'
            },{
                url:'../../statics/kuajing/img/img2.jpg'
            },{
                url:'../../statics/kuajing/img/img1.jpg'
            },{
                url:'../../statics/kuajing/img/img2.jpg'
            },{
                url:'../../statics/kuajing/img/img1.jpg'
            },{
                url:'../../statics/kuajing/img/img2.jpg'
            },{
                url:'../../statics/kuajing/img/img1.jpg'
            },{
                url:'../../statics/kuajing/img/img2.jpg'
            },{
                url:'../../statics/kuajing/img/img1.jpg'
            },{
                url:'../../statics/kuajing/img/img2.jpg'
            },
        ],
        // 产品介绍列表
        proDecList:{
            'productTitle':'',
            'keyWord':'',
            'keyPoints':'',
            'productDescription':''
        },
        // 变体参数列表
        variantList:[

        ],

        recommend:['米色','黑色','蓝色','青铜','棕色','明确','铜','奶油','金','绿色','灰色','金属','多色','橙子','粉','紫色','红','银','白色','黄色'],
        recommendE:['Beige','Black','Blue','Bronze','Brown','Clear','Copper','Cream','Gold','Green','Grey','Metallic','Multi-colored','Orange','Pink','Purple','Red','Silver','White','Yellow'],
        recommend1:['均码','XS','S','M','L','XL','XXL','XXXL','XXXXL','XXXXXL'],
        recommend1E:['OneSize','XS','S','M','L','XL','XXL','XXXL','XXXXL','XXXXXL'],
        recommendAll:[],
        freightData:{
            'usa':122,
            'canada':34,
            'mexico':234,
            'britain':98,
            'france':2387,
            'germany':234,
            'italy':35624,
            'spain':3846,
            'japan':324
        },
        // 一级产品分类
        categoryOneList:[],
        // 二级产品分类
        categoryTwoList:[],
        // 三级产品分类
        categoryThreeList:[],
        // 当前所选分类
        categoryOneName:'',
        categoryTwoName:'',
        categoryThreeName:'',
        // nowategoryThreeName:'',
        nowProTypeId:'',
        // 产品上传
        dialogImageUrl: '',
        // dialogVisible: false
        // 修改变体参数
        upVariantList:{
            id:'',
            type:'',
        },
        // 变体参数
        selVar:0,
        isUP:true

    },
    methods:{
        // 产品上传方法
        handleRemove:function(file, fileList) {
            layer.confirm('确定删除吗？', {
                btn: ['确定','取消'] //按钮
            }, function(){
                // return true
                // layer.msg('的确很重要', {icon: 1});

            }, function(){
                console.log(file);
                console.log(fileList);
                fileList.splice(file.index , 0, file)
                // fileList.push(file);
                // return false
                // layer.msg('也可以这样', {
                //     time: 20000, //20s后自动关闭
                //     btn: ['明白了', '知道了']
                // });
            });
            // console.log(file, fileList);
        },
        // 上传图片移上去的方法
        handlePictureCardPreview:function(file) {
            this.dialogImageUrl = file.url;
            this.dialogVisible = true;
        },
        // 图片上传成功
        upSuccessFunc:function (file,fileList) {
            layer.confirm('确定上传改图片吗',{
                btn: ['确定','取消'] //按钮
            }, function(){
                // 确定方法

            }, function(){
                // 取消方法


            })
        },
        // 图片上传失败
        upErrorFunc:function (file) {
            console.log(file);
            layer.msg('上传失败')
        },
        // 删除上传图片
        delImgFunc:function (file) {
            layer.confirm('您是如何看待前端开发？', {
                btn: ['重要','奇葩'] //按钮
            }, function(){
                return true
                // layer.msg('的确很重要', {icon: 1});

            }, function(){
                return false
                // layer.msg('也可以这样', {
                //     time: 20000, //20s后自动关闭
                //     btn: ['明白了', '知道了']
                // });
            });
        },
        // 获取产品详情
        getProDetails:function () {
            $.ajax({
                url: '../../product/products/productdetails',
                type: 'get',
                data: {
                    'productId': this.id
                },
                dataType: 'json',
                success: function (r) {
                    // console.log(r);
                    if (r.code === 0) {

                        vm.proDetails = r.productsEntity;
                        console.log('产品详情');
                        console.log(vm.proDetails);
                        console.log(111111111);
                        vm.proDecList = vm.proDetails.chinesePRE;
                        console.log(vm.proDecList);
                        console.log(JSON.stringify(vm.proDetails.colorVP) == 'null');
                        console.log(typeof(vm.proDetails.colorVP));
                        if(JSON.stringify(vm.proDetails.colorVP) != 'null'){
                            vm.variantList.push({
                                id:0,
                                name:'颜色（color）',
                                type:vm.proDetails.colorVP.paramsValue.split(',')
                            })
                        }
                        if(JSON.stringify(vm.proDetails.sizeVP) != 'null'){
                            vm.variantList.push({
                                id:1,
                                name:'尺寸（size）',
                                type:vm.proDetails.sizeVP.paramsValue.split(',')
                            })
                        }

                        vm.proDetails.variantsInfos.forEach(function (t,i) {
                            var img = [];
                            if(JSON.stringify(t.imageUrl) != 'null'){
                                img = t.imageUrl.split(',');
                            }
                            vm.recommendAll.push({
                                id:i,
                                name:t.variantCombination,
                                img:img,
                                sku:t.variantSku,
                                addPrice:t.variantAddPrice,
                                stock:t.variantStock,
                                code:t.eanCode


                            })
                            // vm.drapImg();
                        })
                        // vm.drapImg();
                        setTimeout(function(){ vm.drapImg(); }, 1000);

                        console.log('运费')
                        console.log(vm.proDetails.americanFC.freight)

                        // console.log(this.proDetails.chinesePRE);
                        // console.log(this.proDetails.chinesePRE.productTitle);
                        // console.log(this.proDetails.chinesePRE[productTitle]);

                    } else {
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            })
        },
        // 获取产品回收站
        getProStation:function () {
            $.ajax({
                url: '../../product/imageaddress/querydeleteimage',
                type: 'get',
                data: {
                    'productId': this.id
                },
                dataType: 'json',
                success: function (r) {
                    // console.log(r);
                    if (r.code === 0) {
                        console.log('产品回收站');
                        console.log(r);
                        vm.proStation = r.isdeleteList;

                    } else {
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            })
        },
        // 获取产品相册
        getProAlbum:function () {
            $.ajax({
                url: '../../product/imageaddress/imageinfo',
                type: 'get',
                data: {
                    'productId': this.id
                },
                dataType: 'json',
                success: function (r) {
                    // console.log(r);
                    if (r.code === 0) {
                        console.log('产品相册');
                        console.log(r);
                        vm.proAlbum = [];
                        r.imageInfo.forEach(function (item,index) {
                            vm.proAlbum.push({
                                index:index,
                                imgId:item.imageId,
                                url:item.imageUrl,
                                isDeleted:item.isDeleted,
                                createUserId:item.createUserId,
                                createTime:item.createTime,
                                lastOperationTime:item.lastOperationTime,
                                lastOperationUserId:item.lastOperationUserId,
                                productId:item.productId,
                                status:item.status,
                                uid:item.uid

                            })
                        })
                        // vm.proAlbum = r.imageInfo;

                    } else {
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            })
        },
        // 保存相册
        saveXc:function(){
            var arr = [];
            var el = $('.imgDiv>.imgAlbum');
            for(var i = 0;i<el.length;i++){
                var _index = el.eq(i).attr('data-index');
                arr.push({
                    'imageId':vm.proAlbum[_index].imgId,
                    'imageUrl':vm.proAlbum[_index].url,
                    'isDeleted':vm.proAlbum[_index].isDeleted,
                    'createUserId':vm.proAlbum[_index].createUserId,
                    'createTime':vm.proAlbum[_index].createTime,
                    'lastOperationTime':vm.proAlbum[_index].lastOperationTime,
                    'lastOperationUserId':vm.proAlbum[_index].lastOperationUserId,
                    'productId':vm.proAlbum[_index].productId,
                    'status':vm.proAlbum[_index].status,
                    'uid':vm.proAlbum[_index].uid,
                })
            }
            $.ajax({
                url: '../../product/imageaddress/locationsave',
                type: 'post',
                data: JSON.stringify(arr),
                // dataType: 'json',
                contentType: "application/json",
                success: function (r) {
                    // console.log(r);
                    if (r.code === 0) {
                        console.log('产品相册');
                        console.log(r);
                        vm.proAlbum = [];
                        r.imageInfo.forEach(function (item,index) {
                            vm.proAlbum.push({
                                index:index,
                                imgId:item.imageId,
                                url:item.imageUrl,
                                isDeleted:item.isDeleted,
                                createUserId:item.createUserId,
                                createTime:item.createTime,
                                lastOperationTime:item.lastOperationTime,
                                lastOperationUserId:item.lastOperationUserId,
                                productId:item.productId,
                                status:item.status,
                                uid:item.uid

                            })
                        })
                        // vm.proAlbum = r.imageInfo;

                    } else {
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            })
        },
        // 获取产品一级分类
        getProTypeOne:function () {
            $.ajax({
                type: 'post',
                url: '../../product/category/querycategoryone',
                contentType: "application/json",
                data: '',
                success: function (r) {
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
                                vm.proDetails.categoryOneId = parseInt(id);
                                vm.categoryOneName = $(event.target).attr('data-val');

                                // vm.categoryTwoList = r.categoryList;
                                // vm.nowproTypearr[0] = $(event.target).attr('data-val');
                            }else {
                                vm.categoryOneName = $(event.target).attr('data-val');
                                vm.proDetails.productCategory = vm.categoryOneName;

                                // console.log('&&&&');
                                // vm.nowproTypearr[0] = $(event.target).attr('data-val');
                                // vm.nowProTypeId = id;
                                // vm.nowProType = vm.nowproTypearr[0];
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
                                    vm.proDetails.categoryTwoId = parseInt(id);
                                    vm.categoryTwoName = $(event.target).attr('data-val');

                                    // vm.categoryThreeList = r.categoryList;
                                    // vm.nowproTypearr[1] = $(event.target).attr('data-val');
                                }else {
                                    vm.categoryTwoName = $(event.target).attr('data-val');
                                    vm.proDetails.productCategory = vm.categoryOneName + '/'+ vm.categoryTwoName;
                                    // vm.nowproTypearr[1] = $(event.target).attr('data-val');
                                    // vm.nowProTypeId = id;
                                    // vm.nowProType = vm.nowproTypearr[0] + '/' + vm.nowproTypearr[1];
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
                    vm.proDetails.categoryThreeId = parseInt(id);
                    vm.categoryThreeName = $(event.target).attr('data-val');
                    vm.proDetails.productCategory = vm.categoryOneName + '/'+ vm.categoryTwoName + '/' + vm.categoryThreeName;
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

        // 成本运费
        getcostFreight:function (event) {
            if($(event.target).val() == ''){
                layer.msg('内容不能为空');
                $(event.target).val('');
            }else {
                var index = layer.load();
                var index = layer.load(1); //换了种风格
                var index = layer.load(2, {time: 10*1000}); //又换了种风格，并且设定最长等待10秒
                $.ajax({
                    type: 'post',
                    url: '../../product/products/costFreight',
                    contentType: "application/json",
                    data: JSON.stringify(vm.proDetails),
                    success: function (r) {
                        console.log('成本运费');
                        console.log(r)
                        if (r.code == 0) {
                            console.log('成本运费成功')
                            vm.proDetails.americanFC = r.productsEntity.americanFC;
                            vm.proDetails.canadaFC = r.productsEntity.canadaFC;
                            vm.proDetails.mexicoFC = r.productsEntity.mexicoFC;
                            vm.proDetails.britainFC = r.productsEntity.britainFC;
                            vm.proDetails.franceFC = r.productsEntity.franceFC;
                            vm.proDetails.germanyFC = r.productsEntity.germanyFC;
                            vm.proDetails.italyFC = r.productsEntity.italyFC;
                            vm.proDetails.spainFC = r.productsEntity.spainFC;
                            vm.proDetails.japanFC = r.productsEntity.japanFC;
                            vm.proDetails.australiaFC = r.productsEntity.australiaFC;
                            // this.costFreight = r.categoryOneList;
                            layer.close(index);

                            // console.log(vm.categoryOneList)
                        } else {
                            alert(r.msg);
                            layer.close(index);
                        }
                    }
                });
            }

        },
        // 刷新 利润，利润率
        lirFunc:function () {
            var index = layer.load();
            var index = layer.load(1); //换了种风格
            var index = layer.load(2, {time: 10*1000}); //又换了种风格，并且设定最长等待10秒
            $.ajax({
                type: 'post',
                url: '../../product/products/refresh',
                contentType: "application/json",
                data: JSON.stringify(vm.proDetails),
                success: function (r) {
                    console.log('利润');
                    console.log(r)
                    if (r.code == 0) {
                        // console.log('成本运费成功')
                        vm.proDetails.americanFC = r.productsEntity.americanFC;
                        vm.proDetails.canadaFC = r.productsEntity.canadaFC;
                        vm.proDetails.mexicoFC = r.productsEntity.mexicoFC;
                        vm.proDetails.britainFC = r.productsEntity.britainFC;
                        vm.proDetails.franceFC = r.productsEntity.franceFC;
                        vm.proDetails.germanyFC = r.productsEntity.germanyFC;
                        vm.proDetails.italyFC = r.productsEntity.italyFC;
                        vm.proDetails.spainFC = r.productsEntity.spainFC;
                        vm.proDetails.japanFC = r.productsEntity.japanFC;
                        vm.proDetails.australiaFC = r.productsEntity.australiaFC;
                        // this.costFreight = r.categoryOneList;
                        layer.close(index);
                        // console.log(vm.categoryOneList)
                    } else {
                        alert(r.msg);
                        layer.close(index);
                    }
                }
            });
        },
        // 售价修改
        lirFunc1:function () {
            var index = layer.load();
            var index = layer.load(1); //换了种风格
            var index = layer.load(2, {time: 10*1000}); //又换了种风格，并且设定最长等待10秒
            $.ajax({
                type: 'post',
                url: '../../product/products/refreshProfitRate',
                contentType: "application/json",
                data: JSON.stringify(vm.proDetails),
                success: function (r) {
                    console.log('利润');
                    console.log(r)
                    if (r.code == 0) {
                        // console.log('成本运费成功')
                        vm.proDetails.americanFC = r.productsEntity.americanFC;
                        vm.proDetails.canadaFC = r.productsEntity.canadaFC;
                        vm.proDetails.mexicoFC = r.productsEntity.mexicoFC;
                        vm.proDetails.britainFC = r.productsEntity.britainFC;
                        vm.proDetails.franceFC = r.productsEntity.franceFC;
                        vm.proDetails.germanyFC = r.productsEntity.germanyFC;
                        vm.proDetails.italyFC = r.productsEntity.italyFC;
                        vm.proDetails.spainFC = r.productsEntity.spainFC;
                        vm.proDetails.japanFC = r.productsEntity.japanFC;
                        vm.proDetails.australiaFC = r.productsEntity.australiaFC;
                        // this.costFreight = r.categoryOneList;
                        layer.close(index);
                        // console.log(vm.categoryOneList)
                    } else {
                        layer.close(index);
                        alert(r.msg);

                    }
                }
            });
        },
        // 图片回收站
        prostation:function () {
            vm.getProStation();
            if(vm.proStation.length == 0){
                layer.msg("暂无回收站图片");
            }else {
                layer.open({
                    type: 1,
                    title: false,
                    content: $('#proStation'), //这里content是一个普通的String
                    skin: 'openClass openClass1',
                    area: ['800px', '500px'],
                    shadeClose: true,
                    scrollbar:false,
                    btn: ['彻底删除','<i class="layui-icon layui-icon-refresh"></i> 恢复','取消',],
                    btn1: function (index) {
                        var activeImg = $('#proStation .proStationUl li.action');
                        var arr = [];
                        for(var i = 0;i<activeImg.length;i++){
                            arr.push(activeImg.eq(i).attr('data-index'));
                        }
                        if(arr.length == 0){
                            layer.msg("请选择要删除的图片");
                        }else {
                            layer.confirm('确定删除吗',function (index) {
                                $.ajax({
                                    url: '../../product/imageaddress/deleteimage',
                                    type: 'post',
                                    data: JSON.stringify(arr),
                                    // dataType: 'json',
                                    contentType: "application/json",
                                    success: function (r) {
                                        console.log('彻底删除');
                                        console.log(r);
                                        console.log(arr);
                                        if (r.code === 0) {
                                            // 重新获取图片回收站
                                            vm.getProStation();
                                            layer.msg("删除成功");
                                            // vm.proAlbum = r.imageInfo;

                                        } else {
                                            layer.alert(r.msg);
                                        }
                                    },
                                    error: function () {
                                        layer.msg("网络故障");
                                    }
                                })
                            })
                        }


                    },
                    btn2: function (index) {
                        var activeImg = $('#proStation .proStationUl li.action');
                        var arr = [];
                        for(var i = 0;i<activeImg.length;i++){
                            arr.push(activeImg.eq(i).attr('data-index'));
                        }
                        if(arr.length == 0){
                            layer.msg("请选择要恢复的图片");
                        }else {
                            layer.confirm('确定要恢复吗',function (index) {
                                $.ajax({
                                    url: '../../product/imageaddress/recoverdelete',
                                    type: 'post',
                                    data: JSON.stringify(arr),
                                    // dataType: 'json',
                                    contentType: "application/json",
                                    success: function (r) {
                                        // console.log('恢复图片');
                                        // console.log(r);
                                        // console.log(arr);
                                        if (r.code === 0) {
                                            // 重新获取图片回收站
                                            vm.getProStation();
                                            vm.getProAlbum();
                                            layer.close(index);
                                            layer.msg("恢复成功");
                                            // vm.proAlbum = r.imageInfo;

                                        } else {
                                            layer.alert(r.msg);
                                        }
                                    },
                                    error: function () {
                                        layer.msg("网络故障");
                                    }
                                })
                            })
                        }

                        // vm.proAlbum = [];
                        // var _length = $('.proStationUl li.action').length;
                        // console.log(_length);
                        // for(var i =0;i<_length;i++){
                        //     var _index = $('.proStationUl li.action').eq(i).attr('data-index');
                        //     vm.proAlbum.push({
                        //         url:vm.proStation[_index].url
                        //     })
                        // }
                        // console.log(vm.proAlbum)
                        // layer.close(index)
                    },
                    btn3: function (index) {
                        // vm.proAlbum = vm.proStation
                        layer.close(index);
                        return false

                    }
                });
            }

        },
        // 点击每个图片选中（变体参数选中图片弹框）
        activeImg:function (i) {
            $('#selImg .proStationUl li').eq(i).toggleClass('action');

        },
        // 点击图片相册选中
        clickActiveImg:function (event) {
            console.log(111);
            $(event.target).parent().toggleClass('active');
        },
        // 相册图片删除
        proDel:function () {
            var elList = $('.imgAlbum.active');
            var arr = [];
            for(var i = 0;i<elList.length;i++){
                arr.push(elList.eq(i).attr('data-index'));
            }
            console.log('删除图片id');
            console.log(arr);
            if(arr.length == 0){
                layer.msg('请选择要删除的图片');
            }else {
                layer.confirm('确定删除吗？',function () {
                    $.ajax({
                        type: 'post',
                        url: '../../product/imageaddress/updateimage',
                        contentType: "application/json",
                        // data: {
                        //     'imageIds':arr
                        // },
                        data: JSON.stringify(arr),
                        success: function (r) {
                            console.log('删除图片');
                            console.log(r);
                            // console.log(vm.proDetails);
                            if (r.code == 0) {
                                // 重新获取图片相册
                                vm.getProAlbum();
                                console.log('@@@@@@')
                                console.log(vm.proAlbum)
                                // vm.proDetails.productSku = r.SKU;

                                // layer.close(index);
                                layer.msg('删除成功');

                                // window.location.href = document.referrer;

                            } else {
                                alert(r.msg);
                            }
                        }
                    });
                })
            }



        },
        // 获取选中的变体参数图片
        getSelImgVerFunc:function () {
            var els = $('#selImg .proStationUl li.action');
            var arr = [];
            for(var i = 0;i<els.length;i++){
                arr.push({
                    id:els.eq(i).attr('data-index'),
                    url:els.eq(i).attr('data-url')
                })
            }
            // console.log($('#selImg .proStationUl li.active'));
            // console.log(arr);
            return arr
        },
        // 变体参数选择图片
        selImgCFcun:function (data) {
            if(vm.proAlbum.length == 0){
                layer.msg('暂无可选图片');
            }else {
                layer.open({
                    type: 1,
                    title: false,
                    content: $('#selImg'), //这里content是一个普通的String
                    skin: 'openClass',
                    area: ['800px', '500px'],
                    shadeClose: true,
                    scrollbar:false,
                    btn: ['确定','取消',],
                    btn1: function (index) {

                        $('.ul1 li').css({
                            'position':'relative',
                            'top':'0px',
                            'left':'0px',
                            'margin':'10px'
                        });
                        console.log(data);
                        data.img=[];
                        var arr = vm.getSelImgVerFunc();
                        arr.forEach(function (v) {
                            data.img.push(v.url)
                        })
                        // vm.recommendAll.forEach(function (t) {
                        //     if(t.id = data.id){
                        //         t.img = data.img;
                        //     }
                        // })
                        // console.log(vm.recommendAll);
                        $('#selImg .proStationUl li').removeClass('action');

                        layer.close(index)
                    },
                    btn2: function (index) {
                        // vm.proAlbum = vm.proStation
                        layer.close(index);
                        return false

                    }
                });
            }

        },
        // 一键添加（变体图片）
        yijianAddFunc:function (data) {

            if(vm.proAlbum.length == 0){
                layer.msg('暂无可选图片');
            }else{
                layer.confirm('确定添加所有图片吗？',function (index) {
                    $('.ul1 li').css({
                        'position':'relative',
                        'top':'0px',
                        'left':'0px',
                        'margin':'10px'
                    });

                    data.img = [];
                    vm.proAlbum.forEach(function (t) {
                        data.img.push(t.url)
                    })
                    // setTimeout(function(){ vm.drapImg(); }, 1000);
                    layer.msg('添加成功');
                    layer.close(index);
                })
            }



        },
        // 变体添加
        addVariant:function () {
            layer.open({
                type: 1,
                title: false,
                content: $('#addVariant'), //这里content是一个普通的String
                skin: 'openClass',
                area: ['500px', '400px'],
                shadeClose: true,
                scrollbar:false,
                btn: ['<i class="layui-icon layui-icon-add-1"></i> 添加','取消'],
                btn1: function (index) {
                    var _id = $('#variantName option:selected').val();
                    var _name = $('#variantName option:selected').text();
                    var _typeName = $('#variantType').val();
                    var type = '';

                    var reg = new RegExp( '，' , "g" )
                    var str = _typeName.replace(reg,',');
                    _typeName = str;
                    console.log(_typeName);
                    console.log(_typeName.split(','));
                    // _typeName.join(',');
                    if(_id == '0'){
                        type = 'color'
                    }else {
                        type = 'size'
                    }

                    $.ajax({
                        type: 'get',
                        url: '../../product/variantparameter/save',
                        contentType: "application/json",
                        data: {
                            'productId':vm.id,
                            'paramsType':type,
                            'paramsValue':_typeName
                        },
                        success: function (r) {
                            console.log('添加变体');
                            console.log(r)
                            if (r.code == 0) {
                                // this.costFreight = r.categoryOneList;

                                console.log(vm.variantList);

                                if(vm.variantList.length == 0){
                                    vm.variantList.push({
                                        id:_id,
                                        name:_name,
                                        type:_typeName.split(',')
                                    })
                                }else if(vm.variantList.length == 1){
                                    if(vm.variantList[0].id == _id){
                                        var arr = vm.variantList[0].type= _typeName.split(',');
                                        // var arr = vm.variantList[0].type.concat(_typeName.split(','));
                                        vm.variantList[0].type = arr;
                                        // vm.variantList[i].type.push(_typeName.join(','))
                                    }else {
                                        vm.variantList.push({
                                            id:_id,
                                            name:_name,
                                            type:_typeName.split(',')
                                        })
                                    }

                                }else {
                                    for(var i = 0;i<vm.variantList.length;i++){
                                        if(vm.variantList[i].id == _id){
                                            // var arr = vm.variantList[i].type.concat(_typeName.split(','));
                                            var arr = vm.variantList[i].type= _typeName.split(',');
                                            vm.variantList[i].type = arr;
                                            // vm.variantList[i].type.push(_typeName.join(','))
                                        }
                                    }
                                }

                                if(_id == '0'){
                                    vm.proDetails.colorVP = {
                                        paramsId:r.variantParameterId,
                                        paramsType:'color',
                                        paramsValue:_typeName
                                    }
                                }else {
                                    vm.proDetails.sizeVP = {
                                        paramsId:r.variantParameterId,
                                        paramsType:'color',
                                        paramsValue:_typeName
                                    }
                                }

                                vm.getrecommendAll();


                                layer.msg("变体添加成功");
                            } else {
                                alert(r.msg);
                            }
                        }
                    });



                    // console.log(vm.recommendAll);
                    layer.close(index);

                },
                btn2: function (index) {


                }
            });
        },
        choicePro:function (i) {
            // if($('.proStationUl li').eq(i).attr('data-ok') == 'false'){
            //     $('.proStationUl li').eq(i).addClass('action');
            // }else {
            //     $('.proStationUl li').eq(i).removeClass('action');
            // }
            $('#proStation .proStationUl li').eq(i).toggleClass('action');
        },
        imgItem:function (index) {
            $('.imgDiv>div').eq(index).find('img').css('opacity','.5');
            $('.imgDiv>div').eq(index).find('img').parent().find('i').css('display','inline-block');
            $('.imgDiv>div').eq(index).find('i.imgDel').mouseover(function () {
                $(this).prev().css('opacity','.5');
                $(this).css('display','inline-block');
            })
            // 删除当前图片
            $('.imgDiv>div').eq(index).find('i.imgDel').click(function () {
                // console.log(111)
                // console.log($('.imgDiv>div').eq(index))
                // var arr = vm.proAlbum;
                // var _index = $(this).parent().attr('data-index');
                // arr.splice(_index,1);
                // $(this).parent().remove();
                // console.log(arr)
                // return
            })
        },
        imgItemmouseout:function (index) {
            $('.imgDiv>div').eq(index).find('img').css('opacity','1');
            $('.imgDiv>div').eq(index).find('img').parent().find('i').css('display','none');
        },
        variantName:function (event) {
            console.log(event);
            var el = $(event.target);
            el.next().css('visibility','visible');
            el.next().mouseover(function () {
                el.next().css('visibility','visible');
            })
        },
        variantNamenone:function (event) {
            $(event.target).next().css('visibility','hidden');
        },

        // 删除变体参数
        delvariantName:function (index) {
            console.log(index);
            var _id = $('.variantName a').eq(index).attr('data-id');
            var _index;
            vm.variantList.forEach(function (value,key) {
                if(value.id == _id){
                    _index = key;
                }
                return
            })

            // $('.variantName i').eq(_index).remove();


            if(_id == '0'){
                console.log(vm.id);
                console.log(typeof (vm.id));
                console.log(vm.proDetails.colorVP.paramsId);
                console.log(typeof (vm.proDetails.colorVP.paramsId));
                console.log(vm.proDetails.colorVP.paramsType);
                console.log(vm.proDetails.colorVP.paramsValue);
                $.ajax({
                    type: 'post',
                    url: '../../product/variantparameter/delete',
                    contentType: "application/json",
                    // dataType: 'json',
                    data: JSON.stringify({
                        'productId':vm.id,
                        'variantParameter':{
                            'paramsId':vm.proDetails.colorVP.paramsId,
                            'paramsType':vm.proDetails.colorVP.paramsType,
                            'paramsValue':vm.proDetails.colorVP.paramsValue,
                        }
                    }),
                    success: function (r) {
                        // console.log('删除变体');
                        // console.log(r)
                        if (r.code == 0) {
                            layer.confirm('确定删除改参数吗？', function(index){
                                //do something
                                vm.variantList.splice(_index,1);
                                vm.getrecommendAll();

                                layer.close(index);
                                layer.msg('删除成功');
                            });

                            // console.log('删除变体成功');
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            }else {
                console.log(vm.id);
                console.log(typeof (vm.id));
                console.log(vm.proDetails.sizeVP.paramsId);
                console.log(typeof (vm.proDetails.sizeVP.paramsId));
                console.log(vm.proDetails.sizeVP.paramsType);
                console.log(vm.proDetails.sizeVP.paramsValue);
                $.ajax({
                    type: 'post',
                    url: '../../product/variantparameter/delete',
                    contentType: "application/json",
                    data: JSON.stringify({
                        'productId':vm.id,
                        'variantParameter':{
                            'paramsId':vm.proDetails.sizeVP.paramsId,
                            'paramsType':vm.proDetails.sizeVP.paramsType,
                            'paramsValue':vm.proDetails.sizeVP.paramsValue,
                        }
                    }),
                    success: function (r) {
                        // console.log('删除变体');
                        // console.log(r)
                        if (r.code == 0) {
                            layer.confirm('确定删除改参数吗？', function(index){
                                //do something
                                vm.variantList.splice(_index,1);
                                vm.getrecommendAll();

                                layer.close(index);
                                layer.msg('删除成功');
                            });
                            // vm.variantList.splice(_index,1);
                            // console.log('删除变体成功')
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            }

        },
        // 修改变体参数
        upVariant:function (a) {
            vm.upVariantList.id = vm.variantList[a].id;
            vm.upVariantList.type = vm.variantList[a].type.join(',');
            layer.open({
                type: 1,
                title: false,
                content: $('#upVariant'), //这里content是一个普通的String
                skin: 'openClass',
                area: ['500px', '400px'],
                shadeClose: true,
                scrollbar:false,
                btn: ['<i class="layui-icon layui-icon-ok-circle"></i> 保存','取消'],
                btn1: function (index) {
                    var _id = $('#variantName1 option:selected').val();
                    var _name = $('#variantName1 option:selected').text();
                    var _typeName = $('#variantType1').val();
                    var reg = new RegExp( '，' , "g" )
                    var str = _typeName.replace(reg,',');
                    _typeName = str;


                    var type = '';
                    var id;
                    if(_id == '0'){
                        type = 'color';
                        id = vm.proDetails.colorVP.paramsId;
                    }else {
                        type = 'size';
                        id = vm.proDetails.sizeVP.paramsId
                    }


                    $.ajax({
                        type: 'post',
                        url: '../../product/variantparameter/update',
                        contentType: "application/json",
                        data: JSON.stringify({
                            'paramsId':id,
                            'paramsType':type,
                            'paramsValue':_typeName
                        }),
                        success: function (r) {
                            console.log('修改变体');
                            console.log(r)
                            if (r.code == 0) {
                                // this.costFreight = r.categoryOneList;

                                console.log(vm.variantList);

                                if(vm.variantList.length == 0){
                                    vm.variantList.push({
                                        id:_id,
                                        name:_name,
                                        type:_typeName.split(',')
                                    })
                                }else if(vm.variantList.length == 1){
                                    if(vm.variantList[0].id == _id){
                                        var arr = vm.variantList[0].type = _typeName.split(',');
                                        vm.variantList[0].type = arr;
                                        // vm.variantList[i].type.push(_typeName.join(','))
                                    }else {
                                        vm.variantList.push({
                                            id:_id,
                                            name:_name,
                                            type:_typeName.split(',')
                                        })
                                    }

                                }else {
                                    for(var i = 0;i<vm.variantList.length;i++){
                                        if(vm.variantList[i].id == _id){
                                            var arr = vm.variantList[i].type = _typeName.split(',');
                                            vm.variantList[i].type = arr;
                                            // vm.variantList[i].type.push(_typeName.join(','))
                                        }
                                    }
                                }

                                if(_id == '0'){
                                    vm.proDetails.colorVP = {
                                        paramsId:id,
                                        paramsType:'color',
                                        paramsValue:_typeName
                                    }
                                }else {
                                    vm.proDetails.sizeVP = {
                                        paramsId:id,
                                        paramsType:'color',
                                        paramsValue:_typeName
                                    }
                                }

                                vm.getrecommendAll();


                                layer.msg("变体修改成功");
                            } else {
                                alert(r.msg);
                            }
                        }
                    });








                    // console.log(_typeName.split(','));
                    // _typeName.join(',');
                    // if(vm.variantList.length == 0){
                    //     vm.variantList.push({
                    //         id:_id,
                    //         name:_name,
                    //         type:_typeName.split(',')
                    //     })
                    // }else {
                    //     for(var i = 0;i<vm.variantList.length;i++){
                    //         if(vm.variantList[i].id == _id){
                    //             var arr = vm.variantList[i].type.concat(_typeName.split(','));
                    //             vm.variantList[i].type = arr;
                    //             // vm.variantList[i].type.push(_typeName.join(','))
                    //         }
                    //     }
                    // }
                    // console.log(vm.variantList);
                    layer.close(index);

                },
                btn2: function (index) {


                }
            });
        },
        // 点击添加变体信息
        clickAddV:function (event) {
            console.log(11111);
            console.log($(event.target).text());
            // vm.upVariantList = vm.variantList[a];
            // $('#variantName1 option:selected').val();
            // var _name = $('#variantName1 option:selected').text();
            // var _typeName = $('#variantType1').val();
            // $(event.target).toggleClass('active');
            var _text = $('textarea.variantType').val();
            if(_text != ''){
                _text += ','+$(event.target).attr('data-val');
            }else {
                _text += $(event.target).attr('data-val');
            }
            // _text += ','+$(event.target).text();
            $('textarea.variantType').val(_text);
        },
        // 点击添加变体信息
        clickAddV1:function (event) {
            console.log(11111);
            console.log($(event.target).text());
            // vm.upVariantList = vm.variantList[a];
            // $('#variantName1 option:selected').val();
            // var _name = $('#variantName1 option:selected').text();
            // var _typeName = $('#variantType1').val();
            // $(event.target).toggleClass('active');
            var _text = $('textarea.variantType').eq(1).val();
            if(_text != ''){
                _text += ','+$(event.target).attr('data-val');
            }else {
                _text += $(event.target).attr('data-val');
            }
            // _text += ','+$(event.target).text();
            $('textarea.variantType').val(_text);
        },
        // 图片拖拽
        drapImg:function () {
            for(var nn = 0;nn<$('.ul1').length;nn++){
                //      console.log(nn);
                var _index = nn;
                var aLi = $(".ul1").eq(_index).find('li');
                if(aLi.length != 0){
                    var aLiLast = $(".ul1").eq(_index).find('li:last-child');
                    // console.log(aLiLast);
                    var disX = 0;
                    var disY = 0;
                    var minZindex = 1;
                    var aPos = [];
                    for(var i = 0; i < aLi.length; i++) {
                        var t = aLi[i].offsetTop;
                        var l = aLi[i].offsetLeft;
                        aLi[i].style.top = t + "px";
                        aLi[i].style.left = l + "px";
                        aPos[i] = {
                            left: l,
                            top: t
                        };
                        aLi[i].index = i;
                    }
                    for(var i = 0; i < aLi.length; i++) {
                        aLi[i].style.position = "absolute";
                        aLi[i].style.margin = 0;
                        setDrag(aLi[i],aLi);
                    }

                    // if(aLi.length != 0){
                    //     // var _height = aLiLast[0].offsetTop+60;
                    //     var _height = aLiLast[0].offsetTop+60;
                    //     aLiLast.parent().css('height',_height+'px');
                    //     aLiLast.parent().parent().siblings().css('line-height',_height+'px')
                    // }
                    var _height = aLiLast[0].offsetTop+60;
                    aLiLast.parent().css('height',_height+'px');
                    aLiLast.parent().parent().siblings().css('line-height',_height+'px')

                    // console.log(_height);


                    aLi.mouseover(function () {
                        $(this).find('i').css('display','inline-block');
                        $(this).find('i').mouseover(function () {
                            $(this).css('display','inline-block');
                        })
                        $(this).find('i').mouseout(function () {
                            $(this).css('display','none');
                        })
                    })
                    aLi.mouseout(function () {
                        $(this).find('i').css('display','none');
                        $(this).find('i').mouseover(function () {
                            $(this).css('display','inline-block');
                        })
                        $(this).find('i').mouseout(function () {
                            $(this).css('display','none');
                        })
                    })
                }

            }
            //拖拽
            function setDrag(obj,all) {
                obj.onmouseover = function() {
                    obj.style.cursor = "move";
                }
                obj.onmousedown = function(event) {
//						获取滚动条位置
                    var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
                    var scrollLeft = document.documentElement.scrollLeft || document.body.scrollLeft;
//						设置当前拖拽元素位于最上方
                    obj.style.zIndex = minZindex++;
                    //当鼠标按下时计算鼠标与拖拽对象的距离
                    disX = event.clientX + scrollLeft - obj.offsetLeft;
                    disY = event.clientY + scrollTop - obj.offsetTop;
                    document.onmousemove = function(event) {
                        all.removeClass('active');
                        //当鼠标拖动时计算div的位置
                        var l = event.clientX - disX + scrollLeft;
                        var t = event.clientY - disY + scrollTop;
                        obj.style.left = l + "px";
                        obj.style.top = t + "px";
                        /*for(var i=0;i<aLi.length;i++){
                            aLi[i].className = "";
                            if(obj==aLi[i])continue;//如果是自己则跳过自己不加红色虚线
                            if(colTest(obj,aLi[i])){
                                aLi[i].className = "active";
                            }
                        }*/
                        for(var i = 0; i < aLi.length; i++) {
                            aLi[i].className = "";
                        }
//							找到距离最近的元素
                        var oNear = findMin(obj,all);
//							给距离最近的元素添加选中类名active
                        if(oNear) {
                            oNear.className = "active";
                        }
                    }
                    document.onmouseup = function() {
                        document.onmousemove = null; //当鼠标弹起时移出移动事件
                        document.onmouseup = null; //移出up事件，清空内存
                        //检测是否普碰上，在交换位置
                        var oNear = findMin(obj,all);
                        if(oNear) {
                            oNear.className = "";
                            oNear.style.zIndex = minZindex++;
                            obj.style.zIndex = minZindex++;
//								互换位置
                            startMove(oNear, aPos[obj.index]);
                            startMove(obj, aPos[oNear.index]);
                            //交换index
                            oNear.index += obj.index;
                            obj.index = oNear.index - obj.index;
                            oNear.index = oNear.index - obj.index;
//								var _indexN = $(oNear).attr('data-index');
                            var _indexN = oNear.getAttribute('data-index');
//								var _index = $(obj).attr('data-index');
                            var _index = obj.getAttribute('data-index');
                            oNear.setAttribute('data-index',_index);
//								$(oNear).attr('data-index',_index);
                            obj.setAttribute('data-index',_indexN);
//								$(obj).attr('data-index',_indexN);
                        } else {
//								没有选中的最近元素,则拖拽元素返回自己原本的位置
                            startMove(obj, aPos[obj.index]);
                        }
                    }
                    clearInterval(obj.timer);
                    return false; //低版本出现禁止符号
                }
            }
            //碰撞检测
            function colTest(obj1, obj2) {
                var t1 = obj1.offsetTop;
                var r1 = obj1.offsetWidth + obj1.offsetLeft;
                var b1 = obj1.offsetHeight + obj1.offsetTop;
                var l1 = obj1.offsetLeft;

                var t2 = obj2.offsetTop;
                var r2 = obj2.offsetWidth + obj2.offsetLeft;
                var b2 = obj2.offsetHeight + obj2.offsetTop;
                var l2 = obj2.offsetLeft;

                if(t1 > b2 || r1 < l2 || b1 < t2 || l1 > r2) {
                    return false;
                } else {
                    return true;
                }
            }
            //勾股定理求距离
            function getDis(obj1, obj2) {
                var a = obj1.offsetLeft - obj2.offsetLeft;
                var b = obj1.offsetTop - obj2.offsetTop;
                return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
            }
            //找到距离最近的
            function findMin(obj,all) {
                var minDis = 999999999;
                var minIndex = -1;
                for(var i = 0; i < all.length; i++) {
                    if(obj == all[i]) continue;
                    if(colTest(obj, all[i])) {
                        var dis = getDis(obj, all[i]);
                        if(dis < minDis) {
                            minDis = dis;
                            minIndex = i;
                        }
                    }
                }
                if(minIndex == -1) {
                    return null;
                } else {
                    return all[minIndex];
                }
            }

            //通过class获取元素
            function getClass(cls){
                var ret = [];
                var els = document.getElementsByTagName("*");
                for (var i = 0; i < els.length; i++){
                    //判断els[i]中是否存在cls这个className;.indexOf("cls")判断cls存在的下标，如果下标>=0则存在;
                    if(els[i].className === cls || els[i].className.indexOf("cls")>=0 || els[i].className.indexOf(" cls")>=0 || els[i].className.indexOf(" cls ")>0){
                        ret.push(els[i]);
                    }
                }
                return ret;
            }
            function getStyle(obj,attr){//解决JS兼容问题获取正确的属性值
                return obj.currentStyle?obj.currentStyle[attr]:getComputedStyle(obj,false)[attr];
            }
            function startMove(obj,json,fun){
                clearInterval(obj.timer);

                // console.log(11111);
                obj.timer = setInterval(function(){
                    var isStop = true;
                    for(var attr in json){
                        var iCur = 0;
                        //判断运动的是不是透明度值
                        if(attr=="opacity"){
                            iCur = parseInt(parseFloat(getStyle(obj,attr))*100);
                        }else{
                            iCur = parseInt(getStyle(obj,attr));
                        }
                        var ispeed = (json[attr]-iCur)/3;
                        //运动速度如果大于0则向下取整，如果小于0想上取整；
                        ispeed = ispeed>0?Math.ceil(ispeed):Math.floor(ispeed);
                        //判断所有运动是否全部完成
                        if(iCur!=json[attr]){
                            isStop = false;
                        }
                        //运动开始
                        if(attr=="opacity"){
                            obj.style.filter = "alpha:(opacity:"+(json[attr]+ispeed)+")";
                            obj.style.opacity = (json[attr]+ispeed)/100;
                        }else{
                            obj.style[attr] = iCur+ispeed+"px";
                        }
                    }
                    //判断是否全部完成
                    if(isStop){
                        clearInterval(obj.timer);
                        if(fun){
                            fun();
                        }
                    }
                },30);
            }

        },
        // 产品相册图片拖拽
        drapImg1:function () {


            // for(var nn = 0;nn<$('.ul1').length;nn++){
                //      console.log(nn);
                // var _index = nn;
                var aLi = $(".imgDiv>.imgAlbum");
                if(aLi.length != 0){
                    console.log($(".imgDiv>.imgAlbum"));
                    var aLiLast = $(".imgDiv").find('.imgAlbum:last-child');
                    // console.log(aLiLast);
                    var disX = 0;
                    var disY = 0;
                    var minZindex = 1;
                    var aPos = [];
                    for(var i = 0; i < aLi.length; i++) {
                        var t = aLi[i].offsetTop;
                        var l = aLi[i].offsetLeft;
                        aLi[i].style.top = t + "px";
                        aLi[i].style.left = l + "px";
                        aPos[i] = {
                            left: l,
                            top: t
                        };
                        aLi[i].index = i;
                    }
                    for(var i = 0; i < aLi.length; i++) {
                        console.log(i);
                        aLi[i].style.position = "absolute";
                        aLi[i].style.margin = 0;
                        setDrag(aLi[i],aLi);
                    }

                    // if(aLi.length != 0){
                    //     // var _height = aLiLast[0].offsetTop+60;
                    //     var _height = aLiLast[0].offsetTop+60;
                    //     aLiLast.parent().css('height',_height+'px');
                    //     aLiLast.parent().parent().siblings().css('line-height',_height+'px')
                    // }
                    var _height = aLiLast[0].offsetTop+140;
                    aLiLast.parent().css('height',_height+'px');
                    // aLiLast.parent().parent().siblings().css('line-height',_height+'px')

                    // console.log(_height);


                    // aLi.mouseover(function () {
                    //     $(this).find('i').css('display','inline-block');
                    //     $(this).find('i').mouseover(function () {
                    //         $(this).css('display','inline-block');
                    //     })
                    //     $(this).find('i').mouseout(function () {
                    //         $(this).css('display','none');
                    //     })
                    // })
                    // aLi.mouseout(function () {
                    //     $(this).find('i').css('display','none');
                    //     $(this).find('i').mouseover(function () {
                    //         $(this).css('display','inline-block');
                    //     })
                    //     $(this).find('i').mouseout(function () {
                    //         $(this).css('display','none');
                    //     })
                    // })
                }

            // }
            //拖拽
            function setDrag(obj,all) {
                obj.onmouseover = function() {
                    obj.style.cursor = "move";
                }
                obj.onmousedown = function(event) {
//						获取滚动条位置
                    var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
                    var scrollLeft = document.documentElement.scrollLeft || document.body.scrollLeft;
//						设置当前拖拽元素位于最上方
                    obj.style.zIndex = minZindex++;
                    //当鼠标按下时计算鼠标与拖拽对象的距离
                    disX = event.clientX + scrollLeft - obj.offsetLeft;
                    disY = event.clientY + scrollTop - obj.offsetTop;
                    document.onmousemove = function(event) {
                        all.removeClass('active');
                        //当鼠标拖动时计算div的位置
                        var l = event.clientX - disX + scrollLeft;
                        var t = event.clientY - disY + scrollTop;
                        obj.style.left = l + "px";
                        obj.style.top = t + "px";
                        /*for(var i=0;i<aLi.length;i++){
                            aLi[i].className = "";
                            if(obj==aLi[i])continue;//如果是自己则跳过自己不加红色虚线
                            if(colTest(obj,aLi[i])){
                                aLi[i].className = "active";
                            }
                        }*/
                        for(var i = 0; i < aLi.length; i++) {
                            aLi[i].className = "";
                        }
//							找到距离最近的元素
                        var oNear = findMin(obj,all);
//							给距离最近的元素添加选中类名active
                        if(oNear) {
                            oNear.className = "active";
                        }
                    }
                    document.onmouseup = function() {
                        document.onmousemove = null; //当鼠标弹起时移出移动事件
                        document.onmouseup = null; //移出up事件，清空内存
                        //检测是否普碰上，在交换位置
                        var oNear = findMin(obj,all);
                        if(oNear) {
                            oNear.className = "";
                            oNear.style.zIndex = minZindex++;
                            obj.style.zIndex = minZindex++;
//								互换位置
                            startMove(oNear, aPos[obj.index]);
                            startMove(obj, aPos[oNear.index]);
                            //交换index
                            oNear.index += obj.index;
                            obj.index = oNear.index - obj.index;
                            oNear.index = oNear.index - obj.index;
//								var _indexN = $(oNear).attr('data-index');
                            var _indexN = oNear.getAttribute('data-index');
//								var _index = $(obj).attr('data-index');
                            var _index = obj.getAttribute('data-index');
                            oNear.setAttribute('data-index',_index);
//								$(oNear).attr('data-index',_index);
                            obj.setAttribute('data-index',_indexN);
//								$(obj).attr('data-index',_indexN);
                        } else {
//								没有选中的最近元素,则拖拽元素返回自己原本的位置
                            startMove(obj, aPos[obj.index]);
                        }
                    }
                    clearInterval(obj.timer);
                    return false; //低版本出现禁止符号
                }
            }
            //碰撞检测
            function colTest(obj1, obj2) {
                var t1 = obj1.offsetTop;
                var r1 = obj1.offsetWidth + obj1.offsetLeft;
                var b1 = obj1.offsetHeight + obj1.offsetTop;
                var l1 = obj1.offsetLeft;

                var t2 = obj2.offsetTop;
                var r2 = obj2.offsetWidth + obj2.offsetLeft;
                var b2 = obj2.offsetHeight + obj2.offsetTop;
                var l2 = obj2.offsetLeft;

                if(t1 > b2 || r1 < l2 || b1 < t2 || l1 > r2) {
                    return false;
                } else {
                    return true;
                }
            }
            //勾股定理求距离
            function getDis(obj1, obj2) {
                var a = obj1.offsetLeft - obj2.offsetLeft;
                var b = obj1.offsetTop - obj2.offsetTop;
                return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
            }
            //找到距离最近的
            function findMin(obj,all) {
                var minDis = 999999999;
                var minIndex = -1;
                for(var i = 0; i < all.length; i++) {
                    if(obj == all[i]) continue;
                    if(colTest(obj, all[i])) {
                        var dis = getDis(obj, all[i]);
                        if(dis < minDis) {
                            minDis = dis;
                            minIndex = i;
                        }
                    }
                }
                if(minIndex == -1) {
                    return null;
                } else {
                    return all[minIndex];
                }
            }

            //通过class获取元素
            function getClass(cls){
                var ret = [];
                var els = document.getElementsByTagName("*");
                for (var i = 0; i < els.length; i++){
                    //判断els[i]中是否存在cls这个className;.indexOf("cls")判断cls存在的下标，如果下标>=0则存在;
                    if(els[i].className === cls || els[i].className.indexOf("cls")>=0 || els[i].className.indexOf(" cls")>=0 || els[i].className.indexOf(" cls ")>0){
                        ret.push(els[i]);
                    }
                }
                return ret;
            }
            function getStyle(obj,attr){//解决JS兼容问题获取正确的属性值
                return obj.currentStyle?obj.currentStyle[attr]:getComputedStyle(obj,false)[attr];
            }
            function startMove(obj,json,fun){
                clearInterval(obj.timer);

                // console.log(11111);
                obj.timer = setInterval(function(){
                    var isStop = true;
                    for(var attr in json){
                        var iCur = 0;
                        //判断运动的是不是透明度值
                        if(attr=="opacity"){
                            iCur = parseInt(parseFloat(getStyle(obj,attr))*100);
                        }else{
                            iCur = parseInt(getStyle(obj,attr));
                        }
                        var ispeed = (json[attr]-iCur)/3;
                        //运动速度如果大于0则向下取整，如果小于0想上取整；
                        ispeed = ispeed>0?Math.ceil(ispeed):Math.floor(ispeed);
                        //判断所有运动是否全部完成
                        if(iCur!=json[attr]){
                            isStop = false;
                        }
                        //运动开始
                        if(attr=="opacity"){
                            obj.style.filter = "alpha:(opacity:"+(json[attr]+ispeed)+")";
                            obj.style.opacity = (json[attr]+ispeed)/100;
                        }else{
                            obj.style[attr] = iCur+ispeed+"px";
                        }
                    }
                    //判断是否全部完成
                    if(isStop){
                        clearInterval(obj.timer);
                        if(fun){
                            fun();
                        }
                    }
                },30);
            }

        },
        // 获取变体参数列表
        getrecommendAll:function () {
            this.recommendAll = [];
            vm.proDetails.variantsInfos = [];

            if(this.variantList.length == 2){
                var recommend;
                var recommend1;
                this.variantList.forEach(function (t) {
                    if(t.id == 0){
                        recommend = t.type;
                    }
                    if(t.id == 1){
                        recommend1 = t.type;
                    }
                })

                // console.log(recommend.length * recommend1.length);


                for(var i = 0;i<recommend.length;i++){
                    for(var j = 0;j<recommend1.length;j++){
                        vm.recommendAll.push({
                            id:i+j,
                            name:recommend[i]+'*'+recommend1[j],
                            img:[],
                            sku:'',
                            addPrice:'',
                            stock:(Math.round(Math.random()*100) + 100),
                            code:''
                        });

                        vm.proDetails.variantsInfos.push({
                            variantId:null,
                            productId:vm.id,
                            variantSort:i+j,
                            variantCombination:recommend[i]+'*'+recommend1[j],
                            variantSku:'',
                            variantAddPrice:null,
                            variantStock:(Math.round(Math.random()*100) + 100),
                            eanCode:null,
                            imageUrl:''
                        })

                    }
                }
            }else if(this.variantList.length == 1){
                var recommend = this.variantList[0].type;
                for(var i = 0;i<recommend.length;i++){
                    vm.recommendAll.push({
                        id:i,
                        name:recommend[i],
                        img:[],
                        sku:'',
                        addPrice:'',
                        stock:(Math.round(Math.random())*100 + 100),
                        code:''
                    });

                    vm.proDetails.variantsInfos.push({
                        variantId:null,
                        productId:vm.id,
                        variantSort:i,
                        variantCombination:recommend[i],
                        variantSku:'',
                        variantAddPrice:null,
                        variantStock:(Math.round(Math.random())*100 + 100),
                        eanCode:null,
                        imageUrl:''
                    })
                }
                // console.log(recommend.length);
            }

            console.log('1111111');
            // setTimeout(function(){ vm.drapImg(); }, 2000);

        },
        titleChange:function (event) {
            if($(event.target).val().length > 200){
                layer.msg('内容不能超过200个字符')
            }
        },
        titleChange1:function (event) {
            if($(event.target).val().length > 1000){
                layer.msg('内容不能超过1000个字符')
            }
        },
        titleChange2:function (event) {
            if($(event.target).val().length > 250){
                layer.msg('内容不能超过250个字符')
            }
        },
        titleChange3:function (event) {
            if($(event.target).val().length > 2000){
                layer.msg('内容不能超过2000个字符')
            }
        },
        // 修改保存
        savePro:function () {
            // var arr = {
            //     'a':'aa',
            //     'b':'bb',
            //     'c':'cc'
            // }

            // objectKeyIsEmpty(arr);

            // if(vm.proDetails.producerName == '' || vm.proDetails.brandName == '' || vm.proDetails.purchasePrice == '' || vm.proDetails.stock == '' || vm.proDetails.productWeight == ''){
            if(vm.proDetails.producerName == '' || vm.proDetails.brandName == '' || vm.proDetails.purchasePrice == '' || vm.proDetails.stock == '' || JSON.stringify(vm.proDetails.domesticFreight) == '' || JSON.stringify(vm.proDetails.productWeight) == '' || JSON.stringify(vm.proDetails.productLength) == '' || JSON.stringify(vm.proDetails.productWide) == '' || JSON.stringify(vm.proDetails.productHeight) == ''){
                layer.msg('厂商名称、品牌名称、库存数量、采购价格、国内运费、包装毛重、包装尺寸不能为空！！');
            }else {

                // if(vm.proDetails.chinesePRE.productTitle)
                if(JSON.stringify(vm.proDetails.chinesePRE.productTitle).length > 200){
                    layer.msg('产品标题内容不能超过200个字符')
                }else if(JSON.stringify(vm.proDetails.chinesePRE.keyWord).length > 250){
                    layer.msg('关键字内容不能超过250个字符')
                }else if(JSON.stringify(vm.proDetails.chinesePRE.keyPoints).length > 1000){
                    layer.msg('重点说明内容不能超过1000个字符')
                }else if(JSON.stringify(vm.proDetails.chinesePRE.productDescription).length > 2000){
                    layer.msg('产品描述内容不能超过2000个字符')
                }else {
                    // if(vm.)
                    objectKeyIsEmpty(vm.proDetails.francePRE);
                    objectKeyIsEmpty(vm.proDetails.spainPRE);
                    objectKeyIsEmpty(vm.proDetails.germanyPRE);
                    objectKeyIsEmpty(vm.proDetails.italyPRE);
                    objectKeyIsEmpty(vm.proDetails.britainPRE);
                    objectKeyIsEmpty(vm.proDetails.japanPRE);
                    objectKeyIsEmpty(vm.proDetails.chinesePRE);


                    if(vm.proDetails.manufacturerNumber == ''){
                        vm.proDetails.manufacturerNumber = ' ';
                    }
                    if(vm.proDetails.productSku == ''){
                        vm.proDetails.productSku = ' ';
                    }
                    if(vm.proDetails.productSource == ''){
                        vm.proDetails.productSource = ' ';
                    }
                    if(vm.proDetails.sellerLink == ''){
                        vm.proDetails.sellerLink = ' ';
                    }
                    if(vm.proDetails.productRemark == ''){
                        vm.proDetails.productRemark = ' ';
                    }
                    if(vm.proDetails.productAbbreviations == ''){
                        vm.proDetails.productAbbreviations = ' ';
                    }



                    var u = $('.ul1');
                    var shunx = [];
                    for(var j = 0;j<u.length;j++){
                        var arr = u.eq(j).find('li');
                        shunx[j] = []
                        for(var i = 0;i<arr.length;i++){
                            shunx[j].push(arr.eq(i).attr('data-index'))
                        }
                    }

                    // var arr = $('.ul1 li');
                    // var shunx = [];
                    // for(var i = 0;i<arr.length;i++){
                    //     shunx.push(arr.eq(i).attr('data-index'))
                    // }
                    console.log(shunx);

                    shunx.forEach(function (v,i) {
                        var t = vm.recommendAll[i];
                        console.log(t);
                        var string = '';
                        v.forEach(function (n) {
                            string+=t.img[n]+','
                        })
                        console.log('11111111');
                        // console.log(v.join(','));
                        vm.proDetails.variantsInfos[i].variantId = i;
                        vm.proDetails.variantsInfos[i].eanCode = t.code;
                        vm.proDetails.variantsInfos[i].variantAddPrice = parseInt(t.addPrice);
                        vm.proDetails.variantsInfos[i].variantSku = t.sku;
                        vm.proDetails.variantsInfos[i].variantStock = parseInt(t.stock);
                        vm.proDetails.variantsInfos[i].imageUrl = string.slice(0,string.length-1);
                    })

                    layer.confirm('确定修改吗？', function(index){
                        var index = layer.load();
                        var index = layer.load(1); //换了种风格
                        var index = layer.load(2, {time: 10*1000}); //又换了种风格，并且设定最长等待10秒
                        $.ajax({
                            type: 'post',
                            url: '../../product/products/modifyproduct',
                            contentType: "application/json",
                            data: JSON.stringify(vm.proDetails),
                            success: function (r) {
                                console.log('修改产品');
                                console.log(r);
                                console.log(vm.proDetails);
                                if (r.code == 0) {
                                    layer.close(index);

                                    window.location.href = document.referrer;

                                } else {
                                    layer.close(index);
                                    alert(r.msg);
                                }
                            }
                        });



                    });
                }


            }

            function objectKeyIsEmpty(obj) {
                // console.log(obj);
                // let empty = null;
                for (var aa in obj) {
                    if (obj[aa] == '') {
                        obj[aa] = ' '
                        console.log(obj[aa]);
                    }
                }
                // console.log(obj);
                // return empty;
            }





        },
        // 删除变体列表某条数据
        delVariantList:function (event) {
            layer.confirm('确定删除改组合信息吗？',function (index) {
                var _index = $(event.target).attr('data-index');
                // $(event.target).parent().parent().remove();
                var delId;
                for(var i = 0;i<vm.recommendAll.length;i++){
                    if(vm.recommendAll[i].id == _index){
                        delId = i;
                        // return
                    }
                }
                vm.recommendAll.splice(delId,1);
                vm.proDetails.variantsInfos.splice(delId,1);
                layer.msg('删除成功');
                layer.close(index);
            })


        },
        // 一键修正SKU
        skuChFunc:function () {
            $.ajax({
                type: 'get',
                url: '../../product/products/modifySKU',
                contentType: "application/json",
                data: {productId:vm.id},
                success: function (r) {
                    console.log('修改sku');
                    console.log(r);
                    // console.log(vm.proDetails);
                    if (r.code == 0) {
                        vm.proDetails.productSku = r.SKU;
                        layer.msg('修改成功');

                        // window.location.href = document.referrer;

                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        // 上传
        shangchuan:function () {
            if(vm.isUP){
                $("#demo1").zyUpload({
                    width            :   "780px",                 // 宽度
                    height           :   "400px",                 // 宽度
                    itemWidth        :   "100px",                 // 文件项的宽度
                    itemHeight       :   "100px",                 // 文件项的高度
                    url               :   "../../product/imageaddress/upload?productId="+vm.id,  // 上传文件的路径
                    fileType         :   ["jpg","JPG"],// 上传文件的类型
                    fileSize         :   51200000,                // 上传文件的大小
                    multiple         :   true,                    // 是否可以多个文件上传
                    dragDrop         :   true,                    // 是否可以拖动上传文件
                    tailor           :   false,                    // 是否可以裁剪图片
                    del              :   true,                    // 是否可以删除文件
                    finishDel       :   true,                    // 是否在上传文件完成后删除预览
                    /* 外部获得的回调接口 */
                    // 选择文件的回调方法  selectFile:当前选中的文件  allFiles:还没上传的全部文件
                    onSelect: function(selectFiles, allFiles){
                    },
                    // 删除一个文件的回调方法
                    onDelete: function(file){
                        // alert("当前删除了此文件："+file.name);
                    },
                    // 每文件上传成功的回调方法
                    onSuccess: function(file, response){
                        // alert("此文件上传成功："+file.name);
                        layer.msg('上传成功');
                    },
                    // 文件上传失败的回调方法
                    onFailure: function(file, response){
                        // alert("此文件上传失败:"+file.name);
                        layer.msg('上传失败')
                    },
                    // 上传完成的回调方法
                    onComplete: function(response){
                        // alert("1111："+file.name);
                        layer.msg('上传成功');
                        vm.getProAlbum();
                    }
                });
                vm.isUP = false;
            }

            layer.open({
                type: 1,
                title: false,
                content: $('#shangchaunImg'), //这里content是一个普通的String
                skin: 'openClass',
                area: ['800px', '500px'],
                shadeClose: true,
                scrollbar:false,
                btn: [],

            });
        },
        // 图片放大
        imgBig:function (event){
            var top = $(event.target).offset().top - 428;
            var left = $(event.target).offset().left - 350;
            // console.log(right);
            $('#imgBigDiv').css({
                'display':'inline-block',
                'top':top+'px',
                'left':left+'px'
            })
            $('#imgBigDiv img').attr('src',$(event.target).attr('data-url'));

        },
        imgBigS:function () {
            $('#imgBigDiv').css('display','none')
        },
        // 删除参数列表中已选图片
        delImgRecom:function (data,i) {
            console.log(data);
            console.log(i);
            data.img.splice(i,1);
            console.log(data.img);
        },

        // 返回
        returnFunc:function () {
            layer.confirm('确定返回吗？',function () {
                window.location.href = 'myProduct.html';
            })
        },
        // 中文标题翻译
        titleZhtoOther:function () {

            var index = layer.load();
            var index = layer.load(1); //换了种风格
            var index = layer.load(2, {time: 100*1000});
            $.ajax({
                url: '../../product/introduction/titleZhtoOther',
                type: 'post',
                data: JSON.stringify({
                    'productTitle': vm.proDetails.chinesePRE.productTitle
                }),
                // dataType: 'json',
                contentType: "application/json",
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.proDetails.francePRE.productTitle = r.introductionFra.productTitle;
                        vm.proDetails.spainPRE.productTitle = r.introductionSpa.productTitle;
                        vm.proDetails.germanyPRE.productTitle = r.introductionDe.productTitle;
                        vm.proDetails.italyPRE.productTitle = r.introductionIt.productTitle;
                        vm.proDetails.britainPRE.productTitle = r.introductionEn.productTitle;
                        vm.proDetails.japanPRE.productTitle = r.introductionJp.productTitle;
                        if(r.error){
                            layer.close(index);
                            layer.msg(r.error);
                        }else{
                            layer.close(index);
                            layer.msg("翻译成功");
                        }
                        // vm.proAlbum = r.imageInfo;

                    } else {
                        layer.close(index);
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.close(index);
                    layer.msg("网络故障");
                }
            })
        },
        // 中文关键字翻译
        keyWordZhtoOther:function () {

            var index = layer.load();
            var index = layer.load(1); //换了种风格
            var index = layer.load(2, {time: 100*1000});
            $.ajax({
                url: '../../product/introduction/keyWordZhtoOther',
                type: 'post',
                data: JSON.stringify({
                    'keyWord': vm.proDetails.chinesePRE.keyWord
                }),
                // dataType: 'json',
                contentType: "application/json",
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.proDetails.francePRE.keyWord = r.introductionFra.keyWord;
                        vm.proDetails.spainPRE.keyWord = r.introductionSpa.keyWord;
                        vm.proDetails.germanyPRE.keyWord = r.introductionDe.keyWord;
                        vm.proDetails.italyPRE.keyWord = r.introductionIt.keyWord;
                        vm.proDetails.britainPRE.keyWord = r.introductionEn.keyWord;
                        vm.proDetails.japanPRE.keyWord = r.introductionJp.keyWord;
                        if(r.error){
                            layer.close(index);
                            layer.msg(r.error);
                        }else{
                            layer.close(index);
                            layer.msg("翻译成功");
                        }
                        // vm.proAlbum = r.imageInfo;

                    } else {
                        layer.close(index);
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.close(index);
                    layer.msg("网络故障");
                }
            })
        },
        // 中文重点说明翻译
        keyPointZhtoOther:function () {

            var index = layer.load();
            var index = layer.load(1); //换了种风格
            var index = layer.load(2, {time: 100*1000});
            $.ajax({
                url: '../../product/introduction/keyPointZhtoOther',
                type: 'post',
                data: JSON.stringify({
                    'keyPoints': vm.proDetails.chinesePRE.keyPoints
                }),
                // dataType: 'json',
                contentType: "application/json",
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.proDetails.francePRE.keyPoints = r.introductionFra.keyPoints;
                        vm.proDetails.spainPRE.keyPoints = r.introductionSpa.keyPoints;
                        vm.proDetails.germanyPRE.keyPoints = r.introductionDe.keyPoints;
                        vm.proDetails.italyPRE.keyPoints = r.introductionIt.keyPoints;
                        vm.proDetails.britainPRE.keyPoints = r.introductionEn.keyPoints;
                        vm.proDetails.japanPRE.keyPoints = r.introductionJp.keyPoints;
                        if(r.error){
                            layer.close(index);
                            layer.msg(r.error);
                        }else{
                            layer.close(index);
                            layer.msg("翻译成功");
                        }
                        // vm.proAlbum = r.imageInfo;

                    } else {
                        layer.close(index);
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.close(index);
                    layer.msg("网络故障");
                }
            })
        },
        // 中文产品描述翻译
        descriptionZhtoOther:function () {

            var index = layer.load();
            var index = layer.load(1); //换了种风格
            var index = layer.load(2, {time: 100*1000});
            $.ajax({
                url: '../../product/introduction/descriptionZhtoOther',
                type: 'post',
                data: JSON.stringify({
                    'productDescription': vm.proDetails.chinesePRE.productDescription
                }),
                // dataType: 'json',
                contentType: "application/json",
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.proDetails.francePRE.productDescription = r.introductionFra.productDescription;
                        vm.proDetails.spainPRE.productDescription = r.introductionSpa.productDescription;
                        vm.proDetails.germanyPRE.productDescription = r.introductionDe.productDescription;
                        vm.proDetails.italyPRE.productDescription = r.introductionIt.productDescription;
                        vm.proDetails.britainPRE.productDescription = r.introductionEn.productDescription;
                        vm.proDetails.japanPRE.productDescription = r.introductionJp.productDescription;
                        if(r.error){
                            layer.close(index);
                            layer.msg(r.error);
                        }else{
                            layer.close(index);
                            layer.msg("翻译成功");
                        }
                        // vm.proAlbum = r.imageInfo;

                    } else {
                        layer.close(index);
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.close(index);
                    layer.msg("网络故障");
                }
            })
        },
        // 英文标题翻译
        titleEntoOthers:function () {

            var index = layer.load();
            var index = layer.load(1); //换了种风格
            var index = layer.load(2, {time: 100*1000});
            $.ajax({
                url: '../../product/introduction/titleEntoOthers',
                type: 'post',
                data: JSON.stringify({
                    'productTitle': vm.proDetails.britainPRE.productTitle
                }),
                // dataType: 'json',
                contentType: "application/json",
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.proDetails.francePRE.productTitle = r.introductionFra.productTitle;
                        vm.proDetails.spainPRE.productTitle = r.introductionSpa.productTitle;
                        vm.proDetails.germanyPRE.productTitle = r.introductionDe.productTitle;
                        vm.proDetails.italyPRE.productTitle = r.introductionIt.productTitle;
                        vm.proDetails.britainPRE.productTitle = r.introductionEn.productTitle;
                        vm.proDetails.japanPRE.productTitle = r.introductionJp.productTitle;
                        if(r.error){
                            layer.close(index);
                            layer.msg(r.error);
                        }else{
                            layer.close(index);
                            layer.msg("翻译成功");
                        }
                        // vm.proAlbum = r.imageInfo;

                    } else {
                        layer.close(index);
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.close(index);
                    layer.msg("网络故障");
                }
            })
        },
        // 英文关键字翻译
        keyWordEntoOthers:function () {

            var index = layer.load();
            var index = layer.load(1); //换了种风格
            var index = layer.load(2, {time: 100*1000});
            $.ajax({
                url: '../../product/introduction/keyWordEntoOthers',
                type: 'post',
                data: JSON.stringify({
                    'keyWord': vm.proDetails.britainPRE.keyWord
                }),
                // dataType: 'json',
                contentType: "application/json",
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.proDetails.francePRE.keyWord = r.introductionFra.keyWord;
                        vm.proDetails.spainPRE.keyWord = r.introductionSpa.keyWord;
                        vm.proDetails.germanyPRE.keyWord = r.introductionDe.keyWord;
                        vm.proDetails.italyPRE.keyWord = r.introductionIt.keyWord;
                        vm.proDetails.britainPRE.keyWord = r.introductionEn.keyWord;
                        vm.proDetails.japanPRE.keyWord = r.introductionJp.keyWord;
                        if(r.error){
                            layer.close(index);
                            layer.msg(r.error);
                        }else{
                            layer.close(index);
                            layer.msg("翻译成功");
                        }
                        // vm.proAlbum = r.imageInfo;

                    } else {
                        layer.close(index);
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.close(index);
                    layer.msg("网络故障");
                }
            })
        },
        // 英文重点说明翻译
        keyPointEntoOthers:function () {

            var index = layer.load();
            var index = layer.load(1); //换了种风格
            var index = layer.load(2, {time: 100*1000});
            $.ajax({
                url: '../../product/introduction/keyPointEntoOthers',
                type: 'post',
                data: JSON.stringify({
                    'keyPoints': vm.proDetails.britainPRE.keyPoints
                }),
                // dataType: 'json',
                contentType: "application/json",
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.proDetails.francePRE.keyPoints = r.introductionFra.keyPoints;
                        vm.proDetails.spainPRE.keyPoints = r.introductionSpa.keyPoints;
                        vm.proDetails.germanyPRE.keyPoints = r.introductionDe.keyPoints;
                        vm.proDetails.italyPRE.keyPoints = r.introductionIt.keyPoints;
                        vm.proDetails.britainPRE.keyPoints = r.introductionEn.keyPoints;
                        vm.proDetails.japanPRE.keyPoints = r.introductionJp.keyPoints;
                        if(r.error){
                            layer.close(index);
                            layer.msg(r.error);
                        }else{
                            layer.close(index);
                            layer.msg("翻译成功");
                        }
                        // vm.proAlbum = r.imageInfo;

                    } else {
                        layer.close(index);
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.close(index);
                    layer.msg("网络故障");
                }
            })
        },
        // 英文产品描述翻译
        descriptionEntoOthers:function () {

            var index = layer.load();
            var index = layer.load(1); //换了种风格
            var index = layer.load(2, {time: 100*1000});
            $.ajax({
                url: '../../product/introduction/descriptionEntoOthers',
                type: 'post',
                data: JSON.stringify({
                    'productDescription': vm.proDetails.britainPRE.productDescription
                }),
                // dataType: 'json',
                contentType: "application/json",
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.proDetails.francePRE.productDescription = r.introductionFra.productDescription;
                        vm.proDetails.spainPRE.productDescription = r.introductionSpa.productDescription;
                        vm.proDetails.germanyPRE.productDescription = r.introductionDe.productDescription;
                        vm.proDetails.italyPRE.productDescription = r.introductionIt.productDescription;
                        vm.proDetails.britainPRE.productDescription = r.introductionEn.productDescription;
                        vm.proDetails.japanPRE.productDescription = r.introductionJp.productDescription;
                        if(r.error){
                            layer.close(index);
                            layer.msg(r.error);
                        }else{
                            layer.close(index);
                            layer.msg("翻译成功");
                        }
                        // vm.proAlbum = r.imageInfo;

                    } else {
                        layer.close(index);
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.close(index);
                    layer.msg("网络故障");
                }
            })
        },
        // 翻译
        fanyiFunc:function () {

            var index = layer.load();
            var index = layer.load(1); //换了种风格
            var index = layer.load(2, {time: 100*1000});
            $.ajax({
                url: '../../product/introduction/entoOthers',
                type: 'post',
                data: JSON.stringify({
                    'productTitle': vm.proDetails.britainPRE.productTitle,
                    'keyWord':vm.proDetails.britainPRE.keyWord,
                    'keyPoints':vm.proDetails.britainPRE.keyPoints,
                    'productDescription':vm.proDetails.britainPRE.productDescription,
                }),
                // dataType: 'json',
                contentType: "application/json",
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.proDetails.francePRE = r.introductionFra;
                        vm.proDetails.spainPRE = r.introductionSpa;
                        vm.proDetails.germanyPRE = r.introductionDe;
                        vm.proDetails.italyPRE = r.introductionIt;
                        // vm.proDetails.britainPRE = r.introductionEn;
                        vm.proDetails.japanPRE = r.introductionJp;
                        if(r.error){
                            layer.close(index);
                            layer.msg(r.error);
                        }else{
                            layer.close(index);
                            layer.msg("翻译成功");
                        }
                        // vm.proAlbum = r.imageInfo;

                    } else {
                        layer.close(index);
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            })
        },
        // 翻译
        fanyiFunc1:function () {

            var index = layer.load();
            var index = layer.load(1); //换了种风格
            var index = layer.load(2, {time: 100*1000});
            $.ajax({
                url: '../../product/introduction/ZhtoOther',
                type: 'post',
                data: JSON.stringify({
                    'productTitle': vm.proDetails.chinesePRE.productTitle,
                    'keyWord':vm.proDetails.chinesePRE.keyWord,
                    'keyPoints':vm.proDetails.chinesePRE.keyPoints,
                    'productDescription':vm.proDetails.chinesePRE.productDescription,
                }),
                // dataType: 'json',
                contentType: "application/json",
                success: function (r) {
                    console.log(r);
                    if (r.code === 0) {
                        vm.proDetails.francePRE = r.introductionFra;
                        vm.proDetails.spainPRE = r.introductionSpa;
                        vm.proDetails.germanyPRE = r.introductionDe;
                        vm.proDetails.italyPRE = r.introductionIt;
                        vm.proDetails.britainPRE = r.introductionEn;
                        vm.proDetails.japanPRE = r.introductionJp;
                        if(r.error){
                            layer.close(index);
                            layer.msg(r.error);
                        }else{
                            layer.close(index);
                            layer.msg("翻译成功");
                        }


                        // vm.proAlbum = r.imageInfo;

                    } else {
                        layer.close(index);
                        layer.alert(r.msg);
                    }
                },
                error: function () {
                    layer.msg("网络故障");
                }
            })
        },

        // 拖拽多选
        // 鼠标按下时开启拖拽多选，将遮罩定位并展现
        mousedownV:function () {

        },
        vChange:function () {
            $('#variantType').val('');
        },
        // 加粗
        inserttag:function (event) {
            var text=$(event.target).prev()[0];
            text.focus();
            var start=text.selectionStart;
            var end = text.selectionEnd;
            // var ss = text.value.substring(start, end);
            // alert(ss);
            // console.log(start);
            // console.log(end);
            // text.value = text.value.slice(0, start) + '<b>' +text.value.slice(start, end) + '</b>' + text.value.slice(end);
            // console.log(vm.proDetails.chinesePRE.productDescription);
            vm.proDetails.chinesePRE.productDescription = vm.proDetails.chinesePRE.productDescription.slice(0, start) + '<b>' + vm.proDetails.chinesePRE.productDescription.slice(start, end) + '</b>' + vm.proDetails.chinesePRE.productDescription.slice(end)
        },
        inserttag1:function (event) {
            var text=$(event.target).prev()[0];
            text.focus();
            var start=text.selectionStart;
            var end = text.selectionEnd;
            // var ss = text.value.substring(start, end);
            // alert(ss);
            // console.log(start);
            // console.log(end);
            // text.value = text.value.slice(0, start) + '<b>' +text.value.slice(start, end) + '</b>' + text.value.slice(end);
            // console.log(vm.proDetails.chinesePRE.productDescription);
            vm.proDetails.britainPRE.productDescription = vm.proDetails.britainPRE.productDescription.slice(0, start) + '<b>' + vm.proDetails.britainPRE.productDescription.slice(start, end) + '</b>' + vm.proDetails.britainPRE.productDescription.slice(end)
        },
        inserttag2:function (event) {
            var text=$(event.target).prev()[0];
            text.focus();
            var start=text.selectionStart;
            var end = text.selectionEnd;
            // var ss = text.value.substring(start, end);
            // alert(ss);
            // console.log(start);
            // console.log(end);
            // text.value = text.value.slice(0, start) + '<b>' +text.value.slice(start, end) + '</b>' + text.value.slice(end);
            // console.log(vm.proDetails.chinesePRE.productDescription);
            vm.proDetails.francePRE.productDescription = vm.proDetails.francePRE.productDescription.slice(0, start) + '<b>' + vm.proDetails.francePRE.productDescription.slice(start, end) + '</b>' + vm.proDetails.francePRE.productDescription.slice(end)
        },
        inserttag3:function (event) {
            var text=$(event.target).prev()[0];
            text.focus();
            var start=text.selectionStart;
            var end = text.selectionEnd;
            // var ss = text.value.substring(start, end);
            // alert(ss);
            // console.log(start);
            // console.log(end);
            // text.value = text.value.slice(0, start) + '<b>' +text.value.slice(start, end) + '</b>' + text.value.slice(end);
            // console.log(vm.proDetails.chinesePRE.productDescription);
            vm.proDetails.italyPRE.productDescription = vm.proDetails.italyPRE.productDescription.slice(0, start) + '<b>' + vm.proDetails.italyPRE.productDescription.slice(start, end) + '</b>' + vm.proDetails.italyPRE.productDescription.slice(end)
        },
        inserttag4:function (event) {
            var text=$(event.target).prev()[0];
            text.focus();
            var start=text.selectionStart;
            var end = text.selectionEnd;
            // var ss = text.value.substring(start, end);
            // alert(ss);
            // console.log(start);
            // console.log(end);
            // text.value = text.value.slice(0, start) + '<b>' +text.value.slice(start, end) + '</b>' + text.value.slice(end);
            // console.log(vm.proDetails.chinesePRE.productDescription);
            vm.proDetails.spainPRE.productDescription = vm.proDetails.spainPRE.productDescription.slice(0, start) + '<b>' + vm.proDetails.spainPRE.productDescription.slice(start, end) + '</b>' + vm.proDetails.spainPRE.productDescription.slice(end)
        },
        inserttag5:function (event) {
            var text=$(event.target).prev()[0];
            text.focus();
            var start=text.selectionStart;
            var end = text.selectionEnd;
            // var ss = text.value.substring(start, end);
            // alert(ss);
            // console.log(start);
            // console.log(end);
            // text.value = text.value.slice(0, start) + '<b>' +text.value.slice(start, end) + '</b>' + text.value.slice(end);
            // console.log(vm.proDetails.chinesePRE.productDescription);
            vm.proDetails.germanyPRE.productDescription = vm.proDetails.germanyPRE.productDescription.slice(0, start) + '<b>' + vm.proDetails.germanyPRE.productDescription.slice(start, end) + '</b>' + vm.proDetails.germanyPRE.productDescription.slice(end)
        },
        inserttag6:function (event) {
            var text=$(event.target).prev()[0];
            text.focus();
            var start=text.selectionStart;
            var end = text.selectionEnd;
            // var ss = text.value.substring(start, end);
            // alert(ss);
            // console.log(start);
            // console.log(end);
            // text.value = text.value.slice(0, start) + '<b>' +text.value.slice(start, end) + '</b>' + text.value.slice(end);
            // console.log(vm.proDetails.chinesePRE.productDescription);
            vm.proDetails.japanPRE.productDescription = vm.proDetails.japanPRE.productDescription.slice(0, start) + '<b>' + vm.proDetails.japanPRE.productDescription.slice(start, end) + '</b>' + vm.proDetails.japanPRE.productDescription.slice(end)
        }

    },

    created:function () {
        // console.log(this.recommend);
        // console.log(this.recommend1);
        // this.getRecommendAll();

        var url = decodeURI(window.location.href);
        var argsIndex = url.split("?id=");
        var id = argsIndex[1];
        // console.log(id)
        this.id = parseInt(id);
        // console.log(this.id);
        this.getProDetails();
        // this.getcostFreight();
        // this.getVariant();

        // console.log(url);
        this.getProAlbum();
        this.getProStation();






    },
    mounted:function () {

    },
    updated:function () {
        vm.drapImg();
        vm.drapImg1();

    }

})