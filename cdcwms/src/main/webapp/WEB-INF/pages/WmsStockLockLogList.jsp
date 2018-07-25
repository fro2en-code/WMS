<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'center',title:'',border:false" style="padding: 5px;">
		<table id="dg" title="库存管理 " class="easyui-datagrid"
			data-options="remoteSort:false,url:'${app}/wmsStockLockLog/list.action',fit:true,rownumbers:true,toolbar:'#toolbar',singleSelect:true,pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'number',width:'100px'">编码</th>
					<th data-options="sortable:true,field:'state',width:'100px'">状态</th>
					<th data-options="sortable:true,field:'oraCode',width:'100px'">供应商编码</th>
					<th data-options="sortable:true,field:'oraName',width:'100px'">供应商名称</th>
					<th data-options="sortable:true,field:'gCode',width:'100px'">物料编码</th>
					<th data-options="sortable:true,field:'gName',width:'100px'">物料名称</th>
					<th data-options="sortable:true,field:'gType',width:'100px'">物料类型</th>
					<th data-options="sortable:true,field:'startTime',width:'100px'">批次开始时间</th>
					<th data-options="sortable:true,field:'endTime',width:'100px'">批次结束时间</th>
					<th data-options="sortable:true,field:'type',width:'100px'">操作类型</th>
					<th data-options="sortable:true,field:'exector',width:'100px'">操作人</th>
					<th data-options="sortable:true,field:'insertTime',width:'100px'">创建时间</th>
					<th data-options="sortable:true,field:'insertUser',width:'100px'">创建人</th>
					<th data-options="sortable:true,field:'updateTime',width:'100px'">最后操作时间</th>
			</thead>
		</table>
	</div>
	<div id="toolbar">
		<form id="queryForm" name="queryForm" style="padding: 0px; margin: 0px;">
			<table id="tb_query">
				<tr>
					<th>物料编码：</th>
					<td>
						<input name="gcode" class="easyui-textbox" style="width: 120px">
					</td>
					<th>供应商编码：</th>
					<td>
						<input name="oraCode" class="easyui-textbox" style="width: 120px">
					</td>
					<td>
						<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="query()">查询</a>
					</td>
				</tr>
			</table>
			<div id="butDiv" style="border-top: 1px dashed #0066CC;"></div>
		</form>
		<div id="butbar"></div>
	</div>
	<!-- 保存或修改对话框 -->
	<div id="dlg" class="easyui-dialog" style="width: 600px" data-options="closed:true,modal:true,buttons:'#dlg-buttons'">
		<form id="fm" method="post">
			<input type="hidden" name="id" id="id" />
			<input type="hidden" name="number" id="number" />
			<input type="hidden" name="gType" id="gType" />
			<input type="hidden" name="gCode" id="gCode" />
			<input type="hidden" name="gName" id="gName" />
			<input type="hidden" name="oraName" id="oraName" />
			<input type="hidden" name="state" />
			<div class="fitem" id="">
				<label>零件：</label>
				<input class="easyui-combobox" name="wmsgoods" id="wmsgoods" style="width: 145px;"
					data-options="valueField:'valueStr',textField:'valueStr',url:'${app}/goods/getWmsGoodsByKey.action',editable:true,mode:'remote',panelHeight:'auto',missingMessage:'零件不能为空'" />

				<label>供应商名称：</label>
				<input class="easyui-combobox" name="oraCode" id="oraCode" style="width: 145px;"
					data-options="valueField:'companyid',textField:'companyid',url:'${app}/conpany/getComboboxByConCode.action',editable:true,mode:'remote',panelHeight:'auto',required:true,missingMessage:'请选择供应商。'" />
			</div>
			<div class="fitem" id="">
				<label>批次开始时间：</label>
				<input name="startTime" class="easyui-datebox" style="width: 145px;" data-options="sharedCalendar:'#cc'">
				<label>批次结束时阐：</label>
				<input name="endTime" class="easyui-datebox" style="width: 145px;" data-options="sharedCalendar:'#cc'">
			</div>
			<div class="fitem" id="">
				<label>操作类型：</label>
				<select name="type" class="easyui-combobox" style="width: 145px;" data-options="required:true,missingMessage:'操作类型不能为空。',editable:false,panelHeight:'auto'">
					<option value="锁定库存">锁定库存</option>
					<option value="解锁库存">解锁库存</option>
				</select>
			</div>
		</form>

	</div>
	<div id="cc" class="easyui-calendar"></div>
	<div id="dlg-buttons">
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="save()">保存</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	</div>

</body>

<script type="text/javascript">
	$(function() {
		//渲染按钮
		Hmgx.RenderButton("butbar",
<%=session.getAttribute("curLoginUserButton")%>
	, "${param.pageId}");
	});

	$("#oraCode").combobox({
		onSelect : function(param) {
			$("#oraName").val(param.conName);
		}
	});
	$("#wmsgoods").combobox({
		onSelect : function(param) {
			$("#gCode").val(param.gcode);
			$("#gName").val(param.gname);
			$("#gType").val(param.gtype);
			$("#oraName").val(param.oraName);
			$("#oraCode").combobox('setValue', param.oraCode);
		}
	});

	function ok() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要修改的数据！");
		if (!row) {
			return;
		}
		$.messager.confirm('执行确认', '您确定要执行该操作?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/wmsStockLockLog/sure.action', {
					id : row.id
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
	}

	//查询
	function query() {
		$("#dg").datagrid({
			url : "${app}/wmsStockLockLog/list.action",
			queryParams : Hmgx.getQueryParamet("queryForm")
		});
	}
	//新增
	function add() {
		$('#login-name').css('display', '');
		$('#dlg').dialog('open').dialog('setTitle', '新增库存调整申请单 ').window("center");
		$('#fm').form('clear');
		url = '${app}/wmsStockLockLog/save.action';
	}

	//修改
	function edit() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要修改的数据！");
		if (!row) {
			return;
		}
		$('#dlg').dialog('open').dialog('setTitle', '修改库存调整申请单 ');
		$("#wmsgoods").combobox({
			data : [ {
				gcode : row.gCode,
				gtype : row.gType,
				oraCode : row.oraCode
			} ]
		});
		row.wmsgoods = row.gCode
		$('#fm').form('load', row);
	}
	function save() {
		if (!$('#fm').form('validate')) {
			return false;
		}
		showHandleProgress();
		var gCode = $("#wmsgoods").combobox("getText");
		if (null == gCode || gCode == '') {
			$("#gCode").val(null);
			$("#gName").val(null);
			$("#gType").val(null);
		}
		$.ajax({
			type : 'POST',
			url : '${app}/wmsStockLockLog/save.action',
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
		});
	}

	//删除
	function del() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要删除的数据！");
		if (!row) {
			return;
		}
		$.messager.confirm('删除确认', '您确定要删除该记录?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/wmsStockLockLog/del.action', {
					id : row.id
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
	}
	$("#dg").datagrid({
		onLoadError : function() {
			$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
		}
	});
</script>