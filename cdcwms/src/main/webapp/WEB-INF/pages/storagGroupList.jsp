<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'center',title:'',border:false" style="padding: 5px;">

		<table id="dg" title="库位组信息列表 " class="easyui-datagrid"
			data-options="remoteSort:false,url:'${app}/storagGroup/list.action',fit:true,rownumbers:true,singleSelect:true,toolbar:'#toolbar',pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'whCode',width:'100px'">仓库编号</th>
					<th data-options="sortable:true,field:'groupName',width:'100px'">库位组名称</th>
					<th data-options="sortable:true,field:'updateUser',width:'80px'">最后修改人</th>
					<th data-options="sortable:true,field:'updateTime',width:'150px'">最后修改时间</th>
				</tr>
			</thead>
		</table>
	</div>

	<div id="toolbar"></div>

	<!-- 保存或修改对话框 -->
	<div id="dlg" class="easyui-dialog" style="width: 300px" data-options="closed:true,modal:true,buttons:'#dlg-buttons'">
		<form id="fm" method="post">
			<!-- <div class="fitem" id="" >
				<label>仓库编号：</label> <input name="whCode" class="easyui-textbox"
					style="width: 145px;"
					data-options="required:true,missingMessage:'仓库编号不能为空。'">
			</div> -->
			<div class="fitem" id="">
				<label>库位组名称：</label>
				<input name="groupName" class="easyui-textbox" style="width: 145px;" data-options="required:true,missingMessage:'库为组名称不能为空。'">
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
		Hmgx.RenderButton("toolbar",
<%=session.getAttribute("curLoginUserButton")%>
	, "${param.pageId}");
	});

	var url;
	//新增
	function add() {
		$('#login-name').css('display', '');
		$('#dlg').dialog('open').dialog('setTitle', '新增库位组信息').window("center");
		$('#fm').form('clear');
		url = '${app}/storagGroup/save.action';
	}

	//修改
	function edit() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要修改的数据！");
		if (!row) {
			return;
		}
		$('#dlg').dialog('open').dialog('setTitle', '修改库位组信息');
		$('#fm').form('load', row);
		url = '${app}/storagGroup/save.action?id=' + row.id;
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
		$.messager.confirm('删除确认', '您确定要删除该库位组信息?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/storagGroup/del.action', {
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