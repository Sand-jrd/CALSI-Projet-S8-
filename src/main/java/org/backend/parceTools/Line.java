package org.backend.parceTools;

import java.util.ArrayList;

import org.backend.exceptions.BackEndException;



/**
 * Abstract type to represent all types of code lines.
 */
public abstract class Line {
	
	public Line(int id) {
		this.id = id;
		this.mapId = id;
	}
	
	public Line(int id, int mapId) {
		this.id = id;
		this.mapId = mapId;
	}
	
	int id; 		// line Id
	int mapId;		// line original Id, which corresponds to the line in the original source code
	
	
	public void showContent() {
        System.out.println("ID : "+this.id+" trueLien : "+this.mapId);
	}
	
	/**
	 * 
	 * This function returns the final code of the line as a string.
	 * 
	 * @param code This parameters is only used by the LineGoto Class to find the number of the line pointed to.
	 */
	public abstract String getLineCode(ArrayList<Integer> code) throws BackEndException;
	
	static int ID_BEGIN = -1;
	static int ID_END = -2;
	static int ID_NOT_FOUND = -3;
}