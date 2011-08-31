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
}
