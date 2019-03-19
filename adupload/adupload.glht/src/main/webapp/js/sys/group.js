layui.use(['form'], function () {
	var form = layui.form();
	window.form = layui.form();


	//监听提交
	form.on('submit(submit)', function (data) {
		vm.saveOrUpdate();
		return false;
	});
	form.on('radio(isDelete)',function(data)
			{
		   vm.group.isDelete=data.value
			})
});


$(function() {
	$("#jqGrid").jqGrid({
		url : '../sys/group/list',
		colModel : [ {
			label : '集团ID',
			name : 'groupId',
		    index: "group_id",
			width : 60,
			key : true

		}, {
			label : '父级集团',
			name : 'parentId',
			index:"parent_id",
			width : 60
		},

		{
			label : '名称',
			name : 'name',
			width : 100
		},
		{
			label: '状态',
			name: 'isDelete',
			index:"is_delete",
			width: 50,
			formatter: function (value, options, row) {
				return value === 1 ?
					'<span class="badge badge-danger">禁用</span>' :
					'<span class="badge badge-primary">正常</span>';
			}
		}]
	});
});

// var queryParams = function (isExport) {
// isExport = isExport || false;
// var opts = {
// page: 1,
// limit: 1,
// isexport: encodeURI(isExport)
// };
// return opts;
// }

var setting = {
	data : {
		simpleData : {
			enable : true,
			idKey : "groupId",
			pIdKey : "parentId",
			rootPId : -1
		},
		key : {
			url : "nourl"
		}
	},
	callback : {
		onClick : function(event, treeId, treeNode) {
			vm.group.parentId = treeNode.groupId;
			vm.group.parentName = treeNode.name;
		}
	}
};

var ztree;

var vm = new Vue({
	el : '#rrapp',
	data : {
		showList : true,
		title : null,
		issave:false,
		showlable:true,
		showID:true,
		group : {
			parentName : null,
			parentId : 0,
			name : null,
			groupId : 0,
			isDelete: 0,

		}
	},
	methods : {
		getgroup : function(groupId) {
			// 加载菜单树
			$.get("../sys/group/select", function(r) {
				ztree = $.fn.zTree.init($("#groupTree"), setting, r.list);
				var node = ztree.getNodeByParam("groupId", vm.group.parentId);
				if (node) {
				    ztree.selectNode(node);
					vm.group.parentId = 0;
				    vm.group.parentName = node.name;
					vm.group.parentId = node.groupId;
				}
			})
		},
		add : function() {
			vm.showList = false;
			vm.title = "新增";
			vm.issave=true;
			vm.showlable=true;
			vm.showID=true;

			vm.group = {
					isDelete: 0,
					parentName: null,
					parentId: 0,


			};

			vm.getgroup();
		},
		update : function(event) {
			var groupId = getSelectedRow();

			if (groupId == null) {
				return;
			}

			$.get("../sys/group/info/" + groupId, function(r) {
				vm.showList = false;
				vm.title = "修改";
				vm.issave=false;
				vm.showlable=false;
				vm.showID=false;
				vm.group = r.group;
				vm.getgroup();

			});

		},
		del : function(event) {
			var groupIds = getSelectedRows();
			if (groupIds == null) {
				return;
			}

			confirm('确定要删除选中的记录？', function() {
				$.ajax({
					type : "POST",
					url : "../sys/group/delete",
					data : JSON.stringify(groupIds),
					success : function(r) {
						if (r.code === 0) {
							alert('操作成功', function(index) {
								vm.reload();
							});
						} else {
							toastr.error(r.msg, "", {
								"positionClass" : "toast-top-full-width",
							});
						}
					}
				});
			});
		},
		saveOrUpdate : function(event) {
			var url = vm.issave ? "../sys/group/save"
					: "../sys/group/update";
			$.ajax({
				type : "POST",
				url : url,
				data : JSON.stringify(vm.group),
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
		reload : function(event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
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