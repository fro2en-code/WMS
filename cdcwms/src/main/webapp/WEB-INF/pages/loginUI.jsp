<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
<%@ include file="/common/taglibs.jsp"%>
<style type="text/css">
* {
	margin: 0;
	padding: 0;
	list-style: none;
	font-family: "微软雅黑";
}

body {
	margin: 0;
	padding: 0;
	overflow-x: hidden;
	overflow-y: hidden;
	background-color: #E9EAEC;
}

#head {
	width: 100%;
	height: 120px;
	background-color: #fff;
}

#head_div {
	margin: 0 auto;
	width: 1080px;
	height: 120px;
	line-height: 120px;
	position: relative;
	_position: absolute;
	_left: 50%;
	_margin-left: -540px;
}

.li1 {
	position: absolute;
	top: 50%;
	left: 0px;
	margin-top: -20px;
}

.li2 {
	position: absolute;
	top: 50%;
	right: 0px;
	margin-top: -20px;
}

#body {
	margin: 0 auto;
	width: 1366px;
	height: 667px;
	background: url('img/login/banner.png') no-repeat center center;
	position: relative;
}

#box {
	width: 480px;
	height: 340px;
	background: #081b58;
	opacity: 0.8;
	position: absolute;
	right: 150px;
	top: 100px;
}

#box_box {
	margin: 0 auto;
	margin-top: 35px;
	width: 340px;
	overflow: hidden;
	_position: absolute;
	_left: 50%;
	_margin-left: -170px;
}

.user {
	color: #ffffff;
	font-size: 18px;
	width: 100%;
	height: 50px;
	line-height: 50px;
	border-bottom: 1px solid #CD6F1E;
	_padding-top: 15px;
}

.user input {
	width: 240px;
	height: 25px;
	background-color: #081b58;
	border: none;
	color: #ffffff;
	font-size: 18px;
	margin-left: 10px;
	outline: none;
}

#sub {
	display: block;
	text-decoration: none;
	text-align: center;
	line-height: 50px;
	width: 340px;
	height: 50px;
	color: #ffffff;
	font-size: 22px;
	background-color: #FF7C00;
	border-radius: 5px;
	position: absolute;
	left: 805px;
	top: 320px;
}

#sub:hover {
	background-color: #954800;
}

#box #text {
	width: 120px;
}
</style>
<title>中世国际-WMS</title>

<script type="text/javascript">
	$(function() {
		$('#sub').bind('click', login);
	});
	document.onkeydown = function(e) {
		if (!e)
			e = window.event;//火狐中是 window.event
		if ((e.keyCode || e.which) == 13) {
			login();
		}
	}
	function login() {
		var loginname = $.trim($(':text[name=loginname]').val());
		var pwd = $.trim($(':password[name=pwd]').val());
		var authVal = $.trim($(':text[name=code]').val());
		if ('' == loginname) {
			alert('账号不能为空');
			$(':text[name=loginname]').focus();
			return;
		}
		if ('' == pwd) {
			alert('密码不能为空');
			$(':password[name=pwd]').focus();
			return;
		}
		/* if ('' == authVal) {
			alert('验证码不能为空');
			$(':text[name=code]').focus();
			return;
		} */
		$.post('${app}/login?t=' + new Date().getTime(), {
			loginname : encodeURIComponent(loginname),
			pwd : pwd,
			code : authVal
		}, function(json) {
			if ('-1' == json.retcode) {
				alert(json.retmsg);
			} else {
				location.href = '${app}/toMgr.action';
			}
		}, "json");
	}
	function autoLogin() {
		$(':text[name=loginname]').val('admin');
		$(':password[name=pwd]').val('1234');
		login();
	}
</script>
<script type="text/javascript">
	if (navigator.userAgent.indexOf("WebKit") <= 0) {
		alert("推荐使用Chrome浏览器或360浏览器极速模式");
	}
</script>
</head>
<body>
	<div id="head">
		<div id="head_div">
			<div class="li1">
				<img src="${app}/img/login/logo.png" alt="" />
			</div>
			<%-- <div class="li2"><img src="${app}/img/login/jr.png" alt=""/></div> --%>
		</div>
	</div>
	<div id="body">
		<div id="box">
			<div id="box_box">
				<div class="user">
					账 &nbsp;&nbsp;号
					<input type="text" name="loginname" />
				</div>
				<div class="user">
					密 &nbsp;&nbsp;码
					<input type="password" name="pwd" />
				</div>
				<!-- 				
				<div class="user">
					验证码<input id="text" type="text" name="code" onkeyup="value=value.replace(/[\W]/g,'')"> <img src="${app}/authcode?t=<%=new Date().getTime()%>"
						onclick="this.src='${app}/authcode?t=' + (new Date()).getTime()"
						style="cursor: pointer; width: 120px; height: 25px; vertical-align: middle; background-color: Transparent;">
				</div>
 -->
			</div>
		</div>
		<a href="javascript:;" id="sub">登 录</a>
	</div>
</body>
</html>