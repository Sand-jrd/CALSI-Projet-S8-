package org.backend.parceTools;

import java.util.ArrayList;

import org.backend.exceptions.BackEndException;
import org.backend.parceTools.blockType.*;
import org.tools.Tools;

import java.lang.Object;


/**
 * @author Chaimaa & Issam
 */

public class BlocksConversion extends Tools{

	static String[] blocks = { "while", "if", "for", "do" }; // possible blocks

	// list of all the lines of code where the index of a line is the same as its Id
	ArrayList<Line> lines;

	// the actual code composed of ordered line ids
	ArrayList<Integer> code;

	//Struct to make front-end animation (Struct for blocks). 
	ArrayList<Blocks> BlockStruct;
	
	// Normal constructor
	public BlocksConversion(String[] sourceCode) throws BackEndException {
		this.lines = new ArrayList<Line>();
		this.code = new ArrayList<Integer>();
		this.BlockStruct = new ArrayList<Blocks>();
		preTreat(sourceCode);
	}
	
	//Constructor for DeepCopy
	public BlocksConversion(BlocksConversion blocksConversionOld){
		this.lines = blocksConversionOld.getLines();
		this.code = blocksConversionOld.getCode();
	}


	public int getNumberOfLines() {
		return code.size();
	}

	public int originalLineNumber(int lineNumber) throws IndexOutOfBoundsException {
		
		if (lineNumber >= this.getNumberOfLines()) {
			throw new IndexOutOfBoundsException();
		}

		int lineId = this.code.get(lineNumber);
		Line line = this.lines.get(lineId);
		return line.mapId;
	}

	public String getNewSourceCode() throws BackEndException {
		String result = "";

		for (int i = 0; i < this.lines.size(); i++) {
			Line l = this.lines.get(i);
			result = result + Integer.toString(i) + ") " + l.getLineCode(code) + "\n";
		}
		return result;
	}
	
	public String[] getNewSourceCodeAsArray() throws BackEndException {
		System.out.println("lines size getNewSourceCodeAsArray: " + code.size());
		String[] newSourceCode = new String[code.size()];
		
		for (int i = 0; i < this.code.size(); i++) {
			Line line = this.lines.get(code.get(i));
			newSourceCode[i] = line.getLineCode(code);
		}
	
		return newSourceCode;
	}
	
	public String[] getNewSourceCodeArray() throws BackEndException {
		System.out.println("lines size getNewSourceCodeArray: " +lines.size());
		String[] result = new String[this.lines.size()];

		for(int i=0; i<this.lines.size(); i++) {
			Line l = this.lines.get(i);
			result[i] = l.getLineCode(this.code);
		}
		return result;
	}

	/**
	 * 
	 * @param source Source code to pre-treat
	 * @throws BackEndException
	 */
	public void preTreat(String[] sourceCode) throws BackEndException {
		String block;

		this.lines.clear();
		this.code.clear();

		for (int i = 0; i < sourceCode.length; ++i) {
			this.lines.add(i, new LineString(i, sourceCode[i]));
			this.code.add(i, Integer.valueOf(i));
		}
		
		int jump = 0;
		
		block = getFirstBlockString();

		while (block != "none") {
			int line = getFirstBlockLine();
			switch (block) {
			case "while":
				jump = jump + preTreatWhile(line);
				break;
			case "if":
				jump = jump + preTreatIf(line);
				break;
			case "for":
				preTreatFor(line);
				break;
			case "do":
				jump = jump +preTreatDoWhile(line);
				break;
			}
			block = getFirstBlockString();
			
		}
		
		
		//Do usefull prints
		
		/*
		for (int i = 0; i < code.size(); ++i) {
			lines.get(i).showContent();
			System.out.println("Code : " + code.get(i));
		}
		*/
		
	}

	/**
	 * @return The number of the line containing the first occurrence of a block.
	 * @throws BackEndException
	 */
	private int getFirstBlockLine() throws BackEndException {
		String blockString;

		for (int i = 0; i < code.size(); ++i) {

			blockString = containBlock(lines.get(code.get(i)).getLineCode(code));
			if (blockString != "") {
				return i;
			}
		}
		return -1;
	}

	/**
	 * @return The first occurrence of block name if found, and "none" if not.
	 * @throws BackEndException
	 */
	private String getFirstBlockString() throws BackEndException {
		String blockString;

		for (int i = 0; i < code.size(); ++i) {

			blockString = containBlock(lines.get(i).getLineCode(code));
			if (blockString != "") {
				return blockString;
			}

		}
		return "none";
	}

