<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="app" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv=Content-Type content="text/html; charset=UTF-8">
<style>
<!--
table {
	mso-displayed-decimal-separator: "\.";
	mso-displayed-thousand-separator: "\,";
}

.font510074 {
	color: windowtext;
	font-size: 9.0pt;
	font-weight: 400;
	font-style: normal;
	text-decoration: none;
	font-family: 宋体;
	mso-generic-font-family: auto;
	mso-font-charset: 134;
}

.xl6410074 {
	padding: 0px;
	mso-ignore: padding;
	color: black;
	font-size: 20.0pt;
	font-weight: 400;
	font-style: normal;
	text-decoration: none;
	font-family: 微软雅黑, sans-serif;
	mso-font-charset: 134;
	mso-number-format: General;
	text-align: left;
	vertical-align: middle;
	mso-background-source: auto;
	mso-pattern: auto;
	white-space: nowrap;
}

.xl6510074 {
	padding: 0px;
	mso-ignore: padding;
	color: black;
	font-size: 20.0pt;
	font-weight: 400;
	font-style: normal;
	text-decoration: none;
	font-family: 微软雅黑, sans-serif;
	mso-font-charset: 134;
	mso-number-format: General;
	text-align: general;
	vertical-align: middle;
	mso-background-source: auto;
	mso-pattern: auto;
	white-space: nowrap;
}

.xl6610074 {
	padding: 0px;
	mso-ignore: padding;
	color: white;
	font-size: 20.0pt;
	font-weight: 400;
	font-style: normal;
	text-decoration: none;
	font-family: 微软雅黑, sans-serif;
	mso-font-charset: 134;
	mso-number-format: General;
	text-align: left;
	vertical-align: middle;
	mso-background-source: auto;
	mso-pattern: auto;
	white-space: nowrap;
}

.xl6710074 {
	padding: 0px;
	mso-ignore: padding;
	color: black;
	font-size: 20.0pt;
	font-weight: 700;
	font-style: normal;
	text-decoration: none;
	font-family: 微软雅黑, sans-serif;
	mso-font-charset: 134;
	mso-number-format: General;
	text-align: center;
	vertical-align: middle;
	border-top: .5pt solid windowtext;
	border-right: .5pt solid windowtext;
	border-bottom: .5pt solid windowtext;
	border-left: 1.0pt solid windowtext;
	mso-background-source: auto;
	mso-pattern: auto;
	white-space: nowrap;
}

.xl6810074 {
	padding: 0px;
	mso-ignore: padding;
	color: black;
	font-size: 20.0pt;
	font-weight: 400;
	font-style: normal;
	text-decoration: none;
	font-family: 微软雅黑, sans-serif;
	mso-font-charset: 134;
	mso-number-format: General;
	text-align: center;
	vertical-align: middle;
	border-top: .5pt solid windowtext;
	border-right: 1.0pt solid windowtext;
	border-bottom: .5pt solid windowtext;
	border-left: .5pt solid windowtext;
	mso-background-source: auto;
	mso-pattern: auto;
	white-space: nowrap;
}

.xl6910074 {
	padding: 0px;
	mso-ignore: padding;
	color: black;
	font-size: 20.0pt;
	font-weight: 700;
	font-style: normal;
	text-decoration: none;
	font-family: 微软雅黑, sans-serif;
	mso-font-charset: 134;
	mso-number-format: General;
	text-align: center;
	vertical-align: middle;
	border-top: .5pt solid windowtext;
	border-right: .5pt solid windowtext;
	border-bottom: none;
	border-left: 1.0pt solid windowtext;
	mso-background-source: auto;
	mso-pattern: auto;
	white-space: nowrap;
}

.xl7010074 {
	padding: 0px;
	mso-ignore: padding;
	color: black;
	font-size: 20.0pt;
	font-weight: 700;
	font-style: normal;
	text-decoration: none;
	font-family: 微软雅黑, sans-serif;
	mso-font-charset: 134;
	mso-number-format: General;
	text-align: center;
	vertical-align: middle;
	border-top: none;
	border-right: .5pt solid windowtext;
	border-bottom: none;
	border-left: 1.0pt solid windowtext;
	mso-background-source: auto;
	mso-pattern: auto;
	white-space: nowrap;
}

