<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>注册</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	
	<script type="text/javascript" src="<c:url value='/AjaxUtils.js'/>"></script>
	<script type="text/javascript">
	/*
	*	使用javascript来做基础用户数据校验，
	*	并使用ajax来做用户名是否已被注册的查询
	*	如果不能通过校验，则注册按钮将不能使用
	*/
		window.onload=function(){
		
			var oUsername=document.getElementById("username");			
			var oPassword=document.getElementsByName("password")[0];
			//alert(oPassword.id);
			var oConfirmPassword=document.getElementById("confirmPassword");
			var oEmail=document.getElementById("email");
		//	var oVerifyCode=document.getElementById("verifyCode");
			var oVerifyCodeImg=document.getElementById("verifyCodeImg");			
		//	var oVerifyCodeValue=document.getElementById("verifyCodeValue");
			
			var oSpan=document.getElementsByTagName("span");
			
			
			var oForm=document.getElementsByTagName("form")[0];
			
			//校验用户名的方法,返回是否检验合格,true为合格，false为不合格
			function checkUsername(){		
				//事件触发后先清空span内容
				oSpan[0].innerHTML="";						
				//var username=this.value;
				var username=oUsername.value;
				//1.用户名不能为空
				if(username.trim()==""){
					oSpan[0].innerHTML="用户名不能为空!";
				}
				//2.用户名必须是字母，数字，下划线的组合
				var reg=/^\w+$/g;
				if(!reg.test(username)){
					oSpan[0].innerHTML=oSpan[0].innerHTML+" 用户名必须是字母，数字，下划线的组合！";
					return false;
				}
				//3..用户名长度不能大于50字符				
				if(username.trim().length>50){
					oSpan[0].innerHTML=oSpan[0].innerHTML+" 用户名长度不能大于50!";
					return false;
				}	
									
				//4.用户名必须没有被注册：ajax查询	
				Ajax(
					{
						method:"POST",
						url:"<c:url value='/VerifyUsernameServlet'/>",
						params:"username="+username,
						callback:function(data){
							if(data!=null){							
								oSpan[0].innerHTML=oSpan[0].innerHTML+" "+data;
								return false;
							}		
						}
					}
				);
				
				return true;
			}
			
			//用户名校验
			oUsername.onblur=function(){
				checkUsername();
					
			};
			
			//校验密码的方法
			function checkPassword(){
				//事件触发后先清空span内容
				oSpan[1].innerHTML="";
				//长度不能少于8位，不能大于50位
				var password=oPassword.value;
				if(password.trim().length<8){
					oSpan[1].innerHTML="密码长度不能小于8";
					return false;
				}
				if(password.trim().length>50){
					oSpan[1].innerHTML="密码长度不能大于50";
					return false;
				}
				return true;
			}
			//密码校验
			oPassword.onblur=function(){		
				checkPassword();
			}
			
			//校验确认密码的方法
			function checkConfirmPassword(){
				//事件触发后先清空span内容
				oSpan[2].innerHTML="";			
				//长度不能少于8位，不能大于50位							
				if(oConfirmPassword.value != oPassword.value){
					oSpan[2].innerHTML="两次输入的密码不一致！";
					return false;
				}
				return true;
			}
			//确认密码校验
			oConfirmPassword.onblur=function(){
				checkConfirmPassword();
			}
			
			//校验邮箱的方法
			function checkEmail(){			
				//事件触发后先清空span内容
				oSpan[3].innerHTML="";
				//邮箱要符合格式 \\w+@\\w+\\.\\w+
				//var reg=new RegExp("\\w+@\\w+\\.\\w+");	//只要有部分符合reg，则为true 
				//使用^,$表示整个语句都要符合reg
				var reg=new RegExp("^\\w+@\\w+\\.\\w+$");
				var email=oEmail.value;
								
				if(!reg.test(email)){
					oSpan[3].innerHTML="邮箱格式不正确！";
					return false;
				}else{
					return true;
				}				
				
			}			
			
			//邮箱校验
			oEmail.onblur=function(){				
				checkEmail();				
			};	
		
			/*
			*	本地验证码校验存在问题：图片点击后或者刷新之后，hidden节点得到session中的验证码值verifyCode
			*	和实际验证码的值不匹配
			* 	并且本地能得到验证码的方式也并不安全
			*/
			/* //验证码校验方法
			function checkVerifyCode(){
				oSpan[4].innerHTML="";
				alert(oVerifyCodeValue.value);
				if(oVerifyCode.value.toLowerCase() != oVerifyCodeValue.value.toLowerCase()){
					oSpan[4].innerHTML="验证码不正确！";
					return false;	
				}
				return true;
			}
			//验证码本地校验 
			oVerifyCode.onblur=function(){
				checkVerifyCode();
			}
			 */
			//验证码图片点击事件，请求新的验证码
			oVerifyCodeImg.onclick=function(){
				this.src=this.src+"&time=" + new Date().getTime();
			}
		
			//表单提交事件，如果所有校验合格才允许提交
			oForm.onsubmit=function(){
				//如果检验返回ture，则允许提交表单
				if(checkUsername() & checkPassword() & checkConfirmPassword() & checkEmail()/* & checkVerifyCode() */){
					return true;
				}else{	
					return false;
				}
			}
			
		};
		
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
  
  <h1>注册</h1>
<form action="<c:url value='/UserServlet'/>" method="post">
	<input type="hidden" name="method" value="regist"/>
	用 户 名：<input type="text" id="username" name="username" value="${user.username}"/> <span>${error.username}</span> <br/>
	密　 码：<input type="password" id="password" name="password" value="${user.password}"/> <span>${error.password}</span> <br/>
	确认密码：<input type="password" id="confirmPassword" name="confirmPassword" value="${confirmPassword}"/> <span>${error.confirmPassword}</span> <br/>
	邮　 箱：<input type="text" id ="email" name="email" value="${user.email}"/> <span>${error.email}</span> <br/>
	验 证 码:<input type="text" id="verifyCode" name="verifyCode"/> <img id="verifyCodeImg" alt="点击换一张" src="<c:url value='/VerifyCodeServlet?from=regist'/>"/><span>${error.verifyCode}</span><br/>
	<!-- 这样做其实有风险，能否防止注册机呢？？
	<！-- input type="hidden" id="verifyCodeValue" value="${sessionScope.verifyCode }"/ ！-->
	<input type="submit" id="submit" value="注册"/>
</form>

 <br/><br/> 
 <ul>
   <li><a href="<c:url value='/index.jsp'/>">首页</a></li>
   <li><a href="<c:url value='/jsps/user/login.jsp'/>">登陆</a></li>
 </ul>


</div>
  </body>
</html>
