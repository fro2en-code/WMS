<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'center',title:'',border:false"
		style="padding: 5px;">

		<table id="dg" title="配车回单超时预警 " class="easyui-datagrid"
			data-options="remoteSort:false,url:'${app}/stockReport/list.action',fit:true,rownumbers:true,toolbar:'#toolbar',singleSelect:true,pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'mapSheetNo',width:'100px'">对应配送单号</th>
					<th data-options="sortable:true,field:'isEmerge',width:'100px'">是否紧急需求</th>
					<th data-options="sortable:true,field:'deliveryRecType',width:'100px'">目的地类型</th>
					<th data-options="sortable:true,field:'mriCreateTime',width:'100px'">需求创建时间</th>
			</thead>
		</table>
	</div>
</body>
