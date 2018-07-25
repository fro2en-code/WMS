<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'center',title:'',border:false" style="padding: 5px;">

		<table id="dg" title="入库明细" class="easyui-datagrid" data-options="fit:true,rownumbers:true,toolbar:'#toolbar',singleSelect:true,pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'MAP_SHEET_NO',width:'100px'">配送单号</th>
					<th data-options="sortable:true,field:'SUPPL_NO',width:'150px'">供应商编码</th>
					<th data-options="sortable:true,field:'ora_name',width:'100px'">供应商名字</th>
					<th data-options="sortable:true,field:'PART_NO',width:'100px'">零件号</th>
					<th data-options="sortable:true,field:'inner_coding',width:'100px'">内部编码</th>
					<th data-options="sortable:true,field:'SEND_PACKAGE_NUM',width:'100px'">发运包装容量</th>
					<th data-options="sortable:true,field:'REQ_PACKAGE_NUM',width:'100px'">需求箱数</th>
					<th data-options="sortable:true,field:'REQ_QTY',width:'130px'">需求数量</th>
					<th data-options="sortable:true,field:'begin_time',width:'100px'">开始时间</th>
					<th data-options="sortable:true,field:'end_time',width:'100px'">结束时间</th>
					<th data-options="sortable:true,field:'G_TYPE',width:'100px'">零件用途</th>
			</thead>
		</table>
	</div>

	<div id="toolbar">
		<form id="queryForm" name="queryForm" style="padding: 0px; margin: 0px;">
			<table id="tb_query">
				<tr>
					<th>供应商代码：</th>
					<td>
						<input name="SUPPL_NO" id="SUPPL_NO" class="easyui-textbox" style="width: 100px">
					</td>

					<th>零件号：</th>
					<td>
						<input name="PART_NO" id="PART_NO" class="easyui-textbox" style="width: 100px">
					</td>

					<th>配送单号：</th>
					<td>
						<input name="MAP_SHEET_NO" id="MAP_SHEET_NO" class="easyui-textbox" style="width: 100px">
					</td>
					<th>开始时间：</th>
					<td>
						<input name="begin_time" id="begin_time" data-options="formatter:myformatter,parser:myparser" class="easyui-datebox" style="width: 140px">
					</td>
					<th>结束时间：</th>
					<td>
						<input name="end_time" id="end_time" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser" style="width: 140px">
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

	});
	//数据导出
	function exportData() {
		var SUPPL_NO = $.trim($('input[name=SUPPL_NO]').val());
		var PART_NO = $.trim($('input[name=PART_NO]').val());
		var MAP_SHEET_NO = $.trim($('input[name=MAP_SHEET_NO]').val());
		var begin_time = $.trim($('input[name=begin_time]').val());
		var end_time = $.trim($('input[name=end_time]').val());
		var params = {
			keyNo : "${keyNo }",
			SUPPL_NO : SUPPL_NO,
			PART_NO : PART_NO,
			MAP_SHEET_NO : MAP_SHEET_NO,
			begin_time : begin_time,
			end_time : end_time
		}
		Hmgx.serializeDownload("${app}/intoStorage/export.action", "", params);
	}
	//查询
	function query() {
		var SUPPL_NO = $.trim($('input[name=SUPPL_NO]').val());
		var PART_NO = $.trim($('input[name=PART_NO]').val());
		var MAP_SHEET_NO = $.trim($('input[name=MAP_SHEET_NO]').val());
		var begin_time = $.trim($('input[name=begin_time]').val());
		var end_time = $.trim($('input[name=end_time]').val());
		$("#dg").datagrid({
			url : '${app}/intoStorage/list.action',
			queryParams : {
				SUPPL_NO : SUPPL_NO,
				PART_NO : PART_NO,
				MAP_SHEET_NO : MAP_SHEET_NO,
				begin_time : begin_time,
				end_time : end_time
			}
		});
	}

	function myformatter(date) {
		var y = date.getFullYear();
		var m = date.getMonth() + 1;
		var d = date.getDate();
		return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d);

	}

	function myparser(s) {
		if (!s)
			return new Date();
		var y = s.substring(0, 4);
		var m = s.substring(5, 7);
		var d = s.substring(8, 10);
		var h = s.substring(11, 14);
		var min = s.substring(15, 17);
		var sec = s.substring(18, 20);
		if (!isNaN(y) && !isNaN(m) && !isNaN(d) && !isNaN(h) && !isNaN(min) && !isNaN(sec)) {
			return new Date(y, m - 1, d, h, min, sec);
		} else {
			return new Date();
		}
	}
	$("#dg").datagrid({
		onLoadError : function() {
			$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
		}
	});
</script>