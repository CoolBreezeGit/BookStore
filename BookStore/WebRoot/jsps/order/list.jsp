<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>订单列表</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<style type="text/css">
	* {
		font-size: 11pt;
	}
	div {
		border: solid 2px gray;
		width: 75px;
		height: 75px;
		text-align: center;
	}
	li {
		margin: 10px;
	}
	
	#buy {
		background: url(<c:url value='/images/all.png'/>) no-repeat;
		display: inline-block;
		
		background-position: 0 -902px;
		margin-left: 30px;
		height: 36px;
		width: 146px;
	}
	#buy:HOVER {
		background: url(<c:url value='/images/all.png'/>) no-repeat;
		display: inline-block;
		
		background-position: 0 -938px;
		margin-left: 30px;
		height: 36px;
		width: 146px;
	}
</style>
  </head>
  
  <body>
<h1>我的订单</h1>

<c:forEach var="order" items="${requestScope.orderList}">

<table border="1" width="100%" cellspacing="0" background="black">
	<tr bgcolor="gray" bordercolor="gray">
		<td colspan="6">
			订单编号：${order.oid }　成交时间：<fmt:formatDate value="${order.ordertime}" pattern="yyyy-MM-dd HH:mm"/><!-- 2000-01-01 15:30 -->　金额：<font color="red"><b>${order.total }元</b></font>
			
				<c:choose>
					<c:when test="${order.state eq 1 }">
					<a href="<c:url value='/OrderServlet?method=toPayPage&oid=${order.oid }'/>">付款</a>
					</c:when>
					<c:when test="${order.state eq 2 }">
					等待发货
					</c:when>
					<c:when test="${order.state eq 3 }">
					<a href="<c:url value='/OrderServlet?method=confirmReceipt&oid=${order.oid }'/>">确认收货</a>
					</c:when>
					<c:when test="${order.state eq 4 }">
					订单结束
					</c:when>
				</c:choose>	
		</td>
	</tr>
	
	<c:forEach var="item" items="${order.orderItemList }">
		<tr bordercolor="gray" align="center">
			<td width="15%">
				<div><img src="<c:url value='${item.book.image }'/>" height="75"/></div>
			</td>
			<td>书名：${item.book.bname }</td>
			<td>单价：${item.book.price }</td>
			<td>作者：${item.book.author }</td>
			<td>数量：${item.count }</td>
			<td>小计：${item.subTotal }</td>
		</tr>
	</c:forEach>
</table>
<br/>
</c:forEach>


  </body>
</html>
