package com.common.action.error;

import com.opensymphony.xwork2.ActionSupport;

/***
 * 自定义的404 页面.
 * 
 * @author huangwei
 *
 */
public class NotFoundErrerAction extends ActionSupport {

	private static final long serialVersionUID = 6218614965524501080L;
	@Override
	public String execute() throws Exception {
		System.out.println("您访问的action 不存在!");
		return super.execute();
	}
}
