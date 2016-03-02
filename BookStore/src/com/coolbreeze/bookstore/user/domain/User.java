package com.coolbreeze.bookstore.user.domain;


/*
 * 用户bean
 */
public class User {

	private String uid;
	private String username;
	private String password;
	private String email;
	private String activationcode;
	private boolean state;
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getActivationcode() {
		return activationcode;
	}
	public void setActivationcode(String activationcode) {
		this.activationcode = activationcode;
	}
	public boolean getState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	
	@Override
	public String toString() {
		return "User [uid=" + uid + ", username=" + username + ", password="
				+ password + ", email=" + email + ", activationcode="
				+ activationcode + ", state=" + state + "]";
	}	
	
	
}
