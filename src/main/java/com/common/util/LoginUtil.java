package com.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.common.entity.user.interf.GenericUser;

/***
 * 
 * @author huangwei
 * 
 */
public final class LoginUtil {
	/**
	 * Do not allow the instantiation.
	 */
	private LoginUtil() {

	}

	public static final int LOGIN_RESULT_USER_NOT_EXIST = 1;
	public static final int LOGIN_RESULT_PASSWORD_INVALID = 2;
	public static final int LOGIN_RESULT_USERNAME_NULL = 3;
	public static final int LOGIN_RESULT_PASSWORD_NULL = 4;
	public static final int LOGIN_RESULT_OVER_TIMES = 5;
	public static final int LOGIN_RESULT_SUCCESS = 6;
	public static final String ERROR_LOGIN_USERNAME_NULL = "user name is null.";
	public static final String ERROR_LOGIN_PASSWORD_NULL = "password is null.";
	public static final String ERROR_LOGIN_OVER_TIMES = "You have failed three times.";
	public static final String SESSION_KEY_USER = "user";
	public static final String SESSION_KEY_INVOCATION = "invocation";
	public static final String SESSION_KEY_ACTION_NAME = "action_name";
	public static final String SESSION_KEY_ACTION_NAMESPACE = "action_namespace";
	public static final int MAX_LOGIN_FAIL_TIMES = 3;
	public static final int MILLISECONDS_WAIT_WHEN_FAIL = 30000;
	public static final String KEYDES = "jingning!@#$%";
	public static final String RESULT_LOGIN_FAILED="login2";
	public static final Map<Integer, String> ERROR_MAP;
	public static final String APPLICATION_KEY_IS_NEED_LOGIN="isneedlogin";
	static {
		ERROR_MAP = getErrorMap();
	}

	/***
	 * error code and error message.
	 * 
	 * @return :map
	 */
	private static Map<Integer, String> getErrorMap() {
		Map<Integer, String> errorMap = new HashMap<Integer, String>();
		errorMap.put(LOGIN_RESULT_PASSWORD_INVALID, "password is invalid.");
		errorMap.put(LOGIN_RESULT_USER_NOT_EXIST, "username does not exist.");
		errorMap.put(LOGIN_RESULT_USERNAME_NULL, ERROR_LOGIN_USERNAME_NULL);

		errorMap.put(LOGIN_RESULT_PASSWORD_NULL, ERROR_LOGIN_PASSWORD_NULL);
		errorMap.put(LOGIN_RESULT_OVER_TIMES, ERROR_LOGIN_OVER_TIMES);
		return errorMap;
	}

	/***
	 * 解密用户名和密码.
	 * 
	 * @param user : a user
	 * @return  : a user which has been decrpyted
	 * @throws IOException
	 * @throws Exception
	 */
	public static GenericUser decrpytDES(final GenericUser user) throws IOException,
			Exception {
		if (user == null) {
			return null;
		} else {
			String username = user.getUsername();
			String password = user.getPassword();
			user.setUsername(SystemHWUtil.decryptDES(username, KEYDES));
			user.setPassword(SystemHWUtil.decryptDES(password, KEYDES));
			return user;
		}
	}

	/**
	 * 加密用户名和密码.
	 * 
	 * @param user
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static GenericUser encrpytDES(final GenericUser user)
			throws IOException, Exception {
		if (user == null) {
			return null;
		} else {
			String username = user.getUsername();
			String password = user.getPassword();
			if (username != null) {
				user.setUsername(SystemHWUtil.encryptDES(username, KEYDES));
			}
			if (password != null) {
				user.setPassword(SystemHWUtil.encryptDES(password, KEYDES));
			}
			return user;
		}
	}

	public static List<GenericUser> decrpytDES(List<GenericUser> users)
			throws Exception {
		List<GenericUser> users_tmp = new ArrayList<GenericUser>();
		for (int i = 0; i < users.size(); i++) {
			GenericUser user = users.get(i);
			users_tmp.add(decrpytDES(user));
		}
		return users_tmp;
	}
}
