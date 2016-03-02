/*
 * Ajax的一些小工具
 */

//创建XMLHttpRequest的函数
function createXMLHttpRequest(){
				try{
					return new XMLHttpRequest();
				}catch(e){
					try{
						return new ActiveXObject("Msxml2.XMLHTTP");
					}catch(e){
						try{
							return new ActiveXObject("Microsoft.XMLHTTP");
						}catch(e){
							throw e;
						}	
					}
				}
}

/* AJAX函数
 * option可以是json单体对象，
 * 可以提供方法method,请求链接url,是否异步asyn，请求体数据params,返回值类型type，回调函数callback等参数
 */

function Ajax(option){
	
	var XHR=createXMLHttpRequest();
	
	if(option.method==null){			//如果没有给出method，则默认get
		option.method="GET";
	}
	
	/*也可以使用null来比较
	if(option.asyn==null){		//如果没有给出aync，则默认true
		option.asyn=true;
	}*/
	if(option.asyn==undefined){
		option.asyn=true;
	}
	
	XHR.open(option.method,option.url,option.asyn);
	
	//如果为post方法，则需要设置请求头
	if("POST"==option.method.toUpperCase()){
		XHR.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	}
	
	XHR.send(option.params);
	
	XHR.onreadystatechange=function(){
		if(XHR.readyState==4 && XHR.status==200){
			
			var data;
			//文本类型
			if(option.type==null)		//如果没有给出type则默认为text
				data=XHR.responseText;
			else if(option.type=="text")
				data=XHR.responseText;
			//json类型
			else if(option.type="json")
				data=eval("("+XHR.responseText+")");
			//xml类型
			else if(option.type=="xml")
				data=XHR.responseXML;
			
			//把得到的响应数据传递给回调函数处理
			option.callback(data);
		}
	}
	
}
