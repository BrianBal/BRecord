package com.brecord;

/**
 * @author bal BColumn maps database columns to names and types
 */
public class BColumn
{

	/**
	 * The value is a signed integer, stored in 1, 2, 3, 4, 6, or 8 bytes
	 * depending on the magnitude of the value.
	 */
	public static final int TYPE_INTEGER = 0;

	/**
	 * The value is a floating point value, stored as an 8-byte IEEE floating
	 * point number.
	 */
	public static final int TYPE_REAL = 1;

	/**
	 * The value is a text string, stored using the database encoding (UTF-8,
	 * UTF-16BE or UTF-16LE).
	 */
	public static final int TYPE_TEXT = 2;

	/**
	 * . The value is a blob of data, stored exactly as it was input.
	 */
	public static final int TYPE_BLOB = 3;

	/**
	 * . The value is a blob of data, stored exactly as it was input.
	 */
	public static final int TYPE_DATETIME = 4;

	/**
	 * . The value is a blob of data, stored exactly as it was input.
	 */
	public static final int TYPE_BOOLEAN = 5;
	public static final int TYPE_LONG = 6;

	/**
	 * The name of the column in the database
	 */
	public String name;

	/**
	 * The type of the column in the database
	 */
	public int type;

	/**
	 * Default value of the column in the database
	 */
	public String defaultValue;

	/**
	 * true if the value be null
	 */
	public Boolean canBeNull;

	/**
	 * The field name that maps to this column
	 */
	public String fieldName;

	public BColumn()
	{
		name = "";
		type = 0;
		fieldName = "";
		canBeNull = true;
	}

	public BColumn(String columnName, int columnType)
	{
		name = columnName;
		type = columnType;
		fieldName = columnNameToFieldName(columnName);
		canBeNull = true;
	}

	public BColumn(String columnName, int columnType, String theDefault)
	{
		name = columnName;
		type = columnType;
		fieldName = columnNameToFieldName(columnName);
		defaultValue = theDefault;
		canBeNull = true;
	}

	public BColumn(String columnName, int columnType, String theDefault, Boolean canNull)
	{
		name = columnName;
		type = columnType;
		fieldName = columnNameToFieldName(columnName);
		canBeNull = canNull;
	}

	public String getColumnSQL()
	{
		String sql = name + " ";
		switch (type)
		{
			case TYPE_BOOLEAN:
			case TYPE_INTEGER:
			case TYPE_LONG:
				sql += "INTEGER ";
				break;
			case TYPE_DATETIME:
			case TYPE_REAL:
				sql += "REAL ";
				break;
			case TYPE_TEXT:
				sql += "TEXT ";
				break;
			case TYPE_BLOB:
				sql += "BLOB ";
				break;
		}

		if (canBeNull == false)
		{
			sql += "NOT NULL ";
		}

		if (defaultValue != null)
		{
			sql += "DEFAULT ('" + defaultValue + "') ";
		}

		return sql;
	}

	public String columnNameToFieldName(String input)
	{
		input = input.toLowerCase();
		String[] parts = input.split("_");
		String output = "";
		for (int i = 0; i < parts.length; i++)
		{
			String part = parts[i];
			if (i != 0)
			{
				String start = part.substring(0, 1).toUpperCase();
				String end = part.substring(1, part.length());
				output += start + end;
			}
			else
			{
				output += part;
			}
		}

		return output;
	}
}
