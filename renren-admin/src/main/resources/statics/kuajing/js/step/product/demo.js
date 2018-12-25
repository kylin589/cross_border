$(function(){
    // 初始化插件
    $("#demo").zyUpload({
        width            :   "1200px",                 // 宽度
        height           :   "400px",                 // 宽度
        itemWidth        :   "100px",                 // 文件项的宽度
        itemHeight       :   "100px",                 // 文件项的高度
        url               :   "../../product/imageaddress/upload?productId=181",  // 上传文件的路径
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
        },
        // 文件上传失败的回调方法
        onFailure: function(file, response){
            // alert("此文件上传失败:"+file.name);
        },
        // 上传完成的回调方法
        onComplete: function(response){
        }
    });
});