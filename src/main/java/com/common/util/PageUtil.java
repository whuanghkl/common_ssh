package com.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.ListOrderedMap;

import com.common.dao.generic.GenericDao;
import com.common.web.view.PageView;
import com.string.widget.util.ValueWidget;

public class PageUtil {

    public static int getTotalPages(final long totalRecords, final int recordsPerPage) {
    	long tmpPages=totalRecords / recordsPerPage;
    	long totalPages=(totalRecords % recordsPerPage == 0) ?
    			tmpPages: (tmpPages + 1);
        return (int)totalPages;
    }
    /***
     * 
     * @param condition : Map是查询条件
     * @param view
     * @param dao
     */
    public static void paging(Map condition,PageView view, GenericDao dao) {
		List list = new ArrayList();
		int currentPage2=view.getCurrentPage();
		if(currentPage2<1){
			currentPage2=1;
			view.setCurrentPage(1);
		}
		int start = (currentPage2 - 1)
				* view.getRecordsPerPage();
		if(start<0){/* org.hibernate.exception.GenericJDBCException: could not execute query] with root cause
java.sql.SQLException: ResultSet may only be accessed in a forward direction.*/
			start=0;
		}
		System.out.println("start:" + start + ",\tmax:"
				+ view.getRecordsPerPage());
		long count = dao
				.listByPage(condition, list, start, view.getRecordsPerPage(),false/*isDistinctRoot*/);
		view.setRecordList(list);
		paging(count, view);
	}
    /***
	 * 页码从1开始
	 * @param view : 保存分页信息
	 * @param dao  : 控制器中具体的dao
	 * @param 
	 */
	public static void paging(Map condition,PageView view, GenericDao dao,String orderMode,String orderColumn,String notNullColumn,String orderMode2,String orderColumn2) {
		List list = new ArrayList();
		int currentPage2=view.getCurrentPage();
		if(currentPage2<1){
			currentPage2=1;
			view.setCurrentPage(1);
		}
		int start = (currentPage2 - 1)
				* view.getRecordsPerPage();
		if(start<0){/* org.hibernate.exception.GenericJDBCException: could not execute query] with root cause
java.sql.SQLException: ResultSet may only be accessed in a forward direction.*/
			start=0;
		}
		System.out.print("start:" + start + ",\tmax:"
				+ view.getRecordsPerPage());
		long count = dao
				.listByPage(condition,false/*includeZeros*/,false/*isLike*/, list, start, view.getRecordsPerPage(),false/*isDistinctRoot*/,orderMode,orderColumn,notNullColumn,orderMode2,orderColumn2);
		view.setRecordList(list);
		System.out.println(" total sum:"+count);
		paging(count, view);
	}
	 /***
		 * 页码从1开始
		 * @param view : 保存分页信息
		 * @param dao  : 控制器中具体的dao
		 */
		public static void paging(Map condition,PageView view, GenericDao dao,String notNullColumn,ListOrderedMap orderColumnModeMap) {
			List list = new ArrayList();
			int currentPage2=view.getCurrentPage();
			if(currentPage2<1){
				currentPage2=1;
				view.setCurrentPage(1);
			}
			int start = (currentPage2 - 1)
					* view.getRecordsPerPage();
			if(start<0){/* org.hibernate.exception.GenericJDBCException: could not execute query] with root cause
	java.sql.SQLException: ResultSet may only be accessed in a forward direction.*/
				start=0;
			}
			System.out.println("start:" + start + ",\tmax:"
					+ view.getRecordsPerPage());
			long count = dao
					.listByPage(condition,false/*includeZeros*/,false/*isLike*/, list, start, view.getRecordsPerPage(),false/*isDistinctRoot*/,notNullColumn,orderColumnModeMap);
			view.setRecordList(list);
			paging(count, view);
		}
	
	/***
	 * 页码从1开始<br>搜索新闻
	 * @param view : 保存分页信息
	 * @param dao  : 控制器中具体的dao
	 */
	public static void paging(Map condition,String[]columns,String keyword,PageView view, GenericDao dao,String orderMode,String orderColumn,String orderMode2,String orderColumn2) {
		List list = new ArrayList();
		int currentPage2=view.getCurrentPage();
		if(currentPage2<1){
			currentPage2=1;
			view.setCurrentPage(1);
		}
		int start = (currentPage2 - 1)
				* view.getRecordsPerPage();
		if(start<0){/* org.hibernate.exception.GenericJDBCException: could not execute query] with root cause
java.sql.SQLException: ResultSet may only be accessed in a forward direction.*/
			start=0;
		}
		System.out.println("start:" + start + ",\tmax:"
				+ view.getRecordsPerPage());
		long count = dao
				.listByPage(condition,columns, keyword, list,start,view.getRecordsPerPage(),orderMode,orderColumn,orderMode2,orderColumn2);
		view.setRecordList(list);
		paging(count, view);
	}

