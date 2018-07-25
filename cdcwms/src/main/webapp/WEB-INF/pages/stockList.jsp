<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'center',title:'',border:false" style="padding: 5px;">

		<table id="dg" title="库存管理 " class="easyui-datagrid"
			data-options="remoteSort:false,url:'${app}/stock/list.action',fit:true,rownumbers:true,toolbar:'#toolbar',singleSelect:true,pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'whCode',width:'100px'">仓库编码</th>
					<th data-options="sortable:true,field:'zoneCode',width:'100px'">库区编码</th>
					<th data-options="sortable:true,field:'storageCode',width:'150px'">库位编码</th>
					<th data-options="sortable:true,field:'gcode',width:'150px'">物料编码</th>
					<th data-options="sortable:true,field:'gname',width:'100px'">物料名称</th>
					<th data-options="sortable:true,field:'gtype',width:'100px'">零件用途</th>
					<th data-options="sortable:true,field:'supCode',width:'100px'">供应商编码</th>
					<th data-options="sortable:true,field:'supName',width:'100px'">供应商名称</th>
					<th data-options="sortable:true,field:'batchCode',width:'130px'">批次</th>
					<th data-options="sortable:true,field:'quantity',width:'100px'">库存数量</th>
					<th data-options="sortable:true,field:'prePickNum',width:'100px'">预拣货数量</th>
					<th data-options="sortable:true,field:'lockType',width:'100px'">锁定库存类型</th>
					<th data-options="sortable:true,field:'lockNum',width:'100px'">锁定库存数量</th>
					<th data-options="sortable:true,field:'preLock',width:'100px'">占位库存数量</th>
					<th data-options="sortable:true,field:'updateUser',width:'100px'">最后修改人</th>
					<th data-options="sortable:true,field:'updateTime',width:'150px'">最后修改时间</th>
			</thead>
		</table>
	</div>

	<div id="toolbar">
		<form id="queryForm" name="queryForm" style="padding: 0px; margin: 0px;">
			<table id="tb_query">
				<tr>
					<th>仓库编码：</th>
					<td>
						<input name="whCode" id="whCode" class="easyui-textbox" style="width: 120px">
					</td>

					<th>库区编码：</th>
					<td>
						<input name="zoneCode" id="zoneCode" class="easyui-textbox" style="width: 120px">
					</td>

					<th>库位编码：</th>
					<td>
						<input name="storageCode" id="storageCode" class="easyui-textbox" style="width: 120px">
					</td>

					<th>物料编码：</th>
					<td>
						<input name="gcode" id="gcode" class="easyui-textbox" style="width: 120px">
					</td>

					<th>供应商编码：</th>
					<td>
						<input name="supCode" id="supCode" class="easyui-textbox" style="width: 120px">
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

</body>

<script type="text/javascript">
	$(function() {
		//渲染按钮
		Hmgx.RenderButton("butbar",
<%=session.getAttribute("curLoginUserButton")%>
	, "${param.pageId}");

		//$("#dg").datagrid({
		//rowStyler: function(index,row){				
		//if (row.storageCode == ""){
		//return 'color: DarkGray;font-weight: bold;';
		//}
		//}
		//}); 
	});

	//数据导出
	function exportData() {
		var whCode = $.trim($('input[name=whCode]').val());
		var zoneCode = $.trim($('input[name=zoneCode]').val());
		var storageCode = $.trim($('input[name=storageCode]').val());
		var gcode = $.trim($('input[name=gcode]').val());
		var supCode = $.trim($('input[name=supCode]').val());
		var params = {
			keyNo : "${keyNo }",
			whCode : whCode,
			zoneCode : zoneCode,
			storageCode : storageCode,
			gcode : gcode,
			supCode : supCode
		}
		Hmgx.serializeDownload("${app}/stock/export.action", "", params);
	}

	//查询
	function query() {
		$("#dg").datagrid({
			url : "${app}/stock/list.action",
			queryParams : Hmgx.getQueryParamet("queryForm")
		});
	}
	$("#dg").datagrid({
		onLoadError : function() {
			$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
		}
	});
</script>