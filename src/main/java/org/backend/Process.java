package org.backend;

import java.util.ArrayList;

import bsh.EvalError;
import bsh.Interpreter;

public class Process {
	private static Variable[] sharedVars;
	private Variable[] localVars;
	private String[] sourceCode;
	private int currentLine;
	private Interpreter inter;
	private boolean done;
	private boolean crashed;
	private ArrayList<Integer> originalSourceLinesExecutedDuringLastStep;
	private PreTreatment preTreatment;

	public Process(int index, PreTreatment preTreatment) throws BackEndException {
		this.inter = new Interpreter();

		// Reserved variables
		try {
			this.inter.set("i", index);
		} catch (EvalError e1) {
			e1.printStackTrace();
			throw new RipException("EvalError when setting index variable (i).");
		}
		
		// Execution of the initialization block (imports, and variable declarations)
		try {
			this.inter.eval(preTreatment.getInitialisationBlock());
		} catch (EvalError e) {
			e.printStackTrace();
			throw new BadSourceCodeException("Error in initialization.");
		}

		// We receive a copy of the array of local variables
		localVars = preTreatment.getLocalVars();
		
		sourceCode = preTreatment.getPreTreatedSource();

		this.currentLine = preTreatment.getEndOfInitBlocks();
		this.done = false;
		this.crashed = false;
		originalSourceLinesExecutedDuringLastStep = new ArrayList<Integer>();
		this.preTreatment = preTreatment;
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

	public void oneStep() throws EvalError {
		if (this.done)
			return;
		
		originalSourceLinesExecutedDuringLastStep.clear();
		originalSourceLinesExecutedDuringLastStep.add(preTreatment.getOriginalLineNumber(currentLine));

		// The shared variables in the interpreter (which might have been modified in an other process since) are 
		// updated from the array of shared variables.
		for (int i = 0; i < Process.sharedVars.length; ++i) {
			this.inter.set(Process.sharedVars[i].getName(), Process.sharedVars[i].getObj());
		}

		// System.out.println(this.sourceCode[this.currentLine]);

		// One line is executed
		if (this.sourceCode[this.currentLine].indexOf("goto") >= 0) {
			this.treatGoto();
		} else {
			this.inter.eval(this.sourceCode[this.currentLine]);
			this.currentLine++;
		}

		// After the step, the shared variables are updated so their value can be shared between the processes
		for (int i = 0; i < Process.sharedVars.length; ++i) {
			Process.sharedVars[i].update(this.inter.get(Process.sharedVars[i].getName()));
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
	
	public ArrayList<Integer> getOriginalSourceLinesExecutedDuringLastStep() {
		return originalSourceLinesExecutedDuringLastStep;
	}
}