	/**
	 * Method used to find the closing of a block.
	 * 
	 * @param start_line Line where the block begins.
	 * @return Line where the block ends.
	 * @throws BackEndException
	 */
	private Integer getBlockEnd(Integer start_line) throws BackEndException {

		Integer start = start_line;

		int p = 1;
		int i;
		for (i = start+1; i < lines.size() ;i++ ) {
			String line = lines.get(i).getLineCode(code);
			//System.out.println("La ligne est " + line);
			if (line.contains("}")) {
				p--;
				if(p == 0) {
					return i;
				}
			}
			if (line.contains("{"))
				p++;
		}
		return -1;
	}

	// if String line contains block, returns block type, else returns ""
	private static String containBlock(String line) {
		for (int i = 0; i < blocks.length; ++i) {
			if (line.contains(blocks[i])) {
				// TODO affiner la détection du bloque avant de conclure.
				if(lineReallyContainObject(line,blocks[i])) {
					return blocks[i];
				}
			}
		}
		return "";
	}

	private int preTreatWhile(int line) throws BackEndException {
		
		int whileLine = line;
		int closeLine = getBlockEnd(whileLine);
		
		BlockStruct.add(new Blocks("while",whileLine,closeLine));
		
		String whileLineString = lines.get(code.get(whileLine)).getLineCode(code);
		String cond = whileLineString.substring(whileLineString.indexOf('(') + 1, whileLineString.lastIndexOf(')'));
		
		int startToId = closeLine + 1 < code.size() ? code.get(closeLine + 1) : Line.ID_END;
		int closeToId = code.get(whileLine);
		
		ArrayList<String> conds = dividConds(cond);
		int nbConds = conds.size()-1;

		String notCond = "!(" + conds.get(0) + ")"; 
		lines.set(code.get(whileLine), new LineGoto(code.get(whileLine), notCond, startToId));

		for(int i = 1;i<=nbConds;i++) {
			notCond = "!(" + conds.get(i) + ")"; 
			//this.code.add(code.get(whileLine+i), code.get(whileLine+i));
			lines.add(code.get(whileLine+i), new LineGoto(code.get(whileLine+i),whileLine, notCond, startToId));
		}

		BlockStruct.add(new Blocks("While",startToId,closeToId+nbConds));
		lines.set(code.get(closeLine+nbConds), new LineGoto(code.get(closeLine+nbConds), "true", closeToId));

		return nbConds-1;
	}

	private int preTreatIf(int line) throws BackEndException {

		int ifLine = line;
	 
		//On regarde si il y a un else
		if(getBlockEnd(getBlockEnd(ifLine)) > 0) {
			int elseLine = getBlockEnd(ifLine);
			int closeLine = getBlockEnd(elseLine);
			int ifLineId = code.get(ifLine);
			int elseLineId = code.get(elseLine);

			BlockStruct.add(new Blocks("if",ifLine,elseLine));
			BlockStruct.add(new Blocks("else",elseLine,closeLine));
			
			String ifLineString = lines.get(ifLineId).getLineCode(code);
			String cond = ifLineString.substring(ifLineString.indexOf('(') + 1, ifLineString.lastIndexOf(')'));
			//String notCond = "!(" + cond + ")";
			
			//code.remove(closeLine);
			lines.set(closeLine, new LineString(closeLine, closeLine, ""));
			
			int ifToId = elseLine + 1 < code.size() ? code.get(elseLine + 1) : Line.ID_END;
			int elseToId = closeLine < code.size() ? code.get(closeLine) : Line.ID_END;
			
			ArrayList<String> conds = dividConds(cond);
			int nbConds = conds.size()-1;

			String notCond = "!(" + conds.get(0) + ")"; 
			lines.set(ifLineId, new LineGoto(ifLineId, notCond, ifToId));
			
			for(int i = 1;i<=nbConds;i++) {
				notCond = "!(" + conds.get(i) + ")"; 
				lines.add(code.get(ifLine+i), new LineGoto(code.get(ifLine+i), notCond, elseLine+nbConds));
			}
			lines.set(elseLineId+nbConds, new LineGoto(code.get(elseLine+nbConds), "true", elseLineId+nbConds+1));
			return nbConds-1;
		}
		//Sinon, c'est un if tous seul. 
		else {
			int closeLine = getBlockEnd(ifLine);
			int ifLineId = code.get(ifLine);

			BlockStruct.add(new Blocks("if",ifLine,closeLine));
			
			String ifLineString = lines.get(ifLineId).getLineCode(code);
			String cond = ifLineString.substring(ifLineString.indexOf('(') + 1, ifLineString.lastIndexOf(')'));
			
			lines.set(closeLine, new LineString(closeLine, closeLine, ""));
			
			int ifToId = closeLine + 1 < code.size() ? code.get(closeLine + 1) : Line.ID_END;
			
			ArrayList<String> conds = dividConds(cond);
			int nbConds = conds.size()-1;

			String notCond = "!(" + conds.get(0) + ")"; 
			lines.set(ifLineId, new LineGoto(ifLineId, notCond, ifToId));
			
			for(int i = 1;i<=nbConds;i++) {
				notCond = "!(" + conds.get(i) + ")"; 
				lines.add(code.get(ifLine+i), new LineGoto(code.get(ifLine+i), notCond, closeLine+nbConds));
			}
			return nbConds-1;
		}
		
	}

