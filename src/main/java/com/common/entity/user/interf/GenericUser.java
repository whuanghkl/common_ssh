package com.common.entity.user.interf;

import java.io.Serializable;

/***
 * father class .
 * 
 * @author huangwei
 * 
 */
public class GenericUser implements Serializable {
	private static final long serialVersionUID = -3173470758704359790L;
	private int id;
	/**
	 * user name,must be unique.
	 */
	private String username;
	private String password;

	/***
	 * construction method.
	 */
	public GenericUser() {
		super();
	}

	/***
	 * 
	 * @param username  : user name.
	 */
	public GenericUser(String username) {
		super();
		this.username = username;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "GenericUser [username=" + username + ", password=" + password
				+ "]";
	}

}
