package com.coolbreeze.bookstore.order.junit;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.coolbreeze.bookstore.book.domain.Book;
import com.coolbreeze.bookstore.book.service.BookService;
import com.coolbreeze.bookstore.order.dao.OrderDao;
import com.coolbreeze.bookstore.order.domain.Order;
import com.coolbreeze.bookstore.order.domain.OrderItem;
import com.coolbreeze.bookstore.user.domain.User;
import com.coolbreeze.commons.CommonUtils;

/*
 * 测试OrderDao层方法
 */
public class OrderDaoTest {
	
	private OrderDao orderDao=new OrderDao();
	
	/*
	 * 测试AddOrder
	 */
	@Test
	public void testAddOrder(){
		Order order=new Order();
		
		//设置oid
		order.setOid(CommonUtils.uuid());
		//设置owner
		User user=new User();
		user.setUid("8A75408E5EAF480CA5D54121039EDDE0");
		order.setOwner(user);		
		//设置ordertime为系统当前时间
		order.setOrdertime(new Date(System.currentTimeMillis()));
		/*
		 * 添加订单条目
		 */		
		OrderItem orderItem=new OrderItem();
		//添加iid
		orderItem.setIid(CommonUtils.uuid());
		String bid="b9";
		Book book=new BookService().queryByBid(bid);
		orderItem.setBook(book);
		
		orderItem.setCount(3);	
		orderItem.setOrder(order);
		order.getOrderItemList().add(orderItem);	
		//设置订单状态state		
		//short i=1;
		order.setState((short) 1);
		
		//
		order.setAddress("jiangxi");
		
		orderDao.addOrder(order);		
	}
	
	
	/*
	 * 测试queryOrder等方法
	 */
	@Test
	public void testQueryOrder(){
		//通过uid得到所有订单
		String uid="089D46D6AF3F48F4A6EA0DCB4E2A7F68";
		List<Order> orderList=orderDao.queryOrderByUid(uid);
		
		//为每个订单填充订单条目对象
		for(int i=0;i<orderList.size();i++){
			Order order=orderList.get(i);
			order.setOrderItemList(orderDao.queryOrderItemByOid(order.getOid()));
			
		}
		
		//System.out.println(orderList);
		System.out.println("----------------------------");
		/*
		 * 测试自定义填充book类的顺序是否有错误
		 */
		for(Order order : orderList){
			for(OrderItem item : order.getOrderItemList()){
				System.out.println(item.getBook().getBid());
			}
		}
		
	}
	
	
	/*
	 * 测试updateOrderAddress方法
	 */
	@Test
	public void testUpdateOrderAddress(){
		String oid="179B5A55EF6E493EA7E5E822D0DB6E0C";
		//String address="china";
		String address="中国江西";
		
		orderDao.updateOrderAddress(oid, address);
	}
	
	
}
