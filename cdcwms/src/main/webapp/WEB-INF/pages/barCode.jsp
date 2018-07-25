<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<body class="easyui-layout">
	<div class="fitem" id="">
		<label>条码内容：</label>
		<input name="code" id="code" class="easyui-textbox" value="ymt-code" style="width: 145px;" data-options="required:false" />
		<label>长：</label>
		<input name="height" id="height" class="easyui-textbox" value="100" style="width: 145px;" data-options="required:false" />
		<label>宽：</label>
		<input name="width" id="width" class="easyui-textbox" value="100" style="width: 145px;" data-options="required:false" />
		<label>条码类型：</label>
		<select name="type" id="type" class="easyui-combobox" style="width: 145px;" data-options="required:false,editable:false">
			<option value="1">一维条码</option>
			<option value="2">二维条码</option>
		</select>
	</div>
	<div class="fitem" id="">
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="execute()">生成条码</a>
	</div>
	<div align="center">
		<img alt="生成条码" id="image" />
	</div>
	<script type="text/javascript">
		function execute() {
			$("#image").attr(
					"src",
					"${app}/barCode/getImage?code=" + $("#code").val() + "&height=" + $("#height").val() + "&width=" + $("#width").val() + "&type="
							+ $("#type").combobox("getValue") + "&date=" + (new Date()).getTime());
		}
	</script>
</body>
</html>