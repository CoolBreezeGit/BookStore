<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>图书详细</title>
    
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
	}
	#div1 {
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
		
		background-position: 0 -70px;
		margin-left: 30px;
		height: 36px;
		width: 146px;
	}
	#buy:HOVER {
		background: url(<c:url value='/images/all.png'/>) no-repeat;
		display: inline-block;
		
		background-position: 0 -106px;
		margin-left: 30px;
		height: 36px;
		width: 146px;
	}
	
	#addToCart {
		background: url(<c:url value='/images/all.png'/>) no-repeat;
		display: inline-block;
		
		background-position: 0 -830px;
		margin-left: 30px;
		height: 36px;
		width: 146px;
	}
	#addToCart:HOVER {
		background: url(<c:url value='/images/all.png'/>) no-repeat;
		display: inline-block;
		
		background-position: 0 -866px;
		margin-left: 30px;
		height: 36px;
		width: 146px;
	}
</style>

<script type="text/javascript" src="<c:url value='/AjaxUtils.js'/>" ></script>
<script>
	window.onload=function(){
		/*
		*	加减按钮增加或减少商品数量
		*/
		var oCount=document.getElementById("count");
		var oAddBtn=document.getElementById("addBtn");
		var oSubBtn=document.getElementById("subBtn");
		
		oAddBtn.onclick=function(){
			oCount.value=1+parseInt(oCount.value);			
		};
		oSubBtn.onclick=function(){
			oCount.value=parseInt(oCount.value)-1;
			if(parseInt(oCount.value)<1){
				oCount.value=1;
			}
		};
		
		/*
		*	使用AJAX请求来添加商品到购物车
		*/
		var oAddToCart=document.getElementById("addToCart");
		oAddToCart.onclick=function(){
			
			//得到CartServlet的地址
			var oActionValue=document.getElementById("actionValue");
			var bid=document.getElementById("bid").value;	
			var url=oActionValue.value+"?method=addToCart&bid="+bid+"&count="+oCount.value;
			//alert(url);
			Ajax(
				{
					url:url,
					callback:function(data){															
						if(data == "OK"){ //response使用print写入OK（没有换行符）,才能返回true
							alert("加入购物车成功！");								
						}else{
							//window.open('http://localhost:8080/TestBookStore/jsps/user/login.jsp', 'newwindow', 'height=200, width=400, top=200,left=200, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no');
							//如何实现location地址的自动获取??
							window.location='http://localhost:8080/TestBookStore/jsps/user/login.jsp';							
						}
					}
				}
			);
		};
	};
	
</script>
  </head>
  
  <body>
  <div id="div1">
    <img src="<c:url value='/${book.image }'/>" border="0"/>
  </div>
  <ul>
    <li>书名：${book.bname }</li>
    <li>作者：${book.author }</li>
    <li>单价：${book.price }元</li>
  </ul>
  <div id="div2">
	  <button id="addBtn">+</button>
	  <form id="form" action="<c:url value='/OrderServlet'/>" method="post">
	  	<input type="hidden" id="actionValue" value="<c:url value='/CartServlet'/>"/>
	  	<input type="hidden" id="method" name="method" value="goodsBecomeOrder"/>
	  	 <input type="hidden" id="bid" name="bid" value="${book.bid}"/>
	  	<input type="text" size="3" id="count" name="count" value="1"/>
	  </form>
	  <button id="subBtn">-</button>
  </div>
   
  <a id="buy" href="javascript:document.getElementById('form').submit();"></a> 
  <a id="addToCart"></a>
  </body>
</html>
