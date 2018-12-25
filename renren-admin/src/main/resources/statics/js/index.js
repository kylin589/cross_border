//生成菜单
var menuItem = Vue.extend({
    name: 'menu-item',
    props:{item:{}},
    template:[
        '<li data-name="home" class="layui-nav-item">',
        '<a href="javascript:;" :lay-tips="item.name" lay-direction="2">',
        '<i :class="item.icon"></i>',
        '<cite>{{item.name}}</cite>',
        '<span class="layui-nav-more"></span>',
        '</a>',
        '<dl class="layui-nav-child">',
        '<dd v-for="list in item.list" :data-name="list.name" class="">',
        '<a :lay-href="list.url">{{ list.name }}</a>',
        '</dd>',
        '</dl>',
        '</li>',
    ].join('')

});

var menuItem1 = Vue.extend({
    name: 'menu-item',
    props:{item:{}},
    template:[
        '<cite>{{item.name}}</cite>',
    ].join('')

});


//注册菜单组件
Vue.component('menuItem',menuItem);

$(function () {
	$('.passwordLayer').click(function () {
		vm.updatePassword();
    })
	$('.baseInfo').click(function () {
		vm.baseInfo();
    })
})

window.onload=function () {
    // $('cite').css('display','inline-block');
    // console.log($('cite'));
}

//iframe自适应
$(window).on('resize', function() {
	var $content = $('.content');
	$content.height($(this).height() - 120);
	$content.find('iframe').each(function() {
		$(this).height($content.height());
	});

}).resize();



var vm = new Vue({
	el:'#LAY_app',
	data:{
		user:{},
		menuList:{},
		main:"main.html",
		password:'',
		newPassword:'',
        navTitle:"控制台",
		informNum:15,

	},
	methods: {
		getMenuList: function (event) {
			$.getJSON("sys/menu/nav?_"+$.now(), function(r){
				vm.menuList = r.menuList;
				console.log(vm.menuList);

                // new_element=document.createElement('scrit');
                // new_element.setAttribute('type','text/javascript');
                // new_element.setAttribute('src','statics/kuajing/js/layui.js');
                // document.body.appendChild(new_element);
			});
		},
		getUser: function(){
			$.getJSON("sys/user/info?_"+$.now(), function(r){
				vm.user = r.user;
			});
		},
		updatePassword: function(){
			layer.open({
				type: 1,
				skin: 'openClass',
				title: false,
				area: ['400px', '270px'],
				shadeClose: true,
				content: $("#passwordLayer"),
				btn: ['修改','取消'],
				btn1: function (index) {
					var data = "password="+vm.password+"&newPassword="+vm.newPassword;
					$.ajax({
						type: "POST",
					    url: "sys/user/password",
					    data: data,
					    dataType: "json",
					    success: function(result){
							if(result.code == 0){
								layer.close(index);
								layer.alert('修改成功', function(index){
									location.reload();
								});
							}else{
								layer.alert(result.msg);
							}
						}
					});
	            }
			});
		},
        baseInfo: function(){
            layer.open({
                type: 1,
                skin: 'openClass',
                title: false,
                area: ['400px', '430px'],
                shadeClose: true,
                content: $("#baseInfo"),
                btn: ['保存','取消'],
                btn1: function (index) {
                    $.ajax({
                        type: "POST",
                        url: "sys/user/update",
                        data: JSON.stringify(vm.user),
                        contentType: "application/json",
                        success: function(result){
                            if(result.code == 0){
                                layer.close(index);
                                layer.alert('保存成功');
                            }else{
                                layer.alert(result.msg);
                            }
                        }
                    });
                }
            });
        },
        donate: function () {
            layer.open({
                type: 2,
                title: false,
                area: ['806px', '467px'],
                closeBtn: 1,
                shadeClose: false,
                content: ['http://cdn.renren.io/donate.jpg', 'no']
            });
        },
		// 获取未读通知数量
		getinformNum:function () {
            $.ajax({
                url: 'sys/notice/count',
                type: 'get',
                data: '',
                dataType: 'json',
                success: function (r) {
                    // console.log('未读数量');
                    // console.log(r);
                    if (r.code === 0) {
                        vm.informNum = r.count;
                        // layer.close();
                    } else {
                        alert(r.msg);
                    }


                },
                error: function () {
                    alert("网络故障");
                }
            });
        }
	},
	created: function(){
		this.getMenuList();
		this.getUser();
        this.getinformNum();
        setInterval(function () {
			vm.getinformNum();
        },1000)
	},
	updated: function(){
		var el = $('#LAY-system-side-menu>li');
		el.css('display','none');
		for(var i = 0;i<vm.menuList.length;i++){
			el.eq(i).css('display','block');
		}
		//路由
		// var router = new Router();
		// routerList(router, vm.menuList);
		// router.start();
		// console.log(vm.main);
		// if(vm.main == 'modules/sys/inform.html'){
		// 	$('.layui-badge-dot1').css('display','none');
		// }
	}
});



function routerList(router, menuList){
	console.log("1111111111111111111111111");
	for(var key in menuList){
		var menu = menuList[key];
		if(menu.type == 0){
			routerList(router, menu.list);
		}else if(menu.type == 1){
			router.add('#'+menu.url, function() {
				var url = window.location.hash;
				
				//替换iframe的url
			    vm.main = url.replace('#', '');
			    
			    //导航菜单展开
			    $(".treeview-menu li").removeClass("active");
			    $("a[href='"+url+"']").parents("li").addClass("active");
			    
			    vm.navTitle = $("a[href='"+url+"']").text();
			});
		}
	}
}
