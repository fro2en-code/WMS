<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'center',title:'',border:false" style="padding: 5px;">

		<table id="dg" title="司机管理" class="easyui-datagrid"
			data-options="remoteSort:false,url:'${app}/driver/list.action',fit:true,rownumbers:true,toolbar:'#toolbar',singleSelect:true,pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'orgcode',width:'100px'">所属公司编码</th>
					<th data-options="sortable:true,field:'drName',width:'100px'">司机姓名</th>
					<th data-options="sortable:true,field:'sex',width:'100px'">性别</th>
					<th data-options="sortable:true,field:'birth',width:'100px'">出生年月</th>
					<th data-options="sortable:true,field:'licenceid',width:'100px'">驾照号码</th>
					<th data-options="sortable:true,field:'licenceType',width:'100px'">驾照类型</th>
					<th data-options="sortable:true,field:'statu',width:'100px'">状态</th>


					<th data-options="sortable:true,field:'insertUser',width:'80px'">创建人</th>
					<th data-options="sortable:true,field:'insertTime',width:'150px'">创建时间</th>
					<th data-options="sortable:true,field:'updateUser',width:'100px'">最后修改人</th>
					<th data-options="sortable:true,field:'updateTime',width:'100px'">最后修改时间</th>
			</thead>
		</table>
	</div>

	<div id="toolbar"></div>
	<!-- 保存或修改对话框 -->
	<div id="dlg" class="easyui-dialog" style="width: 600px" data-options="closed:true,modal:true,buttons:'#dlg-buttons'">
		<form id="fm" method="post">
			<div class="fitem" id="">
				<label>所属公司编码：</label>
				<input name="orgcode" class="easyui-textbox" style="width: 145px;" data-options="required:true,missingMessage:'所属公司编码不能为空。'">
				<label>司机姓名：</label>
				<input name="drName" class="easyui-textbox" style="width: 145px;" data-options="required:true,missingMessage:'司机姓名不能为空。'">
			</div>

			<div class="fitem" id="">
				<label>性别：</label>
				<input name="sex" class="easyui-textbox" style="width: 145px;" data-options="required:true,missingMessage:'性别不能为空。'">

				<label>出生年月：</label>
				<input name="birth" class="easyui-textbox" style="width: 145px;" data-options="required:true,missingMessage:'出生年月不能为空。'">
			</div>

			<div class="fitem" id="">
				<label>驾照号码：</label>
				<input name="licenceid" class="easyui-textbox" style="width: 145px;" data-options="required:true,missingMessage:'驾照号码不能为空。'">
				<label>驾照类型：</label>
				<input name="licenceType" class="easyui-textbox" style="width: 145px;" data-options="required:true,missingMessage:'驾照类型不能为空。'">
			</div>

			<div class="fitem" id="">
				<label>状态：</label>
				<input name="statu" class="easyui-numberbox" style="width: 145px;" data-options="required:true,missingMessage:'状态不能为空。'">

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

	var url;
	//新增
	function add() {
		$('#login-name').css('display', '');
		$('#dlg').dialog('open').dialog('setTitle', '新增司机').window("center");
		$('#fm').form('clear');
		url = '${app}/driver/save.action';
	}

	//修改
	function edit() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要修改的数据！");
		if (!row) {
			return;
		}
		$('#dlg').dialog('open').dialog('setTitle', '修改司机');
		$('#fm').form('load', row);
		url = '${app}/driver/save.action?id=' + row.id;
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
		$.messager.confirm('删除确认', '您确定要删除该司机?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/driver/del.action', {
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