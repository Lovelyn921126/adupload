var $parentNode = window.parent.document;

function $childNode(name) {
    return window.frames[name]
}

// tooltips
$('.tooltip-demo').tooltip({
    selector: "[data-toggle=tooltip]",
    container: "body"
});

// 使用animation.css修改Bootstrap Modal
$('.modal').appendTo("body");

$("[data-toggle=popover]").popover();

//折叠ibox
$('.collapse-link').click(function () {
    var ibox = $(this).closest('div.ibox');
    var button = $(this).find('i');
    var content = ibox.find('div.ibox-content');
    content.slideToggle(200);
    button.toggleClass('fa-chevron-up').toggleClass('fa-chevron-down');
    ibox.toggleClass('').toggleClass('border-bottom');
    setTimeout(function () {
        ibox.resize();
        ibox.find('[id^=map-]').resize();
    }, 50);
});

//关闭ibox
$('.close-link').click(function () {
    var content = $(this).closest('div.ibox');
    content.remove();
});

//判断当前页面是否在iframe中
if (top == this) {
    var gohome = '<div class="gohome"><a class="animated bounceInUp" href="index.html?v=4.0" title="返回首页"><i class="fa fa-home"></i></a></div>';
    $('body').append(gohome);
}

//animation.css
function animationHover(element, animation) {
    element = $(element);
    element.hover(
        function () {
            element.addClass('animated ' + animation);
        },
        function () {
            //动画完成之前移除class
            window.setTimeout(function () {
                element.removeClass('animated ' + animation);
            }, 2000);
        });
}

//拖动面板
function WinMove() {
    var element = "[class*=col]";
    var handle = ".ibox-title";
    var connect = "[class*=col]";
    $(element).sortable({
        handle: handle,
        connectWith: connect,
        tolerance: 'pointer',
        forcePlaceholderSize: true,
        opacity: 0.8,
    })
        .disableSelection();
};

var public_vars = public_vars || {};
//等待画面
$(document).ready(function() {
	public_vars.$body = $("body");
	public_vars.$pageLoadingOverlay = public_vars.$body.find('.page-loading-overlay');

	// Page Loading Overlay
	if(public_vars.$pageLoadingOverlay.length) {
		$(window).load(function() {
			public_vars.$pageLoadingOverlay.addClass('loaded');
		});
	}
	window.onerror = function() {
		// failsafe remove loading overlay
		public_vars.$pageLoadingOverlay.addClass('loaded');
	}
});

//全局配置
$.ajaxSetup({
    dataType: "json",
    contentType: "application/json",
    cache: false,
    timeout: 60 * 60 * 1000,
    dataFilter:function(response){
        if(response.indexOf('静态资源上传系统-登录') !== -1){
            //如果返回的文本包含"登陆页面"，就跳转到登陆页面
            //window.location.href=context + '/login.jsp';
        	alert('登录超时，请重新登录',function(){
        		window.location.href = '/login.html';
        	})
            //一定要返回一个字符串不能不返回或者不给返回值，否则会进入success方法
            return "";
        }else{
            //如果没有超时直接返回
            return response;
        }
    }
});

//工具集合Tools
window.T = {};

// 获取请求参数
// 使用示例
// location.href = http://localhost:8080/index.html?id=123
// T.p('id') --> 123;
var url = function (name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
};
T.p = url;

//只支持替换'%s'，当参数无法识别时，返回''
var sprintf = function (str) {
    var args = arguments,
        flag = true,
        i = 1;
    str = str.replace(/%s/g, function () {
        var arg = args[i++];
        if (typeof arg === 'undefined') {
            flag = false;
            return '';
        }
        return arg;
    });
    return flag ? str : '';
};

//重写alert
window.alert = function (msg, callback) {
    parent.layer.alert(msg, function (index) {
        parent.layer.close(index);
        if (typeof (callback) === "function") {
            callback("ok");
        }
    });
}

//重写confirm式样框
window.confirm = function (msg, callback, callbackError) {
    parent.layer.confirm(msg, {
        btn: ['确定', '取消']
    },
        function (index) { //确定事件
            parent.layer.close(index);
            if (typeof (callback) === "function") {
                callback("ok");
            }
            return true;
        },
        function (indexC) { //确定事件
            parent.layer.close(indexC);
            if (typeof (callbackError) === "function") {
                callbackError("false");
            }
            return false;
        });
}

