package com.coolbreeze.bookstore.book.web.servlet.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coolbreeze.bookstore.book.domain.Book;
import com.coolbreeze.bookstore.book.service.BookService;
import com.coolbreeze.bookstore.category.domain.Category;
import com.coolbreeze.bookstore.category.service.CategoryService;
import com.coolbreeze.commons.CommonUtils;
import com.coolbreeze.web.servlet.BaseServlet;

public class AdminBookServlet extends BaseServlet {

	private BookService bookService=new BookService();
	private CategoryService categoryService=new CategoryService();
	
	/*
	 * 查看所有商品图书
	 */
	public String queryAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		List<Book> bookList=bookService.queryAll();
		
		request.setAttribute("bookList",bookList);
		
		return "f:/adminjsps/admin/book/list.jsp";
	}
	
	
	
	/*
	 * 通过bid查看单个图书详细信息
	 */
	public String queryByBid(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		Book book=bookService.queryByBid(request.getParameter("bid"));
		
		request.setAttribute("book",book);
		//发送分类信息
		List<Category> categoryList=categoryService.queryAll();
		//System.out.println(categoryList);
		request.setAttribute("categoryList",categoryList );
		
		return "f:/adminjsps/admin/book/desc.jsp";
	}
	
	
	
	/*
	 *  前往增加图书页面
	 */
	public String toAddBookPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//发送分类信息
		request.setAttribute("categoryList", categoryService.queryAll());
			
		return "f:/adminjsps/admin/book/add.jsp";
	}
	
	
	/*
	 * 修改图书
	 */
	public String modifyBook(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//封装bid，bname，author，price等信息到book类
		Book book=CommonUtils.toBean(request.getParameterMap(), Book.class);
		
		//封装只带cid的category类到book类
		String cid=request.getParameter("cid");
		Category category=new Category();
		category.setCid(cid);
		book.setCategory(category);
		
		
		bookService.modifyBook(book);
		
		request.setAttribute("msg", "修改成功！");
		return "f:/adminjsps/msg.jsp";
	}
	

	/*
	 * 删除图书
	 */
	public String deleteBook(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		bookService.deleteBook(request.getParameter("bid"));
		
		request.setAttribute("msg", "删除成功！");
		return "f:/adminjsps/msg.jsp";
	}

}
