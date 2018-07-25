<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'north',title:'',border:false,split:true" style="padding: 2px; height: 50%;">
		<table id="totalDG" title="运单信息" class="easyui-datagrid" data-options="remoteSort:false,url:'',fit:true,rownumbers:true,toolbar:'#totalDGTool',border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'ck',checkbox:true"></th>
					<th data-options="sortable:true,field:'number',width:'125px'">单据编号</th>
					<th data-options="sortable:true,field:'priority',width:'150px'">是否紧急</th>
					<th data-options="sortable:true,field:'type',width:'150px'">类型</th>
					<th data-options="sortable:true,field:'lastRecRequrieTime',width:'150px'">要求到货时间</th>
					<th data-options="sortable:true,field:'mriCreateTime',width:'150px'">需求创建时间</th>
					<th data-options="sortable:true,field:'deliveryRec',width:'150px'">目的地代码</th>
				</tr>
			</thead>
		</table>

		<div id="totalDGTool">
			<div id="toolbar"></div>
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
		<table id="dg" title="发货单明细" data-options="remoteSort:false,url:'',fit:true,rownumbers:true,singleSelect:true,pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'number',width:'125px'">单据编号</th>
					<th data-options="sortable:true,field:'sxCardNo',width:'125px'">随箱卡号</th>
					<th data-options="sortable:true,field:'gCode',width:'125px'">物料编码</th>
					<th data-options="sortable:true,field:'oraCode',width:'125px'">供应商编码</th>
					<th data-options="sortable:true,field:'sendPackageNo',width:'125px'">包装类型</th>
					<th data-options="sortable:true,field:'sendPackageNum',width:'125px'">包装容量</th>
					<th data-options="sortable:true,field:'reqPackageNum',width:'125px'">箱数</th>
					<th data-options="sortable:true,field:'reqQty',width:'125px'">需求件数</th>
				</tr>
			</thead>
		</table>
	</div>
	<div id="dlgList" class="easyui-dialog" style="width: 600px" data-options="closed:true,modal:true,buttons:'#dlgList-buttons'">
		<form id="fmList" method="post">
			<div class="fitem">
				<label>车牌号：</label>
				<input class="easyui-combobox" name="truckNO" id="truckNO" style="width: 145px;"
					data-options="valueField:'plateInd',textField:'plateInd',url:'${app}/truck/getTruckList.action',mode:'remote',editable:true,panelHeight:'auto',required:false" />
				<label>驾驶员：</label>
				<input name="drive" id="drive" class="easyui-textbox" style="width: 145px;" data-options="required:false">
			</div>
			<div class="fitem">
				<label>驾驶员手机号：</label>
				<input name="drivePhone" id="drivePhone" class="easyui-textbox" style="width: 145px;" data-options="required:false">
			</div>
		</form>
	</div>
	<div id="dlgList-buttons">
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="save()">保存</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="javascript:$('#dlgList').dialog('close')">取消</a>
	</div>

</body>

<script type="text/javascript">
	$(function() {
		//渲染按钮
		Hmgx.RenderButton("toolbar",
<%=session.getAttribute("curLoginUserButton")%>
	, "${param.pageId}");

		//主表
		$("#totalDG").datagrid({
			url : "${app}/wmsSendBill/list.action",
			onSelect : function(index, row) {
				$("#dg").datagrid({
					url : "${app}/wmsSendBill/listDetail.action",
					queryParams : {
						number : row.number,
						type : row.type
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
			url : "${app}/wmsSendBill/list.action",
			queryParams : Hmgx.getQueryParamet("cx")
		});
	}

	function ok() {
		var rows = $('#totalDG').datagrid('getSelections');
		if (rows.length == 0) {
			$.messager.alert('错误信息', "请选择数据", 'error');
			return;
		}
		$('#dlgList').dialog('open').dialog('setTitle', '发车单信息').window("center");
		$('#fmList').form('clear');
	}

	function save() {
		var rows = $('#totalDG').datagrid('getSelections');
		if (!$('#fmList').form('validate')) {
			return false;
		}
		showHandleProgress();
		var data = Hmgx.getQueryParamet('fmList');
		for (var i = 0, j = rows.length; i < j; i++) {
			var row = rows[i];
			if (row.type == '手工发货单') {
				if (null == data.handBill) {
					data.handBill = new Array();
				}
				data.handBill[data.handBill.length] = row.number;
			} else if (row.type == 'LES发货单') {
				if (null == data.lesBill) {
					data.lesBill = new Array();
				}
				data.lesBill[data.lesBill.length] = row.number;
			}
		}
		//data = JSON.stringify(data);
		$.ajax({
			type : 'POST',
			url : "${app}/wmsSendBill/send.action",
			data : data,
			success : function(result) {
				hideProgress();
				if (!result.suc) {
					$.messager.alert('错误信息', result.retmsg, 'error');
				} else {
					$('#dlgList').dialog('close');
					$('#totalDG').datagrid('reload');
				}
			}
		}).error(function() {
			hideProgress();
			$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
		});
	}

	$("#truckNO").combobox({
		onSelect : function(param) {
			$("#drive").textbox("setValue", param.firstPilot);
			$("#drivePhone").textbox("setValue", param.phoneNumber);
		}
	});
</script>