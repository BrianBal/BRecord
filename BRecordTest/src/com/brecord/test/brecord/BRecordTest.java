package com.brecord.test.brecord;

import com.brecord.BR;
import com.brecord.sample.models.Contact;

import junit.framework.TestCase;

public class BRecordTest extends TestCase {

	public BRecordTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void test_should_resolve_the_correct_class() {
		Contact contact = BR.find(Contact.class, 123);
		// this will ensure its of the correct type
		assertEquals("", contact.getFirstName());
	}

}
