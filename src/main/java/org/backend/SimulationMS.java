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
 * @author Sandrine Juillard
 *
 */
public class SimulationMS extends Simulation{
	
	private ArrayList<String> sourcesCodesFilesNames;
	private ArrayList<Integer> processPartition;
	private ArrayList<PreTreatment> preTreatments;
	private static int nbSciptActif = 1;
	
	public SimulationMS (SimulationBuilder simulationBuilder) throws BackEndException{
		super(simulationBuilder);
		
		genereateSourceFileNames();
		this.nbSciptActif = simulationBuilder.NumberOfSciptActif;
		this.processPartition = simulationBuilder.processPartition;
		this.preTreatments = new ArrayList<PreTreatment>(nbSciptActif);

		for (int i = 0; i < nbSciptActif; i++) {
			//Lecture de source.txt (Le où est enregistrer le fichier qu'on à ouvert)

			String sourceCode = readSourceCode(i);
			
			//Parcage du code
			this.preTreatments.add( new PreTreatment(sourceCode, processPartition.get(i)));
		}

		//Inisialisation des variables Shared et Local dans l'interpréteur
		this.preTreatment = this.preTreatments.get(0);
		Process.setSharedVars(this.preTreatment);

		//Choix du type de Scheduler (Randome, Step_By_Step ect..)
		scheduler = new Scheduler(this.getSchedulerType(),this.getSchedString());
		
	}

	@Override
	protected void initSimulation() throws BackEndException {


	}

	@Override
	protected void initProcesses() throws BackEndException {
		int procCreated = 0;
		processes = new Process[numberOfProcesses];
		for (int i = 0; i <= nbSciptActif; i++) {
			for (int ProcInScript = 0; ProcInScript <= processPartition.get(i); ProcInScript++) {
				processes[procCreated] = new Process(procCreated,numberOfProcesses, preTreatments.get(i));
				procCreated++;
			}
		}
	}

	
	protected String readSourceCode(int i) throws BackEndException {
		try {
			return Tools.getContentOfFile(sourcesCodesFilesNames.get(i));
		} catch (FileNotFoundException e) {
			//customeAlertTool("File specified for source code not found.");
			throw new BadSimulationParametersException("File specified for source code not found.");
		} catch (IOException e) {
			customeAlertTool("IO error while attempting to read from source code file \n your code might contain a mistake.");
			throw new RipException("IO error while attempting to read from source code file.");
		}
	}
	
	
	
	//Tools
	
	private void genereateSourceFileNames() {
		
		this.sourcesCodesFilesNames = new ArrayList<String>();
		
    	String currentDir = System.getProperty("user.dir");
    	this.sourcesCodesFilesNames.add(currentDir + "\\src\\main\\resources\\org\\Algorithmes\\source.txt");
		
		for (int i = 1; i <= 4; i++) {
			this.sourcesCodesFilesNames.add(currentDir + "\\src\\main\\resources\\org\\Algorithmes\\source"+i+".txt" );
		}
	}
	
}