package com.brecord;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BR {
	
	public static SQLiteDatabase database;
	
	@SuppressWarnings("rawtypes")
	public static <T extends BRecord> T find(Class klass, int id) {
		BQuery query = new BQuery(klass);
		T result = query.where("id = " + id).first();
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	public static BQuery find(Class klass) {
		BQuery query = new BQuery(klass);
		return query;
	}
	
	@SuppressWarnings("rawtypes")
	public static void deleteAll(Class klass) {
		SQLiteDatabase db = BConfig.config.getWritableDatabase();
		BQuery query = new BQuery(klass);
		int removed = db.delete(query.getTableName(), null, null);
		Log.d("BQuery", "deleted " + removed + " rows from " + query.getTableName());
	}
	
	synchronized public static void open()
	{
		database = BConfig.config.getWritableDatabase();
	}
	
	synchronized public static void close()
	{
		if (database != null)
		{
			database.close();
		}
	}
	
}
