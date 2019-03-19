layui.use(['form', 'layedit'], function () {
	var form = layui.form(),
		layer = layui.layer;
	window.form = layui.form();

	//自定义验证规则
	form.verify({
		username: function (value) {
			if (value.length < 0) {
				return '用户名不能为空';
			} else if (value.indexOf("@fang.com") < 0) {
				return '用户名必须包含@fang.com';
			}
		},
	});
});


$(function () {
	$("#jqGrid").jqGrid({
		url: '../sys/online/list',
		colModel: [
			{
				label: '姓名',
				name: 'name',
				width: 90,
				sortable: false
			},
			{
				label: 'IP',
				name: 'ip',
				width: 100,
				sortable: false
			},
			{
				label: '登录时间',
				name: 'loginTime',
				sortable: false ,
				width: 60
			},
			{
				label: '最后访问时间',
				name: 'lastAccessTime',
				sortable: false ,
				width: 80,

			},
		]
	});
});

var setting = {
	data: {
		simpleData: {
			enable: true,
			idKey: "cityId",
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
			username: null
		}

	},
	methods: {
		query: function () {
			vm.reload();
		},
		reload: function (event) {

			var page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
				postData: {
					'name': vm.q.username
				},
				page: page
			}).trigger("reloadGrid");
		}
	}
});