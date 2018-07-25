<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
td,th {
	white-space: nowrap;
}
</style>
<body class="easyui-layout">

	<div data-options="region:'west',title:'',split:true,border:false"
		style="width: 50%; padding: 5px;">
		<table id="userList" class="easyui-datagrid"
			data-options="title:'用户列表',url:'',toolbar:'#toolbar',rownumbers:true,pagination:true,singleSelect:true,fit:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'loginname',width:100">用户登陆名</th>
					<th data-options="sortable:true,field:'truename',width:200">用户真实姓名</th>
				</tr>
			</thead>
		</table>
	</div>


	<div data-options="region:'center',title:'',border:false"
		style="padding: 5px;">
		<table id="whList" class="easyui-datagrid"
			data-options="title:'仓库列表',url:'',toolbar:'#butDiv',rownumbers:true,pagination:true,singleSelect:true,fit:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'whCode',width:80">仓库代码</th>
					<th data-options="sortable:true,field:'whName',width:100">仓库名称</th>
					<th data-options="sortable:true,field:'updateUser',width:80">最后更新人</th>
					<th data-options="sortable:true,field:'updateTime',width:150">最后更新时间</th>

				</tr>
			</thead>
		</table>

		<div id="butDiv"></div>
	</div>

	<div id="dlgWh" class="easyui-dialog" style="width: 350px;"
		data-options="modal:true,closed:true,buttons:'#dlgWh-button'">
		<form id="fmw" method="post">
			<div class="fitem">
				<label>仓库名称：</label> <input name="whCode" id="whCode"
					class="easyui-combobox"
					data-options="valueField:'whCode',textField:'whName',url:'${app}/warehouse/getCombobox.action',editable:false,panelHeight:'auto',required:true,missingMessage:'请选择仓库。'">
			</div>
		</form>
	</div>
	<div id="dlgWh-button">
		<a href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-ok'" onclick="bindSave()">确定</a> <a
			href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-cancel'"
			onclick="javascript:$('#dlgWh').dialog('close')">取消</a>
	</div>

</body>

<script>
	var url = "";
	$(function() {
		//渲染按钮
		Hmgx.RenderButton("butDiv",
<%=session.getAttribute("curLoginUserButton")%>
	,
				"${param.pageId}");

		$('#userList').datagrid({
			url : "${app}/userBindWh/getPageUser.action",
			onSelect : function(index, row) {
				//获取仓库信息
				$('#whList').datagrid({
					url : "${app}/userBindWh/getPageWarehouse.action",
					queryParams : {
						userLoginname : row.loginname,
					}
				});
			}
		});
	});

	//绑定仓库
	function bind() {
		var row = Hmgx
				.GetSelectRow("userList",
						"<div style='hieght:28px; line-height:28px;'>请选择需要绑定的用户！</div>");
		if (!row)
			return;
		$('#dlgWh').dialog('open').dialog('setTitle', '绑定仓库').window("center");
		url = "${app}/userBindWh/save.action";
	};

	//保存绑定数据
	function bindSave() {
		var row = Hmgx
				.GetSelectRow("userList",
						"<div style='hieght:28px; line-height:28px;'>请选择需要绑定的用户！</div>");
		if (!row)
			return;
		var whCode = $.trim($("#whCode").combobox("getValue"));
		if (whCode == "") {
			$.messager.alert('绑定失败', "您选择仓库！", 'error');
			return;
		}
		if (!$('#fm').form('validate')) {
			return false;
		}
		showHandleProgress();
		$.ajax({
			type : "POST",
			url : url,
			data : {
				userId : row.id,
				userLoginname : row.loginname,
				truename : row.truename,
				whCode : whCode
			},
			success : function(result) {
				hideProgress();
				if ('-1' == result.retcode) {
					$.messager.alert('错误信息', result.retmsg, 'error');
				} else {
					$.messager.alert('提示信息', result.retmsg, 'info');
					$('#dlgWh').dialog('close');
					$('#whList').datagrid('reload');
				}
			}
		});
	}

	//解除绑定
	function unBind() {
		var row = Hmgx
				.GetSelectRow("whList",
						"<div style='hieght:28px; line-height:28px;'>请选择需要解除的记录！</div>");
		if (!row)
			return;
		$.messager.confirm('接触绑定确认', '您确认要执行解绑？', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/userBindWh/del.action', {
					id : row.id
				}, function(result) {
					hideProgress();
					if ('-1' == result.retcode) {
						$.messager.alert('错误信息', result.retmsg, 'error');
					} else {
						$('#whList').datagrid('reload');
					}
				});
			}
		});

	}
</script>