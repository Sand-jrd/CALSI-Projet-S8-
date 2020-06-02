package org.backend.parceTools;

import java.util.ArrayList;

import org.backend.exceptions.BackEndException;



/**
 * Class representing goto lines
 */
public class LineGoto extends Line {
	
	String condition;   // Condition du goto
	int toId;			// Goto to line "toId"
	
	public LineGoto(int id, String cond, int toid) {
		super(id);
		this.condition = cond;
		this.toId = toid;
		
	}
	
	public LineGoto(int id, int mapid, String cond, int toid) {
		super(id, mapid);
		this.condition = cond;
		this.toId = toid;   //true line of end of block
		
	}
	
	public LineGoto(int id, int mapid, String cond, int toid,String comment) {
		super(id, mapid, comment);
		this.condition = cond;
		this.toId = toid;   //true line of end of block
		
	}
	
	public void showContent() {
		super.showContent();
        System.out.println("Condition : "+this.condition +" toTRUEid :"+ this.toId );
	}
	
	/**
	 *	This function returns the final code of the line as a string.
	 *	It uses the parameter code to find the number of the line to which the goto is pointing.
	 *  This is because the goto only stores the id of the line, and the interpreter will need the actual number of
	 *  the line to be executed next.
	 */
	@Override
	public String getLineCode(ArrayList<Line> lines) throws BackEndException {
		String com = "";
		if (comment!="") {
			 com = "			//" + comment +"(line"+mapId+")";
		}
		if (this.condition=="FOR") {
			return "goto (" + "true" + ", " + (getIdFromMapId(lines,this.toId)+1) + ");"+com;
		}
		return "goto (" + this.condition + ", " + getIdFromMapId(lines,this.toId) + ");"+com;
	}
	
	@Override
	public String getLineCodeNoComment(ArrayList<Line> lines) throws BackEndException {
		
		if (this.condition=="FOR") {
			return "goto (" + "true" + ", " + (getIdFromMapId(lines,this.toId)+1) + ");";
		}
		return "goto (" + this.condition + ", " + getIdFromMapId(lines,this.toId) + ");";
	}
	
	protected int getIdFromMapId(ArrayList<Line> lines, int toId) {
		
		for (int i = 0; i < lines.size(); i++) {
			if(lines.get(i).getMapId()==toId) {
				return i;
			}
		}
		return lines.size();
	}

}
