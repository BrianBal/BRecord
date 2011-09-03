package com.brecord.association;

public class BHasOne extends BAssociation {

	public BHasOne(Class pKlass, Class aKlass) {
		super(pKlass, aKlass);
		limit = 1;
		offset = 0;
	}

}
