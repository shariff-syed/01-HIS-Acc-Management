package com.ashokit.utils;

import java.io.Serializable;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class PlanIdGenerator implements IdentifierGenerator {
	int baseVal = 100000;

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) {
		String prefix = "HIS_PLAN";
		baseVal++;
		return prefix+baseVal;
	}
}
