<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv=Content-Type content="text/html; charset=utf-8">
<style>
tr td {
	text-align: center; /** 设置水平方向居中 */
	vertical-align: middle; /** 设置垂直方向居中 */
	font-size: 9pt;
}

.main table {
	border-collapse: collapse;
	position: fixed;
	left: 20px;
	top: 38px;
}

.ora td {
	height: 0.6cm;
	padding-left: 45px;
	text-align: left;
}

.headDate td {
	height: 0.5cm;
	padding-left: 234px;
	text-align: left;
}

.endDate td {
	height: 35px;
	padding-left: 52px;
	text-align: left;
}
</style>
</head>

<body>
	<div style='border: 0px solid red; Margin: 0px; width: 17cm; height: 11cm'>
		<div class="main">
			<table width=600px border="0" cellspacing="1" cellpadding="0" >
				<tr class="headDate">
					<td colspan="7">${year }&nbsp;&nbsp;${moon }&nbsp;&nbsp;${day }</td>
				</tr>
				<tr class="ora">
					<td colspan="7">${oraName }</td>
				</tr>
				<tr class="ora">
					<td colspan="7">${oraCode }</td>
				</tr>
				<tr style='height: 0.6cm;'>
					<td colspan="7"></td>
				</tr>
				<tr style='height: 0.7cm;'>
					<td colspan="7"></td>
				</tr>
				<c:forEach var="bill" items="${printList }">
					<tr style='height: 27px;'>
						<td style='font-size: 10pt;width: 135px'>${bill.gCode }</td>
						<td style='font-size: 8pt; width: 135px; white-space: nowrap; white-space: normal;'>${bill.gName }</td>
						<td style='width: 60px'>${bill.sendPackageNo }</td>
						<td style='width: 60px'>${bill.sendPackageNum }</td>
						<td style='width: 65px'>${bill.reqQty }</td>
						<td style='width: 70px'></td>
						<td style='width: 75px'><c:if test="${null != bill.sendPackageNum && null !=bill.reqPackageNum }">
					${bill.sendPackageNum }*${bill.reqPackageNum }
					</c:if></td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="7" style='height: 0.5cm;'></td>
				</tr>
				<tr class="endDate">
					<td colspan="5"></td>
					<td colspan="2">${year }&nbsp;&nbsp;${moon }&nbsp;&nbsp;${day }</td>
				</tr>
			</table>
		</div>
	</div>

	<script type="text/javascript">
		window.print();
	</script>
</body>

</html>
