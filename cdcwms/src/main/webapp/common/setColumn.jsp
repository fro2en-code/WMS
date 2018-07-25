<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="zxmm" class="easyui-menu" style="width: 120px">
	<div id="zxmm-tabclose" data-options="iconCls:'icon-add'" onclick="setSet()">自定义表表</div>
</div>
<div id="zxdlg" class="easyui-dialog" title="" style="width: 600px; height: 400px" data-options="closed:true,modal:true,buttons:'#dlg-buttons'">
	<table id="zxdg" title="自定义表格设置" class="easyui-datagrid" data-options="fit:true,rownumbers:true,border:true">
		<thead>
			<tr>
				<th data-options="field:'columnName',width:'300px'">列名</th>
				<th data-options="field:'show',formatter:zxFormatter">是否显示</th>
				<th data-options="field:'up'">操作</th>
				<th data-options="field:'down'">操作</th>
			</tr>
		</thead>
	</table>
</div>
<div id="dlg-buttons">
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="zxSave()">保存</a>
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="javascript:$('#zxdlg').dialog('close')">取消</a>
</div>

<script type="text/javascript">
	var zxDgid;
	var zxKey;
	function addSet(dgID, key, fun) {
		$(dgID).datagrid({
			onHeaderContextMenu : function(e, rowIndex, rowData) {
				zxDgid = dgID;
				zxKey = key;
				e.preventDefault();
				$('#zxmm').menu('show', {
					left : e.pageX,
					top : e.pageY
				});
			}
		});
		//
		$.post('${app}/userDatagridColumn/getUserDatagridColumn.action', {
			key : key
		}, function(result) {
			hideProgress();
			if ('-1' == result.retcode) {
				$.messager.alert('错误信息', result.retmsg, 'error');
			} else {
				if (null == result.t || null == result.t.column) {
					if (null != fun) {
						fun();
					}
					return;
				}
				zxDgid = dgID;
				zxKey = key;
				var column = result.t.column.split(",");
				var columns = $(zxDgid).datagrid("options").columns[0];
				var newColumns = new Array();
				for (var i = 0; i < column.length; i++) {
					var rowData = column[i];
					for (var j = 0; j < columns.length; j++) {
						if (null != columns[j] && columns[j].field == rowData) {
							columns[j].hidden = false;
							newColumns[newColumns.length] = columns[j];
							columns[j] = null;
							break;
						}
					}
				}
				//
				for (var j = 0; j < columns.length; j++) {
					if (null != columns[j] && columns[j].field != null) {
						columns[j].hidden = true;
						newColumns[newColumns.length] = columns[j];
					}
				}
				var data = $(dgID).datagrid("getData");
				$(dgID).datagrid({
					columns : [ newColumns ],
					data : [ [] ]
				});
				$(dgID).datagrid({
					data : [ data ]
				});
				if (null != fun) {
					fun();
				}
			}
		}, "json");
	};

	function setSet() {
		$('#zxdlg').dialog('open').window("center");
		var columns = $(zxDgid).datagrid("options").columns[0];
		var data = new Array();
		for (var i = 0; i < columns.length; i++) {
			data[i] = {
				columnKey : columns[i].field,
				columnName : columns[i].title,
				show : columns[i].hidden == true ? false : true,
				up : "上移",
				down : "下移"
			};
		}
		$("#zxdg").datagrid({
			data : data,
			onClickCell : function(index, field, value) {
				var zxData = $("#zxdg").datagrid("getData");
				var zxRow = zxData.rows[index];
				if (field == "up") {
					var temp = zxData.rows[index - 1];
					zxData.rows[index] = temp;
					zxData.rows[index - 1] = zxRow;
					$("#zxdg").datagrid('refreshRow', index);
					$("#zxdg").datagrid('refreshRow', index - 1);
				} else if (field == "down") {
					var temp = zxData.rows[index + 1];
					zxData.rows[index] = temp;
					zxData.rows[index + 1] = zxRow;
					$("#zxdg").datagrid('refreshRow', index);
					$("#zxdg").datagrid('refreshRow', index + 1);
				} else if (field == "show") {
					zxRow.show = !zxRow.show;
					$("#zxdg").datagrid('refreshRow', index);
				}
			}
		});
	}

	function zxFormatter(value) {
		if (value == true) {
			return "<input type=\"checkbox\" checked=\"checked\" value=\"true\" >";
		} else {
			return "<input type=\"checkbox\" value=\"true\" >";
		}
	}

	function zxSave() {
		$('#zxdlg').dialog('close');
		var zxData = $("#zxdg").datagrid("getData").rows;
		var columns = $(zxDgid).datagrid("options").columns[0];
		var newColumns = new Array();
		var valueColumns = new Array();
		for (var i = 0; i < zxData.length; i++) {
			var rowData = zxData[i];
			if (rowData.show) {
				for (var j = 0; j < columns.length; j++) {
					if (null != columns[j] && columns[j].field == rowData.columnKey) {
						columns[j].hidden = false;
						newColumns[newColumns.length] = columns[j];
						valueColumns[valueColumns.length] = columns[j].field;
						columns[j] = null;
						break;
					}
				}
			}
		}
		//
		for (var j = 0; j < columns.length; j++) {
			if (null != columns[j] && columns[j].field != null) {
				columns[j].hidden = true;
				newColumns[newColumns.length] = columns[j];
			}
		}
		var data = $(zxDgid).datagrid("getData");
		$(zxDgid).datagrid({
			columns : [ newColumns ],
			data : [ [] ]
		});
		$(zxDgid).datagrid({
			data : [ data ]
		});
		$.post('${app}/userDatagridColumn/save.action', {
			column : valueColumns.join(","),
			key : zxKey
		}, function(result) {
			hideProgress();
			if ('-1' == result.retcode) {
				$.messager.alert('错误信息', result.retmsg, 'error');
			}
		}, "json");
	}
</script>
