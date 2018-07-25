<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'center',title:'',border:false"
		style="padding: 5px;">

		<table id="dg" title="库位策略列表 " class="easyui-datagrid"
			data-options="remoteSort:false,url:'${app}/locate/list.action',fit:true,rownumbers:true,toolbar:'#toolbar',pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'ck',checkbox:true"></th>
					<th data-options="sortable:true,field:'gcode',width:'100px'">零件编码</th>
					<th data-options="sortable:true,field:'gtype',width:'100px'">零件用途</th>
					<th data-options="sortable:true,field:'oraCode',width:'100px'">供应商编码</th>
					<th data-options="sortable:true,field:'oraName',width:'100px'">供应商名称</th>
					<th
						data-options="sortable:true,field:'storagezoneId',width:'100px',formatter:forma_storagezoneId">库区/库位</th>
					<th
						data-options="sortable:true,field:'storageMaxNum',width:'100px'">最大库存数</th>
				</tr>
			</thead>
		</table>
	</div>
	<!-- 查询按钮 -->
	<div id="toolbar">
		<form id="queryForm" name="queryForm"
			style="padding: 0px; margin: 0px;">
			<table id="tb_query">
				<tr>
					<th>零件编码：</th>
					<td><input name="g_code" id="g_code" class="easyui-textbox"
						style="width: 120px"></td>

					<th>供应商编码：</th>
					<td><input name="ora_code" id="ora_code"
						class="easyui-textbox" style="width: 120px"></td>

					<td><a href="javascript:;" class="easyui-linkbutton"
						data-options="iconCls:'icon-search',plain:true" onclick="query()">查询</a></td>
				</tr>
			</table>
			<div id="butDiv" style="border-top: 1px dashed #0066CC;"></div>
		</form>
		<div id="butbar"></div>
	</div>

	<!-- 保存或修改对话框 -->
	<div id="dlg" class="easyui-dialog" style="width: 600px"
		data-options="closed:true,modal:true,buttons:'#dlg-buttons'">
		<form id="fm" method="post">
			<div class="fitem" id="">
				<label>零件编码：</label> <input name="gcode" id="storageCode"
					class="easyui-textbox" style="width: 145px;"
					data-options="required:true,missingMessage:'零件编码不能为空。'">
				<!-- <select name="storage_code" id="storageCode" class="easyui-combobox" style="width: 145px;"
						data-options="required:true,missingMessage:'供应商编码不能为空。',editable:false,panelHeight:'auto'">
						<option value=""></option>
						
					</select> -->

				<label>零件用途：</label> <select name="gtype" class="easyui-combobox"
					style="width: 145px;"
					data-options="required:true,missingMessage:'零件用途不能为空。',editable:false,panelHeight:'auto'">
					<option value="生产件">生产件</option>
					<option value="备件">备件</option>
					<option value="出口件">出口件</option>
				</select>
			</div>

			<div class="fitem" id="">
				<label>库位类别：</label> <select name="storagezoneId"
					class="easyui-combobox" style="width: 145px;"
					data-options="required:true,missingMessage:'库位类别不能为空。',editable:false,panelHeight:'auto'">
					<option value="2">地面平仓</option>
					<option value="1">普通货架</option>
					<option value="3">高位货架</option>
				</select> <label>最大库存数：</label> <input name="storageMaxNum" id="storageCode"
					class="easyui-textbox" style="width: 145px;"
					data-options="required:true,missingMessage:'最大库存数不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>供应商编码：</label>
				<input class="easyui-combobox" name="oraCode" id="oraCodes"
					style="width: 145px;"
					data-options="valueField:'companyid',textField:'companyid',url:'${app}/conpany/getComboboxByConCode.action',editable:false,panelHeight:'auto',required:true,missingMessage:'请选择供应商。'" />
				<input type="hidden" name="oraName" id="oraNames" />
			</div>
		</form>
	</div>



	<!-- 保存/取消 -->
	<div id="dlg-buttons">
		<a href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-ok'" onclick="save()">保存</a> <a
			href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-cancel'"
			onclick="javascript:$('#dlg').dialog('close')">取消</a>
	</div>


	<!-- <div id="dlgList-buttons">
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="edit()">保存</a> <a href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-cancel'" onclick="javascript:$('#dlgList').dialog('close')">取消</a>
	</div> -->


