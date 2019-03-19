layui.use(['form', 'layedit'], function () {
	var form = layui.form();
	window.form = layui.form();

	//监听radio
	form.on('radio(isDelete)', function (data) {
		vm.menu.isDelete = data.value;
	});

	//监听radio
	form.on('radio(type)', function (data) {
		vm.menu.type = data.value;
	});

	//监听提交
	form.on('submit(submit)', function (data) {
		vm.saveOrUpdate();
		return false;
	});
});

$(function () {
	$("#jqGrid").jqGrid({
		url: '../sys/menu/list',
		colModel: [{
				label: '菜单ID',
				name: 'menuId',
				index: "menu_id",
				width: 40,
				key: true
			},
			{
				label: '菜单名称',
				name: 'name',
				index: "menu_name",
				width: 60
			},
			{
				label: '上级菜单',
				name: 'parentName',
				width: 60
			},
			{
				label: '菜单图标',
				name: 'icon',
				sortable: false,
				width: 50,
				formatter: function (value, options, row) {
					return value == null ? '' : '<i class="' + value + ' fa-lg"></i>';
				}
			},
			{
				label: '菜单URL',
				name: 'url',
				width: 100
			},
			{
				label: '授权标识',
				name: 'perms',
				width: 100
			},
			{
				label: '类型',
				name: 'type',
				width: 50,
				formatter: function (value, options, row) {
					if (value === 0) {
						return '<span class="label label-primary">目录</span>';
					}
					if (value === 1) {
						return '<span class="label label-success">菜单</span>';
					}
					if (value === 2) {
						return '<span class="label label-warning">按钮</span>';
					}
				}
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
			}
		]
	});
});

// var queryParams = function (isExport) {
// 	isExport = isExport || false;
// 	var opts = {
// 		page: 1,
// 		limit: 1,
// 		isexport: encodeURI(isExport)
// 	};
// 	return opts;
// }

var setting = {
	data: {
		simpleData: {
			enable: true,
			idKey: "menuId",
			pIdKey: "parentId",
			rootPId: -1
		},
		key: {
			url: "nourl"
		}
	},
	callback: {
		onClick: function (event, treeId, treeNode) {
			vm.menu.parentId = treeNode.menuId;
			vm.menu.parentName = treeNode.name;
		}
	}
};
var ztree;

var vm = new Vue({
	el: '#rrapp',
	data: {
		showList: true,
		title: null,
		menu: {
			parentName: null,
			parentId: 0,
			type: 1,
			orderNum: 0,
			isDelete: 0
		}
	},
	methods: {
		getMenu: function (menuId) {
			//加载菜单树
			$.get("../sys/menu/select", function (r) {
				ztree = $.fn.zTree.init($("#menuTree"), setting, r.menuList);
				var node = ztree.getNodeByParam("menuId", vm.menu.parentId);
				ztree.selectNode(node);

				vm.menu.parentName = node.name;
			})
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.menu = {
				isDelete: 0,
				parentName: null,
				parentId: 0,
				type: 1,
				orderNum: 0
			};
			vm.getMenu();
		},
		update: function (event) {
			var menuId = getSelectedRow();
			if (menuId == null) {
				return;
			}

			$.get("../sys/menu/info/" + menuId, function (r) {
				vm.showList = false;
				vm.title = "修改";
				vm.menu = r.menu;
				
				vm.getMenu();
			});
		},
		del: function (event) {
			var menuIds = getSelectedRows();
			if (menuIds == null) {
				return;
			}

			confirm('确定要删除选中的记录？', function () {
				$.ajax({
					type: "POST",
					url: "../sys/menu/delete",
					data: JSON.stringify(menuIds),
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
			});
		},
		saveOrUpdate: function (event) {
			var url = vm.menu.menuId == null ? "../sys/menu/save" : "../sys/menu/update";
			$.ajax({
				type: "POST",
				url: url,
				data: JSON.stringify(vm.menu),
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