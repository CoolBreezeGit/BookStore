package com.coolbreeze.bookstore.user.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coolbreeze.bookstore.user.service.UserException;
import com.coolbreeze.bookstore.user.service.UserService;


/*
 *	此Servlet用来响应AJAX请求查询用户名是否已经被注册,
 */
public class VerifyUsernameServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserService userService=new UserService();
		response.setContentType("html/text;charset=utf-8");
		PrintWriter pw=response.getWriter();
		try{
			userService.verifyUsername(request.getParameter("username"));
		}catch(UserException e){
			pw.print(e.getMessage());
			return ;
		}		
		//System.out.println("username is not registed");
	}

}
