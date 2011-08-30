package com.brecord;

public class BR {
	
	public static <T extends BRecord> T find(Class klass, int id) {
		BQuery query = new BQuery(klass);
		T result = query.where("id = " + id).first();
		return result;
	}
	
	public static BQuery find(Class klass) {
		BQuery query = new BQuery(klass);
		return query;
	}
	
}
