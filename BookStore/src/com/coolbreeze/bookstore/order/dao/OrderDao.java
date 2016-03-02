package com.coolbreeze.bookstore.order.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.coolbreeze.bookstore.book.domain.Book;
import com.coolbreeze.bookstore.order.domain.Order;
import com.coolbreeze.bookstore.order.domain.OrderItem;
import com.coolbreeze.jdbc.TxQueryRunner;

public class OrderDao {

	private TxQueryRunner tqr=new TxQueryRunner();
	
	/*
	 * 添加订单到数据库orders表 
	 */
	public void addOrder(Order order) {
		
		//System.out.println("addOrder");
		
		//这句话居然导致了StackOverFlow。。。应该是太多内容了
		//System.out.println(order);
		
		String sql="insert into orders values (?,?,?,?,?,?)";
		
		//java.util.Date 变成sql时间需要转换
		Timestamp orderTime=new Timestamp(order.getOrdertime().getTime());
		
		Object[] params={order.getOid(),orderTime,
							order.getTotal(),order.getState(),
							order.getOwner().getUid(),order.getAddress()};
		//System.out.println(params);
		
		try {
			tqr.update(sql,params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * 添加订单条目到数据库orderitems表
	 * 因为是多条记录，可以使用批处理添加记录
	 */
	public void addOrderItem(List<OrderItem> orderItemList){
		
		String sql="insert into orderitems values (?,?,?,?,?)";
		
		/*
		 * 构造二维参数数组，多个一位数组的数组
		 */
		Object[][] params=new Object[orderItemList.size()][];		
		for(int i=0;i<orderItemList.size();i++){
			OrderItem item=orderItemList.get(i);
			params[i]=new Object[]{item.getIid(),item.getCount(),item.getSubTotal(),
									item.getBook().getBid(),item.getOrder().getOid()};
		}
		
		//批处理update
		try {
			tqr.batch(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}

	/*
	 * 通过uid查询订单记录
	 */
	public List<Order> queryOrderByUid(String uid) {
		List<Order> orderList=null;
		
		//通过uid查询订单，并按订单时间最新到最久排序
		String sql="select * from orders where uid=? order by ordertime desc";
		try {
			orderList=tqr.query(sql, new BeanListHandler<Order>(Order.class), uid);
			
			//填充订单的订单条目
			fillItemToOrderList(orderList);	
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return orderList;
	}
	
	/*
	 * 填充订单的订单条目
	 */
	public void fillItemToOrderList(List<Order> orderList){
		
		for(int i=0;i<orderList.size();i++){
			Order order=orderList.get(i);
			order.setOrderItemList(this.queryOrderItemByOid(order.getOid()));				
		}		
	}
	
	
	/*
	 * 方法一：
	 * 通过oid查询订单条目记录
	 * 并根据bid将book类填充订单条目类的book属性上
	 * 但是方式过于复杂！！
	 */
	/*public List<OrderItem> queryOrderItemByOid(String oid) {
		List<OrderItem> orderItemList=null;
		
		String sql="select * from orderitems where oid=?";
		try {
			orderItemList=tqr.query(sql, new BeanListHandler<OrderItem>(OrderItem.class), oid);						
			
			//为orderItemList填充book类
			 
			Connection con=JdbcUtils.getConnection();
			java.sql.PreparedStatement prps=con.prepareStatement(sql);
			prps.setString(1, oid);
			ResultSet result=prps.executeQuery();
			
			BookDao bookDao=new BookDao();
			
			int i=0;
			while(result.next()){
				
				String bid=result.getString("bid");
				//System.out.println(bid);
				Book book=bookDao.queryByBid(bid);
				orderItemList.get(i).setBook(book);
				i++;
			}
			
			con.close();
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return orderItemList;
	}*/
	
	
	/*
	 * 方法二：
	 * 通过oid查询订单条目记录
	 * 并根据bid将book类填充订单条目类的book属性上
	 * 使用MapListHandle来处理
	 */
	/*
	public List<OrderItem> queryOrderItemByOid(String oid) {
		List<Map<String,Object>> mapList=null;
		List<OrderItem> orderItemList=new LinkedList<OrderItem>();
		//同时查询orederitems和boos表格
		String sql="select * from orderitems i,books b where i.bid=b.bid and i.oid=?";
		try {
			//得到mapList，结果表格对应成多个键值对
			mapList=tqr.query(sql, new MapListHandler(),oid);
			//使用map成多个orderItem
			for(Map map : mapList){			
				//构建OrderItem
				
				OrderItem orderItem=new OrderItem();				
				orderItem.setIid((String)map.get("iid"));
				orderItem.setCount((Integer)(map.get("count")));
				
				//利用tobean工具来生成orderItem类
				OrderItem orderItem=CommonUtils.toBean(map, OrderItem.class);
				
				//填充Book
				
				Book book=new Book();
				book.setBid((String)map.get("bid"));
				book.setBname((String)map.get("bname"));
				book.setImage((String)map.get("image"));
				book.setAuthor((String)map.get("author"));
				book.setPrice(((BigDecimal)map.get("price")).doubleValue());
				
				Book book=CommonUtils.toBean(map, Book.class);
				
				//为orderItem填充book
				orderItem.setBook(book);				
				//添加orderItem
				orderItemList.add(orderItem);
			}					
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return orderItemList;
	}
	*/
	
	/*
	 * 方法三：
	 * 通过oid查询订单条目记录
	 * 并根据bid将book类填充订单条目类的book属性
	 * 此方法导致了两次查询，有些费性能，但是最简便。。
	 */
	public List<OrderItem> queryOrderItemByOid(String oid) {
		List<OrderItem> orderItemList=new LinkedList<OrderItem>();
		List<Book> bookList=new LinkedList<Book>();
		
		//同时查询orederitems和boos表格
		String sql="select * from orderitems i,books b where i.bid=b.bid and i.oid=?";
		try {
			//得到订单条目列表和商品列表
			orderItemList=tqr.query(sql, new BeanListHandler<OrderItem>(OrderItem.class),oid);
			bookList=tqr.query(sql, new BeanListHandler<Book>(Book.class),oid);
				
			//填充每个订单条目的book类
			for(int i=0;i<orderItemList.size();i++){
				orderItemList.get(i).setBook(bookList.get(i));
			}
		
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return orderItemList;
	}
	

	/*
	 * 通过oid来查询订单
	 */
	public Order queryOrderByOid(String oid) {		
		String sql="select * from orders where oid=?";
		Order order=null;	
		try{
			order=tqr.query(sql,new BeanHandler<Order>(Order.class),oid);	
			//填充订单条目
			order.setOrderItemList(this.queryOrderItemByOid(oid));
		}catch(SQLException e){
			throw new RuntimeException(e);
		}		 	
		return order;
	}
	
	

	/*
	 * 根据oid来查询订单的状态
	 */
	public short queryOrderStateByOid(String oid) {
		
		String sql="select state from orders where oid=?";
		
		try{
			/*
		   Number n=(Number) tqr.query(sql,new ScalarHandler(),oid);
		   state=n.shortValue();
		   */
			return ((Number)tqr.query(sql,new ScalarHandler(),oid)).shortValue();
		}catch(SQLException e){
			throw new RuntimeException(e);
		}		
		
	}
	
	

	/*
	 * 修改指定oid的订单状态state
	 */
	public void changeOrderState(String oid, short i) {
		String sql="update orders set state=? where oid=?";
		Object[] params={i,oid};
		try{
			tqr.update(sql,params);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}

	
	/*
	 * 更新指定oid的订单的收获地址
	 */
	public void updateOrderAddress(String oid,String address) {
		String sql="update orders set address=? where oid=?";
		Object[] params={address,oid};
		try{
			tqr.update(sql,params);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}

	/*
	 * 查询所有订单
	 */
	public List<Order> queryAllOrder() {
		String sql="select * from orders";
		try{
			List<Order> orderList=tqr.query(sql,new BeanListHandler<Order>(Order.class));
			
			fillItemToOrderList(orderList);
			
			return orderList;
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}

	
	/*
	 * 查询指定state的订单
	 */
	public List<Order> queryByState(int state) {	
		String sql="select * from orders where state=?";
		try{
			List<Order> orderList=tqr.query(sql,new BeanListHandler<Order>(Order.class),state);
			
			fillItemToOrderList(orderList);
			
			return orderList;
		}catch(SQLException e){
			throw new RuntimeException(e);
		}	
	}

	public double queryTotalByOid(String oid) {
		String sql="select total from orders where oid=?";
		try{
			Number total=(Number) tqr.query(sql,new ScalarHandler(),oid);
			
			return total.doubleValue();
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	
}
