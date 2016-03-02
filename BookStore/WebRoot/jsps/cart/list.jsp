<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>购物车列表</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">

<style type="text/css">
	* {
		font-size: 11pt;
	}
	div {
		margin:20px;
		border: solid 2px gray;
		width: 150px;
		height: 150px;
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
<!-- 当购物车为空，或者购物车没有条目时，显示空的购物车图片 -->
<c:choose>	
	<c:when test="${empty sessionScope.cart or fn:length(sessionScope.cart.cartItemMap) eq 0}">		
		<h1>购物车空空如也，快去购物吧！！</h1>
		<img src="<c:url value='/images/cart.png' />" width="200" ></img>
	</c:when>
	<c:otherwise>
		<h1>购物车</h1>
		<table border="1" width="100%" cellspacing="0" background="black">
		<tr>
			<td colspan="7" align="right" style="font-size: 15pt; font-weight: 900">
				<a href="<c:url value='/CartServlet?method=emptyCart'/>" >清空购物车</a>
			</td>
		</tr>
		<tr>
			<th>图片</th>
			<th>书名</th>
			<th>作者</th>
			<th>单价</th>
			<th>数量</th>
			<th>小计</th>
			<th>操作</th>
		</tr>
	<c:forEach var="entry" items="${sessionScope.cart.cartItemMap}">
		<tr>
			<td><div><img src="<c:url value='/${entry.value.book.image }'/>"/></div></td>
			<td>${entry.value.book.bname }</td>
			<td>${entry.value.book.author }</td>
			<td>${entry.value.book.price }</td>
			<td>${entry.value.count }</td>
			<td>${entry.value.subTotal }</td>
			<td><a href="<c:url value='/CartServlet?method=removeFromCart&bid=${entry.value.book.bid}'/>" >删除</a></td>
		</tr>
	</c:forEach>
	
		<tr>
			<td colspan="7" align="right" style="font-size: 15pt; font-weight: 900">
				合计：${sessionScope.cart.total }元
			</td>
		</tr>
		<tr>
			<td colspan="7" align="right" style="font-size: 15pt; font-weight: 900">
				<a id="buy" href="<c:url value='/OrderServlet?method=cartBecomeOrder'/>"></a>
			</td>
		</tr>
	</table>
  </c:otherwise>
</c:choose>

  </body>
</html>