</body>

<script type="text/javascript">
	//渲染按钮
	$(function() {
		//渲染按钮
		Hmgx.RenderButton("butbar",
<%=session.getAttribute("curLoginUserButton")%>
	,
				"${param.pageId}");


	});
	
	$("#oraCodes").combobox({
		onSelect : function(param) {
			$("#oraNames").val(param.conName);
		},
		mode : "remote",
		editable : "true"
	});

	function batchBind() {
		var rows = $('#dg').datagrid('getSelections');
		if (rows.length == 0) {
			$.messager.alert('错误信息', "请选择数据", 'error');
			return;
		}
		$('#dlgList').dialog('open').dialog('setTitle', '库位组信息').window(
				"center");
		$('#fmList').form('clear');
	}

	function bind() {
		var rows = $('#dg').datagrid('getSelections');
		if (!$('#fmList').form('validate')) {
			return false;
		}
		showHandleProgress();
		var data = Hmgx.getQueryParamet('fmList');
		for (var i = 0; i < rows.length; i++) {
			var row = rows[i];
			if (null == data.storags) {
				data.storags = new Array();
			}
			data.storags[data.storags.length] = row.storageCode;
		}
		$.ajax({
			type : 'POST',
			url : "${app}/locate/bind.action",
			data : data,
			success : function(result) {
				hideProgress();
				if (!result.suc) {
					$.messager.alert('错误信息', result.retmsg, 'error');
				} else {
					$('#dlgList').dialog('close');
					$('#dg').datagrid('reload');
				}
			}
		}).error(function() {
			hideProgress();
			$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
		});
	}

	//1普通货架 2地面平仓 3 高位货架
	function forma_storagezoneId(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case "1":
			return "普通货架";
			break;
		case "2":
			return "地面平仓";
			break;
		case "3":
			return "高位货架";
			break;
		default:
			return "未知";
			break;
		}
	}

	//查询
	function query() {
		var g_code = $.trim($('input[name=g_code]').val());
		var ora_code = $.trim($('input[name=ora_code]').val());
		$("#dg").datagrid('reload', {
			//将input框中的值赋给g_code/ora_code
			gcode : g_code,
			oraCode : ora_code
		});
		url = '${app}/locate/list.action';
	}
	var url;
	//新增
	function add() {
		$('#login-name').css('display', '');
		$('#dlg').dialog('open').dialog('setTitle', '新增库位').window("center");
		$('#fm').form('clear');
		url = '${app}/locate/save.action';
		//$("#storageCode").textbox("enable");
	}

	//修改
	function edit() {
		var rows = $('#dg').datagrid('getSelections');
		if (rows.length > 1) {
			$.messager.alert('错误信息', '只允许修改一条数据!', 'error');
			return false;
		}
		$('#dlg').dialog('open').dialog('setTitle', '修改库位');
		$('#fm').form('load', rows[0]);
		url = '${app}/locate/save.action?id=' + rows[0].id;
		//$("#storageCode").textbox("disable");
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
		var rows = $('#dg').datagrid('getSelections');
		if (null == rows || rows.length == 0) {
			$.messager.alert('错误信息', '请选择一条数据!', 'error');
			return false;
		}
		if (rows.length > 1) {
			$.messager.alert('错误信息', '不支持多条数据同时删除,请选择一条记录删除!', 'error');
			return false;
		}
		$.messager.confirm('删除确认', '您确定要删除该条数据吗?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/locate/del.action', {
					id : rows[0].id
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
	$("#groupCode").combobox({
		mode : "remote",
		editable : "true"
	});
	$("#groupCodes").combobox({
		mode : "remote",
		editable : "true"
	});
	$("#dg").datagrid({
		onLoadError : function() {
			$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
		}
	});
</script>