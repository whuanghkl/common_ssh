package com.common.web.servlet.login;

import com.common.entity.user.interf.GenericUser;
import com.common.service.impl.IUserService;
import com.common.util.LoginUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/***
 * login servlet must extends this super class
 * 若登录成功，则把用户存放到session中
 * @author huangwei
 * 
 */
public abstract class SLoginServlet<T extends GenericUser> extends HttpServlet {

	private static final long serialVersionUID = 2573871304382369884L;
	private IUserService<T> userService;

	/***
	 * Not allowed to be rewritten
	 * 
	 * @param user
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public final Integer checkUser(T user, HttpServletRequest request)
			throws UnsupportedEncodingException, Exception {
		// result of login check,please see com.common.util.LoginUtil
		Object[] result = this.userService.login(user);
		Integer result_check = (Integer) result[0];
		if (result_check.intValue() == LoginUtil.LOGIN_RESULT_SUCCESS) {
			//若登录成功，则把用户存放到session中
			HttpSession session = request.getSession();
			session.setAttribute(LoginUtil.SESSION_KEY_USER, result[0]);
			System.out.println("session add key:"+LoginUtil.SESSION_KEY_USER);
		}
		return result_check;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doPost(req, resp);

	}

	public void setUserService(IUserService<T> userService) {
		this.userService = userService;
	}
}
