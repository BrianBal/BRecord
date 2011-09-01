package com.brecord;

import java.util.ArrayList;
import java.util.Iterator;

public class BSchema {
	public static BSchema schema = new BSchema();
	
	public ArrayList<BMigration> tables = new ArrayList<BMigration>();
	
	public BColumn[] columnsForTable(String table) {
		Iterator<BMigration> itr = tables.iterator();
		while(itr.hasNext()) {
			BMigration migration = itr.next();
			if (migration.table.equalsIgnoreCase(table)) {
				return migration.columns;
			}
		}
		return null;
	}
	
	public String fieldNameForColumn(String table, String column) {
		BColumn[] cols = this.columnsForTable(table);
		String field = "";
		for(int i = 0; i < cols.length; i++) {
			if(cols[i].name.equalsIgnoreCase(column)) {
				field = cols[i].fieldName;
				break;
			}
		}
		return field;
	}
	
	public String columnNameForField(String table, String field) {
		BColumn[] cols = this.columnsForTable(table);
		String column = "";
		for(int i = 0; i < cols.length; i++) {
			if(cols[i].fieldName.equalsIgnoreCase(field)) {
				column = cols[i].name;
				break;
			}
		}
		return column;
	}
}
