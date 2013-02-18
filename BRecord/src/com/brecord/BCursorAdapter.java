package com.brecord;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class BCursorAdapter<T extends BRecord> extends CursorAdapter
{
	private BQuery query;
	
	public BCursorAdapter(Context context, BQuery theQuery, Cursor cursor, Boolean autoRequery)
    {
	    super(context, cursor, autoRequery);
	    this.query = theQuery;
    }

	@SuppressWarnings("unchecked")
    @Override
	public void bindView(View view, Context context, Cursor cursor)
	{
		T row = null;
		try
		{
			row = (T) query.klass.newInstance();
			row.id = cursor.getInt(cursor.getColumnIndex("id"));
			
			ArrayList<String> columnNames = query.getTableColumns();
			Iterator<String> itr = columnNames.iterator();
			String tName = query.getTableName();
			while(itr.hasNext())
			{
				String col = itr.next();
				BColumn bcol = BSchema.schema.getColumnInTable(tName, col);
				switch (bcol.type)
				{
					case BColumn.TYPE_BLOB:
					case BColumn.TYPE_TEXT:
						row.setProperty(bcol.fieldName, cursor.getString(cursor.getColumnIndex(col)));
						break;
					case BColumn.TYPE_BOOLEAN:
						row.setProperty(bcol.fieldName, cursor.getInt(cursor.getColumnIndex(col)) > 1);
						break;
					case BColumn.TYPE_REAL:
					case BColumn.TYPE_DATETIME:
						row.setProperty(bcol.fieldName, cursor.getDouble(cursor.getColumnIndex(col)));
						break;
					case BColumn.TYPE_INTEGER:
						row.setProperty(bcol.fieldName, cursor.getInt(cursor.getColumnIndex(col)));
						break;
					case BColumn.TYPE_LONG:
						row.setProperty(bcol.fieldName, cursor.getLong(cursor.getColumnIndex(col)));
						break;
				}
				
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		bindViewToObject(view, context, row);
	}
	
	@SuppressWarnings("unchecked")
    @Override
	public Object getItem (int position)
	{
		Cursor cursor = (Cursor)super.getItem(position);
		T row = null;
		try
		{
			row = (T) query.klass.newInstance();
			row.id = cursor.getInt(cursor.getColumnIndex("id"));
			
			ArrayList<String> columnNames = query.getTableColumns();
			Iterator<String> itr = columnNames.iterator();
			String tName = query.getTableName();
			while(itr.hasNext())
			{
				String col = itr.next();
				BColumn bcol = BSchema.schema.getColumnInTable(tName, col);
				switch (bcol.type)
				{
					case BColumn.TYPE_BLOB:
					case BColumn.TYPE_TEXT:
						row.setProperty(bcol.fieldName, cursor.getString(cursor.getColumnIndex(col)));
						break;
					case BColumn.TYPE_BOOLEAN:
						row.setProperty(bcol.fieldName, cursor.getInt(cursor.getColumnIndex(col)) > 1);
						break;
					case BColumn.TYPE_REAL:
					case BColumn.TYPE_DATETIME:
						row.setProperty(bcol.fieldName, cursor.getDouble(cursor.getColumnIndex(col)));
						break;
					case BColumn.TYPE_INTEGER:
						row.setProperty(bcol.fieldName, cursor.getInt(cursor.getColumnIndex(col)));
						break;
					case BColumn.TYPE_LONG:
						row.setProperty(bcol.fieldName, cursor.getLong(cursor.getColumnIndex(col)));
						break;
				}
				
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return row;
	}
	
	public void bindViewToObject(View view, Context context, T object)
	{
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2)
	{
		return null;
	}

}
