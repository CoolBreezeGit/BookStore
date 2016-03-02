<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>分类列表</title>
    
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
	table {font-family: 宋体; font-size: 11pt; border-color: rgb(78,78,78);  width: 60%;}
	#th {background: rgb(78,78,78);}
</style>

<script type="text/javascript">
/* 	function confirmDel(cid){		
		if(confirm("您确认要删除此分类吗？")){
			var url="<c:url value='/admin/AdminCategoryServlet?method=deleteCategory&cid="+cid+"' />";
			alert(url);
			window.location.href=url;
			return true;
		}else{
			return false;
		}		
	} */
</script>
  </head>
  
  <body>
    <h2 style="text-align: center;">分类列表</h2>
    <table align="center" border="1" cellpadding="0" cellspacing="0">
    	<tr id="th" bordercolor="rgb(78,78,78)">
    		<th>分类名称</th>
    		<th>操作</th>
    	</tr>
    
    	<c:forEach var="category" items="${categoryList }">
	    	<tr bordercolor="rgb(78,78,78)">
	    		<td>${category.cname}</td>
	    		<td>
	    		  <a href="<c:url value='/admin/AdminCategoryServlet?method=toModifyPage&cname=${category.cname}&cid=${category.cid}'/>">修改</a> |
	    		  <a onclick="return confirm("您确认要删除此分类吗？");" href="<c:url value='/admin/AdminCategoryServlet?method=deleteCategory&cid=${category.cid}'/>">删除</a>
	    		</td>
	    	</tr>
    	</c:forEach>
   
    </table>
  </body>
</html>
