layui.use([ 'form', 'layedit' ], function() {
	var form = layui.form();
	window.form = layui.form();

	// 监听radio
	form.on('radio(isDelete)', function(data) {
		vm.channel.isDelete = data.value;
	});

	// 监听提交
	form.on('submit(submit)', function(data) {
		vm.saveOrUpdate();
		return false;
	});
});

$(function() {
	$("#jqGrid").jqGrid({
		url : '../sys/channel/list',
		colModel : [ {
			label : '频道ID',
			name : 'id',
			hidden : true,
			key : true
		}, {
			label : '频道名称',
			name : 'name',
			width : 75
		}, {
			label : '备注',
			name : 'remark',
			width : 90,
		}, {
			label : '创建时间',
			name : 'createTime',
			index : 'create_time',
			width : 100
		}, {
			label : '更新时间',
			name : 'updateTime',
			index : 'update_time',
			width : 100
		}, {
			label : '状态',
			name : 'isDelete',
			index : 'is_delete',
			width : 50,
			formatter : function(value, options, row) {
				return value === 1 ? '<span class="badge badge-danger">禁用</span>' : '<span class="badge badge-primary">正常</span>';
			}
		} ]
	});
});

var vm = new Vue({
	el : '#rrapp',
	data : {
		q : {
			channelname : null
		},
		showList : true,
		title : null,
		channel : {
			name : '',
			remark : '',
			isDelete : 0,
		}
	},
	methods : {
		query : function() {
			vm.reload();
		},
		add : function() {
			vm.showList = false;
			vm.title = "新增";
			vm.channel = {
				name : '',
				remark : '',
				isDelete : 0,
			};
		},
		update : function() {
			var id = getSelectedRow();
			if (id == null) {
				return;
			}
			vm.showList = false;
			vm.title = "修改";
			vm.getChannel(id);

		},
		del : function() {
			var ids = getSelectedRows();
			if (ids == null) {
				return;
			}

			confirm('确定要删除选中的记录？', function() {
				$.ajax({
					type : "POST",
					url : "../sys/channel/delete",
					data : JSON.stringify(ids),
					success : function(r) {
						if (r.code == 0 && r.prompt == null) {
							alert('操作成功', function(index) {
								vm.reload();
							});
						} else if (r.code == 0 && r.prompt != null) {
							alert('正在被广告位使用的频道不能删除', function(index) {
								vm.reload();
							});
						} else {
							toastr.error(r.msg, "");
						}
					}
				});
			});
		},
		saveOrUpdate : function(event) {
			var url = vm.channel.id == null ? "../sys/channel/save" : "../sys/channel/update";
			$.ajax({
				type : "POST",
				url : url,
				data : JSON.stringify(vm.channel),
				success : function(r) {
					if (r.code === 0) {
						alert('操作成功', function(index) {
							vm.reload();
						});
					} else {
						toastr.error(r.msg, "");
					}
				}
			});
		},
		getChannel : function(id) {
			$.get("../sys/channel/info/" + id, function(r) {
				vm.channel = r.channel;
			});
		},
		reload : function(event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
				postData : {
					'name' : vm.q.channelname
				},
				page : page
			}).trigger("reloadGrid");
		}
	},
	updated : function() {
		try {
			window.form.render();
		} catch (error) {
			//
		}
	}
});