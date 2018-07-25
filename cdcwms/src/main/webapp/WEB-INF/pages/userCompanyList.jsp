<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
td, th {
	white-space: nowrap;
}
</style>
<body class="easyui-layout" border="false">
	<div data-options="region:'east',title:'标签tid',split:false" style="width: 25%;">
		<div id="yyy">
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="javascript:bindLabel()"> 绑定标签tid</a> <a
				href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="javascript:dell()"> 删除</a>

		</div>

		<table id="dg1" class="easyui-datagrid" data-options="title:'',url:'',toolbar:'#yyy',rownumbers:true,pagination:true,singleSelect:true,fit:true">
			<thead>
				<tr>

					<th data-options="sortable:true,field:'tid',width:270">人员标签tid</th>

				</tr>
			</thead>
		</table>
	</div>
	<div data-options="region:'west',title:'用户列表',split:false" style="width: 25%;">
		<td>
			<table id="dgl" class="easyui-datagrid"
				data-options="title:'',url:'${app}/user/list.action',toolbar:'#toolbar',rownumbers:true,pagination:true,singleSelect:true,fit:true">
				<thead>
					<tr>
						<th data-options="sortable:true,field:'userLoginname',width:100">用户登陆名</th>
						<th data-options="sortable:true,field:'truename',width:200">用户真实姓名</th>
					</tr>
				</thead>
			</table>
		</td>
		<div id="dlgw" class="easyui-dialog" style="width: 350px;" data-options="closed:true,buttons:'#dlg-buttonsw'">
			<form id="fmw" method="post">

				<div class="fitem">
					<label>仓库名称：</label> <input name="whCode" id="whCode" class="easyui-combobox"
						data-options="valueField:'whCode',textField:'whName',url:'${app}/userCompany/getWarehouseCombobox.action',editable:false,panelHeight:'auto',required:true,missingMessage:'请选择仓库。'">
				</div>
			</form>
		</div>
	</div>


	<div data-options="region:'center',title:''" border="false"background:#eee;">

		<div id="dd" class="easyui-layout" fit="true">

			<div data-options="region:'east',title:'',split:false" style="width: 45%;">
				<div id="www">
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="javascript:bindWarehouse()">绑定仓库</a> <a
						href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="javascript:del()"> 删除</a>
				</div>

				<table id="dg3" class="easyui-datagrid" data-options="title:'仓库列表',url:'',toolbar:'#www',rownumbers:true,pagination:true,singleSelect:true,fit:true">
					<thead>
						<tr>
							<th data-options="sortable:true,field:'whName',width:270">仓库名称</th>
						</tr>
					</thead>
				</table>

			</div>
			<div data-options="region:'center',title:'公司列表'" style="padding: 0px;">
				<div id="sss">
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="javascript:bindCompany()">绑定公司</a> <a
						href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="javascript:delc()"> 删除</a>

				</div>

				<table id="dg2" class="easyui-datagrid" data-options="title:'',url:'',toolbar:'#sss',rownumbers:true,pagination:true,singleSelect:true,fit:true">
					<thead>
						<tr>

							<th data-options="sortable:true,field:'companyName',width:100">公司名称</th>
							<th data-options="sortable:true,field:'insertUser',width:100">创建人</th>
							<th data-options="sortable:true,field:'insertTime',width:150">创建日期</th>

						</tr>
					</thead>
				</table>
			</div>
			<div id="dlg" class="easyui-dialog" style="width: 350px;" data-options="closed:true,buttons:'#dlg-buttons'">
				<form id="fm" method="post">
					<input type="hidden" name="companyid" id="companyid" /> <input type="hidden" name="companyName" id="companyName" /> <input type="hidden"
						name="companyType" id="companyType" />
					<div class="fitem">
						<label>公司名称：</label> <input class="easyui-combobox" name="Name" id="Name" style="width: 145px;"
							data-options="valueField:'conName',textField:'conName',url:'${app}/conpany/getComboboxByConCode.action',editable:false,panelHeight:'auto',required:true,missingMessage:'请选择公司。'" />
					</div>
				</form>
			</div>
		</div>

		<div id="dlgr" class="easyui-dialog" style="width: 350px;" data-options="closed:true,buttons:'#dlg-buttonsr'">
			<form id="fmr" method="post">
				<div class="fitem">
					<label>人员标签名称：</label> <input name="tid" id="tid" class="easyui-textbox" data-options="required:true,missingMessage:'位置标签不能为空。',editable:true">
					<!-- <input type="button" onclick="getDeviceID()" value="获取" />  -->
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="getDeviceID()">读取</a>
				</div>
			</form>
		</div>

		<div id="dlg-buttons">
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="bindCompanySave()">确定</a> <a href="javascript:;"
				class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="javascript:$('#dlg').dialog('close')">取消</a>
		</div>
		<div id="dlg-buttonsw">
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="bindWarehouseSave()">确定</a> <a href="javascript:;"
				class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="javascript:$('#dlgw').dialog('close')">取消</a>
		</div>
		<div id="dlg-buttonsr">
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="bindLabelSave()">确定</a> <a href="javascript:;"
				class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="javascript:$('#dlgr').dialog('close')">取消</a>
		</div>
	</div>

