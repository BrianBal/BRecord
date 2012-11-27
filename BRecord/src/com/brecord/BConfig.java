package com.brecord;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BConfig extends SQLiteOpenHelper {
	
	public static BConfig config;
	public static String DB_LOCK = "dblock";
	
	public BConfig(Context context, String name, CursorFactory factory, int version) {
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
	
	public void migrate(SQLiteDatabase db, int currentVersion) {
		BMigration.Migrate(db, currentVersion);
	}
	
	@Override
	public SQLiteDatabase getWritableDatabase()
	{
		synchronized(DB_LOCK)
		{
			if (BR.database == null || BR.database.isOpen() == false)
			{
				BR.database = super.getWritableDatabase();
			}
		}
		return BR.database;
	}
	
	public void closeDatabase()
	{
		synchronized(DB_LOCK)
		{
			if (BR.database != null && BR.database.isOpen())
			{
				BR.database.close();
			}
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
