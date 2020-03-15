package org.backend;
import java.util.Random;

public class Scheduler {
	
	private Simulation simulation;
	public Random random; //La m�thode nextInt() de la class Random permet de g�n�rer un entier al�atoire compris entre 0 inclus et l'entier pass� en param�tre exclus
	
	public Scheduler(Simulation simulation) {
		this.simulation = simulation;
		this.random = new Random();
		if(simulation.getSchedulerType().equals("with file")) {
			// TODO Adapter
		}
		
	}
	
	public int getNext() {
		
		Process procs[] = simulation.getProcesses();
		boolean allDone = true;
		
		// On v�rifie qu'il reste des proccesus � faire avanc�
		for(int i=0; i < procs.length; ++i) {
			if(!procs[i].isDone()) {
				allDone = false;
				break;
			}
		}
		if(allDone)
			return -1; // Si tous le monde a fini on retourne la valeur d'erreur
		
		int next = -1;
		
		// Dans le cas ou la simulation est al�atoire : 
		if(simulation.getSchedulerType().equals("random")) {
			
			next = this.random.nextInt(procs.length); // On g�n�re un entier al�atoire (entre 0 et nbProc)
			
			while (procs[next].isDone()) { // Si le processus que l'on a pioch� a termin�, 
				if(simulation.getSchedulerType().equals("random")) {
					next = this.random.nextInt(procs.length); //On en repioche 1
				}
			}
		}
		
		// Dans le cas ou on suit un fichier : 
		if(simulation.getSchedulerType().equals("with file")) {
			// TODO, adapter � un fichier
		}
		
		
		return next;
	}

}
