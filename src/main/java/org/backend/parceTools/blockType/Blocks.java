package org.backend.parceTools.blockType;

import java.util.ArrayList;

public class Blocks {

	String type;
	int idStart;
    int idEnd;
	int truelineStart;
    int truelineStop;
    
    public Blocks (String type, int idStart,int idEnd){
    	this.type = type;
    	this.idStart = idStart;
    	this.idEnd = idEnd;
	}
    
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
	
	public int getTruelineStart() {
		return truelineStart;
	}
	public void setTruelineStart(int truelineStart) {
		this.truelineStart = truelineStart;
	}
	public int getTruelineStop() {
		return truelineStop;
	}
	public void setTruelineStop(int truelineStop) {
		this.truelineStop = truelineStop;
	}
	
}
