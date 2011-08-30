package com.brecord.test.query;

import java.util.ArrayList;
import java.util.Iterator;

import com.brecord.BQuery;
import com.brecord.BRecord;
import com.brecord.sample.models.Contact;

import junit.framework.TestCase;

public class BQueryTest extends TestCase {
	
	public BQueryTest(String name) {
		super(name);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void test_should_resolve_the_correct_tablename() {
		BQuery query = new BQuery(Contact.class);
		assertEquals("Should resolve the correct table name", "contacts", query.getTableName());
	}
	
	public void test_first_should_return_contact() {
		BQuery query = new BQuery(Contact.class);
		assertTrue(query.first().getClass().getName().endsWith("Contact"));
	}
	
	public void test_all_should_return_array_list_of_contacts() {
		BQuery query = new BQuery(Contact.class);
		ArrayList<Contact> result = query.all();
		Iterator<Contact> itr = result.iterator();
		if(itr.hasNext())
		{
			assertTrue(itr.next().getClass().getName().endsWith("Contact"));
		}else{
			assertTrue("results should not be null", false);
		}
		assertEquals("should return 10 contacts", 10, result.size());
	}
	
	public void test_where_should_add_a_where_condition_to_the_query() {
		BQuery query = new BQuery(Contact.class);
		query = query.where("test");
		assertTrue(query.buildSelectStatement().contains("test"));
	}
	
	public void test_where_should_add_mutiple_where_conditions_to_the_query() {
		BQuery query = new BQuery(Contact.class);
		query = query.where("test");
		query = query.where("saturday in the park");
		assertTrue(query.buildSelectStatement().contains("test"));
		assertTrue(query.buildSelectStatement().contains("saturday in the park"));
	}
	
	public void test_order_should_add_a_order_to_the_query() {
		BQuery query = new BQuery(Contact.class);
		query = query.order("test");
		assertTrue(query.buildSelectStatement().contains("test"));
	}
	
	public void test_order_should_add_multiple_orders_to_the_query() {
		BQuery query = new BQuery(Contact.class);
		query = query.order("test");
		query = query.order("saturday in the park");
		assertTrue(query.buildSelectStatement().contains("test"));
		assertTrue(query.buildSelectStatement().contains("saturday in the park"));
	}
	
	public void test_limit_should_add_a_limit_to_the_query() {
		BQuery query = new BQuery(Contact.class);
		query = query.limit(138);
		assertTrue(query.buildSelectStatement().contains("LIMIT 138"));
	}
	
	public void test_limit_should_add_a_offset_to_the_query() {
		BQuery query = new BQuery(Contact.class);
		query = query.offset(138);
		assertTrue(query.buildSelectStatement().contains("OFFSET 138"));
	}
	
	public void test_should_build_valid_sql_for_select() {
		BQuery query = new BQuery(Contact.class);
		assertEquals("SELECT * FROM contacts WHERE (1=1) ", query.buildSelectStatement());
	}
	
	public void test_should_be_able_to_chain_query_methods() {
		BQuery query = new BQuery(Contact.class);
		String sql = query.where("where1").order("order1").limit(20).offset(30).buildSelectStatement();
		assertEquals("SELECT * FROM contacts WHERE (1=1) AND (where1) ORDER BY order1 LIMIT 20 OFFSET 30 ", sql);
	}
	
}
