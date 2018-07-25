<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'center',title:'',border:false"
		style="padding: 5px;">

		<table id="dg" title="预拣货任务列表 " class="easyui-datagrid"
			data-options="remoteSort:false,url:'${app}/taskPrePick/list.action',fit:true,rownumbers:true,singleSelect:true,toolbar:'#toolbar',pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'expression',width:'100px'">表达式</th>
					<th data-options="sortable:true,field:'gcode',width:'100px'">物料编码</th>
					<th data-options="sortable:true,field:'gname',width:'100px'">物料名称</th>
					<th data-options="sortable:true,field:'gtype',width:'100px'">零件用途</th>
					<th data-options="sortable:true,field:'prePickNum',width:'80px'">预拣货数量</th>
					<th data-options="sortable:true,field:'reqPackageNum',width:'80px'">箱数</th>
					<th
						data-options="sortable:true,field:'sendPackageNum',width:'80px'">每箱包装容量</th>
					<th data-options="sortable:true,field:'whCode',width:'100px'">仓库编号</th>
					<th data-options="sortable:true,field:'whName',width:'100px'">仓库名称</th>
					<th data-options="sortable:true,field:'oraCode',width:'100px'">供应商编号</th>
					<th data-options="sortable:true,field:'oraName',width:'100px'">供应商名称</th>
					<th data-options="sortable:true,field:'insertUser',width:'100px'">创建人</th>
					<th data-options="sortable:true,field:'insertTime',width:'120px'">创建时间</th>
				</tr>
			</thead>
		</table>
	</div>

	<div id="toolbar"></div>

	<!-- 保存或修改对话框 -->
	<div id="dlg" class="easyui-dialog" style="width: 600px"
		data-options="closed:true,modal:true,buttons:'#dlg-buttons'">
		<form id="fm" method="post">
			<input type="hidden" name="gcode" id="gcode" /> <input type="hidden"
				name="gname" id="gname" /> <input type="hidden" name="gtype"
				id="gtype" /> <input type="hidden" name="oraCode" id="oraCode" />
			<input type="hidden" name="oraName" id="oraName" />
			<div class="fitem" id="">
				<label>表达式：</label> <input name="expression" class="easyui-textbox"
					style="width: 145px;"
					data-options="required:true,missingMessage:'表达式不能为空。'"> <label>零件号：</label>
				<input class="easyui-combobox" name="wmsgoods" id="wmsgoods"
					style="width: 145px;"
					data-options="valueField:'valueStr',textField:'valueStr',url:'${app}/goods/getWmsGoodsByKey.action',editable:false,panelHeight:'auto',required:true,missingMessage:'零件不能为空'" />
			</div>
			<div class="fitem" id="">
				<label>每箱包装容量：</label> <input name="sendPackageNum"
					class="easyui-textbox" style="width: 145px;"
					data-options="required:true,missingMessage:'每箱包装容量不能为空。'">
				<label>箱数：</label> <input name="reqPackageNum"
					class="easyui-textbox" style="width: 145px;"
					data-options="required:true,missingMessage:'箱数不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>预拣货数量：</label> <input name="prePickNum"
					class="easyui-textbox" style="width: 145px;"
					data-options="required:true,missingMessage:'预拣货数量不能为空。'">
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
	$(function() {
		//渲染按钮
		Hmgx.RenderButton("toolbar",
<%=session.getAttribute("curLoginUserButton")%>
	,
				"${param.pageId}");
	});
	//添加任务
	function addTask() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要修改的数据！");
		if (!row) {
			return;
		}
		showHandleProgress();
		$.post('${app}/taskPrePick/addTask.action', {
			billId : row.id
		}, function(result) {
			hideProgress();
			if ('-1' == result.retcode) {
				$.messager.alert('错误信息', result.retmsg, 'error');
			} else {
				$('#dg').datagrid('reload');//刷新主表
				$("#dgSub").datagrid('loadData', {
					total : 0,
					rows : []
				});//清空子表
			}
		}, "json").error(function() {
			hideProgress();
			$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
		});
	}
	var url;
	//新增
	function add() {
		$('#login-name').css('display', '');
		$('#dlg').dialog('open').dialog('setTitle', '新增预拣货任务').window("center");
		$('#fm').form('clear');
		url = '${app}/taskPrePick/save.action';
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
		$.messager.confirm('删除确认', '您确定要删除该预拣货任务?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/taskPrePick/del.action', {
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

	//获取物料，供应商
	$("#wmsgoods").combobox({
		onSelect : function(param) {
			//$("#oraCode").val(param.oraCode);
			$("#gcode").val(param.gcode);
			$("#gname").val(param.gname);
			$("#gtype").val(param.gtype);
			$("#oraCode").val(param.oraCode);
			$("#oraName").val(param.oraName);
		},
		mode : "remote",
		editable : "true"
	});
	$("#dg").datagrid({
		onLoadError : function() {
			$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
		}
	});
</script>