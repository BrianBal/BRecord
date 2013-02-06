package com.brecord;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class BContentProvider extends ContentProvider
{
	UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	@Override
	public boolean onCreate()
	{
		return true;
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		String tableName = uri.getLastPathSegment();
		SQLiteDatabase db = BDatabase.getDatabase();
		
		Long id = db.insert(tableName, null, values);
		
		getContext().getContentResolver().notifyChange(uri, null);
		
		Uri insertedUri = null;
		if (id > 0)
		{
			insertedUri = Uri.parse("context://" + BConfig.AUTHORITY + "/" + tableName + "/" + id.toString());
		}
		return insertedUri;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		String tableName = uri.getLastPathSegment();
		SQLiteDatabase db = BDatabase.getReadDatabase();
		
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(tableName);
		
	    Cursor c = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
	    c.setNotificationUri(getContext().getContentResolver(), uri);
		
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
	{
		String tableName = uri.getLastPathSegment();
		SQLiteDatabase db = BDatabase.getDatabase();
		
		int affected = db.update(tableName, values, selection, selectionArgs);
		
		getContext().getContentResolver().notifyChange(uri, null);
		
		return affected;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		String tableName = uri.getLastPathSegment();
		SQLiteDatabase db = BDatabase.getDatabase();
		
		int affected = db.delete(tableName, selection, selectionArgs);
		
		getContext().getContentResolver().notifyChange(uri, null);
		
		return affected;
	}
	
	@Override
	public String getType(Uri arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
