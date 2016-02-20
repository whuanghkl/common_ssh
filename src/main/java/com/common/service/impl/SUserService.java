package com.common.service.impl;

import com.common.dao.interf.IUserLoginDao;
import com.common.entity.user.interf.GenericUser;
import com.common.util.LoginUtil;
import com.common.util.SystemHWUtil;
import com.opensymphony.xwork2.ActionContext;
import com.string.widget.util.ValueWidget;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;

/***
 * super class for User Service class.
 * 
 * @author huangwei
 * 
 */
public abstract class SUserService<T extends GenericUser> implements
		IUserService<T> {
	protected final Class<T> clz = SystemHWUtil.getGenricClassType(getClass());
	private IUserLoginDao<T> userDao;

	/***
	 * construction method.
	 */
	public SUserService() {
		super();
	}

	@Override
	/***
	 * Not allowed to be rewritten
	 * @return : [state,user object]
	 */
	public Object[] login(final String username, final String passwd)
			throws UnsupportedEncodingException, Exception {
		System.out.println(SystemHWUtil.DIVIDING_LINE);
		System.out.println("SUserService:login");
		System.out.println(SystemHWUtil.DIVIDING_LINE);
		GenericUser user = null;
		Object[] results = new Object[2];
		if (StringUtils.isEmpty(username)) {
			results[0] = LoginUtil.LOGIN_RESULT_USERNAME_NULL;
			return results;
		} else if (StringUtils.isEmpty(passwd)) {
			results[0] = LoginUtil.LOGIN_RESULT_PASSWORD_NULL;
			return results;
		}
		T aUser = getClz().newInstance();
		aUser.setUsername(username);
		//从数据库中查出来的用户对象
		user = this.userDao.getByName(aUser);

		if (user == null) {
			System.out.println("can NOT find user username is "+username);
			results[0] = LoginUtil.LOGIN_RESULT_USER_NOT_EXIST;
			return results;
		} else {
			// passwd = SystemUtil.encryptDES(passwd, LoginUtil.keyDES);
			if (user.getPassword().equals(passwd)) {
				results[1] = user;
			} else {
				results[0] = LoginUtil.LOGIN_RESULT_PASSWORD_INVALID;
				return results;
			}
		}
		results[0] = LoginUtil.LOGIN_RESULT_SUCCESS;
		return results;
	}

	@Override
	/****
	 * Not allowed to be rewritten
	 * @return : [state,user object]
	 */
	public Object[] login(final ActionContext actionContext,
			final GenericUser user) throws UnsupportedEncodingException,
			Exception {
		// logger.info("login(ActionContext actionContext,User user)");
		Object[] results = new Object[2];
		if (user == null) {
			results[0] = LoginUtil.LOGIN_RESULT_USERNAME_NULL;
			return results;
		} else {
			return login(user.getUsername(), user.getPassword());
		}
	}

	/***
	 * get user dao.
	 * 
	 * @return : a user dao.
	 */
	public IUserLoginDao<T> getUserDao() {
		return userDao;
	}

	/***
	 * set user dao.
	 * 
	 * @param userDao
	 *            : a user dao.
	 */
	public void setUserDao(IUserLoginDao<T> userDao) {
		this.userDao = userDao;
	}

	@Override
	public final Object[] login(GenericUser user)
			throws UnsupportedEncodingException, Exception {
		if (ValueWidget.isNullOrEmpty(user)) {
			Object[] results = new Object[2];
			results[0] = LoginUtil.LOGIN_RESULT_USERNAME_NULL;
			return results;
		}
		return login(user.getUsername(), user.getPassword());
	}

	public Class<T> getClz() {
		return clz;
	}

}