<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<%@ include file="/common/taglibs.jsp"%>
<title>WMS管理平台</title>
<style>
.icon-Transparent {
	width: 0px;
}

.tree-indent {
	width: 8px;
}
</style>
<script type="text/javascript">
	$(function() {
		//实例化树形菜单
		$("#tree").tree({
			data :
<%=session.getAttribute("curLoginUserAuth")%>
	,
			lines : false,
			onClick : function(node) {
				if (node.url) {
					var _url = '${app}/' + node.url;
					if (_url.indexOf("?") > 0) {
						_url += "&pageId=" + node.id;
					} else {
						_url += "?pageId=" + node.id;
					}
					Open(node.text, _url);
				}
			}
		});

		//在右边center区域打开菜单，新增tab
		function Open(text, url) {
			if ($("#tabs").tabs('exists', text)) {
				$('#tabs').tabs('close', text);
			}
			$("#tabs").tabs('add', {
				title : text,
				bodyCls : 'noOverFlow',
				content : CreateFrame(url),
				closable : true
			});
		}
		function CreateFrame(URL) {
			setTimeout("DynamicCreate('" + URL + "')", 10);
		}

		//绑定tabs的右键菜单
		$("#tabs").tabs({
			onContextMenu : function(e, title) {
				e.preventDefault();
				$('#tabsMenu').menu('show', {
					left : e.pageX,
					top : e.pageY
				}).data("tabTitle", title);
			}
		});

		//实例化menu的onClick事件
		$("#tabsMenu").menu({
			onClick : function(item) {
				CloseTab(this, item.name);
			}
		});

		//几个关闭事件的实现
		function CloseTab(menu, type) {
			var curTabTitle = $(menu).data("tabTitle");
			var tabs = $("#tabs");

			if (type === "close") {
				tabs.tabs("close", curTabTitle);
				return;
			}

			var allTabs = tabs.tabs("tabs");
			var closeTabsTitle = [];

			$.each(allTabs, function() {
				var opt = $(this).panel("options");
				if (opt.closable && opt.title != curTabTitle && type === "Other") {
					closeTabsTitle.push(opt.title);
				} else if (opt.closable && type === "All") {
					closeTabsTitle.push(opt.title);
				}
			});

			for (var i = 0; i < closeTabsTitle.length; i++) {
				tabs.tabs("close", closeTabsTitle[i]);
			}
		}

		$("#setWarehouse").combobox({
			onChange : function(newValue, oldValue) {
				if (newValue != oldValue) {
					showHandleProgress();
					var data = {};
					data["whCode"] = $("#setWarehouse").combobox("getValue");
					data["whName"] = $("#setWarehouse").combobox("getText");
					$.post("${app}/userBindWh/setWarehouse.action", data, function(result) {
						hideProgress();
						if ('-1' == result.retcode) {
							$.messager.alert('错误信息', result.retmsg, 'error');
						} else {
							$.messager.alert('提示信息', result.retmsg, 'info');
						}
					}, "json");
				}
			}
		});
	});

	function logout() {
		location.href = '${app}/logout';
	}
	var url;
	function editPwd() {
		$('#dlg').dialog('open').dialog('setTitle', '修改密码');
		$('#fm').form('clear');
		url = '${app}/user/modifyPwd.action';
	}
	function savePwd() {
		if (!$('#fm').form('validate')) {
			return false;
		}
		showHandleProgress();
		$.ajax({
			type : 'POST',
			url : url,
			data : $('#fm').serialize(),
			dataType : "json",
			success : function(result) {
				hideProgress();
				if ('-1' == result.retcode) {
					$.messager.alert('错误信息', result.retmsg, 'error');
				} else {
					$('#dlg').dialog('close');
					$.messager.alert('通知信息', result.retmsg, 'info');
				}
			}
		});
	}
	function OpenTab(text, url) {
		if ($("#tabs").tabs('exists', text)) {
			$('#tabs').tabs('select', text);
			var tab = $('#tabs').tabs('getSelected');
			var tabHtml = '<iframe frameborder="0"  src="' + url + '" style="width:100%;height:100%;"></iframe>';
			$('#tabs').tabs('update', {
				tab : tab,
				options : {
					title : text,
					content : tabHtml
				}
			});
		} else {
			var tabHtml = '<iframe frameborder="0" src="' + url + '" style="width:100%;height:100%;"></iframe>';
			$('#tabs').tabs('add', {
				title : text,
				closable : true,
				content : tabHtml
			});
		}
	}

	function DynamicCreate(URL) {
		try {
			showHandleProgress();
			var iframe = document.createElement("iframe");
			iframe.src = URL;
			iframe.scrolling = "auto";
			iframe.setAttribute('frameborder', '0', 0);
			iframe.style.cssText = "position:relative;width:100%;height:100%;";
		} catch (e) {
			$.messager.alert('发生错误', "错误代码:" + e.number + "<br>错误描述:" + e.message, 'error');
			hideProgress();
		}
		if (iframe.attachEvent) {
			iframe.attachEvent("onload", function() {
				hideProgress();
				$(iframe).css("opacity", "1");
			});
		} else {
			iframe.onload = function() {
				hideProgress();
				$(iframe).css("opacity", "1");
			}
		}
		var DivPanel = $("div[class='panel']:visible > div[class='panel-body panel-body-noheader panel-body-noborder noOverFlow']");
		if (DivPanel.length == 1) {
			$(iframe).appendTo(DivPanel);
			$(iframe).css("opacity", "0");
		} else {
			$.messager.alert('无法创建iframe', "共发现[" + DivPanel.length + "]个DIV容器！", 'error');
			hideProgress();
		}
	}

	//导入窗口
	function importExcel(keyNo) {
		var src = "${app}/excel/uploadImport.action?keyNo=" + keyNo;
		$('#TopLevelWin').window({
			iconCls : 'icon-excel'
		});
		Hmgx.E_OpenWin("#TopLevelWin", "#TopLevelIframe", src, "导入数据", 400, 250);
	}

	//判断窗口
	function closeImport() {
		Hmgx.E_CloseWin("#TopLevelWin");
	}
