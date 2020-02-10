package org.backend;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.tools.Tools;
import bsh.EvalError;

/**
 * Manager class for the simulation.
 * @author Hugo
 *
 */
public class Simulation {
	private Infos infos;

	// Simulation initialization parameters
	private String sourceCodeFileName;
	private int numberOfProcesses;
	private String schedulerType;

	private Process processes[];
	private Scheduler scheduler;
	private PreTreatment preTreatment;
	private ArrayList<Integer> executionOrderHistory;

	public Simulation(SimulationBuilder simulationBuilder) throws BackEndException {
		
		this.sourceCodeFileName = simulationBuilder.sourceCodeFileName;
		this.numberOfProcesses = simulationBuilder.numberOfProcesses;
		this.schedulerType = simulationBuilder.schedulerType;

		this.executionOrderHistory = new ArrayList<Integer>();

		initSimulation();

		// infos gets a reference to simulation being created
		this.infos = new Infos(this);
	}

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
		int i = scheduler.getNext();
		nextStep(i);
	}

	/**
	 * Execute one step of the specified process.
	 * 
	 * @param processId the id of the specified process
	 * @throws BadSourceCodeException if there is a problem with the source code
	 *                                executed during that step
	 */
	public void nextStep(int processId) throws BadSourceCodeException {
		try {
			processes[processId].oneStep();
		} catch (EvalError e) {
			e.printStackTrace();
			throw new BadSourceCodeException("EvalError when executing next step ");
		}
		executionOrderHistory.add(processId);
	}
	
	public void stepBack() throws BadSourceCodeException {
		int i = scheduler.getStepBack();
		nextStep(i);
	}

	/**
	 * Get an Infos instance, which has several method giving various information on
	 * the current and past state of the simulation. Mainly for use by the GUI/front
	 * end, and intended as the only interface between the front end and back end
	 * data.
	 * 
	 * @return the infos
	 */
	public Infos getInfos() {
		return infos;
	}

	private void initSimulation() throws BackEndException {
		
		//Lecture de source.txt (Le o� est enregistrer le fichier qu'on � ouvert)
		String sourceCode = readSourceCode();
		
		//Parcage du code
		this.preTreatment = new PreTreatment(sourceCode);

		//Copie des variables Shared et Local � prioris mais c'est un peu mysterieux.
		Process.setSharedVars(preTreatment);

		//Choix du type de Scheduler (Randome, Step_By_Step ect..)
		scheduler = new Scheduler(this);

		//La fonction juste en dessous
		initProcesses();
	}

	private void initProcesses() throws BackEndException {
		processes = new Process[numberOfProcesses];
		for (int i = 0; i < numberOfProcesses; i++) {
			//On re traite chaque process d'un step.
			processes[i] = new Process(i, preTreatment);
		}
	}

	private String readSourceCode() throws BackEndException {
		try {
			return Tools.getContentOfFile(sourceCodeFileName);
		} catch (FileNotFoundException e) {
			throw new BadSimulationParametersException("File specified for source code not found.");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RipException("IO error while attempting to read from source code file.");
		}
	}

	public ArrayList<Integer> getExecutionOrderHistory() {
		return executionOrderHistory;
	}

	public Process[] getProcesses() {
		return processes;
	}
	
	public PreTreatment getPreTreatment() {
		return preTreatment;
	}

}