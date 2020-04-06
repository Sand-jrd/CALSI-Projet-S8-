package org.backend.parceTools;

import java.util.ArrayList;

import org.backend.exceptions.BackEndException;



/**
 * Class representing a simple line to be executed by the interpreter
 */
public class LineString extends Line {
	
	public LineString(int id, String code) {
		super(id);
		this.code = code;
	}
	
	public LineString(int id,int mapid, String code) {
		super(id,mapid);
		this.code = code;
	}
	public void showContent() {
		super.showContent();
        System.out.println("code: '"+this.code +"'" );
	}


	@Override
	public String getLineCode(ArrayList<Integer> code) throws BackEndException {
		return this.code;
	}
	
	
	String code;
}
