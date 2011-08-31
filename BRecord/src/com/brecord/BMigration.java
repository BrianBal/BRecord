package com.brecord;

import java.util.ArrayList;
import java.util.Iterator;

import android.database.sqlite.SQLiteDatabase;

public class BMigration {
	public String table;
	public BColumn[] columns;
	
	public static Boolean Migrate(SQLiteDatabase db) {
		
		Iterator<BMigration> itr = BSchema.schema.tables.iterator();
		while(itr.hasNext()) {
			BMigration mig = itr.next();
			String sql = mig.dropSQL();
			db.execSQL(sql);
			sql = mig.createSQL();
			db.execSQL(sql);
		}
		
		return true;
	}
	
	public BMigration(String tableName, BColumn[] tableColumns) {
		table = tableName;
		columns = tableColumns;
		BSchema.schema.tables.add(this);
	}
	
	public String dropSQL() {
		String sql = "DROP TABLE IF EXISTS " + table;
		
		return sql;
	}
	
	public String createSQL() {
		String sql = "CREATE TABLE IF NOT EXISTS " + table + "( id INTEGER PRIMARY KEY AUTOINCREMENT";
		for(int i = 0; i < columns.length; i++) {
			sql +=  ", ";
			sql += columns[i].getColumnSQL();
		}
		sql += ", created_at TEXT, updated_at TEXT)";
		
		return sql;
	}
	
	// TODO: need to add alter table sql and migrations
	
}
