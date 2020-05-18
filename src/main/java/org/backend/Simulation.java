package org.backend;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import org.backend.parceTools.blockType.Blocks;
import org.backend.varStorage.Variable;
import org.backend.exceptions.*;
import org.tools.Tools;
import bsh.EvalError;


/**
 * Manager class for the simulation.
 * @author Hugo
 *
 */
public class Simulation extends Tools{

	// Simulation initialization parameters 

	//Same as simulationBuilder
	protected String sourceCodeFileName;
	protected int numberOfProcesses;
	protected String schedulerType;
	protected String SchedString;

	//Les param�tres de la simulation
	protected Process processes[];
	protected Scheduler scheduler;
	protected PreTreatment preTreatment;
	protected ArrayList<Integer> executionOrderHistory;

	//Builder classique
	public Simulation(SimulationBuilder simulationBuilder) throws BackEndException {

		this.sourceCodeFileName = simulationBuilder.sourceCodeFileName;
		this.numberOfProcesses = simulationBuilder.numberOfProcesses;
		this.schedulerType = simulationBuilder.schedulerType;
		this.SchedString = simulationBuilder.SchedString;

		this.executionOrderHistory = new ArrayList<Integer>();

		initSimulation();
	}

	//Builder Quand on fait une copie d'une autre simu (pour History)
	public Simulation(Simulation simulationOld){

		//this.infos = new Infos(simulationOld.getInfos());

		// Simulation initialization parameters
		this.sourceCodeFileName = new String(simulationOld.getSourceCodeFileName());
		this.numberOfProcesses = simulationOld.getNumberOfProcesses();
		this.schedulerType = simulationOld.getSchedulerType();

		this.processes = new Process[numberOfProcesses];
		for(int i = 0;i<numberOfProcesses;i++) {
			this.processes[i] = new Process(simulationOld.getProcesse(i));
		}

		this.scheduler = simulationOld.getScheduler();
		this.preTreatment = new PreTreatment(simulationOld.getPreTreatment());
		this.executionOrderHistory = (ArrayList<Integer>)((simulationOld.getExecutionOrderHistory()).clone());

	}

	//Pour changer de Scheduler mais c'est pas encore impl�ment�.
	public void changeScheduler(String newSchedulerType) {
		// TODO
	}


	/**
	 * Crashes specified process
	 * @param processId of the process to crash
	 * @throws RipException if the specified process does not exist
	 */
	public void crashProcess(int processId) throws RipException {
		if (processId >= processes.length) {
			throw new RipException("Can't crash process " + processId + " as it does not exist.");
		}
		processes[processId].crashProcess();
	}

