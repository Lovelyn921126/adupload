layui.use(['form', 'layedit'], function () {
    var form = layui.form();
    window.form = layui.form();

	//自定义验证规则
	form.verify({
		code: function (value) {
			if (value == null || value.length == 0) {
				return '编码不能为空';
			} else {
                if(!/^[A-Za-z]([_]{0,1}[A-Za-z0-9]+)*$/.test(value)){
                    return '编码不符合规范'
                }
            }
		}
	});
    //监听radio
    form.on('radio(isDelete)', function (data) {
        vm.city.isDelete = data.value;
    });

    //监听提交
    form.on('submit(submit)', function (data) {
        vm.saveOrUpdate();
        return false;
    });
});

$(function () {
    $("#jqGrid").jqGrid({
        url: '../sys/city/list',
        colModel: [{
            label: '城市ID',
            name: 'cityId',
            index: "city_id",
            width: 45,
            key: true,
            hidden: true
        },
        {
            label: '城市名称',
            name: 'name',
            width: 60
        },
        {
            label: '城市编码',
            name: 'code',
            width: 60
        },
        {
            label: '备注',
            name: 'remark',
            width: 100
        },
        {
            label: '状态',
            name: 'isDelete',
            index: "is_delete",
            width: 50,
            formatter: function (value, options, row) {
                return value === 1 ?
                    '<span class="badge badge-danger">禁用</span>' :
                    '<span class="badge badge-primary">正常</span>';
            }
        }]
    });
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        q: {
            cityName: null
        },
        showList: true,
        insert: true,
        title: null,
        city: {
            isDelete: 0
        }
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.insert = true;
            vm.title = "新增";
            vm.city = {
                isDelete: 0
            };
        },
        update: function () {
            var cityId = getSelectedRow();
            if (cityId == null) {
                return;
            }
            vm.showList = false;
            vm.insert = false;
            vm.title = "修改";
            vm.getCity(cityId);
        },
        del: function (event) {
            var cityIds = getSelectedRows();
            if (cityIds == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: "../sys/city/delete",
                    data: JSON.stringify(cityIds),
                    success: function (r) {
                        if (r.code == 0) {
                            alert('操作成功', function (index) {
                                vm.reload();
                            });
                        } else {
                            toastr.error(r.msg, "");
                        }
                    }
                });
            });
        },
        getCity: function (cityId) {
            $.get("../sys/city/info/" + cityId, function (r) {
                vm.city = r.city;
            });
        },
        saveOrUpdate: function (event) {
            var url = vm.city.cityId == null ? "../sys/city/save" : "../sys/city/update";
            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(vm.city),
                success: function (r) {
                    if (r.code === 0) {
                        alert('操作成功', function (index) {
                            vm.reload();
                        });
                    } else {
                        toastr.error(r.msg, "");
                    }
                }
            });
        },
        reload: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    'cityName': vm.q.cityName
                },
                page: page
            }).trigger("reloadGrid");
        }
    },
    updated: function () {
        try {
            window.form.render();
        } catch (error) {
            //
        }
    }
});