<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'center',title:'',border:false"
		style="padding: 5px;">

		<table id="dg" title="配置列表 " class="easyui-datagrid"
			data-options="remoteSort:false,url:'${app}/systemConfig/getPage.action',fit:true,rownumbers:true,toolbar:'#toolbar',singleSelect:true,pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'systemKey',width:'100px'">标识</th>
					<th data-options="sortable:true,field:'name',width:'100px'">参数名称</th>
					<th data-options="sortable:true,field:'whCode',width:'100px'">仓库编码</th>
					<th data-options="sortable:true,field:'flag1',width:'100px'">参数1</th>
					<th data-options="sortable:true,field:'flag2',width:'100px'">参数2</th>
					<th data-options="sortable:true,field:'flag3',width:'100px'">参数3</th>
					<th data-options="sortable:true,field:'flag4',width:'100px'">参数4</th>
					<th data-options="sortable:true,field:'remark',width:'100px'">参数说明</th>
				</tr>
			</thead>
		</table>
	</div>

	<div id="toolbar">
		<form id="queryForm" name="queryForm"
			style="padding: 0px; margin: 0px;">
			<table id="tb_query">
				<tr>
					<th>标识：</th>
					<td><input name="systemKey" id="systemKey"
						class="easyui-textbox" style="width: 120px"></td>
					<td><a href="javascript:;" class="easyui-linkbutton"
						data-options="iconCls:'icon-search',plain:true" onclick="query()">查询</a>
					</td>
				</tr>
			</table>
			<div style="border-top: 1px dashed #0066CC;"></div>
		</form>
		<div id="butbar"></div>
	</div>
	<!-- 保存或修改对话框 -->
	<div id="dlg" class="easyui-dialog" style="width: 600px;"
		data-options="closed:true,modal:true,buttons:'#dlg-buttons'">
		<form id="fm" method="post">
			<div class="fitem" id="">
				<label>标识KEY:</label> <input name="systemKey" class="easyui-textbox"
					style="width: 145px;"
					data-options="required:true,missingMessage:'标识key不能为空。'"> <label>参数名称:</label>
				<input name="name" class="easyui-textbox" style="width: 145px;"
					data-options="required:true,missingMessage:'参数名称不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>参数1:</label> <input name="flag1" class="easyui-textbox"
					style="width: 145px;"
					data-options="required:false,missingMessage:'参数1不能为空'"> <label>参数2:</label>
				<input name="flag2" class="easyui-textbox" style="width: 145px;"
					data-options="required:false,missingMessage:'参数2不能为空'">
			</div>
			<div class="fitem" id="">
				<label>参数3:</label> <input name="flag3" class="easyui-textbox"
					style="width: 145px;"
					data-options="required:false,missingMessage:'参数3不能为空'"> <label>参数4:</label>
				<input name="flag4" class="easyui-textbox" style="width: 145px;"
					data-options="required:false,missingMessage:'参数4不能为空'">
			</div>
			<div class="fitem" id="">
				<label>参数说明:</label> <input name="remark" class="easyui-textbox"
					style="width: 145px;"
					data-options="required:false,missingMessage:'参数说明'">
			</div>
		</form>
	</div>

	<div id="dlg-buttons">
		<a href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-ok'" onclick="save()">保存</a> <a
			href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-cancel'"
			onclick="javascript:$('#dlg').dialog('close')">取消</a>
	</div>
</body>

<script type="text/javascript">
	$(function() {
		//渲染按钮
		Hmgx.RenderButton("butbar",
<%=session.getAttribute("curLoginUserButton")%>
	,
				"${param.pageId}");
	});

	//查询
	function query() {
		var systemKey = $.trim($('input[name=systemKey]').val());
		$("#dg").datagrid('reload', {
			systemKey : systemKey
		});
	}
	var url;
	//新增
	function add() {
		$('#login-name').css('display', '');
		$('#dlg').dialog('open').dialog('setTitle', '新增').window("center");
		$('#fm').form('clear');
		url = '${app}/systemConfig/save.action';
	}

	//修改
	function edit() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要修改的数据！");
		if (!row) {
			return;
		}
		$('#dlg').dialog('open').dialog('setTitle', '修改');
		$('#fm').form('load', row);
		url = '${app}/systemConfig/save.action?id=' + row.id;
	}
	function save() {
		if (!$('#fm').form('validate')) {
			return false;
		}
		showHandleProgress();
		$.ajax({
			type : 'POST',
			url : url,
			data : $('#fm').serialize(),
			success : function(result) {
				hideProgress();
				if (!result.suc) {
					$.messager.alert('错误信息', result.retmsg, 'error');
				} else {
					$('#dlg').dialog('close');
					$('#dg').datagrid('reload');
				}
			}
		}).error(function() {
			hideProgress();
			$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
		});
	}

	//删除
	function del() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要删除的数据！");
		if (!row) {
			return;
		}
		$.messager.confirm('删除确认', '确定要删除该信息?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/systemConfig/del.action', {
					id : row.id
				}, function(result) {
					hideProgress();
					if ('-1' == result.retcode) {
						$.messager.alert('错误信息', result.retmsg, 'error');
					} else {
						$('#dg').datagrid('reload');
					}
				}, "json").error(function() {
					hideProgress();
					$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
				});
			}
		});
	}
	$("#dg").datagrid({
		onLoadError : function() {
			$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
		}
	});
</script>