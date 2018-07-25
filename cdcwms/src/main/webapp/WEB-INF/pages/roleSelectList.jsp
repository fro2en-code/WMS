<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style type="text/css">
.fitem {
	float: left;
}

.fitem#fitem-btn {
	margin-top: 70px;
}
</style>
<body class="easyui-layout">
	<div data-options="region:'center',title:'',border:false" style="padding: 5px;">
		<table id="roleList" title="角色列表" fit="true"
			url="${app}/role/list.action" rownumbers="true"
			singleSelect="true" pagination="true">
			<thead>
				<tr>
					<th field="name" width="100">角色名称</th>
					<th field="platURL" width="250">平台地址</th>
				</tr>
			</thead>
		</table>
	</div>
	
	<div data-options="region:'east',title:'',split:true,border:false" style="width:50%;padding:5px;">
		<table id="selectList" title="可选角色" fit="true" class="easyui-datagrid"
			url="" rownumbers="true" toolbar="#toolbar"
			singleSelect="true" pagination="true">
			<thead>
				<tr>
					<th field="name" width="100">可选角色</th>
					<th field="platURL" width="150">平台地址</th>
				</tr>
			</thead>
		</table>
	</div>
	
	<div id="toolbar">
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addRoleSelect()">新增可选择绑定</a>	
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="delRoleSelect()">删除可选绑定</a>
	</div>
	
	<!-- 保存或修改对话框 -->
	<div id="roleDlg" class="easyui-dialog" closed="true" buttons="#roleDlg-buttons" style="width:500px; height:400px;">
		<table id="newSelectList" title="角色列表" fit="true" class="easyui-datagrid"
			url="" rownumbers="true" border="false"
			singleSelect="false" pagination="true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'ck',checkbox:true"></th>					
					<th field="name" width="150">角色名称</th>
					<th field="platURL" width="100">平台地址</th>
				</tr>
			</thead>
		</table>
	</div>
	<div id="roleDlg-buttons">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok"
			onclick="save()">保存</a> <a href="#" class="easyui-linkbutton"
			iconCls="icon-cancel" onclick="javascript:$('#roleDlg').dialog('close')">取消</a>
	</div>

</body>
<script type="text/javascript">
	$(function() {
		//渲染按钮
		//Hmgx.RenderButton("toolbar",<%=session.getAttribute("curLoginUserButton")%>,"${param.pageId}");
		$("#roleList").datagrid({
			onSelect:function(index,row){
				$("#selectList").datagrid({url:"${app}/role/roleSelectList.action",
					queryParams: {
						roleId: row.id
					}
				});
			}
		});
	});
	
	//新增可选择角色
	function addRoleSelect() {
		var row = Hmgx.GetSelectRow("roleList","请先选择需要绑定的角色！");
		if(!row){
			return;
		}
		$('#roleDlg').dialog('open').dialog('setTitle', '新增可选择角色').window("center");
		$("#newSelectList").datagrid({url:"${app}/role/getUnBindSelect.action",
			queryParams: {
				roleId: row.id
			}
		});
	}
	
	//删除可选择角色
	function delRoleSelect() {
		var row = $('#selectList').datagrid('getSelected');
		if (row) {
			$.messager.confirm('删除确认', '您确定要删除？', function(r) {
				if (r) {
					showHandleProgress();
					$.post('${app}/role/delRoleSelectBind.action', {
						id : row.id
					}, function(result) {
						hideProgress();
						if ('-1' == result.retcode) {
							$.messager.alert('错误信息', result.retmsg, 'error');
						} else {
							$("#selectList").datagrid("reload");
						}
					},"json");
				}
			});
		}
	}

	//保存数据
	function save() {
		var row = Hmgx.GetSelectRow("roleList","请先选择需要绑定的角色！");
		if(!row){
			return;
		}
		var role = $('#newSelectList').datagrid('getSelections');
		var roleIds=[];
		if(role.length < 1 ){
			$.messager.alert("提示信息","请选择需要绑定的可选角色！","info");
			return;
		}else{
			for(var i = 0 ; i < role.length; i++){
				roleIds.push(role[i].id);
			}
		}
		$.post("${app}/role/saveRoleSelectBind.action",{roleId:row.id,roleIds:roleIds.join(",")},
			function(data){
				if(data.retcode=="0"){
					$('#roleDlg').dialog('close');
					$("#selectList").datagrid("reload");
				}else{
					$.messager.alert("提示信息",data.retmsg,"error");
				} 
		},"json");
	}

</script>
