<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'center',title:'',border:false"
		style="padding: 5px;">
		<table id="dg" title="物料列表" class="easyui-datagrid"
			data-options="remoteSort:false,url:'${app}/goods/list.action',fit:true,rownumbers:true,singleSelect:true,toolbar:'#toolbar',pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'gcode',width:'150px'">物料编号</th>
					<th data-options="sortable:true,field:'gname',width:'150px'">物料名称</th>
					<th data-options="sortable:true,field:'units',width:'80px'">计量单位</th>
					<th data-options="sortable:true,field:'oraCode',width:'100px'">供应商编号</th>
					<th data-options="sortable:true,field:'oraName',width:'200px'">供应商名称</th>
					<th data-options="sortable:true,field:'whCode',width:'100px'">仓库编号</th>
					<th data-options="sortable:true,field:'whName',width:'100px'">仓库名称</th>
					<th data-options="sortable:true,field:'gtype',width:'100px'">零件用途</th>
					<th data-options="sortable:true,field:'chineseDes',width:'200px'">中文描述</th>
					<th data-options="sortable:true,field:'englishDes',width:'100px'">英文描述</th>
					<th data-options="sortable:true,field:'abcType',width:'80px'">ABC类型</th>
					<th data-options="sortable:true,field:'qualityDay',width:'80px'">保质期</th>
					<th data-options="sortable:true,field:'gValue',width:'80px'">单件货值</th>
					<th data-options="sortable:true,field:'sendBoxid',width:'80px'">发运包装编号</th>
					<th
						data-options="sortable:true,field:'boxType',width:'80px',formatter:forma_boxType">存储包装类型</th>
					<th data-options="sortable:true,field:'boxNum',width:'80px'">存储包装数量</th>
					<th data-options="sortable:true,field:'packLength',width:'80px'">包装规格长度</th>
					<th data-options="sortable:true,field:'packWidth',width:'80px'">包装规格宽度</th>
					<th data-options="sortable:true,field:'packHeigth',width:'80px'">包装规格高度</th>
					<th data-options="sortable:true,field:'packWeigth',width:'80px'">包装重量</th>
					<th
						data-options="sortable:true,field:'batchType',width:'80px',formatter:forma_batchType">批次管理</th>
					<th
						data-options="sortable:true,field:'storageType',width:'80px',formatter:forma_storageType">存储类型</th>
					<th data-options="sortable:true,field:'trayNum',width:'100px'">托盘存储包装数</th>
					<th
						data-options="sortable:true,field:'receivetrayNum',width:'100px'">托盘收货包装数</th>
					<th
						data-options="sortable:true,field:'storagezoneType',width:'100px',formatter:forma_storagezoneType">指定库位/库区</th>
					<th
						data-options="sortable:true,field:'storagezoneId',width:'100px'">库位/库区编号</th>
					<th data-options="sortable:true,field:'repleniShment',width:'80px'">补货的库存数</th>
					<th data-options="sortable:true,field:'moveNum',width:'80px'">移库的库存数</th>
					<th data-options="sortable:true,field:'maxNum',width:'80px'">最大的库存数</th>
					<th
						data-options="sortable:true,field:'storageMaxNum',width:'100px'">库位最大库存数</th>
					<th
						data-options="sortable:true,field:'warningMaxNum',width:'100px'">最大预警库存</th>
					<th
						data-options="sortable:true,field:'warningMinNum',width:'100px'">最小预警库存</th>
					<th data-options="sortable:true,field:'forkliftNum',width:'80px'">叉车数量</th>
					<th
						data-options="sortable:true,field:'skipPutaway',width:'100px',formatter:forma_skipPutaway">搬运直接上架</th>
					<th data-options="sortable:true,field:'innerCoding',width:'80px'">内部编码</th>
					<th data-options="sortable:true,field:'updateUser',width:'80px'">最后修改人</th>
					<th data-options="sortable:true,field:'updateTime',width:'150px'">最后修改时间</th>
				</tr>
			</thead>
		</table>
	</div>
	<div id="toolbar">
		<form id="queryForm" name="queryForm"
			style="padding: 0px; margin: 0px;">
			<input type="hidden" name="keyNo" value="${keyNo }" />
			<table id="tb_query">
				<tr>
					<th>物料编号：</th>
					<td><input name="gcode" id="gcode" class="easyui-textbox"
						style="width: 120px"></td>
					<th>供应商编码：</th>
					<td><input name="oraCode" id="oraCode" class="easyui-textbox"
						style="width: 120px"></td>
					<th>库区/库位编号：</th>
					<td><input name="storagezoneId" class="easyui-textbox"
						style="width: 120px"></td>

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
	<div id="dlg" class="easyui-dialog" style="width: 800px;"
		data-options="closed:true,modal:true,buttons:'#dlg-buttons'">
		<form id="fm" method="post">
			<div class="fitem" id="">
				<label>物料编号：</label> <input name="gcode" class="easyui-textbox"
					style="width: 145px;"
					data-options="required:true,missingMessage:'物料编号不能为空。'"> <label>物料名称：</label>
				<input name="gname" class="easyui-textbox" style="width: 145px;"
					data-options="required:true,missingMessage:'物料名称不能为空。'"> <label>供应商：</label>
				<input class="easyui-combobox" name="oraCode" id="oraCodes"
					style="width: 145px;"
					data-options="valueField:'companyid',textField:'companyid',url:'${app}/conpany/getComboboxByConCode.action',editable:false,panelHeight:'auto',required:true,missingMessage:'请选择供应商。'" />

			</div>

			<div class="fitem" id="">
				<label>存储类型：</label> <select name="storageType"
					class="easyui-combobox" style="width: 145px;"
					data-options="required:true,missingMessage:'存储类型不能为空。',editable:false,panelHeight:'auto'">
					<option value="1">托盘</option>
					<option value="2">堆放</option>
				</select> <label>中文描述：</label> <input name="chineseDes"
					class="easyui-textbox" style="width: 145px;"
					data-options="required:false,missingMessage:'中文描述不能为空。'"> <label>英文描述：</label>
				<input name="englishDes" class="easyui-textbox"
					style="width: 145px;"
					data-options="required:false,missingMessage:'英文描述不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>ABC类型：</label> <select name="abcType" class="easyui-combobox"
					style="width: 145px;"
					data-options="required:true,missingMessage:'ABC类型不能为空。',editable:false,panelHeight:'auto'">
					<option value="A">A</option>
					<option value="B">B</option>
					<option value="C">C</option>
				</select> <label>单件货值：</label> <input name="gValue" class="easyui-textbox"
					style="width: 145px;"
					data-options="required:false,missingMessage:'单件货值不能为空。'"> <label>发运包装编号：</label>
				<input name="sendBoxid" class="easyui-textbox" style="width: 145px;"
					data-options="required:false,missingMessage:'发运包装编号不能为空。'">
			</div>
			<div class="fitem" id="">

				<label>存储包装类型：</label> <select name="boxType"
					class="easyui-combobox" style="width: 145px;"
					data-options="required:true,missingMessage:'存储包装类型不能为空。',editable:false,panelHeight:'auto'">
					<option value="1">纸包装</option>
					<option value="2">料盒</option>
					<option value="3">大件器具</option>
				</select> <label>存储包装包含数：</label> <input name="boxNum" class="easyui-textbox"
					style="width: 145px;"
					data-options="required:false,missingMessage:'存储包装包含数不能为空。'">
				<label>批次管理：</label> <select name="batchType"
					class="easyui-combobox" style="width: 145px;"
					data-options="required:true,missingMessage:'批次管理不能为空。',editable:false,panelHeight:'auto'">
					<option value="0">不分批次管理</option>
					<option value="1">批次管理</option>
				</select>
			</div>
			<div class="fitem" id="">
				<label>包装规格长度：</label> <input name="packLength"
					class="easyui-textbox" style="width: 145px;"
					data-options="required:false,missingMessage:'包装规格长度不能为空。'">
				<label>包装规格宽度：</label> <input name="packWidth"
					class="easyui-textbox" style="width: 145px;"
					data-options="required:false,missingMessage:'包装规格宽度不能为空。'">
				<label>包装规格高度：</label> <input name="packHeigth"
					class="easyui-textbox" style="width: 145px;"
					data-options="required:false,missingMessage:'包装规格高度不能为空。'">
			</div>
			<div class="fitem" id="">

				<label>包装重量：</label> <input name="packWeigth" class="easyui-textbox"
					style="width: 145px;"
					data-options="required:false,missingMessage:'包装重量不能为空。'"> <label>托盘存储包装数：</label>
				<input name="trayNum" class="easyui-textbox" style="width: 145px;"
					data-options="required:false,missingMessage:'托盘存储包装数不能为空。'">
				<label>托盘收货包装数：</label> <input name="receivetrayNum"
					class="easyui-textbox" style="width: 145px;"
					data-options="required:false,missingMessage:'托盘收货包装数不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>指定库位/库区：</label> <select name="storagezoneType"
					id="storagezoneType" class="easyui-combobox" style="width: 145px;"
					data-options="required:true,missingMessage:'指定库位/库区不能为空。',editable:false,panelHeight:'auto'">
					<option value="0">库区动态存储</option>
					<option value="1">指定库位</option>
				</select> <label>库位/库区编号：</label> <input class="easyui-combobox"
					name="storagezoneId" id="storagezoneId" style="width: 145px;"
					data-options="valueField:'storagecode',textField:'storagecode',multiple:true,url:'',panelHeight:300,editable:false,required:true,missingMessage:'库位/库区编号不能为空。'" />
				<label>最大的库存数：</label> <input name="maxNum" class="easyui-textbox"
					style="width: 145px;"
					data-options="required:true,missingMessage:'最大的库存数不能为空。'">
			</div>
			<div class="fitem" id="">

				<label>零件用途：</label> <select name="gtype" class="easyui-combobox"
					style="width: 145px;"
					data-options="required:true,missingMessage:'零件用途不能为空。',editable:false,panelHeight:'auto'">
					<option value="生产件">生产件</option>
					<option value="备件">备件</option>
					<option value="出口件">出口件</option>
				</select> <label>保质期：</label> <input name="qualityDay" class="easyui-textbox"
					style="width: 145px;"
					data-options="required:false,missingMessage:'保质期不能为空。'"> <label>库位最大库存数：</label>
				<input name="storageMaxNum" class="easyui-textbox"
					style="width: 145px;"
					data-options="required:true,missingMessage:'库位最大库存数不能为空。'">
			</div>
			<div class="fitem" id="">
				<label>叉车数量：</label> <input name="forkliftNum" id="forkliftNum"
					class="easyui-textbox" style="width: 145px;"
					data-options="required:true,missingMessage:'叉车数量不能为空。'" /> <label>搬运直接上架：</label>
				<select name="skipPutaway" class="easyui-combobox"
					style="width: 145px;"
					data-options="required:true,missingMessage:'搬运直接上架不能为空。',editable:false,panelHeight:'auto'">
					<option value="0">是</option>
					<option value="1">否</option>
				</select> <label>内部编码：</label> <input name="innerCoding" id="innerCoding"
					class="easyui-textbox" style="width: 145px;"
					data-options="required:false,missingMessage:'内部编码不能为空。'" /> </select>
			</div>
			<div class="fitem" id="">
				<label>最大预警库存：</label> <input name="warningMaxNum"
					id="warningMaxNum" class="easyui-textbox" style="width: 145px;"
					data-options="required:true,missingMessage:'最大预警库存不能为空。'" /> <label>最小预警库存：</label>
				<input name="warningMinNum" id="warningMinNum"
					class="easyui-textbox" style="width: 145px;"
					data-options="required:true,missingMessage:'最小预警库存不能为空。'" /><label>计量单位：</label>
				<select name="units" class="easyui-combobox" style="width: 145px;"
					data-options="required:true,missingMessage:'计量单位不能为空。',editable:false,panelHeight:'auto'">
					<option value="件">件</option>
					<option value="包">包</option>
					<option value="框">框</option>
					<option value="托盘">托盘</option>
					<option value="盒">盒</option>
				</select>

			</div>
			<input type="hidden" name="oraName" id="oraNames" /> <input
				type="hidden" name="zonecode" id="zonecode" />
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
		Hmgx.RenderButton("butbar",
<%=session.getAttribute("curLoginUserButton")%>
	,
				"${param.pageId}");
	});

	$("#storagezoneId").combobox({
		mode : "remote",
		editable : false
	});

	$("#oraCodes").combobox({
		onSelect : function(param) {
			$("#oraNames").val(param.conName);
		},
		mode : "remote",
		editable : "true"
	});

	//存储包装类型
	function forma_boxType(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 1:
			return "纸包装";
			break;
		case 2:
			return "料盒";
			break;
		case 3:
			return "大件器具";
			break;
		default:
			return "未知";
			break;
		}
	}

	//存储类型
	function forma_storageType(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 1:
			return "托盘";
			break;
		case 2:
			return "堆放";
			break;
		default:
			return "未知";
			break;
		}
	}
	function forma_storagezoneType(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 0:
			return "库区动态存储";
			break;
		case 1:
			return "指定库位";
			break;
		default:
			return "未知";
			break;
		}
	}
	//批次管理
	function forma_batchType(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 0:
			return "不分批次";
			break;
		case 1:
			return "批次管理";
			break;
		default:
			return "未知";
			break;
		}
	}
	//搬运是否上架
	function forma_skipPutaway(val, row) {
		if (null == val) {
			return;
		}
		switch (val) {
		case 0:
			return "是";
			break;
		case 1:
			return "否";
			break;
		default:
			return "未知";
			break;
		}
	}
	//查询
	function query() {
		var gcode = $.trim($('input[name=gcode]').val());
		var oraCode = $.trim($('input[name=oraCode]').val());
		var storagezoneId = $.trim($('input[name=storagezoneId]').val());
		$("#dg").datagrid('reload', {
			gcode : gcode,
			oraCode : oraCode,
			storagezoneId : storagezoneId
		});
	}

	//数据导出
	function exportData() {
		var gcode = $.trim($('input[name=gcode]').val());
		var params = Hmgx.getQueryParamet("queryForm");
		Hmgx.serializeDownload("${app}/goods/export.action", "", params);
	}
	var url;
	//新增
	function add() {
		$('#login-name').css('display', '');
		$('#dlg').dialog('open').dialog('setTitle', '新增物料').window("center");
		$('#fm').form('clear');
		$("#forkliftNum").textbox('setValue', '0');
		url = '${app}/goods/save.action';
	}

	//修改
	function edit() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要修改的数据！");
		if (!row) {
			return;
		}
		$('#dlg').dialog('open').dialog('setTitle', '修改物料');
		$('#fm').form('load', row);
		url = '${app}/goods/save.action?id=' + row.id;
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
		$.messager.confirm('删除确认', '您确定要删除该物料?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/goods/del.action', {
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

	$('#storagezoneType').combobox({
		onSelect : function(rec) {
			var url = null;
			if (rec.value == 1) {
				url = "${app}/storag/getComboBoxStorage.action";
				valueField = "storagecode";
				textField = "storagecode";
			} else if (rec.value == 0) {
				url = "${app}/zone/getCombobox.action";
				valueField = "zonecode";
				textField = "zonecode";
			} else {
				url = "";
			}
			$('#storagezoneId').combobox({
				url : url,
				valueField : valueField,
				textField : textField
			});
		}
	});
	$("#dg").datagrid({
		onLoadError : function() {
			hideProgress();
			$.messager.alert('错误信息', '操作超时,请刷新页面重新操作', 'error');
		}
	});
</script>