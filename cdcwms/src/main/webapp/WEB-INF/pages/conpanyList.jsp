<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'center',title:'',border:false" style="padding: 5px;">

		<table id="dg" title="公司列表 " class="easyui-datagrid"
			data-options="remoteSort:false,url:'${app}/conpany/list.action',fit:true,rownumbers:true,toolbar:'#toolbar',singleSelect:true,pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'companyid',width:'100px'">公司编码</th>
					<th data-options="sortable:true,field:'name',width:'100px'">公司名称</th>
					<th data-options="sortable:true,field:'contacter',width:'100px'">联系人</th>
					<th data-options="sortable:true,field:'address',width:'100px'">地址</th>
					<th data-options="sortable:true,field:'longitude',width:'100px'">gps经度</th>
					<th data-options="sortable:true,field:'latitude',width:'100px'">gps纬度</th>
					<th data-options="sortable:true,field:'telphone',width:'100px'">联系电话</th>
					<th data-options="sortable:true,field:'mobile',width:'100px'">手机号码</th>
					<th data-options="sortable:true,field:'type',width:'100px'">公司类型</th>
					<th data-options="sortable:true,field:'companyemail',width:'100px'">公司电子邮箱</th>
					<th data-options="sortable:true,field:'updateUser',width:'80px'">最后更新人</th>
					<th data-options="sortable:true,field:'updateTime',width:'150px'">最后更新时间</th>
				</tr>
			</thead>
		</table>
	</div>

	<div id="toolbar">
		<form id="queryForm" name="queryForm" style="padding: 0px; margin: 0px;">
			<table id="tb_query">
				<tr>
					<th>公司编码：</th>
					<td>
						<input name="companyid" id="companyid" class="easyui-textbox" style="width: 120px">
					</td>

					<th>公司名字：</th>
					<td>
						<input name="name" id="name" class="easyui-textbox" style="width: 200px">
					</td>

					<td>
						<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="query()">查询</a>
					</td>
				</tr>
			</table>
			<div style="border-top: 1px dashed #0066CC;"></div>
		</form>
		<div id="butbar"></div>
	</div>
	<!-- 保存或修改对话框 -->
	<div id="dlg" class="easyui-dialog" style="width: 600px" data-options="closed:true,modal:true,buttons:'#dlg-buttons'">
		<form id="fm" method="post">
			<div class="fitem" id="">
				<label>公司编码：</label>
				<input name="companyid" class="easyui-textbox" style="width: 145px;" data-options="required:true,missingMessage:'公司编码不能为空。'">
				<label>公司名称：</label>
				<input name="name" class="easyui-textbox" style="width: 145px;" data-options="required:true,missingMessage:'公司名称不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>公司类型：</label>
				<select name="type" class="easyui-combobox" style="width: 145px;" data-options="required:true,editable:false,missingMessage:'公司类型不能为空。'">
					<option value="运行公司">运行公司</option>
					<option value="供应商">供应商</option>
					<option value="承运商">承运商</option>
					<option value="业务中心">业务中心</option>
				</select>
				<label>地址：</label>
				<input name="address" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'地址不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>gps经度：</label>
				<input name="longitude" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'gps经度不能为空。'">
				<label>gps纬度：</label>
				<input name="latitude" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'gps纬度不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>联系人：</label>
				<input name="contacter" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'联系人不能为空。'">
				<label>联系电话：</label>
				<input name="telphone" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'联系电话不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>手机号码：</label>
				<input name="mobile" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'手机号码不能为空。'">
				<label>公司电子邮箱：</label>
				<input name="companyemail" class="easyui-textbox" style="width: 145px;" data-options="required:false,missingMessage:'公司电子邮箱不能为空。'">

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
		Hmgx.RenderButton("butbar",
<%=session.getAttribute("curLoginUserButton")%>
	, "${param.pageId}");
	});

	//查询
	function query() {
		$("#dg").datagrid({
			queryParams : Hmgx.getQueryParamet("queryForm")
		});
	}
	var url;
	//新增
	function add() {
		$('#login-name').css('display', '');
		$('#dlg').dialog('open').dialog('setTitle', '新增').window("center");
		$('#fm').form('clear');
		url = '${app}/conpany/save.action';
	}

	//修改
	function edit() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要修改的数据！");
		if (!row) {
			return;
		}
		$('#dlg').dialog('open').dialog('setTitle', '修改');
		$('#fm').form('load', row);
		url = '${app}/conpany/save.action?id=' + row.id;
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
		$.messager.confirm('删除确认', '您确定要删除该信息?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/conpany/del.action', {
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