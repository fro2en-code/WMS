<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'north',title:'',border:false,split:true" style="padding: 2px; height: 50%;">
		<table id="totalDG" title="退货台账" class="easyui-datagrid"
			data-options="remoteSort:false,url:'',fit:true,rownumbers:true,singleSelect:true,toolbar:'#totalDGTool',pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'gcode',width:'100px'">物料代码</th>
					<th data-options="sortable:true,field:'gname',width:'100px'">物料名称</th>
					<th data-options="sortable:true,field:'gtype',width:'100px'">零件用途</th>
					<th data-options="sortable:true,field:'oraCode',width:'100px'">供应商代码</th>
					<th data-options="sortable:true,field:'oraName',width:'100px'">供应商名称</th>
					<th data-options="sortable:true,field:'whCode',width:'100px'">仓库代码</th>
					<th data-options="sortable:true,field:'whName',width:'100px'">仓库名称</th>
					<th data-options="sortable:true,field:'totalNum',width:'80px'">库存数量</th>
				</tr>
			</thead>
		</table>

		<div id="totalDGTool">
			<div title="" class="easyui-panel">
				<form id="cx" name="cx" style="padding: 0px; margin: 0px;">
					<table id="tb_query">
						<tr>
							<th>物料代码：</th>
							<td><input name="gcode" class="easyui-textbox"></td>
							<th>供应商代码：</th>
							<td><input name="oraCode" class="easyui-textbox"></td>
							<td><a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="query()">查询</a></td>
						</tr>
					</table>
				</form>
			</div>
			<div id="butbar">
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-back',plain:false" onclick="salesReturn()">退货</a> <a href="javascript:;"
					class="easyui-linkbutton" data-options="iconCls:'icon-back',plain:false" onclick="again()">二次入库</a> <a href="javascript:;" class="easyui-linkbutton"
					data-options="iconCls:'icon-back',plain:false" onclick="supplierSalesReturn()">供应商退货</a>
			</div>
		</div>
	</div>

	<div data-options="region:'center',title:'',border:false" style="padding: 2px;">

		<table id="dg" title="退货台账明细 " data-options="remoteSort:false,url:'',fit:true,rownumbers:true,singleSelect:true,toolbar:'#toolbar',pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'dealCode',width:'150px'">单据号</th>
					<th data-options="sortable:true,field:'salesReturnType',width:'150px',formatter:forma_salesReturnType">退货单据类型</th>
					<th data-options="sortable:true,field:'gcode',width:'100px'">物料代码</th>
					<th data-options="sortable:true,field:'gname',width:'100px'">物料名称</th>
					<th data-options="sortable:true,field:'gtype',width:'100px'">零件用途</th>
					<th data-options="sortable:true,field:'batchCode',width:'100px'">批次号</th>
					<th data-options="sortable:true,field:'oraCode',width:'100px'">供应商代码</th>
					<th data-options="sortable:true,field:'oraName',width:'100px'">供应商名称</th>
					<th data-options="field:'accountType',width:'100px',formatter:forma_accountType">台账类型</th>
					<th data-options="field:'dealType',width:'100px',formatter:forma_dealType">流水类型</th>
					<th data-options="sortable:true,field:'dealNum',width:'80px'">流水数量</th>
					<th data-options="sortable:true,field:'inTime',width:'150px'">入库时间</th>
					<th data-options="sortable:true,field:'totalNum',width:'80px'">结余</th>
					<th data-options="sortable:true,field:'dealTime',width:'150px'">台账时间</th>
					<th data-options="sortable:true,field:'remark',width:'200px'">备注</th>
					<th data-options="sortable:true,field:'whCode',width:'100px'">仓库代码</th>
					<th data-options="sortable:true,field:'whName',width:'100px'">仓库名称</th>
				</tr>
			</thead>
		</table>
	</div>
	<!-- 退货 -->
	<div id="account" class="easyui-dialog" style="width: 600px" data-options="closed:true,modal:true,buttons:'#account-buttons'">
		<form id="fm" method="post">
			<input type="hidden" name="gtype" id="gtype" /> <input type="hidden" name="gcode" id="gcode" /> <input type="hidden" name="oraCode" id="oraCode" />
			<div class="fitem" id="">
				<label>零件号 ：</label><input class="easyui-combobox" name="gcodes" id="gcodes" style="width: 145px;"
					data-options="valueField:'valueStr',textField:'valueStr',url:'${app}/goods/getWmsGoodsByKey.action',editable:false,panelHeight:'auto',required:true,missingMessage:'零件不能为空'" />
				<label>批次号 ：</label><input name="batchCode" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'批次号不能为空。'">
			</div>
			<div class="fitem">
				<label>退货单号 ：</label><input name="dealCode" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'对应单号不能为空。'"> <label>数量
					：</label><input name="dealNum" class="easyui-textbox" style="width: 145px;" data-options="required:true,missingMessage:'数量不能为空。'">
			</div>
			<div class="fitem">
				<label>退货类型 ：</label><select name="salesReturnType" class="easyui-combobox" style="width: 145px;"
					data-options="required:true,editable:false,missingMessage:'发货状态不能为空。'">
					<option value="0">合格品退货</option>
					<option value="1">不合格品退货</option>
				</select> <label>备注 ：</label><input name="remark" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'备注不能为空。'">
			</div>
		</form>
	</div>
	<div id="account-buttons">
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="save()">确定</a> <a href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-cancel'" onclick="javascript:$('#account').dialog('close')">取消</a>
	</div>
	<!-- 二次入库 -->
	<div id="again" class="easyui-dialog" style="width: 600px" data-options="closed:true,modal:true,buttons:'#again-buttons'">
		<form id="fms" method="post">
			<div class="fitem" id="">
				<label>入库数量 ：</label><input type="text" name="dealNum" class="easyui-textbox" style="width: 145px;" data-options="required:true,missingMessage:'入库数量不能为空。'">
				<label>入库库位：</label> <input class="easyui-combobox" name="storageCode" id="storageCode" style="width: 145px;"
					data-options="valueField:'storagecode',textField:'storagecode',url:'${app}/storag/getComboBoxStorageCode.action',editable:false,panelHeight:'auto',required:true,missingMessage:'入库库位不能为空。'" />
			</div>
			<div class="fitem" id="">
				<label>二次入库单号 ：</label><input type="text" name="dealCode" class="easyui-textbox" style="width: 145px;"
					data-options="required:false,missingMessage:'入库单号不能为空。'">
			</div>
			<input type="hidden" name="storagecode" id="storagecode" />
		</form>
	</div>
	<div id="again-buttons">
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="complete()">确定</a> <a href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-cancel'" onclick="javascript:$('#again').dialog('close')">取消</a>
	</div>
	<!-- 供应商退货 -->
	<div id="supplierSalesReturn" class="easyui-dialog" style="width: 600px" data-options="closed:true,modal:true,buttons:'#ssr-buttons'">
		<form id="ssr" method="post">
			<div class="fitem" id="">
				<label>退货数量 ：</label><input type="text" name="dealNum" class="easyui-textbox" style="width: 145px;" data-options="required:true,missingMessage:'退货数量不能为空。'">
				<label>退货单号 ：</label><input type="text" name="dealCode" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'退货不能为空。'">
			</div>
		</form>
	</div>
	<div id="ssr-buttons">
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="ssr()">确定</a> <a href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-cancel'" onclick="javascript:$('#supplierSalesReturn').dialog('close')">取消</a>
	</div>

