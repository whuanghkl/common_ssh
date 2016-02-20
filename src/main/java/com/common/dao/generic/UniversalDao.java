package com.common.dao.generic;

import com.common.bean.OrderByBean;
import com.common.util.ReflectHWUtils;
import com.common.util.SystemHWUtil;
import com.string.widget.util.ValueWidget;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/***
 * 通用的dao
 * @author huangweii
 * 2015年11月21日
 */
public class UniversalDao {
	private static Logger logger = Logger.getLogger(UniversalDao.class);
	protected SessionFactory sessionFactory;

	private static Object[] formatRSObjects(Object result) {
		Object[] objs = null;
		if (result instanceof Object[]) {
			objs = (Object[]) result;
		} else {//当只返回一个成员变量的时候
			objs = new Object[]{result};
		}
		return objs;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Resource
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		logger.debug("sessionFactory is setted successfully.");
//		logger.debug(this.sessionFactory);
	}

	public Session getCurrentSession(){
		Session session=this.sessionFactory.getCurrentSession();
		if(ValueWidget.isNullOrEmpty(session)){
			System.out.println("this.sessionFactory.getCurrentSession() return null!!!");
			session=this.sessionFactory.openSession();
			System.out.println("invoke this.sessionFactory.openSession()");
		}
		return session;
	}

