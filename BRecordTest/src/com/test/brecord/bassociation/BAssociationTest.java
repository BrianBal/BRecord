package com.test.brecord.bassociation;

import junit.framework.TestCase;

import com.brecord.association.BAssociation;
import com.brecord.sample.models.Contact;
import com.brecord.sample.models.PhoneNumber;

public class BAssociationTest extends TestCase {
	
	public void test_getForeignKeyFromClass_short() {
		BAssociation assoc = new BAssociation(Contact.class, PhoneNumber.class);
		assertEquals("ForeignKey should be correct", "contact_id", assoc.getForeignKeyFromClass(Contact.class));
	}
	
	public void test_getForeignKeyFromClass_long() {
		BAssociation assoc = new BAssociation(Contact.class, PhoneNumber.class);
		assertEquals("ForeignKey should be correct", "phone_number_id", assoc.getForeignKeyFromClass(PhoneNumber.class));
	}
	
}
