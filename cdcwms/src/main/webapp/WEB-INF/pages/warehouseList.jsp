<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'center',title:'',border:false"
		style="padding: 5px;">

		<table id="dg" title="仓库管理 " class="easyui-datagrid"
			data-options="remoteSort:false,url:'${app}/warehouse/list.action',fit:true,rownumbers:true,toolbar:'#toolbar',singleSelect:true,pagination:true,border:true">
			<thead>
				<tr>
					<th data-options="sortable:true,field:'whCode',width:'100px'">仓库代码</th>
					<th data-options="sortable:true,field:'whName',width:'200px'">仓库名称</th>
					<th data-options="sortable:true,field:'sendStorage',width:'150px'">发货区库位</th>
					<th data-options="sortable:true,field:'spillZone',width:'150px'">溢库区</th>
					<th data-options="sortable:true,field:'photo',width:'200px'">仓库正面照片路径</th>
					<th data-options="sortable:true,field:'useArea',width:'100px'">实用面积</th>
					<th data-options="sortable:true,field:'wcountry',width:'100px'">国家</th>
					<th data-options="sortable:true,field:'wprovince',width:'100px'">省</th>
					<th data-options="sortable:true,field:'wcity',width:'100px'">城市</th>
					<th data-options="sortable:true,field:'waddress',width:'100px'">地址</th>
					<th data-options="sortable:true,field:'wpostcode',width:'100px'">邮编</th>
					<th data-options="sortable:true,field:'wcontact',width:'100px'">联系人</th>
					<th data-options="sortable:true,field:'wmobile',width:'100px'">联系人号码</th>
					<th data-options="sortable:true,field:'wphone',width:'100px'">仓库电话</th>
					<th data-options="sortable:true,field:'wfax',width:'100px'">传真</th>
					<th data-options="sortable:true,field:'wemail',width:'100px'">电子邮件</th>
					<th data-options="sortable:true,field:'longitude',width:'100px'">经度</th>
					<th data-options="sortable:true,field:'latitude',width:'100px'">纬度</th>
					<th data-options="sortable:true,field:'descrip',width:'100px'">描述</th>
					<th data-options="sortable:true,field:'companyid',width:'100px'">运行公司编码</th>
					<th data-options="sortable:true,field:'centerid',width:'100px'">业务中心编码</th>
					<th data-options="sortable:true,field:'startTime',width:'100px'">仓库启动时间</th>
					<th data-options="sortable:true,field:'expiredTime',width:'100px'">仓库到期时间</th>
					<th
						data-options="sortable:true,field:'receivebeginTime',width:'100px'">每日收货开始时间</th>
					<th
						data-options="sortable:true,field:'receiveendTime',width:'100px'">每日收货结束时间</th>
					<th
						data-options="sortable:true,field:'sendbeginTime',width:'100px'">每日发货开始时间</th>
					<th data-options="sortable:true,field:'sendendTime',width:'100px'">每日发货结束时间</th>
					<th data-options="sortable:true,field:'updateUser',width:'100px'">最后修改人</th>
					<th data-options="sortable:true,field:'updateTime',width:'200px'">最后修改时间</th>
				</tr>
			</thead>
		</table>
	</div>

	<div id="toolbar"></div>
	<!-- 保存或修改对话框 -->
	<div id="dlg" class="easyui-dialog" style="width: 900px"
		data-options="closed:true,modal:true,buttons:'#dlg-buttons'">
		<form id="fm" method="post">
			<div class="fitem" id="">
				<label>仓库代码：</label> <input name="whCode" id="whCode"
					class="easyui-textbox" style="width: 145px;"
					data-options="required:true,missingMessage:'仓库代码不能为空。'"> <label>仓库名称：</label>
				<input name="whName" class="easyui-textbox" style="width: 145px;"
					data-options="required:true,missingMessage:'所属仓库编码不能为空。'">
				<label>仓库正面照片路径：</label> <input name="photo" id="photo"
					class="easyui-textbox" style="width: 145px;"
					data-options="required:false,missingMessage:'仓库正面照片路径不能为空。'">
			</div>

			<div class="fitem" id="">
				<label>溢库区：</label> <input class="easyui-combobox" name="spillZone"
					id="spillZone" style="width: 145px;"
					data-options="valueField:'storagecode',textField:'storagecode',url:'${app}/storag/getComboBoxStorageCode.action',editable:false,panelHeight:'auto',missingMessage:'请选择溢库区。'" />
				<label>发货区库位：</label> <input class="easyui-combobox"
					name="sendStorage" id="sendStorage" style="width: 145px;"
					data-options="valueField:'storagecode',textField:'storagecode',url:'${app}/storag/getComboBoxStorageCode.action',editable:false,panelHeight:'auto',missingMessage:'请选择发货区库位。'" />
				<label>实用面积：</label> <input name="useArea" class="easyui-textbox"
					style="width: 145px;"
					data-options="required:false,missingMessage:'实用面积不能为空。'">
			</div>

			<div class="fitem" id="">
				<label>国家：</label> <input name="WCountry" class="easyui-textbox"
					style="width: 145px;"
					data-options="required:false,missingMessage:'国家不能为空。'"> <label>省：</label>
				<input name="WProvince" class="easyui-textbox" style="width: 145px;"
					data-options="required:false,missingMessage:'省不能为空。'"> <label>城市：</label>
				<input name="WCity" class="easyui-textbox" style="width: 145px;"
					data-options="required:false,missingMessage:'城市不能为空。'">

			</div>

			<div class="fitem" id="">
				<label>地址：</label> <input name="WAddress" class="easyui-textbox"
					style="width: 145px;"
					data-options="required:false,missingMessage:'地址不能为空。'"> <label>邮编：</label>
				<input name="WPostcode" class="easyui-numberbox"
					style="width: 145px;"
					data-options="required:false,missingMessage:'邮编不能为空。'"> <label>联系人：</label>
				<input name="WContact" class="easyui-textbox" style="width: 145px;"
					data-options="required:false,missingMessage:'联系人不能为空。'">
			</div>

			<div class="fitem" id="">
				<label>联系人号码：</label> <input name="WMobile" class="easyui-numberbox"
					style="width: 145px;"
					data-options="required:false,missingMessage:'联系人号码不能为空。'">
				<label>仓库电话：</label> <input name="WPhone" class="easyui-numberbox"
					style="width: 145px;"
					data-options="required:false,missingMessage:'仓库电话不能为空。'"> <label>传真：</label>
				<input name="WFax" class="easyui-numberbox" style="width: 145px;"
					data-options="required:false,missingMessage:'传真不能为空。'">
			</div>

			<div class="fitem" id="">
				<label>电子邮件：</label> <input name="WEmail" class="easyui-textbox"
					style="width: 145px;"
					data-options="required:false,missingMessage:'电子邮件不能为空。'"> <label>经度：</label>
				<input name="longitude" class="easyui-numberbox"
					style="width: 145px;"
					data-options="required:false,missingMessage:'经度不能为空。',precision:6">
				<label>纬度：</label> <input name="latitude" class="easyui-numberbox"
					style="width: 145px;"
					data-options="required:false,missingMessage:'纬度不能为空。',precision:6">
			</div>
			<div class="fitem" id="">
				<label>运行公司编码：</label> <input name="companyid"
					class="easyui-textbox" style="width: 145px;"
					data-options="required:false,missingMessage:'运行公司编码不能为空。'">

				<label>业务中心编码：</label> <input name="centerid" class="easyui-textbox"
					style="width: 145px;"
					data-options="required:false,missingMessage:'业务中心编码不能为空。'">

				<label>仓库启动时间：</label><input class="easyui-datebox" name="startTime"
					data-options="formatter:date_matter,labelPosition:'top',required:true,missingMessage:'请选择仓库启动时间。'"
					style="width: 145px;">
			</div>

			<div class="fitem" id="">
				<label>合同到期时间：</label><input class="easyui-datebox"
					name="expiredTime"
					data-options="formatter:date_matter,labelPosition:'top',required:true,missingMessage:'请选择合同到期时间。'"
					style="width: 145px;"> <label>收货开始时间：</label> <input
					name="receivebeginTime" class="easyui-timespinner"
					style="width: 145px;"
					data-options="required:true,missingMessage:'请选择收货开始时间。'"> <label>收货结束时间：</label>
				<input name="receiveendTime" class="easyui-timespinner"
					style="width: 145px;"
					data-options="required:true,missingMessage:'请选择收货结束时间。'">
			</div>

			<div class="fitem" id="">
				<label>发货开始时间：</label> <input name="sendbeginTime"
					class="easyui-timespinner" style="width: 145px;"
					data-options="required:true,missingMessage:'请选择发货开始时间。'"> <label>发货结束时间：</label>
				<input name="sendendTime" class="easyui-timespinner"
					style="width: 145px;"
					data-options="required:true,missingMessage:'请选择发货结束时间。'"> <label>描述：</label>
				<input name="descrip" class="easyui-textbox" style="width: 145px;"
					data-options="required:false,missingMessage:'描述不能为空。'">
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

	//发货区库位
	$("#sendStorage").combobox({
		mode : "remote",
		editable : "true"
	});
	//溢库区
	$("#spillZone").combobox({
		mode : "remote",
		editable : "true"
	});
	//格式化日期
	function date_matter(date) {
		var y = date.getFullYear();
		var m = date.getMonth() + 1;
		var d = date.getDate();
		return y + '-' + (m < 10 ? ('0' + m) : m) + '-'
				+ (d < 10 ? ('0' + d) : d);
	}
	//格式化日期时间
	function dateTime_matter(date) {
		var y = date.getFullYear();
		var m = date.getMonth() + 1;
		var d = date.getDate();
		var h = date.getHours();
		return y + '-' + (m < 10 ? ('0' + m) : m) + '-'
				+ (d < 10 ? ('0' + d) : d) + " " + (h < 10 ? '0' + h : h) + ":"
				+ date.getMinutes();
	}

	var url;
	//新增
	function add() {
		$('#dlg').dialog('open').dialog('setTitle', '新增仓库').window("center");
		$('#fm').form('clear');
		$("#whCode").textbox('enable');
		url = '${app}/warehouse/save.action';
	}

	//修改
	function edit() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要修改的数据！");
		if (!row) {
			return;
		}
		$('#dlg').dialog('open').dialog('setTitle', '修改仓库').window("center");
		row.WCountry = row.wcountry;
		row.WProvince = row.wprovince;
		row.WCity = row.wcity;
		row.WAddress = row.waddress;
		row.WPostcode = row.wpostcode;
		row.WContact = row.wcontact;
		row.WMobile = row.wmobile;
		row.WPhone = row.wphone;
		row.WFax = row.wfax;
		row.WEmail = row.wemail;
		$('#fm').form('load', row);
		$("#whCode").textbox('disable');
		url = '${app}/warehouse/save.action?id=' + row.id;
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
		});
	}

	//删除
	function del() {
		var row = Hmgx.GetSelectRow("dg", "请选择需要删除的数据！");
		if (!row) {
			return;
		}
		$.messager.confirm('删除确认', '您确定要删除该仓库?', function(r) {
			if (r) {
				showHandleProgress();
				$.post('${app}/warehouse/del.action', {
					id : row.id
				}, function(result) {
					hideProgress();
					if ('-1' == result.retcode) {
						$.messager.alert('错误信息', result.retmsg, 'error');
					} else {
						$('#dg').datagrid('reload');
					}
				}, "json");
			}
		});
	}
</script>