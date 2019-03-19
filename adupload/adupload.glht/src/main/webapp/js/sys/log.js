layui.use([ 'form', 'layedit' ], function() {
	var form = layui.form();
	window.form = layui.form();

});

$(function() {
	$("#jqGrid").jqGrid({
		url : '../sys/log/list',
		multiselect : false,
		colModel : [ {
			label : '用户名',
			name : 'username',
			width : 55
		}, {
			label : '用户操作',
			name : 'operation',
			width : 25,
		}, {
			label : '请求方法',
			name : 'method',
			width : 110
		}, {
			label : '请求参数',
			name : 'params',
			width : 200
		}, {
			label : 'IP地址',
			name : 'ip',
			width : 30,
		}, {
			label : '创建时间',
			name : 'createDate',
			index : 'create_date',
			width : 40,
		} ]
	});

});

var vm = new Vue({
	el : '#rrapp',
	data : {
		showList : true,
		usernameOrOperation : null
	},
	methods : {
		query : function() {
			vm.reload();
		},
		reload : function(event) {
			var page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
				postData : {
					'key' : vm.usernameOrOperation
				},
				page : page
			}).trigger("reloadGrid");
		}
	}
});