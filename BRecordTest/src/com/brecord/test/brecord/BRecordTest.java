package com.brecord.test.brecord;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.brecord.BConfig;
import com.brecord.BR;
import com.brecord.sample.models.Contact;

import junit.framework.TestCase;

public class BRecordTest extends TestCase {

	public BRecordTest(String name) {
		super(name);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		BConfig.config.dropDataBase();
		SQLiteDatabase db = BConfig.config.getReadableDatabase();
		BConfig.config.migrate(db);
		
		for(int i = 0; i < 10; i++)
		{
			ContentValues vals = new ContentValues();
			vals.put("first_name", "John");
			vals.put("last_name", "Doe");
			vals.put("phone", "555-555-555" + i);
			db.insert("contacts", null, vals);
		}
		
		db.close();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void test_should_resolve_the_correct_class() {
		Contact contact = BR.find(Contact.class, 1);
		// this will ensure its of the correct type
		assertEquals("", contact.getFirstName());
	}

}
