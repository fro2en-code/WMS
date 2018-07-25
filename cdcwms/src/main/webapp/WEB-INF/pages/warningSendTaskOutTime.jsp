<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'center',title:'',border:false"
		style="padding: 5px;">

		<table id="dg" title="分拣任务接收超时预警 " class="easyui-datagrid"
			data-options="remoteSort:false,url:'${app}/warningSendTaskOutTime/list.action',fit:true,rownumbers:true,toolbar:'#toolbar',singleSelect:true,pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'Taskdesc',width:'150px'">任务描述</th>
					<th data-options="sortable:true,field:'Status',width:'100px'">任务状态</th>
					<th data-options="sortable:true,field:'executorName',width:'150px'">任务执行人名称</th>
					<th data-options="sortable:true,field:'Billid',width:'100px'">单据ID</th>
					<th data-options="sortable:true,field:'Type',width:'100px'">单据类型</th>
			</thead>
		</table>
	</div>
	<div id="toolbar">
		<form id="queryForm" name="queryForm"
			style="padding: 0px; margin: 0px;">
			<table id="tb_query">
				<tr>
					<th>任务描述：</th>
					<td><input name="Taskdesc" id="Taskdesc"
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
		Hmgx.RenderButton("tabBut",
<%=session.getAttribute("curLoginUserButton")%>
	,
				"${param.pageId}");
	});
	//查询
	function query() {
		var Taskdesc = $.trim($('input[name=Taskdesc]').val());
		$("#dg").datagrid({
			url : '${app}/warningSendTaskOutTime/list.action',
			queryParams : {
				Taskdesc : Taskdesc
			}
		});
	}
</script>
