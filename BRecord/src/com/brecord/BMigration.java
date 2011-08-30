package com.brecord;

import java.util.ArrayList;
import java.util.Iterator;

import android.database.sqlite.SQLiteDatabase;

public class BMigration {
	public static ArrayList<BMigration> Migrations = new ArrayList<BMigration>();
	public String table;
	public BColumn[] columns;
	
	public static Boolean Migrate(SQLiteDatabase db) {
		
		Iterator<BMigration> itr = Migrations.iterator();
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
		Migrations.add(this);
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
		sql += ")";
		
		return sql;
	}
	
	// TODO: need to add alter table sql and migrations
	
}
