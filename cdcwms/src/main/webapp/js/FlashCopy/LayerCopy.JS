// FLASH COPY 需要JQUERY支持

var FlashCopy = function(MoviePath,DivID,SwfID,width,height,IsCreate){
	//SWF路径
	if(MoviePath==""||MoviePath===undefined){
		this.MoviePath = 'LayerCopy.swf';
	}
	else{
		this.MoviePath = MoviePath;
	}	
	// 放置SWF 容器ID
	if(DivID==""||DivID===undefined){
		this.DivID = $(document.body); 
	}else{
		this.DivID = $("#" + DivID);
	}
	//SWF ID
	if(SwfID==""||SwfID===undefined){
		this.SwfID = "LayerCopySwf";
	}
	else{
		this.SwfID = SwfID;
	}
	//宽度
	if(width==""||width===undefined){
		this.width =1;
	}
	else{
		this.width =width;
	}
	//高度
	if(height==""||height===undefined){
		this.height =1;
	}
	else{
		this.height =width;
	}	
	//是否创建HTML
	if(IsCreate){
		this.CreateSwf();	
	}
	this.Ver = "1.0";
	this.SwfObj = null;
}

//创建HTML代码
FlashCopy.prototype.CreateSwf=function(){
	//object 里不要加 name= 属性，否则在 火狐下 提示找不到SWF的函数
	//SWF 默认大小为1 ，如果为0 或DIV的LEFT 位置为不可见时 火狐 提示找不到SWF的函数
	var Html = "<object id='" + this.SwfID  + "' classid='clsid:D27CDB6E-AE6D-11cf-96B8-444553540000' height='" + this.width + "' width='" + this.height + "'>";
	Html += "<param name='movie' value='" + this.MoviePath + "'>" ;
	Html += "<param name='quality' value='high'>" ;
	Html += "<param name='swfversion' value='9.0.45.0'>";
	Html += "<param name='wmode' value='transparent' />";
	Html += "<embed play='true' swliveconnect='true' name='" + this.SwfID + "' src='" + this.MoviePath + "' quality='high' ";
	Html += "bgcolor='#003366' width='" + this.width + "' height='" + this.height + "' type='application/x-shockwave-flash' wmode='transparent'/>";
	Html += "</object>";
	this.DivID.append(Html)
}

//获取SWF对象
FlashCopy.prototype.GetSwf=function (){
	if(this.SwfObj !=null){
		return this.SwfObj;
	}
	if (window.document[this.SwfID]){
		this.SwfObj = window.document[this.SwfID];
		return this.SwfObj;
	}
	if (navigator.appName.indexOf("Microsoft Internet")==-1){
		if (document.embeds && document.embeds[this.SwfID]){
			this.SwfObj = document.embeds[this.SwfID];
			return this.SwfObj;
		}
	}
	else {
		this.SwfObj = document.getElementByIdx_x_x(this.SwfID);
		return this.SwfObj;
	}
	return null
}

//设置SWF路径
FlashCopy.prototype.SetMoviePath=function(MoviePath){
	if(MoviePath==""||MoviePath===undefined){		
		alert("[SetMoviePath]参数不能为这空！");
	}
	else{
		this.MoviePath = MoviePath;
	}
}

//设置放置 SWF的容器ID
FlashCopy.prototype.SetDivID=function(DivID){
	if(DivID==""||DivID===undefined){		
		alert("[SetDivID]参数不能为这空！");
	}
	else{
		this.DivID = DivID;
	}
}

//设置Swf ID
FlashCopy.prototype.SetSwfID=function(SwfID){
	if(SwfID==""||SwfID===undefined){		
		alert("[SetDivID]参数不能为这空！");
	}
	else{
		this.SwfID = SwfID;
	}
}

//设置宽度
FlashCopy.prototype.SetWidth=function(width){
	if(!isNaN(width)){
		this.width = width;
	}
	else{
		alert("[SetWidth]参数必须为数字！");
	}
}

//设置高度
FlashCopy.prototype.SetHeight=function(height){
	if(!isNaN(height)){
		this.height = height;
	}
	else{
		alert("[SetHeight]参数必须为数字！");
	}
}

//设置需要复制的内容[ 复制的内容 JS回调  SWF 回调]
FlashCopy.prototype.SetText=function(CopyStr,JsCallBack,SwfCallBack){
	//setTimeout(function(){Copy.SetText("调用时必须延时调用,否则失败!","testFun('Js回调')","testFun('Swf回调')");},500);
	// testFun 回调函数需要自已定义
	if(this.GetSwf() == null && typeof(this.GetSwf()) == "undefined" ){
		alert("获取SWF对象失败,请检查!");
		return	
	}
	try{
		this.GetSwf().setText(CopyStr,SwfCallBack);
		if(JsCallBack){
			eval(JsCallBack);
		}
	}catch (e){
		return false;
		//alert("调用函数错误!" + e.description);
	}		
}