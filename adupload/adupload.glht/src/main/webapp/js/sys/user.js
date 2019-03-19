layui.use([ 'form', 'layedit' ], function() {
	var form = layui.form(), layer = layui.layer;
	window.form = layui.form();

	// 自定义验证规则
	form.verify({
		username : function(value) {
			if (value.length < 0) {
				return '用户名不能为空';
			} else if (value.indexOf("@fang.com") < 0) {
				return '用户名必须包含@fang.com';
			}
		},
		group : function(value) {
			if (value == null || value.length == 0) {
				return '集团不能为空';
			}
		},
		role : function(value) {
			if (vm.user.roleIdList.length == 0) {
				return '角色不能为空';
			}
		}
	});

	// 监听select
	form.on('select(group)', function(data) {
		vm.user.groupId = data.value;
	});

	// 监听radio
	form.on('radio(isDelete)', function(data) {
		vm.user.isDelete = data.value;
	});

	// 监听checkbox
	form.on('checkbox(role)', function(data) {

		if (data.elem.checked) {
			vm.user.roleIdList.push(data.value);
		} else {
			_.pull(vm.user.roleIdList, data.value);
		}
	});

	// 监听checkbox
	form.on('checkbox(checkAll)', function(data) {

		if (data.elem.checked) {
			ztree.checkAllNodes(true);
		} else {
			ztree.checkAllNodes(false);
			ztree.cancelSelectedNode();
		}
	});

	// 监听提交
	form.on('submit(submit)', function(data) {
		vm.saveOrUpdate();
		return false;
	});
});

$(function() {
	$("#jqGrid").jqGrid({
		url : '../sys/user/list',
		colModel : [ {
			label : '用户ID',
			name : 'userId',
			index : "user_id",
			hidden : true,
			key : true
		}, {
			label : '用户名',
			name : 'username',
			width : 75
		}, {
			label : '姓名',
			name : 'name',
			width : 90
		}, {
			label : '手机号',
			name : 'mobile',
			width : 100
		}, {
			label : '集团',
			name : 'groupName',
			width : 60
		}, {
			label : '状态',
			name : 'isDelete',
			index : 'is_delete',
			width : 80,
			formatter : function(value, options, row) {
				return value === 1 ? '<span class="badge badge-danger">禁用</span>' : '<span class="badge badge-primary">正常</span>';
			}
		}, {
			label : '创建时间',
			name : 'createTime',
			index : "create_time",
			width : 80
		}, {
			label : '更新时间',
			name : 'updateTime',
			index : "update_time",
			width : 80
		} ],
		gridComplete: function () {
	        //隐藏grid底部滚动条
	        $("#jqGrid").closest(".ui-jqgrid-bdiv").css({
	            "overflow-x": "hidden"
	        });

	        var re_records = $("#jqGrid").jqGrid('getGridParam', 'records'); //获取数据总条数
	        if(re_records == 0 && vm.q.username != null && _.trim(vm.q.username).length != 0){
	        	$.get("../sys/user/list", {
	        		'username' : vm.q.username,
	        		'listcheck': true,
	        		'page': 1,
	        		'limit': 20
	        	}, function(r) {
	        		if(r.page.list.length != 0) {
	        			var list = r.page.list;
	        			var body = [] ;
	        			body.push('<div style="padding:20px;">');
	        			//body.push('是不是要找以下用户？选中一个，就可以管理他了<br/><br/>');
	        			for (var i = 0; i < list.length; i++) {
	        				body.push('<div>');
	        				if(list[i].createUserId != null && _.trim(list[i].createUserId).length != 0) {
	        					body.push(sprintf('<span>%s</span>(<small class="text-danger">已经有主了</small>)', list[i].username));
	        				} else {
	        					body.push(sprintf('<a href="javascript:vm.changeCreateUser(%s)">%s</a>',"'" + list[i].userId + "'",list[i].username));
	        				}

	        				body.push('</div>');
	    				}
	        			body.push('</div>');

	        			var index = layer.open({
	        				title: '是不是要找以下用户？选中一个，就可以管理他了',
	                        type: 1,
	                        closeBtn: 2,
	                        content: body.join(" "),
	                        area: ['410px', 'auto']
	                    });
	        		}
				});
	        }
	    }
	});
});

