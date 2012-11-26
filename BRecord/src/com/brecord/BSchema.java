package com.brecord;

import java.util.ArrayList;
import java.util.Iterator;

public class BSchema {
	public static BSchema schema = new BSchema();
	
	public ArrayList<BMigration> tables = new ArrayList<BMigration>();
	private BMigration lastTableMigration = null;
	
	public BColumn[] columnsForTable(String table) {
		
		if (lastTableMigration != null && lastTableMigration.table.equals(table))
		{
			return lastTableMigration.columns;
		}
		
		Iterator<BMigration> itr = tables.iterator();
		while(itr.hasNext()) {
			BMigration migration = itr.next();
			if (migration.table.equals(table)) {
				lastTableMigration = migration;
				return migration.columns;
			}
		}
		return null;
	}
	
	public BColumn getColumnInTable(String table, String column) {
		BColumn[] cols = this.columnsForTable(table);
		for(int i = 0; i < cols.length; i++) {
			if(cols[i].name.equals(column)) {
				return cols[i];
			}
		}
		return null;
	}
	
	public String fieldNameForColumn(String table, String column) {
		BColumn[] cols = this.columnsForTable(table);
		String field = "";
		for(int i = 0; i < cols.length; i++) {
			if(cols[i].name.equals(column)) {
				field = cols[i].fieldName;
				break;
			}
		}
		return field;
	}
	
	public int typeForColumn(String table, String column)
	{
		BColumn[] cols = this.columnsForTable(table);
		int fieldType = 0;
		for(int i = 0; i < cols.length; i++) {
			if(cols[i].name.equals(column)) {
				fieldType = cols[i].type;
				break;
			}
		}
		return fieldType;
	}
	
	public String columnNameForField(String table, String field) {
		BColumn[] cols = this.columnsForTable(table);
		String column = "";
		for(int i = 0; i < cols.length; i++) {
			if(cols[i].fieldName.equals(field)) {
				column = cols[i].name;
				break;
			}
		}
		return column;
	}
}
