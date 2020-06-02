package org.backend.parceTools;

import java.util.ArrayList;

import org.backend.exceptions.BackEndException;



/**
 * Class representing a simple line to be executed by the interpreter
 */
public class LineString extends Line {
	
	String code; // String code line
	
	public LineString(int id, String code) {
		super(id);
		this.code = code;
	}
	
	public LineString(int id,int mapid, String code) {
		super(id,mapid);
		this.code = code;
	}
	
	public LineString(int id,int mapid, String code,String comment) {
		super(id,mapid,comment);
		this.code = code;
	}
	

	public void showContent() {
		super.showContent();
        System.out.println("code: '"+this.code +"'" );
	}


	@Override
	public String getLineCode(ArrayList<Line> lines) throws BackEndException {
		String com = "";
		if (comment!="") {
			 com = "			//" + comment +"(line"+mapId+")";
		}
		return this.code+com;
	}
	
	@Override
	public String getLineCodeNoComment(ArrayList<Line> lines) throws BackEndException {
		return this.code;
	}

}
