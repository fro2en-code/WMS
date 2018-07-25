<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<!-- 初始化主表 -->
	<div data-options="region:'north',title:'',border:false,split:true"
		style="padding: 2px 2px 0px 2px; height: 50%">
		<table id="dg" title="任务列表"
			data-options="remoteSort:false,url:'${app}/task/list.action',fit:true,rownumbers:true,singleSelect:true,toolbar:'#toolbar',pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'ck',checkbox:true"></th>
					<th data-options="sortable:true,field:'taskdesc',width:'200px'">任务名称</th>
					<th data-options="sortable:true,field:'whCode',width:'100px'">仓库代码</th>
					<th data-options="sortable:true,field:'billNumber',width:'150px'">对应单据编码</th>
					<th
						data-options="sortable:true,field:'level',width:'80px',formatter:format_Level">任务优先级</th>
					<th
						data-options="sortable:true,field:'status',width:'80px',formatter:format_Status">状态</th>
					<th
						data-options="sortable:true,field:'type',width:'80px',formatter:format_Type">任务类型</th>
					<th data-options="sortable:true,field:'beginTime',width:'150px'">发起时间</th>
					<th data-options="sortable:true,field:'acceptTime',width:'150px'">任务接收时间</th>
					<th data-options="sortable:true,field:'endTime',width:'150px'">任务结束时间</th>
					<th data-options="sortable:true,field:'executorName',width:'150px'">任务执行人</th>
					<th data-options="sortable:true,field:'storageCode',width:'150px'">当前库位</th>
					<th
						data-options="sortable:true,field:'nextStorageCode',width:'150px'">目标库位</th>
				</tr>
			</thead>
		</table>
	</div>

	<div data-options="region:'center',title:'',border:false,minHeight:50"
		style="padding: 0px 2px 2px 2px;">
		<table id="dgSub" title="任务明细" class="easyui-datagrid"
			data-options="remoteSort:false,url:'',fit:true,rownumbers:true,singleSelect:true,pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'gcode',width:'150px'">物料编码</th>
					<th data-options="sortable:true,field:'gname',width:'150px'">物料名称</th>
					<th data-options="sortable:true,field:'oraCode',width:'150px'">供应商编码</th>
					<th data-options="sortable:true,field:'gtype',width:'150px'">零件用途</th>
					<th data-options="sortable:true,field:'goodNeedNum',width:'150px'">物料需求数量</th>
					<th
						data-options="sortable:true,field:'goodRealNum',width:'150px',editor:'numberbox'">物料实际数量</th>
				</tr>
			</thead>
		</table>
	</div>
	<div id="dlg" class="easyui-dialog" style="width: 950px; height: 500px"
		data-options="closed:true,modal:true,buttons:'#dlg-buttons'">
		<table id="fm" title="请填写物料实际数量" class="easyui-datagrid"
			data-options="onClickRow: onClickRow,remoteSort:false,url:'',fit:true,rownumbers:true,singleSelect:true,pagination:false,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'gcode',width:'150px' ">物料编码</th>
					<th data-options="sortable:true,field:'gname',width:'150px'">物料名称</th>
					<th data-options="sortable:true,field:'oraCode',width:'150px'">供应商编码</th>
					<th data-options="sortable:true,field:'gtype',width:'150px'">零件用途</th>
					<th data-options="sortable:true,field:'goodNeedNum',width:'150px'">物料需求数量</th>
					<th
						data-options="sortable:true,field:'goodRealNum',width:'150px',editor:'numberbox'">物料实际数量</th>
				</tr>
			</thead>
		</table>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-ok'" onclick="saveList()">保存</a> <a
			href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-cancel'"
			onclick="javascript:$('#dlg').dialog('close')">取消</a>
	</div>
	<!-- 工具栏按钮 -->
	<div id="toolbar">
		<form id="queryForm" name="queryForm"
			style="padding: 0px; margin: 0px;">
			<table id="tb_query">
				<tr>
					<th>单据编码：</th>
					<td><input name="billNumber" id="billNumber"
						class="easyui-textbox" style="width: 120px"></td>
					<th>任务状态：</th>
					<td><select name="status" id="status" class="easyui-combobox"
						style="width: 145px;"
						data-options="required:false,editable:false,panelHeight:'auto',panelMinHeight:'10px'">
							<option></option>
							<option value="0">新任务</option>
							<option value="1">接收</option>
							<option value="2">完成</option>
							<option value="3">取消</option>
					</select></td>
					<td><a href="javascript:;" class="easyui-linkbutton"
						data-options="iconCls:'icon-search',plain:true" onclick="query()">查询</a>
					</td>
				</tr>
			</table>
			<div id="butDiv" style="border-top: 1px dashed #0066CC;"></div>
		</form>
		<div id="butbar"></div>
	</div>
</body>

