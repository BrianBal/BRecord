package com.brecord.sample.models;

import com.brecord.BRecord;

public class Contact extends BRecord {
	
	protected String first_name = "";
	protected String last_name = "";
	protected String phone = "";
	
	
	public Contact() {
		
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer value) {
		id = value;
	}
	
	public String getFirstName() {
		return first_name;
	}
	
	public void setFirstName(String value) {
		first_name = value;
	}
	
	public String getLastName() {
		return last_name;
	}
	
	public void setLastName(String value) {
		last_name = value;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String value) {
		phone = value;
	}
	
}
