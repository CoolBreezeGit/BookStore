package com.coolbreeze.bookstore.category.web.servlet.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coolbreeze.bookstore.category.domain.Category;
import com.coolbreeze.bookstore.category.service.CategoryException;
import com.coolbreeze.bookstore.category.service.CategoryService;
import com.coolbreeze.commons.CommonUtils;
import com.coolbreeze.web.servlet.BaseServlet;

/*
 * 管理员category servlet
 */
public class AdminCategoryServlet extends BaseServlet {

	private CategoryService categoryService=new CategoryService();
	
	
	/*
	 * 查看所有分类
	 */
	public String queryAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {			
		List<Category> categoryList=categoryService.queryAll();
		
		request.setAttribute("categoryList", categoryList);
		
		return "f:/adminjsps/admin/category/list.jsp";
	}
	
	
	/*
	 * 前往分类修改页面
	 */
	public String toModifyPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Category category=new Category();
		category.setCid(request.getParameter("cid"));
		category.setCname(request.getParameter("cname"));
		
		request.setAttribute("category",category);
		
		return "f:/adminjsps/admin/category/mod.jsp";
	}
	
	/*
	 * 修改分类
	 */
	public String modifyCategory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {		
		 String cid=request.getParameter("cid");
		 String cname=request.getParameter("cname");
		 
		 if(cid==null || cname==null){
			 return null;
		 }
		 
		categoryService.modifyCategory(cid,cname);
		request.setAttribute("msg", "成功修改分类！");
		return "f:/adminjsps/msg.jsp";
	}
	
	
	/*
	 * 通过cid来删除目录
	 */
	public String deleteCategory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cid=request.getParameter("cid");
		
		if(cid==null){
			request.setAttribute("msg", "参数不能为空！");
			 
			 return "f:/adminjsps/msg.jsp";
		}
		
		try {
			categoryService.deleteCategory(cid);
		} catch (CategoryException e) {
			request.setAttribute("msg",e.getMessage());
			return "f:/adminjsps/msg.jsp";
		}
		
		request.setAttribute("categoryList", categoryService.queryAll());		 
		return "f:/adminjsps/admin/category/list.jsp";
	
	}
	
	
	/*
	 * 添加分类
	*/
	 public String addCategory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		 
		 Category category=CommonUtils.toBean(request.getParameterMap(), Category.class);
		 category.setCid(CommonUtils.uuid());
		 
		 categoryService.addCategory(category);
		 
		 request.setAttribute("msg", "添加分类成功！");
		 
		 return "f:/adminjsps/msg.jsp";
	 }
	
	
}
