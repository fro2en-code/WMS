<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">
	<div data-options="region:'center',title:'',border:false" style="padding: 5px;">
		<table id="dg" title="库存调整申请单 " class="easyui-datagrid"
			data-options="remoteSort:false,url:'${app}/adj/list.action',fit:true,rownumbers:true,singleSelect:true,toolbar:'#toolbar',pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'adjCode',width:'125px'">调库单编码</th>
					<th data-options="sortable:true,field:'whCode',width:'125px'">仓库代码</th>
					<th data-options="sortable:true,field:'adjTime',width:'150px'">调库时间</th>
					<th data-options="sortable:true,field:'gcode',width:'125px'">物料编码</th>
					<th data-options="sortable:true,field:'gtype',width:'125px'">零件用途</th>
					<th data-options="sortable:true,field:'oraCode',width:'125px'">供应商代码</th>
					<th data-options="sortable:true,field:'storageCode',width:'100px'">调整库位</th>
					<th data-options="sortable:true,field:'adjNum',width:'100px'">调整数量</th>
					<th data-options="sortable:true,field:'usedNum',width:'100px'">库存数量</th>
					<th data-options="sortable:true,field:'goalNum',width:'100px'">目标数量</th>
					<th data-options="sortable:true,field:'type',width:'150px',formatter:forma_type">操作类型</th>
					<th data-options="sortable:true,field:'adjReason',width:'300px'">调库原因</th>
					<th data-options="sortable:true,field:'statu',width:'100px',formatter:format_Statu">执行状态</th>
					<th data-options="sortable:true,field:'updateUser',width:'80px'">最后更新人</th>
					<th data-options="sortable:true,field:'updateTime',width:'150px'">最后更新时间</th>
				</tr>
			</thead>
		</table>
	</div>
	<div id="toolbar">
		<form id="queryForm" name="queryForm" style="padding: 0px; margin: 0px;">
			<table id="tb_query">
				<tr>
					<th>调库单编码：</th>
					<td><input name="adjCode" id="adjCode" class="easyui-textbox" style="width: 120px"></td>
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
				<input type="hidden" name="gtype" id="gtype" /> <input type="hidden" name="gcode" id="gcode" /> <input type="hidden" name="oraCode" id="oraCode" /> <label>零件：</label>
				<input class="easyui-combobox" name="wmsgoods" id="wmsgoods" style="width: 145px;"
					data-options="valueField:'valueStr',textField:'valueStr',url:'${app}/goods/getWmsGoodsByKey.action',editable:false,panelHeight:'auto',required:true,missingMessage:'零件不能为空'" />
				<label>调整库位：</label> <input class="easyui-combobox" name="storageCode" id="storageCode" style="width: 145px;"
					data-options="valueField:'storagecode',textField:'storagecode',url:'${app}/storag/getComboBoxStorageCode.action',editable:false,panelHeight:'auto',required:true,missingMessage:'调整库位不能为空。'" />
			</div>
			<div class="fitem" id="">
				<label>调整数量：</label> <input name="adjNum" class="easyui-numberbox" style="width: 145px;" data-options="required:true,missingMessage:'批次不能为空。'"> <label>调库原因：</label>
				<input name="adjReason" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'调库原因不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>操作类型：</label> <select name="type" class="easyui-combobox" style="width: 145px;"
					data-options="required:true,missingMessage:'是否操作锁定库存不能为空。',editable:false,panelHeight:'auto'">
					<option value="0">新增库存</option>
					<option value="1">减少库存</option>
					<option value="2">锁定库存</option>
					<option value="3">解锁库存</option>
					<option value="4">转生产件</option>
					<option value="5">转出口件</option>
					<option value="6">转备件</option>
			</div>
			<input type="hidden" name="adjCode" id="adjCodes" /> <input type="hidden" name="statu" id="status" /> <input type="hidden" name="storagecode"
				id="storagecode" />
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="save()">保存</a> <a href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-cancel'" onclick="javascript:$('#dlg').dialog('close')">取消</a>
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
	//库位
	$("#storageCode").combobox({
		onSelect : function(Storage) {
			$("#storageCode").val(Storage.storagecode);
		},
		mode : "remote",
		editable : "true"
	});

	//状态   0未执行   1已执行
	function format_Statu(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 0:
			return "未执行 ";
			break;
		case 2:
			return "已执行";
			break;
		default:
			return "未知";
			break;
		}
	}

	//查询

	function query() {
		var adjCode = $.trim($('input[name=adjCode]').val());

		$("#dg").datagrid('reload', {
			adjCode : adjCode,
		});
	}

	var url;
	//新增
	function add() {
		$('#login-name').css('display', '');
		$('#dlg').dialog('open').dialog('setTitle', '新增库存调整申请单 ').window(
				"center");
		$('#fm').form('clear');
		url = '${app}/adj/save.action';
	}

	//修改
	function edit() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要修改的数据！");
		if (!row) {
			return;
		}
		$("#adjCodes").val(row.adjCode);
		$("#status").val(row.statu);

		$('#dlg').dialog('open').dialog('setTitle', '修改库存调整申请单 ');
		$("#wmsgoods").combobox({
			data : [ {
				gcode : row.gcode,
				gtype : row.gtype,
				oraCode : row.oraCode
			} ]
		});
		row.wmsgoods = row.gcode
		$('#fm').form('load', row);
		url = '${app}/adj/save.action?id=' + row.id;
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
				$.post('${app}/adj/del.action', {
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
	//确认调库申请单,重新更改库存
	function adjStock() {

		var row = Hmgx.GetSelectRow("dg", "请选择需要确认的数据！");
		if (!row) {
			return;
		}

		showHandleProgress();
		$.post('${app}/adj/update.action', {
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

	//格式化日期时间
	function dateTime_matter(date) {
		var y = date.getFullYear();
		var m = date.getMonth() + 1;
		var d = date.getDate();
		var h = date.getHours();
		return y + '-' + (m < 10 ? ('0' + m) : m) + '-'
				+ (d < 10 ? ('0' + d) : d) + " " + (h < 10 ? '0' + h : h) + ":"
				+ date.getMinutes();
	}
	$("#wmsgoods").combobox({
		onSelect : function(param) {
			//$("#oraCode").val(param.oraCode);
			$("#gcode").val(param.gcode);
			$("#gtype").val(param.gtype);
			$("#oraCode").val(param.oraCode);
		},
		mode : "remote",
		editable : "true"
	});

	//是否操作锁定库存
	function forma_type(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 0:
			return "新增库存";
			break;
		case 1:
			return "减少库存";
			break;
		case 2:
			return "锁定库存";
			break;
		case 3:
			return "解锁库存";
			break;
		case 4:
			return "转生产件";
			break;
		case 5:
			return "转出口件";
			break;
		case 6:
			return "转备件";
			break;
		default:
			return "未知";
			break;
		}
	}
	$("#dg").datagrid({
		onLoadError : function() {
			$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
		}
	});
</script>