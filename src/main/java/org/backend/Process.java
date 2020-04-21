package org.backend;


import java.util.ArrayList;
import java.nio.file.Paths;

import org.backend.exceptions.BackEndException;
import org.backend.exceptions.BadSourceCodeException;
import org.backend.exceptions.RipException;
import org.backend.varStorage.Variable;
import org.tools.Tools;
import org.objects.*;

import bsh.EvalError;
import bsh.Interpreter;

import bsh.BshClassManager;
import java.net.URL;



public class Process extends Tools{
	private static Variable[] sharedVars;
	private Variable[] localVars;
	private String[] sourceCode;
	private int currentLine;
	private Interpreter inter;
	private boolean done;
	private boolean crashed;
	private ArrayList<Integer> originalSourceLinesExecutedDuringLastStep;
	private PreTreatment preTreatment;

	//Normal builder
	public Process(int index,int numberOfProcesses, PreTreatment preTreatment) throws BackEndException {
		this.inter = new Interpreter();
		
		
		BshClassManager manag  = inter.getClassManager();
		
		//On ajoute le package Object à ClassPath pour qu'il puisse être importer: 
		String currentDir = System.getProperty("user.dir");
		String sourcecode = currentDir + "\\src\\main\\java\\org\\objects\\";
		try {
			//URL myURL = new URL(sourcecode);
			//System.out.println("Path to Objects:" +sourcecode);
			manag.addClassPath(Paths.get(sourcecode).toUri().toURL());
			manag.addClassPath(Paths.get(sourcecode + "\\mycommands\\").toUri().toURL());
			this.inter.source(sourcecode + "\\mycommands\\StandardFonctions.bsh");  //TRY TO IMPLEMENT SIMPLE FONCTIONS
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
		
		// Reserved variables index et numberOfProcesses
		try {
			this.inter.set("index", index);
		} catch (EvalError e1) {
			e1.printStackTrace();
			throw new RipException("EvalError when setting index variable (i).");
		}
		
		try {
			this.inter.set("nbProc", numberOfProcesses);
		} catch (EvalError e1) {
			e1.printStackTrace();
			customeAlertTool(e1.getMessage());
			throw new RipException("EvalError when setting index variable (NbProc).");
		}
		
		// Execution of the initialization block (imports, and variable declarations)
		try {
			this.inter.eval(preTreatment.getInitialisationBlock());
		} catch (EvalError e) {
			e.printStackTrace();
			customeAlertTool(e.getMessage());
			throw new BadSourceCodeException("Error in initialization.");
		}

		// We receive a copy of the array of local variables
		localVars = preTreatment.getLocalVars();
		sharedVars = preTreatment.getSharedVars();
		
		sourceCode = preTreatment.getPreTreatedSource();

		this.currentLine = preTreatment.getEndOfInitBlocks();
		this.done = false;
		this.crashed = false;
		originalSourceLinesExecutedDuringLastStep = new ArrayList<Integer>();
		
		this.preTreatment = preTreatment;
	}

	//Builder for deep copy
	public Process(Process processe) {
		
		this.setSharedVars(processe.getSharedVars().clone());
		this.localVars = processe.getLocalVars().clone();
		this.sourceCode = processe.getSourceCode().clone() ;
		this.currentLine = processe.getCurrentLine();
		this.inter = processe.getInter();
		this.done = processe.isDone();
		this.crashed = processe.isCrashed();
		this.originalSourceLinesExecutedDuringLastStep = (ArrayList<Integer>) processe.getOriginalSourceLinesExecutedDuringLastStep().clone();
		this.preTreatment = processe.getPreTreatment();
	}

	public void treatGoto() throws EvalError {
		// currentLine is a goto
		String instruction = this.sourceCode[this.currentLine];
		System.out.println("Treating following goto instruction: " + instruction);
		String condition = instruction.substring(6, instruction.indexOf(','));
		int targetLine = Integer
				.parseInt(instruction.substring(instruction.indexOf(',') + 1, instruction.length() - 2).trim());

		boolean test = (boolean) this.inter.eval(condition);

		if (test)
			this.currentLine = targetLine;
		else
			++this.currentLine;

	}

	// --------------  ICI on utilise l'interpréteur ------------------- //
	
	public void oneStep() throws EvalError {
		if (this.done) {
			return;
		}
		originalSourceLinesExecutedDuringLastStep.clear();
		originalSourceLinesExecutedDuringLastStep.add(preTreatment.getOriginalLineNumber(currentLine));

		// The shared variables in the interpreter (which might have been modified in an other process since) are 
		// updated from the array of shared variables.
		
		for (int i = 0; i < Process.sharedVars.length; ++i) {
				this.inter.set(Process.getSharedVars()[i].getName(), Process.getSharedVars()[i].getObj());
		}

		// One line is executed
		if (this.sourceCode[this.currentLine].indexOf("goto") >= 0) {
			this.treatGoto();
		} else {
			try {
			this.inter.eval(this.sourceCode[this.currentLine]);
			this.currentLine++;
			}catch (EvalError e) {
				customeAlertTool(e.getMessage());
			}
		}
		
		// After the step, the shared variables are updated so their value can be shared between the processes
		for (int i = 0; i < Process.sharedVars.length; ++i) {
			Process.getSharedVars()[i].update(this.inter.get(Process.getSharedVars()[i].getName()));
		}
		
		// Local variables are also updated for the GUI
		for (int i = 0; i < localVars.length; ++i) {
			localVars[i].update(this.inter.get(localVars[i].getName()));
		}
		
		if (this.currentLine >= this.sourceCode.length) {
			this.done = true;
		}

	}

	public static void setSharedVars(PreTreatment preTreatment) {
		Process.sharedVars = preTreatment.getSharedVars();
	}
	
	public static Variable[] getSharedVars() {
		return Process.sharedVars;
	}
	
	public Variable[] getLocalVars() {
		return localVars;
	}
	
	public Boolean isDone() {
		return done;
	}
	
	public boolean isCrashed() {
		return crashed;
	}
	
	public void crashProcess() {
		done = true;
		crashed = true;
	}
	
	public int getCurrentLine() {
		return currentLine;
	}
	
	public String[] getSourceCode() {
		return sourceCode;
	}
	
	public Interpreter getInter() {
		return inter;
	}
	
	private PreTreatment getPreTreatment() {
		return preTreatment;
	}
	
	public ArrayList<Integer> getOriginalSourceLinesExecutedDuringLastStep() {
		return originalSourceLinesExecutedDuringLastStep;
	}

	public void setSharedVars(Variable[] sharedVars) {
		this.sharedVars = sharedVars;
	}
}