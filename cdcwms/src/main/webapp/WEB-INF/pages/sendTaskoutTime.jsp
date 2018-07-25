<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'center',title:'',border:false"
		style="padding: 5px;">

		<table id="dg" title="分拣任务超时预警 " class="easyui-datagrid"
			data-options="remoteSort:false,url:'${app}/stockReport/list.action',fit:true,rownumbers:true,toolbar:'#toolbar',singleSelect:true,pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'taskdesc',width:'100px'">任务描述</th>
					<th data-options="sortable:true,field:'status',width:'100px'">任务状态</th>
					<th data-options="sortable:true,field:'executorName',width:'100px'">任务执行人名称</th>
					<th data-options="sortable:true,field:'billid',width:'100px'">单据ID</th>
					<th data-options="sortable:true,field:'type',width:'100px'">单据类型</th>
			</thead>
		</table>
	</div>
</body>
