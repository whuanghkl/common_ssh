package com.common.action.user.impl;

import com.common.entity.user.interf.GenericUser;
import com.common.service.impl.IUserService;
import com.common.util.LoginUtil;
import com.common.util.SystemHWUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/***
 * user login action must extends this super Class<br>
 * 用于Struts2
 * 
 * @author huangwei
 * 
 * @param <T>
 */
public abstract class SUserLoginAction<T extends GenericUser> extends
		ActionSupport {
	private static final long serialVersionUID = -7922520899726205294L;
	/***
	 * 子类的类型
	 */
	protected final Class<T> clz = SystemHWUtil.getGenricClassType(getClass());
	/***
	 * Using Generics type
	 */
	protected T user;
	private IUserService<T> userService;

	@Override
	public String execute() throws Exception {
		ActionContext actionContext = ActionContext.getContext();
		return doExecute(checkUser(actionContext, this.user));
	}
	public String logout(){
		System.out.println("SUserLoginAction logout");
		return "login";
	}

	/***
	 * check user
	 * 
	 * @param actionContext
	 * @param user
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public Integer checkUser(ActionContext actionContext, T user)
			throws UnsupportedEncodingException, Exception {
		// result of login check,please see com.common.util.LoginUtil
		Object[] result =this.userService.login(null, user);
		Integer result_check = (Integer) result[0];
		if (result_check == LoginUtil.LOGIN_RESULT_SUCCESS) {
			Map<String, Object> session = ActionContext.getContext()
					.getSession();
			session.put(LoginUtil.SESSION_KEY_USER, result[1]/*从数据库中查出来的用户对象*/);
			System.out.println("session add key:"+LoginUtil.SESSION_KEY_USER);
		}
		return result_check;
	}

	/***
	 * user action must implement this method
	 * 
	 * @param result_check
	 * @return
	 */
	public abstract String doExecute(Integer result_check);

	public void setUser(T user) {
		this.user = user;
	}

	public IUserService<T> getUserService() {
		return userService;
	}

	/**
	 * Using Dependency Injection
	 * 
	 * @param userService
	 */
	public void setUserService(IUserService<T> userService) {
		this.userService = userService;
	}



	public Class<T> getClz() {
		return clz;
	}

}
