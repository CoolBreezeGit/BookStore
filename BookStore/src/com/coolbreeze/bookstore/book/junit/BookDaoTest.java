package com.coolbreeze.bookstore.book.junit;

import java.util.List;

import org.junit.Test;

import com.coolbreeze.bookstore.book.dao.BookDao;
import com.coolbreeze.bookstore.book.domain.Book;

/*
 * 测试BookDao类
 */
public class BookDaoTest {

	BookDao bookDao=new BookDao();
	
	/*
	 * 测试queryAll方法
	 */
	@Test
	public void testQueryAll(){
		
		List<Book> list=bookDao.queryAll();
				
		System.out.println(list);
	}
	
	/*
	 * 测试queryByCid方法
	 */
	@Test
	public void testQueryByCid(){
		String cid="c1";
		
		List<Book> list=bookDao.queryByCid(cid);
				
		System.out.println(list);
	}
	
}
