package com.coolbreeze.bookstore.book.domain;

import com.coolbreeze.bookstore.category.domain.Category;


public class Book {

	private String bid;
	private String bname;
	private String author;
	private double price;		//价格
	private String image;
	//private String cid;
	private Category category;		//应该把cid变成实体模型！！！
	private boolean del;		//是否假删除
	
	
	public boolean getDel() {
		return del;
	}
	public void setDel(boolean del) {
		this.del = del;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getBname() {
		return bname;
	}
	public void setBname(String bname) {
		this.bname = bname;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}	
	
}
