package com.brecord;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import android.content.ContentValues;

public class BRecord
{

	public Integer id = -1;
	public Date createdAt = new Date(0);
	public Date updatedAt = new Date(0);

	public BRecord()
	{

	}
	
	public BRecord clone()
	{
		return new BRecord();
	}

	public void setupAssociations()
	{

	}

	/**
	 * @return
	 */
	public Boolean save()
	{
		if (id >= 0)
		{
			return update();
		}
		else
		{
			return create();
		}
	}

	/**
	 * @return
	 */
	public Boolean create()
	{
		beforeSave();
		beforeCreate();

		createdAt = new Date();
		updatedAt = new Date();
		BQuery query = new BQuery(this.getClass());

		Boolean result = query.insert(this);
		if (result)
		{
			afterSave();
			afterCreate();
		}
		
		return result;
	}

	/**
	 * @return
	 */
	public Boolean update()
	{
		beforeSave();
		beforeUpdate();

		updatedAt = new Date();
		BQuery query = new BQuery(this.getClass());

		Boolean result = query.update(this);
		if (result)
		{
			afterSave();
			afterUpdate();
		}
		
		return result;
	}

	/**
	 * @return
	 */
	public Boolean destroy()
	{
		BQuery query = new BQuery(this.getClass());

		if (query.destroy(this))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * @param property
	 * @param val
	 */
	public void setProperty(String property, Object val)
	{
		Field field;
		try
		{
			field = this.getClass().getField(property);
			String typeName = field.getType().getName();
			String pkgName = field.getType().getPackage().getName() + ".";
			typeName = typeName.replace(pkgName, "");
			field.setAccessible(true);
			if (typeName.equalsIgnoreCase("String"))
			{
				field.set(this, (String) val);
			}
			else if (typeName.equalsIgnoreCase("Integer") || typeName.equalsIgnoreCase("int"))
			{
				field.set(this, (Integer)val);
			}
			else if (typeName.equalsIgnoreCase("Long") || typeName.equalsIgnoreCase("long"))
			{
				field.set(this, (Long)val);
			}
			else if (typeName.equalsIgnoreCase("Double") || typeName.equalsIgnoreCase("double"))
			{
				field.set(this, (Double) val);
			}
			else if (typeName.equalsIgnoreCase("Date"))
			{
				Date dateVal = new Date(0);
				if (val != null)
				{
					dateVal = new Date((long) ((Double)val * 1000.0));
				}
				field.set(this, dateVal);
			}
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}

	/* Associations */
	@SuppressWarnings("rawtypes")
	protected void hasMany(Class klass)
	{
		// TODO: implement this
	}

	@SuppressWarnings("rawtypes")
	protected void hasOne(Class klass)
	{
		// TODO: implement this
	}

	@SuppressWarnings("rawtypes")
	protected void belongsTo(Class klass)
	{
		// TODO: implement this
	}

	@SuppressWarnings("rawtypes")
	protected void hasAndBelongsToMany(Class klass)
	{
		// TODO: implement this
	}
	
	/* Misc */
	
	public String getTableName()
	{
		String key = "";
		
		String name = getClass().getName();
		String pkg = getClass().getPackage().getName() + ".";
		name = name.replace(pkg, "");
		String[] parts = name.split("");
		String pre = "";
		for(int i = 0; i < parts.length; i++) {
			if(parts[i].matches("[A-Z]")) {
				key += pre + parts[i].toLowerCase();
				pre = "_";
			} else {
				key += parts[i].toLowerCase();
			}
		}
		key += "s";
		
		return key;
	}
	
	public ArrayList<Field> getFieldsForTableColumns()
	{
		String tableName = getTableName();
		BColumn[] cols = BSchema.schema.columnsForTable(tableName);
		ArrayList<Field> fields = new ArrayList<Field>();
		
		if (cols != null) {
			for(int i = 0; i < cols.length; i++) {
				try {
					fields.add(getClass().getField(cols[i].fieldName));
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}
			}
		}
		return fields;
	}
	
	public ContentValues getContentValues()
	{
		ArrayList<Field> cols = getFieldsForTableColumns();
		ContentValues vals = new ContentValues();
		
		Iterator<Field> itr = cols.iterator();
		while(itr.hasNext()) {
			Field field = itr.next();
			String fs = field.getName();
			String col = BSchema.schema.columnNameForField(getTableName(), fs);
			String val;
			String typeName = field.getType().getName();
			String pkgName = field.getType().getPackage().getName() + ".";
			typeName = typeName.replace(pkgName, "");
			try {
				if (typeName.equalsIgnoreCase("Boolean"))
				{
					int bval = 0;
					val = field.get(this).toString();
					if (val != null)
					{
						bval = Boolean.parseBoolean(val) == true ? 1 : 0;
					}
					vals.put(col, bval);
				}
				else if (typeName.equalsIgnoreCase("Date"))
				{
					Date date = (Date)field.get(this);
					if (date != null)
					{
						Double dval = date.getTime() / 1000.0;
						vals.put(col, dval);
					}
				}
				else if (typeName.equalsIgnoreCase("Long"))
				{
					vals.put(col, field.getLong(this));
				}
				else if (! col.equalsIgnoreCase("id") && ! col.equalsIgnoreCase(""))
				{
					val = field.get(this).toString();
					vals.put(col, val);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return vals;
	}

	/* Callbacks */

	protected void beforeSave()
	{
	}

	protected void beforeCreate()
	{
	}

	protected void beforeUpdate()
	{
	}

	protected void afterSave()
	{
	}

	protected void afterCreate()
	{
	}

	protected void afterUpdate()
	{
	}

}
