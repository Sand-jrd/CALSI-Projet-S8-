package org.backend;
import java.util.Random;
import org.tools.Tools;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.backend.exceptions.*;

public class Scheduler extends Tools{
	
	private Simulation simulation;
	public Random random; //La m�thode nextInt() de la class Random permet de g�n�rer un entier al�atoire compris entre 0 inclus et l'entier pass� en param�tre exclus
	
	private ArrayList<Integer> ScheduleFileParsed;  //On mettra la dedans la suite de int. 
	private String SchedString;
	
	public Scheduler(Simulation simulation) {
		
		this.simulation = simulation;
		this.random = new Random();
		
		if(simulation.getSchedulerType().equals("with file")) {
			
			this.SchedString = simulation.getSchedString();
			this.ScheduleFileParsed = GetSchedFileParsed(this.SchedString);
			// TODO transform� le fichier texte en suite de int que l'on met dans -> "SheduleFileParced"
		}
		
	}
	
	
	public ArrayList<Integer> GetSchedFileParsed(String SchedStringName) {
		
		ArrayList<Integer> SchedInt = new ArrayList<Integer>();
        String ordre[]=SchedStringName.split("\n");  //On sépare la chaine par chaque \n
        for (int i = 0; i < ordre.length; i++)   { //Tant qu'on arrive pas au bout de la chaine					//Réinitialisation du buffer pour chaque processus

           	SchedInt.add(Integer.parseInt(ordre[i])); //Une fois le numéro du processus récupéré sous
           													//forme de String on le cast en int
        }

        return SchedInt; //On retourne l'ArrayList d'entier nous permettant de programmer la simulation
    }	
	
	
	
	public int getNext() throws SchedulerException {
		
		
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
		
		System.out.println( "No de step effectu� : " + simulation.getExecutionOrderHistory().size());

		
		// Dans le cas ou on suit un fichier : 
		if(simulation.getSchedulerType().equals("with file")) {
			if(simulation.getExecutionOrderHistory().size()>=(this.ScheduleFileParsed.size()-1)) {
				throw new SchedulerException("Fin de l'ordonnanceur déjà atteinte");
			}
			else {
				next = this.ScheduleFileParsed.get(simulation.getExecutionOrderHistory().size());
				if(next>simulation.getNumberOfProcesses()) {
					throw new SchedulerException("Numéro de processus non existant dans cette simulation");
				}
				if(procs[next].isDone()) {
					throw new SchedulerException("Processus déjà fini, ordonnanceur erroné");
					//Processeur à faire avancer déjà terminé, scheduleur erroné
				}			
			}
			// TODO renvoyer le bon int, throw les bonnes exeptions
			// simulation.getExecutionOrderHistory().size() permet d'avoir le nombre de step qui a �t� effectu�. 

			
			
		}
		
		
		return next;
	}

}
