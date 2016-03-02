package com.coolbreeze.bookstore.user.service;

import com.coolbreeze.bookstore.user.dao.UserDao;
import com.coolbreeze.bookstore.user.domain.User;

/*
 * 用户业务逻辑层
 */
public class UserService {

	private UserDao userDao=new UserDao();
	
	/*
	 * 用户注册验证
	 */
	public void userRegist(User user) throws UserException{
		//1.验证用户名是否已经被注册
		User t_user=userDao.findByUsername(user.getUsername());
		
		if(t_user!=null){
			throw new UserException("该用户名已经被注册!");
		}
		
		//2.添加用户
		userDao.addUser(user);
	}
	
	
	/*
	 * 用户激活验证 
	 */
	public void activate(String activationcode) throws UserException{
		//1.通过dao层的方法用激活码查询用户是否存在，否则抛异常
		User user=userDao.findByActivationCode(activationcode);
		if(user==null){
			throw new UserException("你要激活的用户没有注册！");
		}
		//2.如果存在用户，查看激活状态state是否为false，否则抛异常
		if(user.getState()==true){
			throw new UserException("你的用户已经被激活了，不需要再次激活！");
		}
		//3.激活成功，设置state为true
		userDao.setStateTrue(activationcode);
	}
	
	
	/*
	 * ajax查询用户名是否已被注册
	 */
	public void verifyUsername(String username) throws UserException{
		//1.调用dao层findByUsername来查询
		User user=userDao.findByUsername(username);
		if(user!=null){
			throw new UserException("此用户名已经被注册了");
		}
	}


	
	
	
	
	/*
	 * 用户登录验证
	 */
	public User login(User user) throws UserException{
		
		User t_user=userDao.findByUsername(user.getUsername());
		
		//用户名错误
		if(t_user==null){
			throw new UserException("用户名不存在！");
		}		
		//密码错误
		if(!t_user.getPassword().equals(user.getPassword())){
			throw new UserException("密码错误！");
		}
		//为激活
		if(t_user.getState()==false){
			throw new UserException("用户未激活,请到您的邮箱"+t_user.getEmail()+"进行激活操作再登录！");
		}

		return t_user;
		
	}
	
	
}
