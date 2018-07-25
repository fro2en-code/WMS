<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<body class="easyui-layout">
	<div data-options="region:'center',title:'',border:false" style="padding: 5px;">
		<table id="dg" title="菜单管理" class="easyui-datagrid"
			url="${app}/menu/list.action" fit="true" toolbar="#toolbar"
			rownumbers="true" singleSelect="true" pagination="true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'ck',checkbox:true"></th>
					<th field="menuLblName" width="80px">标签名称</th>
					<th field="menuName" width="80px">菜单名称</th>
					<th field="menuLink" width="250px">链接</th>
					<th field="parentName" width="80px">父级菜单</th>
					<th field="insertUser" width="80px">创建人</th>
					<th field="insertTime" width="120px">创建时间</th>
					<th field="updateUser" width="80px">更新人</th>
					<th field="updateTime" width="120px">更新时间</th>
				</tr>
			</thead>
		</table>
		
	</div>
	
	<div id="toolbar">
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addMenu()">新增</a> 
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editMenu()">修改</a> 		
		<a href="#"	class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delMenu()">删除</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-asc" plain="true" onclick="asc()">升序</a> 		
		<a href="#"	class="easyui-linkbutton" iconCls="icon-desc" plain="true" onclick="desc()">降序</a>
	</div>
	<!-- 保存或修改对话框 -->
	<div id="dlg" class="easyui-dialog" style="width:300px;" closed="true" modal="true"	buttons="#dlg-buttons">
		<form id="fm" method="post">
			<div class="fitem">
				<label>标签名称:</label> <input name="menuLblName" id="menuLblName" class="easyui-textbox"
					required="true" missingMessage="标签名称不能为空">
			</div>
			<div class="fitem">
				<label>菜单名称:</label> <input name="menuName" id="menuName" class="easyui-textbox"
					required="true" missingMessage="菜单名称不能为空">
			</div>
			<div class="fitem">
				<label>url链接:</label> <input name="menuLink" class="easyui-textbox"
					required="true" missingMessage="菜单url不能为空">
			</div>
			<div class="fitem">
				<label>父级菜单:</label>
   			 	<input class="easyui-combotree" id="parentId" name="parentId" 
   			 	data-options="remoteSort:false,url:'${app}/menu/getParentTree.action',lines:false,valueField: 'id',textField: 'text'">
   			 
			</div>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok"
			onclick="save()">保存</a> <a href="#" class="easyui-linkbutton"
			iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	</div>

</body>

<script type="text/javascript">
	$(function(){
		//渲染按钮
		//Hmgx.RenderButton("toolbar",<%=session.getAttribute("curLoginUserButton")%>,"${param.pageId}");
	});
	var url;
	function addMenu() {
		$('#dlg').dialog('open').dialog('setTitle', '新增菜单').window("center");
		$('#fm').form('clear');
		url = '${app}/menu/save.action';
	}
	function editMenu() {
		var row = $('#dg').datagrid('getSelected');
		if (row) {
			$('#dlg').dialog('open').dialog('setTitle', '修改菜单').window("center");
			$('#fm').form('load', row);
			url = '${app}/menu/save.action?id=' + row.id;
		} else {
			alert('请选择需要修改的菜单！');
		}
	}
	function delMenu() {
		var row = $('#dg').datagrid('getSelected');
		if (row) {
			$.messager.confirm('删除确认', '你确定要删除该菜单吗?', function(r) {
				if (r) {
					showHandleProgress();
					$.post('${app}/menu/del.action', {
						id : row.id
					}, function(result) {
						hideProgress();
						if ('-1' == result.retcode) {
							$.messager.alert('错误信息', result.retmsg, 'error');
						} else {
							$('#dg').datagrid('reload');
						}
					});
				}
			},"json");
		} else {
			alert('请选择需要删除的菜单!');
		}
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
			dataType:"json",
			success : function(result) {
				hideProgress();
				if ('-1' == result.retcode) {
					$.messager.alert('错误信息', result.retmsg, 'error');
				} else {
					$('#dlg').dialog('close');
					$('#dg').datagrid('reload');
					$.messager.alert('提示信息', result.retmsg, 'info');
				}
			}
		});
	}
	
	function asc() {
		var row = Hmgx.GetSelectRow("dg");
		if(!row){
			return;
		}
		showHandleProgress();
		$.post('${app}/menu/sortAsc.action?t=' + new Date().getTime(),{id:row.id}, function(result) {
			hideProgress();
			if ('-1' == result.retcode) {
				$.messager.alert('错误信息', result.retmsg, 'error');
			} else {
				$('#dg').datagrid('reload');
			}
		}, "json");	
	}
	function desc() {
		var row = Hmgx.GetSelectRow("dg");
		if(!row){
			return;
		}
		showHandleProgress();
		$.post('${app}/menu/sortDesc.action?t=' + new Date().getTime(),{id:row.id}, function(result) {
			hideProgress();
			if ('-1' == result.retcode) {
				$.messager.alert('错误信息', result.retmsg, 'error');
			} else {
				$('#dg').datagrid('reload');
			}
		}, "json");	
	}

	
</script>