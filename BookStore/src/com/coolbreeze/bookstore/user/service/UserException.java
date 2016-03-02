package com.coolbreeze.bookstore.user.service;


/*
 * 自定义用户异常：便于返回信息
 * 	比如用户名已经被注册，激活失败等异常信息
 */
public class UserException extends Exception {

	public UserException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
}
