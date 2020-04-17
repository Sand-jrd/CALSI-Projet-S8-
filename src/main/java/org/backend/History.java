package org.backend;

import java.util.ArrayList;
import org.backend.Simulation;


/**
 * This class will saved all the states simulation will come true. Hence, We will be able to StepBack and Save a simulation.
 * @author Sandrine Juillard
 *
 */
public class History {
	
	//private	ArrayList<ArrayList<Text>> textForProcess;
	int weAreHere;
	private ArrayList<Simulation> simulations;
	private ArrayList<int []> processline;
	
	//-- Historique vide
	public History() {
		//this.textForProcess = new ArrayList<ArrayList<Text>>();
		this.simulations = new ArrayList<Simulation>();
		this.processline = new  ArrayList<int []>();
		this.weAreHere=-1;
	}
	
	// -- Les addStep pour enregistrer les différente information qui se trouve un peu partout dans le code (ça sera optimisable plus tard pour la qualité du code)
	public void addStep(Simulation simulation,int [] processline) {
		this.simulations.add(new Simulation(simulation));
		
		this.processline.add(processline.clone());
		this.weAreHere++;
	}
	
	public Simulation getBackInTime(Simulation simulationvide) {
			//Simulation simulationsOld = simulationvide.getInfos().getSimulation();
			Simulation simulationsOld = this.simulations.get(weAreHere);
			this.simulations.remove( this.simulations.size() - 1 );
			return simulationsOld;
	}
	
	public int [] getBackInTime(int [] processlineVide) {
		if(this.weAreHere>=0) {
			System.out.println("Processline is set !");
			int [] processlineOld = this.processline.get(weAreHere);
			this.processline.remove( this.processline.size() - 1 );
			return processlineOld;
		}
		return processlineVide;
	}
	
	public void getBackInTime() {
		if(this.weAreHere>=0) {
			this.weAreHere--;
			System.out.println("Now we are : "+weAreHere);
		}
	}
}