.xl7110074 {
	padding: 0px;
	mso-ignore: padding;
	color: black;
	font-size: 20.0pt;
	font-weight: 400;
	font-style: normal;
	text-decoration: none;
	font-family: 微软雅黑, sans-serif;
	mso-font-charset: 134;
	mso-number-format: "General Date";
	text-align: center;
	vertical-align: middle;
	border-top: .5pt solid windowtext;
	border-right: 1.0pt solid windowtext;
	border-bottom: none;
	border-left: .5pt solid windowtext;
	mso-background-source: auto;
	mso-pattern: auto;
	white-space: nowrap;
}

.xl7210074 {
	padding: 0px;
	mso-ignore: padding;
	color: black;
	font-size: 20.0pt;
	font-weight: 400;
	font-style: normal;
	text-decoration: none;
	font-family: 微软雅黑, sans-serif;
	mso-font-charset: 134;
	mso-number-format: "General Date";
	text-align: center;
	vertical-align: middle;
	border-top: none;
	border-right: 1.0pt solid windowtext;
	border-bottom: none;
	border-left: .5pt solid windowtext;
	mso-background-source: auto;
	mso-pattern: auto;
	white-space: nowrap;
}

.xl7310074 {
	padding: 0px;
	mso-ignore: padding;
	color: black;
	font-size: 20.0pt;
	font-weight: 700;
	font-style: normal;
	text-decoration: none;
	font-family: 微软雅黑, sans-serif;
	mso-font-charset: 134;
	mso-number-format: General;
	text-align: center;
	vertical-align: middle;
	border-top: .5pt solid windowtext;
	border-right: .5pt solid windowtext;
	border-bottom: 1.0pt solid windowtext;
	border-left: 1.0pt solid windowtext;
	mso-background-source: auto;
	mso-pattern: auto;
	white-space: nowrap;
}

.xl7410074 {
	padding: 0px;
	mso-ignore: padding;
	color: black;
	font-size: 20.0pt;
	font-weight: 400;
	font-style: normal;
	text-decoration: none;
	font-family: 微软雅黑, sans-serif;
	mso-font-charset: 134;
	mso-number-format: General;
	text-align: center;
	vertical-align: middle;
	border-top: .5pt solid windowtext;
	border-right: 1.0pt solid windowtext;
	border-bottom: 1.0pt solid windowtext;
	border-left: .5pt solid windowtext;
	mso-background-source: auto;
	mso-pattern: auto;
	white-space: nowrap;
}

.xl7510074 {
	padding: 0px;
	mso-ignore: padding;
	color: black;
	font-size: 20.0pt;
	font-weight: 700;
	font-style: normal;
	text-decoration: none;
	font-family: 微软雅黑, sans-serif;
	mso-font-charset: 134;
	mso-number-format: General;
	text-align: center;
	vertical-align: middle;
	border-top: 1.0pt solid windowtext;
	border-right: .5pt solid windowtext;
	border-bottom: .5pt solid windowtext;
	border-left: 1.0pt solid windowtext;
	mso-background-source: auto;
	mso-pattern: auto;
	white-space: nowrap;
}

.xl7610074 {
	padding: 0px;
	mso-ignore: padding;
	color: black;
	font-size: 20.0pt;
	font-weight: 400;
	font-style: normal;
	text-decoration: none;
	font-family: 微软雅黑, sans-serif;
	mso-font-charset: 134;
	mso-number-format: General;
	text-align: center;
	vertical-align: middle;
	border-top: 1.0pt solid windowtext;
	border-right: 1.0pt solid windowtext;
	border-bottom: .5pt solid windowtext;
	border-left: .5pt solid windowtext;
	mso-background-source: auto;
	mso-pattern: auto;
	white-space: nowrap;
}

ruby {
	ruby-align: left;
}

rt {
	color: windowtext;
	font-size: 9.0pt;
	font-weight: 400;
	font-style: normal;
	text-decoration: none;
	font-family: 宋体;
	mso-generic-font-family: auto;
	mso-font-charset: 134;
	mso-char-type: none;
}

