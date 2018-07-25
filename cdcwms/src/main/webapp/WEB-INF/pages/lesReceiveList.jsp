<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">
	<div data-options="region:'north',title:'',border:false,split:true" style="padding: 2px; height: 50%;">
		<table id="dg" title="les收货单据" class="easyui-datagrid"
			data-options="remoteSort:false,url:'',fit:true,rownumbers:true,singleSelect:true,toolbar:'#toolbar',pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'mapSheetNo',width:'125px'">对应配送单号</th>
					<th data-options="field:'isEmerge',width:'150px',formatter:format_isEmerge">是否紧急需求</th>
					<th data-options="sortable:true,field:'lastRecRequrieTime',width:'150px'">目的地要求到货时间</th>
					<th data-options="sortable:true,field:'whCode',width:'125px'">仓库代码</th>
					<th data-options="sortable:true,field:'mriCreateTime',width:'125px'">需求创建时间</th>
					<th data-options="sortable:true,field:'lastUpdateTime',width:'150px'">最后更新时间</th>
					<th data-options="sortable:true,field:'mriStatus',width:'100px'">需求状态</th>
					<th data-options="sortable:true,field:'mriType',width:'125px'">需求类别</th>
					<th data-options="sortable:true,field:'sheetSuppRecTime',width:'150px'">供应商接收时间</th>
					<th data-options="sortable:true,field:'deliveryRec',width:'100px'">目的地代码</th>
					<th data-options="sortable:true,field:'deliveryRecType',width:'125px'">目的地类型</th>
					<th data-options="sortable:true,field:'deliverySend',width:'150px'">发运地代码</th>
					<th data-options="sortable:true,field:'pullType',width:'100px'">配送模式</th>
					<th data-options="sortable:true,field:'sheetStatus',width:'125px'">配送单状态</th>
					<th data-options="sortable:true,field:'plantNo',width:'100px'">工厂</th>
					<th data-options="field:'status',width:'100px',formatter:forma_lesType">收货状态</th>
					<th data-options="sortable:true,field:'updateUser',width:'80px'">创建人</th>
					<th data-options="sortable:true,field:'updateTime',width:'150px'">创建时间</th>
				</tr>
			</thead>
		</table>

		<div id="toolbar">
			<form id="cx" name="cx" style="padding: 0px; margin: 0px;">
				<table id="tb_query">
					<tr>
						<th>对应配送单号：</th>
						<td>
							<input name="mapSheetNo" id="mapSheetNo" class="easyui-textbox">
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
	</div>

	<div data-options="region:'center',title:'',border:false" style="padding: 2px;">

		<table id="totalDG" title="les单据明细 "
			data-options="remoteSort:false,url:'',fit:true,rownumbers:true,singleSelect:true,toolbar:'#toolbarSub',pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'mapSheetNo',width:'125px'">对应配送单号</th>
					<th data-options="sortable:true,field:'partNo',width:'100px'">零件号</th>
					<th data-options="sortable:true,field:'supplNo',width:'100px'">供应商编码</th>
					<th data-options="sortable:true,field:'sxCardNo',width:'100px'">随箱卡号</th>
					<th data-options="sortable:true,field:'sendPackageNo',width:'100px'">发运包装编号</th>
					<th data-options="sortable:true,field:'sendPackageNum',width:'100px'">发运包装容量</th>
					<th data-options="sortable:true,field:'reqPackageNum',width:'100px'">需求箱数</th>
					<th data-options="sortable:true,field:'reqQty',width:'100px'">需求数量</th>
					<th data-options="sortable:true,field:'sendQty',width:'100px'">实发数量</th>
					<th data-options="sortable:true,field:'receiveQty',width:'100px'">实收数量</th>
					<th data-options="field:'status',width:'100px',formatter:forma_lesType">收货状态</th>
					<th data-options="sortable:true,field:'updateUser',width:'80px'">创建人</th>
					<th data-options="sortable:true,field:'updateTime',width:'150px'">创建时间</th>
				</tr>
			</thead>
		</table>
	</div>

	<!-- 明细表工具栏按钮 -->
	<div id="toolbarSub">
		<a href="javascript:;" class="easyui-linkbutton" onclick="addList()">新增</a>
		<a href="javascript:;" class="easyui-linkbutton" onclick="updateList()">修改</a>
		<a href="javascript:;" class="easyui-linkbutton" onclick="deleteList()">删除</a>
	</div>
	<!-- 保存或修改对话框 -->
	<div id="dlg" class="easyui-dialog" style="width: 600px" data-options="closed:true,modal:true,buttons:'#dlg-buttons'">
		<form id="fm" method="post">
			<input type="hidden" name="status" id="status" />
			<div class="fitem" id="">
				<label>对应配送单号：</label>
				<input name="mapSheetNo" class="easyui-textbox" style="width: 145px;" data-options="required:true,missingMessage:'配送单号不能为空,不能修改'">
				<label>是否紧急需求：</label>
				<select name="isEmerge" class="easyui-combobox" style="width: 145px;" data-options="required:true,editable:false,missingMessage:'紧急需求不能为空。'">
					<option value="0">正常</option>
					<option value="1">紧急</option>
				</select>

			</div>

			<div class="fitem" id="">
				<label>目的地要求到货时间：</label>
				<input name="lastRecRequrieTime" class="easyui-datetimebox" style="width: 145px;"
					data-options="formatter:dateTime_matter,labelPosition:'top',required:false,missingMessage:'目的地要求到货时间不能为空。'">
				<label>需求创建时间：</label>
				<input name="mriCreateTime" class="easyui-datetimebox" style="width: 145px;"
					data-options="formatter:dateTime_matter,labelPosition:'top',required:false,missingMessage:'需求创建时间不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>最后更新时间：</label>
				<input name="lastUpdateTime" class="easyui-datetimebox" style="width: 145px;"
					data-options="formatter:dateTime_matter,labelPosition:'top',required:false,missingMessage:'最后更新时间不能为空。'">
				<label>需求状态：</label>
				<input name="mriStatus" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'需求状态不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>需求类别：</label>
				<input name="mriType" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'需求类别不能为空。'">
				<label>供应商接收时间：</label>
				<input name="sheetSuppRecTime" class="easyui-datetimebox" style="width: 145px;"
					data-options="formatter:dateTime_matter,labelPosition:'top',required:false,missingMessage:'供应商接收时间不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>目的地代码：</label>
				<input name="deliveryRec" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'目的地代码不能为空。'">

				<label>目的地类型：</label>
				<input name="deliveryRecType" class="easyui-numberbox" style="width: 145px;" data-options="required:false,missingMessage:'目的地类型不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>发运地代码：</label>
				<input name="deliverySend" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'发运地代码不能为空。'">

				<label>配送模式：</label>
				<input name="pullType" class="easyui-numberbox" style="width: 145px;" data-options="required:false,missingMessage:'配送模式不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>配送单状态：</label>
				<input name="sheetStatus" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'配送单状态不能为空。'">

				<label>工厂：</label>
				<input name="plantNo" class="easyui-numberbox" style="width: 145px;" data-options="required:false,missingMessage:'工厂不能为空。'">
			</div>

		</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="save()">保存</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	</div>
	<!-- 明细表的保存,修改对话框 -->
	<div id="dlgList" class="easyui-dialog" style="width: 600px" data-options="closed:true,modal:true,buttons:'#dlgList-buttons'">
		<form id="fmList" method="post">
			<input type="hidden" name="mapSheetNo" id="mapSheetNoList" />
			<input type="hidden" name="partNo" id="partNo" />
			<input type="hidden" name="supplNo" id="supplNo" />
			<div class="fitem" id="">
				<label>零件：</label>
				<input class="easyui-combobox" name="wmsgoods" id="wmsgoods" style="width: 145px;"
					data-options="valueField:'valueStr',textField:'valueStr',url:'${app}/goods/getWmsGoodsInfoByKey.action',editable:false,panelHeight:'auto',required:true,missingMessage:'零件不能为空'" />
				<label>随箱卡号：</label>
				<input name="sxCardNo" class="easyui-textbox" style="width: 145px;" data-options="required:true,missingMessage:'随箱卡号不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>发运包装编号：</label>
				<input name="sendPackageNo" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'发运包装编号不能为空。'">

				<label>发运包装容量：</label>
				<input name="sendPackageNum" class="easyui-numberbox" style="width: 145px;" data-options="required:false,missingMessage:'发运包装容量不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>需求箱数：</label>
				<input name="reqPackageNum" class="easyui-textbox" style="width: 145px;" data-options="required:true,missingMessage:'需求箱数不能为空。'">
				<label>需求数量：</label>
				<input name="reqQty" class="easyui-numberbox" style="width: 145px;" data-options="required:true,missingMessage:'需求数量不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>实发数量：</label>
				<input name=sendQty class="easyui-textbox" style="width: 145px;" data-options="required:true,missingMessage:'实发数量不能为空。'">
			</div>
		</form>
	</div>

	<div id="dlgList-buttons">
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="saveList()">保存</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="javascript:$('#dlgList').dialog('close')">取消</a>
	</div>