//toastr设置
toastr.options = {
    "closeButton": true,
    "debug": false,
    "progressBar": true,
    "positionClass": "toast-top-full-width",
    "onclick": null,
    "showDuration": "400",
    "hideDuration": "1000",
    "timeOut": "7000",
    "extendedTimeOut": "1000",
    "showEasing": "swing",
    "hideEasing": "linear",
    "showMethod": "fadeIn",
    "hideMethod": "fadeOut"
}

//jqGrid表格公共
$.jgrid.defaults.responsive = true;
$.jgrid.defaults.styleUI = "Bootstrap";
$.extend($.jgrid.defaults, {
    datatype: "json",
    shrinkToFit: true,
    viewrecords: true,
    height: getHeight(),
    rowNum: 20,
    rowList: [20, 30, 50],
    rownumbers: true,
    rownumWidth: 25,
    autowidth: true,
    multiselect: true,
    sortorder: 'desc',
    pager: "#jqGridPager",
    sortorder: 'desc',
    jsonReader: {
        root: "page.list",
        page: "page.currPage",
        total: "page.totalPage",
        records: "page.totalCount"
    },
    prmNames: {
        page: "page",
        rows: "limit",
        order: "order",
        sort: "sort"
    },
    gridComplete: function () {
        //隐藏grid底部滚动条
        $("#jqGrid").closest(".ui-jqgrid-bdiv").css({
            "overflow-x": "hidden"
        });

        // $("#jqGrid").closest(".ui-pg-input").css({
        // 	"overflow-x": "hidden"
        // });
    }
});
$(window).bind("resize", function () {
    var width = $(".jqGrid_wrapper").width();
    $("#jqGrid").setGridWidth(width);
    //$("#jqGrid").setGridHeight(getHeight());
})

function getHeight() {
    var height = $(".gray-bg").height();
    return height - 255;
}

//选择一条记录
function getSelectedRow() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if (!rowKey) {
        alert("请选择一条记录");
        return;
    }

    var selectedIDs = grid.getGridParam("selarrrow");
    if (selectedIDs.length > 1) {
        alert("只能选择一条记录");
        return;
    }

    return selectedIDs[0];
}

//选择一条记录
function getSelectedRowData() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if (!rowKey) {
        alert("请选择一条记录");
        return;
    }

    var selectedIDs = grid.getGridParam("selarrrow");
    if (selectedIDs.length > 1) {
        alert("只能选择一条记录");
        return;
    }

    return grid.getRowData(rowKey);
}

//选择多条记录
function getSelectedRows() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if (!rowKey) {
        alert("请选择一条记录");
        return;
    }

    return grid.getGridParam("selarrrow");
}

$.date = new Object();
$.date.format = function (date, format, zone) {
    format = format.replace("yyyy", date.getFullYear());
    format = format.replace("MM", (date.getMonth() + 1) > 10 ? (date.getMonth() + 1) : "0" + (date.getMonth() + 1))
    format = format.replace("dd", date.getDate() > 10 ? date.getDate() : "0" + date.getDate());
    format = format.replace("HH", date.getHours() > 10 ? date.getHours() : "0" + date.getHours());
    format = format.replace("mm", date.getMinutes() > 10 ? date.getMinutes() : "0" + date.getMinutes());
    format = format.replace("ss", date.getSeconds() > 10 ? date.getSeconds() : "0" + date.getSeconds());
    format = format.replace("fff", date.getMilliseconds() > 100 ? date.getMilliseconds() : "0" + (date.getMilliseconds() > 10 ? date.getMilliseconds() : "0" + date.getMilliseconds()));
    if (zone) {
        format = format.replace(/\b(\d)\b/g, '0$1 ');
    }
    return format;
}
$.date.now = function (format) {
	format = format || 'yyyy-MM-dd HH:mm:ss.fff';
    return $.date.format(new Date(), format);
}

var uaex = ['applewebkit', 'firefox'];
var ua = window.navigator.userAgent.toLowerCase();
if(!containsAny(ua, uaex)) {
	toastr.success("为了保障您使用体验，如果您正在使用【360安全浏览器】或者【360极速浏览器】，请切换到浏览器极速模式（一般在地址栏右侧）<br/>如果您正在使用IE浏览器，请更换到其他浏览器", "", {
		"timeOut": "700000",
	});
}

function containsAny(str, searchStr) {
	for(var i = 0; i < searchStr.length; i++) {
		var value = searchStr[i];
		if(str.indexOf(value) != -1) {
			return true;
		}
	}
	return false;
}