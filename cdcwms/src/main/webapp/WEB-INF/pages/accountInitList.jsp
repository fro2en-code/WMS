<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<!-- 初始化主表 -->
	<div data-options="region:'north',title:'',border:false,split:true" style="padding: 2px 2px 0px 2px; height: 50%">
		<table id="dg" title="初始化单据"
			data-options="remoteSort:false,url:'${app}/accountInit/list.action',fit:true,rownumbers:true,singleSelect:true,toolbar:'#toolbar',pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'ck',checkbox:true"></th>
					<th data-options="sortable:true,field:'initCode',width:'150px'">初始化单号</th>
					<th data-options="sortable:true,field:'whCode',width:'100px'">仓库代码</th>
					<th data-options="sortable:true,field:'whName',width:'100px'">仓库名称</th>
					<th data-options="sortable:true,field:'wkCode',width:'100px'">操作员</th>
					<th data-options="sortable:true,field:'status',width:'100px',formatter:format_status">状态</th>
					<th data-options="sortable:true,field:'insertUser',width:'80px'">创建人</th>
					<th data-options="sortable:true,field:'insertTime',width:'150px'">创建时间</th>
					<th data-options="sortable:true,field:'initTime',width:'150px'">初始化时间</th>
				</tr>
			</thead>
		</table>
	</div>

	<!-- 初始化子表 -->
	<div data-options="region:'center',title:'',border:false,minHeight:50" style="padding: 0px 2px 2px 2px;">
		<table id="dgSub" title="初始化单据明细" class="easyui-datagrid"
			data-options="remoteSort:false,url:'',fit:true,rownumbers:true,singleSelect:true,toolbar:'#toolbarSub',pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'ck',checkbox:true"></th>
					<th data-options="sortable:true,field:'initCode',width:'150px'">初始化单号</th>
					<th data-options="sortable:true,field:'whCode',width:'100px'">仓库代码</th>
					<th data-options="sortable:true,field:'whName',width:'100px'">仓库名称</th>
					<th data-options="sortable:true,field:'zoneCode',width:'100px'">库区代码</th>
					<th data-options="sortable:true,field:'storageCode',width:'100px'">库位代码</th>
					<th data-options="sortable:true,field:'gcode',width:'100px'">物料代码</th>
					<th data-options="sortable:true,field:'gname',width:'100px'">物料名称</th>
					<th data-options="sortable:true,field:'gtype',width:'100px'">物料用途</th>
					<th data-options="sortable:true,field:'oraCode',width:'100px'">客户代码</th>
					<th data-options="sortable:true,field:'oraName',width:'100px'">客户名称</th>
					<th data-options="sortable:true,field:'initNum',width:'100px'">初始化数量</th>
				</tr>
			</thead>
		</table>
	</div>

	<!-- 工具栏按钮 -->
	<div id="toolbar"></div>
	<div id="toolbarSub">
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-down',plain:false" onclick="downTemp()">下载模板</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-excel',plain:false" onclick="importDate()">导入数据</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:false" onclick="delSubOne()">删除单条</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:false" onclick="delSubAll()">删除全部</a>
	</div>

	<!-- 保存或修改对话框 -->
	<div id="dlg" class="easyui-dialog" style="width: 300px" data-options="closed:true,modal:true,buttons:'#dlg-buttons'">
		<!-- <form id="fm" method="post">
			<div class="fitem" id="" >
				 <label>出库操作员：</label><input name="wkCode" class="easyui-textbox"
					style="width: 145px;"
					data-options="required:false,missingMessage:'出库操作员不能为空。'">		
			</div>		
		</form>-->
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
		$("#dg").datagrid({
			onSelect : function(index, row) {
				$("#dgSub").datagrid({
					url : "${app}/accountInitList/list.action",
					queryParams : {
						initCode : row.initCode
					}
				});
			},
			rowStyler : function(index, row) {
				if (row.status == 1) {
					return 'background-color:#6293BB;color:#fff;';
				}
			}
		});
	});

	//--------------------------------------------------------
	//------------------------------------------------主表操作
	//--------------------------------------------------------
	//状态 0未确认 1已确认
	function format_status(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 0:
			return "未确认";
			break;
		case 1:
			return "已确认";
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
		//$('#dlg').dialog('open').dialog('setTitle', '新增初始化单据').window("center");
		$('#fm').form('clear');
		url = '${app}/accountInit/save.action';
		save();
	}

	//修改
	function edit() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要修改的数据！");
		if (!row) {
			return;
		}
		$('#dlg').dialog('open').dialog('setTitle', '修改初始化单据');
		$('#fm').form('load', row);
		url = '${app}/accountInit/save.action?id=' + row.id;
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
		var row = CheckSelect();
		if (!row) {
			return;
		}
		$.messager.confirm('删除确认', '您确定要删除该单据?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/accountInit/del.action', {
					id : row.id
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
		});
	}

	//初始化单据确认
	function sure() {
		var row = CheckSelect();
		if (!row) {
			return;
		}
		$.messager.confirm('初始化单据确认', '您确定要确认该单据?<br>确认后将不能执行任何操作！', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/accountInit/sure.action', {
					id : row.id
				}, function(result) {
					hideProgress();
					if ('-1' == result.retcode) {
						$.messager.alert('错误信息', result.retmsg, 'error');
					} else {
						$('#dg').datagrid('reload');//刷新主表
					}
				}, "json").error(function() {
					hideProgress();
					$.messager.alert('提示信息', '系统后台执行中,请稍后查看,请勿重复操作.', 'error');
				});
			}
		});
	}
	//--------------------------------------------------------
	//------------------------------------------------子表操作
	//--------------------------------------------------------
	//删除子表单条物料
	function delSubOne() {
		var mainRow = CheckSelect();
		if (!mainRow)
			return;
		var subRow = Hmgx.GetSelectRow("dgSub", "<div style='height:28px;line-height:28px'>请选择需要删除的记录！</div>");
		if (!subRow) {
			return;
		}
		$.messager.confirm('删除确认', '您确定要删除该物料?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/accountInitList/del.action', {
					id : subRow.id
				}, function(result) {
					hideProgress();
					if ('-1' == result.retcode) {
						$.messager.alert('错误信息', result.retmsg, 'error');
					} else {
						$('#dgSub').datagrid('reload');
					}
				}, "json");
			}
		});
	}

	//删除子表全部物料
	function delSubAll() {
		var mainRow = CheckSelect();
		if (!mainRow)
			return;
		$.messager.confirm('删除确认', '您确定要删除该单据的全部物料?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/accountInitList/del.action', {
					initCode : mainRow.initCode
				}, function(result) {
					hideProgress();
					if ('-1' == result.retcode) {
						$.messager.alert('错误信息', result.retmsg, 'error');
					} else {
						$('#dgSub').datagrid('reload');
					}
				}, "json").error(function() {
					hideProgress();
					$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
				});
			}
		});
	}

	//--------------------------------------------------------
	//------------------------------------------------公共方法
	//--------------------------------------------------------
	//检查主表
	function CheckSelect() {
		var mainRow = Hmgx.GetSelectRow("dg", "<div style='height:28px;line-height:28px'>请先选择初使化单据！</div>");
		if (!mainRow) {
			return false;
		} else if (mainRow.status == 1) {
			$.messager.alert("提示信息", "<div style='height:28px;line-height:28px'>此单据已确认,不可在执行其它操作！</div>", "error");
			return false;
		}
		return mainRow;
	}
	$("#dg").datagrid({
		onLoadError : function() {
			$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
		}
	});
</script>