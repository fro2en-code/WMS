<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">
	<div id="toolbar"></div>
	<div data-options="region:'north',title:'',border:false,split:true" style="padding: 2px; height: 50%;">
		<table id="dg" title="供应商发货单据" class="easyui-datagrid"
			data-options="remoteSort:false,url:'',fit:true,rownumbers:true,singleSelect:true,toolbar:'#totalDGTool',pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'mapSheetNo',width:'125px',sortable:true">对应配送单号</th>
					<th data-options="sortable:true,field:'isEmerge',width:'150px',formatter:format_isEmerge">是否紧急需求</th>
					<th data-options="sortable:true,field:'status',width:'100px',formatter:forma_lesType">发货状态</th>
					<th data-options="sortable:true,field:'supplNo',width:'100px'">供应商代码</th>
					<th data-options="sortable:true,field:'whCode',width:'100px'">仓库代码</th>
					<th data-options="sortable:true,field:'updateUser',width:'80px'">创建人</th>
					<th data-options="sortable:true,field:'updateTime',width:'150px'">创建时间</th>
				</tr>
			</thead>
		</table>

		<div id="totalDGTool">
			<div title="" class="easyui-panel">
				<form id="cx" name="cx" style="padding: 0px; margin: 0px;">
					<table id="tb_query">
						<tr>
							<th>对应配送单号：</th>
							<td><input name="mapSheetNo" id="mapSheetNo" class="easyui-textbox"></td>
							<td><a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="query()">查询</a></td>
							<td><a href="javascript:;" class="easyui-linkbutton" onclick="complete()">发货完成</a></td>
						</tr>
					</table>
					<div id="butDiv" style="border-top: 1px dashed #0066CC;"></div>
				</form>
				<div id="butbar"></div>
			</div>
		</div>
	</div>

	<div data-options="region:'center',title:'',border:false" style="padding: 2px;">

		<table id="totalDG" title="供应商发货单据明细 "
			data-options="remoteSort:false,url:'',fit:true,rownumbers:true,singleSelect:true,toolbar:'#toolbarSub',pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'mapSheetNo',width:'125px',sortable:true">对应配送单号</th>
					<th data-options="sortable:true,field:'partNo',width:'100px'">零件号</th>
					<th data-options="sortable:true,field:'supplNo',width:'100px'">供应商编码</th>
					<th data-options="sortable:true,field:'sxCardNo',width:'100px'">随箱卡号</th>
					<th data-options="sortable:true,field:'reqQty',width:'100px'">需求数量</th>
					<th data-options="sortable:true,field:'sendQty',width:'100px'">实发数量</th>
					<th data-options="sortable:true,field:'updateUser',width:'80px'">最后更新人</th>
					<th data-options="sortable:true,field:'updateTime',width:'150px'">最后更新时间</th>
				</tr>
			</thead>
		</table>
	</div>

	<!-- 明细表工具栏按钮 -->
	<div id="totalDG"></div>
	<div id="toolbarSub">
		<a href="javascript:;" class="easyui-linkbutton" onclick="addList()">新增</a> <a href="javascript:;" class="easyui-linkbutton" onclick="updateList()">修改</a> <a
			href="javascript:;" class="easyui-linkbutton" onclick="deleteList()">删除</a>
	</div>
	<!-- 保存或修改对话框 -->
	<div id="dlg" class="easyui-dialog" style="width: 600px" data-options="closed:true,modal:true,buttons:'#dlg-buttons'">
		<form id="fm" method="post">
			<input type="hidden" name="status" id="status" />
			<div class="fitem" id="">
				<label>对应配送单号：</label> <input name="mapSheetNo" class="easyui-textbox" style="width: 145px;" data-options="required:true,missingMessage:'配送单号不能为空,并且不能修改。'">
				<label>是否紧急需求：</label> <select name="isEmerge" class="easyui-combobox" style="width: 145px;"
					data-options="required:true,editable:false,missingMessage:'紧急需求不能为空。'">
					<option value="0">正常</option>
					<option value="1">紧急</option>
				</select>
			</div>
			<input type="hidden" name="status" value="0" />
	</div>
	</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="save()">保存</a> <a href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-cancel'" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	</div>
	<!-- 明细表的保存,修改对话框 -->
	<div id="dlgList" class="easyui-dialog" style="width: 600px" data-options="closed:true,modal:true,buttons:'#dlgList-buttons'">
		<form id="fmList" method="post">
			<input type="hidden" name="mapSheetNo" id="mapSheetNoList" /> <input type="hidden" name="partNo" id="partNo" /> <input type="hidden" name="supplNo"
				id="supplNo" />
			<div class="fitem" id="">
				<label>零件号：</label> <input class="easyui-combobox" name="wmsgoods" id="wmsgoods" style="width: 145px;"
					data-options="valueField:'valueStr',textField:'valueStr',url:'${app}/goods/getWmsGoodsInfoByKey.action',editable:false,panelHeight:'auto',required:true,missingMessage:'零件不能为空'" />
				<label>随箱卡号：</label> <input name="sxCardNo" class="easyui-numberbox" style="width: 145px;" data-options="required:false,missingMessage:'随箱卡号不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>需求数量：</label> <input name="reqQty" class="easyui-numberbox" style="width: 145px;" data-options="required:true,missingMessage:'需求数量不能为空。'">
			</div>
			<input type="hidden" name="status" value="0" />
		</form>
	</div>

	<div id="dlgList-buttons">
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="saveList()">保存</a> <a href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-cancel'" onclick="javascript:$('#dlgList').dialog('close')">取消</a>
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
		$("#dg").datagrid({
			url : "${app}/supplierSend/list.action",
			onSelect : function(index, row) {
				$("#totalDG").datagrid({
					url : "${app}/supplierSendList/list.action",
					queryParams : {
						mapSheetNo : row.mapSheetNo
					}
				});
			}
		});

		//子表
		$("#totalDG").datagrid({
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
		var mapSheetNo = $.trim($("#mapSheetNo").val());
		$("#dg").datagrid({
			url : "${app}/supplierSend/list.action",
			queryParams : Hmgx.getQueryParamet("cx")
		});
	}

	//发货完成
	function complete() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要修改的数据！");
		if (!row) {
			return;
		}
		$.messager.confirm('发货确认', '确认要进行发货操作?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/supplierSend/complete.action', {
					id : row.id
				}, function(result) {
					hideProgress();
					if ('-1' == result.retcode) {
						$.messager.alert('错误信息', result.retmsg, 'error');
					} else {
						$('#dg').datagrid('reload');//刷新主表
						$("#dgSub").datagrid('loadData', {
							total : 0,
							rows : []
						});//清空子表
					}
				}, "json").error(function() {
					hideProgress();
					$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
				});
			}
		});
	}

	//新增
	function add() {
		$('#login-name').css('display', '');
		$('#dlg').dialog('open').dialog('setTitle', '新增发货单').window("center");
		$('#fm').form('clear');
		url = '${app}/supplierSend/save.action';
	}
	//修改
	function edit() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要修改的数据！");
		if (!row) {
			return;
		}
		$("#status").val(row.status);
		$('#dlg').dialog('open').dialog('setTitle', '修改发货单');
		$('#fm').form('load', row);
		url = '${app}/supplierSend/save.action?id=' + row.id;
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
		$.messager.confirm('删除确认', '您确定要删除该发货单?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/supplierSend/delete.action', {
					id : row.id
				}, function(result) {
					hideProgress();
					if ('-1' == result.retcode) {
						$.messager.alert('错误信息', result.retmsg, 'error');
					} else {
						$('#dg').datagrid('reload');//刷新主表
						$("#dgSub").datagrid('loadData', {
							total : 0,
							rows : []
						});//清空子表
					}
				}, "json").error(function() {
					hideProgress();
					$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
				});
			}
		});
	}
	//新增明细表
	function addList() {
		var row = Hmgx.GetSelectRow("dg", "请选择供应商发货单！");
		if (!row) {
			return;
		}
		$('#login-name').css('display', '');
		$('#dlgList').dialog('open').dialog('setTitle', '新增发货单明细').window(
				"center");
		$('#fmList').form('clear');
		url = '${app}/supplierSendList/save.action';
	}

	//修改明细表
	function updateList() {
		var row = Hmgx.GetSelectRow("totalDG", "请选择需要修改的数据！");
		if (!row) {
			return;
		}
		$('#dlgList').dialog('open').dialog('setTitle', '修改发货单');
		$("#wmsgoods").combobox({
			data : [ {
				partNo : row.partNo
			} ]
		});
		row.wmsgoods = row.partNo
		$('#fmList').form('load', row);
		url = '${app}/supplierSendList/save.action?id=' + row.id;
	}

	//保存明细表
	function saveList() {
		var row = Hmgx.GetSelectRow("dg", "请选择供应商发货单！");
		$("#mapSheetNoList").val(row.mapSheetNo);
		if (!$('#fmList').form('validate')) {
			return false;
		}
		showHandleProgress();
		$.ajax({
			type : 'POST',
			url : url,
			data : $('#fmList').serialize(),

			success : function(result) {
				hideProgress();
				if (!result.suc) {
					$.messager.alert('错误信息', result.retmsg, 'error');
				} else {
					$('#dlgList').dialog('close');
					$('#totalDG').datagrid('reload');
				}
			}
		}).error(function() {
			hideProgress();
			$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
		});
	}

	//删除明细表
	function deleteList() {
		var row = Hmgx.GetSelectRow("totalDG", "请选择需要删除的数据！");
		if (!row) {
			return;
		}
		$.messager.confirm('删除确认', '您确定要删除该发货明细单?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/supplierSendList/del.action', {
					id : row.id
				}, function(result) {
					hideProgress();
					if ('-1' == result.retcode) {
						$.messager.alert('错误信息', result.retmsg, 'error');
					} else {
						$('#totalDG').datagrid('reload');//刷新主表
						$("#dgSub").datagrid('loadData', {
							total : 0,
							rows : []
						});//清空子表
					}
				}, "json").error(function() {
					hideProgress();
					$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
				});
			}
		});
	}

	//是否紧急   0紧急   1正常
	function format_isEmerge(val, row) {
		switch (val) {
		case 0:
			return "正常 ";
			break;
		case 1:
			return "紧急";
			break;
		default:
			return "未知";
			break;
		}
	}

	function forma_lesType(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 0:
			return "获取单据";
			break;
		case 1:
			return "任务已生成";
			break;
		case 2:
			return "发货完成";
			break;
		case 3:
			return "单据取消";
			break;
		case 4:
			return "单据回写";
			break;
		case 5:
			return "任务异常";
			break;
		default:
			return "未知";
			break;
		}
	}

	//获取物料，供应商
	$("#wmsgoods").combobox({
		onSelect : function(param) {
			//$("#oraCode").val(param.oraCode);
			$("#partNo").val(param.gcode);
			$("#supplNo").val(param.oraCode);
			;
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