package com.coolbreeze.bookstore.user.web.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coolbreeze.commons.VerifyCode;


/*
 * 响应验证码图片的servlet，需要带from参数，以区分到底是login还是regist需要的验证码，防止冲突混淆
 */
public class VerifyCodeServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//
		VerifyCode verifyCode=new VerifyCode();
		
		//设置验证码为中文形式
		//verifyCode.setCodesByChineseArr();		
		//verifyCode.setHeight(40);
		//verifyCode.setWidth(160);
		
		BufferedImage image=verifyCode.getImage();
		String code=verifyCode.getText();
		//System.out.println(code);
		
		
		//把验证码存入session，以便验证其正确性
		String from=request.getParameter("from");
		//System.out.println(from);
		if(from!=null){
			//根据来源不同，存放不同的属性名的验证码值
			if(from.contains("regist")){
				request.getSession().setAttribute("regist_verifyCode", code);
			}else if(from.contains("login")){
				request.getSession().setAttribute("login_verifyCode", code);
			}				
		}
		//设置头，内容为图片，不缓存
		response.setHeader("Content-type", "image/jpeg");
		response.setDateHeader("expires",-1);
		response.setHeader("Cache-control","no-cache");
		response.setHeader("Pragma", "no-cache");
		verifyCode.output(image,response.getOutputStream());
	}

}
