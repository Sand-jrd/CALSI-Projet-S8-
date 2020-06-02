package org.backend.parceTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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
		return lines.size();
	}

	public int originalLineNumber(int lineNumber) throws IndexOutOfBoundsException {
		
		if (lineNumber >= this.getNumberOfLines()) {
			throw new IndexOutOfBoundsException();
		}

		Line line = this.lines.get(lineNumber);
		return line.mapId;
	}

	public String getNewSourceCode() throws BackEndException {
		String result = "";

		for (int i = 0; i < this.lines.size(); i++) {
			Line l = this.lines.get(i);
			result = result + Integer.toString(i) + ") " + l.getLineCode(lines) + "\n";
		}
		return result;
	}
	
	public String[] getNewSourceCodeArray() throws BackEndException {
		System.out.println("lines size getNewSourceCodeArray: " +lines.size());
		String[] result = new String[this.lines.size()];

		for(int i=0; i<this.lines.size(); i++) {
			Line l = this.lines.get(i);
			result[i] = l.getLineCodeNoComment(lines);
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
			this.lines.add(i,new LineString(i,i, sourceCode[i]));
			this.code.add(i, Integer.valueOf(i));
		}
		
		int jump = 0;
		
		// Return the first block it find
		block = getFirstBlockString();

		while (block != "none") { 
			
			// Treat the block
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
			restorTrueParceCodeId();
			block = getFirstBlockString();

			//Continu 
		}
		
		restorTrueParceCodeId();
		//Do usefull prints
		/*
		for (int i = 0; i < lines.size(); ++i) {
			lines.get(i).showContent();
		}
		*/
	}

	/**
	 * @return The number of the line containing the first occurrence of a block.
	 * @throws BackEndException
	 */
	private int getFirstBlockLine() throws BackEndException {
		String blockString;

		for (int i = 0; i < lines.size(); ++i) {

			blockString = containBlock(lines.get(i).getLineCodeNoComment(lines));
			if (blockString != "") {
				return lines.get(i).getId();
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

		for (int i = 0; i < lines.size(); ++i) {

			blockString = containBlock(lines.get(i).getLineCodeNoComment(lines));
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
			String line = lines.get(i).getLineCode(lines);
			System.out.println("Id("+ i +") La ligne est :" + line + "et p = "+p);
			if (line.contains("}")) {
				p--;
				System.out.println("La ligne } trouvé, p= "+p);
				if(p == 0) {
					return i;
				}
			}
			else if (line.contains("{")) {
				System.out.println("La ligne { trouvé p= " + p);
				p++;
			}
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
		int whileLineTrue = lines.get(whileLine).getMapId();
		int closeLineTrue = lines.get(closeLine).getMapId();
		

		
		String whileLineString = lines.get(whileLine).getLineCode(lines);
		String cond = whileLineString.substring(whileLineString.indexOf('(') + 1, whileLineString.lastIndexOf(')'));
		
		ArrayList<String> conds = dividConds(cond);
		int nbConds = conds.size()-1;

		if(nbConds> 1) {
			System.out.println("Conds "+nbConds+": " + conds.get(nbConds));
			for(int i = 0;i<=nbConds-1;i++) {
				lines.add(whileLine+i, new LineString(whileLine+i,whileLineTrue, conds.get(i), "While"));
			}
		}
		System.out.println("Goto cond"+nbConds+": " + conds.get(nbConds));
		String notCond = "!(" + conds.get(nbConds) + ")"; 
		lines.set(whileLine+nbConds, new LineGoto(whileLine+nbConds,whileLineTrue, notCond,closeLineTrue+1 , "While"));
		
		lines.set(closeLine+nbConds, new LineGoto(closeLine+nbConds,closeLineTrue, "true", whileLineTrue,"End of while"));
		
		BlockStruct.add(new Blocks("while",whileLineTrue,closeLineTrue));
		
		return nbConds-1;
	}

	private int preTreatIf(int line) throws BackEndException {

		int ifLine = line;
	 
		//On regarde si il y a un else
		if(getBlockEnd(getBlockEnd(ifLine)) > 0) {
			
			int elseLine = getBlockEnd(ifLine);
			int closeLine = getBlockEnd(elseLine);
			int ifLineTrue = lines.get(ifLine).getMapId();
			int elseLineTrue = lines.get(elseLine).getMapId();
			int closeLineTrue = lines.get(closeLine).getMapId();
			
			String ifLineString = lines.get(ifLine).getLineCode(lines);
			String cond = ifLineString.substring(ifLineString.indexOf('(') + 1, ifLineString.lastIndexOf(')'));
			//String notCond = "!(" + cond + ")";
			
			//code.remove(closeLine);
			lines.set(closeLine, new LineString(closeLine, closeLineTrue,"","End of else"));
			
			ArrayList<String> conds = dividConds(cond);
			int nbConds = conds.size()-1;
			
			
			if(nbConds> 1) {
				for(int i = 0;i<=nbConds-1;i++) {
					lines.add(ifLine+i, new LineString(ifLine+i,ifLineTrue, conds.get(i), "if"));
				}
			}
			String notCond = "!(" + conds.get(nbConds) + ")"; 
			lines.set(ifLine+nbConds, new LineGoto(ifLine+nbConds,ifLineTrue, notCond,elseLineTrue , "if"));
			
			lines.set(elseLine+nbConds, new LineGoto(elseLine+nbConds,elseLineTrue, "true", elseLineTrue+1,"Else"));
			
			BlockStruct.add(new Blocks("if",ifLineTrue,elseLineTrue));
			BlockStruct.add(new Blocks("else",elseLineTrue,closeLineTrue));
			
			return nbConds-1;
		}
		//Sinon, c'est un if tous seul. 
		else {
			int closeLine = getBlockEnd(ifLine);
			int ifLineTrue = lines.get(ifLine).getMapId();
			int closeLineTrue = lines.get(closeLine).getMapId();
			
			String ifLineString = lines.get(ifLine).getLineCode(lines);
			String cond = ifLineString.substring(ifLineString.indexOf('(') + 1, ifLineString.lastIndexOf(')'));
			
			lines.add(closeLine, new LineGoto(closeLine,closeLineTrue, "true", closeLineTrue+1));
			
			ArrayList<String> conds = dividConds(cond);
			int nbConds = conds.size()-1;

			String notCond = "!(" + conds.get(0) + ")"; 
			lines.set(ifLine, new LineGoto(ifLine,ifLineTrue, notCond, closeLineTrue+1));
			
			for(int i = 1;i<=nbConds;i++) {
				notCond = "!(" + conds.get(i) + ")"; 
				lines.add(ifLine+i, new LineGoto(ifLine+i,ifLineTrue, notCond, closeLineTrue+1));
			}
			BlockStruct.add(new Blocks("if",ifLineTrue,closeLineTrue));
			return nbConds-1;
			
		}
		
	}

	private int preTreatDoWhile(int line) throws BackEndException {

		int startLine = line;
		int closeLine = getBlockEnd(startLine);
		int startLineTrue = lines.get(startLine).getMapId();
		int closeLineTrue = lines.get(closeLine).getMapId();

		BlockStruct.add(new Blocks("do",startLineTrue,closeLineTrue));
		
		lines.set(startLine, new LineGoto(startLine,startLineTrue, "true", startLineTrue+1,"Do start line"));
		
		String whileLineString = lines.get(closeLine).getLineCode(lines);
		String cond = whileLineString.substring(whileLineString.indexOf('(') + 1, whileLineString.lastIndexOf(')'));
		
		ArrayList<String> conds = dividConds(cond);
		int nbConds = conds.size()-1;
		
		String notCond = "!(" + conds.get(0) + ")"; 
		lines.set(closeLine, new LineGoto(closeLine,closeLineTrue, conds.get(0), startLineTrue,"Dowhile condition"));
		for(int i = 1;i<=nbConds;i++) {
			
			lines.add(closeLine+i, new LineGoto(closeLine+i,closeLineTrue, conds.get(i), startLineTrue,"Dowhile condition"));
			
		}
		

		
		return 0;
	}

	private void preTreatFor(int line) throws BackEndException {

		int startLine = line;
		int endLine = getBlockEnd(startLine);
		int startLineTrue = lines.get(startLine).getMapId();
		int endLineTrue = lines.get(endLine).getMapId();

		String forLineString = lines.get(startLine).getLineCode(lines);
		String innerFor = forLineString.substring(forLineString.indexOf('(') + 1, forLineString.lastIndexOf(')'));
		String[] forThings = innerFor.split(";");

		String i1 = forThings[0].strip();
		String cond = forThings[1].strip();
		String i2 = forThings[2].strip();
		String notCond = "!(" + cond + ")";

		lines.set(startLine, new LineString(startLine,startLineTrue, i1+";","for init"));
		lines.add(startLine+1, new LineGoto(startLine+1,startLineTrue, notCond, endLineTrue+1,"for condition"));
		lines.add(endLine+1, new LineString(endLine+1,endLineTrue, i2+";","for increntation"));
		lines.set(endLine+2, new LineGoto(endLine+2,endLineTrue, "FOR", startLineTrue,"for end"));
		
		BlockStruct.add(new Blocks("for",startLineTrue,endLineTrue));

	}
	
	private ArrayList<String> dividConds(String cond) {
		
		// Divid conditions which are in the same line
		ArrayList<String> conds = new ArrayList<String>();
		
		String gluedConds = new String(cond);
		
		int indexEt;
		int indexOu;
		int condCount = 0;


			indexEt = gluedConds.indexOf("&&");
			if(indexEt==-1) {
				indexEt = gluedConds.length();
			}
			
			indexOu = gluedConds.indexOf("||");
			if(indexOu==-1) {
				indexOu = gluedConds.length();
			}
		
		//Cette condition est vérifié ssi indexOu=IndexEt=End. Soit on a attient la fin
		while(indexOu!=indexEt){
			
			if(indexOu>indexEt) {
				
				conds.add("cond"+condCount+"=("+gluedConds.substring(0,indexEt)+");");
				gluedConds = gluedConds.substring(indexEt+2,gluedConds.length());
				condCount++;
			
			}
			else {
				
				conds.add("cond"+condCount+"=("+gluedConds.substring(1,indexOu)+");");
				gluedConds = gluedConds.substring(indexOu+2,gluedConds.length());
				condCount++;
			}
			
			indexEt = gluedConds.indexOf("&&");
			if(indexEt==-1) {
				indexEt = gluedConds.length();
			}
			
			indexOu = gluedConds.indexOf("||");
			if(indexOu==-1) {
				indexOu = gluedConds.length();
			}
			
		}
		
		conds.add("cond"+condCount+"=("+gluedConds+")");
		
		if(conds.size()>1) {
			condCount = 1;
			String lineConds = "";
			System.out.println("First cond : " + conds.get(0));
			if(cond.charAt(0) == '(') {
				lineConds = lineConds + "(";
				lineConds = lineConds +  "cond0";
			}
			else {
				lineConds = lineConds +  "cond0";
			}

			Integer[] nextchar = {0,0,0,0};
			
			nextchar[0] = cond.indexOf("&") > 0 ? cond.indexOf("&") : cond.length();
			nextchar[1] = cond.indexOf("|") > 0 ? cond.indexOf("|") : cond.length();
			nextchar[2] = cond.indexOf("(") > 0 ? cond.indexOf("(") : cond.length();
			nextchar[3] = cond.indexOf(")") > 0 ? cond.indexOf(")") : cond.length();
			
			int i = Collections.min(Arrays.asList(nextchar));
			System.out.println("start at i "+i+"till length "+cond.length());
			while(i<cond.length() && i!=-1) {
				System.out.println("cond stat: " + cond);
				if(cond.charAt(i) == '&') {
					lineConds = lineConds + " && ";
					i = i+2;
				}
				if(cond.charAt(i) == '|') {
					lineConds = lineConds + " || ";
					i = i+2;
				}
				if(cond.charAt(i) == '(') {
					lineConds = lineConds + "(";
					i++;
				}
				if(cond.charAt(i) == ')') {
					lineConds = lineConds + ")";
					i++;
				}
				else {
					lineConds = lineConds + "cond"+condCount;
					condCount++;
					String subs = cond.substring(i);
					nextchar[0] = subs.indexOf("&") > 0 ? subs.indexOf("&") : cond.length();
					nextchar[1] = subs.indexOf("|") > 0 ? subs.indexOf("|") : cond.length();
					nextchar[2] = subs.indexOf("(") > 0 ? subs.indexOf("(") : cond.length();
					nextchar[3] = subs.indexOf(")") > 0 ? subs.indexOf(")") : cond.length();
					int min = Collections.min(Arrays.asList(nextchar));
					if(min == -1) {
						break;
					}
					else {
						i = i + min;
					}
				}
			}
			conds.add(lineConds);
			
		}
		else {
			conds.set(0, cond);
		}
		return conds;

	}
	
	void restorTrueParceCodeId() {
		
		for (int i = 0; i < lines.size(); ++i) {
			lines.get(i).setId(i);
		}
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
