package org.backend;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.util.ArrayList;

import org.backend.exceptions.*;
import org.tools.Tools;
import bsh.EvalError;


/**
 * Manager class for the simulation.
 * @author Hugo
 *
 */
public class Simulation extends Tools{
	
	//Infos ça correspond contient la simu à l'étape d'avant.
	private Infos infos;  
	
	// Simulation initialization parameters 
	
	//Same as simulationBuilder
	private String sourceCodeFileName;
	private int numberOfProcesses;
	private String schedulerType;
	
	//Les paramètres de la simulation 
	private Process processes[];
	private Scheduler scheduler; // Dans un Scheduler y'a une simu, dans une simu y'a un scheduler... mdr
	private PreTreatment preTreatment;
	private ArrayList<Integer> executionOrderHistory;

	//Builder classique
	public Simulation(SimulationBuilder simulationBuilder) throws BackEndException {
		
		this.sourceCodeFileName = simulationBuilder.sourceCodeFileName;
		this.numberOfProcesses = simulationBuilder.numberOfProcesses;
		this.schedulerType = simulationBuilder.schedulerType;

		this.executionOrderHistory = new ArrayList<Integer>();

		initSimulation();

		// infos gets a reference to simulation being created
		this.infos = new Infos(this);
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
	
	//Builder Quand on fait une copie d'une autre avec des info (Ou on ne update pas Info du coup !) (pour History)
		public Simulation(Infos infosOld,Simulation simulationLinked){

			Simulation simulationOld = infosOld.getSimulation();
			
			// Simulation initialization parameters
			//this.infos = new Infos(simulationOld.getInfos());

			this.sourceCodeFileName = simulationLinked.getSourceCodeFileName();
			this.numberOfProcesses = simulationLinked.getNumberOfProcesses();
			this.schedulerType = simulationLinked.getSchedulerType();


			this.processes = simulationLinked.getProcesses();
			
			
			this.scheduler = simulationLinked.getScheduler();
			this.preTreatment = simulationLinked.getPreTreatment();
			this.executionOrderHistory = simulationLinked.getExecutionOrderHistory();
			
			
		}
	
	//Pour changer de Scheduler mais c'est pas encore implémenté. 
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
		System.out.println("Next Step !"+i);

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
		System.out.println("On a ajouté "+processId);
	}

	private void initSimulation() throws BackEndException {
		
		//Lecture de source.txt (Le où est enregistrer le fichier qu'on à ouvert)
		String sourceCode = readSourceCode();
		
		//Parcage du code
		this.preTreatment = new PreTreatment(sourceCode);

		//Inisialisation des variables Shared et Local dans l'interpréteur
		Process.setSharedVars(preTreatment);

		//Choix du type de Scheduler (Randome, Step_By_Step ect..)
		scheduler = new Scheduler(this);

		//La fonction juste en dessous
		initProcesses();
	}

	private void initProcesses() throws BackEndException {
		processes = new Process[numberOfProcesses];
		for (int i = 0; i < numberOfProcesses; i++) {
			processes[i] = new Process(i,numberOfProcesses, preTreatment);
		}
	}

	private String readSourceCode() throws BackEndException {
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

	// -- Getters -- //
	
	public Infos getInfos() {
		return this.infos;
	}
	
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
	
	public PreTreatment getPreTreatment() {
		return preTreatment;
	}
	
	public ArrayList<Integer> getExecutionOrderHistory() {
		return executionOrderHistory;
	}

	public void setInfos(Infos oldInfos) {
		this.infos = oldInfos;
	}

	public void setProcess(Process[] processes) {
		this.processes = processes;
		
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