<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'center',title:'',border:false" style="padding: 5px;">

		<table id="dg" title="任务流程列表 " class="easyui-datagrid"
			data-options="remoteSort:false,url:'${app}/taskFlow/list.action',fit:true,rownumbers:true,singleSelect:true,toolbar:'#toolbar',pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'nowPerson',width:'100px'">当前执行人</th>
					<th data-options="sortable:true,field:'nowRole',width:'80px'">当前执行人角色</th>
					<th data-options="sortable:true,field:'nowTaskCode',width:'150px',formatter:format_taskType">当前任务编号</th>
					<th data-options="sortable:true,field:'nextTaskCode',width:'150px',formatter:format_taskType">下一任务编号</th>
					<th data-options="sortable:true,field:'cron',width:'80px'">定时器</th>
				</tr>
			</thead>
		</table>
	</div>

	<div id="toolbar"></div>

	<!-- 保存或修改对话框 -->
	<div id="dlg" class="easyui-dialog" style="width: 300px" data-options="closed:true,modal:true,buttons:'#dlg-buttons'">
		<form id="fm" method="post">
			<div class="fitem">
				<label>当前执行人：</label>
				<input name="nowPerson" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'当前执行人不能为空。'">
			</div>
			<div class="fitem">
				<label>当前执行人角色：</label>
				<input name="nowRole" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'当前执行人角色不能为空。'">
			</div>
			<div class="fitem">
				<label>当前任务：</label>
				<select name="nowTaskCode" class="easyui-combobox" style="width: 145px;"
					data-options="required:true,missingMessage:'请选择当前任务。',editable:false,panelHeight:'auto'">
					<option value="1">手工收货</option>
					<option value="2">LES收货</option>
					<option value="3">收货搬运</option>
					<option value="4">上架</option>
					<option value="5">预拣货</option>
					<option value="6">手工发货</option>
					<option value="7">LES发货</option>
					<option value="8">移库</option>
					<option value="9">盘点</option>
					<option value="10">发货装车</option>
					<option value="11">溢库区上架</option>
				</select>
			</div>
			<div class="fitem">
				<label>下一任务：</label>
				<select name="nextTaskCode" class="easyui-combobox" style="width: 145px;"
					data-options="required:false,missingMessage:'请选择下一任务。',editable:false,panelHeight:'auto'">
					<option value="">&nbsp</option>
					<option value="1">手工收货</option>
					<option value="2">LES收货</option>
					<option value="3">收货搬运</option>
					<option value="4">上架</option>
					<option value="5">预拣货</option>
					<option value="6">手工发货</option>
					<option value="7">LES发货</option>
					<option value="8">移库</option>
					<option value="9">盘点</option>
					<option value="10">发货装车</option>
					<option value="11">溢库区上架(定时执行)</option>
				</select>
			</div>
			<div class="fitem">
				<label>定时器：</label>
				<input name="cron" class="easyui-textbox" style="width: 145px;" data-options="required:false">
			</div>

		</form>
	</div>

	<div id="dlg-buttons">
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="save()">保存</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	</div>

</body>

<script type="text/javascript">
	$(function() {
		//渲染按钮
		Hmgx.RenderButton("toolbar",
<%=session.getAttribute("curLoginUserButton")%>
	, "${param.pageId}");
	});
	//任务列表
	function format_taskType(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 1:
			return "手工收货";
			break;
		case 2:
			return "LES收货";
			break;
		case 3:
			return "收货搬运";
			break;
		case 4:
			return "上架";
			break;
		case 5:
			return "预拣货";
			break;
		case 6:
			return "手工发货";
			break;
		case 7:
			return "LES发货";
			break;
		case 8:
			return "移库";
			break;
		case 9:
			return "盘点 ";
			break;
		case 10:
			return "发货装车";
			break;
		case 11:
			return "溢库区上架";
			break;
		default:
			return "未知";
			break;
		}
	}

	var url;
	//新增
	function add() {
		$('#login-name').css('display', '');
		$('#dlg').dialog('open').dialog('setTitle', '新增').window("center");
		$('#fm').form('clear');
		url = '${app}/taskFlow/save.action';
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
		$.messager.confirm('删除确认', '您确定要删除该任务流程?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/taskFlow/del.action', {
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