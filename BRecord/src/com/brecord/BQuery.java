package com.brecord;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

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
	 * @param <T>
	 * @return
	 */
	public <X extends BRecord> void allAsync(final BResultHandler<X> resultHandler) {
		
		AsyncTask<Integer, Void, Void> queryTask = new AsyncTask<Integer, Void, Void>()
		{
			private ArrayList<X> results;
			
			@Override
			protected Void doInBackground(Integer ...integers) {
				results = executeSelectStatement();
				return null;
		    }
			
			@Override
			protected void onPostExecute(Void param)
			{
				resultHandler.onComplete(results);
			}
		};
		queryTask.execute(1);
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
		if (offset < 0)
		{
			offset = 0;
		}
		limit = limitTo;
		return this;
	}
	
	/**
	 * @param offsetTo
	 * @return
	 */
	public BQuery offset(int offsetTo) {
		if (limit < 0)
		{
			limit = 0;
		}
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
					// Log.w("BRecord", "security exception for column '"+cols[i].name+"' for table '"+getTableName()+"'");
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					// Log.w("BRecord", "unknow field '"+cols[i].name+"' for table '"+getTableName()+"'");
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
	
	@SuppressWarnings("unchecked")
	public <T extends BRecord> ArrayList<T> executeSelectStatement()
	{
		ArrayList<T> result = new ArrayList<T>();
		
		Uri uri = Uri.parse("content://" + BConfig.AUTHORITY + "/" + this.getTableName());
		Cursor c = BConfig.CONTEXT.getContentResolver().query(uri, null, this.buildWhereClause(), null, this.buildOrderClause());
		try
		{
			if(c.moveToFirst()) {
				do {
					T row = (T) klass.newInstance();
					row.id = c.getInt(c.getColumnIndex("id"));
					
					ArrayList<String> columnNames = this.getTableColumns();
					Iterator<String> itr = columnNames.iterator();
					String tName = getTableName();
					while(itr.hasNext())
					{
						String col = itr.next();
						BColumn bcol = BSchema.schema.getColumnInTable(tName, col);
						switch (bcol.type)
						{
							case BColumn.TYPE_BLOB:
							case BColumn.TYPE_TEXT:
								row.setProperty(bcol.fieldName, c.getString(c.getColumnIndex(col)));
								break;
							case BColumn.TYPE_BOOLEAN:
								row.setProperty(bcol.fieldName, c.getInt(c.getColumnIndex(col)) > 1);
								break;
							case BColumn.TYPE_REAL:
							case BColumn.TYPE_DATETIME:
								row.setProperty(bcol.fieldName, c.getDouble(c.getColumnIndex(col)));
								break;
							case BColumn.TYPE_INTEGER:
								row.setProperty(bcol.fieldName, c.getInt(c.getColumnIndex(col)));
								break;
							case BColumn.TYPE_LONG:
								row.setProperty(bcol.fieldName, c.getLong(c.getColumnIndex(col)));
								break;
						}
						
					}
					
					result.add(row);
				} while (c.moveToNext());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return result;
		}
		
		c.close();
		
		return result;
	}
	
	public <T extends BRecord> Boolean insert(T valObj)
	{
		Boolean result = false;
		ContentValues vals = valObj.getContentValues();
		
		Uri uri = BConfig.CONTEXT.getContentResolver().insert(Uri.parse("content://" + BConfig.AUTHORITY + "/" + this.getTableName()), vals);
		
		if (uri != null)
		{
			result = true;
			int newId = Integer.parseInt(uri.getLastPathSegment());
			valObj.setProperty("id", newId);
		}
		
		return result;
	}
	
	public <T extends BRecord> Boolean destroy(T valObj)
	{
		Boolean result = false;
		
		int deleted = BConfig.CONTEXT.getContentResolver().delete(Uri.parse("content://" + BConfig.AUTHORITY + "/" + getTableName()), "id = ?", new String[] { valObj.id.toString() });		
		if (deleted > 0)
		{
			result = true;
		}
		else
		{
			result = false;
		}
		
		return result;
	}

	public <T extends BRecord> Boolean update(T valObj) {
		Boolean result = false;
		ContentValues vals = valObj.getContentValues();
		
		String id = valObj.id.toString();
		int changed = BConfig.CONTEXT.getContentResolver().update(Uri.parse("content://" + BConfig.AUTHORITY + "/" + getTableName()), vals, "id = '" + id + "'", null);
		if (changed > 0)
		{
			result =  true;
		}
		else
		{
			result = false;
		}
		
		return result;
	}
	
	/**
	 * @return
	 */
	public String buildSelectStatement()
	{
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
	
	public String buildWhereClause()
	{
		String sql = "(1=1) ";
		
		// add where conditions
		Iterator<String> citr = conditions.iterator();
		while(citr.hasNext()) {
			String cond = citr.next();
			sql += "AND (" + cond + ") ";
		}
		
		return sql;
	}
	
	public String buildOrderClause()
	{
		String sql = "";
		
		// add where conditions
		Iterator<String> oitr = orders.iterator();
		if (oitr.hasNext())
		{
			sql += "";
			String pre = "";
			while(oitr.hasNext())
			{
				String order = oitr.next();
				sql += pre + order;
				pre = ", ";
			}
			sql += " ";
		}
		
		return sql;
	}
	
}
