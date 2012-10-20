package com.brecord;

import java.lang.reflect.Field;
import java.util.Date;

public class BRecord
{

	public Integer id = -1;
	public Date createdAt = new Date(0);
	public Date updatedAt = new Date(0);

	public BRecord()
	{

	}

	public void setupAssociations()
	{

	}

	/**
	 * @return
	 */
	public Boolean save()
	{
		if (id > 0)
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

		if (query.insert(this))
		{
			afterSave();
			afterCreate();
			return true;
		}
		else
		{
			return false;
		}
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

		if (query.update(this))
		{
			afterSave();
			afterUpdate();
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * @return
	 */
	public Boolean destroy()
	{
		// TODO: implement this
		return false;
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
				String strVal = val + "";
				int intVal = Integer.parseInt(strVal);
				field.set(this, intVal);
			}
			else if (typeName.equalsIgnoreCase("Long") || typeName.equalsIgnoreCase("long"))
			{
				String strVal = val + "";
				Long longVal = Long.parseLong(strVal);
				field.set(this, longVal);
			}
			else if (typeName.equalsIgnoreCase("Double") || typeName.equalsIgnoreCase("double"))
			{
				field.set(this, Double.parseDouble((String) val));
			}
			else if (typeName.equalsIgnoreCase("Date"))
			{
				Date dateVal = new Date(0);
				if (val != null)
				{
					double dval = Double.parseDouble((String) val);
					dateVal = new Date((long) (dval * 1000.0));
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
