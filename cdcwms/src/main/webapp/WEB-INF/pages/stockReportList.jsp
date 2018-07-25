<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'center',title:'',border:false"
		style="padding: 5px;">

		<table id="dg" title="库存月报表 " class="easyui-datagrid"
			data-options="remoteSort:false,url:'${app}/stockReport/list.action',fit:true,rownumbers:true,toolbar:'#toolbar',singleSelect:true,pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'reportDate',width:'100px'">日期</th>
					<th data-options="sortable:true,field:'oraCode',width:'100px'">奇瑞供应商代码</th>
					<th data-options="sortable:true,field:'oraName',width:'150px'">供应商名称</th>
					<th data-options="sortable:true,field:'goodsCode',width:'100px'">物料编码</th>
					<th data-options="sortable:true,field:'goodsAlias',width:'100px'">内部零件号</th>
					<th data-options="sortable:true,field:'goodsName',width:'100px'">物料名称</th>
					<th data-options="sortable:true,field:'goodsType',width:'100px'">用途</th>
					<th data-options="sortable:true,field:'beforeStock',width:'100px'">上月结存</th>
					<th data-options="sortable:true,field:'inStock',width:'130px'">当月入库量</th>
					<th data-options="sortable:true,field:'outStock',width:'100px'">当月出库量</th>
					<th data-options="sortable:true,field:'nowStock',width:'100px'">现余库存</th>
			</thead>
		</table>
	</div>

	<div id="toolbar">
		<form id="queryForm" name="queryForm"
			style="padding: 0px; margin: 0px;">
			<table id="tb_query">
				<tr>
					<th>供应商代码：</th>
					<td><input name="oraCode" id="oraCode" class="easyui-textbox"
						style="width: 120px"></td>

					<th>物料编号：</th>
					<td><input name="goodsCode" id="goodsCode"
						class="easyui-textbox" style="width: 120px"></td>

					<th>内部零件号：</th>
					<td><input name="goodsAlias" id="goodsAlias"
						class="easyui-textbox" style="width: 120px"></td>

					<th>零件用途：</th>
					<td><input name="goodsType" id="goodsType"
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
	//excel导出
	function exportData() {
		var params = Hmgx.getQueryParamet("queryForm")
		params.keyNo = "${keyNo }";
		Hmgx.serializeDownload("${app}/stockReport/export.action", "", params);
	}
	//查询
	function query() {
		$("#dg").datagrid({
			url : "${app}/stockReport/list.action",
			queryParams : Hmgx.getQueryParamet("queryForm")
		});
	}
	$("#dg").datagrid({
		onLoadError : function() {
			$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
		}
	});
</script>