.PageNext {
	page-break-after: always;
}
-->
</style>
</head>
<body>
	<c:forEach var="box" items="${printList }">
		<div align=center class="PageNext">
			<table border=0 cellpadding=0 cellspacing=0 width=819 class=xl6510074
				style='border-collapse: collapse; table-layout: fixed; width: 615pt'>
				<col class=xl6510074 width=57
					style='mso-width-source: userset; mso-width-alt: 1824; width: 43pt'>
				<col class=xl6410074 width=258
					style='mso-width-source: userset; mso-width-alt: 8256; width: 194pt'>
				<col class=xl6410074 width=447
					style='mso-width-source: userset; mso-width-alt: 14304; width: 335pt'>
				<col class=xl6510074 width=57
					style='mso-width-source: userset; mso-width-alt: 1824; width: 43pt'>
				<tr height=20 style='height: 15.6pt'>
					<td height=20 class=xl6510074 width=57
						style='height: 15.6pt; width: 43pt'></td>
					<td class=xl6410074 width=258 style='width: 194pt'></td>
					<td class=xl6410074 width=447 style='width: 335pt'></td>
					<td class=xl6510074 width=57 style='width: 43pt'></td>
				</tr>
				<tr height=20 style='height: 15.6pt'>
					<td height=20 class=xl6510074 style='height: 15.6pt'></td>
					<td class=xl6410074></td>
					<td class=xl6410074></td>
					<td class=xl6510074></td>
				</tr>
				<tr height=20 style='mso-height-source: userset; height: 15.6pt'>
					<td height=20 class=xl6510074 style='height: 15.6pt'></td>
					<td class=xl6610074></td>
					<td class=xl6410074></td>
					<td class=xl6510074></td>
				</tr>
				<tr height=20 style='mso-height-source: userset; height: 15.6pt'>
					<td height=20 class=xl6510074 style='height: 15.6pt'></td>
					<td rowspan=4 class=xl7510074>收货方</td>
					<td rowspan=4 height=80 width=447
						style='height: 62.4pt; width: 335pt' align=left valign=top><span
						style='mso-ignore: vglayout; position: absolute; z-index: 1; margin-left: 14px; margin-top: 11px; width: 405px; height: 61px'><img
							width=405 height=61 src="${app }/img/3PLXNY.png""></span><span
						style='mso-ignore: vglayout2'>
							<table cellpadding=0 cellspacing=0>
								<tr>
									<td rowspan=4 height=80 class=xl7610074 width=447
										style='border-left: none; height: 62.4pt; width: 335pt'></td>
								</tr>
							</table>
					</span></td>
					<td class=xl6510074></td>
				</tr>
				<tr height=20 style='mso-height-source: userset; height: 15.6pt'>
					<td height=20 class=xl6510074 style='height: 15.6pt'></td>
					<td class=xl6510074></td>
				</tr>
				<tr height=20 style='mso-height-source: userset; height: 15.6pt'>
					<td height=20 class=xl6510074 style='height: 15.6pt'></td>
					<td class=xl6510074></td>
				</tr>
				<tr height=20 style='mso-height-source: userset; height: 15.6pt'>
					<td height=20 class=xl6510074 style='height: 15.6pt'></td>
					<td class=xl6510074></td>
				</tr>
				<tr height=20 style='mso-height-source: userset; height: 15.6pt'>
					<td height=20 class=xl6510074 style='height: 15.6pt'></td>
					<td rowspan=2 class=xl6710074 style='border-top: none'>零件号</td>
					<td rowspan=2 class=xl6810074 style='border-top: none'>${box.gCode }</td>
					<td class=xl6510074></td>
				</tr>
				<tr height=20 style='mso-height-source: userset; height: 15.6pt'>
					<td height=20 class=xl6510074 style='height: 15.6pt'></td>
					<td class=xl6510074></td>
				</tr>
				<tr height=20 style='mso-height-source: userset; height: 15.6pt'>
					<td height=20 class=xl6510074 style='height: 15.6pt'></td>
					<td rowspan=2 class=xl6710074 style='border-top: none'>零件名称</td>
					<td rowspan=2 class=xl6810074 style='border-top: none'>${box.gName }</td>
					<td class=xl6510074></td>
				</tr>
				<tr height=20 style='mso-height-source: userset; height: 15.6pt'>
					<td height=20 class=xl6510074 style='height: 15.6pt'></td>
					<td class=xl6510074></td>
				</tr>
				<tr height=20 style='mso-height-source: userset; height: 15.6pt'>
					<td height=20 class=xl6510074 style='height: 15.6pt'></td>
					<td rowspan=2 class=xl6710074 style='border-top: none'>供应商代码</td>
					<td rowspan=2 class=xl6810074 style='border-top: none'>${box.oraCode }</td>
					<td class=xl6510074></td>
				</tr>
				<tr height=20 style='mso-height-source: userset; height: 15.6pt'>
					<td height=20 class=xl6510074 style='height: 15.6pt'></td>
					<td class=xl6510074></td>
				</tr>
				<tr height=20 style='mso-height-source: userset; height: 15.6pt'>
					<td height=20 class=xl6510074 style='height: 15.6pt'></td>
					<td rowspan=2 class=xl6710074 style='border-top: none'>供应商名称</td>
					<td rowspan=2 class=xl6810074 style='border-top: none'>${box.oraName }</td>
					<td class=xl6510074></td>
				</tr>
				<tr height=20 style='mso-height-source: userset; height: 15.6pt'>
					<td height=20 class=xl6510074 style='height: 15.6pt'></td>
					<td class=xl6510074></td>
				</tr>
				<tr height=20 style='mso-height-source: userset; height: 15.6pt'>
					<td height=20 class=xl6510074 style='height: 15.6pt'></td>
					<td rowspan=2 class=xl6710074 style='border-top: none'>数量</td>
					<td rowspan=2 class=xl6810074 style='border-top: none'>${box.number }</td>
					<td class=xl6510074></td>
				</tr>
				<tr height=20 style='mso-height-source: userset; height: 15.6pt'>
					<td height=20 class=xl6510074 style='height: 15.6pt'></td>
					<td class=xl6510074></td>
				</tr>
				<tr height=20 style='mso-height-source: userset; height: 15.6pt'>
					<td height=20 class=xl6510074 style='height: 15.6pt'></td>
					<td rowspan=3 class=xl6910074 style='border-top: none'>送货日期</td>
					<td rowspan=3 class=xl7110074 style='border-top: none'>${box.time }</td>
					<td class=xl6510074></td>
				</tr>
				<tr height=20 style='mso-height-source: userset; height: 15.6pt'>
					<td height=20 class=xl6510074 style='height: 15.6pt'></td>
					<td class=xl6510074></td>
				</tr>
				<tr height=20 style='mso-height-source: userset; height: 15.6pt'>
					<td height=20 class=xl6510074 style='height: 15.6pt'></td>
					<td class=xl6510074></td>
				</tr>
				<tr height=20 style='mso-height-source: userset; height: 15.6pt'>
					<td height=20 class=xl6510074 style='height: 15.6pt'></td>
					<td rowspan=4 class=xl6710074
						style='border-bottom: 1.0pt solid black'>条形码</td>
					<td rowspan=4 height=100 width=447
						style='border-bottom: 1.0pt solid black; height: 62.4pt; width: 335pt'
						align=left valign=top><span
						style='mso-ignore: vglayout; position: absolute; z-index: 101; margin-left: 20px; margin-top: 5px; width: 406px; height: 68px'>
							<img width=406 height=68
							src="/cdcwms/barCode/getImage?code=${box.gCode }$${box.number }&height=100&width=300&type=1">
					</span>
						<table cellpadding=0 cellspacing=0>
							<tr>
								<td rowspan=4 height=80 class=xl6810074 width=447
									style='border-bottom: 1.0pt black; border-left: none; height: 62.4pt; width: 335pt'>
								</td>
							</tr>
						</table></td>
					<td class=xl6510074></td>
				</tr>
				<tr height=20 style='mso-height-source: userset; height: 15.6pt'>
					<td height=20 class=xl6510074 style='height: 15.6pt'></td>
					<td class=xl6510074></td>
				</tr>
				<tr height=20 style='mso-height-source: userset; height: 15.6pt'>
					<td height=20 class=xl6510074 style='height: 15.6pt'></td>
					<td class=xl6510074></td>
				</tr>
				<tr height=20 style='mso-height-source: userset; height: 15.6pt'>
					<td height=20 class=xl6510074 style='height: 15.6pt'></td>
					<td class=xl6510074></td>
				</tr>
				<tr height=20 style='height: 15.6pt'>
					<td height=20 class=xl6510074 style='height: 15.6pt'></td>
					<td class=xl6410074></td>
					<td class=xl6410074></td>
					<td class=xl6510074></td>
				</tr>
				<![if supportMisalignedColumns]>
				<tr height=0 style='display: none'>
					<td width=57 style='width: 43pt'></td>
					<td width=258 style='width: 194pt'></td>
					<td width=447 style='width: 335pt'></td>
					<td width=57 style='width: 43pt'></td>
				</tr>
				<![endif]>
			</table>

		</div>
	</c:forEach>
	<script type="text/javascript">
		window.print();
	</script>
</body>
</html>