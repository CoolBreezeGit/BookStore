package com.coolbreeze.bookstore.category.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.coolbreeze.bookstore.category.domain.Category;
import com.coolbreeze.jdbc.TxQueryRunner;

public class CategoryDao {

	TxQueryRunner tqr=new TxQueryRunner();
	
	
	/*
	 * 查询所有的商品分类
	 */	
	public List<Category> queryAll(){
		String sql="select * from category";
		List<Category> categoryList=null;
		try {
			categoryList=tqr.query(sql, new BeanListHandler<Category>(Category.class));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return categoryList;
	}


	/*
	 *  通过cid来删除目录记录
	 */
	public void deleteCategoryByCid(String cid) {
		String sql="delete from category where cid=?";
		try {
			tqr.update(sql,cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	
	/*
	 * 通过cid修改分类名称cname
	 */
	public void modifyCategoryName(String cid, String cname) {
		String sql="update category set cname=? where cid=?";
		Object[] params={cname,cid};
		try {
			tqr.update(sql,params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}


	/*
	 * 添加新的分类
	 */
	public void addCategory(Category category) {
		String sql="insert into category values (?,?)";
		Object[] params={category.getCid(),category.getCname()};
		try {
			tqr.update(sql,params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
}
