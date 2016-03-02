package com.coolbreeze.bookstore.admin.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * 管理员拦截器
 * 	只有登录的管理员才能访问：
 * 			/admin/*
 */
public class AdminFilter implements Filter{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
	
		HttpServletRequest req=(HttpServletRequest)request;
		HttpServletResponse res=(HttpServletResponse)response;					
		
		//先判断是否客户机是否登录
		String admin=(String)req.getSession().getAttribute("admin");
		
		if(admin==null){			
			//如果未登录，则先保存原来要访问的页面到session，重定向到登录界面提示用户登录						
			res.sendRedirect(req.getContextPath()+"/adminjsps/login.jsp");
		}else{
			//放行
			chain.doFilter(req,res);
		}
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	
}
