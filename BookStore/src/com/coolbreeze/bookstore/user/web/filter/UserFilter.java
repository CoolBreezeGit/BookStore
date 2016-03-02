package com.coolbreeze.bookstore.user.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coolbreeze.bookstore.user.domain.User;


/*
 * 用户拦截器，
 * 	只有登录的用户才能访问：
 * 			/jsps/cart/* , /jsps/order/*,
 * 			CartServlet , OrderServlet
 */
public class UserFilter implements Filter{

	@Override
	public void destroy() {	
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req=(HttpServletRequest)request;
		HttpServletResponse res=(HttpServletResponse)response;					
		
		//先判断是否客户机是否登录
		User user=(User) req.getSession().getAttribute("user");
		
		if(user==null){			
			/*
			 *  如果未登录，则先保存原来所在的的页面地址preUrl到session，重定向到登录界面提示用户登录，
			 *  登录成功后在转发到原来要访问的页面在body的iframe框架中，并且刷新top栏的登录状态！
			 */	
			/*	
			String preUrl=req.getHeader("referer").toString();
			System.out.println(preUrl);
			req.getSession().setAttribute("reqUrl", preUrl);
			res.sendRedirect(req.getContextPath()+"/jsps/user/login.jsp");
			*/
			req.setAttribute("msg", "您还没有登陆，请点击左上方登录再使用购物车等功能！");
			req.getRequestDispatcher("/jsps/NoLoginMsg.jsp").forward(req, res);
		}else{
			 chain.doFilter(req, res);
		}
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {	
		
	}
	
}