	/***
	 * 页码从1开始<br>搜索新闻
	 * @param view : 保存分页信息
	 * @param dao  : 控制器中具体的dao
	 */
	public static void paging(Map condition,String[]columns,String keyword,PageView view, GenericDao dao,ListOrderedMap orderColumnModeMap) {
		List list = new ArrayList();
		int currentPage2=view.getCurrentPage();
		if(currentPage2<1){
			currentPage2=1;
			view.setCurrentPage(1);
		}
		int start = (currentPage2 - 1)
				* view.getRecordsPerPage();
		if(start<0){/* org.hibernate.exception.GenericJDBCException: could not execute query] with root cause
java.sql.SQLException: ResultSet may only be accessed in a forward direction.*/
			start=0;
		}
		System.out.println("start:" + start + ",\tmax:"
				+ view.getRecordsPerPage());
		long count = dao
				.listByPage(condition,columns, keyword, list,start,view.getRecordsPerPage(),orderColumnModeMap);
		view.setRecordList(list);
		paging(count, view);
	}
	
	public static void paging(String key,Object value,PageView view, GenericDao dao,String order,String orderColumn,String orderMode2,String orderColumn2) {
		Map condition=new HashMap();
		condition.put(key, value);
		paging(condition, view, dao,order,orderColumn,null,orderMode2,orderColumn2);
	}
	/***
	 * 
	 * @param key : 成员变量名称
	 * @param value : 成员变量的值
	 * @param view
	 * @param dao : 该实体类对应的dao
	 */
	public static void paging(String key,Object value,PageView view, GenericDao dao){
		paging(key, value, view, dao, null/*order*/, null/*orderColumn*/,null/*orderMode2*/,null/*orderColumn2*/);
	}
	/***
	 * @param conditionObj : <br />Whether to include query criteria which field is 0. <br />true:add[where xxx=0];<br /> false:no [where xxx=0]
	 * @param criteria
	 * @param view
	 * @param dao
	 * @param orderModel : 取值必须是['asc','desc']
	 * @param orderColumn : 对哪个列进行排序
	 */
	public static void paging(Object conditionObj,boolean isLike,PageView view, GenericDao dao,String orderModel,String orderColumn,String notNullColumn,String orderMode2,String orderColumn2) {
		List list = new ArrayList();
		int start = (view.getCurrentPage() - 1)
				* view.getRecordsPerPage();
		if(!ValueWidget.isNullOrEmpty(conditionObj)){
			try {
				//把对象中空字符串改为null
				ReflectHWUtils.convertEmpty2Null(conditionObj);
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
		long count = dao
				.listByPage(conditionObj,false/*includeZeros*/,isLike	, list, start, view.getRecordsPerPage(),false/* isDistinctRoot */,orderModel,orderColumn,notNullColumn,orderMode2,orderColumn2);
		view.setRecordList(list);
		view.setRecordNumOfCurrent(list.size());
		paging(count, view);
	}
	/***
	 * 
	 * @param conditionObj
	 * @param isLike
	 * @param view
	 * @param dao
	 * @param notNullColumn
	 * @param orderColumnModeMap : 用于排序
	 */
	public static void paging(Object conditionObj,boolean isLike,PageView view, GenericDao dao,String notNullColumn,ListOrderedMap orderColumnModeMap) {
		List list = new ArrayList();
		int start = (view.getCurrentPage() - 1)
				* view.getRecordsPerPage();
		if(!ValueWidget.isNullOrEmpty(conditionObj)){
			try {
				//把对象中空字符串改为null
				ReflectHWUtils.convertEmpty2Null(conditionObj);
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
		long count = dao
				.listByPage(conditionObj,false/*includeZeros*/,isLike	, list, start, view.getRecordsPerPage(),false/* isDistinctRoot */,notNullColumn,orderColumnModeMap);
		view.setRecordList(list);
		view.setRecordNumOfCurrent(list.size());
		paging(count, view);
	}
	public static void paging(Object conditionObj,boolean isLike,PageView view, GenericDao dao,String order,String orderColumn,String notNullColumn){
		paging(conditionObj, isLike, view, dao, order, orderColumn, notNullColumn, null/*orderMode2*/, null/*orderColumn2*/);
	}
	public static void paging(Object conditionObj,boolean isLike,PageView view, GenericDao dao,String orderMode,String orderColumn){
		paging(conditionObj, isLike,view, dao, orderMode, orderColumn, null/*orderMode2*/, null/*orderColumn2*/);
	}
	public static void paging(Object conditionObj,boolean isLike,PageView view, GenericDao dao,String order,String orderColumn,String orderMode2,String orderColumn2) {
		paging(conditionObj, isLike, view, dao, order, orderColumn, null,orderMode2,orderColumn2);
	}
	
	public static void paging(Object conditionObj,boolean isLike,PageView view, GenericDao dao,ListOrderedMap orderColumnModeMap){
		paging(conditionObj, isLike,view, dao,  null/* notNullColumn */, orderColumnModeMap);
	}
	
	public static void paging(long count,PageView view){
//		view.setRecordList(list);
		view.setTotalRecords(count);
		int totalPages = PageUtil.getTotalPages(view.getTotalRecords(),
				view.getRecordsPerPage());
		view.setTotalPages(totalPages);
	}

	/***
	 * 
	 * @param view
	 * @param dao
	 */
	public static void paging(PageView view, GenericDao dao) {
		paging((Map)null, view, dao);
	}

	/***
	 * Delete all spaces
	 * 
	 * @param input
	 * @return
	 */
	public static String deleteAllCRLF(String input) {
		return input.replaceAll("((\r\n)|\n)[\\s\t ]*", " ").replaceAll(
				"^((\r\n)|\n)", "");
	}

	
}
