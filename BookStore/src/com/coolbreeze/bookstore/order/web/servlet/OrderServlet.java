package com.coolbreeze.bookstore.order.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coolbreeze.bookstore.book.domain.Book;
import com.coolbreeze.bookstore.book.service.BookService;
import com.coolbreeze.bookstore.cart.domain.Cart;
import com.coolbreeze.bookstore.cart.domain.CartItem;
import com.coolbreeze.bookstore.order.domain.Order;
import com.coolbreeze.bookstore.order.domain.OrderItem;
import com.coolbreeze.bookstore.order.service.OrderException;
import com.coolbreeze.bookstore.order.service.OrderService;
import com.coolbreeze.bookstore.user.domain.User;
import com.coolbreeze.commons.CommonUtils;
import com.coolbreeze.web.servlet.BaseServlet;

public class OrderServlet extends BaseServlet {
	
	private OrderService orderService=new OrderService();
	
	/*
	 * 点击商品详情页面购买，直接由商品生成订单
	 */
	public String goodsBecomeOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		String bid=request.getParameter("bid");
		String p_count=request.getParameter("count");
		
		if(bid==null || p_count==null){
			return null;
		}
		
		//为了防止异常，需要上面先判断是否为空
		int count=Integer.parseInt(p_count);
		
		
		Order order=new Order();
		//设置oid
		order.setOid(CommonUtils.uuid());
		//设置owner,这个uid一定不能为空，因为是外键关联
		User user=(User) request.getSession().getAttribute("user");
		order.setOwner(user);		
		//设置ordertime为系统当前时间
		order.setOrdertime(new Date(System.currentTimeMillis()));
		/*
		 * 添加订单条目
		 */		
		OrderItem orderItem=new OrderItem();
		//添加iid
		orderItem.setIid(CommonUtils.uuid());
		Book book=new BookService().queryByBid(bid);
		orderItem.setBook(book);
		orderItem.setCount(count);	
		orderItem.setOrder(order);
		order.getOrderItemList().add(orderItem);		
		
		//设置订单状态state		
		//short i=1;
		order.setState((short) 1);
		
		
		orderService.addOrder(order);
		
