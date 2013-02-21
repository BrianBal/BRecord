package com.brecord;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class BR
{

	public static SQLiteDatabase database;

	@SuppressWarnings("rawtypes")
	public static <T extends BRecord> T find(Class klass, int id)
	{
		BQuery query = new BQuery(klass);
		T result = query.where("id = " + id).first();
		return result;
	}

	@SuppressWarnings("rawtypes")
	public static BQuery find(Class klass)
	{
		BQuery query = new BQuery(klass);
		return query;
	}

	@SuppressWarnings("rawtypes")
	public static <T extends BRecord> void batchInsert(Class klass, ArrayList<T> records)
	{
		try
		{
			if (records.size() > 0)
			{
				ContentValues[] values = new ContentValues[records.size()];
				Iterator<T> itr = records.iterator();
				int i = 0;
				while (itr.hasNext())
				{
					T record = itr.next();
					values[i] = record.getContentValues();
					i ++;
				}
				BConfig.CONTEXT.getContentResolver().bulkInsert(Uri.parse("content://" + BConfig.AUTHORITY + "/" + records.get(0).getTableName()), values);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public static void deleteAll(Class klass)
	{
		try
		{
			BQuery query = new BQuery(klass);
			BConfig.CONTEXT.getContentResolver().delete(Uri.parse("content://" + BConfig.AUTHORITY + "/" + query.getTableName()), null, null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
