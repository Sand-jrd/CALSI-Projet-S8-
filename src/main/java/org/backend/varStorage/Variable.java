package org.backend.varStorage;

import java.util.Arrays;

public class Variable {
	protected String name;
	protected boolean isArray;
	protected String type;
	protected Object obj;

	public Variable(String name, Object obj) {
		this.name = name;
		this.obj = obj;
		this.type = obj.getClass().getName();
		this.isArray = obj.getClass().isArray();
	}

	public Variable(String name) {
		this.name = name;
		this.type = "";
		this.isArray = false;
	}

	public void update(Object obj) {
		
		if (obj != null) {
			this.obj = obj;
			this.type = obj.getClass().getName();
			this.isArray = obj.getClass().isArray();
		}
		
	}

	public final String getName() {
		return this.name;
	}

	public final boolean isArray() {
		return this.isArray;
	}

	public final Object getObj() {
		return this.obj;
	}
	
	public final String getType() {
		return this.type;
	}
	
	public Object getRealValue() {
		
		return this.obj;

	}
	
	public String getValue() {	
		
		if (isArray) {
			return Arrays.deepToString((Object[]) obj);
		}

		if (this.obj != null) {

			return this.obj.toString();
		}
		
		return "null";
	}
}
