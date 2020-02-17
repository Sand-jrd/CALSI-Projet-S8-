package org.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.*;
import java.lang.String;
import java.math.RoundingMode;

import org.backend.BackEndException;
import org.backend.BadSourceCodeException;
import org.backend.Infos;
import org.backend.RipException;
import org.backend.Simulation;
import org.backend.SimulationBuilder;
import org.backend.VariableInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.text.Text;

import org.tools.Tools;
import bsh.EvalError;

/**
 * This class will saved all the states simulation will come true. Hence, We will be able to StepBack and Save a simulation.
 * @author Sandrine Juillard
 *
 */
public class History {
	
	private	ArrayList<ArrayList<Text>> textForProcess;
	int weAreHere;
	private ArrayList<Simulation> simulations;
	private ArrayList<Infos> infoss;
	private ArrayList<int []> processline;
	
	//-- Historique vide
	public History() {
		this.textForProcess = new ArrayList<ArrayList<Text>>();
		this.simulations = new ArrayList<Simulation>();
		this.infoss = new  ArrayList<Infos>();
		this.processline = new  ArrayList<int []>();
		this.weAreHere=-1;
	}
	
	// -- Les addStep pour enregistrer les différente information qui se trouve un peu partout dans le code (ça sera optimisable plus tard pour la qualité du code)
	public void addStep(Infos infos,Simulation simulation,int [] processline) {
		this.simulations.add(new Simulation(simulation));
		this.infoss.add(new Infos(infos));
		this.processline.add(processline.clone());
		this.weAreHere++;
	}
	
	public void addStep(ArrayList<Text> textForProcess) {
		this.textForProcess.add(textForProcess);
	}
	
	
	//Les GetBackInTime pour revenir en arrière
	public ArrayList<Text> getBackInTime(ArrayList<Text> textForProcessvide) {
		if(this.weAreHere>=0) {
			System.out.println("Text is set !");
			ArrayList<Text> textForProcessOld = this.textForProcess.get(weAreHere);
			this.textForProcess.remove( this.textForProcess.size() - 1 );
			return textForProcessOld;
		}
		return textForProcessvide;
	}
	
	public Simulation getBackInTime(Simulation simulationvide) {
			System.out.println("Simulation is set !");
			Simulation simulationsOld = simulationvide.getInfos().getSimulation();
			return simulationsOld;
	}
	
	public Infos getBackInTime(Infos infosvide) {
		if(this.weAreHere>=0) {
			System.out.println("Info is set !");
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