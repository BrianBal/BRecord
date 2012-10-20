package com.brecord.association;

import com.brecord.BQuery;
import com.brecord.BRecord;

public class BHasMany extends BAssociation {

	@SuppressWarnings("rawtypes")
	public BHasMany(BRecord p, Class aKlass) {
		super(p, aKlass);
	}
	
	public BQuery get() {
		BQuery query = new BQuery(associatedKlass);
		query.setWhereConditions(conditions);
		query.setOrderBy(orders);
		query.setLimit(limit);
		query.setOffset(offset);		
		return query.where(this.getForeignKeyFromClass(parent.getClass()) + " = " + parent.id);
	}

}
