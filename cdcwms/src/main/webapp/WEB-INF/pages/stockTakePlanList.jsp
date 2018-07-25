<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<body class="easyui-layout">
	<div data-options="region:'center',title:'',border:false" style="padding: 5px;">
		<table id="dg" title="盘库申请单" url="${app}/stocktakePlan/list.action" fit="true" class="easyui-datagrid" toolbar="#toolbar" rownumbers="true"
			singleSelect="true" pagination="true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'takePlanCode',width:150">盘库计划编码</th>
					<th data-options="sortable:true,field:'beginDatetime',width:150">计划盘点开始时间</th>
					<th data-options="sortable:true,field:'endDatetime',width:150">计划盘点结束时间</th>
					<th data-options="sortable:true,field:'takeWokerCode',width:100">操作人</th>
					<th data-options="sortable:true,field:'takeType',width:100,formatter:formatstate">盘点类型</th>
					<th data-options="sortable:true,field:'storageCode',width:100">库位(库区)代码</th>
					<th data-options="sortable:true,field:'whCode',width:100">仓库代码</th>
					<th data-options="sortable:true,field:'batchCode',width:100">批次</th>
					<th data-options="sortable:true,field:'gcode',width:100">指定具体货物</th>
					<th data-options="sortable:true,field:'ownerCode',width:100">指定货主</th>
					<th data-options="sortable:true,field:'status',width:'100px',formatter:forma_createType">状态</th>
					<th data-options="sortable:true,field:'updateUser',width:100">更新人</th>
					<th data-options="sortable:true,field:'updateTime',width:150">更新时间</th>
				</tr>
			</thead>
		</table>
	</div>

	<div id="toolbar">
		<div title="" class="easyui-panel">
			<table id="tb_query">
				<tr>
					<th>盘库计划编码：</th>
					<td>
						<input name="takePlanCode" class="easyui-textbox">
					</td>
					<td>
						<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="query()">查询</a>
					</td>
				</tr>
			</table>
		</div>
		<div id="tabBut"></div>
	</div>
	<!-- 保存或修改对话框 -->
	<div id="dlg" class="easyui-dialog" style="width: 600px" closed="true" modal="true" buttons="#dlg-buttons">
		<form id="fm" method="post">
			<div class="fitem" id="">
				<label>计划盘点开始时间：</label>
				<input name="beginDatetime" class="easyui-datetimebox" style="width: 145px;"
					data-options="formatter:dateTime_matter,labelPosition:'top',required:false,missingMessage:'计划盘点开始时间不能为空。'" />
				<label>计划盘点结束时间：</label>
				<input name="endDatetime" class="easyui-datetimebox" style="width: 145px;"
					data-options="formatter:dateTime_matter,labelPosition:'top',required:false,missingMessage:'计划盘点结束时间不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>操作人：</label>
				<input name="takeWokerCode" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'操作人不能为空。'" />
				<label>盘点类型：</label>
				<select name="takeType" id="takeType" class="easyui-combobox" style="width: 145px;" data-options="required:true,editable:false,missingMessage:'盘点类型不能为空。'">
					<option value="0">库位</option>
					<option value="1">库区</option>
					<option value="2">批次</option>
				</select>
			</div>
			<div class="fitem" id="">
				<label>姓名：</label>
				<input name="zoneCode" id="zoneCode" class="easyui-textbox" style="width: 145px;" required:false,missingMessage:'姓名不能为空。'" />
				<label>库位(库区)代码：</label>
				<input class="easyui-combobox" name="storageCode" id="storageCode" style="width: 145px;"
					data-options="valueField:'storagecode',textField:'storagecode',url:'${app}/storag/getComboBoxStorageCode.action',editable:false,panelHeight:'auto',required:true,missingMessage:'库位(库区)代码不能为空。'" />
			</div>
			<div class="fitem" id="">
				<label>批次：</label>
				<input name="batchCode" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'批次不能为空。'" />
				<label>指定具体货物：</label>
				<input name="gcode" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'指定具体货物不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>指定货主：</label>
				<input name="ownerCode" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'指定货主不能为空。'" />
			</div>
			<input type="hidden" name="takePlanCode" id="takePlanCodes" />
			<input type="hidden" name="status" id="status" />
			<input type="hidden" name="storagecode" id="storagecode" />
			<input type="hidden" name="zonecode" id="zonecode" />
		</form>
	</div>

	<div id="dlg-buttons">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="save()">保存</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	</div>

</body>
<script type="text/javascript">
	$(function() {
		//渲染按钮
		Hmgx.RenderButton("tabBut",
<%=session.getAttribute("curLoginUserButton")%>
	, "${param.pageId}");
	});
	$("#storageCode").combobox({
		onSelect : function(Storage) {
			$("#storageCode").val(Storage.storagecode);
			$("#storageCode").val(Storage.zonecode);
		},
		mode : "remote",
		editable : "true"
	});
	//添加任务
	function addTask() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要修改的数据！");
		if (!row) {
			return;
		}
		showHandleProgress();
		$.post('${app}/stocktakePlan/addTask.action', {
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
		var takePlanCode = $.trim($('input[name=takePlanCode]').val());

		$("#dg").datagrid('reload', {
			takePlanCode : takePlanCode,
		});
	}
	//格式化日期时间
	function dateTime_matter(date) {
		var y = date.getFullYear();
		var m = date.getMonth() + 1;
		var d = date.getDate();
		var h = date.getHours();
		return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d) + " " + (h < 10 ? '0' + h : h) + ":" + date.getMinutes();
	}

	//盘点类型	0 库位1 库区 2 批次
	function formatstate(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 0:
			return "库位 ";
			break;
		case 1:
			return "库区 ";
			break;
		case 2:
			return "批次 ";
			break;
		default:
			return "未知";
			break;
		}
	}

	var url;
	//新增
	function add() {
		$('#login-name').css('display', '');
		$('#dlg').dialog('open').dialog('setTitle', '新增盘库申请表').window("center");
		$('#fm').form('clear');
		url = '${app}/stocktakePlan/save.action';
	}

	//修改
	function edit() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要修改的数据！");
		if (!row) {
			return;
		}
		$("#status").val(row.status);
		$('#dlg').dialog('open').dialog('setTitle', '修改盘库申请表');
		$('#fm').form('load', row);
		url = '${app}/stocktakePlan/save.action?id=' + row.id;
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
		$.messager.confirm('删除确认', '您确定要删除?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/stocktakePlan/del.action', {
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
	//	     0未创建任务    1已创建任务 
	function forma_createType(val, row) {
		switch (val) {
		case 0:
			return "任务未生成";
			break;
		case 1:
			return "任务已生成";
			break;
		case 2:
			return "任务已完成 ";
			break;
		case 3:
			return "任务已取消";
			break;
		default:
			return "未知";
			break;
		}
	}
	$('#takeType').combobox({
		onSelect : function(rec) {
			var url = null;
			if (rec.value == 0) {
				url = "${app}/storag/getComboBoxStorageCode.action";
				valueField = "storagecode";
				textField = "storagecode";
			} else if (rec.value == 1) {
				url = "${app}/zone/getCombobox.action";
				valueField = "zonecode";
				textField = "zonecode";
			} else {
				url = "";
			}
			$('#storageCode').combobox({
				url : url,
				valueField : valueField,
				textField : textField
			});
		}
	});
	$("#dg").datagrid({
		onLoadError : function() {
			$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
		}
	});
</script>