layui.use(['form', 'laydate'], function () {
    var form = layui.form(),
        layer = layui.layer,
        laydate = layui.laydate;
    window.form = layui.form();

    var start = {
        format: "YYYY/MM/DD",
        min: "1900/01/01",
        max: "2099/12/30",
        istime: false,
        isclear: true,
        istoday: true,
        issure: false,
        festival: true,
        choose: function (datas) {
            end.min = datas;
            end.start = datas;
            vm.q.startDate = datas;
        }
    };
    var end = {
        format: "YYYY/MM/DD",
        min: "1900/01/01",
        max: "2099/12/30",
        istime: false,
        isclear: true,
        istoday: true,
        issure: false,
        festival: true,
        choose: function (datas) {
            start.max = datas;
            vm.q.endDate = datas;
        }
    };

    document.getElementById('startDate').onclick = function () {
        start.elem = this;
        laydate(start);
    }
    document.getElementById('endDate').onclick = function () {
        end.elem = this
        laydate(end);
    }

	//监听城市select
	form.on('select(citySearch)', function (data) {
		vm.q.cityId = data.value;
	});

    //监听指定开关
    form.on('switch(switchMyOwn)', function(data){
    	vm.q.myOwn = this.checked;
    	if(this.checked){
    		vm.q.uploadUsername = null;
    	}
    	if(this.checked){
    		layer.tips('开关打开时，只查找当前自己创建的广告', data.othis, {
    			tips: 1
	      	})
    	} else {
    		layer.tips('开关关闭时，可以查找自己权限内的所有广告', data.othis, {
    			tips: 1
	      	})
    	}
    });

    //监听提交
    form.on('submit(search)', function (data) {
        vm.query();
        return false;
    });

});