</body>

<script type="text/javascript">
	$(function() {
		//渲染按钮
		Hmgx.RenderButton("butbar",
<%=session.getAttribute("curLoginUserButton")%>
	,
				"${param.pageId}");

		//主表
		$("#totalDG").datagrid({
			onSelect : function(index, row) {
				$("#dg").datagrid({
					url : "${app}/salesReturnAccountList/list.action",
					queryParams : {
						oraCode : row.oraCode,
						gcode : row.gcode,
						gtype : row.gtype,
						whCode : row.whCode
					}
				});
			}
		});
		//子表
		$("#dg").datagrid({
			rowStyler : function(index, row) {
				if (row.accountType == 0) {
					return 'background-color:#DFFFDF;color:#000000;';
				} else if (row.accountType == 1) {
					return 'background-color:#FFE6D9;color:#000000;';
				}
			}
		});
	});

	//查询
	function query() {
		$("#totalDG").datagrid({
			url : "${app}/salesReturnAccount/list.action",
			queryParams : Hmgx.getQueryParamet("cx")
		});
	}

	//退货
	function salesReturn() {
		$('#login-name').css('display', '');
		$('#account').dialog('open').dialog('setTitle', '请填写退货信息').window(
				"center");
		$('#fm').form('clear');
		url = '${app}/salesReturnAccount/salesRrturn.action';

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
					$('#account').dialog('close');
					$('#totalDG').datagrid('reload');
				}
			}
		});
	}

	//二次入库
	function again() {
		var row = Hmgx.GetSelectRow("totalDG", "请选择需要入库的数据！");
		if (!row) {
			return;
		}
		$('#login-name').css('display', '');
		$('#again').dialog('open').dialog('setTitle', '请填写入库数量').window(
				"center");
		$('#fms').form('clear');
		url = '${app}/salesReturnAccount/again.action?id=' + row.id;
	}
	function complete() {
		if (!$('#fms').form('validate')) {
			return false;
		}
		showHandleProgress();
		$.ajax({
			type : 'POST',
			url : url,
			data : $('#fms').serialize(),
			success : function(result) {
				hideProgress();
				if (!result.suc) {
					$.messager.alert('错误信息', result.retmsg, 'error');
				} else {
					$('#again').dialog('close');
					$('#totalDG').datagrid('reload');
				}
			}
		});
	}

	//供应商退货
	function supplierSalesReturn() {
		var row = Hmgx.GetSelectRow("totalDG", "请选择需要退货的数据！");
		if (!row) {
			return;
		}
		$('#login-name').css('display', '');
		$('#supplierSalesReturn').dialog('open').dialog('setTitle', '请填写退货数量')
				.window("center");
		$('#ssr').form('clear');
		url = '${app}/salesReturnAccount/supplierSalesReturn.action?id='
				+ row.id;
	}
	function ssr() {
		if (!$('#ssr').form('validate')) {
			return false;
		}
		showHandleProgress();
		$.ajax({
			type : 'POST',
			url : url,
			data : $('#ssr').serialize(),
			success : function(result) {
				hideProgress();
				if (!result.suc) {
					$.messager.alert('错误信息', result.retmsg, 'error');
				} else {
					$('#supplierSalesReturn').dialog('close');
					$('#totalDG').datagrid('reload');
				}
			}
		});
	}

	$("#gcodes").combobox({
		onSelect : function(param) {
			$("#gcode").val(param.gcode);
			$("#gtype").val(param.gtype);
			$("#oraCode").val(param.oraCode);
		},
		mode : "remote",
		editable : "true"
	});
	//状态： 台账类型 0 出 1 入
	function forma_accountType(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 0:
			return "出";
			break;
		case 1:
			return "入";
			break;
		case 2:
			return "二次入库";
			break;
		case 3:
			return "供应商退货";
			break;
		default:
			return "未知";
			break;
		}
	}
	//流水类型 0 出库 1 入库 2 红冲
	function forma_dealType(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 0:
			return "出库";
			break;
		case 1:
			return "收货";
			break;
		case 2:
			return "红冲";
			break;
		case 3:
			return "初始化";
			break;
		case 4:
			return "退货";
			break;
		default:
			return "未知";
			break;
		}
	}

	function forma_salesReturnType(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 0:
			return "合格品退货";
			break;
		case 1:
			return "不合格品退货";
			break;
		case 2:
			return "二次入库";
			break;
		case 3:
			return "供应商退货";
			break;
		default:
			return "未知";
			break;
		}
	}
	//库位
	$("#storageCode").combobox({
		onSelect : function(Storage) {
			$("#storageCode").val(Storage.storagecode);
		},
		mode : "remote",
		editable : "true"
	});

	$("#dg").datagrid({
		onLoadError : function() {
			$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
		}
	});
	$("#totalDG").datagrid({
		onLoadError : function() {
			$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
		}
	});
</script>