<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'bookdesc.jsp' starting page</title>
    
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
	body {
		font-size: 10pt;
		background: rgb(254,238,189);
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
</style>
<script type="text/javascript">
function confirmDel(){	
	if(confirm("您确定要删除此图书吗？")){
		document.getElementsByName("method")[0].value="deleteBook";
		return true;
	}else{
		return false;	
	}
}
	
	
	//检测是否已经修改了商品book的信息，否则返回false
	function checkBookIsChange(){
		var name=document.getElementsByName("bname")[0].value=="${book.bname}";
		var author=document.getElementsByName("author")[0].value=="${book.author}";
		var price=document.getElementsByName("price")[0].value=="${book.price}";
		/*
		var oCid=document.getElementsByName("cid")[0];
		alert(oCid.options[oCid.selectedindex].value);
		alert(document.getElementsByName("cid")[0].value+" : "+"${book.category.cid}");
		
		var oCid=document.getElementsByTagName("select")[0];
		alert(oCid.value);
		*/
		var category=document.getElementsByName("cid")[0].value=="${book.category.cid}";
		//alert(name+" "+author+" "+price+" "+category);
		if(name&&author&&price&&category){
			return false;
		}else{
			return true;
		}
	}
	
	function confirmMod(){
		if(!checkBookIsChange()){		//如果没有修改信息，则不提交修改表单
			alert("请修改此商品后再提交！");
			return false;
		}
		if(confirm("您确定要修改此图书吗？")){
			document.getElementsByName("method")[0].value="modifyBook";
			return true;
		}else{
			return false;	
		}
	}
</script>
  </head>
  
  <body>
  <div>
    <img src="<c:url value='/${book.image}'/>" border="0"/>
  </div>
  <form style="margin:20px;" id="form" action="<c:url value='/admin/AdminBookServlet'/>" method="post">
  			<input type="hidden" name="method"/>
  			<input type="hidden" name="bid" value="${book.bid}"/>
  	图书名称：<input type="text" name="bname" value="${book.bname }"/><br/>
  	图书单价：<input type="text" name="price" value="${book.price }"/>元<br/>
  	图书作者：<input type="text" name="author" value="${book.author }"/><br/>
  	图书分类：<select style="width: 150px; height: 40px;" name="cid">
	     		<c:forEach var="category" items="${categoryList }">
	     			<c:choose>
	     			<c:when test="${book.category.cid eq category.cid }">
						<option value="${category.cid}" selected='selected'>${category.cname}</option>
					</c:when>
					<c:otherwise>
						<option value="${category.cid}">${category.cname}</option>
					</c:otherwise>
					</c:choose>				
				</c:forEach>
    	</select><br/>
  	<input type="submit" name="method" value="delBook" onclick="return confirmDel();" />
  	<input type="submit" name="method" value="modBook" onclick="return confirmMod()"/>
  </form>
  </body>
</html>
