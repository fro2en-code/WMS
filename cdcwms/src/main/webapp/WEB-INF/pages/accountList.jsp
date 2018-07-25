<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'north',title:'',border:false,split:true"
		style="padding: 2px; height: 50%;">
		<table id="totalDG" title="台账总账" class="easyui-datagrid"
			data-options="remoteSort:false,url:'',fit:true,rownumbers:true,singleSelect:true,toolbar:'#totalDGTool',pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'gcode',width:'100px'">物料代码</th>
					<th data-options="sortable:true,field:'gname',width:'100px'">物料名称</th>
					<th data-options="sortable:true,field:'gtype',width:'100px'">零件用途</th>
					<th data-options="sortable:true,field:'oraCode',width:'100px'">供应商代码</th>
					<th data-options="sortable:true,field:'oraName',width:'100px'">供应商名称</th>
					<th data-options="sortable:true,field:'whCode',width:'100px'">仓库代码</th>
					<th data-options="sortable:true,field:'whName',width:'100px'">仓库名称</th>
					<th data-options="sortable:true,field:'totalNum',width:'80px'">库存数量</th>
				</tr>
			</thead>
		</table>

		<div id="totalDGTool">
			<div title="" class="easyui-panel">
				<form id="cx" name="cx" style="padding: 0px; margin: 0px;">
					<table id="tb_query">
						<tr>
							<th>物料代码：</th>
							<td><input name="gcode" id="gcode" class="easyui-textbox">
							</td>
							<%
								String type = request.getParameter("type");
								request.setAttribute("type", type);
							%>
							<c:if test="${type == '1'}">
								<th>供应商代码：</th>
								<td><input name="oraCode" id="oraCode" type="text"
									class="easyui-textbox" /></td>
							</c:if>
							<c:if test="${type == '2'}">
								<input name="remark" value="1" type="hidden" />
							</c:if>
							<td><a href="javascript:;" class="easyui-linkbutton"
								data-options="iconCls:'icon-search',plain:true"
								onclick="query()">查询</a></td>
						</tr>
					</table>
				</form>
			</div>
			<div id="butbar">
				<a href="javascript:;" class="easyui-linkbutton"
					data-options="iconCls:'icon-down',plain:false"
					onclick="exportData()">导出</a>
			</div>
		</div>
	</div>

	<div data-options="region:'center',title:'',border:false"
		style="padding: 2px;">

		<table id="dg" title="台账列表 "
			data-options="remoteSort:false,url:'',fit:true,rownumbers:true,singleSelect:true,toolbar:'#toolbar',pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'dealCode',width:'150px'">单据号</th>
					<th data-options="sortable:true,field:'gcode',width:'100px'">物料代码</th>
					<th data-options="sortable:true,field:'gname',width:'100px'">物料名称</th>
					<th data-options="sortable:true,field:'gtype',width:'100px'">零件用途</th>
					<th data-options="sortable:true,field:'batchCode',width:'100px'">批次号</th>
					<th data-options="sortable:true,field:'oraCode',width:'100px'">供应商代码</th>
					<th data-options="sortable:true,field:'oraName',width:'100px'">供应商名称</th>
					<th data-options="sortable:true,field:'whCode',width:'100px'">仓库代码</th>
					<th data-options="sortable:true,field:'whName',width:'100px'">仓库名称</th>
					<th
						data-options="field:'accountType',width:'100px',formatter:forma_accountType">台账类型</th>
					<th
						data-options="field:'dealType',width:'100px',formatter:forma_dealType">流水类型</th>
					<th data-options="sortable:true,field:'dealNum',width:'80px'">流水数量</th>
					<th data-options="sortable:true,field:'zoneCode',width:'80px'">库区编码</th>
					<th data-options="sortable:true,field:'inTime',width:'80px'">入库时间</th>
					<th data-options="sortable:true,field:'totalNum',width:'80px'">结余</th>
					<th data-options="sortable:true,field:'dealTime',width:'150px'">台账时间</th>
				</tr>
			</thead>
		</table>
	</div>

	<div id="toolbar">
		<a href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-down',plain:false"
			onclick="exportData_account()">导出</a>
	</div>
</body>

<script type="text/javascript">
	$(function() {
		//渲染按钮
		Hmgx.RenderButton("butbar",
<%=session.getAttribute("curLoginUserButton")%>
	,
				"${param.pageId}");

		//主表
		$("#totalDG").datagrid({
			onSelect : function(index, row) {
				$("#dg").datagrid({
					url : "${app}/account/list.action",
					queryParams : {
						oraCode : row.oraCode,
						gcode : row.gcode,
						gtype : row.gtype,
						whCode : row.whCode
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
		$("#totalDG").datagrid({
			url : "${app}/totalAccount/list.action",
			queryParams : Hmgx.getQueryParamet("cx")
		});
	}

	//数据导出
	function exportData() {
		var gcode = $.trim($('input[name=gcode]').val());
		var oraCode = $.trim($('input[name=oraCode]').val());
		var params = {
			keyNo : "${keyNo }",
			gcode : gcode,
			oraCode : oraCode,
		}
		Hmgx.serializeDownload("${app}/totalAccount/export.action", "", params);
	}

	//子表数据导出
	function exportData_account() {
		var rowList = Hmgx.GetSelectRow("totalDG", "选择需要导出的数据");
		if (!rowList) {
			return;
		}

		params = {
			keyNo : "${keyNoList }",
			oraCode : rowList.oraCode,
			gcode : rowList.gcode,
			gtype : rowList.gtype,
			whCode : rowList.whCode
		}
		Hmgx.serializeDownload("${app}/account/export.action", "", params);
	}

	//状态： 台账类型 0 出 1 入
	function forma_accountType(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 0:
			return "出";
			break;
		case 1:
			return "入";
			break;
		default:
			return "未知";
			break;
		}
	}
	//流水类型 0 出库 1 入库 2 红冲
	function forma_dealType(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 0:
			return "出库";
			break;
		case 1:
			return "收货";
			break;
		case 2:
			return "红冲";
			break;
		case 3:
			return "初始化";
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
	$("#totalDG").datagrid({
		onLoadError : function() {
			$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
		}
	});
</script>