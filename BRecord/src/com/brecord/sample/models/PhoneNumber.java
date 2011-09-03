package com.brecord.sample.models;

import com.brecord.BRecord;
import com.brecord.association.BHasMany;
import com.brecord.association.BHasOne;

public class PhoneNumber extends BRecord {
	
	/* variables that are mapped to table columns */
	public Integer contactId = -1;
	public String label = "";
	public String number = "";
	
	/* Associations */
	public BHasOne contact;
	
	public PhoneNumber() {
		
	}
	
}
