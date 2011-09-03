package com.brecord.association;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.brecord.BQuery;
import com.brecord.BRecord;
import com.brecord.BSchema;

public class  BAssociation {
	
	/**
	 * REQUIRED:
	 * Specify the class name of the association.
	 */
	public BRecord parent;
	
	/**
	 * REQUIRED:
	 * Specify the class name of the association.
	 */
	public Class associatedKlass;
	
	
	/**
	 * Specify the conditions that the associated object must meet in order to be included as a WHERE SQL fragment, such as authorized = 1.
	 */
	public ArrayList<String> conditions = new ArrayList<String>();
	
	/**
	 * 
	 */
	public ArrayList<String> orders = new ArrayList<String>();
	
	
	/**
	 * Specify the foreign key used for the association. By default this is guessed to be the name of the association with an “_id” suffix. So a class that defines a belongs_to :person association will use “person_id” as the default :foreign_key. Similarly, belongs_to :favorite_person, :class_name => "Person" will use a foreign key of “favorite_person_id”.
	 */
	public String foreignKey = null;
	

	/**
	 * Specify the column used to store the associated object’s type, if this is a polymorphic association. By default this is guessed to be the name of the association with a “_type” suffix. So a class that defines a belongs_to :taggable, :polymorphic => true association will use “taggable_type” as the default :foreign_type.
	 */
	public String foreignType = null;
	
	public int limit = -1;
	public int offset = -1;

	/**
	 * Specify this association is a polymorphic association by passing true. Note: If you’ve enabled the counter cache, then you may want to add the counter cache attribute to the attr_readonly list in the associated classes (e.g. class Post; attr_readonly :comments_count; end).
	 */
	public Boolean polymorphic = false;
	
	public BAssociation(BRecord p, Class aKlass) {
		parent = p;
		associatedKlass = aKlass;
		foreignKey = getForeignKeyFromClass(p.getClass());
	}
	
	public String getForeignKeyFromClass(Class klass) {
		String key = "";
		
		String name = klass.getName();
		String pkg = klass.getPackage().getName() + ".";
		name = name.replace(pkg, "");
		String[] parts = name.split("");
		String pre = "";
		for(int i = 0; i < parts.length; i++) {
			if(parts[i].matches("[A-Z]")) {
				key += pre + parts[i].toLowerCase();
				pre = "_";
			} else {
				key += parts[i].toLowerCase();
			}
		}
		key += "_id";
		
		return key;
	}
	
	public String getTableName(Class klass) {
		String key = "";
		
		String name = klass.getName();
		String pkg = klass.getPackage().getName() + ".";
		name = name.replace(pkg, "");
		String[] parts = name.split("");
		String pre = "";
		for(int i = 0; i < parts.length; i++) {
			if(parts[i].matches("[A-Z]")) {
				key += pre + parts[i].toLowerCase();
				pre = "_";
			} else {
				key += parts[i].toLowerCase();
			}
		}
		key += "s";
		
		return key;
	}
	
	public BQuery get() {
		BQuery query = new BQuery(associatedKlass);
		query.setWhereConditions(conditions);
		query.setOrderBy(orders);
		query.setLimit(limit);
		query.setOffset(offset);		
		return query;
	}
	
}
