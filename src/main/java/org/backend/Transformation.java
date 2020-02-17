package org.backend;

public class Transformation {
	
	String[] code;
	Integer[] mapping;
	
	//Classique Builder
	public Transformation() {
		
	}
	
	//Builder for deepCopie
	public Transformation(Transformation transOld) {
		this.code = transOld.getCode();
		this.mapping = transOld.getMapping();
	}
	
	public Transformation(String[] c, int n) {

		this.code = c;
		
		Integer[] tab = new Integer[n];
		for(int i=0; i<n; ++i) {
			tab[i] = i;
		}
		
		this.mapping = tab;
	}
	
	// -- Getters -- //
	
	public String[] getCode() {
		return this.code;
	}
	
	public Integer[] getMapping() {
		return this.mapping;
	}
	
}
