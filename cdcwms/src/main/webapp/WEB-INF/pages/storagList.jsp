<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'center',title:'',border:false" style="padding: 5px;">

		<table id="dg" title="库位列表 " class="easyui-datagrid"
			data-options="remoteSort:false,url:'${app}/storag/list.action',fit:true,rownumbers:true,toolbar:'#toolbar',pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'ck',checkbox:true"></th>
					<th data-options="sortable:true,field:'storageCode',width:'100px'">库位编号</th>
					<th data-options="sortable:true,field:'zoneCode',width:'100px'">所属库区</th>
					<th data-options="sortable:true,field:'whCode',width:'100px'">所属仓库编号</th>
					<th data-options="sortable:true,field:'groupCode',width:'100px'">所属库位组</th>
					<th data-options="sortable:true,field:'lineNo',width:'80px'">道编号</th>
					<th data-options="sortable:true,field:'colNo',width:'80px'">列编号</th>
					<th data-options="sortable:true,field:'rowNo',width:'80px'">排编号</th>
					<th data-options="sortable:true,field:'layerNo',width:'80px'">层编号</th>
					<th data-options="sortable:true,field:'storageType',width:'100px',formatter:forma_storageType">库位功能</th>
					<th data-options="sortable:true,field:'sType',width:'100px',formatter:forma_SType">库位类型</th>
					<th data-options="sortable:true,field:'layType',width:'100px',formatter:forma_layType">物品放置类别</th>
					<th data-options="sortable:true,field:'mulSup',width:'80px',formatter:forma_mulSup">供应商混放标志</th>
					<th data-options="sortable:true,field:'mulBth',width:'80px',formatter:forma_mulBth">批次混放标志</th>
					<th data-options="sortable:true,field:'statu',width:'100px',formatter:forma_statu">库位状态</th>
					<th data-options="sortable:true,field:'gStatu',width:'80px',formatter:forma_gStatu">货物状态</th>
					<th data-options="sortable:true,field:'updateUser',width:'80px'">最后修改人</th>
					<th data-options="sortable:true,field:'updateTime',width:'120px'">最后修改时间</th>

				</tr>
			</thead>
		</table>
	</div>
	<!-- 查询按钮 -->
	<div id="toolbar">
		<form id="queryForm" name="queryForm" style="padding: 0px; margin: 0px;">
			<table id="tb_query">
				<tr>
					<th>库位编号：</th>
					<td><input name="storageCode" id="storageCode" class="easyui-textbox" style="width: 120px"></td>

					<th>库区编号：</th>
					<td><input name="zoneCode" id="zoneCode" class="easyui-textbox" style="width: 120px"></td>

					<td><a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="query()">查询</a></td>
				</tr>
			</table>
			<div id="butDiv" style="border-top: 1px dashed #0066CC;"></div>
		</form>
		<div id="butbar"></div>
	</div>

	<!-- 保存或修改对话框 -->
	<div id="dlg" class="easyui-dialog" style="width: 600px" data-options="closed:true,modal:true,buttons:'#dlg-buttons'">
		<form id="fm" method="post">
			<div class="fitem" id="">
				<label>所属库区：</label> <input class="easyui-combobox" name="zoneCode" id="zoneCode" style="width: 145px;"
					data-options="valueField:'zonecode',textField:'zname',url:'${app}/zone/getCombobox.action',editable:false,panelHeight:'auto',required:true,missingMessage:'请选择所属库区。'" />

				<label>货物状态：</label> <select name="gStatu" class="easyui-combobox" style="width: 145px;"
					data-options="required:false,missingMessage:'请选择货物状态不能为空。',editable:false,panelHeight:'auto'">
					<option value="0">空库位</option>
					<option value="1">预分配</option>
					<option value="2">有货</option>
				</select>
			</div>
			<div class="fitem" id="">
				<label>库位编号：</label> <input name="storageCode" id="storageCode" class="easyui-textbox" style="width: 145px;"
					data-options="required:true,missingMessage:'库位编号不能为空。'"> <label>库位组：</label> <input class="easyui-combobox" name="groupCode" id="groupCode"
					style="width: 145px;"
					data-options="valueField:'groupid',textField:'groupid',url:'${app}/storagGroup/getCombobox.action',editable:true,panelHeight:'auto',required:false" />
			</div>
			<div class="fitem" id="">
				<label>道编号：</label> <input name="lineNo" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'道编号不能为空。'"> <label>列编号：</label>
				<input name="colNo" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'列编号不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>排编号：</label> <input name="rowNo" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'排编号不能为空。'"> <label>层编号：</label>
				<input name="layerNo" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'层编号不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>库位功能：</label> <select name="storageType" class="easyui-combobox" style="width: 145px;"
					data-options="required:false,missingMessage:'请选择库位功能不能为空。',editable:false,panelHeight:'auto'">
					<option value="0">存储+拣货</option>
					<option value="1">存储</option>
				</select> <label>库位类别：</label> <select name="sType" class="easyui-combobox" style="width: 145px;"
					data-options="required:true,missingMessage:'请选择库位类别不能为空。',editable:false,panelHeight:'auto'">
					<option value="2">地面平仓</option>
					<option value="1">普通货架</option>
					<option value="3">高位货架</option>
				</select>
			</div>
			<div class="fitem" id="">
				<label>物品放置类别：</label> <select name="layType" class="easyui-combobox" style="width: 145px;"
					data-options="required:false,missingMessage:'请选择物品放置类别不能为空。',editable:false,panelHeight:'auto'">
					<option value="1">托盘放置</option>
					<option value="2">堆码放置</option>
				</select> <label>供应商混放标志：</label> <select name="mulSup" class="easyui-combobox" style="width: 145px;"
					data-options="required:false,missingMessage:'请选择供应商混放标志不能为空。',editable:false,panelHeight:'auto'">
					<option value="0">不允许</option>
					<option value="1">允许</option>
				</select>
			</div>
			<div class="fitem" id="">
				<label>批次混放标志：</label> <select name="mulBth" class="easyui-combobox" style="width: 145px;"
					data-options="required:true,missingMessage:'请选择批次混放标志不能为空。',editable:false,panelHeight:'auto'">
					<option value="0">不允许</option>
					<option value="1">允许</option>
				</select> <label>库位状态：</label> <select name="statu" class="easyui-combobox" style="width: 145px;"
					data-options="required:false,missingMessage:'请选择库位状态不能为空。',editable:false,panelHeight:'auto'">
					<option value="0">正常</option>
					<option value="1">停用</option>
				</select>
			</div>
		</form>
	</div>

	<div id="dlg-buttons">
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="save()">保存</a> <a href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-cancel'" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	</div>
	<!-- 绑定库位组对话框 -->
	<div id="dlgList" class="easyui-dialog" style="width: 500px" data-options="closed:true,modal:true,buttons:'#dlgList-buttons'">
		<form id="fmList" method="post">
			<div class="fitem">
				<label>库位组：</label> <input class="easyui-combobox" name="groupCode" id="groupCodes" style="width: 145px;"
					data-options="valueField:'groupid',textField:'groupid',url:'${app}/storagGroup/getCombobox.action',editable:true,panelHeight:'auto',required:true" />
			</div>

		</form>
	</div>
	<div id="dlgList-buttons">
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="bind()">保存</a> <a href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-cancel'" onclick="javascript:$('#dlgList').dialog('close')">取消</a>
	</div>

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
			url : "${app}/storag/bind.action",
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
	//库位状态 1停用 0 正常
	function forma_statu(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 0:
			return "正常";
			break;
		case 1:
			return "停用";
			break;
		default:
			return "未知";
			break;
		}
	}

	//物品放置类别： 1.托盘放置 2 堆码放置
	function forma_layType(val, row) {
		switch (val) {
		case 1:
			return "托盘放置";
			break;
		case 2:
			return "堆码放置";
			break;
		default:
			return "未知";
			break;
		}
	}

	//库位功能
	function forma_storageType(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 0:
			return "存储+拣货";
			break;
		case 1:
			return "存储";
			break;
		default:
			return "未知";
			break;
		}
	}

	//1普通货架 2地面平仓 3 高位货架
	function forma_SType(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 1:
			return "普通货架";
			break;
		case 2:
			return "地面平仓";
			break;
		case 3:
			return "高位货架";
			break;
		default:
			return "未知";
			break;
		}
	}

	//混放标志: 0不允许 1允许
	function forma_mulSup(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 0:
			return "不允许";
			break;
		case 1:
			return "允许";
			break;
		default:
			return "未知";
			break;
		}
	}
	//货物状态 0 空库位 1预分配 2 有货
	function forma_gStatu(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 0:
			return "空库位";
			break;
		case 1:
			return "预分配";
			break;
		case 2:
			return "有货";
			break;
		default:
			return "未知";
			break;
		}
	}

	//批次混放标志 : 0 不允许 1允许
	function forma_mulBth(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 0:
			return "不允许";
			break;
		case 1:
			return "允许";
			break;
		default:
			return "未知";
			break;
		}
	}
	//查询
	function query() {
		var storageCode = $.trim($('input[name=storageCode]').val());
		var zoneCode = $.trim($('input[name=zoneCode]').val());
		$("#dg").datagrid('reload', {
			storageCode : storageCode,
			zoneCode : zoneCode
		});
	}
	var url;
	//新增
	function add() {
		$('#login-name').css('display', '');
		$('#dlg').dialog('open').dialog('setTitle', '新增库位').window("center");
		$('#fm').form('clear');
		url = '${app}/storag/save.action';
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
		url = '${app}/storag/save.action?id=' + rows[0].id;
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
		if (null==rows || rows.length==0) {
			$.messager.alert('错误信息', '请选择一条数据!', 'error');
			return false;
		}
		if (rows.length > 1) {
			$.messager.alert('错误信息', '不支持多条数据同时删除,请选择一条记录删除!', 'error');
			return false;
		}
		$.messager.confirm('删除确认', '您确定要删除该库位?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/storag/del.action', {
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