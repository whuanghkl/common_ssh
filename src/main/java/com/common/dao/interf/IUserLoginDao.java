package com.common.dao.interf;

import com.common.entity.user.interf.GenericUser;

import java.io.Serializable;
import java.util.List;

/***
 * user log in
 * @author huangwei
 *
 * @param <T>
 */
public interface IUserLoginDao<T extends GenericUser> {

	public abstract T getByName(T user) throws Exception;

    public abstract T getByName(String username);

    public void updateSpecial(int id, String propertyName, String value);

    public int modifyPass(int userId, String old_password, String new_password);

    public int modifyPass(int userId, String new_password);

	public abstract T getByNameAndPassword(T user2)
			throws Exception;
	public abstract Serializable add(T user);

	public abstract List<T> find(T user);
	/***
	 * get real type
	 * @return
	 */
	public abstract Class getClz();

	public abstract T load(int id);
	public abstract T get(int id);
	/***
	 * 删除用户
	 * @param user
	 */
	public abstract void delete(Object user);
	/***
	 * 保存编辑后的用户
	 * @param user
	 */
	public abstract void update(Object user);

}