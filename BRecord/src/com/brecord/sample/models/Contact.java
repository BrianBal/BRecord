package com.brecord.sample.models;

import com.brecord.BRecord;

public class Contact extends BRecord {
	
	/* variables that are mapped to table columns */
	public String firstName = "";
	public String lastName = "";
	public String phone = "";
	
	/* just used for testing */
	private boolean didCallBeforeSave = false;
	private boolean didCallBeforeCreate = false;
	private boolean didCallBeforeUpdate = false;
	
	private boolean didCallAfterSave = false;
	private boolean didCallAfterCreate = false;
	private boolean didCallAfterUpdate = false;
	
	public Contact() {
		
	}
	
	/* Callbacks */
	protected void beforeSave() { didCallBeforeSave = true; }
	protected void beforeCreate() { didCallBeforeCreate = true; }
	protected void beforeUpdate() { didCallBeforeUpdate = true; }
	
	protected void afterSave() { didCallAfterSave = true; }
	protected void afterCreate() { didCallAfterCreate = true; }
	protected void afterUpdate() { didCallAfterUpdate = true; }
	
	public boolean getDidCallBeforeSave() {
		return didCallBeforeSave;
	}
	
	public boolean getDidCallBeforeCreate() {
		return didCallBeforeCreate;
	}
	
	public boolean getDidCallBeforeUpdate() {
		return didCallBeforeUpdate;
	}
	
	public boolean getDidCallAfterSave() {
		return didCallAfterSave;
	}
	
	public boolean getDidCallAfterCreate() {
		return didCallAfterCreate;
	}
	
	public boolean getDidCallAfterUpdate() {
		return didCallAfterUpdate;
	}
	
}
