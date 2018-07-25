<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'center',title:'',border:false"
		style="padding: 5px;">

		<table id="dg" title="库存数量预警 " class="easyui-datagrid"
			data-options="remoteSort:false,url:'${app}/warningStock/list.action',fit:true,rownumbers:true,toolbar:'#toolbar',singleSelect:true,pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'gcode',width:'100px'">物料编码</th>
					<th data-options="sortable:true,field:'gname',width:'100px'">物料名称</th>
					<th data-options="sortable:true,field:'gtype',width:'100px'">零件类型</th>
					<th data-options="sortable:true,field:'oraCode',width:'100px'">供应商代码</th>
					<th data-options="sortable:true,field:'oraName',width:'100px'">供应商名称</th>
					<th data-options="sortable:true,field:'warningMaxNum',width:'150px'">最大预警库存数量</th>
					<th data-options="sortable:true,field:'warningMinNum',width:'150px'">最小预警库存数量</th>
			</thead>
		</table>
	</div>
	<div id="toolbar">
		<form id="queryForm" name="queryForm"
			style="padding: 0px; margin: 0px;">
			<table id="tb_query">
				<tr>
					<th>供应商代码：</th>
					<td><input name="ora_code" id="ora_code" class="easyui-textbox"
						style="width: 120px"></td>

					<th>物料编码：</th>
					<td><input name="G_code" id="G_code"
						class="easyui-textbox" style="width: 120px"></td>


					<th>物料名称：</th>
					<td><input name="G_NAME" id="G_NAME"
						class="easyui-textbox" style="width: 120px"></td>


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

	});
	//查询
	function query() {
		var ora_code = $.trim($('input[name=ora_code]').val());
		var G_code = $.trim($('input[name=G_code]').val());
		var G_NAME = $.trim($('input[name=G_NAME]').val());
		$("#dg").datagrid({
			url : '${app}/warningStock/list.action',
			queryParams : {
				ora_code : ora_code,
				G_code : G_code,
				G_NAME : G_NAME,
			}
		});
	}
</script>