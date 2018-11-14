$(function () {
    $(function () {
        $('.inner-content-div2').slimScroll({
            height: '400px' //设置显示的高度
        });
    })

    // 语言选项卡
    $('.layui-tab-title li').click(function () {
        $('.layui-tab-title li').removeClass('layui-this');
        $(this).addClass('layui-this');
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
    $('.proStation').click(function () {
        vm.prostation();
    })
})

window.onload = function() {
    // var oUl = $("ul1");
    // var oUl = document.getElementById("ul1");

    // var aLi = oUl.getElementsByTagName("li");
    for(var nn = 0;nn<$('.ul1').length;nn++){
        //      console.log(nn);
        var _index = nn;
        var aLi = $(".ul1").eq(_index).find('li');
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
        var _height = aLiLast[0].offsetTop+60;
        // console.log(_height);
        aLiLast.parent().css('height',_height+'px');
        aLiLast.parent().parent().siblings().css('line-height',_height+'px')

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

}


var vm = new Vue({
    el: '#step',
    data: {
        proAlbum:[{
            id:'0',
            url:'../../statics/kuajing/img/img1.jpg'
        },{
            id:'0',
            url:'../../statics/kuajing/img/img2.jpg'
        },{
            id:'0',
            url:'../../statics/kuajing/img/img1.jpg'
        },{
            id:'0',
            url:'../../statics/kuajing/img/img2.jpg'
        }],
        proStation:[{
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
        },],
        variantList:[{
            id:'0',
            name:'颜色（color）',
            type:['红色']
        },
            {
            id:'1',
            name:'尺寸（size）',
            type:['S','M','L','XL','XXL',]
        }
        ],
        recommend:['红色','橙色','黄色','绿色','青色','蓝色','紫色','米色','黑色','白色','棕色','金色','灰色','银色'],
        recommend1:['S','M','L','XL','XXL','XXXL','大号','中号','小号'],
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
        }

    },
    methods:{
        prostation:function () {
            layer.open({
                type: 1,
                title: false,
                content: $('#proStation'), //这里content是一个普通的String
                skin: 'openClass',
                area: ['800px', '500px'],
                shadeClose: true,
                scrollbar:false,
                btn: ['<i class="layui-icon layui-icon-refresh"></i> 恢复','取消',],
                btn1: function (index) {
                    vm.proAlbum = [];
                    var _length = $('.proStationUl li.action').length;
                    console.log(_length);
                    for(var i =0;i<_length;i++){
                        var _index = $('.proStationUl li.action').eq(i).attr('data-index');
                        vm.proAlbum.push({
                            url:vm.proStation[_index].url
                        })
                    }
                    console.log(vm.proAlbum)
                    layer.close(index)
                },
                btn2: function (index) {
                    vm.proAlbum = vm.proStation
                    layer.close(index);
                    return false

                }
            });
        },
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
                    var reg = new RegExp( '，' , "g" )
                    var str = _typeName.replace(reg,',');
                    _typeName = str;
                    console.log(_typeName.split(','));
                    // _typeName.join(',');
                    if(vm.variantList.length == 0){
                        vm.variantList.push({
                            id:_id,
                            name:_name,
                            type:_typeName.split(',')
                        })
                    }else {
                        for(var i = 0;i<vm.variantList.length;i++){
                            if(vm.variantList[i].id == _id){
                                var arr = vm.variantList[i].type.concat(_typeName.split(','));
                                vm.variantList[i].type = arr;
                                // vm.variantList[i].type.push(_typeName.join(','))
                            }
                        }
                    }
                    // console.log(vm.variantList);
                    vm.getrecommendAll();
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
            $('.proStationUl li').eq(i).toggleClass('action');
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
        variantName:function (index) {
            $('.variantName a').eq(index).next().css('visibility','visible');
            $('.variantName a').eq(index).next().mouseover(function () {
                $('.variantName a').eq(index).next().css('visibility','visible');
            })
        },
        variantNamenone:function (index) {
            $('.variantName a').eq(index).next().css('visibility','hidden');
        },
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
            vm.variantList.splice(_index,1);

        },
        upVariant:function () {
            layer.open({
                type: 1,
                title: false,
                content: $('#addVariant'), //这里content是一个普通的String
                skin: 'openClass',
                area: ['500px', '400px'],
                shadeClose: true,
                scrollbar:false,
                btn: ['<i class="layui-icon layui-icon-ok-circle"></i> 保存','取消'],
                btn1: function (index) {
                    var _id = $('#variantName option:selected').val();
                    var _name = $('#variantName option:selected').text();
                    var _typeName = $('#variantType').val();
                    var reg = new RegExp( '，' , "g" )
                    var str = _typeName.replace(reg,',');
                    _typeName = str;
                    console.log(_typeName.split(','));
                    // _typeName.join(',');
                    if(vm.variantList.length == 0){
                        vm.variantList.push({
                            id:_id,
                            name:_name,
                            type:_typeName.split(',')
                        })
                    }else {
                        for(var i = 0;i<vm.variantList.length;i++){
                            if(vm.variantList[i].id == _id){
                                var arr = vm.variantList[i].type.concat(_typeName.split(','));
                                vm.variantList[i].type = arr;
                                // vm.variantList[i].type.push(_typeName.join(','))
                            }
                        }
                    }
                    console.log(vm.variantList);
                    layer.close(index);

                },
                btn2: function (index) {


                }
            });
        },
        drapImg:function () {
            for(var nn = 0;nn<$('.ul1').length;nn++){
                //      console.log(nn);
                var _index = nn;
                var aLi = $(".ul1").eq(_index).find('li');
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
                var _height = aLiLast[0].offsetTop+60;
                // console.log(_height);
                aLiLast.parent().css('height',_height+'px');
                aLiLast.parent().parent().siblings().css('line-height',_height+'px')

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

        getrecommendAll:function () {
            this.recommendAll = [];
            if(this.variantList.length == 2){
                var recommend = this.variantList[0].type;
                var recommend1 = this.variantList[1].type;

                for(var i = 0;i<recommend.length;i++){
                    for(var j = 0;j<recommend1.length;j++){
                        this.recommendAll.push({
                            name:recommend[i]+'*'+recommend1[j],
                            img:['../../statics/kuajing/img/img1.jpg','../../statics/kuajing/img/img2.jpg','../../statics/kuajing/img/img1.jpg','../../statics/kuajing/img/img2.jpg',
                                '../../statics/kuajing/img/img1.jpg','../../statics/kuajing/img/img2.jpg']
                        });
                    }
                }
            }else if(this.variantList.length == 1){
                var recommend = this.variantList[0].type;
                for(var i = 0;i<recommend.length;i++){
                    this.recommendAll.push({
                        name:recommend[i],
                        img:['../../statics/kuajing/img/img1.jpg','../../statics/kuajing/img/img2.jpg','../../statics/kuajing/img/img1.jpg','../../statics/kuajing/img/img2.jpg']
                    });
                }
            }

            console.log('1111111');
            setTimeout(function(){ vm.drapImg(); }, 3000);

        }
    },
    created:function () {
        console.log(this.recommend);
        console.log(this.recommend1);
        // this.getRecommendAll();


        if(this.variantList.length == 2){
            var recommend = this.variantList[0].type;
            var recommend1 = this.variantList[1].type;

            for(var i = 0;i<recommend.length;i++){
                for(var j = 0;j<recommend1.length;j++){
                    this.recommendAll.push({
                        name:recommend[i]+'*'+recommend1[j],
                        img:['../../statics/kuajing/img/img1.jpg','../../statics/kuajing/img/img2.jpg','../../statics/kuajing/img/img1.jpg','../../statics/kuajing/img/img2.jpg',
                            '../../statics/kuajing/img/img1.jpg','../../statics/kuajing/img/img2.jpg']
                    });
                }
            }
        }else if(this.variantList.length == 1){
            var recommend = this.variantList[0].type;
            for(var i = 0;i<recommend.length;i++){
                this.recommendAll.push({
                    name:recommend[i],
                    img:['../../statics/kuajing/img/img1.jpg','../../statics/kuajing/img/img2.jpg','../../statics/kuajing/img/img1.jpg','../../statics/kuajing/img/img2.jpg']
                });
            }
        }


        // this.recommend.forEach(function (value) {
        //     this.recommend1.forEach(function (value2) {
        //         this.recommendAll.push({
        //             name:value+'*'+value2,
        //             img:[]
        //         });
        //     })
        // })
        console.log(this.recommendAll);
    }

})