<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'center',title:'',border:false"
		style="padding: 5px;">

		<table id="dg" title="配车回单超时预警 " class="easyui-datagrid"
			data-options="remoteSort:false,url:'${app}/warningReturnOutTime/list.action',fit:true,rownumbers:true,toolbar:'#toolbar',singleSelect:true,pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'mapSheetNo',width:'100px'">对应配送单号</th>
					<th data-options="sortable:true,field:'isEmerge',width:'100px'">是否紧急</th>
					<th data-options="sortable:true,field:'deliveryRecType',width:'100px'">目的地类型</th>
					<th data-options="sortable:true,field:'mriCreateTime',width:'100px'">需求创建时间</th>
			</thead>
		</table>
	</div>
	<div id="toolbar">
		<form id="queryForm" name="queryForm"
			style="padding: 0px; margin: 0px;">
			<table id="tb_query">
				<tr>
					<th>对应配送单号：</th>
					<td><input name="MAP_SHEET_NO" id="MAP_SHEET_NO" class="easyui-textbox"
						style="width: 120px"></td>


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
		var MAP_SHEET_NO = $.trim($('input[name=MAP_SHEET_NO]').val());
		$("#dg").datagrid({
			url : '${app}/warningReturnOutTime/list.action',
			queryParams : {
				MAP_SHEET_NO : MAP_SHEET_NO
			}
		});
	}
</script>
