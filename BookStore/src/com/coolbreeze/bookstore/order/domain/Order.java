package com.coolbreeze.bookstore.order.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.coolbreeze.bookstore.user.domain.User;


/*
 * 订单实体
 */
public class Order {
	private String oid;	//订单编号
	private User owner;	//订单主人,User实体
	private short state;  //订单状态，未付款，付款未发货，发货未签收，签收结束
	private Date ordertime;	//下订单时间 Date为java.util.Date
	private String address;	//订单收获地址（包括收货人）	
	private List<OrderItem> orderItemList=new LinkedList<OrderItem>();//订单条目list
	
	//获取总金额,最好使用double，使用float即使用了BigDecimal依然会有精度问题
	public double getTotal(){
		
		BigDecimal total=new BigDecimal("0");
		
		Iterator<OrderItem> iterator=this.orderItemList.iterator();
		while(iterator.hasNext()){
			total=total.add(new BigDecimal(iterator.next().getSubTotal()+""));
		}
		
		return total.doubleValue();
	}
		
	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
	
	
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public short getState() {
		return state;
	}
	public void setState(short state) {
		this.state = state;
	}
	public Date getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Order [oid=" + oid + ", owner=" + owner + ", state=" + state
				+ ", ordertime=" + ordertime + ", address=" + address
				+ ", orderItemList=" + orderItemList + "]";
	}	
	
}
