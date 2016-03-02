package com.coolbreeze.bookstore.book.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coolbreeze.bookstore.book.domain.Book;
import com.coolbreeze.bookstore.book.service.BookService;
import com.coolbreeze.web.servlet.BaseServlet;

public class BookServlet extends BaseServlet {
	
	private BookService bookService=new BookService();
	
	/*
	 * 查询所有图书
	 */
	public String queryAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {		
		
		List<Book> bookList=bookService.queryAll();
		
		request.setAttribute("bookList", bookList);
		
		return "f:/jsps/book/list.jsp";
	}
	
	/*
	 * 按图书分类cid查询图书
	 */
	public String queryByCid(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String cid=request.getParameter("cid");
		if(cid==null){
			return null;
		}
		
		List<Book> bookList=bookService.queryByCid(cid);
		
		request.setAttribute("bookList", bookList);
		
		return "f:/jsps/book/list.jsp";
	}
	
	/*
	 * 按图书bid查询图书
	 */
	public String queryByBid(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String bid=request.getParameter("bid");
		if(bid==null){
			return null;
		}
		
		Book book=bookService.queryByBid(bid);		
		
		request.setAttribute("book", book);
		
		return "f:/jsps/book/desc.jsp";
	}
}
