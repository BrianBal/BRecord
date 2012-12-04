package com.brecord;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class App extends Application {
	private static Application sApp;
	public static String DATABASE_NAME = "brecord.db";
	public static int DATABASE_VERSION = 1;
	
	@Override
	public void onCreate() {
		super.onCreate();
		sApp = this;
		Log.d("App", "Application onCreate: ");
		
		@SuppressWarnings("unused")
		BMigration migration1 = new BMigration("contacts", new BColumn[] {
				new BColumn("first_name", BColumn.TYPE_TEXT, "John", false),
				new BColumn("last_name", BColumn.TYPE_TEXT, "Doe", false),
				new BColumn("phone", BColumn.TYPE_TEXT, "555-555-5555", false)
		});
		
		@SuppressWarnings("unused")
		BMigration migration2 = new BMigration("phone_numbers", new BColumn[] {
				new BColumn("contact_id", BColumn.TYPE_INTEGER),
				new BColumn("label", BColumn.TYPE_TEXT, "mobile", false),
				new BColumn("number", BColumn.TYPE_TEXT, "555-555-5555", false)
		});
		
		BDatabase.setup(sApp, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onTerminate() {
		Log.d("App", "Application onTerminate: ");
		super.onTerminate();
	}
	
	public static Context getContext() {
		return sApp;
	}
	
	public static Application getInstance()
	{
		if(sApp == null)sApp = new App();
		
		return sApp;
	}
	
}
