<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<style>
</style>
<body class="easyui-layout">

	<div data-options="region:'center',title:'',border:false"style="padding: 5px; background-color: #dddddd">
		<form id="fmImport" method="post" enctype="multipart/form-data">
		<br>
			<input type="hidden" id="keyNo" name="keyNo" value="${param.keyNo }">
			<input name="excelFile" id="excelFile"
				style="width:300px" class="easyui-filebox"
				data-options="buttonText:'选择文件',required:true,missingMessage:'请导入.xls\.xlsx格式的文件'" />
			<a href="javascript:;"  onclick="submitImport()" class="easyui-linkbutton" data-options="iconCls:'icon-add'">导入</a>
		</form>
		<div style="color: Orange;text-align: center; font-size: 24px;"><br>注意:当数据导入成功后,请手动刷新以显示本次导入的结果！</div>
	</div>



</body>

<script type="text/javascript">
	
	//提交导入EXCEL文件
	function submitImport() {
		$('#fmImport').form('submit', {
			url : "${app}/excel/importExcel.action",
			onSubmit : function() {
				if($("#keyNo").val() == ""){
					$.messager.alert('错误信息', "表格列数据不能为空!", 'error');
					return false;
				}
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
					$.messager.alert('导入成功', result.retmsg, 'info',function(){
						parent.closeImport();
					});
				}
			}
		});
	}
	 
</script>