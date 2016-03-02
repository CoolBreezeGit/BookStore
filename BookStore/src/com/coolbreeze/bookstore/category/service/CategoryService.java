package com.coolbreeze.bookstore.category.service;

import java.util.List;

import com.coolbreeze.bookstore.book.dao.BookDao;
import com.coolbreeze.bookstore.category.dao.CategoryDao;
import com.coolbreeze.bookstore.category.domain.Category;

public class CategoryService {
	
	CategoryDao categoryDao=new CategoryDao();	
	
	/*
	 * 查询所有分类
	 */
	public List<Category> queryAll(){
		return categoryDao.queryAll();
		
	}


	/*
	 * 删除目录分类
	 * 	但是要注意问题：cid被books表外键关联，不能随意删除！！
	 * 			所以只能删除空分类，也就是此分类下没有商品图书
	 */
	public void deleteCategory(String cid) throws CategoryException {
		int count=new BookDao().queryBookCountByCid(cid);
		if(count==0){
			categoryDao.deleteCategoryByCid(cid);
		}else{
			//抛出自定义异常
			throw new CategoryException("该分类下有图书，不能删除！");
		}
	}

	
	/*
	 * 添加分类
	 */
	public void addCategory(Category category) {
		categoryDao.addCategory(category);
	}


	/*
	 * 修改分类：名称
	 */
	public void modifyCategory(String cid, String cname) {
		categoryDao.modifyCategoryName(cid,cname);
	}
	
}
