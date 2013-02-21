package com.brecord;

import java.util.Iterator;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BMigration
{
	public int version = 1;
	public String table;
	public BColumn[] columns;

	public static Boolean Migrate(SQLiteDatabase db, int currentVersion)
	{
		Long start = System.currentTimeMillis();
		
		Iterator<String> itr = BSchema.sql.iterator();
		int version = 0;
		while (itr.hasNext())
		{
			Long subStart = System.currentTimeMillis();
			
			String sql = itr.next();
			
			if (version >= currentVersion || currentVersion == 0)
			{
				try
				{
					Log.d("BRecord", sql);
					db.execSQL(sql);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			version ++;
			
			Long subTotal = System.currentTimeMillis() - subStart;
			Log.d("BDatabase", "BMigration.migrate query took " + subTotal.toString() + " miliseconds");
		}

		Long total = System.currentTimeMillis() - start;
		Log.d("BDatabase", "BMigration.migrate took " + total.toString() + " miliseconds");
		return true;
	}
	
	public static void add(String tableName, BColumn[] tableColumns)
	{
		new BMigration(tableName, tableColumns);
	}
	
	public static void add(String sql)
	{
		BSchema.sql.add(sql);
	}
	
	public BMigration(String tableName, BColumn[] tableColumns)
	{
		table = tableName;

		columns = new BColumn[tableColumns.length + 3];
		columns[0] = new BColumn("id", BColumn.TYPE_INTEGER);
		columns[1] = new BColumn("created_at", BColumn.TYPE_REAL);
		columns[2] = new BColumn("updated_at", BColumn.TYPE_REAL);
		for (int i = 0; i < tableColumns.length; i++)
		{
			columns[i + 3] = tableColumns[i];
		}
		version = BSchema.schema.tables.size() + 1;
		
		BSchema.sql.add(createSQL());
		BSchema.schema.tables.add(this);
	}
	
	public String dropSQL()
	{
		String sql = "DROP TABLE IF EXISTS " + table;

		return sql;
	}

	public String createSQL()
	{
		String sql = "CREATE TABLE IF NOT EXISTS " + table + "( id INTEGER PRIMARY KEY AUTOINCREMENT";
		for (int i = 0; i < columns.length; i++)
		{
			if (!columns[i].name.equalsIgnoreCase("id") && !columns[i].name.equalsIgnoreCase("created_at")
					&& !columns[i].name.equalsIgnoreCase("updated_at"))
			{
				sql += ", ";
				sql += columns[i].getColumnSQL();
			}
		}
		sql += ", created_at REAL, updated_at REAL)";

		return sql;
	}

	// TODO: need to add alter table sql and migrations

}
