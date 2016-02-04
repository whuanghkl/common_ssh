package com.common.interceptor;

import java.util.Map;

import org.apache.log4j.Logger;

import com.common.entity.user.interf.GenericUser;
import com.common.util.LoginUtil;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.util.ValueStack;
import com.string.widget.util.ValueWidget;

/***
 * 权限校验的拦截器,判断用户是否登录, 若未登录，则不允许访问goods和supermarket 。
 * 
 * @author huangwei
 * 
 */
public class AuthorityInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = -7647845319004031067L;
	Logger logger = Logger.getLogger(this.getClass());

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		// logger.info("interceptor:before");
		logger.info("This is a Authority interceptor:before");
		Object action = invocation.getAction();
		System.out.println("action:" + action);
		String isNeedLoginStr = (String) invocation.getInvocationContext()
				.getApplication().get(LoginUtil.APPLICATION_KEY_IS_NEED_LOGIN);
		boolean isNeedLogin = true;
		if ((!ValueWidget.isNullOrEmpty(isNeedLoginStr))
				&& (!Boolean.valueOf(isNeedLoginStr))) {
			isNeedLogin = false;//不需要登录
		} else {//需要登录，若非法访问，则自动跳转到登录页面
			isNeedLogin = true;
		}
		if (isNeedLogin) {//需要登录
			Map<String, Object> session = invocation.getInvocationContext()
					.getSession();
			// 判断用户是否登录，若在session中能够找到用户，则证明用户已经登录过
			GenericUser user = (GenericUser) session
					.get(LoginUtil.SESSION_KEY_USER);
			if (user == null) {// 若用户没有登录
				ValueStack stack = invocation.getInvocationContext()
						.getValueStack();
				// 用于在登录页面展示
				stack.set("MESSAGE_IS_LOGIN", "You have not loged in yet.");
				ActionProxy prox = invocation.getProxy();
				// 当前的action的namespace
				String currentNamespace = prox.getNamespace();
				// 当前的action的名称
				String currentActionName = prox.getActionName();
				String actionPath = currentNamespace + "/" + currentActionName;
				System.out.println("failed action path:" + actionPath);

				// 在UserLoginAction 中会用到，那么是在哪儿清空的呢？
				session.put(LoginUtil.SESSION_KEY_ACTION_NAME,
						currentActionName);
				session.put(LoginUtil.SESSION_KEY_ACTION_NAMESPACE,
						currentNamespace);
				// session.put(LoginUtil.SESSION_KEY_INVOCATION, invocation);
				return LoginUtil.RESULT_LOGIN_FAILED;// point to
														// /user/loginInput.jsp
														// ,return login page
				// because user has no access to resource
			} else {// 用户已经登录过
				if (session.containsKey(LoginUtil.SESSION_KEY_ACTION_NAME)) {/* 清除访问记录 */
					session.remove(LoginUtil.SESSION_KEY_ACTION_NAME);
					session.remove(LoginUtil.SESSION_KEY_ACTION_NAMESPACE);
				}
			}
		}
		String result = invocation.invoke();
		// after action
		logger.info("interceptor:" + this.getClass().getName()
				+ "after,action is "
				+ invocation.getAction().getClass().getSimpleName()
				+ " name of result:" + result);
		return result;
	}

}