		request.setAttribute("order",order);
		return "f:/jsps/order/desc.jsp";
	}
	
	
	
	/*
	 * 购物车一键生成订单，
	 * 并且生成订单之后要清空购物车
	 */
	public String cartBecomeOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//1.从session中取出cart购物车
		Cart cart=(Cart) request.getSession().getAttribute("cart");
		
		/*
		 * 2.通过购物车来生成order订单对象
		 */
		Order order=new Order();
		//设置oid
		order.setOid(CommonUtils.uuid());
		//设置owner
		User user=(User) request.getSession().getAttribute("user");
		order.setOwner(user);		
		//设置ordertime为系统当前时间
		order.setOrdertime(new Date(System.currentTimeMillis()));
		//添加订单条目
		for(CartItem item : cart.getCartItemMap().values()){
			OrderItem orderItem=new OrderItem();
			
			orderItem.setIid(CommonUtils.uuid());
			orderItem.setCount(item.getCount());
			orderItem.setBook(item.getBook());
			orderItem.setOrder(order);
			
			order.getOrderItemList().add(orderItem);
		}
		//设置订单状态state		
		//short i=1;
		order.setState((short) 1);
		
		//3.调用orderService#addOrder方法添加订单和订单条目到数据库
		orderService.addOrder(order);
		
		//4.清空购物车
		cart.getCartItemMap().clear();
		
		//5.转发到/jsps/order/desc.jsp页面给用户填写收获地址和付款
		request.setAttribute("order", order);
		
		return "f:/jsps/order/desc.jsp";
	}
	
	
	/*
	 * 查看我的订单，通过uid来查询订单
	 */
	public String queryMyOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//得到session中的uid
		String uid=((User)request.getSession().getAttribute("user")).getUid();
		
		List<Order> orderList=orderService.queryMyOrder(uid);
		
		request.setAttribute("orderList",orderList);
		return "f:/jsps/order/list.jsp";
		
	}
	
	
	/*
	 * 在订单列表点击付款前往结算页面，通过oid来查询订单
	 */
	public String toPayPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//得到要付款的订单oid
		String oid=request.getParameter("oid");
		
		Order order=orderService.toPlay(oid);
		
		request.setAttribute("order",order);
		return "f:/jsps/order/desc.jsp";
		
	}
	
	/*
	 * 支付方法之去银行付款页面，使用第三方支付平台易宝来实现
	 * 	易宝接口：https://www.yeepay.com/app-merchant-proxy/node
	 */
	public String toPay(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//1.收集收获地址保存到数据库
		String oid=request.getParameter("oid");
		String address=request.getParameter("address");
		orderService.updateOrderAddress(oid,address);	
		
		//2.构建发送给易宝接口的数据
		/*
		 * 加载商店帐号信息：易宝用户名，keyValue，银行回调地址
		 */
		Properties props=new Properties();
		InputStream in=this.getClass().getClassLoader().getResourceAsStream("MerchantAccountInfo.properties");
		props.load(in);
		/*
		 * 13参数：易宝接口帮助文档中有说明
		 */
		String p0_Cmd = "Buy";
		String p1_MerId = props.getProperty("p1_MerId");
		String p2_Order = oid;
		//应付金额
		//String p3_Amt = "0.01";		
		double total=orderService.queryTotalByOid(oid);
		String p3_Amt = ""+total;
		//System.out.println(p3_Amt);
		
		String p4_Cur = "CNY";
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";					
		String p8_Url = props.getProperty("p8_Url");
		String p9_SAF = "";
		String pa_MP = "";
		String pd_FrpId = request.getParameter("pd_FrpId");//支付通道：即选择哪家银行支付
		String pr_NeedResponse = "1";	//使用户重定向回来，但是此种方式不安全
		
		//3.生成hmac值：此值用于校验是否有人在传输过程中修改了参数
		String keyValue = props.getProperty("keyValue");
		String hmac=PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);
		
		//4.构建请求易宝网关的url：参数包括13参数+hmac值
		StringBuilder url = new StringBuilder(props.getProperty("url"));
		url.append("?p0_Cmd=").append(p0_Cmd);
		url.append("&p1_MerId=").append(p1_MerId);
		url.append("&p2_Order=").append(p2_Order);
		url.append("&p3_Amt=").append(p3_Amt);
		url.append("&p4_Cur=").append(p4_Cur);
		url.append("&p5_Pid=").append(p5_Pid);
		url.append("&p6_Pcat=").append(p6_Pcat);
		url.append("&p7_Pdesc=").append(p7_Pdesc);
		url.append("&p8_Url=").append(p8_Url);
		url.append("&p9_SAF=").append(p9_SAF);
		url.append("&pa_MP=").append(pa_MP);
		url.append("&pd_FrpId=").append(pd_FrpId);
		url.append("&pr_NeedResponse=").append(pr_NeedResponse);
		url.append("&hmac=").append(hmac);

		//System.out.println(url);
		
		//5.重定向到易宝
		response.sendRedirect(url.toString());
				
		return null;
	}
	
	
	/*
	 * 支付之银行(易宝)回调方法，
	 * 在此项目中，因为没有固定ip，所以采用易宝让客户端重定向请求此方法的方式来通知
	 * 商店用户已经支付成功了，所以用户必须重定向此方法，否则订单状态不会改变
	 */
	public String callback(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//1.获取易宝发送过来的数据:11个参数+1个hmac校验值
		String p1_MerId = request.getParameter("p1_MerId");
		String r0_Cmd = request.getParameter("r0_Cmd");
		String r1_Code = request.getParameter("r1_Code");
		String r2_TrxId = request.getParameter("r2_TrxId");
		String r3_Amt = request.getParameter("r3_Amt");
		String r4_Cur = request.getParameter("r4_Cur");
		String r5_Pid = request.getParameter("r5_Pid");
		String r6_Order = request.getParameter("r6_Order");
		String r7_Uid = request.getParameter("r7_Uid");
		String r8_MP = request.getParameter("r8_MP");
		String r9_BType = request.getParameter("r9_BType");

		String hmac = request.getParameter("hmac");

		
		/*
		 * 得到keyValue
		 */
		Properties props=new Properties();
		InputStream in=this.getClass().getClassLoader().getResourceAsStream("MerchantAccountInfo.properties");
		props.load(in);
		String keyValue = props.getProperty("keyValue");
		
		//2.验证hmac值的正确与否，判断是否为易宝的请求，防止别人故意伪造请求		
		boolean isYeePay=PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType, keyValue);
		
		if(!isYeePay){
			request.setAttribute("msg", "请不要伪造支付回调请求！");
			return "f:/jsps/order/msg.jsp";
		}
		
		orderService.toPay(r6_Order);
		
		request.setAttribute("msg", "支付成功，请等待商店发货！");
		return "f:/jsps/order/msg.jsp";
	}
	
	
	/*
	 * 确认收货，通过oid来更改订单的状态state为4（已经收货，订单结束）
	 */
	public String confirmReceipt(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//得到要确认收货的订单oid
		String oid=request.getParameter("oid");
		
		try {
			orderService.confirmReceipt(oid);
		} catch (OrderException e) {
			request.setAttribute("msg",e.getMessage());
			return "f:/jsps/order/msg.jsp";
		}
		
		request.setAttribute("msg","已经确认收货了，您付的款会立即到达商家的账户！");
		return "f:/jsps/order/msg.jsp";
	}
}

