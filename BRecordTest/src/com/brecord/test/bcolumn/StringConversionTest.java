package com.brecord.test.bcolumn;

import com.brecord.BColumn;

import junit.framework.TestCase;

public class StringConversionTest extends TestCase {
	
	public StringConversionTest(String name) {
		super(name);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void test_column_to_field_no_change() {
		String col = "name";
		String field = "name";
		BColumn bcol = new BColumn();
		
		assertEquals(field, bcol.columnNameToFieldName(col));
	}
	
	public void test_column_to_field_one_change() {
		String col = "first_name";
		String field = "firstName";
		BColumn bcol = new BColumn();
		
		assertEquals(field, bcol.columnNameToFieldName(col));
	}
	
	public void test_column_to_field_many_changes() {
		String col = "this_is_my_first_name_dude";
		String field = "thisIsMyFirstNameDude";
		BColumn bcol = new BColumn();
		
		assertEquals(field, bcol.columnNameToFieldName(col));
	}
	
}
