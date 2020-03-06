package org.backend;

import java.util.ArrayList;
import org.backend.Infos;
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
	private ArrayList<Infos> infoss;
	private ArrayList<int []> processline;
	
	//-- Historique vide
	public History() {
		//this.textForProcess = new ArrayList<ArrayList<Text>>();
		this.simulations = new ArrayList<Simulation>();
		this.infoss = new  ArrayList<Infos>();
		this.processline = new  ArrayList<int []>();
		this.weAreHere=-1;
	}
	
	// -- Les addStep pour enregistrer les différente information qui se trouve un peu partout dans le code (ça sera optimisable plus tard pour la qualité du code)
	public void addStep(Infos infos,Simulation simulation,int [] processline) {
		this.simulations.add(new Simulation(simulation));
		this.infoss.add(new Infos(infos,this.simulations.get(weAreHere+1)));
		this.simulations.get(weAreHere+1).setInfos(this.infoss.get(weAreHere+1));
		
		System.out.println("Check if Sim/Info are linked : "+(this.simulations.get(weAreHere+1).getInfos())+" == "+this.infoss.get(weAreHere+1));
		System.out.println(this.simulations.get(weAreHere+1).getProcesse(1));
		System.out.println(this.infoss.get(weAreHere+1).getSimulation().getProcesse(1));
		
		this.processline.add(processline.clone());
		this.weAreHere++;
	}
	
	public Simulation getBackInTime(Simulation simulationvide) {
			//Simulation simulationsOld = simulationvide.getInfos().getSimulation();
			Simulation simulationsOld = this.simulations.get(weAreHere);
			this.simulations.remove( this.simulations.size() - 1 );
			return simulationsOld;
	}
	
	public Infos getBackInTime(Infos infosvide) {
		if(this.weAreHere>=0) {
			Infos infosOld = this.infoss.get(weAreHere);
			this.infoss.remove( this.infoss.size() - 1 );
			return infosOld;
		}
		return infosvide;
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