</body>



<script>
	var url = "";
	$(function() {
		//渲染按钮
		Hmgx.RenderButton("butDiv",
<%=session.getAttribute("curLoginUserButton")%>
	, "${param.pageId}");
	});

	$("#Name").combobox({
		onSelect : function(param) {
			$("#companyid").val(param.companyid);
			$("#companyName").val(param.conName);
			$("#companyType").val(param.type);
		},
		mode : "remote",
		editable : "true"
	});

	function query() {
		$('#dg').datagrid('reload', {
			areaname : $("#QxName").val().trim()
		});
	}

	$('#dgl').datagrid({
		onSelect : function(index, row) {
			//获取仓库信息
			$('#dg3').datagrid({
				url : "${app}/userBindWh/getPageWarehouse.action",
				queryParams : {
					userLoginname : row.userLoginname,
					page : 10,
					rows : 10
				}
			});
			//获取标签tid信息
			$('#dg1').datagrid({
				url : "${app}/userLabel/getPage.action",
				queryParams : {
					userLoginname : row.userLoginname,
					page : 10,
					rows : 10
				}
			});
			//获取公司信息
			$('#dg2').datagrid({
				url : "${app}/userCompany/getPage.action",
				queryParams : {
					userLoginname : row.userLoginname,
					page : 10,
					rows : 10
				}
			});
		}
	});
	//绑定公司
	function bindCompany() {
		var row = Hmgx.GetSelectRow("dgl", "<div style='hieght:28px; line-height:28px;'>请选择需要绑定的用户！</div>");
		if (!row)
			return;
		$('#dlg').dialog('open').dialog('setTitle', '绑定公司').window("center");
		$('#fm').form('clear');
		$('#fm').form('load', row);
		url = "${app}/userCompany/save.action";
	};
	//保存数据
	function bindCompanySave() {
		var row = Hmgx.GetSelectRow("dgl", "<div style='hieght:28px; line-height:28px;'>请选择需要绑定的用户！</div>");
		if (!row)
			return;
		if (!$('#fm').form('validate')) {
			return false;
		}
		showHandleProgress();
		$.ajax({
			type : "POST",
			url : url,
			data : {
				userId : row.id,
				userLoginname : row.userLoginname,
				companyid : $("#companyid").val(),
				companyName : $("#companyName").val(),
				companyType : $("#companyType").val()
			},
			success : function(result) {
				hideProgress();
				if ('-1' == result.retcode) {
					$.messager.alert('错误信息', result.retmsg, 'error');
				} else {
					$.messager.alert('提示信息', result.retmsg, 'info');
					$('#dlg').dialog('close');
					$('#dg2').datagrid('reload');
				}
			}
		});
	}
	function delc() {
		var row = Hmgx.GetSelectRow("dg2", "<div style='hieght:28px; line-height:28px;'>请选择需要删除的记录！</div>");
		if (!row)
			return;
		$.messager.confirm('删除确认', '您确认要删除？', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/userCompany/del.action', {
					id : row.id
				}, function(result) {
					hideProgress();
					if ('-1' == result.retcode) {
						$.messager.alert('错误信息', result.retmsg, 'error');
					} else {
						$('#dg2').datagrid('reload');
					}
				});
			}
		});

	}

	//绑定仓库
	function bindWarehouse() {
		var row = Hmgx.GetSelectRow("dgl", "<div style='hieght:28px; line-height:28px;'>请选择需要绑定的用户！</div>");
		if (!row)
			return;
		$('#dlgw').dialog('open').dialog('setTitle', '绑定仓库').window("center");
		$('#fmw').form('clear');
		$('#fmw').form('load', row);

		url = "${app}/userBindWh/save.action";
	};

	function bindWarehouseSave() {

		var row = Hmgx.GetSelectRow("dgl", "<div style='hieght:28px; line-height:28px;'>请选择需要绑定的用户！</div>");
		if (!row)
			return;
		if (!$('#fmw').form('validate')) {
			return false;
		}

		showHandleProgress();

		$.ajax({
			type : "POST",
			url : url,

			data : {
				userId : row.id,
				userLoginname : row.userLoginname,
				whCode : $("#whCode").combobox("getValue")
			},
			success : function(result) {
				hideProgress();
				if ('-1' == result.retcode) {
					$.messager.alert('错误信息', result.retmsg, 'error');
				} else {
					$.messager.alert('提示信息', result.retmsg, 'info');
					$('#dlgw').dialog('close');
					$('#dg3').datagrid('reload');
				}
			}
		}); 
	}
	function bindLabel() {
		var row = Hmgx.GetSelectRow("dgl", "<div style='hieght:28px; line-height:28px;'>请选择需要绑定的用户！</div>");
		if (!row)
			return;
		$('#dlgr').dialog('open').dialog('setTitle', '绑定标签').window("center");
		$('#fmr').form('clear');
		$('#fmr').form('load', row);

		url = "${app}/userLabel/save.action";
	};
	function bindLabelSave() {
		var row = Hmgx.GetSelectRow("dgl", "<div style='hieght:28px; line-height:28px;'>请选择需要绑定的用户！</div>");
		if (!row)
			return;
		if (!$('#fmr').form('validate')) {
			return false;
		}
		showHandleProgress();
		$.ajax({
			type : "POST",
			url : url,
			data : {
				userId : row.id,
				userLoginname : row.loginname,
				tid : $("#tid").textbox("getValue")
			},
			success : function(result) {
				hideProgress();
				if ('-1' == result.retcode) {
					$.messager.alert('错误信息', result.retmsg, 'error');
				} else {
					$.messager.alert('提示信息', result.retmsg, 'info');
					$('#dlgr').dialog('close');
					$('#dg1').datagrid('reload');
				}
			}

		});

	}
	function dell() {
		var row = Hmgx.GetSelectRow("dg1", "<div style='hieght:28px; line-height:28px;'>请选择需要删除的记录！</div>");
		if (!row)
			return;
		$.messager.confirm('删除确认', '您确认要删除？', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/userLabel/del.action', {
					id : row.tid
				}, function(result) {
					hideProgress();
					if ('-1' == result.retcode) {
						$.messager.alert('错误信息', result.retmsg, 'error');
					} else {
						$('#dg1').datagrid('reload');
					}
				});
			}
		});
	}
	//获取设备数据
	function getDeviceID() {

		showHandleProgress();

		$.ajax({
			url : "${url}",
			type : "POST",
			data : {},
			timeout : 5000,
			dataType : "jsonp",
			jsonpCallback : "localJsonpCallback",
			complete : function(XMLHttpRequest, status) {
				if (status == 'timeout') {
					$.messager.alert("提示信息", "连接设备超时,请检查设备！", "error");
					hideProgress();
				}
			}
		});
	}
	//回调函数
	function localJsonpCallback(json) {
		if (json.SUCCESS) {
			$("#tid").textbox('setValue', json.TID);
		} else {
			$.messager.alert("提示信息", "获取数据失败,请检查卡！", "error");
		}
		hideProgress();
	}
	
	function del(){var row = Hmgx.GetSelectRow("dg3", "<div style='hieght:28px; line-height:28px;'>请选择需要删除的记录！</div>");
	if (!row)
		return;
	$.messager.confirm('删除确认', '您确认要删除？', function(r) {
		if (r) {
			showHandleProgress();
			$.post('${app}/userBindWh/del.action', {
				id : row.id
			}, function(result) {
				hideProgress();
				if ('-1' == result.retcode) {
					$.messager.alert('错误信息', result.retmsg, 'error');
				} else {
					$('#dg3').datagrid('reload');
				}
			});
		}
	});
		
	}
</script>