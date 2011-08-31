package com.brecord;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
		
		ArrayList<T> result = executeSelectStatement();
		if (result != null && result.size() > 0)
		{
			return result.get(0);
		}
		return null;
	}
	
	/**
	 * @return
	 */
	public <T extends BRecord> ArrayList<T> all() {
		return executeSelectStatement();
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
	
	public <T extends BRecord> ArrayList<T> executeSelectStatement() {
		SQLiteDatabase db = BConfig.config.getReadableDatabase();
		Cursor c = db.rawQuery(this.buildSelectStatement(), null);
		
		ArrayList<T> result = new ArrayList<T>();
		if(c.moveToFirst()) {
			do {
				try {
					@SuppressWarnings("unchecked")
					T row = (T) klass.newInstance(); 
					String[] columnNames = c.getColumnNames();
					for(int i = 0; i < columnNames.length; i++)
					{
						String col = columnNames[i];
						String val = c.getString(c.getColumnIndex(col));
						row.setProperty(col, val);
					}
					result.add(row);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				}
			} while (c.moveToNext());
		}
		
		db.close();
		c.close();
		return result;
	}
	
	public <T extends BRecord> Boolean insert(T valObj) {
		Field[] cols = valObj.getClass().getFields();
		ContentValues vals = new ContentValues();
		for(int i = 0; i < cols.length; i++) {
			String col = cols[i].getName();
			String val;
			try {
				val = cols[i].get(valObj).toString();
				if (! col.equalsIgnoreCase("id")) {
					vals.put(col, val);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		SQLiteDatabase db = BConfig.config.getWritableDatabase();
		int new_id = (int)db.insert(getTableName(), null, vals);
		db.close();
		
		if (new_id > 0) {
			valObj.setProperty("id", new_id);
			return true;
		} else {
			return false;
		}
	}

	public <T extends BRecord> Boolean update(T valObj) {
		Field[] cols = valObj.getClass().getFields();
		ContentValues vals = new ContentValues();
		for(int i = 0; i < cols.length; i++) {
			String col = cols[i].getName();
			String val;
			try {
				val = cols[i].get(valObj).toString();
				if (! col.equalsIgnoreCase("id")) {
					vals.put(col, val);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		SQLiteDatabase db = BConfig.config.getWritableDatabase();
		String table = getTableName();
		String id = valObj.id.toString();
		int changed = db.update(table, vals, "id = '" + id + "'", null);
		db.close();
		
		if (changed > 0) {
			return true;
		} else {
			return false;
		}
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
