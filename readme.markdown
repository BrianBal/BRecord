BRecord
=======

Installation
------------

 - Setup your Application subclass
 - Setup your BConfig
 - Setup your Migrations

Creating Models
---------------

 - Create your BRecord sub class
 - if your table name is contacts your class name needs to be Contact
 - create public variables for each of your table columns
 - done

Creating Migrations
-------------------

 - add migration to Application subclass

Querying
--------

	Contact contact = BR.find(Contact.class).where("first_name = 'Billy'").first();
	
	ArrayList<contact> contacts = BR.find(Contact.class).where("last_name = 'Doe'").order("phone ASC").all();
	

Inserting Records
-----------------

	Contact contact = new Contact();
	contact.first_name = "John";
	... setup the rest of your class properties ...
	if(contact.save()){
		// create was successfull
	} else {
		// create failed
	}

Updating Records
----------------

	Contact contact = BR.find(Contact.class).where("first_name = 'Billy'").first();
	contact.first_name = "John";
	... setup the rest of your class properties ...
	if(contact.save()){
		// update was successfull
	} else {
		// update failed
	}