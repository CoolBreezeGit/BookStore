package com.coolbreeze.bookstore.category.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coolbreeze.bookstore.category.domain.Category;
import com.coolbreeze.bookstore.category.service.CategoryService;
import com.coolbreeze.web.servlet.BaseServlet;

public class CategoryServlet extends BaseServlet {
	
	private CategoryService categoryService=new CategoryService();
	
	public String listCategory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		List<Category> categoryList=categoryService.queryAll();
		
		request.setAttribute("categoryList", categoryList);
		
		return "f:jsps/left.jsp";
	}
}
