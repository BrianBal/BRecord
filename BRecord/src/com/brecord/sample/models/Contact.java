package com.brecord.sample.models;

import com.brecord.BRecord;

public class Contact extends BRecord {
	
	private String first_name = "";
	private String last_name = "";
	private String phone = "";
	
	public Contact() {
		
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
