package com.coolbreeze.bookstore.order.domain;

import java.math.BigDecimal;

import com.coolbreeze.bookstore.book.domain.Book;


/*
 * 订单条目实体
 */
public class OrderItem {

	private String iid;
	private int count;	//商品数量	
	private Book book;	//商品book类,对应bid
	private Order order;	//所属订单，实体，对应oid
	
	public String getIid() {
		return iid;
	}
	public void setIid(String iid) {
		this.iid = iid;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	/*
	 * 得到小计金额
	 */
	public double getSubTotal() {
		//注意使用string来生成BigDecimal类
		BigDecimal _count=new BigDecimal(count+"");
		BigDecimal price=new BigDecimal(book.getPrice()+"");
		return price.multiply(_count).doubleValue();
	}

	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	
	
	@Override
	public String toString() {
		return "OrderItem [iid=" + iid + ", count=" + count + ", book=" + book
				+ ", order=" + order + "]";
	}
		
	
	
}
