<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="app" value="${pageContext.request.contextPath}" />
<meta name="renderer" content="webkit" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<link rel="shortcut icon" type="image/x-icon" href="${app}/img/favicon.ico">
<link type="text/css" rel="stylesheet" href="${app}/JqueryEasyui/themes/material/easyui.css" />
<link type="text/css" rel="stylesheet" href="${app}/JqueryEasyui/themes/icon.css" />
<style>
a {
	text-decoration: none;
}

.fitem {
	border-bottom: 1px #95b8e7 dashed;
	padding: 5px;
}

.fitem label {
	display: inline-block;
	text-align: right;
	width: 100px;
}

table {
	font-size: 12px;
}
</style>

<script type="text/javascript" src="${app}/JqueryEasyui/jquery.min.js"></script>
<script type="text/javascript" src="${app}/JqueryEasyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${app}/JqueryEasyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${app}/js/HmgxExtend.js"></script>
<script type="text/javascript" src="${app}/js/Jquery.Expand.js"></script>
<script type="text/javascript" src="${app}/js/FlashCopy/FlashCopy.js"></script>
<div id='Loading' style="position: absolute; z-index: 1000; top: 0px; left: 0px; width: 100%; height: 100%; background: #FFF; text-align: center;"></div>
<script>
	function closes() {
		$("#Loading").fadeOut("normal", function() {
			$(this).remove();
		});
	}

	var pc;
	$.parser.onComplete = function() {
		if (pc)
			clearTimeout(pc);
		pc = setTimeout(closes, 0);
	};
	//显示进度条
	var showHandleProgress = function() {
		$.messager.progress({
			title : '请等待',
			msg : '正在处理...',
			text : '处理中.......'
		});
	};
	//隐藏进度条
	var hideProgress = function() {
		$.messager.progress('close');
	};
	//下载模版
	function downTemp() {
		var param = {
			keyNo : "${keyNo}"
		};
		if (!param.keyNo) {
			$.messager.alert("下载模版失败", "由于该模版文件未定义，所以您无法下载。", "error");
		} else {
			Hmgx.serializeDownload("${app}/excel/downTemp.action", "", param);
		}
	}

	//导入数据
	function importDate() {
		var keyNo = "${keyNo}";
		if (!keyNo) {
			$.messager.alert("导入数据失败", "由于该导入模版未定义，所以您无法导入。", "error");
		} else {
			parent.importExcel(keyNo);
		}
	}
</script>