$(function () {
    $("#jqGrid").jqGrid({
        multiselect: false,
        url: '../advert/advert/list?serviceType=1',
        postData : vm.q,
        colModel: [{
            label: '操作',
            width: 25,
            sortable: false,
            formatter: operateFormat
        }, {
            label: '文件ID',
            name: 'sourceId',
            index: "source_id",
            width: 40,
            key: true,
            sortable: false,
            hidden: true
        }, {
            label: '文件url',
            name: 'sourceUrl',
            index: "source_url",
            width: 40,
            key: true,
            sortable: false,
            hidden: true
        }, {
            label: '项目名称',
            name: 'projectName',
            index: "project_name",
            width: 60,
            sortable: false,
            hidden: true
        }, {
            label: '文件ID<br/>项目名称',
            width: 80,
            sortable: false,
            formatter: projectNameFormat
        }, {
            label: '频道',
            name: 'channelName',
            index: "channelName",
            width: 60,
            sortable: false,
            hidden: true
        }, {
            label: '频道Id',
            name: 'channelId',
            index: "channelId",
            width: 60,
            sortable: false,
            hidden: true
        }, {
            label: '广告位Id',
            name: 'locationId',
            index: "locationId",
            width: 60,
            sortable: false,
            hidden: true
        }, {
            label: '广告位',
            name: 'locationName',
            index: "locationName",
            width: 60,
            sortable: false,
            hidden: true
        }, {
            label: '频道<br/>广告位',
            width: 60,
            sortable: false,
            formatter: function (value, options, row) {
                return (row.channelName == null ? '-' : row.channelName) + "\n" + (row.locationName == null ? '-' : row.locationName)
            }
        }, {
            label: '城市',
            name: 'cityName',
            index: "cityName",
            sortable: false,
            width: 30
        }, {
            label: '城市Id',
            name: 'cityId',
            index: "cityId",
            width: 30,
            sortable: false,
            hidden: true,
        }, {
            label: '宽',
            name: 'sourceWidth',
            index: "source_width",
            width: 60,
            sortable: false,
            hidden: true
        }, {
            label: '高',
            name: 'sourceHeight',
            index: "source_height",
            width: 60,
            sortable: false,
            hidden: true
        }, {
            label: '宽(px)<br/>高(px)',
            width: 30,
            sortable: false,
            formatter: function (value, options, row) {
                return (row.sourceWidth == null ? '-' : row.sourceWidth) + "\n" + (row.sourceHeight == null ? '-' : row.sourceHeight)
            }
        }, {
            label: '类型',
            name: 'sourceExtension',
            index: "source_extension",
            formatter: function (value, options, row) {
            		var lowValue = _.toLower(value),i;
            		switch (lowValue){
            			case 'zip':
            				i = '<i class="fa fa-file-archive-o" aria-hidden="true"></i> '
            				break;
            			case 'swf':
            				i = '<i class="fa fa-file-video-o" aria-hidden="true"></i> '
            				break;
            			default:
            				i = '<i class="fa fa-file-image-o" aria-hidden="true"></i> '
            				break;
            		}

                return i + value;
            },
            width: 30
        }, {
            label: '大小(KB)',
            name: 'sourceSize',
            index: "source_size",
            width: 30,
            formatter: function (value, options, row) {
                return parseInt(value / 1024);
            }
        }, {
            label: '上传时间',
            name: 'createTime',
            index: "create_time",
            width: 50
        }, {
            label: '最后修改时间',
            name: 'updateTime',
            index: "update_time",
            width: 50
        }, {
            label: '上传者',
            name: 'uploadUsername',
            index: "upload_username",
            width: 50
        }, {
            label: '上传者Id',
            name: 'uploadUserId',
            index: "upload_user_id",
            width: 50,
            hidden: true
        }]
    });

    function operateFormat(value, options, row) {
        var body = [];
        body.push("<div style='font-size:16px;width:70px;'>");
        body.push('<a class="preview_op text-navy" href="javascript:vm.preview()" title="预览">', '<i class="fa fa-play-circle-o"></i></a>');
        if (row.uploadUserId == parent.vm.user.userId || parent.vm.user.isHost == true) {
            body.push('<a class="edit_op text-navy" href="javascript:vm.edit()" title="编辑">', '<i class="fa fa-pencil-square-o"></i></a>');
        }

        body.push("</div>");
        return body.join(' ');
    }

    function projectNameFormat(value, options, row) {
        return row.sourceId + '<br/>' + sprintf('<a style="border-bottom: 1px dashed #16a085;" href="javascript:vm.projectNamePreview(%s)">%s</a>', "'" + row.projectName + "'", row.projectName)
    }

    // 手动赋值日期
    $('#startDate').val(vm.q.startDate);
    $('#endDate').val(vm.q.endDate);
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        q: {
            startDate: $.date.now('yyyy/MM/dd'),
            endDate: $.date.now('yyyy/MM/dd'),
            cityId: null,
            projectName: null,
            uploadUsername: null,
            serviceType: 1,
            myOwn:true
        },
        opType: 1, //1:新增；2:编辑
        cityList: {},
        showList: 0,
        titleSuffix: '',
        file: {
            sourceId: '',
            sourceUrl: '',
            projectName: '',
        }
    },
    methods: {
        query: function () {
            vm.reload();
        },
        zipAdd: function () {
            vm.titleSuffix = '';
            vm.opType = 1;
            vm.operation("zip");
        },
        batchAdd: function () {
            vm.titleSuffix = '';
            vm.opType = 1;
            vm.operation("batch");
        },
        operation: function (type) {
            if (type === "zip") {
                var index = layer.open({
                    title: 'ZIP上传<span class="text-navy">' + vm.titleSuffix + '</span>',
                    type: 2,
                    closeBtn: 2,
                    content: '/advert/advertZip.html',
                    area: ['90%', '80%'],
                    cancel: function (index, layero) {
                        layer.confirm('关闭后，再打开此弹窗，上传的文件将和之前的文件不在同一目录下；如果是分批zip文件上传的情况，请不要关闭此弹窗，直到所有zip都上传完毕。现在确定要关闭么？', {
                            title: '提示'
                        }, function (indexC) {
                            layer.close(indexC);
                            layer.close(index);
                        });
                        return false;
                    },
                    end: function () {
                        vm.reload();
                    }
                });
                //layer.full(index);
            } else {
                var index = layer.open({
                    title: '图片批量上传<span class="text-navy">' + vm.titleSuffix + '</span>-支持 *.png、*.jpg、*.swf、*.gif 格式文件',
                    type: 2,
                    closeBtn: 2,
                    content: '/advert/advertBatch.html',
                    area: ['90%', '80%'],
                    end: function () {
                        vm.reload();
                    }
                });
                //layer.full(index);
            }
        },
        preview: function () {
            vm.file = getSelectedRowData();
            var index = parent.layer.open({
                title: false,
                type: 2,
                closeBtn: 2,
                shadeClose: false,
                shade: 0.8,
                content: '/advert/preview.html',
                area: ['700px', '80%'],
                success: function (layero, index) {
                    var body = parent.layer.getChildFrame('body', index);
                    var iframeWin = parent[layero.find('iframe')[0]['name']];
                    iframeWin.vm.file = vm.file;
                    iframeWin.vm.q.projectName = vm.file.projectName;
                    iframeWin.vm.q.myOwn = vm.q.myOwn;
                    iframeWin.vm.preSelectShow = false;
                    iframeWin.vm.initSingleBody();
                }
            });
        },
        projectNamePreview: function (projectName) {
            var index = parent.layer.open({
                title: false,
                type: 2,
                closeBtn: 2,
                shadeClose: false,
                shade: 0.8,
                content: '/advert/preview.html',
                area: ['90%', '90%'],
                success: function (layero, index) {
                    var body = parent.layer.getChildFrame('body', index);
                    var iframeWin = parent[layero.find('iframe')[0]['name']];
                    iframeWin.vm.q.projectName = projectName;
                    iframeWin.vm.q.myOwn = vm.q.myOwn;
                    iframeWin.vm.loadFlow();
                }
            });
        },
        edit: function () {
            vm.file = getSelectedRowData();
            vm.opType = 2;
            vm.titleSuffix = '-编辑-' + vm.file.projectName;
            var ext = vm.file.sourceExtension.toLowerCase();
            if (_.includes(ext, 'zip')) {
                vm.operation("zip");
            } else {
                vm.operation("batch");
            }
        },
        getCityList: function () {
            $.get("../sys/city/select", function (r) {
                vm.cityList = r.cityList
            });
        },
        reload: function (event) {
            vm.showList = 0;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');

            // 手动获取日期
            vm.q.startDate = $('#startDate').val();
            vm.q.endDate = $('#endDate').val();

            $("#jqGrid").jqGrid('setGridParam', {
                postData: vm.q,
                page: page
            }).trigger("reloadGrid");

        }
    },
    created: function () {
        this.getCityList();
    },
    updated: function () {
        try {
            window.form.render();
        } catch (error) {
            //
        }
    }
});
$('.tooltip-demo').tooltip({
    selector: "[data-toggle=tooltip]",
    container: "body"
});