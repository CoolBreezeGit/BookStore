package com.coolbreeze.bookstore.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.handlers.BeanHandler;

import com.coolbreeze.bookstore.user.domain.User;
import com.coolbreeze.jdbc.TxQueryRunner;

public class UserDao {
	
	private TxQueryRunner tqr=new TxQueryRunner();

	/*
	 * 通过用户名查找用户记录 
	 */
	public User findByUsername(String username){
		String sql="select * from users where username=?";
		
		User user=null;
		try {
			user=tqr.query(sql,new BeanHandler<User>(User.class),username);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return user;
	}
	
	/*
	 * 通过激活码查找用户记录
	 */
	public User findByActivationCode(String activationcode){
		String sql="select * from users where activationcode=?";
		
		User user=null;
		try {
			user=tqr.query(sql,new BeanHandler<User>(User.class),activationcode);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return user;
	}
	
	
	/*
	 * 添加用户记录
	 */
	public void addUser(User user){
		
		String sql="insert into users(uid,username,password,email,activationcode,state) values(?,?,?,?,?,?)";
		
		Object[] params={user.getUid(),user.getUsername(),user.getPassword(),user.getEmail(),user.getActivationcode(),user.getState()};
		
		try {
			tqr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * 把激活码对应的用户记录的激活状态更改为true
	 */
	public void setStateTrue(String activationcode) {
		String sql="update users set state=1 where activationcode=?";
		try {
			tqr.update(sql,activationcode);
		} catch (SQLException e) {		
			throw new RuntimeException(e);
		}
	}
	
}
