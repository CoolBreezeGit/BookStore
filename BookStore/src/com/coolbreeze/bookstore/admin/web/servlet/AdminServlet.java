package com.coolbreeze.bookstore.admin.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coolbreeze.web.servlet.BaseServlet;

/*
 * 管理员Servlet
 */
public class AdminServlet extends BaseServlet {

	/*
	 * 管理员登录
	 */
	public String login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String adminname = request.getParameter("adminname");
		String password = request.getParameter("password");

		// System.out.println(adminname+" : "+password);

		if (!"coolbreeze".equals(adminname)) {
			request.setAttribute("msg", "用户名错误！");
			request.setAttribute("adminname", adminname);
			request.setAttribute("password", password);
			return "f:/adminjsps/login.jsp";
		}

		if (!"12345678".equals(password)) {
			request.setAttribute("msg", "密码错误！");
			request.setAttribute("adminname", adminname);
			request.setAttribute("password", password);
			return "f:/adminjsps/login.jsp";
		}

		// 登录成功！
		request.getSession().setAttribute("admin", "coolbreeze");
		return "f:/adminjsps/admin/index.jsp";
	}

	/*
	 * 管理员退出
	 */
	public String logout(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.getSession().invalidate();
		return "f:/adminjsps/login.jsp";
	}
}
