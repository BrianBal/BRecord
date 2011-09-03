package com.test.brecord.bassociation;

import java.util.ArrayList;
import java.util.Iterator;

import junit.framework.TestCase;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.brecord.BConfig;
import com.brecord.BR;
import com.brecord.association.BAssociation;
import com.brecord.sample.models.Contact;
import com.brecord.sample.models.PhoneNumber;

public class BAssociationTest extends TestCase {
	
	protected void setUp() throws Exception {
		super.setUp();
		BConfig.config.dropDataBase();
		SQLiteDatabase db = BConfig.config.getReadableDatabase();
		BConfig.config.migrate(db);
		
		ContentValues vals = new ContentValues();
		vals.put("first_name", "John");
		vals.put("last_name", "Doe");
		vals.put("phone", "555-555-5550");
		int id = (int) db.insert("contacts", null, vals);
		
		ContentValues numvals = new ContentValues();
		numvals.put("contact_id", id);
		numvals.put("number", "555-555-1234");
		numvals.put("label", "iPhone");
		int p1id = (int) db.insert("phone_numbers", null, numvals);
		
		ContentValues numvals2 = new ContentValues();
		numvals2.put("contact_id", id);
		numvals2.put("number", "555-555-4312");
		numvals2.put("label", "Android");
		int p2id = (int) db.insert("phone_numbers", null, numvals);
		
		ContentValues numvals3 = new ContentValues();
		numvals.put("contact_id", id);
		numvals.put("number", "555-555-0000");
		numvals.put("label", "Home");
		int p3id = (int) db.insert("phone_numbers", null, numvals);
		
		Log.v("p1", "p1id: " + p1id);
		Log.v("p2", "p2id: " + p2id);
		Log.v("p3", "p3id: " + p3id);
		
		db.close();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void test_getForeignKeyFromClass_short() {
		Contact contact = new Contact();
		BAssociation assoc = new BAssociation(contact, PhoneNumber.class);
		assertEquals("ForeignKey should be correct", "contact_id", assoc.getForeignKeyFromClass(Contact.class));
	}
	
	public void test_getForeignKeyFromClass_long() {
		PhoneNumber phoneNumber = new PhoneNumber();
		BAssociation assoc = new BAssociation(phoneNumber, Contact.class);
		assertEquals("ForeignKey should be correct", "phone_number_id", assoc.getForeignKeyFromClass(PhoneNumber.class));
	}
	
	public void test_has_man_get_size() {
		Contact contact = BR.find(Contact.class).first();
		ArrayList<PhoneNumber> numbers = contact.phoneNumbers.get().all();
		assertEquals("Should have 3 phone numbers", 3, numbers.size());
	}
	
	public void test_has_man_get_values() {
		Contact contact = BR.find(Contact.class).first();
		ArrayList<PhoneNumber> numbers = contact.phoneNumbers.get().all();
		Iterator<PhoneNumber> itr = numbers.iterator();
		while(itr.hasNext()) {
			PhoneNumber number = itr.next();
			assertEquals("number should all be from the correct contact", contact.id, number.contactId);
		}
	}
	
}
