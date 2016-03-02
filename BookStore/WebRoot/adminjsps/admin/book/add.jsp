<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>添加图书</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<style type="text/css">
	body {background: rgb(254,238,189);}
</style>

<script type="text/javascript">
	//校验，bname等等不能为空！！
	function check(){
		var bname=document.getElementsByName("bname")[0].value;
		var image=document.getElementsByName("image")[0].value;	//文件名
		var price=document.getElementsByName("price")[0].value;
		var author=document.getElementsByName("author")[0].value;
		var cid=document.getElementsByName("cid")[0].value;
		//alert(image);
		if(bname=="" || image=="" || price=="" || author=="" || cid==""){
			alert("请填写完整图书的信息！");
			return false;
		}
	}
</script>

  </head>
  
  <body>
    <h1>添加图书</h1>
    <p style="font-weight: 900; color: red">${msg }</p>
    <form action="<c:url value='/admin/AdminAddBookServlet'/>" method="post" enctype="multipart/form-data">
    	图书名称：<input style="width: 150px; height: 25px;" type="text" name="bname"/><br/>
    	图书图片：<input style="width: 223px; height: 25px;" type="file" name="image"/><br/>
    	图书单价：<input style="width: 150px; height: 25px;" type="text" name="price"/>元<br/>
    	图书作者：<input style="width: 150px; height: 25px;" type="text" name="author"/><br/>
    	图书分类：<select style="width: 150px; height: 40px;" name="cid">
	   			 <c:forEach var="category" items="${categoryList }">		     		
					<option value="${category.cid}">${category.cname}</option>			
				</c:forEach>
    	</select>
    	<br/>
    	<input type="submit" onclick="return check()" value="添加图书"/>
    </form>
  </body>
</html>
