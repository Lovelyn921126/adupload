layui.use(['form', 'layedit'], function () {
    var form = layui.form(),
        layer = layui.layer;
    window.form = layui.form();

    //监听提交
    form.on('submit(submit)', function (data) {
        $('#hiddenIFrame').load(function () {
            alert("推送成功，更新请求系统处理中，5分钟内完成请求！", "推送成功");
        });
    });
});