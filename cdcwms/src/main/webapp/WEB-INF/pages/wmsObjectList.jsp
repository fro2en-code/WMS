<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<body class="easyui-layout">
	<div data-options="region:'center',title:'',border:false"
		style="padding: 5px;">
		<table id="dg" title="零件管理" url="${app}/wmsObject/list.action"
			fit="true" class="easyui-datagrid" toolbar="#toolbar"
			rownumbers="true" singleSelect="true" pagination="true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'ck',checkbox:true"></th>
					<th data-options="sortable:true,field:'whCode',width:100">仓库代码</th>
					<th data-options="sortable:true,field:'whName',width:100">仓库名称</th>
					<th data-options="sortable:true,field:'gcode',width:100">物料编号</th>
					<th data-options="sortable:true,field:'gname',width:100">物料名称</th>
					<th data-options="sortable:true,field:'gtype',width:100">零件用途</th>
					<th data-options="sortable:true,field:'supCode',width:100">供应商代码</th>
					<th data-options="sortable:true,field:'supName',width:100">供应商名字</th>
					<th data-options="sortable:true,field:'batchCode',width:100">批次号</th>
					<th data-options="sortable:true,field:'inTime',width:100">入库时间</th>
					<th data-options="sortable:true,field:'outTime',width:100">过期时间</th>
					<th data-options="sortable:true,field:'quantity',width:100">零件数量</th>
					<th data-options="sortable:true,field:'lockNum',width:100">锁定零件数量</th>
					<th data-options="sortable:true,field:'updateUser',width:100">最后修改人</th>
					<th data-options="sortable:true,field:'updateTime',width:100">最后修改时间</th>
				</tr>
			</thead>
		</table>
	</div>

	<div id="toolbar">
		<div title="" class="easyui-panel">
			<table id="tb_query">
				<tr>
					<th>物料编码:</th>
					<td><input name="gcode" class="easyui-textbox"></td>
					<th>供应商编码：</th>
					<td><input name="supCode" class="easyui-textbox"></td>
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

	//数据导出
	function exportData() {
		var gcode = $.trim($('input[name=gcode]').val());
		var supCode = $.trim($('input[name=supCode]').val());
		var params = {
			keyNo : "${keyNo }",
			gcode : gcode,
			supCode : supCode
		}
		Hmgx.serializeDownload("${app}/wmsObject/export.action", "", params);
	}

	//查询
	function query() {
		var gcode = $.trim($('input[name=gcode]').val());
		var supCode = $.trim($('input[name=supCode]').val());
		$("#dg").datagrid('reload', {
			gcode : gcode,
			supCode : supCode
		});
	}
	$("#dg").datagrid({
		onLoadError : function() {
			$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
		}
	});
</script>