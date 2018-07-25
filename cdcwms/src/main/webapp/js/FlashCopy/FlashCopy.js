// FLASH COPY 需要JQUERY支持

var FlashCopy = function(MoviePath,DivID,SwfID,width,height,left,top,IsCreate){
	//SWF路径
	if(MoviePath==""||MoviePath===undefined){
		this.MoviePath = 'FlashCopy.swf';
	}
	else{
		this.MoviePath = MoviePath;
	}
	//DIV容器ID
	if(DivID==""||DivID===undefined){
		this.DivID = "CopyDiv";
	}
	else{
		this.DivID = DivID;
	}
	//SWF ID
	if(SwfID==""||SwfID===undefined){
		this.SwfID = "CopySwf";
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
	//容器X坐标
	if(left==""||left===undefined){
		this.left =1;
	}
	else{
		this.left =left;
	}
	//容器Y坐标
	if(top==""||top===undefined){
		this.top =1;
	}
	else{
		this.top =top;
	}
	//是否创建HTML
	if(IsCreate){
		this.CreateSwf();	
	}
	this.Ver = "1.0";
	//获取实例名称 
	//如果需要SWF调用此实例则必须 window.Instance = new FlashCopy(); 这样实例化
	//如果不需要SWF调用此实例 则可以 var Instance = new FlashCopy(); 这样实例化
	this.GetClassName=function(){
		for (var ClassName in window){
			if (window[ClassName]==this){
				return ClassName;
				break;
			}
		}
	};
};

//创建HTML代码
FlashCopy.prototype.CreateSwf=function(){
	//同一DivID容器只创建一次
	if($("#" + this.DivID).length>0){
		return false;
	}
	var Html = "<div style='position:absolute;border:0px solid;z-index:999999; overflow:hidden;left:" + this.left + "px;top:" + this.top + "px; width:" + this.width + "; height:" + this.height + ";' id='" + this.DivID + "'>";
	//object 里不要加 name= 属性，否则在 火狐下 提示找不到SWF的函数
	//SWF 默认大小为1 ，如果为0 或DIV的LEFT 位置为不可见时 火狐 提示找不到SWF的函数
	Html += "<object id='" + this.SwfID + "' classid='clsid:D27CDB6E-AE6D-11cf-96B8-444553540000' width='" + this.width + "' height='" + this.height + "' >";
	Html += "<param name='movie' value='" + this.MoviePath + "'>";
	Html += "<param name='quality' value='high'>";
	Html += "<param name='wmode' value='opaque'>";
	Html += "<param name='wmode' value='transparent'>";
	Html += "<param name='swfversion' value='9.0.45.0'>";
	Html += "<embed play='true' swliveconnect='true' name='" + this.SwfID + "' src='" + this.MoviePath + "' quality='high' ";
	Html += "bgcolor='#003366' width='" + this.width + "' height='" + this.height + "' type='application/x-shockwave-flash' wmode='transparent'/>";
	Html += "</object>";
	Html += "</div>";
	$(document.body).append(Html); 
};

//获取SWF对象
FlashCopy.prototype.GetSwf=function (){
	//alert(this.SwfID);
	if (window.document[this.SwfID]){
		return window.document[this.SwfID];
	}
	if (navigator.appName.indexOf("Microsoft Internet")==-1){
		if (document.embeds && document.embeds[this.SwfID]){
			return document.embeds[this.SwfID];
		}
	}
	else {
		return document.getElementByIdx_x_x(this.SwfID);
	}
};

//设置SWF路径
FlashCopy.prototype.SetMoviePath=function(MoviePath){
	if(MoviePath==""||MoviePath===undefined){		
		alert("[SetMoviePath]参数不能为这空！");
	}
	else{
		this.MoviePath = MoviePath;
	}
};

//设置DIV容器ID
FlashCopy.prototype.SetDivID=function(DivID){
	if(DivID==""||DivID===undefined){		
		alert("[SetDivID]参数不能为这空！");
	}
	else{
		this.DivID = DivID;
	}
};

//设置Swf ID
FlashCopy.prototype.SetSwfID=function(SwfID){
	if(SwfID==""||SwfID===undefined){		
		alert("[SetDivID]参数不能为这空！");
	}
	else{
		this.SwfID = SwfID;
	}
};

//设置宽度
FlashCopy.prototype.SetWidth=function(width){
	if(!isNaN(width)){
		this.width = width;
	}
	else{
		alert("[SetWidth]参数必须为数字！");
	}
};

//设置高度
FlashCopy.prototype.SetHeight=function(height){
	if(!isNaN(height)){
		this.height = height;
	}
	else{
		alert("[SetHeight]参数必须为数字！");
	}
};

//设置X坐标
FlashCopy.prototype.SetX=function(left){
	if(!isNaN(left)){
		this.left = left;
	}
	else{
		alert("[SetX]参数必须为数字！");
	}
};

//设置Y坐标
FlashCopy.prototype.SetY=function(top){
	if(!isNaN(top)){
		this.top = top;
	}
	else{
		alert("[SetY]参数必须为数字！");
	}
};

//设置需要复制的内容
FlashCopy.prototype.SetText=function(CopyStr){
	//setTimeout(function(){FlashCopy.setText("调用时必须延时调用，否则失败！");},100); 
	
	//获取实例名称 SWF根据传入的实例名称 处理点击SWF后是否自动隐藏
	var InstanceName = this.GetClassName();
	//alert(InstanceName);
	this.GetSwf().setText(CopyStr,InstanceName);	
};

//显示
FlashCopy.prototype.show=function(Delay,w,h,x,y){
	try{
		//设置SWF大小
		this.GetSwf().width = w;
		this.GetSwf().height = h;
		//DIV容器的大小 与 位置	
		$("#" + this.DivID).css({width:w,height:h,left:x,top:y,opacity:'0'});
		//渐变显示
		$("#" + this.DivID).animate({opacity: '1'},Delay);
	}catch (e) {
		alert(e.message); 
		return false;
	} 
};

//隐藏
FlashCopy.prototype.hide=function(Delay){	
	//渐变隐藏
	$("#" + this.DivID).animate({opacity: '0'}, Delay ,function(){
		//不隐藏，只缩小为1像素。如果隐藏了JS就获取不到SWF函数了
		$(this).css({width:1,height:1});
	});
};