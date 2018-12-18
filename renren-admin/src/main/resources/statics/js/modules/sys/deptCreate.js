$(function () {

})

window.onload = function() {


}


var vm = new Vue({
    el: '#step',
    data: {
        // 产品id
        id:null,
        couDetails:{
            "deptId": null,
            "parentId": 1,
            "name": "",
            "parentName": "",
            "delFlag": null,
            "open": null,
            "list": null,
            "accountCount": null,
            "companySku": "",
            "companyAddress": "",
            "companyPerson": "",
            "companyTel": "",
            "companyQq": "",
            "companyPoints": null,
            "balance": 0,
            "availableBalance": 0,
            "unliquidatedNumber": 0,
            "unshippedNumber": 0,
            "estimatedCost": 0,
            "estimatedOrder": 0,
            "updateTime": "",
            "createTime": ""}


    },
    methods:{


        // 修改保存
        saveCou:function () {

            layer.confirm('确定创建改公司吗？', function(index){

                var index = layer.load();
                var index = layer.load(1); //换了种风格
                var index = layer.load(2, {time: 10*1000}); //又换了种风格，并且设定最长等待10秒
                $.ajax({
                    type: 'post',
                    url: '../../sys/dept/save',
                    contentType: "application/json",
                    data: JSON.stringify(vm.couDetails),
                    success: function (r) {
                        // console.log('保存');
                        // console.log(r);
                        // console.log(vm.proDetails);
                        if (r.code == 0) {
                            layer.close(index);

                            window.location.href = document.referrer;

                        } else {
                            alert(r.msg);
                        }
                    }
                });



            });



        },

        // 返回
        returnFunc:function () {
            layer.confirm('确定返回吗？',function () {
                window.location.href = document.referrer;
            })
        }
    },

    created:function () {
        // this.getCouDetails();
    }

})