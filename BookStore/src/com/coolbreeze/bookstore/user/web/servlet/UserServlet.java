package com.coolbreeze.bookstore.user.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coolbreeze.bookstore.cart.domain.Cart;
import com.coolbreeze.bookstore.user.domain.User;
import com.coolbreeze.bookstore.user.service.UserException;
import com.coolbreeze.bookstore.user.service.UserService;
import com.coolbreeze.commons.CommonUtils;
import com.coolbreeze.mail.Mail;
import com.coolbreeze.mail.MailUtils;
import com.coolbreeze.web.servlet.BaseServlet;

public class UserServlet extends BaseServlet {

	private UserService userService=new UserService();
	
	//用户注册相关方法
	
	/*
	 * 用户注册regist
	*/
	public String regist(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException, UserException {
		
		//1.封装用户的参数到user bean中	
		User user=CommonUtils.toBean(request.getParameterMap(), User.class);	
		
		/*
		 * 2.基础校验：虽然客户端有了js校验，但是服务端必须重新校验，防止js被禁用
		 * 		注意：每个参数基本都应该先判断是否为null，否则容易出现空指针异常！！
		 */
		Map<String,String> errorMap=new HashMap<String,String>();
		//2.1 用户名：不能太长或者为空
		if(user.getUsername()==null || user.getUsername().trim()==""){
			errorMap.put("username", "用户名不能为空！");
		}else if(user.getUsername().trim().length()>50){
			errorMap.put("username", "用户名不能长于50个字符！");
		}
		//2.2 密码：不能太短
		if(user.getPassword()==null || user.getPassword().length()<8){
			errorMap.put("password","密码长度不能少于8个字符");
		}else if(user.getPassword().length()>50){
			errorMap.put("password", "密码不能长于50个字符！");
		}
		//2.3 确认密码：两次输入密码要一致
		if(!user.getPassword().equals(request.getParameter("confirmPassword"))){
			errorMap.put("confirmPassword", "两次输入的密码buyizhi");
		}
		//2.4 邮箱：格式要正确
		if(user.getEmail()==null || !user.getEmail().matches("\\w+@\\w+\\.\\w+")){
			errorMap.put("email", "邮箱格式不对!");
		}
		//2.5 验证码校验
		String t_verifyCode=request.getParameter("verifyCode");
		String s_verifyCode=(String) request.getSession().getAttribute("regist_verifyCode");
		if(t_verifyCode == null || 
				s_verifyCode == null || 
				!t_verifyCode.toUpperCase().equals(s_verifyCode.toUpperCase())){
			errorMap.put("verifyCode", "验证码不正确！");
		}
			
		
		//如果有错误信息，则转发到regist.jsp回显
		if(errorMap.size()>0){
			//将基础校验得到的错误回显给客户
			request.setAttribute("error",errorMap);
			//把用户已经填写的数据再保存到页面，以免重写所有数据
			request.setAttribute("user", user);
			request.setAttribute("confirmPassword",request.getParameter("confirmPassword"));
			return "/jsps/user/regist.jsp";
		}
		
		//3..设置uid和激活码
		user.setUid(CommonUtils.uuid());
		user.setActivationcode(CommonUtils.uuid()+CommonUtils.uuid());
		
		//4.调用业务service层进行进一步校验注册，如果成功往下发送激活邮件，如果失败则把错误信息回显到regist.jsp页面
		try{
			userService.userRegist(user);
		}catch(UserException e){
			//将UserException的异常信息（用户名已经被注册）回显给客户机
			errorMap.put("username", e.getMessage());
			request.setAttribute("error",errorMap);
			//把用户已经填写的数据再保存到页面，以免重写所有数据
			request.setAttribute("user", user);
			return "/jsps/user/regist.jsp";
		}
		
		//5.保存user对象到session中，为重新发送邮件提供激活码，邮箱等参数
		request.getSession().setAttribute("user", user);
		
		//6.发送激活邮件
		sendActivationCodeLinkMail(request);
		
		//7.转发到msg页面提示用户激活,并且可以重新发送激活邮件
		request.setAttribute("msg", 
				"已经发送了激活码链接到您的邮箱"+user.getEmail()+",请到您的邮箱激活再登录。。<br/>"
				+ "如果您未收到邮件，请点击下方的'重新发送'来再次发送邮件：<br/>"
				+"<a href='"+request.getContextPath()+"/UserServlet?method=sendMailAgain'>重新发送</a>");
		
		return "f:/jsps/msg.jsp";
	}
	
	
	/*
	 * 响应用户点击重新发送激活码到邮箱的请求
	 */
	public String sendMailAgain(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		sendActivationCodeLinkMail(request);
		
		request.setAttribute("msg", "已经重新发送邮件到您的邮箱了，请注意查收！");
		
		return "f:/jsps/msg.jsp";
	}
	
	
	/*
	 * 发送激活码链接邮件到用户邮箱
	 */
	public void sendActivationCodeLinkMail(HttpServletRequest request){
		/*
		 * 1.加载EmailConfigure配置文件
		 */
		InputStream in=this.getClass().getClassLoader().getResourceAsStream("EmailConfigure.Properties");
		Properties prop=new Properties();
		try {
			prop.load(in);
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		
		/*
		 * 2.得到参数，发送邮件
		 */
		//2.1.得到smtp的连接session
		Session session=MailUtils.createSession(prop.getProperty("host"),
				prop.getProperty("username"),
				prop.getProperty("password"));
		
		//2.2.构建邮件
		//从seesion得到user对象
		User user=(User) request.getSession().getAttribute("user");
				
		//使用user对象的激活码来填充占位符
		String content=MessageFormat.format(prop.getProperty("content"), user.getActivationcode(),user.getUsername());
		Mail mail=new Mail(prop.getProperty("from"),
				user.getEmail(),
				prop.getProperty("subject"),
				content);
		
		//2.3.发送邮件
		try {
			MailUtils.send(session, mail);
		} catch (Exception e2) {
			throw new RuntimeException(e2);
		}	
	}
	

	/*
	 * 响应用户邮箱激活操作
	 */
	public String activate(HttpServletRequest request,HttpServletResponse response){
		
		String activationcode=request.getParameter("activationcode");
		
		//如果service层抛出UserException异常，说明激活失败，回显信息到msg.jsp
		try {
			userService.activate(activationcode);
		} catch (UserException e) {
			request.setAttribute("msg", e.getMessage());
			return "f:/jsps/msg.jsp";
		}
		
		//激活成功
		request.setAttribute("msg", "恭喜您，激活成功，请登录！");
		return "f:/jsps/msg.jsp";
	}
	
	
	
	
	
	//用户登录相关方法
	
	/*
	* 用户登录
	*/
	public String login(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			User user=CommonUtils.toBean(request.getParameterMap(), User.class);
			User t_user=null;
			String errorNum=null;
			try {
				t_user=userService.login(user);
			} catch (UserException e) {
				request.setAttribute("msg", e.getMessage());
				request.setAttribute("user", user);
				
				//判断登录错误次数
				errorNum=(String) request.getSession().getAttribute("errorNum");
				//System.out.println(errorNum);
				if(errorNum==null){
					//System.out.println("errorNum is null!");
					request.getSession().setAttribute("errorNum", "1");
				}else{
					request.getSession().setAttribute("errorNum", Integer.parseInt(errorNum)+1+"");
					//如果登录错误次数大于3,则需要输入验证码
					if(Integer.parseInt(errorNum) >= 3){
						//设置此属性，客户端判断tooManyError
						request.setAttribute("tooManyError", errorNum);
					}	
				}													
				
				return "f:/jsps/user/login.jsp";
			}
			//校验验证码
			String verifyCode=(String) request.getSession().getAttribute("login_verifyCode");
			//System.out.println(verifyCode);
			String form_verifyCode=request.getParameter("verifyCode");
			//System.out.println(form_verifyCode==null);
			
			/*
			 * 如果session中verifyCode，说明错误大于3次，已经需要验证码
			 * 但是这样做，会导致一个问题：
			 * 因为注册使用的也是 VerifyCodeServlet类，导致如果登录的时候，同时请求注册页面，
			 * 则造成session中存入了不同的值，导致验证码校验失败
			 * 需要避免这个问题：VerifyCodeServlet根据请求页面的不同，存放不同属性名字的验证码
			 */
			if(verifyCode!=null){
				//判断为空是为了防止用户模拟页面提交，导致可能出现的空指针异常！
				if(form_verifyCode==null || !verifyCode.toUpperCase().equals(form_verifyCode.toUpperCase())){
					request.setAttribute("user", user);
					request.setAttribute("tooManyError", "3");
					request.setAttribute("verifyCodeError", "验证码错误！");
					return "f:/jsps/user/login.jsp";
				}
			}
			
			//因为t_user包含更多用户信息，所以将查询出来t_user保存在session，
			request.getSession().setAttribute("user", t_user);
			//登录成功后，应去除登录错误次数和验证码session
			request.getSession().removeAttribute("errorNum");
			request.getSession().removeAttribute("login_verifyCode");					
						
			//在session中加入购物车！
			request.getSession().setAttribute("cart", new Cart());
			
			//重定向到原来要访问的页面！
			/*
			String preUrl=(String) request.getSession().getAttribute("preUrl");
			if(preUrl != null){
				response.sendRedirect(preUrl);
				//response.getWriter().write("<script>window.open("+preUrl+",'_parent');</script>");
				return null;
			}
			*/
			
			//重定向到主页
			//return "r:/index.jsp";
			//response.getWriter().write("<script>window.open("+request.getContextPath()+"/index.jsp"+",'_parent');</script>");
			return "r:/index.jsp";
	}
	
	
	/*
	 * 用户退出
	 */
	public  String logout(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//去除seesion中的user属性
		//request.getSession().removeAttribute("user");
		request.getSession().invalidate();
		return "f:/jsps/user/login.jsp";
	}
}
