<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<body class="easyui-layout">

	<div data-options="region:'center',title:'',border:false"
		style="padding: 5px;">
		<table id="dg" title="盘库清单" url="${app}/stocktake/list.action"
			fit="true" class="easyui-datagrid" toolbar="#toolbar"
			rownumbers="true" singleSelect="true" pagination="true">
			<thead>
				<tr>

					<th data-options="sortable:true,field:'takeWokerCode',width:100">操作人</th>
					<th data-options="sortable:true,field:'takePlanCode',width:150">盘库计划编码</th>
					<th data-options="sortable:true,field:'lineNum',width:150">行号</th>
					<th data-options="sortable:true,field:'storageCode',width:100">货位编码</th>
					<th data-options="sortable:true,field:'whCode',width:100">仓库代码</th>
					<th data-options="sortable:true,field:'gcode',width:100">物料编码</th>
					<th data-options="sortable:true,field:'gname',width:100">物料名称</th>
					<th data-options="sortable:true,field:'oraCode',width:100">供应商编码</th>
					<th data-options="sortable:true,field:'batchCode',width:100">批次</th>
					<th data-options="sortable:true,field:'quantity',width:100">库存数量</th>
					<th data-options="sortable:true,field:'actQuantity',width:100">实际库存数量</th>
					<th
						data-options="sortable:true,field:'statu',width:150,formatter:formatstate">状态</th>
				</tr>
			</thead>
		</table>
	</div>

	<div id="toolbar">
		<div title="" class="easyui-panel">
			<table id="tb_query">
				<tr>
					<th>盘库计划编码：</th>
					<td><input name="takePlanCode" class="easyui-textbox">
					</td>

					<th>库位：</th>
					<td><input name="storageCode" class="easyui-textbox">
					</td>

					<th>物料编码：</th>
					<td><input name="gcode" class="easyui-textbox"></td>

					<th>供应商编码：</th>
					<td><input name="oraCode" class="easyui-textbox"></td>
					<td><a href="javascript:;" class="easyui-linkbutton"
						iconCls="icon-search" plain="true" onclick="query()">查询</a></td>
				</tr>
			</table>
		</div>
		<div id="tabBut"></div>
	</div>


</body>
<script type="text/javascript">
	$(function() {
		//渲染按钮
		Hmgx.RenderButton("tabBut",
<%=session.getAttribute("curLoginUserButton")%>
	,
				"${param.pageId}");
	});

	//查询

	function query() {
		var takePlanCode = $.trim($('input[name=takePlanCode]').val());
		var storageCode = $.trim($('input[name=storageCode]').val());
		var gcode = $.trim($('input[name=gcode]').val());
		var oraCode = $.trim($('input[name=oraCode]').val());
		$("#dg").datagrid('reload', {
			takePlanCode : takePlanCode,
			storageCode : storageCode,
			gcode : gcode,
			oraCode : oraCode
		});
	}

	//生成库存调整单
	function addAdj() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要生成的数据！");
		if (!row) {
			return;
		}
		$.messager.confirm('生成确认', '您确定要生成库存调整单?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/stocktake/addAdj.action', {
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
	//excel导出
	function exportData() {
		var takePlanCode = $.trim($('input[name=takePlanCode]').val());
		var params = {
			keyNo : "${keyNo }",
			takePlanCode : takePlanCode
		}
		Hmgx.serializeDownload("${app}/stocktake/export.action", "", params);
	}
	//状态	0 未查    1完毕
	function formatstate(val, row) {
		if (null == val) {
			return;
		}
		if (!row) {
			return;
		}
		switch (val) {
		case 0:
			return "未查 ";
			break;
		case 1:
			return "完毕 ";
			break;
		case 2:
			return "已生成库存调整申请单";
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