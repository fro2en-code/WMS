<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'center',title:'',border:false" style="padding: 5px;">

		<table id="dg" title="移库申请单 " class="easyui-datagrid"
			data-options="remoteSort:false,url:'${app}/move/list.action',fit:true,rownumbers:true,singleSelect:true,toolbar:'#toolbar',pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'whCode',width:'125px'">仓库代码</th>
					<th data-options="sortable:true,field:'moveCode',width:'150px'">移库单编码</th>
					<th data-options="sortable:true,field:'moveTime',width:'150px'">计划转移时间</th>
					<th data-options="sortable:true,field:'moveReason',width:'125px'">转移原因</th>
					<th data-options="sortable:true,field:'gcode',width:'125px'">物料编码</th>
					<th data-options="sortable:true,field:'gname',width:'150px'">物料名称</th>
					<th data-options="sortable:true,field:'supCode',width:'125px'">供应商代码</th>
					<th data-options="sortable:true,field:'supName',width:'150px'">供应商名称</th>
					<th data-options="sortable:true,field:'batchCode',width:'100px'">批次</th>
					<th data-options="sortable:true,field:'boxCode',width:'125px'">包装代码</th>
					<th data-options="sortable:true,field:'usedStorageCode',width:'150px'">原始库位</th>
					<th data-options="sortable:true,field:'newStorageCode',width:'125px'">目标库位</th>
					<th data-options="sortable:true,field:'moveNum',width:'150px'">移动数量</th>
					<th data-options="field:'statu',width:'100px',formatter:format_Statu">执行状态</th>
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
					<th>移库单编码：</th>
					<td>
						<input name="moveCode" id="moveCode" class="easyui-textbox" style="width: 120px">
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
			<input type="hidden" name="gcode" id="gcode" />
			<input type="hidden" name="gname" id="gname" />
			<input type="hidden" name="supCode" id="supCode" />
			<input type="hidden" name="supName" id="supName" />
			<input type="hidden" name="gtype" id="gtype" />
			<div class="fitem" id="">
				<label>转移原因：</label>
				<input name="moveReason" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'转移原因不能为空。'">
				<label>零件：</label>
				<input class="easyui-combobox" name="wmsgoods" id="wmsgoods" style="width: 145px;"
					data-options="valueField:'valueStr',textField:'valueStr',url:'${app}/goods/getWmsGoodsByKey.action',editable:false,panelHeight:'auto',required:true,missingMessage:'零件不能为空'" />
			</div>
			<div class="fitem" id="">
				<label>包装代码：</label>
				<input name="boxCode" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'包装代码不能为空。'">
				<label>原始库位：</label>
				<input class="easyui-combobox" name="usedStorageCode" id="usedStorageCode" style="width: 145px;"
					data-options="valueField:'storagecode',textField:'storagecode',url:'${app}/storag/getComboBoxStorageCode.action',editable:false,panelHeight:'auto',required:true,missingMessage:'请选择原始库位。'" />
			</div>
			<div class="fitem" id="">
				<label>目标库位：</label>
				<input class="easyui-combobox" name="newStorageCode" id="newStorageCode" style="width: 145px;"
					data-options="valueField:'storagecode',textField:'storagecode',url:'${app}/storag/getComboBoxStorageCode.action',editable:false,panelHeight:'auto',required:true,missingMessage:'请选择目标库位。'" />
				<label>移动数量：</label>
				<input name="moveNum" class="easyui-numberbox" style="width: 145px;" data-options="required:true,missingMessage:'移动数量不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>计划转移时间：</label>
				<input name="moveTime" class="easyui-datetimebox" style="width: 145px;"
					data-options="formatter:dateTime_matter,labelPosition:'top',required:true,missingMessage:'计划转移时间不能为空。'">
			</div>
			<input type="hidden" name="moveCode" id="moveCodes" />
			<input type="hidden" name="storagecode" id="storagecode" />
			<input type="hidden" name="statu" id="status" />
		</form>
	</div>

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
	//原始库位
	$("#usedStorageCode").combobox({
		onSelect : function(Storage) {
			$("#usedStorageCode").val(Storage.storagecode);
		},
		mode : "remote",
		editable : "true"
	});
	//目标库位
	$("#newStorageCode").combobox({
		onSelect : function(Storage) {
			$("#newStorageCode").val(Storage.storagecode);
		},
		mode : "remote",
		editable : "true"
	});

	//添加任务
	function addTask() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要添加的数据！");
		if (!row) {
			return;
		}
		showHandleProgress();
		$.post('${app}/move/addTask.action', {
			id : row.id,
			whCode : row.whCode
		}, function(result) {
			hideProgress();
			if ('-1' == result.retcode) {
				$.messager.alert('错误信息', result.retmsg, 'error');
			} else {
				$('#dg').datagrid('reload');//刷新主表
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
		return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d) + " " + (h < 10 ? '0' + h : h) + ":" + date.getMinutes();
	}
	//状态   0未执行   1已执行
	function format_Statu(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 0:
			return "获取单据";
			break;
		case 1:
			return "创建任务";
			break;
		case 2:
			return "任务接收";
			break;
		case 3:
			return "任务完成 ";
			break;
		default:
			return "未知";
			break;
		}
	}

	//查询

	function query() {
		var moveCode = $.trim($('input[name=moveCode]').val());

		$("#dg").datagrid('reload', {
			moveCode : moveCode,
		});
	}

	var url;
	//新增
	function add() {
		$('#login-name').css('display', '');
		$('#dlg').dialog('open').dialog('setTitle', '新增移库申请单').window("center");
		$('#fm').form('clear');
		url = '${app}/move/save.action';
	}

	//修改
	function edit() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要修改的数据！");
		if (!row) {
			return;
		}
		$("#moveCodes").val(row.moveCode);
		$("#status").val(row.statu);
		$('#dlg').dialog('open').dialog('setTitle', '修改移库申请单');
		$("#wmsgoods").combobox({
			data : [ {
				gcode : row.gcode,
				gtype : row.gtype
			} ]
		});
		row.wmsgoods = row.gcode
		$('#fm').form('load', row);
		url = '${app}/move/save.action?id=' + row.id;
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
		$.messager.confirm('删除确认', '您确定要删除该数据?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/move/del.action', {
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
	$("#wmsgoods").combobox({
		onSelect : function(param) {
			$("#supCode").val(param.oraCode);
			$("#supName").val(param.oraName);
			$("#gcode").val(param.gcode);
			$("#gname").val(param.gname);
			$("#gtype").val(param.gtype);
		},
		mode : "remote",
		editable : "true"
	});
	$("#dg").datagrid({
		onLoadError : function() {
			$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
		}
	});
</script>