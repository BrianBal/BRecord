package com.brecord.test.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.brecord.App;
import com.brecord.BColumn;
import com.brecord.BConfig;
import com.brecord.BMigration;

import junit.framework.TestCase;

public class BMigrationTest extends TestCase {
	public BMigrationTest() {
		super();
	}
	
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void test_001_database_creation() {
		BConfig.config.dropDataBase();
		SQLiteDatabase db = BConfig.config.getReadableDatabase();
		BConfig.config.migrate(db);
		
		Cursor c = db.rawQuery("SELECT * FROM sqlite_master WHERE type='table' AND tbl_name='contacts'", null);
		printCursorContents(c);
		assertEquals(1, c.getCount());
		
		db.close();
	}
	
	private void printCursorContents(Cursor c)
	{
		Log.v("BConfigTest", "Cursor has a count of: " + c.getCount());

		if(c.moveToFirst())
		{
			for(int rows = 0;rows<c.getCount();rows++)
			{
				for(int cols = 0;cols<c.getColumnCount();cols++)
				{

					Log.v("BConfigTest", "Row " + rows + ": " + c.getPosition() + " Col " + cols + " " + c.getColumnName(cols) + ": " + c.getString(cols));
				}

				c.moveToNext();
			}
		}
	}
}