</script>

</head>
<body class="easyui-layout">
	<div data-options="region:'center',title:'',border:true">
		<div class="easyui-tabs" fit="true" border="false" id="tabs">

			<div title="首页" style="text-align: left:top; margin-left: 20px; margin-top: 20px">
				<h3 class="">2017.09.14 更新内容</h3>
				<ol>
					<li>收货流程支持跳过搬运和上架</li>
					<li>溢库区上架改为定时方式执行</li>
					<li>重要操作添加日志记录</li>
					<li>变更缓存组件及策略,优化性能</li>
					<li>新增占位库存功能</li>
					<li>预警功能上线(测试中)</li>
					<li>新增退货台账功能</li>
					<li>手工收货单支持自定义表列功能,右键表头使用(测试中)</li>
				</ol>
				<h3 class="">2017.09.05 更新内容</h3>
				<ol>
					<li>新能源仓库支持出库单打印</li>
					<li>新能源仓库支持随箱卡打印</li>
					<li>新增批量锁定库存功能</li>
					<li>调整发车流程增加发货单发车功能</li>
					<li>库存调整申请单增加零件用途转换功能</li>
					<li>用户信息相关功能新增仓库隔离</li>
					<li>LES/手功发货单支持按状态查询</li>
					<li>台账新增明细导出功能</li>
					<li>新增供应商发货功能</li>
				</ol>
				<h3 class="">2017.08.17 更新内容</h3>
				<ol>
					<li>库存调整申请单支持操作锁定库存</li>
					<li>手持 LES/手工 收货支持ABC收货</li>
				</ol>
				<h3 class="">2017.08.10 更新内容</h3>
				<ol>
					<li>入库报表优化查询性能</li>
					<li>系统任务操作性能优化</li>
				</ol>
				<h3 class="">2017.08.07 更新内容</h3>
				<ol>
					<li>出库报表优化查询速度</li>
				</ol>
				<h3 class="">2017.08.03 更新内容</h3>
				<ol>
					<li>收货员绑定月台功能优化</li>
					<li>出/入库报表取消默认查询</li>
					<li>物料基础资料选择库位功能优化</li>
					<li>手持收货支持默认件数的优化</li>
				</ol>
				<h3 class="">2017.08.01 更新内容</h3>
				<ol>
					<li>修复手工收货物料不存在偶尔导致库存错误的BUG</li>
					<li>手工发货单增加备注和附属单号</li>
					<li>修复手工发货单生成任务,库存不足时库存丢失的BUG</li>
					<li>修复库存导出的BUG</li>
					<li>库存调整申请单优化</li>
					<li>修复供应商查询BUG</li>
					<li>任务列表支持根据任务状态查询</li>
				</ol>
			</div>

		</div>
	</div>
	<div data-options="region:'west',split:true,minWidth:100,maxWidth:300,title:'菜单'" style="width: 200px; padding: 5px;">
		<ul id="tree"></ul>
	</div>
	<div data-options="region:'north',title:'',split:false" style="height: 60px;">
		<div style="float: left; font-size: 40px; font-weight: bold;">WMS管理系统</div>
		<div style="font-size: 12px; float: right; margin-right: 50px; border: solid 0px red;">
			<br>
			<br>
			<select id="setWarehouse" style="width: 100px; height: 20px" data-options="panelHeight:'auto'" class="easyui-combobox">
				<c:forEach items="${sessionScope.warehouseList}" var="entry">
					<c:choose>
						<c:when test="${entry.whCode  eq sessionScope.defaultWhCode }">
							<option value="${entry.whCode }" selected="selected">${entry.whName }</option>
						</c:when>
						<c:otherwise>
							<option value="${entry.whCode }">${entry.whName }</option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</select>
			&nbsp;&nbsp;&nbsp;&nbsp; 您好,
			<font style="font-weight: bold; color: OrangeRed;">${sessionScope.curLoginUser.truename}</font>
			!
			<a href="javascript:;" onclick="logout()" style="">退出</a>
			<a href="javascript:;" onclick="editPwd()" style="">修改密码</a>
		</div>

	</div>

	<div id="tabsMenu" class="easyui-menu" style="width: 120px;">
		<div name="close">关闭</div>
		<div name="Other">关闭其他</div>
		<div name="All">关闭所有</div>
	</div>

	<div id="dlg" class="easyui-dialog" closed="true" style="width: 300px;" buttons="#dlg-buttons">
		<form id="fm" method="post">
			<div class="fitem">
				<label>原始密码:</label>
				<input name="pwd" class="easyui-textbox" type="password" required="true" missingMessage="原始密码不能为空。">
			</div>
			<div class="fitem">
				<label>新&nbsp;密&nbsp;码:</label>
				<input id="newPwd" name="newPwd" class="easyui-textbox" type="password" required="true" missingMessage="新密码不能为空。" validType="minLength[4]">
			</div>
			<div class="fitem">
				<label>确认密码:</label>
				<input name="confirmPwd" class="easyui-textbox" type="password" required="true" missingMessage="确认密码不能为空。" validType="equalTo['#newPwd']">
			</div>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="savePwd()">保存</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	</div>

	<div id="TopLevelWin" align="center" class="easyui-window" style="padding: 5px; overflow: hidden;" closed="true">
		<iframe id="TopLevelIframe" scrolling="auto" src="" frameborder="0" style="width: 100%; height: 100%"></iframe>
	</div>
	<c:if test="${isReciver && defaultDock==null && isShow==true}">
		<div id="dd" class="easyui-dialog" title="" style="width: 600px; height: 600px;" data-options="iconCls:'icon-save',resizable:true,modal:true,close:false">
			<table id="dgytbd" title="绑定月台 " class="easyui-datagrid"
				data-options="remoteSort:false,url:'${app}/dock/list.action',fit:true,rownumbers:true,singleSelect:true,toolbar:'#toolbar',pagination:true,border:true">
				<thead>
					<tr>
						<th data-options="sortable:true,field:'dockCode',width:'100px'">月台编号</th>
						<th data-options="sortable:true,field:'zoneCode',width:'100px'">所属库位</th>
						<th data-options="sortable:true,field:'tagId',width:'100px'">tag扫码</th>
						<th data-options="sortable:true,field:'updateUser',width:'80px'">最后修改人</th>
						<th data-options="sortable:true,field:'updateTime',width:'150px'">最后修改时间</th>
					</tr>
				</thead>
			</table>
			<div id="toolbar">
				<a href="javascript:;" class="easyui-linkbutton" onclick="bind()">绑定月台</a>
			</div>
		</div>
		<script type="text/javascript">
			//绑定
			function bind() {
				var row = Hmgx.GetSelectRow("dgytbd", "请选择需要绑定的数据！");
				if (!row) {
					return;
				}
				showHandleProgress();
				$.post('${app}/dock/bindDock.action', {
					id : row.id,
					dockCode : row.dockCode,
					zoneCode : row.zoneCode
				}, function(result) {
					hideProgress();
					if ('-1' == result.retcode) {
						$.messager.alert('错误信息', result.retmsg, 'error');
					} else {
						$('#dd').dialog('close');
					}
				}, "json");
			}
		</script>
	</c:if>
</body>
</html>