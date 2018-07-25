<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'west',split:true,border:false" style="width: 33%;">
		<table id="dg-role" title="角色列表" class="easyui-datagrid"
			data-options="remoteSort:false,url:'${app}/role/list.action',fit:true,toolbar:'#toolbar-role',rownumbers:true,singleSelect:true,pagination:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'name',width:'80px'">角色名称</th>
					<th data-options="sortable:true,field:'pkname',width:'120px'">所属平台</th>
				</tr>
			</thead>
		</table>
		
	</div>

	<div data-options="region:'center',split:true,border:false" style="width: 33%;">
		<table id="dg-menu" title="已绑菜单" class="easyui-datagrid"
			data-options="fit:true,toolbar:'#toolbar-menu',rownumbers:true,singleSelect:true,pagination:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'menuLblName',width:'150px'">标签名称</th>
					<th data-options="sortable:true,field:'menuName',width:'80px'">菜单名称</th>
					<th data-options="sortable:true,field:'sortNum',width:'80px'">显示顺序</th>
					<th data-options="sortable:true,field:'menuLink',width:'120px'">链接</th>
				</tr>
			</thead>
		</table>
	</div>
	<div id="toolbar-role">
		<div id="tabBut"></div>
	</div>
	<div id="toolbar-menu">
		<a href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-add',plain:true" onclick="menuSelect()">可选</a> <a
			href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-remove',plain:true" onclick="menuDel()">删除</a>
		<a href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-asc',plain:true" onclick="menuSort(1)">升序</a> <a
			href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-desc',plain:true" onclick="menuSort(2)">降序</a>
	</div>
	
    <!-- 角色对话框 -->
	<div id="dlg-role" class="easyui-dialog" style="width:300px;" closed="true" modal="true"	buttons="#dlg-buttons-role">
		<form id="fm" method="post">
			<div class="fitem">
				<label>角色名称:</label> <input name="name" id="name" class="easyui-textbox"
					required="true" missingMessage="角色名称不能为空">
			</div>
			<div class="fitem">
				<label>所属平台：</label> <input class="easyui-combobox" name="platKey" id="platKey"
				data-options="valueField:'pKey',textField:'name',editable:false,multiple:false,panelHeight:'auto',required:true,missingMessage:'请选择平台。'" />
			</div>
		</form>
	</div>
	<div id="dlg-buttons-role">
		<a href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-ok'" onclick="save()">保存</a> <a
			href="javascript:;" class="easyui-linkbutton"
			onclick="javascript:$('#dlg-role').dialog('close')"
			data-options="iconCls:'icon-cancel'">取消</a>
	</div>
	
	 <!-- 可选菜单对话框 -->
	<div id="dlg-sel-menu" class="easyui-dialog" style="width:300px;height:100%" closed="true" modal="true"	buttons="#dlg-buttons-sel-menu">
		<ul id="sel-menu"></ul>
	</div>
	<div id="dlg-buttons-sel-menu">
		<a href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-ok'" onclick="saveSelMenu()">保存</a> <a
			href="javascript:;" class="easyui-linkbutton"
			onclick="javascript:$('#dlg-sel-menu').dialog('close')"
			data-options="iconCls:'icon-cancel'">取消</a>
	</div>
</body>