	/***
	 *
	 * @param obj
	 *            :Must not be detached (other state is persistent,transient)
	 */
	public void delete(Object obj) {
		if(ValueWidget.isNullOrEmpty(obj)){
			logger.warn("delete BUT obj is null");
			System.out.println("delete BUT obj is null");
			return;
		}
		this.getCurrentSession().delete(obj);
		try {
			logger.debug("delete " + obj+".id:"+ReflectHWUtils.getObjectValue(obj, "id"));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	protected Criteria orderBy(String orderedColumn,String orderByMode,Criteria criteria){
		if(!ValueWidget.isNullOrEmpty(orderedColumn)
				&& !ValueWidget.isNullOrEmpty(criteria)){
			if (!ValueWidget.isNullOrEmpty(orderByMode)
					&&orderByMode.equalsIgnoreCase("asc")){
				criteria.addOrder(Order.asc(orderedColumn));
			}else{
				criteria.addOrder(Order.desc(orderedColumn));
			}
		}
		return criteria;
	}

	/***
	 * @param clazz
	 * @param notNullColumn : 类型必须是String类型
	 * @param criteria
	 * @return
	 */
	protected Criteria notNullColumn(Class clazz,String notNullColumn,Criteria criteria){
		if(!ValueWidget.isNullOrEmpty(notNullColumn)
				&& !ValueWidget.isNullOrEmpty(criteria)){
			try {
				Field f= clazz.getDeclaredField(notNullColumn);
				String fieldType=f.getType().getSimpleName();//int or String
				System.out.println(fieldType);
				if(!fieldType.equalsIgnoreCase("String")){
					return criteria;
				}
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			criteria.add(Restrictions.isNotNull(notNullColumn)).add(Restrictions.ne(notNullColumn, SystemHWUtil.EMPTY));
		}
		return criteria;
	}

	protected List<OrderByBean> orderByBeans(String orderByMode,String orderedColumn,String orderByMode2,String orderedColumn2){
		List<OrderByBean> list=new ArrayList<OrderByBean>();
		OrderByBean byBean=new OrderByBean(orderByMode,orderedColumn);
		list.add(byBean);
		byBean=new OrderByBean(orderByMode2,orderedColumn2);
		list.add(byBean);
		return list;
	}

	/***
     * 通过id,查询到一条记录,但是只返回指定的两个字段
     * @param id
	 * @param propertyName1 : 类的成员变量
	 * @param propertyName2 : 类的成员变量
	 * @return
	 */
	public Object[] getPropertiesById2(Class clz, int id, String propertyName1, String propertyName2) {
		/*Criteria c=this.getCurrentSession().createCriteria(clz);
    	ProjectionList projectionList=Projections.projectionList()
    			.add(Projections.property(propertyName1));
    	if(!ValueWidget.isNullOrEmpty(propertyName2)){
    		projectionList.add(Projections.property(propertyName2));
    	}*/
		String[] propertyNames = new String[]{propertyName1, propertyName2};
		return getPropertiesById2(clz, id, propertyNames);
    }

	/***
	 * 通过数据库ID查询一条记录,但是只返回指定的字段<br>
	 * 注意权限的校验,只有授予权限才能调用该方法,即一定要验权
	 * @param id
	 * @param propertyNames : 指定的多个成员变量
	 * @return
	 */
	public Object[] getPropertiesById2(Class clz, int id, String[] propertyNames) {
		Criteria c = this.getCurrentSession().createCriteria(clz);
		ProjectionList projectionList = Projections.projectionList();
		if (!ValueWidget.isNullOrEmpty(propertyNames)) {
			for (int i = 0; i < propertyNames.length; i++) {
				String string = propertyNames[i];
				if (!ValueWidget.isNullOrEmpty(string)) {
					projectionList.add(Projections.property(string));
				}
			}
		}
		Object result = c.add(Restrictions.idEq(id)).setProjection(projectionList).uniqueResult();
		Object[] objs = formatRSObjects(result);
		return objs;

    }

	/***
	 * 通过id,查询到一条记录,但是只返回指定的两个字段
	 *
	 * @param id
	 * @param propertyName1 : 类的成员变量
	 * @param propertyName2 : 类的成员变量
	 * @return
	 */
	public Object[] getPropertiesById(String entityName, int id, String propertyName1, String propertyName2) {
		String hql = "select " + propertyName1;
		if (!ValueWidget.isNullOrEmpty(propertyName2)) {
			hql += "," + propertyName2;
		}
		String parameterId = "id22";
		hql += " from " + entityName + " c where c.id=:" + parameterId;

		Query q = this.getCurrentSession().createQuery(hql);
		Object result = q.setInteger(parameterId, id).uniqueResult();
		Object[] objs = formatRSObjects(result);
		return objs;
	}

	/***
	 * 根据id查询单个列,比如查询用户名
     * @param id : unique
     * @param propertyName : 类的成员变量
     * @return
     */
    public Object getOnePropertyById(String entityName,int id,String propertyName){
    	String parameterId="id22";
    	Query q= this.getCurrentSession().createQuery("select "+propertyName+" from "+entityName+" c where c.id=:"+parameterId);
    	Object result=q.setInteger(parameterId, id).uniqueResult();
    	return result;
    }
    /***
	 * 模糊查询<br>搜索新闻
	 * @param condition
	 * @param columns
	 * @param keyword
	 * @return
	 */
	protected Criteria getCriteria(Class clz,Map condition,String[]columns,String keyword){
		Criteria criteria =this.getCurrentSession().createCriteria(clz);
		condition(criteria,condition);
		if(!ValueWidget.isNullOrEmpty(keyword)){
			Criterion c=Restrictions.like(columns[0], keyword, MatchMode.ANYWHERE);
			if(columns.length==1){
				criteria.add(c);
			}else{
				for(int i=1;i<columns.length;i++){
					c=Restrictions.or(c,Restrictions.like(columns[i], keyword, MatchMode.ANYWHERE));
				}
				criteria.add(c);
			}
		}
		return criteria;
	}
	/***
	 * 设置查询条件
	 * @param criteria
	 * @param condition
	 * @return
	 */
	protected Criteria condition(Criteria criteria, Map condition) {
		if (!ValueWidget.isNullOrEmpty(condition)) {
			Iterator it = condition.entrySet().iterator();
			logger.debug("start to set query condition:");
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				String key = (String) entry.getKey();
				Object value = entry.getValue();
				logger.debug("query condition---" + key + ":\t" + value);
				criteria.add(Restrictions.eq(key, value));
			}
			logger.debug("set query condition over.");
		}
		return criteria;
	}
	/***
	 * 
	 * @param clz
	 * @param obj
	 * @param includeZeros : Whether to include query criteria which field is 0.
	 *            true:add[where xxx=0]; false:no [where xxx=0]
	 * @param isLike
	 * @param notNullColumn
	 * @return
	 */
	protected Criteria getCriteria(Class clz, Object obj, boolean includeZeros,boolean isLike,String notNullColumn) {
		return getCriteria(clz, obj, includeZeros, isLike, false/*isDistinctRoot*/, notNullColumn);
	}
	/***
	 * 
	 * @param obj
	 * @param includeZeros : Whether to include query criteria which field is 0.
	 *            true:add[where xxx=0]; false:no [where xxx=0]
	 * @return
	 */
	protected Criteria getCriteria(Class clz, Object obj, boolean includeZeros,boolean isLike,boolean isDistinctRoot,String notNullColumn) {
		Criteria criteria =this.getCurrentSession().createCriteria(clz);
		criteria =notNullColumn(clz,notNullColumn, criteria);
		if(isDistinctRoot){
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		}
		if (obj == null) {
			return criteria;
		} else {
			if(obj instanceof Map){
				Map condition=(Map)obj;
				condition(criteria,condition);
			} else {
				Example example = getExample(obj, includeZeros, isLike);
				
				criteria.add(example);
			}
			return criteria;
		}
	}
	/***
	 * 
	 * @param obj : 查询条件<br />Example.create(obj)
	 * @param includeZeros
	 *            : Whether to include query criteria which field is 0.
	 *            true:add[where xxx=0]; false:no [where xxx=0]
	 * @param first
	 * @param maxRecordsNum
	 * @return
	 */
	protected Criteria getCriteria(Class clz,Map condition, Object obj,
			boolean includeZeros, int first, int maxRecordsNum,boolean isDistinctRoot,boolean isLike) {
		Criteria criteria = getCriteria(clz,condition, obj, includeZeros,isLike);
		paging(criteria, first, maxRecordsNum);
		
		if(isDistinctRoot){
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		}
		return criteria;
	}
	/***
	 * 
	 * @param obj : 查询条件<br />Example.create(obj)
	 * @param includeZeros
	 *            : Whether to include query criteria which field is 0.
	 *            true:add[where xxx=0]; false:no [where xxx=0]
	 * @return
	 */
	protected Criteria getCriteria(Class clz,Map condition, Object obj, boolean includeZeros,boolean isDistinctRoot,boolean isLike) {//TODO condition and obj 同时使用会有问题吗???
		if (obj == null) {
			return this.getCriteriaByPage(clz,condition, -1, -1,isDistinctRoot);
		} else {
			Example example = getExample(obj, includeZeros, isLike);
			Criteria criteria = getCriteriaByPage(clz, condition, SystemHWUtil.NEGATIVE_ONE, SystemHWUtil.NEGATIVE_ONE,isDistinctRoot);
			criteria.add(example);
			return criteria;
		}
	}

	private Example getExample(Object obj, boolean includeZeros, boolean isLike) {
		Example example = Example.create(obj);
		if (!includeZeros) {
			example = example.excludeZeroes();
		}
		if (isLike) {
			example.enableLike(MatchMode.ANYWHERE);
		}
		return example;
	}

	protected Criteria getCriteria(Class clz,Map condition, Object obj, boolean includeZeros,boolean isLike){
		return getCriteria(clz,condition, obj, includeZeros, false,isLike);
	}
	/***
	 * 精确查询
	 * @param obj
	 * @param includeZeros
	 * @param first
	 * @param maxRecordsNum
	 * @return
	 */
	public Criteria getCriteriaByPage(Class clz,Object obj,boolean includeZeros, int first,
			int maxRecordsNum){
		return getCriteriaByPage(clz,obj, includeZeros, first, maxRecordsNum, false/*isLike*/);
	}
	/***
	 * 
	 * @param obj
	 * @param includeZeros
	 * @param first
	 * @param maxRecordsNum
	 * @param isLike : 是否模糊查询
	 * @return
	 */
	public Criteria getCriteriaByPage(Class clz,Object obj,boolean includeZeros, int first,
			int maxRecordsNum,boolean isLike) {
		Criteria criteria = this.getCurrentSession()
				.createCriteria(clz);
		Example example = getExample(obj, includeZeros, isLike);
		criteria.add(example);
		paging(criteria, first, maxRecordsNum);
		return criteria;
	}
	/***
	 * 分页
	 * @param criteria
	 * @param first
	 * @param maxRecordsNum
	 * @return
	 */
	protected Criteria paging(Criteria criteria,int first,
			int maxRecordsNum){
		/* Paging */
		if (!ValueWidget.isNullOrEmpty(criteria)&& first != SystemHWUtil.NEGATIVE_ONE
				&& maxRecordsNum != SystemHWUtil.NEGATIVE_ONE) {
			criteria.setFirstResult(first);
			criteria.setMaxResults(maxRecordsNum);
		}
		return criteria;
	}
	/***
	 * 设置（1）查询条件；（2）分页
	 * 
	 * @param condition
	 *            : 查询条件
	 * @param first
	 * @param maxRecordsNum
	 * @return
	 */
	public Criteria getCriteriaByPage(Class clz,Map condition, int first,
			int maxRecordsNum,boolean isDistinctRoot) {
		Criteria criteria = this.getCurrentSession()
				.createCriteria(clz);
		if(isDistinctRoot){
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		}
		condition(criteria, condition);
		paging(criteria, first, maxRecordsNum);
		return criteria;
	}
	/***
	 * 
	 * @param key : 条件查询的列名
	 * @param value : 条件查询的值
	 * @param first
	 * @param maxRecordsNum
	 * @return
	 */
	public Criteria getCriteriaByPage(Class clz,String key,Object value, int first,
			int maxRecordsNum) {
		Criteria criteria = this.getCurrentSession()
				.createCriteria(clz);

		criteria.add(Restrictions.eq(key, value));
		paging(criteria, first, maxRecordsNum);
		return criteria;
	}
	/***
	 * 查询条件	
	 * @param criteria
	 * @return
	 */
	public long count(Criteria criteria) {
		Object result=criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		if(result==null){
			System.out.println("请检查beans.xml,实体类是否包含在packagesToScan 中.");
			return SystemHWUtil.NEGATIVE_ONE;
		}
		return (Long)result;
	}

	/***
	 * 
	 * @param key : 条件查询的列名
	 * @param value : 条件查询的值
	 * @return
	 */
	public long count(Class clz,String key,Object value) {
		Criteria criteria = getCriteriaByPage(clz,key,value, -1/*first */, -1/*maxRecordsNum*/);
		return (Long) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
	}
	/***
	 * 根据查询条件获取记录总数（共有多少条）
	 * 
	 * @param condition
	 * @return
	 */
	public long count(Class clz,Map condition,boolean isDistinctRoot) {
		Criteria criteria = getCriteriaByPage(clz,condition, -1, -1,isDistinctRoot);
		return (Long) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
	}
	public long count(Class clz,Map condition ) {
		return count(clz,condition, false/*isDistinctRoot*/);
	}
	protected Criteria getCriteria(Class clz, Object obj,
								   boolean includeZeros, int first, int maxRecordsNum, boolean isLike) {
		Criteria criteria = this.getCurrentSession().createCriteria(clz);
		if(!ValueWidget.isNullOrEmpty(obj)){
			if(obj instanceof Map){
				Map condition=(Map)obj;
				condition(criteria, condition);
			}else{
				Example example=Example.create(obj);
				if (!includeZeros) {
					example = example.excludeZeroes();
				}
				if (isLike) {
					example.enableLike(MatchMode.ANYWHERE);
				}
				criteria.add(example);
			}
		}
		
		paging(criteria, first, maxRecordsNum);
		return criteria;
	}

	public void update(Object obj) {
		if(ValueWidget.isNullOrEmpty(obj)){
			logger.warn("obj is null");
			return;
		}
		this.getCurrentSession().update(obj);
		logger.debug("update " + obj);
	}

	
	public void update(List objs) {
		int sum=objs.size();
		for(int i=0;i<sum;i++){
			Object obj=objs.get(i);
			if(!ValueWidget.isNullOrEmpty(obj)){
				update(obj);
			}
		}
	}
	public int deleteByIdSimplely(Class clz,int id) {
		return deleteByIdSimplely(clz.getSimpleName(),id);
	}
	/***
	 * 不级联,只删除本对象
	 * @param id
	 */
	public int deleteByIdSimplely(String entityName,int id) {
		return this.getCurrentSession().createQuery("delete from "+entityName+" p where p.id=:id")
		.setInteger("id", id)
		.executeUpdate();
	}
	public Serializable add(Object obj) {
		return this.getCurrentSession().save(obj);
	}

	public Serializable save(Object obj) {
		return this.add(obj);
	}
	/***
	 * 只更新一个字段
	 * @param id : 数据库ID
	 * @param propertyName : 成员变量名称
	 * @param value : 成员变量的值,即查询条件
	 */
	public void updateSpecail(Class clz,int id,String propertyName,String value){
		this.getCurrentSession().createQuery("update "+clz.getSimpleName()+" p set p."+propertyName+"=:column2322 where p.id=:id")
		.setString("column2322", value)
		.setInteger("id", id)
		.executeUpdate();
	}

	/***
	 * 只更新一个字段
	 *
	 * @param id           : 数据库ID
	 * @param propertyName : 成员变量名称
	 * @param value        : 成员变量的值,即查询条件
	 */
	public void updateSpecail(Class clz, int id, String propertyName, int value) {
		this.getCurrentSession().createQuery("update " + clz.getSimpleName() + " p set p." + propertyName + "=:column2322 where p.id=:id")
				.setInteger("column2322", value)
				.setInteger("id", id)
				.executeUpdate();
	}

	/***
	 *
	 * @param obj
	 * @param includeZeros
	 * @param isLike
	 * @return
	 */
	public long count(Class clz,Object obj, boolean includeZeros,boolean isLike) {
		Criteria criteria = getCriteria(clz,null, obj, includeZeros,isLike);
		return (Long) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
	}
	/***
     * 根据id查询单个列,比如查询用户名
     * @param id
     * @param propertyName : 类的成员变量
     * @return
     */
    public Object getOnePropertyById2(Class clz,int id,String propertyName){
    	Criteria c=this.getCurrentSession().createCriteria(clz);
    	return c.add(Restrictions.idEq(id)).setProjection(Projections.property(propertyName)).uniqueResult();
    }
    /***
     * 根据id查询单个列
     * @param id
     * @param propertyName
     * @return
     */
    public String getStringById(String entityName,int id,String propertyName){
    	Object result=getOnePropertyById(entityName,id,propertyName);
    	if(result==null){
    		return null;
    	}
    	if(result instanceof String){
    		return (String)result;
    	}else if(result instanceof Number){
    		return String.valueOf(result);
    	}else{
    		return result.toString();
    	}
    }
    /***
     * 根据id查询单个列
     * @param id
     * @param propertyName
     * @return
     */
    public int getIntegerById(String entityName,int id,String propertyName){
    	Object result=getOnePropertyById(entityName,id,propertyName);
    	if(result==null){
    		return SystemHWUtil.NEGATIVE_ONE;
    	}
    	if(result instanceof String){
    		return Integer.parseInt((String)result);
    	}else if(result instanceof Integer){
    		return (Integer)result;
    	}else if(result instanceof Long){
    		Long l=(Long)result;
    		return l.intValue();
    	}else{
    		return Integer.parseInt(result.toString());
    	}
    }
    /***
	 * 仅仅创建一个空对象而已,不涉及数据库操作
	 * @return
	 */
	public Object createEmptyObj2(Class clz){
		if(ValueWidget.isNullOrEmpty(clz)){
			logger.warn("clz is null");
			return null;
		}
		try {
			return  clz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	/***
	 * 同时更新两个字段
	 * @param id
	 * @param propertyName
	 * @param value
	 * @param propertyName2
	 * @param value2
	 */
	public void updateSpecail(Class clz,int id,String propertyName,String value,String propertyName2,String value2){
		this.getCurrentSession().createQuery("update "+clz.getSimpleName()+" p set p."+propertyName+"=:column2322,p."+propertyName2+"=:column2333 where p.id=:id")
		.setString("column2322", value)
		.setString("column2333", value2)
		.setInteger("id", id)
		.executeUpdate();
	}
	/***
     * 通过数据库ID查询一条记录,但是只返回指定的字段<br>
     * 注意权限的校验,只有授予权限才能调用该方法,即一定要验权
     * @param id
     * @param propertyNames : 指定的多个成员变量
     * @return
     */
    public Object[] getPropertiesById(String entityName,int id,String[] propertyNames){
    	if(ValueWidget.isNullOrEmpty(propertyNames)){
    		return null;
    	}
    	String hql="select "+propertyNames[0];
    	for (int i = 1; i < propertyNames.length; i++) {
			String string = propertyNames[i];
			hql+=","+string;
		}
    	String parameterId="id22";
    	hql+=" from "+entityName/*clz.getCanonicalName()*/+" c where c.id=:"+parameterId;
    	
    	Query q= this.sessionFactory.getCurrentSession().createQuery(hql);
    	Object result = q.setInteger(parameterId, id).uniqueResult();
		Object[] objs = formatRSObjects(result);
		return objs;
    }
    /***
	 * 分页查询，并且查询结果存储在参数list中
	 * 
	 * @param condition
	 *            : 查询条件
	 * @param list
	 * @param first
	 * @param maxRecordsNum
	 * @return : 总记录条数
	 */
	public long listByPage(Class clz,Map condition, List list, int first,
						   int maxRecordsNum,boolean isDistinctRoot) {
		listIsNull(list);

		Criteria criteria = getCriteriaByPage(clz,condition, first, maxRecordsNum,isDistinctRoot);

		list.addAll(criteria.list());/* 获取查询结果 */
		return count(clz,condition,isDistinctRoot);
	}
	/***
	 * 
	 * @param conditonObj : <br />Whether to include query criteria which field is 0. <br />true:add[where xxx=0];<br /> false:no [where xxx=0]
	 * @param list
	 * @param first : 起始位置
	 * @param maxRecordsNum : 最多查询多少条
	 * 
	 * @return
	 */
	public long listByPage(Class clz,Object conditonObj,boolean includeZeros,boolean isLike, List list, int first,
			int maxRecordsNum,boolean isDistinctRoot,String orderMode,String orderColumn,String notNullColumn,String orderMode2,String orderColumn2) {
		//第三方的有序的map
		ListOrderedMap orderColumnModeMap=new ListOrderedMap();
		orderColumnModeMap.put(orderColumn, orderMode);
		orderColumnModeMap.put(orderColumn2, orderMode2);
		return listByPage(clz,conditonObj,includeZeros,isLike, list, first,
				maxRecordsNum,isDistinctRoot,notNullColumn,orderColumnModeMap) ;
	}
	/***
	 * 
	 * @param conditonObj : <br />Whether to include query criteria which field is 0. <br />true:add[where xxx=0];<br /> false:no [where xxx=0]
	 * @param list
	 * @param first : 起始位置
	 * @param maxRecordsNum : 最多查询多少条
	 * @param orderColumnModeMap : 有序的map用于order by
	 * @return
	 */
	public long listByPage(Class clz,Object conditonObj,boolean includeZeros,boolean isLike, List list, int first,
			int maxRecordsNum,boolean isDistinctRoot,String notNullColumn,ListOrderedMap orderColumnModeMap) {
		Criteria criteria=getCriteria(clz, conditonObj, includeZeros,isLike,isDistinctRoot,notNullColumn);
		/***
		 * 获取记录总条数
		 * select
        count(*) as y0_ 
    from
        orders this_ 
    inner join
        OrdersDetail ordersdeta1_ 
            on this_.Order_ID=ordersdeta1_.Order_ID 
    where
        (
            this_.doctor like ?
        )
		 */
		if(clz.getName().contains("ToothOrders")){
			criteria.createAlias("ordersDetail", "ordersDetail22",CriteriaSpecification.LEFT_JOIN);
//			criteria.setFetchMode(arg0, FetchMode.)
		}
		long count=count(criteria);
		if(count<1){
			return count;
		}
		//当count为0时就不需要下面的条件查询了
		Criteria criteria2=getCriteria(clz, conditonObj, includeZeros,isLike,isDistinctRoot,notNullColumn);
		/*if(this.clz.getName().contains("com.entity.Product")){// taobao定制
			criteria2.addOrder(Order.desc("yuliu2"));
		}*/
		orderBy(orderColumnModeMap, criteria2);
		paging(criteria2, first, maxRecordsNum);
		list.addAll(criteria2.list());/* 获取查询结果 */
		
		return count;
	}
	/***
	 * 模糊查询,搜索新闻
	 * @param condition
	 * @param columns
	 * @param keyword
	 * @param list
	 * @param first
	 * @param maxRecordsNum
	 * @return
	 */
	public long listByPage(Class clz,Map condition,String[]columns,String keyword,List list, int first,
			int maxRecordsNum,String orderMode,String orderColumn,String orderMode2,String orderColumn2){
		Criteria criteria =getCriteria(clz,condition, columns, keyword);
		long count=count(criteria);
		if(count<1){
			return count;
		}
		Criteria criteria2=getCriteria(clz,condition, columns, keyword);
		orderBy(orderColumn, orderMode, criteria2);
		orderBy(orderColumn2, orderMode2, criteria2);
		paging(criteria2, first, maxRecordsNum);
		
		list.addAll(criteria2.list());/* 获取查询结果 */
		return count;
	}
	/***
	 * 模糊查询,搜索新闻
	 * @param condition
	 * @param columns
	 * @param keyword
	 * @param list
	 * @param first
	 * @param maxRecordsNum
	 * @return
	 */
	public long listByPage(Class clz,Map condition,String[]columns,String keyword,List list, int first,
			int maxRecordsNum,ListOrderedMap orderColumnModeMap){
		Criteria criteria =getCriteria(clz,condition, columns, keyword);
		long count=count(criteria);
		if(count<1){
			return count;
		}
		Criteria criteria2 = getCriteria(clz, condition, columns, keyword);

		orderBy(orderColumnModeMap, criteria2);
//		orderBy(orderColumn2, orderMode2, criteria2);
		paging(criteria2, first, maxRecordsNum);
		
		list.addAll(criteria2.list());/* 获取查询结果 */
		return count;
	}

	protected void orderBy(ListOrderedMap orderColumnModeMap, Criteria criteria2) {
		if (orderColumnModeMap != null) {
			int orderLength = orderColumnModeMap.size();
			for (int i = 0; i < orderLength; i++) {
				String orderMode = (String) orderColumnModeMap.getValue(i);
				String orderColumn = (String) orderColumnModeMap.get(i);
				orderBy(orderColumn, orderMode, criteria2);
			}
		}
	}

	public void listIsNull(List list) {
		if (list == null) {
			Class clazz = this.getClass();// 因为是实例方法，所以可以用this
			String className = clazz.getCanonicalName();/* com.jn.bean.Student */
			Thread currentThread = Thread.currentThread();
			StackTraceElement stackElement = currentThread.getStackTrace()[1];
			// 当前的方法名
			String methodName = stackElement.getMethodName();
			logger.error("list is null.");
			throw new RuntimeException(className + "." + methodName
					+ ": list must not be null");
		}
	}
}
