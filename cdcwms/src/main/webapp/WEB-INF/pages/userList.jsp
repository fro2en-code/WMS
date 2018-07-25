<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<body class="easyui-layout">
	<div data-options="region:'center',title:'',border:false" style="padding: 5px;">
		<table id="dg" title="用户管理" url="${app}/user/list.action" fit="true" class="easyui-datagrid" toolbar="#toolbar" rownumbers="true" singleSelect="true"
			pagination="true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'ck',checkbox:true"></th>
					<th data-options="sortable:true,field:'userLoginname',width:100">登录名称</th>
					<th data-options="sortable:true,field:'truename',width:100">姓名</th>
					<th data-options="sortable:true,field:'updateUser',width:100">更新人</th>
					<th data-options="sortable:true,field:'updateTime',width:150">更新时间</th>
				</tr>
			</thead>
		</table>
	</div>

	<div id="toolbar">
		<div title="" class="easyui-panel">
			<table id="tb_query">
				<tr>
					<th>查询关键字：</th>
					<td>
						<input name="searchKey" class="easyui-textbox">
					</td>
					<td>
						<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="query()">查询</a>
					</td>
				</tr>
			</table>
		</div>
		<div id="tabBut"></div>
	</div>
	<!-- 保存或修改对话框 -->
	<div id="dlg" class="easyui-dialog" style="width: 350px" closed="true" modal="true" buttons="#dlg-buttons">
		<form id="fm" method="post">
			<input type="hidden" name="id" />
			<div class="fitem" id="div-loginname">
				<label>登录名：</label>
				<input name="loginname" class="easyui-textbox" id="loginname" data-options="required:true,missingMessage:'登录名称不能为空。'">
			</div>
			<div class="fitem" id="div-pwd">
				<label>密码：</label>
				<input name="pwd" class="easyui-textbox" id="pwd" data-options="required:true,missingMessage:'密码不能为空。',validType:'length[4,20]'">
			</div>
			<div class="fitem">
				<label>姓名：</label>
				<input name="truename" class="easyui-textbox" data-options="required:true,missingMessage:'姓名不能为空。'" />
			</div>
			<div class="fitem">
				<label>角色：</label>
				<input class="easyui-combobox" name="userRoles" id="userRoles"
					data-options="valueField:'id',url:'${app}/role/getRoleListByPlat.action',textField:'name',editable:false,multiple:true,panelHeight:'auto',required:true,missingMessage:'请选择角色。'" />
			</div>
		</form>
	</div>

	<div id="dlg-buttons">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="save()">保存</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	</div>

</body>
<script type="text/javascript">
	$(function() {
		//渲染按钮
		Hmgx.RenderButton("tabBut",
<%=session.getAttribute("curLoginUserButton")%>
	, "${param.pageId}");
	});

	//查询
	function query() {
		var searchKey = $.trim($('input[name=searchKey]').val());
		$("#dg").datagrid('reload', {
			searchKey : searchKey
		});
	}

	var url;
	//新增用户
	function add() {
		$('#dlg').dialog('open').dialog('setTitle', '新增用户').window("center");
		$('#fm').form('clear');
		url = '${app}/user/save_user.action';
	}

	//修改用户
	function edit() {
		var row = $('#dg').datagrid('getSelected');
		if (!row) {
			return;
		}
		//
		$.post('${app}/user/getRolesByUser.action', {
			loginName : row.userLoginname
		}, function(result) {
			row.userRoles = result;
			$('#dlg').dialog('open').dialog('setTitle', '修改');
			$('#fm').form('clear');
			$('#fm').form('load', row);
		}, "json");
		//
		//if (row.userRoles) {
		//	$("#userRoles").combobox("setValues", eval('(' + row.userRoles + ')'));
		//}
	}
	function save() {
		if (!$('#fm').form('validate')) {
			return false;
		}
		showHandleProgress();
		$.ajax({
			type : 'POST',
			url : '${app}/user/save_user.action',
			data : $('#fm').serialize(),
			success : function(data) {
				hideProgress();
				var result = eval('(' + data + ')');
				if ('-1' == result.retcode) {
					$.messager.alert('错误信息', result.retmsg, 'error');
				} else {
					$('#dlg').dialog('close');
					$('#dg').datagrid('reload');
				}
			}
		});
	}

	function enabled() {
		var row = $('#dg').datagrid('getSelected');
		if (row) {
			if (row.state == 0) {
				alert('该用户已启用！');
				return;
			}
			$.messager.confirm('提示信息', '你确定启用该用户吗?', function(r) {
				if (r) {
					showHandleProgress();
					$.post('${app}/user/enabled_user.action', {
						loginname : row.userLoginname
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
		} else {
			alert('请选择一条记录');
		}

	}

	function del() {
		var row = $('#dg').datagrid('getSelected');
		if (!row) {
			$.messager.alert('提示信息', '请选择要删除的用户');
			return;
		}
		$.messager.confirm('删除确认', '你确定要删除该用户吗?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/user/del.action', {
					loginname : row.userLoginname
				}, function(result) {
					hideProgress();
					if ('-1' == result.retcode) {
						$.messager.alert('错误信息', result.retmsg, 'error');
						return;
					}
					$('#dg').datagrid('reload');
				}, "json");
			}
		});
	}

	function disable() {
		var row = $('#dg').datagrid('getSelected');
		if (row) {
			if (row.state == 1) {
				alert('此用户已停用！');
				return;
			}
			$.messager.confirm('提示信息', '您确定要停用该用户?', function(r) {
				if (r) {
					showHandleProgress();
					$.post('${app}/user/disable_user.action', {
						loginname : row.userLoginname
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
		} else {
			alert('请选择一条记录');
		}
	}

	//重置密码
	function resetPass() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要重置密码的用户！");
		if (!row) {
			return;
		}
		$.messager.prompt('重置密码', '请输入新密码！', function(r) {
			if (r) {
				if (r != "") {
					showHandleProgress();
					$.post("${app}/user/resetPass.action", {
						loginname : row.userLoginname,
						pwd : r
					}, function(result) {
						hideProgress();
						if ('-1' == result.retcode) {
							$.messager.alert('重置失败', result.retmsg, 'error');
						} else {
							$.messager.alert('重置成功', result.retmsg, 'info');
						}
					}, "json");
				}
			}
		});
	}
</script>