</body>

<script type="text/javascript">
	$(function() {
		//渲染按钮
		Hmgx.RenderButton("butbar",
<%=session.getAttribute("curLoginUserButton")%>
	, "${param.pageId}");

		//主表
		$("#dg").datagrid({
			url : "${app}/lesReceive/list.action",
			onSelect : function(index, row) {
				$("#totalDG").datagrid({
					url : "${app}/lesReceiveList/list.action",
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

	//添加任务
	function addTask() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要修改的数据！");
		if (!row) {
			return;
		}
		showHandleProgress();
		$.post('${app}/lesReceive/addTask.action', {
			billId : row.id
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

	//查询
	function query() {
		var mapSheetNo = $.trim($("#mapSheetNo").val());
		$("#dg").datagrid({
			url : "${app}/lesReceive/list.action",
			queryParams : Hmgx.getQueryParamet("cx")
		});
	}
	//新增
	function add() {
		$('#login-name').css('display', '');
		$('#dlg').dialog('open').dialog('setTitle', '新增LES收货单').window("center");
		$('#fm').form('clear');
		url = '${app}/lesReceive/save.action';
	}
	//修改
	function edit() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要修改的数据！");
		if (!row) {
			return;
		}
		$("#status").val(row.status);
		$('#dlg').dialog('open').dialog('setTitle', '修改LES收货单');
		$('#fm').form('load', row);
		url = '${app}/lesReceive/save.action?id=' + row.id;
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
		$.messager.confirm('删除确认', '您确定要删除该收货单?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/lesReceive/del.action', {
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
		var row = Hmgx.GetSelectRow("dg", "请选择手工收货单！");
		if (!row) {
			return;
		}
		$('#login-name').css('display', '');
		$('#dlgList').dialog('open').dialog('setTitle', '新增LES收货单明细').window("center");
		$('#fmList').form('clear');
		url = '${app}/lesReceiveList/save.action';
	}

	//修改明细表
	function updateList() {
		var rowList = Hmgx.GetSelectRow("totalDG", "请选择需要修改的数据！");
		if (!rowList) {
			return;
		}
		$('#dlgList').dialog('open').dialog('setTitle', '修改LES收货单明细');
		$("#wmsgoods").combobox({
			data : [ {
				gcode : rowList.partNo,
				oraCode : rowList.supplNo
			} ]
		});
		rowList.wmsgoods = rowList.partNo
		$('#fmList').form('load', rowList);
		url = '${app}/lesReceiveList/save.action?id=' + rowList.id;
	}

	function saveList() {
		var row = Hmgx.GetSelectRow("dg", "请选择手工收货单！");
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
		$.messager.confirm('删除确认', '您确定要删除该收货明细单?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/lesReceiveList/del.action', {
					id : row.id,
					mapSheetNo : row.mapSheetNo
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

	//格式化日期时间
	function dateTime_matter(date) {
		var y = date.getFullYear();
		var m = date.getMonth() + 1;
		var d = date.getDate();
		var h = date.getHours();
		return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d) + " " + (h < 10 ? '0' + h : h) + ":" + date.getMinutes();
	}

	//les收货清单表  收货状态    0获取单据    1收货登记     2 收货完成    3单据取消    4单据回写
	function forma_lesType(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 0:
			return "获取单据";
			break;
		case 1:
			return "收货登记";
			break;
		case 2:
			return "收货完成";
			break;
		case 3:
			return "单据取消";
			break;
		case 4:
			return "单据回写";
			break;
		default:
			return "未知";
			break;
		}
	}

	//les收货清单明细表  收货状态	0获取单据      1收货登记     2 收货完成    3单据取消
	function forma_lesListType(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 0:
			return "获取单据";
			break;
		case 1:
			return "收货登记";
			break;
		case 2:
			return "收货完成";
			break;
		case 3:
			return "单据取消";
			break;
		default:
			return "未知";
			break;
		}
	}

	$("#wmsgoods").combobox({
		onSelect : function(param) {
			$("#supplNo").val(param.oraCode);
			$("#partNo").val(param.gcode);
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