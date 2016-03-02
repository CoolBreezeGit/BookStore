<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<!-- 当用户没有登录点击购物车等功能时，重定向回来的登录页面会显示在body.jsp，
		但是登录后返回的页面会根据body.jsp的base来默认回显在body.jsp所在的框架，
		导致原来的整个页面并没有得到刷新。 
		设置这个base属性是为了让登录后的页面覆盖原来的整个页面
			<base target="_parent">
	-->

    
    <title>登录</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">

  <script type="text/javascript">	  
		function check(){		
			//alert("check");
			var username=document.getElementById("username").value;			
			var password=document.getElementById("password").value;
			var oVerifyCode=document.getElementById("verifyCode");		
			//alert(username+" : "+password);	
			if(/*username==null || */ username.trim()==""){
				//alert("null");
				var oSpan1=document.getElementsByTagName("span")[0];
				oSpan1.innerHTML="";
				oSpan1.innerHTML="请填写用户名！";
				return false;
			}
			if(/* password==null || */password.trim()==""){
				var oSpan2=document.getElementsByTagName("span")[1];
				oSpan2.innerHTML="";
				oSpan2.innerHTML="请填写密码！";
				return false;
			}
			
			//如果需要输入验证码的情况下判断
			if(oVerifyCode!=null){
				//alert("not null");
				if(oVerifyCode.value.trim()==""){
					//alert("null");
					var oErrorSpan=document.getElementById("verifyCodeError");
					oErrorSpan.innerHTML="";
					oErrorSpan.innerHTML="验证码不能为空！";
					return false;
				}
			}
			return true;
		};
		
		/*
		window.onload=function(){
		 	var oForm=document.getElementById("form");
			oForm.onsubmit=function(){
				return check();
			}
		}; 
		 */
		 
	function changeImg(){
		this.src=this.src+"&t="+new Date().getTime();
	}
		
</script>
<style>
	div{
		float:left;
		margin:100px 400px;
	}
</style>

  </head>
  
<body>
 
  <div>
  
  <h1>登录</h1>

  <form id="form" action="<c:url value='/UserServlet?method=login'/>" onsubmit="return check()" method="post">
	用户名：<input type="text" id="username" name="username" value="${user.username }"><span></span><br/>
	密　码：<input type="password" id="password" name="password" value="${user.password}"/><span></span><br/>
	<c:if test="${tooManyError != null}">
		<input id="verifyCode" type="text" name="verifyCode"/><img alt="换一张" src="<c:url value='/VerifyCodeServlet?from=login'/>" onclick="changeImg()"/>
		<span id="verifyCodeError">${verifyCodeError }</span><br/>
	</c:if>
	<span>${msg}</span><br/>
	<input type="submit" value="登录" />
  </form>


 <br/><br/> 
 <ul>
   <li><a href="<c:url value='/index.jsp'/>">首页</a></li>
   <li><a href="<c:url value='/jsps/user/regist.jsp'/>">注册</a></li>
 </ul>

</div>

</body>  
  
</html>
