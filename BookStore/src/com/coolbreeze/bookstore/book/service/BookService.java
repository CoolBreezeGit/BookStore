package com.coolbreeze.bookstore.book.service;

import java.util.List;

import com.coolbreeze.bookstore.book.dao.BookDao;
import com.coolbreeze.bookstore.book.domain.Book;

public class BookService {

	private BookDao bookDao=new BookDao();
	
	/*
	 * 查询所有图书
	 */
	public List<Book> queryAll(){
		return bookDao.queryAll();
	}
	
	/*
	 * 通过图书分类cid来查询图书
	 */
	public List<Book> queryByCid(String cid){
		return bookDao.queryByCid(cid);
	}

	/*
	 * 通过图书bid查询图书
	 */
	public Book queryByBid(String bid) {
		return bookDao.queryByBid(bid);
	}

	
	/*
	 * 修改图书book信息
	 */
	public void modifyBook(Book book) {
		
		bookDao.updateBook(book);
		
	}

	/*
	 * 删除图书
	 * 	注意：不能随意删除图书，因为orderitem表有bid的外键关联，会报错
	 * 		所以只能假删除，也就把book的del字段设置为true，使得之后查询不到此书籍
	 * 		而且用户的订单中还是能够看到曾经买过此图书，虽然再也查询不到此图书！！
	 */
	public void deleteBook(String bid) {
		bookDao.deleteBookByBid(bid);
	}

	/*
	 * 添加图书
	 */
	public void addBook(Book book) {
		bookDao.addBook(book);		
	}
}
