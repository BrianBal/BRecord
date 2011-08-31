package com.brecord;

import java.lang.reflect.Field;

public class BRecord {
	
	@SuppressWarnings("unused")
	protected Integer id = -1;
	
	public BRecord() {
		
	}
	
	public void setProperty(String property, Object val) {
		Field field;
		try {
			field = this.getClass().getDeclaredField(property);
			String typeName = field.getType().getName();
			String pkgName = field.getType().getPackage().getName() + ".";
			typeName = typeName.replace(pkgName, "");
			field.setAccessible(true);
			if(typeName.equalsIgnoreCase("String")) {
				field.set(this, (String)val);
			} else if(typeName.equalsIgnoreCase("Integer") || typeName.equalsIgnoreCase("int")) {
				field.set(this, Integer.getInteger((String)val, 0));
			} else if(typeName.equalsIgnoreCase("Double") || typeName.equalsIgnoreCase("double")) {
				field.set(this, Double.parseDouble((String)val));
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/*
	public Class getBaseClass()
	public String getTableName();
	public String getPrimaryKey();
	public String[] getColumnNames();
	*/
}
