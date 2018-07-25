<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
.ViewImg {
	cursor: pointer;
}
</style>
<body class="easyui-layout">

	<div data-options="region:'center',title:'',border:false" style="padding: 5px;">

		<table id="dg" title="库区列表 " class="easyui-datagrid"
			data-options="remoteSort:false,url:'${app}/zone/list.action',fit:true,rownumbers:true,singleSelect:true,toolbar:'#toolbar',pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'whCode',width:'100px'">所属仓库</th>
					<th data-options="sortable:true,field:'zoneCode',width:'100px'">库区代码</th>
					<th data-options="sortable:true,field:'zname',width:'100px'">库区名称</th>
					<th data-options="sortable:true,field:'statu',width:'80px',formatter:formatstate">库区状态</th>
					<th data-options="sortable:true,field:'descrip',width:'80px'">描述</th>
				</tr>
			</thead>
		</table>
	</div>

	<div id="toolbar"></div>

	<!-- 保存或修改对话框 -->
	<div id="dlg" class="easyui-dialog" style="width: 300px" data-options="closed:true,modal:true,buttons:'#dlg-buttons'">
		<form id="fm" method="post">
			<div class="fitem" id="">
				<label>库区代码：</label>
				<input name="zoneCode" id="zoneCode" class="easyui-textbox" style="width: 145px;" data-options="required:true,missingMessage:'库区编号不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>库区名称：</label>
				<input name="ZName" class="easyui-textbox" style="width: 145px;" data-options="required:true,missingMessage:'库区名称不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>库区状态：</label>
				<select name="statu" class="easyui-combobox" style="width: 145px;"
					data-options="required:true,missingMessage:'请选择库区状态不能为空。',editable:false,panelHeight:'auto'">
					<option value="0">启用</option>
					<option value="1">停用</option>
				</select>
			</div>
			<div class="fitem" id="">
				<label>描述：</label>
				<input name="descrip" class="easyui-textbox" style="width: 145px; height: 60px" data-options="required:false,multiline:true,missingMessage:'货区描述不能为空。'">
			</div>
		</form>
	</div>

	<div id="dlg-buttons">
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="save()">保存</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	</div>

	<!-- 上传图片 -->
	<div id="dlgUpImg" class="easyui-dialog" style="width: 300px" data-options="closed:true,modal:true,buttons:'#dlgUpImg-buttons'">
		<form id="upFileFrom" method="post" enctype="multipart/form-data">
			<div class="fitem" id="">
				<input class="easyui-filebox" name="imgFile" style="width: 100%" data-options="buttonText:'选择文件',required:true,missingMessage:'请导入图片格式的文件!'">
			</div>
		</form>
	</div>

	<div id="dlgUpImg-buttons">
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="saveUpImg()">上传</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="javascript:$('#dlgUpImg').dialog('close')">取消</a>
	</div>


</body>

<script type="text/javascript">
	$(function() {
		//渲染按钮
		Hmgx.RenderButton("toolbar",
<%=session.getAttribute("curLoginUserButton")%>
	, "${param.pageId}");

	});

	//状态
	function formatstate(val, row) {
		if (val == '0') {
			return '启用';
		} else {
			return '停用';
		}
	}
	//图片
	function format_ZGraph(val, row) {
		if (val != "") {
			return '<a href="${app}' + val + '" target="_new"><img src="${app}' + val + '" height="16" width="16" class="ViewImg" /></a>';
		} else {
			return '';
		}
	}

	//货区属性
	function format_ZProperty(val, row) {
		switch (val) {
		case 1:
			return "正常货区";
			break;
		case 2:
			return "封存货区 ";
			break;
		case 3:
			return "损坏货区";
			break;
		case 4:
			return "虚拟货区";
			break;
		case 5:
			return "待发货区";
			break;
		case 6:
			return "待入货区";
			break;
		case 7:
			return "设备区域";
			break;
		case 8:
			return "材料货区";
			break;

		default:
			return "未知";
			break;
		}
	}
	////货区类型
	function format_ZType(val, row) {
		if (val == '1') {
			return '货架区';
		} else if (val == '2') {
			return '平铺区';
		} else {
			return "未知";
		}
	}

	var url;
	//新增
	function add() {
		$('#dlg').dialog('open').dialog('setTitle', '新增库区').window("center");
		$('#fm').form('clear');
		url = '${app}/zone/save.action';
	}

	//修改
	function edit() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要修改的数据！");
		if (!row) {
			return;
		}
		$('#dlg').dialog('open').dialog('setTitle', '修改货区');
		row.ZName = row.zname;
		$('#fm').form('load', row);
		url = '${app}/zone/save.action?id=' + row.id;
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
		$.messager.confirm('删除确认', '您确定要删除该货区?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/zone/del.action', {
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

	//打开上传图片窗口
	function UpImg() {
		var row = Hmgx.GetSelectRow("dg", "请先选择货区！");
		if (!row) {
			return;
		}
		$('#dlgUpImg').dialog('open').dialog('setTitle', '上传图片').window("center");
		$('#upFileFrom').form('clear');
	}
	//上传图片
	function saveUpImg() {
		var row = Hmgx.GetSelectRow("dg", "请先选择货区！");
		if (!row) {
			return;
		}
		$('#upFileFrom').form('submit', {
			url : "${app}/zone/upImg.action?id=" + row.id,
			onSubmit : function() {
				if ($(this).form('validate')) {
					showHandleProgress();
					return true;
				}
				return false;
			},
			success : function(result) {
				hideProgress();
				var result = eval('(' + result + ')');
				if ('-1' == result.retcode) {
					$.messager.alert('错误信息', result.retmsg, 'error');
				} else {
					$.messager.alert('提示信息', result.retmsg, 'info');
					$('#dg').datagrid('reload');
					$('#dlgUpImg').dialog('close');
				}
			}
		});
	}
	$("#dg").datagrid({
		onLoadError : function() {
			$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
		}
	});
</script>