<script type="text/javascript">
	$(function() {
		//渲染按钮
		Hmgx.RenderButton("butbar",
<%=session.getAttribute("curLoginUserButton")%>
	,
				"${param.pageId}");
		$("#dg").datagrid({
			onSelect : function(index, row) {
				$("#dgSub").datagrid({
					url : "${app}/task/listSub.action",
					queryParams : {
						id : row.id
					}
				});
			},
			rowStyler : function(index, row) {
				if (row.status == 1) {
					return 'background-color:#6293BB;color:#fff;';
				}
			}
		});

	});

	var editIndex = undefined;
	function endEditing() {
		if (editIndex == undefined) {
			return true
		}
		//校验指定的行，如果有效返回true  
		if ($('#fm').datagrid('validateRow', editIndex)) {
			$('#fm').datagrid('endEdit', editIndex); //结束编辑  
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}
	function onClickRow(index) {
		if (editIndex != index) {
			if (endEditing()) {
				$('#fm').datagrid('selectRow', index).datagrid('beginEdit',
						index); //开始启用编辑  
				editIndex = index; //将正在编辑的行号赋值给变量  
			} else {
				$('#fm').datagrid('selectRow', editIndex);
			}
		}
	}
	function saveList() {
		showHandleProgress();
		if (endEditing()) {
			$.ajax({
				url : '${app}/task/complete.action',
				type : 'post',
				async : true,
				dataType : 'json',
				data : JSON.stringify($('#fm').datagrid('getRows')),
				contentType : 'application/json',
				error : function() {
					alert("任务完成失败!!!");
				},
				success : function(result) {

					if ('0' == result.retcode) {

					} else {
						hideProgress();
						$.messager.alert('错误信息', result.retmsg, 'error');
						return;
					}
					hideProgress();
					$('#dlg').dialog('close');
					$('#dg').datagrid('reload');
				}
			}).error(function() {
				hideProgress();
				$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
			});

		}
		editIndex = undefined;

	}

	function format_Level(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 0:
			return "普通";
			break;
		case 1:
			return "加急";
			break;
		default:
			return "未知";
			break;
		}
	}
	function format_Status(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 0:
			return "新任务";
			break;
		case 1:
			return "接收";
			break;
		case 2:
			return "完成";
			break;
		case 3:
			return "取消";
			break;
		default:
			return "未知";
			break;
		}
	}
	function format_Type(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 1:
			return "手工收货";
			break;
		case 2:
			return "LES收货";
			break;
		case 3:
			return "收货搬运";
			break;
		case 4:
			return "上架";
			break;
		case 5:
			return "预拣货";
			break;
		case 6:
			return "手工发货";
			break;
		case 7:
			return "LES发货";
			break;
		case 8:
			return "移库";
			break;
		case 9:
			return "盘点";
			break;
		case 10:
			return "发货区发货";
			break;
		default:
			return "未知";
			break;
		}
	}

	function cancelTask() {
		operate("${app}/task/cancelTask.action");
	}
	function cancelAndAdd() {
		operate("${app}/task/cancelAndAddTask.action");
	}
	//任务完成
	function completeTask() {
		//operate("${app}/task/completeTask.action");
		var row = Hmgx.GetSelectRow("dg", "请选择需要修改的数据！");
		if (!row) {
			return;
		}
		$('#dlg').dialog('open').dialog('setTitle', '修改任务明细单据实际数量').window(
				"center");
		$("#fm").datagrid({
			url : "${app}/task/getWmsTaskBillList.action",
			queryParams : {
				id : row.id
			}
		});
		if (row.type == 1 || row.type == 2) {
			($("#fm").datagrid("options").columns)[0][3].editor = {
				type : 'combobox',
				options : {
					valueField : 'id',
					textField : 'projectName',
					data : [ {
						id : '生产件',
						projectName : '生产件',
						'selected' : 'true'
					}, {
						id : '出口件',
						projectName : '出口件'
					}, {
						id : '备件',
						projectName : '备件'
					} ],
					panelHeight : 'auto',
					required : true
				}
			};
		} else {
			($("#fm").datagrid("options").columns)[0][3].editor = null;
		}
	}
	function receiveTask() {
		operate("${app}/task/receiveTask.action");
	}
	function nextTask() {
		operate("${app}/task/nextTask.action");
	}

	function operate(url) {
		var row = Hmgx.GetSelectRow("dg", "请选择需要完成的任务！");
		if (!row) {
			return;
		}
		$.messager.confirm('取消确认', '您确定要完成指定任务?', function(r) {
			if (r) {
				showHandleProgress();
				$.post(url, {
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
	//查询
	function query() {
		$("#dg").datagrid({
			url : "${app}/task/list.action",
			queryParams : Hmgx.getQueryParamet("queryForm")
		});
	}
	$("#dg").datagrid({
		onLoadError : function() {
			$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
		}
	});
	$("#dgSub").datagrid({
		onLoadError : function() {
			$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
		}
	});
</script>