var setting = {
	data : {
		simpleData : {
			enable : true,
			idKey : "cityId",
			pIdKey : "parentId",
			rootPId : -1
		},
		key : {
			url : "nourl"
		}
	},
	check : {
		enable : true,
		nocheckInherit : true
	}
};
var ztree;

var vm = new Vue({
	el : '#rrapp',
	data : {
		q : {
			username : null
		},
		showList : true,
		title : null,
		roleList : {},
		groupList : {},
		cityCount : 0,
		user : {
			isDelete : 0,
			groupId : '',
			roleIdList : [],
			cityIdList : []
		}
	},
	methods : {
		query : function() {
			vm.reload();
		},
		add : function() {
			vm.showList = false;
			vm.title = "新增";
			vm.user = {
				isDelete : 0,
				groupId : '',
				roleIdList : [],
				cityIdList : []
			};
			vm.getCity(null);
		},
		update : function() {
			var userId = getSelectedRow();
			if (userId == null) {
				return;
			}

			vm.showList = false;
			vm.title = "修改";

			vm.getUser(userId);
		},
		del : function() {
			var userIds = getSelectedRows();
			if (userIds == null) {
				return;
			}

			confirm('确定要删除选中的记录？', function() {
				$.ajax({
					type : "POST",
					url : "../sys/user/delete",
					data : JSON.stringify(userIds),
					success : function(r) {
						if (r.code == 0) {
							alert('操作成功', function(index) {
								vm.reload();
							});
						} else {
							toastr.error(result.msg, "");
						}
					}
				});
			});
		},
		saveOrUpdate : function(event) {

			// 获取选择的城市
			var nodes = ztree.getCheckedNodes(true);
			if (nodes.length === 0) {
				layer.msg('城市不能为空', {
					icon : 5
				}, function() {
				});
				return;
			}
			var cityIdList = new Array();
			if (nodes.length != vm.cityCount) {
				for (var i = 0; i < nodes.length; i++) {
					cityIdList.push(nodes[i].cityId);
				}
			} else {
				cityIdList.push(0);
			}
			vm.user.cityIdList = cityIdList;

			var url = vm.user.userId == null ? "../sys/user/save" : "../sys/user/update";
			$.ajax({
				type : "POST",
				url : url,
				data : JSON.stringify(vm.user),
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
		getUser : function(userId) {
			$.get("../sys/user/info/" + userId, function(r) {
				vm.user = r.user;
				vm.getCity(vm.user.cityIdList);
			});
		},
		getRoleList : function() {
			$.get("../sys/role/select", function(r) {
				vm.roleList = r.list;
			});
		},
		getGroupList : function() {
			$.get("../sys/group/select", function(r) {
				vm.groupList = r.list;
			});
		},
		getCityTree : function() {
			// 加载菜单树
			$.get("../sys/city/select", function(r) {
				ztree = $.fn.zTree.init($("#cityTree"), setting, r.cityList);
				// 展开所有节点
				ztree.expandAll(true);
			});
		},
		getCity : function(cityIdList) {
			ztree.checkAllNodes(false);
			ztree.cancelSelectedNode();
			if(cityIdList == null || cityIdList.length === 0){
				return;
			}
			if (cityIdList[0] === 0) {
				ztree.checkAllNodes(true);
				return;
			}
			for (var i = 0; i < cityIdList.length; i++) {
				var node = ztree.getNodeByParam("cityId", cityIdList[i]);
				ztree.checkNode(node, true, false);
			}
		},
		getAllCityCount : function() {
			$.get("../sys/city/getcount", function(r) {
				vm.cityCount = r.allcitycount;
			});
		},
		changeCreateUser : function(userId) {
			$.ajax({
				type : "get",
				url : "../sys/user/changecreateuser/" + userId,
				success : function(r) {
					if (r.code === 0) {
						layer.close(layer.index);
						vm.reload();
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
				postData : {
					'username' : vm.q.username
				},
				page : page
			}).trigger("reloadGrid");
		}
	},
	created : function() {
		// 获取角色信息
		this.getRoleList();
		this.getGroupList();
		this.getCityTree();
		this.getAllCityCount();
	},
	updated : function() {
		try {
			window.form.render();
		} catch (error) {
			//
		}
	}
});