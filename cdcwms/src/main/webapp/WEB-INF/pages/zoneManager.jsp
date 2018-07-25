<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
td, th {
	white-space: nowrap;
}
</style>
<body class="easyui-layout">

	<div data-options="region:'west',title:'',split:true,border:false" style="width: 30%; padding: 5px;">
		<table id="userList" class="easyui-datagrid" data-options="title:'用户列表',url:'',toolbar:'#toolbar',rownumbers:true,pagination:true,singleSelect:true,fit:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'userLoginname',width:100">用户登陆名</th>
					<th data-options="sortable:true,field:'truename',width:200">用户真实姓名</th>
				</tr>
			</thead>
		</table>
	</div>


	<div data-options="region:'center',title:'',border:false" style="padding: 5px;">
		<table id="whList" class="easyui-datagrid" data-options="title:'库区列表',url:'',toolbar:'#butDiv',rownumbers:true,pagination:true,singleSelect:true,fit:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'groupid',width:80">库位组编码</th>
					<th data-options="sortable:true,field:'zonecode',width:100">所属库区编号</th>
					<th data-options="sortable:true,field:'zonename',width:100">库区名称</th>
					<th data-options="sortable:true,field:'whcode',width:100">所属仓库编号</th>
					<th data-options="sortable:true,field:'updateUser',width:80">最后更新人</th>
					<th data-options="sortable:true,field:'updateTime',width:150">最后更新时间</th>

				</tr>
			</thead>
		</table>

		<div id="butDiv"></div>
	</div>


	<div id="dlgWh" class="easyui-dialog" style="width: 600px;" data-options="modal:true,closed:true,buttons:'#dlgWh-button'">
		<form id="fmw" method="post">
			<input type="hidden" name="groupid" id="Groupid" />
			<input type="hidden" name="groupName" id="groupName" />
			<div class="fitem" id="">
				<label>库位组：</label>
				<input class="easyui-combobox" name="groupid" id="groupid" style="width: 145px;"
					data-options="valueField:'groupid',textField:'groupid',url:'${app}/storagGroup/getCombobox.action',editable:false,panelHeight:'auto',required:false,missingMessage:'零件不能为空'" />
				<label>库区：</label>
				<input class="easyui-combobox" name="zonecode" id="zonecode" style="width: 145px;"
					data-options="valueField:'zonecode',textField:'zname',url:'${app}/zone/getCombobox.action',editable:false,panelHeight:'auto',required:false,missingMessage:'请选择库区。'" />
			</div>
		</form>
	</div>
	<div id="dlgWh-button">
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="bindSave()">确定</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="javascript:$('#dlgWh').dialog('close')">取消</a>
	</div>

</body>

<script>
	var url = "";
	$(function() {
		//渲染按钮
		Hmgx.RenderButton("butDiv",
<%=session.getAttribute("curLoginUserButton")%>
	, "${param.pageId}");

		$('#userList').datagrid({
			url : "${app}/zoneUser/getUserList.action",
			onSelect : function(index, row) {
				//获取库区信息 
				$('#whList').datagrid({
					url : "${app}/zoneUser/getPageZone.action",
					queryParams : {
						userLoginname : row.userLoginname,
					}
				});
			}
		});
	});

	//绑定库区 
	function bind() {
		var row = Hmgx.GetSelectRow("userList", "<div style='hieght:28px; line-height:28px;'>请选择需要绑定的用户！</div>");
		if (!row)
			return;
		$('#dlgWh').dialog('open').dialog('setTitle', '绑定库区').window("center");
		$('#groupid').combobox({
			value : null
		});
		$('#zonecode').combobox({
			value : null
		});
		url = "${app}/zoneUser/save.action";
	};

	//保存绑定数据
	function bindSave() {
		var row = Hmgx.GetSelectRow("userList", "<div style='hieght:28px; line-height:28px;'>请选择需要绑定的用户！</div>");
		if (!row)
			return;
		var zonecode = $("#zonecode").textbox("getValue");
		var groupid = $("#groupid").textbox("getValue");
		showHandleProgress();
		$.ajax({
			type : 'POST',
			url : url,
			data : {
				userid : row.id,
				userloginname : row.userLoginname,
				zonecode : zonecode,
				groupid : groupid
			},

			success : function(result) {
				hideProgress();
				if (!result.suc) {
					$.messager.alert('错误信息', result.retmsg, 'error');
				} else {
					$('#dlgWh').dialog('close');
					$('#whList').datagrid('reload');
				}
			}
		});
	}

	//解除绑定
	function unBind() {
		var row = Hmgx.GetSelectRow("whList", "<div style='hieght:28px; line-height:28px;'>请选择需要解除的记录！</div>");
		if (!row)
			return;
		$.messager.confirm('接触绑定确认', '您确认要执行解绑？', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/zoneUser/del.action', {
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
	$("#groupid").combobox({
		onSelect : function(param) {
			$("#Groupid").val(param.groupid);
			$("#groupName").val(param.groupName);

		},
		mode : "remote",
		editable : "true"
	});
</script>