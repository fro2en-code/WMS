// FLASH COPY ��ҪJQUERY֧��

var FlashCopy = function(MoviePath,DivID,SwfID,width,height,IsCreate){
	//SWF·��
	if(MoviePath==""||MoviePath===undefined){
		this.MoviePath = 'LayerCopy.swf';
	}
	else{
		this.MoviePath = MoviePath;
	}	
	// ����SWF ����ID
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
	//����
	if(width==""||width===undefined){
		this.width =1;
	}
	else{
		this.width =width;
	}
	//�߶�
	if(height==""||height===undefined){
		this.height =1;
	}
	else{
		this.height =width;
	}	
	//�Ƿ񴴽�HTML
	if(IsCreate){
		this.CreateSwf();	
	}
	this.Ver = "1.0";
	this.SwfObj = null;
}

//����HTML����
FlashCopy.prototype.CreateSwf=function(){
	//object �ﲻҪ�� name= ���ԣ������� ����� ��ʾ�Ҳ���SWF�ĺ���
	//SWF Ĭ�ϴ�СΪ1 �����Ϊ0 ��DIV��LEFT λ��Ϊ���ɼ�ʱ ��� ��ʾ�Ҳ���SWF�ĺ���
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

//��ȡSWF����
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

//����SWF·��
FlashCopy.prototype.SetMoviePath=function(MoviePath){
	if(MoviePath==""||MoviePath===undefined){		
		alert("[SetMoviePath]��������Ϊ��գ�");
	}
	else{
		this.MoviePath = MoviePath;
	}
}

//���÷��� SWF������ID
FlashCopy.prototype.SetDivID=function(DivID){
	if(DivID==""||DivID===undefined){		
		alert("[SetDivID]��������Ϊ��գ�");
	}
	else{
		this.DivID = DivID;
	}
}

//����Swf ID
FlashCopy.prototype.SetSwfID=function(SwfID){
	if(SwfID==""||SwfID===undefined){		
		alert("[SetDivID]��������Ϊ��գ�");
	}
	else{
		this.SwfID = SwfID;
	}
}

//���ÿ���
FlashCopy.prototype.SetWidth=function(width){
	if(!isNaN(width)){
		this.width = width;
	}
	else{
		alert("[SetWidth]��������Ϊ���֣�");
	}
}

//���ø߶�
FlashCopy.prototype.SetHeight=function(height){
	if(!isNaN(height)){
		this.height = height;
	}
	else{
		alert("[SetHeight]��������Ϊ���֣�");
	}
}

//������Ҫ���Ƶ�����[ ���Ƶ����� JS�ص�  SWF �ص�]
FlashCopy.prototype.SetText=function(CopyStr,JsCallBack,SwfCallBack){
	//setTimeout(function(){Copy.SetText("����ʱ������ʱ����,����ʧ��!","testFun('Js�ص�')","testFun('Swf�ص�')");},500);
	// testFun �ص�������Ҫ���Ѷ���
	if(this.GetSwf() == null && typeof(this.GetSwf()) == "undefined" ){
		alert("��ȡSWF����ʧ��,����!");
		return	
	}
	try{
		this.GetSwf().setText(CopyStr,SwfCallBack);
		if(JsCallBack){
			eval(JsCallBack);
		}
	}catch (e){
		return false;
		//alert("���ú�������!" + e.description);
	}		
}