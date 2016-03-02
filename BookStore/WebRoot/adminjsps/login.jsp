<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>管理员登录页面</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

	<script type="text/javascript">
		//检查用户名密码不能为空
		function check(){
			if(document.getElementsByName("adminname")[0].value==""){
				alert("用户名不能为空！");
				return false;
			}
			if(document.getElementsByName("password")[0].value==""){
				alert("密码不能为空！");
				return false;
			}
			return true;
		}
	</script>

  </head>
  
  <body>
<h1>管理员登录页面</h1>
<hr/>
  <p style="font-weight: 900; color: red">${msg }</p>
<form action="<c:url value='/admin/AdminServlet?method=login'/>" method="post">
	管理员账户：<input type="text" name="adminname" value="${adminname}"/><br/>
	密　　　码：<input type="password" name="password" value="${password}"/><br/>
	<input type="submit" onclick="return check();" value="进入后台"/>
</form>
  </body>
</html>
