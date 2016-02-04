package com.common.service.impl;

import java.io.UnsupportedEncodingException;

import com.common.entity.user.interf.GenericUser;
import com.opensymphony.xwork2.ActionContext;

public interface IUserService<T extends GenericUser> {

	/***
	 * 
	 * @param username
	 * @param passwd
	 * @return : [state,user object]
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public abstract  Object[] login(String username, String passwd)
			throws UnsupportedEncodingException, Exception;

	/***
	 * 
	 * @param actionContext
	 * @param user
	 * @return : [state,user object]
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public abstract Object[] login(ActionContext actionContext, T user)
			throws UnsupportedEncodingException, Exception;
	/***
	 * 
	 * @param user
	 * @return : [state,user object]
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public abstract Object[] login(T user)
			throws UnsupportedEncodingException, Exception;

}