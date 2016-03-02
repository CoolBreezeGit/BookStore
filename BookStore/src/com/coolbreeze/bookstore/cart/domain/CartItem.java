package com.coolbreeze.bookstore.cart.domain;

import java.math.BigDecimal;

import com.coolbreeze.bookstore.book.domain.Book;

/*
 * 购物车条目
 */
public class CartItem {
	private int count;
	private Book book;
	
	/*
	 * 小计 属性subTotal
	 */
	public double getSubTotal(){
		//使用stringl来生成BigDecimal类
		BigDecimal _count=new BigDecimal(count+"");
		BigDecimal price=new BigDecimal(book.getPrice()+"");
		return price.multiply(_count).doubleValue();
	}
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	//增加商品数量方法，方便购物车同类条目的增加操作！
	public void addCount(int count){
		this.count=this.count+count;
	}
	
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	
	public CartItem(int count, Book book) {
		super();
		this.count = count;
		this.book = book;
	}
	
	
}
