<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>修改分类</title>
    
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
	function check(){
		if(document.getElementsByName("cname")[0].value=="${category.cname}"){
			alert("请输入新的分类名称！");
			return false;		//不提交表单
		}else{
			return true;
		}
	}
</script>
  </head>
  
  <body>
    <h1>修改分类</h1>
    <form action="<c:url value='/admin/AdminCategoryServlet?method=modifyCategory'/>" method="post">
    	<input type="hidden" name="cid" value="${category.cid }" />
    	分类名称：<input type="text" name="cname" value="${category.cname }"/>
    	<input type="submit" onclick="return check();" value="修改分类" />
    	
    </form>
  </body>
</html>
