package org.backend.parceTools;

import java.util.ArrayList;

import org.backend.exceptions.BackEndException;



/**
 * Abstract type to represent all types of code lines.
 */
public abstract class Line {
	
	int id; 		// line Id
	int mapId;		// line original Id, which corresponds to the line in the original source code
	String comment;
	
	public Line(int id) {
		this.id = id;
		this.mapId = id;
		this.comment = "";
	}
	
	public Line(int id, int mapId) {
		this.id = id;
		this.mapId = mapId;
		this.comment = "";
	}
	
	public Line(int id, int mapId, String comment) {
		this.id = id;
		this.mapId = mapId;
		this.comment = comment;
	}
	
	public void showContent() {
        System.out.println("ID : "+this.id+" trueLien : "+this.mapId);
	}
	
	// GETTERS AND SETTERS
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	/**
	 * 
	 * This function returns the final code of the line as a string.
	 * 
	 * @param code This parameters is only used by the LineGoto Class to find the number of the line pointed to.
	 */
	public abstract String getLineCode(ArrayList<Line> lines) throws BackEndException;
	
	public abstract String getLineCodeNoComment(ArrayList<Line> lines) throws BackEndException;
	
	static int ID_BEGIN = -1;
	static int ID_END = -2;
	static int ID_NOT_FOUND = -3;


}