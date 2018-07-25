<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'north',title:'',border:false,split:true" style="padding: 2px; height: 50%;">
		<table id="totalDG" title="运单信息" class="easyui-datagrid"
			data-options="remoteSort:false,url:'',fit:true,rownumbers:true,singleSelect:true,toolbar:'#totalDGTool',pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'number',width:'125px'">单据编号</th>
					<th data-options="sortable:true,field:'truckNO',width:'150px'">车牌号码</th>
					<th data-options="sortable:true,field:'drive',width:'150px'">驾驶员</th>
					<th data-options="sortable:true,field:'drivePhone',width:'150px'">驾驶员电话</th>
					<th data-options="sortable:true,field:'updateUser',width:'80px'">创建人</th>
					<th data-options="sortable:true,field:'updateTime',width:'150px'">创建时间</th>
				</tr>
			</thead>
		</table>

		<div id="totalDGTool">
			<div title="" class="easyui-panel">
				<form id="cx" name="cx" style="padding: 0px; margin: 0px;">
					<table id="tb_query">
						<tr>
							<th>单据编号：</th>
							<td>
								<input name="number" class="easyui-textbox">
							</td>
							<td>
								<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="query()">查询</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>

	<div data-options="region:'center',title:'',border:false" style="padding: 2px;">

		<table id="dg" title="运单信息明细" data-options="remoteSort:false,url:'',fit:true,rownumbers:true,singleSelect:true,toolbar:'#toolbar',pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'number',width:'125px'">单据编号</th>
					<th data-options="sortable:true,field:'sendBillNumber',width:'125px'">发货单编号</th>
					<th data-options="sortable:true,field:'type',width:'125px'">发货单类型</th>
					<th data-options="sortable:true,field:'updateUser',width:'80px'">创建人</th>
					<th data-options="sortable:true,field:'updateTime',width:'150px'">创建时间</th>
				</tr>
			</thead>
		</table>
	</div>

	<div id="toolbar"></div>
</body>

<script type="text/javascript">
	$(function() {
		//渲染按钮
		Hmgx.RenderButton("toolbar",
<%=session.getAttribute("curLoginUserButton")%>
	, "${param.pageId}");

		//主表
		$("#totalDG").datagrid({
			url : "${app}/waybill/list.action",
			onSelect : function(index, row) {
				$("#dg").datagrid({
					url : "${app}/waybillList/list.action",
					queryParams : {
						number : row.number
					}
				});
			}
		});

		//子表
		$("#dg").datagrid({
			rowStyler : function(index, row) {
				if (row.accountType == 0) {
					return 'background-color:#DFFFDF;color:#000000;';
				} else if (row.accountType == 1) {
					return 'background-color:#FFE6D9;color:#000000;';
				}
			}
		});
	});

	//查询
	function query() {
		var mapSheetNo = $.trim($("#billId").val());
		$("#totalDG").datagrid({
			url : "${app}/waybill/list.action",
			queryParams : Hmgx.getQueryParamet("cx")
		});
	}
</script>