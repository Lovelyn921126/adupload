<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>

	<head>
		<title>预览</title>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta http-equiv = "X-UA-Compatible" content = "IE=edge,chrome=1"/>
		<!--360浏览器优先以webkit内核解析-->
		<meta name="renderer" content="webkit">
		<link rel="shortcut icon" href="favicon.ico">
		<link href="${rc.contextPath}/statics/css/style2.css?v=4.1.0" rel="stylesheet">
		<link href="${rc.contextPath}/statics/libs/plugins/layui/css/layui.css" rel="stylesheet" media="all">
		<link href="${rc.contextPath}/statics/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
		<link href="${rc.contextPath}/statics/css/font-awesome.css?v=4.4.0" rel="stylesheet">
		<link href="${rc.contextPath}/statics/css/animate.min.css" rel="stylesheet">
		<link href="${rc.contextPath}/statics/css/style.css?v=4.1.0" rel="stylesheet">

		<style>
			.wrapper-content .blocks>li>.widget {
				margin-bottom: 0
			}
			
			.widget {
				padding: 0;
			}
			
			.widget-body {
				padding: 15px 15px;
			}
		</style>
	</head>

	<body class="gray-bg">
		<div id="rrapp" class="wrapper wrapper-content animated fadeInRight " v-cloak>
			<ul class="" id="masonry" style="padding-top: 10px;" data-plugin="masonry"></ul>
		</div>

		<script src="${rc.contextPath}/statics/libs/jquery.min.js?v=2.1.4"></script>
		<script src="${rc.contextPath}/statics/libs/bootstrap.min.js?v=3.3.6"></script>
		<script src="${rc.contextPath}/statics/libs/plugins/layui/layui.js"></script>
		<script src="${rc.contextPath}/statics/libs/plugins/lodash/lodash.min.js"></script>
		<script src="${rc.contextPath}/statics/libs/plugins/swfobject/swfobject.js"></script>
		<script src="${rc.contextPath}/statics/libs/plugins/clipboard/clipboard.min.js"></script>
		<script>
			layui.use('flow', function() {
				var flow = layui.flow;
				flow.load({
					elem: '#masonry',
					done: function(page, next) {
						setTimeout(function() {
							var lis = [];
							var data = ${adverList};
							layui.each(data, function(index, item) {
								var index = index + 1;
								lis.push(initBody(item, index));
							});
							next(lis.join(''), page < 1);

							initZeroClipboard();
						}, 500);
					}
				});
				
			});

			function initBody(data, index) {
				var ext = _.toLower(data.sourceExtension);
				var coverBody = '';
				if(_.includes(ext, 'zip')) {
					coverBody =
						'<div style="max-width:300px;" class="alert alert-icon alert-primary alert-dismissibl center-block margin-bottom-0" role="alert">\
              <i class="icon fa fa-info"></i>\
              Hi，我是一个ZIP文件，不支持预览！\
            </div>';
				} else if(_.includes(ext, 'swf')) {
					coverBody = sprintf('<embed class="cover-image" src="%s?token=%s" type="application/x-shockwave-flash" style="max-width: %spx;max-height: %spx;">', data.sourceUrl, _.now(), data.sourceWidth, data.sourceHeight);
				} else {
					coverBody = sprintf('<img class="cover-image" src="%s?token=%s" alt="..." style="max-width: %spx;max-height: %spx;">', data.sourceUrl, _.now(), data.sourceWidth, data.sourceHeight);
				}

				var body = '\
          <li class="text-center masonry-item">\
            <div class="widget widget-article widget-shadow">\
              <div class="row">' + coverBody + '</div>\
              <div class="widget-body">\
                <p class="widget-metas row">\
                  项目名 / <a href="javascript:;">' + data.projectName + '</a> · \
                  宽高 / <a href="javascript:;">' + data.sourceWidth + '*' + data.sourceHeight + '</a> · \
                  大小 / <a href="javascript:;">' + parseInt(data.sourceSize / 1024) + '(KB)</a> · \
                  上传日期 / <a href="javascript:;">' + data.createTime + '</a> \
                  </p>\
                <div class="widget-body-footer margin-top-10">\
                  <div class="input-group" style="max-width:700px;margin-left: auto;margin-right: auto;">\
                    <input type="text" class="form-control input-sm sourceUrl' + index + '" value="' + data.sourceUrl + '" readonly="readonly" >\
                    <span class="input-group-btn">\
                        <button class="btn btn-primary btn-sm copybtn" data-clipboard-action="copy" data-clipboard-target=".sourceUrl' + index + '">复制</button>\
                    </span>\
                  </div>\
                  <div class="widget-actions pull-right">\
                    <span class="badge badge-radius badge-primary">' + index + '</span>\
                  </div>\
                </div>\
              </div>\
            </div>\
          </li>';

				return body;
			}

			function initZeroClipboard() {
				var clipboard = new Clipboard('.copybtn');
			}
			
			//只支持替换'%s'，当参数无法识别时，返回''
			var sprintf = function(str) {
				var args = arguments,
					flag = true,
					i = 1;
				str = str.replace(/%s/g, function() {
					var arg = args[i++];
					if(typeof arg === 'undefined') {
						flag = false;
						return '';
					}
					return arg;
				});
				return flag ? str : '';
			};</script>
	</body>

</html>