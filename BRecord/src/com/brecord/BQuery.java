package com.brecord;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import junit.framework.TestCase;

public class BQuery extends TestCase {
	
	@SuppressWarnings("rawtypes")
	private Class klass;
	private ArrayList<String> conditions = new ArrayList<String>();
	private ArrayList<String> orders = new ArrayList<String>();
	private Integer limit = -1;
	private Integer offset = -1;
	
	@SuppressWarnings("rawtypes")
	public BQuery(Class type) {
		klass = type;
	}
	
	public void setWhereConditions(ArrayList<String> value) {
		conditions = value;
	}
	
	public void setOrderBy(ArrayList<String> value) {
		orders = value;
	}
	
	public void setLimit(int value) {
		limit = value;
	}
	
	public void setOffset(int value) {
		offset = value;
	}
	
	/**
	 * @return Null if no records where selected or the record object that was found
	 */
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
		String key = "";
		
		String name = klass.getName();
		String pkg = klass.getPackage().getName() + ".";
		name = name.replace(pkg, "");
		String[] parts = name.split("");
		String pre = "";
		for(int i = 0; i < parts.length; i++) {
			if(parts[i].matches("[A-Z]")) {
				key += pre + parts[i].toLowerCase();
				pre = "_";
			} else {
				key += parts[i].toLowerCase();
			}
		}
		key += "s";
		
		return key;
	}
	
	public ArrayList<Field> getFieldsForTableColumns() {
		String tableName = getTableName();
		BColumn[] cols = BSchema.schema.columnsForTable(tableName);
		ArrayList<Field> fields = new ArrayList<Field>();
		
		if (cols != null) {
			for(int i = 0; i < cols.length; i++) {
				try {
					
					fields.add(klass.getField(cols[i].fieldName));
				} catch (SecurityException e) {
					Log.w("BRecord", "security exception for column '"+cols[i].name+"' for table '"+getTableName()+"'");
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					Log.w("BRecord", "unknow field '"+cols[i].name+"' for table '"+getTableName()+"'");
					e.printStackTrace();
				}
			}
		}
		
		return fields;
	}
	
	public ArrayList<String> getTableColumns() {
		String tableName = getTableName();
		BColumn[] cols = BSchema.schema.columnsForTable(tableName);
		ArrayList<String> columns = new ArrayList<String>();
		if(cols != null) {
			for(int i = 0; i < cols.length; i++) {
				columns.add(cols[i].name);
			}
		}
		return columns;
	}
	
	public <T extends BRecord> ArrayList<T> executeSelectStatement() {
		SQLiteDatabase db = BConfig.config.getReadableDatabase();
		String sql = this.buildSelectStatement();
		Cursor c = db.rawQuery(sql, null);
		
		ArrayList<T> result = new ArrayList<T>();
		if(c.moveToFirst()) {
			do {
				try {
					@SuppressWarnings("unchecked")
					T row = (T) klass.newInstance(); 
					ArrayList<String> columnNames = this.getTableColumns();
					Iterator<String> itr = columnNames.iterator();
					while(itr.hasNext())
					{
						String col = itr.next();
						String fieldName = BSchema.schema.fieldNameForColumn(getTableName(), col);
						String val = c.getString(c.getColumnIndex(col));
						row.setProperty(fieldName, val);
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
		ArrayList<Field> cols = getFieldsForTableColumns();
		ContentValues vals = new ContentValues();
		
		Iterator<Field> itr = cols.iterator();
		while(itr.hasNext()) {
			Field field = itr.next();
			String fs = field.getName();
			String col = BSchema.schema.columnNameForField(getTableName(), fs);
			String val;
			String typeName = field.getType().getName();
			String pkgName = field.getType().getPackage().getName() + ".";
			typeName = typeName.replace(pkgName, "");
			try {
				val = field.get(valObj).toString();
				if (typeName.equalsIgnoreCase("Boolean"))
				{
					int bval = 0;
					if (val != null)
					{
						bval = Boolean.parseBoolean(val) == true ? 1 : 0;
					}
					vals.put(col, bval);
				}
				else if (typeName.equalsIgnoreCase("Date"))
				{
					Date date = (Date)field.get(valObj);
					if (date != null)
					{
						Double dval = date.getTime() / 1000.0;
						vals.put(col, dval);
					}
				}
				else if (! col.equalsIgnoreCase("id"))
				{
					vals.put(col, val);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		SQLiteDatabase db = BConfig.config.getWritableDatabase();
		String tableName = getTableName();
		int new_id = (int)db.insert(tableName, null, vals);
		db.close();
		
		if (new_id > 0) {
			valObj.setProperty("id", new_id);
			return true;
		} else {
			return false;
		}
	}

	public <T extends BRecord> Boolean update(T valObj) {
		ArrayList<Field> cols = getFieldsForTableColumns();
		ContentValues vals = new ContentValues();
		
		Iterator<Field> itr = cols.iterator();
		while(itr.hasNext()) {
			Field field = itr.next();
			String fs = field.getName();
			String col = BSchema.schema.columnNameForField(getTableName(), fs);
			String val;
			String typeName = field.getType().getName();
			String pkgName = field.getType().getPackage().getName() + ".";
			typeName = typeName.replace(pkgName, "");
			try {
				val = field.get(valObj).toString();
				if (typeName.equalsIgnoreCase("Boolean"))
				{
					Boolean bval = false;
					if (val != null)
					{
						bval = Boolean.parseBoolean(val);
					}
					vals.put(col, bval);
				}
				else if (typeName.equalsIgnoreCase("Date"))
				{
					Date date = (Date)field.get(valObj);
					if (date != null)
					{
						Double dval = date.getTime() / 1000.0;
						vals.put(col, dval);
					}
				}
				else if (! col.equalsIgnoreCase("id"))
				{
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
