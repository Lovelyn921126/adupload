layui.use(['form', 'layedit'], function () {
	var form = layui.form();
	window.form = layui.form();

	//监听radio
	form.on('radio(role)', function (data) {
		vm.role.parentId = data.value;
	});

	//监听提交
	form.on('submit(submit)', function (data) {
		vm.saveOrUpdate();
		return false;
	});
});

$(function () {
	$("#jqGrid").jqGrid({
		url: '../sys/role/list',
		colModel: [{
				label: '角色ID',
				name: 'roleId',
				index: "role_id",
				width: 45,
				key: true
			},
			{
				label: '角色名称',
				name: 'roleName',
				index: "role_name",
				width: 60
			},
			{
				label: '父角色名称',
				name: 'parentName',
				width: 60
			},
			{
				label: '备注',
				name: 'remark',
				width: 100
			},
			{
				label: '创建时间',
				name: 'createTime',
				index: "create_time",
				width: 80
			},
			{
				label: '更新时间',
				name: 'updateTime',
				index: "update_time",
				width: 80
			}
		]
	});
});

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
	check: {
		enable: true,
		nocheckInherit: true
	}
};
var ztree;

var vm = new Vue({
	el: '#rrapp',
	data: {
		q: {
			roleName: null
		},
		showList: true,
		title: null,
		role: {
			parentId: ''
		},
		roleList: {},
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.role = {};
			vm.role.parentId = '';
			vm.getMenuTree(null);
		},
		update: function () {
			var roleId = getSelectedRow();
			if (roleId == null) {
				return;
			}

			vm.showList = false;
			vm.title = "修改";
			vm.getMenuTree(roleId);
		},
		del: function (event) {
			var roleIds = getSelectedRows();
			if (roleIds == null) {
				return;
			}

			confirm('确定要删除选中的记录？', function () {
				$.ajax({
					type: "POST",
					url: "../sys/role/delete",
					data: JSON.stringify(roleIds),
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
		getRole: function (roleId) {
			$.get("../sys/role/info/" + roleId, function (r) {
				vm.role = r.role;

				//勾选角色所拥有的菜单
				var menuIds = vm.role.menuIdList;
				for (var i = 0; i < menuIds.length; i++) {
					var node = ztree.getNodeByParam("menuId", menuIds[i]);
					ztree.checkNode(node, true, false);
				}
			});
		},
		saveOrUpdate: function (event) {
			//获取选择的菜单
			var nodes = ztree.getCheckedNodes(true);
			if (nodes.length === 0) {
				layer.msg('真的一个权限都没有吗？', {icon: 5 },function(){});
				return;
			}
			var menuIdList = new Array();
			for (var i = 0; i < nodes.length; i++) {
				menuIdList.push(nodes[i].menuId);
			}
			vm.role.menuIdList = menuIdList;

			var url = vm.role.roleId == null ? "../sys/role/save" : "../sys/role/update";
			$.ajax({
				type: "POST",
				url: url,
				data: JSON.stringify(vm.role),
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
		getRoleList: function () {
			$.get("../sys/role/select", function (r) {
				vm.roleList = r.list;
			});
		},
		getMenuTree: function (roleId) {
			//加载菜单树
			$.get("../sys/menu/perms", function (r) {
				ztree = $.fn.zTree.init($("#menuTree"), setting, r.menuList);
				//展开所有节点
				ztree.expandAll(true);

				if (roleId != null) {
					vm.getRole(roleId);
				}
			});
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
				postData: {
					'roleName': vm.q.roleName
				},
				page: page
			}).trigger("reloadGrid");
		}
	},
	created: function () {
		//获取角色信息
		this.getRoleList();
	},
	updated: function () {
		try {
			window.form.render();
		} catch (error) {
			//
		}
	}
});