/*
 * data 2016.7.21
 * function js扩展tool类
 * author hmgx
 */


var Hmgx = {
    showParam:false,
    openWin: function (url, name, iWidth, iHeight) {//功能:弹出窗口
        if (typeof(iWidth) === "undefined") {
            iWidth = 720;
        }
        if (typeof(iHeight) === "undefined") {
            iHeight = 620;
        }
        //获得窗口的垂直位置
        var iTop = (window.screen.availHeight - 30 - iHeight) / 2;
        //获得窗口的水平位置
        var iLeft = (window.screen.availWidth - 10 - iWidth) / 2;
        window.open(url, name, 'height=' + iHeight + ',,innerHeight=' + iHeight + ',width=' + iWidth + ',innerWidth=' + iWidth + ',top=' + iTop + ',left=' + iLeft + ',status=no,toolbar=no,menubar=no,location=no,resizable=no,scrollbars=1,titlebar=no');
    },
    getQueryParamet: function (FormId, ParamObj) {//功能:将表单字段转换为object
        if (typeof(ParamObj) !== "object") {
            ParamObj = {};
        }
        $.each($("#" + FormId).serializeArray(), function (index, param) {
            ParamObj[param.name] = param.value;
        });
        return ParamObj;
    },
    serializeDownload: function (Url, FormId, AddedParam, DownloadIframe) {//功能:下载
        if (typeof(DownloadIframe) === "undefined") {
            DownloadIframe = "_new";
        }
        var form = $('<form />', {
            action: Url,
            method: "post",
            target: DownloadIframe,
            style: "display:none;"
        }).appendTo('body');
        //处理表单参数
        $.each($('#' + FormId).serializeArray(), function (index) {
            if(Hmgx.showParam)alert( this['name']  + "=" + this['value']);
            form.append('<input type="hidden" name="' + this['name'] + '" value="' + this['value'] + '" />');
        });
        //处理附加参数
        if (typeof(AddedParam) === "object") {
            for (var Key in AddedParam) {
                if(Hmgx.showParam)alert( Key  + "=" + AddedParam[Key]);
                form.append('<input type="hidden" name="' + Key + '" value="' + AddedParam[Key] + '" />');
            }
        }
        form.submit();
        setTimeout(function () {
            form.remove();
        }, 100);
    },
    GetSelectRow:function (TabId, TipsMsg) {//功能:获取选择的行数据,并将index值插入到行数据[TabId=表格ID TipsMsg=当未选择数据行时，提示信息]
		if (typeof (TabId) == "undefined") {
			$.messager.alert('温馨提示', "<br>请输入需要获取的表格ID值！", 'error');
			return false;
		}
		if (typeof (TipsMsg) == "undefined") {
			TipsMsg = '<br>您未选择数据行！';
		}
		var Row = $("#" + TabId).datagrid('getSelected');
		if (!Row) {
			$.messager.alert('温馨提示', TipsMsg, 'error');
			return false;
		}
		Row.Index = $("#" + TabId).datagrid('getRowIndex', Row);
		return Row;
	},
	RenderButton:function(elementId,btnJson,pageId){//功能:渲染按钮
		var btnHtml= "";
		if(typeof(btnJson[pageId]) === "undefined")
			return;		
		$.each(btnJson[pageId],function(i,b){
			btnHtml+= "<a href='javascript:;' class='easyui-linkbutton' iconCls='";
			btnHtml+= b.btnIcon + "' plain='true' onclick=\"";
			btnHtml+= b.btnClick + "\">" + b.btnName +  "</a>";
		});
		$("#" + elementId).html(btnHtml);
		$.parser.parse("#" + elementId);
	},
	w:function(){//功能:可见宽度
		return window.innerWidth || document.documentElement && document.documentElement.clientWidth || document.body.clientWidth;
	},
	h:function(){//功能:可见高度
		return window.innerHeight || document.documentElement && document.documentElement.clientHeight || document.body.clientHeight;
	},
	E_OpenWin:function (WinName,IframeName,IframeSrc,Title,Ww,Hh,fn){//功能：打开窗口Easyui
		if(!Ww)Ww=800;
		if(!Hh)Hh=700;
		var w = Hmgx.w()<Ww?Hmgx.w():Ww;
		var h = Hmgx.h()<Hh?Hmgx.h():Hh;
		var t = h==Hh?(Hmgx.h() - Hh)/2:0;
		var l = w==Ww?(Hmgx.w() - Ww)/2:0;
		
		$(WinName).window({
			title:Title,width:w,height:h,modal:true,top:t,left:l,onBeforeClose:function()
			{$(IframeName).attr('src','');eval(fn);},collapsible:false,minimizable:false,maximizable:false,resizable:true
		});
		if(IframeName!=""){
			$(IframeName).attr('src',IframeSrc);
		}
		$(WinName).window('open');
	},
	E_CloseWin:function (WinName,MsgStr,fn){//功能： 关闭窗口Easyui
		if(fn){eval(fn);}
		if(MsgStr !== undefined && MsgStr != "" )
		{
			$.messager.alert('温馨提示',MsgStr,'info',function(){$(WinName).window('close');});
		}	else	{
			$(WinName).window('close');
		}
	}
};