	/**
	 * Execute one step of one process, chosen by the scheduler.
	 *
	 * @throws BadSourceCodeException if there is a problem with the source code
	 *                                executed during that step
	 */
	public void nextStep() throws BadSourceCodeException {
		try {
			int i = scheduler.getNext();
			nextStep(i);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void nextStepAtomique() throws BadSourceCodeException {
		int i;
		
		try {
			
			i = scheduler.getNext();
			// La valeur de retour de nextStep est vrai si l'op�ration est atomique ou si le processus est terminer. Donc, tant que l'op�ration n'est pas atomique, on est bloquer dans le while.
			while(!nextStep(i)) {
				System.out.println("C'est pas atomique, on continu");
			}
			
		}catch(SchedulerException e){
			e.printStackTrace();
			customeAlertTool("Error with the Sheduler, check your shed file");
		}
	
	}


	/**
	 * Execute one step of the specified process.
	 *
	 * @param processId the id of the specified process
	 * @throws BadSourceCodeException if there is a problem with the source code
	 *                                executed during that step
	 */
	public boolean nextStep(int processId) throws BadSourceCodeException {
		boolean res = true;
		try {
			res = processes[processId].oneStep();
		} catch (EvalError e) {
			e.printStackTrace();
			customeAlertTool(e.getMessage());
			throw new BadSourceCodeException("EvalError when executing next step ");
		}
		executionOrderHistory.add(processId);
		System.out.println("Le processus " + processId + "avance d'une ligne.");
		return res;

	}

	protected void initSimulation() throws BackEndException {

		//Lecture de source.txt (Le o� est enregistrer le fichier qu'on � ouvert)
		String sourceCode = readSourceCode();

		//Parcage du code
		this.preTreatment = new PreTreatment(sourceCode, numberOfProcesses);

		//Inisialisation des variables Shared et Local dans l'interpr�teur
		Process.setSharedVars(preTreatment);

		//Choix du type de Scheduler (Randome, Step_By_Step ect..)
		scheduler = new Scheduler(this);

		//La fonction juste en dessous
		initProcesses();
	}

	protected void initProcesses() throws BackEndException {
		processes = new Process[numberOfProcesses];
		for (int i = 0; i < numberOfProcesses; i++) {
			processes[i] = new Process(i,numberOfProcesses, preTreatment);
		}
	}

	protected String readSourceCode() throws BackEndException {
		try {
			return Tools.getContentOfFile(sourceCodeFileName);
		} catch (FileNotFoundException e) {
			//customeAlertTool("File specified for source code not found.");
			throw new BadSimulationParametersException("File specified for source code not found.");
		} catch (IOException e) {
			customeAlertTool("IO error while attempting to read from source code file \n your code might contain a mistake.");
			throw new RipException("IO error while attempting to read from source code file.");
		}
	}

	/**
	 * Get the id of the last process that was executed.
	 *
	 * @return an int corresponding to the id of the last executed process
	 */
	public int getIdOfLastExecutedProcess() {
		try{
			ArrayList<Integer> executionOrderHistory = this.getExecutionOrderHistory();
			return executionOrderHistory.get(executionOrderHistory.size() - 1);
		}catch(Exception e) {
			customeAlertTool("There is no previous step ! ");
			return -1;
		}
	}

	/**
	 * Get the line of the last executed pre-treated line of code. For debugging
	 * purposes, not intended to be displayed to the user.
	 *
	 * @return a string of the executed line
	 */
	public String getExecutedPreTreatedLine() {
		Process executedProcess = this.getProcesses()[getIdOfLastExecutedProcess()];
		String sourceCode[] = executedProcess.getSourceCode();
		return sourceCode[executedProcess.getCurrentLine() - 1];
	}

	/**
	 * Get info about the shared variables.
	 *
	 * @return infos about the shared variables.
	 */
	public Variable[] getSharedVariables() {

		return Process.getSharedVars();

	}

	/**
	 * Get infos about the local variables of the specified process
	 * @param processId the id of the specified process
	 * @return infos about the local variables
	 * @throws RipException
	 */
	public Variable[] getLocalVariables(int processId) throws RipException {
		return getProcess(processId).getLocalVars();
	}

	// Accessible from package only
	void updateInfos() {
		// TODO
	}

	/**
	 * Check whether the simulation is finished (ie that all processes are done)
	 *
	 * @return true if done, false if not
	 */
	public boolean simulationIsDone() {
		System.out.print("simulationIsDone function \n");
		Process processes[] = this.getProcesses();
		for (int i = 0; i < processes.length; i++) {
			if (!processes[i].isDone()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Check if the specified process has terminated.
	 * @param processId the id of the process to be checked for termination
	 * @return true if terminated, false otherwise
	 * @throws RipException if the process id specified does not correspond to a process
	 */
	public boolean processIsDone(int processId) throws RipException {
		return getProcess(processId).isDone();
	}

	/**
	 * Check if the specified process has crashed.
	 * @param processId the id of the process to be checked for termination
	 * @return true if terminated, false otherwise
	 * @throws RipException if the process id specified does not correspond to a process
	 */
	public boolean processIsCrashed(int processId) throws RipException {
		return getProcess(processId).isCrashed();
	}

	/**
	 * Get a list of original source code line numbers, corresponding to the lines executed
	 * during the last step of the specified process
	 * @param processId of the process in question
	 * @return an list of the original source code line numbers, corresponding to the lines executed
	 * during the last step of the specified process
	 * @throws RipException if the process id specified does not correspond to a process
	 */
	public ArrayList<Integer> getOriginalSourceLinesExecutedDuringLastStep(int processId) throws RipException {
		Process process = getProcess(processId);
		return process.getOriginalSourceLinesExecutedDuringLastStep();
	}

	public String getNewSourceCode() throws BackEndException {
		PreTreatment preTreatment = this.getPreTreatment();
		return preTreatment.getNewSourceCode();
	}

	public Process getProcess(int processId) throws RipException {
		Process processes[] = this.getProcesses();
		if (processId >= processes.length) {
			throw new RipException("Can't get termination status for process " + processId + " as it does not exist.");
		}
		return processes[processId];
	}

	// -- Getters -- //

	public String getSourceCodeFileName() {
		return sourceCodeFileName;
	}

	public int  getNumberOfProcesses() {
		return numberOfProcesses;
	}

	public String  getSchedulerType() {
		return schedulerType;
	}

	public Process[] getProcesses() {
		return processes;
	}

	public Process getProcesse(int i) {
		return processes[i];
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public String getSchedString() {
		return SchedString;
	}

	public PreTreatment getPreTreatment() {
		return preTreatment;
	}

	public ArrayList<Integer> getExecutionOrderHistory() {
		return executionOrderHistory;
	}

	public void setProcess(Process[] processes) {
		this.processes = processes;

	}

	public ArrayList<Blocks> getBlockStruct() {
		return this.preTreatment.getBlockStruct();
	}

	/**
	 * Get an Infos instance, which has several method giving various information on
	 * the current and past state of the simulation. Mainly for use by the GUI/front
	 * end, and intended as the only interface between the front end and back end
	 * data.
	 *
	 * @return the infos
	 */


}
