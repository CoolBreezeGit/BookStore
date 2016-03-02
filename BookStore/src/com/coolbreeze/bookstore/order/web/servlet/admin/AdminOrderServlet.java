package com.coolbreeze.bookstore.order.web.servlet.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coolbreeze.bookstore.order.domain.Order;
import com.coolbreeze.bookstore.order.service.OrderService;
import com.coolbreeze.web.servlet.BaseServlet;

public class AdminOrderServlet extends BaseServlet {

	private OrderService orderService=new OrderService();
	
	/*
	 * 查询所有订单
	 */
	public String queryAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {		
		request.setAttribute("orderList",orderService.queryAllOrder());
		return "f:/adminjsps/admin/order/list.jsp";
	}
	
	
	/*
	 * 根据state查询四种状态的订单
	 */
	public String queryOrderByState(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int state=Integer.parseInt(request.getParameter("state"));
		request.setAttribute("orderList",orderService.queryOrderByState(state));
		
		return "f:/adminjsps/admin/order/list.jsp";
	}

}