	private int preTreatDoWhile(int line) throws BackEndException {

		int startLine = line;
		int closeLine = getBlockEnd(startLine);

		BlockStruct.add(new Blocks("do",startLine,closeLine));
		
		String whileLineString = lines.get(code.get(closeLine)).getLineCode(code);
		String cond = whileLineString.substring(whileLineString.indexOf('(') + 1, whileLineString.lastIndexOf(')'));

		int startLineId = code.get(startLine);
		int closeLineId = code.get(closeLine);
		int afterDoId = code.get(startLine + 1);

		ArrayList<String> conds = dividConds(cond);
		int nbConds = conds.size()-1;
		
		String notCond = "!(" + conds.get(0) + ")"; 
		lines.set(closeLineId, new LineGoto(closeLineId, conds.get(0), startLineId));

		for(int i = 1;i<=nbConds;i++) {
			
			lines.add(closeLineId+i, new LineGoto(closeLineId+i,closeLine, conds.get(i), startLineId));
			
		}
		
		//code.remove(startLine);
		
		Line lineAfterDo = lines.get(afterDoId);
		lineAfterDo.id = startLineId;

		lines.set(startLineId, lineAfterDo);
		code.set(startLine, startLineId);
		return 0;
	}

	private void preTreatFor(int line) throws BackEndException {

		int startLine = line;
		int endLine = getBlockEnd(startLine);

		BlockStruct.add(new Blocks("for",startLine,endLine));
		
		int startLineId = code.get(startLine);
		int endLineId = code.get(endLine);
		int afterEndId = endLine + 1 < code.size() ? code.get(endLine + 1) : Line.ID_END;

		String forLineString = lines.get(code.get(startLine)).getLineCode(code);
		String innerFor = forLineString.substring(forLineString.indexOf('(') + 1, forLineString.lastIndexOf(')'));
		String[] forThings = innerFor.split(";");

		String i1 = forThings[0].strip();
		String cond = forThings[1].strip();
		String i2 = forThings[2].strip();
		String notCond = "!(" + cond + ")";

		Line inst1 = new LineString(startLineId, i1);
		Line startLineGoto = new LineGoto(lines.size(), notCond, afterEndId);
		Line inst2 = new LineString(lines.size() + 1, i2);
		Line endLineGoto = new LineGoto(endLineId, "true", startLineGoto.id);

		lines.add(startLineGoto);
		lines.add(inst2);
		lines.set(inst1.id, inst1);
		lines.set(endLineGoto.id, endLineGoto);
		
		code.add(startLine + 1, startLineGoto.id);
		code.add(endLine + 1, inst2.id);
		
		for (int i = 0; i < code.size(); ++i) {
			lines.get(i).showContent();
			System.out.println("Code : " + code.get(i));
		}



	}
	
	private ArrayList<String> dividConds(String cond) {
		
		// Divid conditions which are in the same line
		ArrayList<String> conds = new ArrayList<String>();
		
		String gluedConds = new String(cond);
		
		int indexEt;
		int indexOu;


			indexEt = gluedConds.indexOf("&&");
			if(indexEt==-1) {
				indexEt = gluedConds.length()-1;
			}
			
			indexOu = gluedConds.indexOf("||");
			if(indexOu==-1) {
				indexOu = gluedConds.length()-1;
			}
		
		//Cette condition est vérifié ssi indexOu=IndexEt=End. Soit on a attient la fin
		while(indexOu!=indexEt){
			
			if(indexOu>indexEt) {
				//System.out.print(gluedConds.substring(0,indexEt));
				conds.add(gluedConds.substring(0,indexEt));
				gluedConds = gluedConds.substring(indexEt+2,gluedConds.length());
				//System.out.print(gluedConds);
			}
			else {
				conds.add(gluedConds.substring(1,indexOu));
				gluedConds = gluedConds.substring(indexOu+2,gluedConds.length());
			}
			
			indexEt = gluedConds.indexOf("&&");
			if(indexEt==-1) {
				indexEt = gluedConds.length()-1;
			}
			
			indexOu = gluedConds.indexOf("||");
			if(indexOu==-1) {
				indexOu = gluedConds.length()-1;
			}
			
		}
		conds.add(gluedConds);
		return conds;
	}
	
	// -- Getters -- //
	
	public ArrayList<Line> getLines() {
		return  (ArrayList<Line>)lines.clone();
	}
	
	public ArrayList<Integer> getCode() {
		return (ArrayList<Integer>)code.clone();
	}
	
	public ArrayList<Blocks> getBlockStruct() {
		return this.BlockStruct;
	}

}
