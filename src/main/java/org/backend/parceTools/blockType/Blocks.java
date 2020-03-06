package org.backend.parceTools.blockType;

import java.util.ArrayList;

public class Blocks {

	String type;
	int idStart;
    int idEnd;
    ArrayList<Blocks> blocksInside;
    
	// --- Getters and Setters --- //
    
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getIdStart() {
		return idStart;
	}
	public void setIdStart(int idStart) {
		this.idStart = idStart;
	}
	public int getIdEnd() {
		return idEnd;
	}
	public void setIdEnd(int idEnd) {
		this.idEnd = idEnd;
	}
	
	public ArrayList<Blocks> getBlocksInside() {
		return blocksInside;
	}
	public void setBlocksInside(ArrayList<Blocks> blocksInside) {
		this.blocksInside = blocksInside;
	}

	
}
