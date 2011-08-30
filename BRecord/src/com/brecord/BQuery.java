package com.brecord;

import java.util.ArrayList;
import java.util.Iterator;

import junit.framework.TestCase;

public class BQuery extends TestCase {
	
	private Class klass;
	private ArrayList<String> conditions = new ArrayList<String>();
	private ArrayList<String> orders = new ArrayList<String>();
	private Integer limit = -1;
	private Integer offset = -1;
	
	public BQuery(Class type) {
		klass = type;
	}
	
	/**
	 * @return Null if no records where selected or the record object that was found
	 */
	@SuppressWarnings("unchecked")
	public <T extends BRecord> T first() {
		limit = 1;
		offset = 0;
		String sql = this.buildSelectStatement();
		T result = null;
		try {
			result = (T) klass.newInstance();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @return
	 */
	public <T extends BRecord> ArrayList<T> all() {
		String sql = this.buildSelectStatement();
		return null;
	}
	
	/**
	 * @param where
	 * @return
	 */
	public BQuery where(String where) {
		conditions.add(where);
		return this;
	}
	
	/**
	 * @param order
	 * @return
	 */
	public BQuery order(String order) {
		orders.add(order);
		return this;
	}
	
	/**
	 * @param limitTo
	 * @return
	 */
	public BQuery limit(int limitTo) {
		limit = limitTo;
		return this;
	}
	
	/**
	 * @param offsetTo
	 * @return
	 */
	public BQuery offset(int offsetTo) {
		offset = offsetTo;
		return this;
	}
	
	/**
	 * @return
	 */
	public String getTableName() {
		String packageName = klass.getPackage().getName() + ".";
		String className = klass.getName();
		// TODO: this need to be a little smarter
		className = className.replaceAll(packageName, "").toLowerCase() + "s";
		
		return className;
	}
	
	/**
	 * @return
	 */
	public String buildSelectStatement() {
		String sql = "SELECT * FROM " + getTableName() + " WHERE (1=1) ";
		
		// add where conditions
		Iterator<String> citr = conditions.iterator();
		while(citr.hasNext()) {
			String cond = citr.next();
			sql += "AND (" + cond + ") ";
		}
		
		// add where conditions
		Iterator<String> oitr = orders.iterator();
		if (oitr.hasNext()) {
			sql += "ORDER BY ";
			String pre = "";
			while(oitr.hasNext()) {
				String order = oitr.next();
				sql += pre + order;
				pre = ", ";
			}
			sql += " ";
		}
		
		if (limit > 0) {
			sql += "LIMIT " + limit.toString() + " ";
		}
		
		if (offset >= 0) {
			sql += "OFFSET " + offset.toString() + " ";
		}
		
		return sql;
	}
	
}
