package com.brecord;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class BDatabase extends SQLiteOpenHelper {
	
	final public static String LOCK = "brecord_lock";
	
	private static BDatabase sharedDatabase;
	
	public static void setup(Context context, String name, CursorFactory factory, int version)
	{
		sharedDatabase = new BDatabase(context, name, factory, version);
	}
	
	public static SQLiteDatabase getDatabase()
	{
		SQLiteDatabase db = null;
		try {
			db = sharedDatabase.getWritableDatabase();
		}
		catch (SQLiteException e)
		{
			e.printStackTrace();
		}
		return db;
	}
	
	public BDatabase(Context context, String name, CursorFactory factory, int version) 
	{
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		try{
			migrate(db, 0);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try{
			migrate(db, oldVersion);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public SQLiteDatabase getWritableDatabase()
	{
		if (BR.database == null || BR.database.isOpen() == false)
		{
			BR.database = super.getWritableDatabase();
		}
		return BR.database;
	}
	
	public void migrate(SQLiteDatabase db, int currentVersion) {
		BMigration.Migrate(db, currentVersion);
	}
	
	public static void closeDatabase()
	{
		if (BR.database != null && BR.database.isOpen())
		{
			BR.database.close();
		}
	}
	
	public void dropDataBase() {
		SQLiteDatabase db = getWritableDatabase();
		
		//Drop tables
		Cursor c = db.rawQuery("SELECT * FROM sqlite_master WHERE type='table'", null);
		if(c.getCount() > 0){	
			//App.printCursorContents(c);
			while(c.moveToNext()) {
				String tableName = c.getString(1);
				if(!(tableName.equals("android_metadata") || tableName.equals("sqlite_sequence"))){
					try {
						db.execSQL("DROP TABLE " + tableName + ";");
					} catch (Exception e){
						e.printStackTrace();
					}
				}
			}
		}
		
		c.close();
		db.close();
	}
	
}
