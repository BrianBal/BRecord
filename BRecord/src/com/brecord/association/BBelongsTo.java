package com.brecord.association;

public class BBelongsTo extends BAssociation {

	public BBelongsTo(Class pKlass, Class aKlass) {
		super(pKlass, aKlass);
		foreignKey = "id";
		limit = 1;
		offset = 1;
	}

}
