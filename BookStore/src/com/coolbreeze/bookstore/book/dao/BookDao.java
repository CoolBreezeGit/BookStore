package com.coolbreeze.bookstore.book.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.coolbreeze.bookstore.book.domain.Book;
import com.coolbreeze.bookstore.category.domain.Category;
import com.coolbreeze.jdbc.TxQueryRunner;

public class BookDao {

	private TxQueryRunner tqr=new TxQueryRunner();
	
	/*
	 * 查询所有图书
	 */
	public List<Book> queryAll(){
		
		String sql="select * from books where del=false";
		List<Book> bookList=null;
		try {
			bookList=tqr.query(sql, new BeanListHandler<Book>(Book.class));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return bookList;
	}
	
	/*
	 * 通过cid图书分类来查询图书
	 */
	public List<Book> queryByCid(String cid){
		String sql="select * from books where cid=? and del=false";
		List<Book> bookList=null;
		try {
			bookList=tqr.query(sql, new BeanListHandler<Book>(Book.class),cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return bookList;
	}

	/*
	 * 通过图书bid查询图书
	 * 	并填充book类的category分类类
	 */
	public Book queryByBid(String bid) {
		String sql="select * from books b,category c where b.cid=c.cid and b.bid=? and b.del=false";
		Book book=null;
		try {
			book=tqr.query(sql, new BeanHandler<Book>(Book.class),bid);
			//通过查询生成Category类，并填充book类
			Category category=tqr.query(sql, new BeanHandler<Category>(Category.class),bid);
			book.setCategory(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return book;
	}

	/*
	 * 修改指定bid的book记录
	 */
	public void updateBook(Book book) {
		String sql="update books set bname=?,author=?,price=?,cid=? where bid=?";
		Object[] params={book.getBname(),book.getAuthor(),
				book.getPrice(),book.getCategory().getCid(),book.getBid()};
		try {
			tqr.update(sql,params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * 删除指定bid的book记录
	 */
	public void deleteBookByBid(String bid) {
		String sql="update books set del=true where bid=?";		
		try {
			tqr.update(sql,bid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	
	/*
	 * 向books表添加图书book对象
	 */
	public void addBook(Book book) {
		String sql="insert into books values(?,?,?,?,?,?,?)";
		Object[] params={book.getBid(),book.getBname(),book.getAuthor(),book.getPrice(),
							book.getImage(),book.getCategory().getCid(),book.getDel()};
		try{
			tqr.update(sql,params);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}

	
	/*
	 * 查询指定目录下有多少图书
	 */
	public int queryBookCountByCid(String cid) {
		String sql="select count(*) from books where cid=? and del=false";
		try{
			Number n=(Number) tqr.query(sql, new ScalarHandler(),cid);
			return n.intValue();
		}catch(SQLException e){
			throw new RuntimeException(e);
		} 
	}
}
