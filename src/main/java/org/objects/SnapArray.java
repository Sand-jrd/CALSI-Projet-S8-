package org.objects;

import java.util.ArrayList;

public class SnapArray {

	private ArrayList<Object> REG;
	
	//Constructeur
	SnapArray(){
		this.REG = new ArrayList<Object>();
	}

	public ArrayList<Object> getREG() {
		return REG;
	}

	public void setREG(ArrayList<Object> rEG) {
		REG = rEG;
	}
	
	public ArrayList<Object> getREG(int i) {
		return REG;
	}

	public void setREG(int i,Object obj) {
		REG.set(i, obj);
	}
	
}
