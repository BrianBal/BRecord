package com.brecord;

import java.lang.reflect.Field;
import java.util.Date;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class BRecord {
	
	public Integer id = -1;
	public Date created_at = null;
	public Date updated_at = null;
	
	public BRecord() {
		
	}
	
	/**
	 * @return
	 */
	public Boolean save() {
		if (id > 0) {
			return update();
		} else {
			return create();
		}
	}
	
	/**
	 * @return
	 */
	public Boolean create() {
		created_at = new Date();
		updated_at = new Date();
		BQuery query = new BQuery(this.getClass());
		return query.insert(this);
	}
	
	/**
	 * @return
	 */
	public Boolean update() {
		updated_at = new Date();
		BQuery query = new BQuery(this.getClass());
		return query.update(this);
	}
	
	/**
	 * @param property
	 * @param val
	 */
	public void setProperty(String property, Object val) {
		Field field;
		try {
			field = this.getClass().getField(property);
			String typeName = field.getType().getName();
			String pkgName = field.getType().getPackage().getName() + ".";
			typeName = typeName.replace(pkgName, "");
			field.setAccessible(true);
			if(typeName.equalsIgnoreCase("String")) {
				field.set(this, (String)val);
			} else if(typeName.equalsIgnoreCase("Integer") || typeName.equalsIgnoreCase("int")) {
				String strVal = val + "";
				int intVal = Integer.parseInt(strVal);
				field.set(this, intVal);
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
	
}
