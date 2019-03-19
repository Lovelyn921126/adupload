layui.use(['form', 'layedit'], function () {
    var form = layui.form();
    window.form = layui.form();

	//自定义验证规则
	form.verify({
		channel: function (value) {
			if (value == null || value.length == 0) {
				return '频道不能为空';
			}
		},
        number: function(value){
            if(isNaN(value) || value <= 0){
                return '该项必须为一个大于零的数字';
            }
        }
	});

	//监听select
	form.on('select(channel)', function (data) {
		vm.location.channelId = data.value;
	});

	//监听select
	form.on('select(units)', function (data) {
		vm.location.units = data.value;
	});

    //监听radio
    form.on('radio(isDelete)', function (data) {
        vm.location.isDelete = data.value;
    });

    //监听提交
    form.on('submit(submit)', function (data) {
        vm.saveOrUpdate();
        return false;
    });
});

$(function () {
    $("#jqGrid").jqGrid({
        url: '../sys/location/list',
        colModel: [{
            label: '广告位ID',
            name: 'id',
            index: "location_id",
            width: 40,
            key: true,
            hidden: true
        },
		{
		    label: '广告位名称',
		    name: 'name',
		    index: "location_name",
		    width: 60
		},
		{
		    label: '频道',
		    name: 'channelName',
		    width: 50
		},
		{
		    label: '文件大小',
		    name: 'size',
		    width: 40,
            formatter: function(value, options, row){
                return value + row.units;
            }
		},
		{
		    label: '【单位】',
		    name: 'units',
		    width: 20,
            hidden: true
		},
		{
		    label: '宽度(px)',
		    name: 'width',
		    width: 40
		},
		{
		    label: '高度(px)',
		    name: 'height',
		    width: 40
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
        q:{
            name: null
        },
        showList: true,
        title: null,
        channelList: {},
        location: {
            channelId: '',
            units: 'KB',
            size: 0,
            width:0,
            height:0,
            isDelete: 0
        }
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = '新增',
            vm.location = {
                channelId: '',
                units: 'KB',
                size: 0,
                width:0,
                height:0,
                isDelete: 0
            };
        },
        update: function () {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }

            vm.showList = false;
            vm.title = '修改';

            vm.getLocation(id);
        },
        del: function () {
            var ids = getSelectedRows();
            if (ids == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: "../sys/location/delete",
                    data: JSON.stringify(ids),
                    success: function (r) {
                        if (r.code == 0) {
                            alert('操作成功', function (index) {
                                vm.reload();
                            });
                        } else {
                            toastr.error(result.msg, "");
                        }
                    }
                });
            });
        },
        saveOrUpdate: function (event) {
            var url = vm.location.id == null ? "../sys/location/save" : "../sys/location/update";
            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(vm.location),
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
        getLocation: function (id) {
			$.get("../sys/location/info/" + id, function (r) {
				vm.location = r.location;
			});
		},
        getChannelList: function(){
            $.get("../sys/channel/select", function (r) {
				vm.channelList = r.list;
			});
        },
        reload: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    'name': vm.q.name
                },
                page: page
            }).trigger("reloadGrid");
        }
    },
	created: function () {
		//获取频道信息
		this.getChannelList();
	},
    updated: function () {
        try {
            window.form.render();
        } catch (error) {
            //
        }
    }
});