<script type="text/javascript">
	$(function(){
		//渲染按钮
		Hmgx.RenderButton("tabBut",<%=session.getAttribute("curLoginUserButton")%>,"${param.pageId}");
	});
	var url;
	$(function(){
		$('#dg-role').datagrid({
			onClickRow:function(index,row){
				$('#dg-menu').datagrid({
				    url:'${app}/role/getMenuListByRole.action?roleId='+row.id
				});
			}
		});
	});
	function add() {
		$('#dlg-role').dialog('open').dialog('setTitle', '新增角色').window("center");
		$('#fm').form('clear');
		$('#platKey').combobox({
			url:'${app}/user/getPlatKeyList.action'
		});
		url = '${app}/role/save.action';
	}
	function edit() {
		var row = $('#dg-role').datagrid('getSelected');
		if (!row) {
			$.messager.alert('提示信息', '请选择要修改的角色');
			return;
		}
		$('#dlg-role').dialog('open').dialog('setTitle', '修改角色').window("center");
		$('#fm').form('load', row);
		url = '${app}/role/save.action?id=' + row.id;
	}
	function del(){
		var row = $('#dg-role').datagrid('getSelected');
		if (!row) {
			$.messager.alert('提示信息', '请选择要删除的角色');
			return;
		}
		$.messager.confirm('删除确认', '你确定要删除该角色吗?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/role/del.action', {id : row.id}, function(result) {
					hideProgress();
					if ('-1' == result.retcode) {
						$.messager.alert('错误信息', result.retmsg, 'error');
						return;
					} 
					$('#dg-role').datagrid('reload');
					$('#dg-menu').datagrid('reload');
				},"json");
			}
		});
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
					return;
				} 
				$('#dlg-role').dialog('close');
				$('#dg-role').datagrid('reload');
				
			}
		});
	}
	//待选菜单列表
	function menuSelect(){
		
		var row = $('#dg-role').datagrid('getSelected');
		if (!row) {
			$.messager.alert('提示信息', '请先选择一个角色', 'error');
			return;
		}
		$('#sel-menu').tree({
		    url:'${app}/role/getMenuTree.action?roleId='+row.id,
		    checkbox:true,
		    lines:true
		});
		$('#dlg-sel-menu').dialog('open').dialog('setTitle', row.name+'角色待绑菜单').window("center");
	}
	//保存已选菜单
	function saveSelMenu(){
		var row = $('#dg-role').datagrid('getSelected');
		if (row) {
			var nodes = $('#sel-menu').tree('getChecked', ['checked','indeterminate']);
			if(!nodes||nodes.length==0){
				$.messager.alert('提示信息', '请选择菜单', 'error');
				return;
			}
			var menuIds=new Array();
			$.each(nodes,function(i,node){
				menuIds.push(node.id);
			});
			showHandleProgress();
			$.ajax({
				type : 'POST',
				url : '${app}/role/saveRoleMenu.action',
				data : 'roleId='+row.id+'&menuIds='+menuIds,
				dataType:"json",
				success : function(result) {
					hideProgress();
					if ('-1' == result.retcode) {
						$.messager.alert('错误信息', result.retmsg, 'error');
						return;
					} 
					$('#dlg-sel-menu').dialog('close');
					$('#dg-menu').datagrid('reload');
					
				}
			});
		}
	}
	
	//删除已选菜单
	function menuDel(){
		var rowRole = $('#dg-role').datagrid('getSelected');
		if(!rowRole){
			$.messager.alert('提示信息', '请选择角色');
			return;
		}
		var rowMenu = $('#dg-menu').datagrid('getSelected');
		if(!rowMenu){
			$.messager.alert('提示信息', '请先选择菜单', 'error');
			return;
		}
		$.messager.confirm('删除确认', '您确定要删除该绑定菜单?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/role/delRoleMenu.action', {
					roleId : rowRole.id,
					menuId:rowMenu.id
				}, function(result) {
					hideProgress();
					if ('-1' == result.retcode) {
						$.messager.alert('错误信息', result.retmsg, 'error');
						return;
					} 
					$('#dg-menu').datagrid('reload');
					
				},"json");
			}
		});
	}
	/**
		菜单排序 1:升序；2：降序
	*/
	function menuSort(sortFlag){
		var rowRole = $('#dg-role').datagrid('getSelected');
		if (!rowRole) {
			$.messager.alert('提示信息', '请先选择一个角色', 'error');
			return;
		}
		var rowMenu = $('#dg-menu').datagrid('getSelected');
		if (!rowMenu) {
			$.messager.alert('提示信息', '请先选择排序菜单', 'error');
			return;
		}
		showHandleProgress();
		$.post('${app}/role/menuSort.action', {sortFlag:sortFlag,roleId : rowRole.id,menuId:rowMenu.id}, function(result) {
			hideProgress();
			if ('-1' == result.retcode) {
				$.messager.alert('错误信息', result.retmsg, 'error');
				return;
			} 
			$('#dg-menu').datagrid('reload');
		},"json");
	}
</script>