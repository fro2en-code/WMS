<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'center',title:'',border:false" style="padding: 5px;">

		<table id="dg" title="月台列表 " class="easyui-datagrid"
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
	</div>


	<div id="toolbar">
		<div id="toolbar2">
			<p id="showDock">当前绑定月台:${defaultDock }</p>
		</div>
		<div id="butDiv" style="border-top: 1px dashed #0066CC;"></div>
		<div id="toolbar3"></div>
	</div>

	<!-- 保存或修改对话框 -->
	<div id="dlg" class="easyui-dialog" style="width: 300px" data-options="closed:true,modal:true,buttons:'#dlg-buttons'">
		<form id="fm" method="post">
			<div class="fitem" id="">
				<label>月台编号：</label>
				<input name="dockCode" class="easyui-textbox" style="width: 145px;" data-options="required:true,missingMessage:'月台编号不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>所属库位：</label>
				<input class="easyui-combobox" name="zoneCode" id="zoneCode" style="width: 145px;"
					data-options="valueField:'storagecode',textField:'storagecode',url:'${app}/storag/getComboBoxStorageCode.action',editable:false,panelHeight:'auto',required:true,missingMessage:'请选择库位。'" />
			</div>
			<div class="fitem" id="">
				<label>tag扫码：</label>
				<input name="tagId" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'tag扫码不能为空。'">
			</div>
		</form>
	</div>

	<div id="dlg-buttons">
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="save()">保存</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	</div>

</body>

<script type="text/javascript">
	$(function() {
		//渲染按钮
		Hmgx.RenderButton("toolbar3",
<%=session.getAttribute("curLoginUserButton")%>
	, "${param.pageId}");
	});

	//库位
	$("#zoneCode").combobox({
		mode : "remote",
		editable : "true"
	});

	var url;
	//新增
	function add() {
		$('#login-name').css('display', '');
		$('#dlg').dialog('open').dialog('setTitle', '新增月台').window("center");
		$('#fm').form('clear');
		url = '${app}/dock/save.action';
	}

	//修改
	function edit() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要修改的数据！");
		if (!row) {
			return;
		}
		$('#dlg').dialog('open').dialog('setTitle', '修改月台');
		$('#fm').form('load', row);
		url = '${app}/dock/save.action?id=' + row.id;
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
		});
	}

	//删除
	function del() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要删除的数据！");
		if (!row) {
			return;
		}
		$.messager.confirm('删除确认', '您确定要删除该月台?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/dock/del.action', {
					id : row.id
				}, function(result) {
					hideProgress();
					if ('-1' == result.retcode) {
						$.messager.alert('错误信息', result.retmsg, 'error');
					} else {
						$('#dg').datagrid('reload');
					}
				}, "json");
			}
		});
	}

	//绑定
	function bind() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要绑定的数据！");
		if (!row) {
			return;
		}
		$.messager.confirm('确认绑定', '您确定要绑定该月台?', function(r) {
			if (r) {
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
						$.messager.alert('提示信息', result.retmsg, 'success');
						$("#showDock").html("当前绑定月台:" + row.dockCode);
					}
				}, "json").error(function() {
					hideProgress();
					$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
				});
			}
		});
	}
	$("#dg").datagrid( {
		onLoadError : function(){
			$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
		}
	});
</script>