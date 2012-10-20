package com.brecord;

public class BR {
	
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
	
}
