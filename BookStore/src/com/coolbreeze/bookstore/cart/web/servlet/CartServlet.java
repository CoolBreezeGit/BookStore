package com.coolbreeze.bookstore.cart.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coolbreeze.bookstore.book.domain.Book;
import com.coolbreeze.bookstore.book.service.BookService;
import com.coolbreeze.bookstore.cart.domain.Cart;
import com.coolbreeze.bookstore.cart.domain.CartItem;
import com.coolbreeze.web.servlet.BaseServlet;

public class CartServlet extends BaseServlet {	
	
	/*
	 * 查看我的购物车
	 */
	public String lookMyCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		/*		
 		// 从session得到购物车对象
		Cart cart=(Cart) request.getSession().getAttribute("cart");
		if(cart==null){
			System.out.println("cart is null");
			return null;
		}
		
		request.setAttribute("cart", cart);
		*/
		
		return "f:/jsps/cart/list.jsp";
	}
	
	/*
	 * 通过bid添加购物车条目,页面应该保持在原来的页面，可以用此方法来响应AJAX请求
	 */
	public void addToCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// 从session得到购物车对象
		Cart cart=(Cart) request.getSession().getAttribute("cart");
		if(cart==null){
			System.out.println("cart is null");
			return ;
		}
		String bid=request.getParameter("bid");
		String p_count=request.getParameter("count");
		//如果参数为空，则返回不处理
		if(bid==null || p_count==null){
			return ;
		}
		
		int count=Integer.parseInt(p_count);
		
		//如果购物车商品条目即bid已经存在，则单纯添加商品数量即可
		if(cart.getCartItemMap().keySet().contains(bid)){
			cart.getCartItemMap().get(bid).addCount(count);
		}else{		
			//通过bid对象来查询生成对应的book对象
			BookService bookService=new BookService();
			Book book=bookService.queryByBid(bid);
			
			//构建条目并插入
			CartItem cartItem=new CartItem(count, book);
			cart.addItem(bid, cartItem);
		}					
		
		request.getSession().setAttribute("cart", cart);
		
		//对AJAX请求返回成功信息
		response.getWriter().print("OK");
		
	}
	
	/*
	 * 通过bid删除购物车条目
	 */
	public String removeFromCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		Cart cart=(Cart) request.getSession().getAttribute("cart");
		
		String bid=request.getParameter("bid");		
						
		cart.deleteItem(bid);
		
		request.getSession().setAttribute("cart", cart);
		
		return "f:/jsps/cart/list.jsp";
		
	}
	
	/*
	 * 清空购物车
	 */
	public String emptyCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Cart cart=(Cart) request.getSession().getAttribute("cart");
					
		cart.getCartItemMap().clear();				
				
		request.setAttribute("cart", cart);
		
		return "f:/jsps/cart/list.jsp";
	}
}
