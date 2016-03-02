package com.coolbreeze.bookstore.order.service;

import java.sql.SQLException;
import java.util.List;

import com.coolbreeze.bookstore.order.dao.OrderDao;
import com.coolbreeze.bookstore.order.domain.Order;
import com.coolbreeze.jdbc.JdbcUtils;


public class OrderService {

	private OrderDao orderDao=new OrderDao();
	
	
	/*
	 * 添加订单
	 * 应该是一个事务操作：
	 * 	添加订单和添加订单条目之间不能中断！！！
	 */
	public void addOrder(Order order) {
		try{
			//1.开启事务
			JdbcUtils.beginTransaction();
			/*
			 * 2.执行操作
			 */
			//2.1添加订单
			orderDao.addOrder(order);
			//2.2添加订单条目
			orderDao.addOrderItem(order.getOrderItemList());
			//3.提交事务
			JdbcUtils.commitTransaction();
		}catch(Exception e){
			//查看异常信息
			e.printStackTrace();
			
			//发生异常回滚事物
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
				throw new RuntimeException(e1);
			}
		}
			
	}

	/*
	 * 通过uid查询订单记录，在通过订单id查询订单对应的订单条目
	 */
	public List<Order> queryMyOrder(String uid) {
		
		return orderDao.queryOrderByUid(uid);
		
	}

	/*
	 * 通过oid查询订单去支付订单
	 */
	public Order toPlay(String oid) {
		
		return orderDao.queryOrderByOid(oid);
		
	}

	/*
	 * 通过oid来确认收货,设置订单state为4
	 * 	但是确认收货之前必须校验，也就是state状态为3！已发货但是为收货
	 */
	public void confirmReceipt(String oid) throws OrderException {
		//校验订单状态，如果错误，抛出自定义异常
		short state=orderDao.queryOrderStateByOid(oid);
		if(state !=3){
			throw new OrderException("确认收货失败，确认之前订单状态必须是已经发货但未收货！");
		}
		orderDao.changeOrderState(oid,(short)4);
	}

	/*
	 * 更新订单的收获地址
	 */
	public void updateOrderAddress(String oid,String address) {
		orderDao.updateOrderAddress(oid,address);
	}
	
	/*
	 * 用户支付
	 * 如果用户已经支付了，也就是订单状态state不为1,则什么都不操作
	 * 否则更新state为2：已经付款，但是未发货
	 */
	public void toPay(String oid) {
		
		short state=orderDao.queryOrderStateByOid(oid);
		
		if(state==1){
			orderDao.changeOrderState(oid, (short)2);
		}
		
	}

	/*
	 * 查询所有订单
	 */
	public List<Order> queryAllOrder() {
		return orderDao.queryAllOrder();
	}

	/*
	 * 根据state来查询四种状态的订单
	 */
	public List<Order> queryOrderByState(int state) {
		return orderDao.queryByState(state);
	}

	/*
	 * 通过订单号来查询订单总金额
	 */
	public double queryTotalByOid(String oid) {
		return orderDao.queryTotalByOid(oid);
	}

}
