<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'center',title:'',border:false"
		style="padding: 5px;">

		<table id="dg" title="车辆管理" class="easyui-datagrid"
			data-options="remoteSort:false,url:'${app}/truck/list.action',fit:true,rownumbers:true,toolbar:'#toolbar',singleSelect:true,pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'companyid',width:'100px'">所属公司</th>
					<th data-options="sortable:true,field:'plateInd',width:'100px'">车牌号码</th>
					<th data-options="sortable:true,field:'identity',width:'100px'">行驶证号码</th>
					<th data-options="sortable:true,field:'firstPilot',width:'100px'">主驾驶员</th>
					<th data-options="sortable:true,field:'secondPilot',width:'100px'">副驾驶员</th>
					<th data-options="sortable:true,field:'phoneNumber',width:'100px'">驾驶员电话号码</th>
					<th data-options="sortable:true,field:'updateUser',width:'100px'">最后更新人</th>
					<th data-options="sortable:true,field:'updateTime',width:'100px'">最后更新时间</th>
			</thead>
		</table>
	</div>

	<div id="toolbar">
		<form id="queryForm" name="queryForm"
			style="padding: 0px; margin: 0px;">
			<table id="tb_query">
				<tr>
					<th>公司：</th>
					<td><input name="companyid" id="companyid"
						class="easyui-textbox" style="width: 120px"></td>

					<th>车牌号码：</th>
					<td><input name="plateInd" id="plateInd"
						class="easyui-textbox" style="width: 120px"></td>

					<th>行驶证号码：</th>
					<td><input name="identity" id="identity"
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
	<!-- 保存或修改对话框 -->
	<div id="dlg" class="easyui-dialog" style="width: 600px"
		data-options="closed:true,modal:true,buttons:'#dlg-buttons'">
		<form id="fm" method="post">
			<div class="fitem" id="">
				<label>公司编码：</label> <input name="companyid" id="companyids"
					class="easyui-textbox" style="width: 145px;"
					data-options="valueField:'companyid',textField:'companyid',url:'${app}/conpany/getComboboxByConCode.action',editable:false,panelHeight:'auto',required:true,missingMessage:'请选择公司编码。'" />
				<label>车牌号：</label> <input name="plateInd" class="easyui-textbox"
					style="width: 145px;"
					data-options="required:true,missingMessage:'车牌号不能为空。'">
			</div>

			<div class="fitem" id="">
				<label>行驶证号码：</label> <input name="identity" class="easyui-textbox"
					style="width: 145px;"
					data-options="required:false,missingMessage:'行驶证号码不能为空。'">
				<label>主驾驶员：</label> <input name="firstPilot" class="easyui-textbox"
					style="width: 145px;"
					data-options="required:false,missingMessage:'主驾驶员不能为空。'">

			</div>
			<div class="fitem" id="">
				<label>副驾驶员：</label> <input name="secondPilot"
					class="easyui-textbox" style="width: 145px;"
					data-options="required:false,missingMessage:'副驾驶员不能为空。'"> <label>电话号码：</label>
				<input name="phoneNumber" class="easyui-textbox"
					style="width: 145px;"
					data-options="required:false,missingMessage:'电话号码不能为空。'">

			</div>


		</form>
	</div>

	<div id="dlg-buttons">
		<a href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-ok'" onclick="save()">保存</a> <a
			href="javascript:;" class="easyui-linkbutton"
			data-options="iconCls:'icon-cancel'"
			onclick="javascript:$('#dlg').dialog('close')">取消</a>
	</div>
</body>

<script type="text/javascript">
	$("#companyids").combobox({
		onSelect : function(param) {
			//$("#companyid").val(param.companyid);
		},
		mode : "remote",
		editable : "true"
	});

	$(function() {
		//渲染按钮
		Hmgx.RenderButton("butbar",
<%=session.getAttribute("curLoginUserButton")%>
	,
				"${param.pageId}");
	});

	//查询
	function query() {
		var companyid = $.trim($('input[name=companyid]').val());
		var plateInd = $.trim($('input[name=plateInd]').val());
		var identity = $.trim($('input[name=identity]').val());
		$("#dg").datagrid('reload', {
			companyid : companyid,
			plateInd : plateInd,
			identity : identity
		});

	}

	var url;
	//新增
	function add() {
		$('#login-name').css('display', '');
		$('#dlg').dialog('open').dialog('setTitle', '新增车辆').window("center");
		$('#fm').form('clear');
		url = '${app}/truck/save.action';
	}

	//修改
	function edit() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要修改的数据！");
		if (!row) {
			return;
		}
		$('#dlg').dialog('open').dialog('setTitle', '修改车辆');
		$('#fm').form('load', row);
		url = '${app}/truck/save.action?id=' + row.id;
	}
	function save() {
		if (!$('#fm').form('validate')) {
			return false;
		}
		showHandleProgress();
		$.ajax({
			type : 'POST',
			url : url,
			data : $('#fm').serialize(),
			success : function(result) {
				hideProgress();
				if (!result.suc) {
					$.messager.alert('错误信息', result.retmsg, 'error');
				} else {
					$('#dlg').dialog('close');
					$('#dg').datagrid('reload');
				}
			}
		}).error(function() {
			hideProgress();
			$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
		});
	}

	//删除
	function del() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要删除的数据！");
		if (!row) {
			return;
		}
		$.messager.confirm('删除确认', '您确定要删除该车辆?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/truck/del.action', {
					id : row.id
				}, function(result) {
					hideProgress();
					if ('-1' == result.retcode) {
						$.messager.alert('错误信息', result.retmsg, 'error');
					} else {
						$('#dg').datagrid('reload');
					}
				}, "json").error(function() {
					hideProgress();
					$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
				});
			}
		});
	}

	$("#dg").datagrid({
		onLoadError : function() {
			$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
		}
	});
</script>