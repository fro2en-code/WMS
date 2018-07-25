<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'center',title:'',border:false"
		style="padding: 5px;">

		<table id="dg" title="物料供应商关系表 " class="easyui-datagrid"
			data-options="remoteSort:false,url:'${app}/wmsGoodsOraRelation/list.action',fit:true,rownumbers:true,toolbar:'#toolbar',singleSelect:true,pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'gcode',width:'150px'">零件编号</th>
					<th data-options="sortable:true,field:'gname',width:'150px'">零件名称</th>
					<th data-options="sortable:true,field:'oraCode',width:'100px'">供应商编号</th>
					<th data-options="sortable:true,field:'oraName',width:'200px'">供应商名称</th>
					<th data-options="sortable:true,field:'whCode',width:'100px'">仓库编号</th>
					<th data-options="sortable:true,field:'gtype',width:'100px'">零件用途</th>
					<th data-options="sortable:true,field:'storageType',width:'80px',formatter:forma_storageType">存储类型</th>
					<th data-options="sortable:true,field:'boxType',width:'80px',formatter:forma_boxType">存储包装类型</th>
					<th data-options="sortable:true,field:'warningMaxNum',width:'100px'">最大预警库存</th>
					<th data-options="sortable:true,field:'warningMinNum',width:'100px'">最小预警库存</th>
				</tr>
			</thead>
		</table>
	</div>

	<div id="toolbar">
		<form id="queryForm" name="queryForm"
			style="padding: 0px; margin: 0px;">
			<table id="tb_query">
				<tr>
					<th>零件编号：</th>
					<td><input name="gcode" id="gcode" class="easyui-textbox"
						style="width: 120px"></td>
					<th>供应商编号：</th>
					<td><input name="oraCode" id="oraCode" class="easyui-textbox"
						style="width: 120px"></td>
					<td><a href="javascript:;" class="easyui-linkbutton"
						data-options="iconCls:'icon-search',plain:true" onclick="query()">查询</a>
					</td>
				</tr>
			</table>
			<div style="border-top: 1px dashed #0066CC;"></div>
		</form>
		<div id="butbar"></div>
	</div>
	<!-- 保存或修改对话框 -->
	<div id="dlg" class="easyui-dialog" style="width: 600px;"
		data-options="closed:true,modal:true,buttons:'#dlg-buttons'">
		<form id="fm" method="post">
			<div class="fitem" id="">
				<label>零件编号:</label> <input name="gcode" class="easyui-textbox"
					style="width: 145px;"
					data-options="required:true,missingMessage:'零件编号不能为空。'"> <label>零件名称:</label>
				<input name="gname" class="easyui-textbox" style="width: 145px;"
					data-options="required:true,missingMessage:'零件名称不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>最大预警库存:</label> <input name="warningMaxNum" class="easyui-textbox"
					style="width: 145px;"
					data-options="required:true,missingMessage:'最大预警库存不能为空。'"> <label>最小预警库存:</label>
				<input name="warningMinNum" class="easyui-textbox" style="width: 145px;"
					data-options="required:true,missingMessage:'最小预警库存不能为空。'">
			</div>
			<div class="fitem" id="">
			    <label>零件用途：</label> <select name="gtype" class="easyui-combobox"
					style="width: 145px;"
					data-options="required:true,missingMessage:'零件用途不能为空。',editable:false,panelHeight:'auto'">
					<option value="生产件">生产件</option>
					<option value="备件">备件</option>
					<option value="出口件">出口件</option>
				</select>
				<label>存储类型：</label> <select name="storageType"
					class="easyui-combobox" style="width: 145px;"
					data-options="required:true,missingMessage:'存储类型不能为空。',editable:false,panelHeight:'auto'">
					<option value="1">托盘</option>
					<option value="2">堆放</option>
				</select>
			</div>
			<div class="fitem" id="">
				<label>存储包装类型：</label> <select name="boxType"
					class="easyui-combobox" style="width: 145px;"
					data-options="required:true,missingMessage:'存储包装类型不能为空。',editable:false,panelHeight:'auto'">
					<option value="1">纸包装</option>
					<option value="2">料盒</option>
					<option value="3">大件器具</option>
				</select>
				<label>供应商：</label>
				<input class="easyui-combobox" name="oraCode" id="oraCodes"
					style="width: 145px;"
					data-options="valueField:'companyid',textField:'companyid',url:'${app}/conpany/getComboboxByConCode.action',editable:false,panelHeight:'auto',required:true,missingMessage:'请选择供应商。'" />
			</div>
			<input type="hidden" name="oraName" id="oraNames" />
		</form>
	</div>

	<div id="dlg-buttons">
		<a href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-ok'" onclick="save()">保存</a> <a
			href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-cancel'"
			onclick="javascript:$('#dlg').dialog('close')">取消</a>
	</div>
</body>

<script type="text/javascript">
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
	
	//存储包装类型
	function forma_boxType(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 1:
			return "纸包装";
			break;
		case 2:
			return "料盒";
			break;
		case 3:
			return "大件器具";
			break;
		default:
			return "未知";
			break;
		}
	}
	
	//存储类型
	function forma_storageType(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 1:
			return "托盘";
			break;
		case 2:
			return "堆放";
			break;
		default:
			return "未知";
			break;
		}
	}

	//查询
	function query() {
		var gcode = $.trim($('input[name=gcode]').val());
		var oraCode = $.trim($('input[name=oraCode]').val());
		$("#dg").datagrid('reload', {
			gcode : gcode,
			oraCode : oraCode
		});
	}
	var url;
	//新增
	function add() {
		$('#login-name').css('display', '');
		$('#dlg').dialog('open').dialog('setTitle', '新增').window("center");
		$('#fm').form('clear');
		url = '${app}/wmsGoodsOraRelation/save.action';
	}

	//修改
	function edit() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要修改的数据！");
		if (!row) {
			return;
		}
		$('#dlg').dialog('open').dialog('setTitle', '修改');
		$('#fm').form('load', row);
		url = '${app}/wmsGoodsOraRelation/save.action?id=' + row.id;
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
		$.messager.confirm('删除确认', '确定要删除该信息?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/wmsGoodsOraRelation/del.action', {
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