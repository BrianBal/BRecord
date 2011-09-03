package com.brecord.association;

public class BHasMany extends BAssociation {

	public BHasMany(Class pKlass, Class aKlass) {
		super(pKlass, aKlass);
		foreignKey = associatedKlass.getName();
	}

}
