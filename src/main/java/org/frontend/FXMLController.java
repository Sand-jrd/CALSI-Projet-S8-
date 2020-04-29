package org.frontend;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.text.DecimalFormat;
import javafx.stage.FileChooser;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.control.Tooltip;

import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.geometry.Insets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.KeyStroke;

import org.backend.PreTreatment;
import org.backend.Simulation;
import org.backend.SimulationBuilder;
import org.backend.parceTools.blockType.Blocks;
import org.backend.varStorage.Variable;
import org.backend.exceptions.*;
import org.backend.History;

import java.io.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.text.TextFlow;
import javafx.scene.control.Alert.AlertType;

/**
 * @author renon
 *
 */
/**
 * @author renon
 *
 */


public class FXMLController {


//-------------- Veleurs par default dans "Shared Variable" et "Local Variable" ---------//
	ObservableList<String> content1 = FXCollections.observableArrayList(
			"e", "d","f","g","h","i","j","k","l");
	ObservableList<String> content2 = FXCollections.observableArrayList(
			"3", "7");
	ObservableList<String> content3 = FXCollections.observableArrayList(
			"a", "b", "c", "d");
	ObservableList<String> content4 = FXCollections.observableArrayList(
			"1", "2" , "3", "4");

//---------------------------- DECLARATION des BOUTTONS --------------------------------//

	@FXML
	private Button buttonStart;
	@FXML
	private Button buttonStop;
	@FXML
	private Button buttonNewExecution;
	@FXML
	private Button buttonDoSteps;
	@FXML
	private Button buttonPlusStep;
	@FXML
	private Button buttonMinusStep;
	@FXML
	private Button EditShed;
	@FXML
	private Label SchedName;
	@FXML
	private Button buttonProcessCrash;
	@FXML
	private ListView<String> listView1;   //Mdr on laisse pas de nom comme �a ptn
	@FXML
	private ListView<String> listView2;
	@FXML
	private ListView<String> listView3;
	@FXML
	private ListView<String> listView4;

	@FXML
	private ChoiceBox<String> choiceBoxLocalVariables;

	@FXML
	private ChoiceBox<String> choiceBoxScheduling;

	@FXML
	private ChoiceBox<String> choiceBoxProcessToCrash;

	@FXML
	private ChoiceBox<String> choiceBoxStepByStep;

	@FXML
	private Slider sliderSpeed;

	@FXML
	private TextArea textAreaOriginalCode;

	@FXML
	private TextArea textAreaParsedCode;

	@FXML
	private TextFlow lineProc;

	@FXML
	private TextField textFieldSpeed;

	@FXML
	private TextField textFieldNumberOfProcessesRandom;

	@FXML
	private GridPane Animation;

	@FXML
	private GridPane numCod;

	@FXML
	private TextField textFieldNumberOfSteps;

	 @FXML
	 private Canvas lineProcCanvas;

	 private TextField initialisationBlock;
	 private TextFlow initialisedProc;

	//---------------------------- VARIABLES GLOBALES --------------------------------------//

	boolean auto = false;
	private static DecimalFormat df = new DecimalFormat("0.0");
	private String code=" ";
	private String fichiercode="";
	private String cordo="";
	private int numberOfProcesses;
	private Timeline timeline;
	private double s=50.00;
	private PreTreatment preTreatment;

	private String fichierShed=""; // Chemain absolu du fichier
	private String ShedString="";  // Contenu du fichier.

	private int [] processline;
	private SimulationBuilder simulationBuilder;
	private Simulation simulation;
	private boolean ignorAlert;
	private	History history;
	private GraphicsContext gc;


	//---------------------------------------------------------------------------------------------------------------------------//
	//---------------------------- FONCTIONS d'ACTION QUAND ON CLIQUE SUR UN BOUTON  --------------------------------------------//
	//---------------------------------------------------------------------------------------------------------------------------//




	//Initialisation 1er lancement
	public void initialize() {

        Tooltip tooltipdetest = new Tooltip("This is the way the scheduler will decide about the next processus to run");
        choiceBoxScheduling.setTooltip(tooltipdetest);

		choiceBoxLocalVariables.getSelectionModel().selectedItemProperty()
	    .addListener((obs, oldV, newV) -> updateLocalVariables());

		choiceBoxScheduling.getItems().addAll("Random");
		choiceBoxScheduling.setValue("Random");
		listView1.setItems(content1);
		listView2.setItems(content2);
		listView3.setItems(content3);
		listView4.setItems(content4);
		textAreaOriginalCode.setText(code);

	}

	//---------------------------------------------------------------------------------------------------------------------------//
										//---------- BARRE DE MENU_1 (File Edit Help) ----------//

	//-> FILE <-//

	// Bouton "OPEN"
	public void openFile() {
		System.out.print("File will be open"+"\n");

		FileChooser fileChooser = new FileChooser();
		File selectedFile = fileChooser.showOpenDialog(null);
		System.out.print("File selected is : "+ selectedFile + "\n");
		if (selectedFile != null) {
			System.out.print("selectedFile n'est pas null\n");
			fichiercode= selectedFile.getAbsolutePath();
			try (BufferedReader reader = new BufferedReader(new FileReader(new File(fichiercode)))) {
				System.out.print("Reading code \n");
				String line;
				code="";

				while ((line = reader.readLine()) != null)
					code=code+line+"\n";
				System.out.print(code +"\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			textAreaOriginalCode.setText(code);
		}
		else {
			System.out.print("cancel"+"\n");
		}
	}

	// Bouton "SAVE"
	public void saveFile() {

	    FileChooser fileChooser = new FileChooser();
	    File selectedFile = fileChooser.showSaveDialog(null);
	    code=textAreaOriginalCode.getText();

		try (FileWriter fw = new FileWriter(selectedFile.getAbsolutePath())){
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(code);
			bw.flush();
			bw.close();
			System.out.print("saved\n");
		}catch (IOException e) {
			e.printStackTrace();
		}

	}


	// Bouton "OPEN Sheduler"
	public void openSched() {
		System.out.print("File will be open"+"\n");

		// Fen�tre qui permet de naviger dans les fichiers et faire ouvrir
		FileChooser fileChooser = new FileChooser();
		File selectedFile = fileChooser.showOpenDialog(null);

		// LECTURE DU FICHIER
		if (selectedFile != null) {
			fichierShed= selectedFile.getAbsolutePath(); //R�cup�ration du chemain absolu
			try (BufferedReader reader = new BufferedReader(new FileReader(new File(fichierShed)))) {

				String line;
				ShedString="";

				//On lit ligne par ligne, ici.
				while ((line = reader.readLine()) != null) {

					// On concact�ne les lignes pour les enregsitrer dans un long string.
					//Si tu p�f�re une liste ou autre chose tu peu changer �a.
					ShedString=ShedString+line+"\n";
				}

			// Truc pour le font.
			SchedName.setText(selectedFile.getName());
			choiceBoxScheduling.getItems().addAll("With File");
			choiceBoxScheduling.setValue("With File");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.print("cancel"+"\n");
		}
	}

	// Bouton "SAVE SCheduler"
	public void saveSched() {

	    FileChooser fileChooser = new FileChooser();
	    File selectedFile = fileChooser.showSaveDialog(null);

		try (FileWriter fw = new FileWriter(selectedFile.getAbsolutePath())){
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(ShedString);
			bw.flush();
			bw.close();
			System.out.print("saved\n");
		}catch (IOException e) {
			e.printStackTrace();
		}

	}

	//-> HELP <-//

	// Bouton AtomicOperation
	public void help1(){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("AtomicOperation");
        alert.setHeaderText(null);
        alert.setContentText("This is a list of operation that you can made** on shared variable. \r\n" +
        		"This list is exhaustive of what our program can made and you cannot do any other atomic operation with shared variable.***\r\n" +
        		"\r\n" +
        		"	1) .read()			: Return the value of the shared register\r\n" +
        		"	2) .write(x)		: Write x in the shared register\r\n" +
        		"	\r\n" +
        		"	3) .update(x)		: Write x in the shared array at the position i (i=index of the process)\r\n" +
        		"	4) .snapshot()		: Return the value of the shared array\r\n" +
        		"	\r\n" +
        		"	5) .enqueue(x)		: Add the value x on queue of the shared queue\r\n" +
        		"	6) .dequeue()		: Return and removed the value at the tail of the shared queue\r\n" +
        		"\r\n" +
        		"**	All those operations are not yet available. ");
        alert.setResizable(true);
        alert.getDialogPane().setPrefSize(500, 500);
        alert.showAndWait();
	}

	// Bouton HowToLaunchYourSimulation
	public void help2(){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("HowToLaunchYourSimulation");
        alert.setHeaderText(null);
        alert.setContentText("How To Launch Your Simulation.\r\n" +
        		"\r\n" +
        		"To launch the execution, first load (or write) your algorithm on the tab \"Original Code\". To load your algorithm, use the tab \"File->Open...\"\r\n" +
        		"Make sure this file respect the rules describe in HowToWriteYourCode. \r\n" +
        		"Then you have to choose your scheduler policy with \"Scheduling Policy\". You have the choice between \"Random, StepByStep and with File\".\r\n" +
        		"Unless you choose \"With file\", you have then to specify the number of process in your simulation in \"Number of processes\" at the right of the Window. \r\n" +
        		"\r\n" +
        		"After those settings, you can launch the simulation clicking on \"New Execution\".\r\n" +
        		"Once launch, you can use the button \"+step\" to execute one step. \r\n" +
        		"The scheduler will then choose a process and execute a step, all the value of local and shared variables will be updated.\r\n" +
        		"The button \"-step\" allow you to go back one step before.**\r\n" +
        		"\r\n" +
        		"At the end of the simulation you can then save the last execution, \"Scheduler->Save last execution\". To know more about the format of those file read HowToWriteYourScheduler\r\n" +
        		"\r\n" +
        		"** The feature \"-step\" is not yet implemented");
        alert.setResizable(true);
        alert.getDialogPane().setPrefSize(500, 500);
        alert.showAndWait();
	}

	// Bouton HowToWriteYourCode
	public void help3(){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("HowToWriteYourCode");
        alert.setHeaderText(null);
        alert.setContentText("When you write your code you have to respect the following syntax, you can find an example in the file sampleCode.txt\r\n" +
        		"Before your code, your text file must be compose of three compact blocks without any blank line.**\r\n" +
        		"The different block must be separated from each other and from the code of your algorithm with a blank line.\r\n" +
        		"	1)	Import are done in the beginning of the file, before the first block, without any blank line between the different import\r\n" +
        		"	2)	The first block is there for the Shared Variable declaration, without any blank line between the different declaration\r\n" +
        		"	3)	The second block is there for the Shared Variable initialization, without any blank line between the different initialization\r\n" +
        		"	4)	The third block is there for Local Variable declaration, without any blank line between the different declaration\r\n" +
        		"	5)	Then you can write the code of your concurrent algorithm*\r\n" +
        		"** The initialization of the simulation may change in the future, and the usage of block may be outdated soon.***\r\n" +
        		"*** This is still how you have to write your code for now.\r\n" +
        		"\r\n" +
        		"\r\n" +
        		"\r\n" +
        		"* When you write your code you have to respect the following rules:\r\n" +
        		"	1)	The code of your algorithm should be written respecting the Java syntax\r\n" +
        		"	2)	You only can made 1 atomic operation per line (without what your severals operation will be done atomicly, which may change the way your algorithm work)\r\n" +
        		"	\r\n" +
        		"What is important to notice is that all operation made in one line are done atomicly.\r\n" +
        		"We know that the second rule is restrictive for the user and that it asks him to assure that every line don't contain more than 1 atomic operation.\r\n" +
        		"That why this implementation should be changed in the future.");
        alert.setResizable(true);
        alert.getDialogPane().setPrefSize(500, 500);
        alert.showAndWait();
	}

	// Bouton HowToWriteYourScheduler
	public void help4(){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("HowToWriteYourScheduler");
        alert.setHeaderText(null);
        alert.setContentText("// Scheduler Syntax.**\r\n" +
        		"When you want to use your own scheduler policy*, you have to specify at every step which process has the hand.\r\n" +
        		"So if you want to load a scheduler you have to write a text file with the following syntax :\r\n" +
        		"	1)	In the first line you write the number of process which are running you algorithm\r\n" +
        		"	2)	At every line you write the ID of the process that has the hand\r\n" +
        		"	3) 	To simulate a crash, use \"!\" before the number of the process.\r\n" +
        		"\r\n" +
        		"If at the end of the file all the processes have not finished the execution (or are crashed), a loop is made.\r\n" +
        		"It is important to note nothing guaranteed that the simulation will finish. \r\n" +
        		"\r\n" +
        		"You can see an example of the scheduler format, in the text file sampleScheduler.txt\r\n" +
        		"\r\n" +
        		"To generate those files, you can at the end of the simulation save the last execution, \"Scheduler->Save last execution\".\r\n" +
        		"	\r\n" +
        		"* ie. not random\r\n" +
        		"** Feature not implemented yet");
        alert.setResizable(true);
        alert.getDialogPane().setPrefSize(500, 500);
        alert.showAndWait();
	}

	// Bouton samleScheduler
	public void help5(){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("samleScheduler");
        alert.setHeaderText(null);

        Image image = new Image("file:Images/1.png", true);
        ImageView imageView = new ImageView(image);
        alert.setResizable(true);
        alert.getDialogPane().setPrefSize(800, 800);
        alert.setGraphic(imageView);
        alert.showAndWait();
	}

	// Bouton sampleCode
	public void help6(){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("sampleCode");
        alert.setHeaderText(null);
        alert.setContentText("import java.lang.Math; 						//	All import have to be made without any blank line\r\n" +
        		"\r\n" +
        		"// // Shared variables declaration			//	All declaration have to be made without any blank line\r\n" +
        		"Integer turn;\r\n" +
        		"Boolean[] flag;\r\n" +
        		"\r\n" +
        		"// // Shared variables initialization		//	All initialization have to be made without any blank line\r\n" +
        		"turn = new Integer(0);\r\n" +
        		"flag = new Boolean[2];\r\n" +
        		"flag[0] = false;\r\n" +
        		"flag[1] = false;\r\n" +
        		"\r\n" +
        		"// // Local variables declaration			//	All declaration have to be made without any blank line\r\n" +
        		"int j;\r\n" +
        		"boolean a;\r\n" +
        		"int b;\r\n" +
        		"\r\n" +
        		"// // Algorithm								//	You can write your algorithm using Java Syntax, blank line are allowed\r\n" +
        		"// 											//	You only can access one variable operation at a time\r\n" +
        		"// \r\n" +
        		"\r\n" +
        		"// // There for example, Petreson algorithm \r\n" +
        		"j = (i+1) % 2;\r\n" +
        		"flag[i] = true;\r\n" +
        		"turn = j;\r\n" +
        		"a = flag[j];								// All operation made in one line are atomic\r\n" +
        		"b = turn;										\r\n" +
        		"											//	Blank line are allowed\r\n" +
        		"while ( a == true && b == j) {				//	The opening accolade \"{\" have to be in the same line than the condition\r\n" +
        		"a = flag[j];\r\n" +
        		"b = turn;\r\n" +
        		"}											//	The closing accolade \"}\" have to be alone on the line\r\n" +
        		"// critical section \r\n" +
        		"1\r\n" +
        		"1\r\n" +
        		"1\r\n" +
        		"1\r\n" +
        		"1\r\n" +
        		"1\r\n" +
        		"1\r\n" +
        		"1\r\n" +
        		"// end of critical section\r\n" +
        		"flag[i] = false;\r\n" +
        		"");
        alert.setResizable(true);
        alert.getDialogPane().setPrefSize(500, 500);
        alert.showAndWait();
	}

	//-> Teaching <-//

		// Bouton Splitter
		public void splitter(){
			openTeachingWin("splitter");
		}
		// Bouton Bakery
		public void bakery(){
			openTeachingWin("bakery");
		}

	//---------------------------------------------------------------------------------------------------------------------------//
						//---------- BARRE DE MENU_2 (New execution et tous se qu'il y a deri�re) ----------//

	// -- Bouton "NEW EXECUTION" --  //
	public void newExecution() throws BackEndException {
		flushall();
		System.out.print("New Execution button clicked \n");
		ignorAlert = false;

		simulationBuilder = new SimulationBuilder();

		//Print du r�pertoire courant
		String currentDir = System.getProperty("user.dir");
        System.out.println("Current dir using System:" +currentDir);

		//Path to test/sources.txt
		String sourcecode = currentDir + "\\src\\main\\resources\\org\\Algorithmes\\source.txt";
		System.out.println("Path to source code :" +sourcecode);

		// On copie le code du 'shell' dans sources.txt
		code=textAreaOriginalCode.getText();

		try (FileWriter fw = new FileWriter(sourcecode)){
			BufferedWriter bw = new BufferedWriter(fw);
			bw.flush();
			bw.write(code);
			bw.close();
			System.out.print("saved\n");
		}catch (IOException e) {
			//customeAlert("Vous n'avez aucun code !");
		}

		try {
		//Simulation et r�cup�rations des infos de la simulation
		String schedChoice = choiceBoxScheduling.getValue().toLowerCase();
		System.out.print("Start simulation\n");
		simulation = simulationBuilder
				.withSourceCodeFromFile(sourcecode)
				.withNumberOfProcesses(Integer.parseInt(textFieldNumberOfProcessesRandom.getText()))
				.withScheduler(schedChoice,ShedString)
				.build(); //Cr�ation de la simulation
		System.out.print("Finishing simulation \n");
		System.out.print(simulation.simulationIsDone());

		//Nouvelle m�thode, tous enregistrer dans History
		history = new History();

		//Updates de l'affichage
		setThenumberToTheCode();
		initalizeProcess(Integer.parseInt(textFieldNumberOfProcessesRandom.getText()));  //La fonction qui initialise le truc � gauche (avec les lignes)
		updateChoiceBoxLocalVariables();
		updateChoiceBoxStepByStep();
		updateChoiceBoxProcessToCrash();
		updateSharedVariables();
		textAreaParsedCode.setText(simulation.getNewSourceCode());

		}catch (Exception e) {
			System.out.print(e);
			System.out.print("Error in parsing code \n");
			customeAlert("Echec de l'execution");
			flushall();
		}

	}

	// -- Bouton "Choose Sheduler" -- //
	public void chooseSched() throws BackEndException{

	}

	// -- Bouton "SPEED" -- //

	//Quand on �crit dans le texte
	public void speedtex() {
		sliderSpeed.setValue(Double.valueOf(textFieldSpeed.getText()) );
	}

	//Quand on fait glisser le curceur
	public void slidert() {
		s= sliderSpeed.getValue();
		String s2= df.format(s);
		System.out.print(s+"\n");
		textFieldSpeed.setText(""+s2);
	}


	// -- Bouton "START" -- //
	public void startAuto() throws BackEndException, InterruptedException{
		if(simulation != null) {
		System.out.println( "Process starting ...");
		if (!auto) {
		auto = true;
		timeline = new Timeline(new KeyFrame(Duration.millis(10000/s), new EventHandler<ActionEvent>() {

		    @Override
		    public void handle(ActionEvent event) {
		    	if (!simulation.simulationIsDone() && auto) {
		    		try {
						controllerPlusStep();
					} catch (BackEndException e) {
						e.printStackTrace();
					}
		    	}
		    	if(simulation.simulationIsDone()) {
					stopAuto();
				}

		    }
		}));
		timeline.setCycleCount(10000000);
		timeline.play();
		}
		}
		else if(simulation == null) {
	    	customeAlert("You must start a new execution");
    	}
	}


	// -- Bouton "STOP" -- //
	public void stopAuto(){
		if(auto != false) {
			System.out.println( "Process Stop");
			auto = false;
			timeline.stop();
			if(simulation != null && simulation.simulationIsDone()) {
				customeAlert("Simulation is done !");
			}
		}
		else {
			customeAlert("No auto-simaltion is in process");
		}
	}


	// -- Bouton "DO STEPS" -- //
	public void controllerDoSteps() throws BackEndException{
		try {
			int count = Integer.parseInt(textFieldNumberOfSteps.getText());
			while (!simulation.simulationIsDone() && count>0) {
				count -= 1;
				controllerPlusStep(); //D�clanche i fois la fonction PlusStep ci dessous
			}
			if(simulation.simulationIsDone()) {
				customeAlert("Simulation is done !");
			}
		}catch(Exception e) {
	    	if(simulation == null) {
		    	customeAlert("You must start a new execution");
	    	}
		}
	}

	// -- Bouton "+ step", ET EGALEMENT UTILISER DANS DO STEPS -- //
	public void controllerPlusStep() throws BackEndException{

		if(ignorAlert == false && checkCodeChange() == true) {
			ignorAlert = true;
	    	customeAlert("Beware ! \nYou made some modification in your code.\nThis will NOT apply for this simulation.\nStart a new execution if you want to test your new code.\nOtherwise, juste ignor this message. ");
		}

		try {
		if (!simulation.simulationIsDone()) {

			history.addStep(simulation,processline);

			simulation.nextStep();

			ArrayList<Integer> arrayExec = simulation.getOriginalSourceLinesExecutedDuringLastStep(simulation.getIdOfLastExecutedProcess());

			updateProcess(simulation.getIdOfLastExecutedProcess(),arrayExec.get(0));

			updateSharedVariables();

			updateLocalVariables();
		}
		else {
	    	customeAlert("Simulation is done !");
		}
	    } catch (Exception e) {
	    	if(simulation == null) {
		    	customeAlert("You must start a new execution");
	    	}
	    	else {
	    	customeAlert("An unknown probleme have occured, try to restart your app");
	        System.out.println(e);
	    	}
	    }

	}

	// -- Bouton "- step" -- //
	public void controllerMinusStep() throws BackEndException{

		try {
			System.out.println("Step Back");
			simulation = history.getBackInTime(simulation);
			processline = history.getBackInTime(processline);

			history.getBackInTime();




		}catch(Exception e) {
			System.out.println("New Exe");
			newExecution();
		}

		ArrayList<Integer> arrayExec = simulation.getOriginalSourceLinesExecutedDuringLastStep(simulation.getIdOfLastExecutedProcess());

        updateProcess(simulation.getIdOfLastExecutedProcess(),arrayExec.get(0));
		updateSharedVariables();
		updateLocalVariables();

	}

	//Edit shed
	public void EditShed(){

			FXMLLoader loader = new FXMLLoader(getClass().getResource("editShed.fxml"));

	        Parent secondroot;

	        try {

			secondroot = loader.load();

	        Scene secondscene = new Scene(secondroot);
	        secondscene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

	        EditController controller = loader.getController();


	        Stage secondStage = new Stage();
	        secondStage.setTitle("Sheduler Editor");
	        secondStage.setScene(secondscene);
	        controller.initialize(ShedString);
	        secondStage.showAndWait();

	        ShedString = controller.getShed();

			} catch (IOException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}

	}

	//---------------------------------------------------------------------------------------------------------------------------//
					//---------- ONLGETS (Random step_by_step crashes) ((fen�tre � droite)) ----------//


	// ------- ONLGET STEp_BY_STEP ------- //

	//BOUTON CRASH
	public void onClickedCrashProcess() throws RipException {

	try {
		String currentProcess = choiceBoxProcessToCrash.getSelectionModel().getSelectedItem();
		int currentProcessId = Character.getNumericValue(currentProcess.charAt(1));
		simulation.crashProcess(currentProcessId);
		choiceBoxProcessToCrash.getItems().remove(currentProcess);
		choiceBoxStepByStep.getItems().remove(currentProcess);
		System.out.println(currentProcess + " crashed");
		updateProcess(currentProcessId,0);
	}catch(Exception e){
		if(simulation == null) {
	    	customeAlert("You must start a new execution");
    	}
	}
	}

	// ------- ONLGET STEp_BY_STEP ------- //

	//BOUTON NEXT_STEP
	public void onClickedStepByStepNextStep() throws BadSourceCodeException, RipException {

	try {
		String processToExecute = choiceBoxStepByStep.getSelectionModel().getSelectedItem();
		int processToExecuteId = Character.getNumericValue(processToExecute.charAt(1));

		ArrayList<Integer> arrayExec = simulation.getOriginalSourceLinesExecutedDuringLastStep(simulation.getIdOfLastExecutedProcess());

		simulation.nextStep(processToExecuteId);
		updateProcess(simulation.getIdOfLastExecutedProcess(),arrayExec.get(0));
		updateSharedVariables();
		updateLocalVariables();
	}catch(Exception e){
		if(simulation == null) {
	    	customeAlert("You must start a new execution");
    	}
	}
	}


	//---------------------------------------------------------------------------------------------------------------------------//
											//---------- JE SUIS PAS SUR DE A QUOI �A SERT ----------//


	public void updateChoiceBoxLocalVariables() {
		System.out.println( "updateChoiceBoxLocalVariables");
		choiceBoxLocalVariables.getItems().clear();
		for (int i = 0; i < numberOfProcesses; i++) {
			choiceBoxLocalVariables.getItems().add("P"+ Integer.toString(i));
		}
		choiceBoxLocalVariables.setValue("P0");
	}

	public void updateChoiceBoxStepByStep() {
		System.out.println( "updateChoiceBoxStepByStep");
		choiceBoxStepByStep.getItems().clear();
		for (int i = 0; i < numberOfProcesses; i++) {
			choiceBoxStepByStep.getItems().add("P"+ Integer.toString(i));
		}
		choiceBoxStepByStep.setValue("P0");
	}

	public void updateChoiceBoxProcessToCrash() {
		System.out.println( "updateChoiceBoxProcessToCrash");
		choiceBoxProcessToCrash.getItems().clear();
		for (int i = 0; i < numberOfProcesses; i++) {
			choiceBoxProcessToCrash.getItems().add("P"+ Integer.toString(i));
		}
		choiceBoxProcessToCrash.setValue("P0");
	}

	public void choiceordon() {
		System.out.println( "choiceordon ?wtf");
		cordo=choiceBoxScheduling.getValue();
		System.out.print(cordo+"\n");
	}

	//---------------------------------------------------------------------------------------------------------------------------//
									//---------- UPTADTES DES TABLEAU D'INFORMATION  ---------//


	// Updates de Shared Variables
	public void updateSharedVariables() {
		content3.remove(0, content3.size());
		content4.remove(0, content4.size());
		Variable[] variableInfo = simulation.getSharedVariables();

		for(int i=0;i<variableInfo.length;i++)
		{
			if(variableInfo[i] == null)
			{
				break;
			}
			else {
				content3.addAll(variableInfo[i].getName());
				content4.addAll(variableInfo[i].getValue());
			}
		}
	}

	// Updates de LocalVariables
	public void updateLocalVariables() {
		content1.remove(0, content1.size());
		content2.remove(0, content2.size());
		String currentProcess = choiceBoxLocalVariables.getSelectionModel().getSelectedItem();

		int currentProcessId = Character.getNumericValue(currentProcess.charAt(1));
		System.out.println("chosen process " + Integer.toString(currentProcessId));
		Variable[] variableInfo;
		try {
			variableInfo = simulation.getLocalVariables(currentProcessId);
		} catch (RipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		for(int i=0;i<variableInfo.length;i++)
		{
			if(variableInfo[i] == null)
			{
				break;
			}
			else {
				System.out.println("    " + variableInfo[i].getName() + " " + variableInfo[i].getValue());
				content1.addAll(variableInfo[i].getName());
				content2.addAll(variableInfo[i].getValue());
			}
		}
	}

	//---------------------------------------------------------------------------------------------------------------------------//
													//  -- FONCTIONS TOOLS -- //


	//Compteur de lignes d'un code
	private static int countLines(String str){
		   String[] lines = str.split("\r\n|\r|\n");
		   return  lines.length;
	}

	public void initGrid() {

		Animation.getChildren().clear();

		//preTreatment = new PreTreatment(code, numberOfProcesses);
		//ArrayList<Blocks> blocksList = preTreatment.getBlocksConversion().getBlockStruct();
		//int endOfInit = preTreatment.getEndOfInitBlocks();

	    for (int y = 0 ; y < countLines(code) ; y++) {
	        Animation.add(new Label(y+")"),0,y);
		}

		/* On fait apparaître les if/while/for dans la grille 
	    for (int x = 0 ; x < blocksList.size() ; x++){
	    	//Blocks block = blocksList.get(x);
	    	//String type = block.getType();
	    	//int startLine = block.getTruelineStart();
	    	Animation.add(new Label(startLine+")"+type), 0, startLine);
		}
		 */
	}

	//La fonction qui r�initialise l'execution
	public void initalizeProcess(int nbrp) throws RipException{

		initGrid();

		//Initialisation animation texte
		numberOfProcesses=nbrp;
		processline= new int[nbrp];
		Arrays.fill(processline, 0);
		updateProcess(0,0);
	}

	//La fonction qui r�initialise l'execution
	public void customeAlert(String alertText) {

	 Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(alertText);
        alert.setResizable(true);
        alert.getDialogPane().setPrefSize(400, 150);
        alert.show();

	}

	// Vide toute les initialisation, les variable de l'app sont comme si elle venait juste de d�marrer.
	public void flushall() {
		processline = null;
		simulationBuilder = null;
		simulation= null;
		history = null;
		listView1.setItems(content1);
		listView2.setItems(content2);
		listView3.setItems(content3);
		listView4.setItems(content4);
		System.out.print(simulation = null);
		lineProc.getChildren().clear();

	}

	// Charge un code depuis teaching
	public void loadFromTeaching() {

    	String currentDir = System.getProperty("user.dir");
        //System.out.println(currentDir);
    	String sourcecode = currentDir + "\\src\\main\\resources\\org\\Algorithmes\\source.txt";

		try (BufferedReader reader = new BufferedReader(new FileReader(new File(sourcecode)))) {

			String line;
			code="";
			while ((line = reader.readLine()) != null)
				code=code+line+"\n";
		} catch (IOException e) {
			e.printStackTrace();
		}
		textAreaOriginalCode.setText(code);

	}

	// Pour v�rifi� si le code � �t� modifi� ou pas
	public boolean checkCodeChange() {
		if(textAreaOriginalCode.getText().equals(code)) {
			return false;
		}
		return true;
	}

	public void openTeachingWin(String algo) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("teaching.fxml"));

        Parent secondroot;

        try {

		secondroot = loader.load();

        Scene secondscene = new Scene(secondroot);
        secondscene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        TeachingController controller = loader.getController();

        // capitalize first letter
        String Algo = algo.substring(0, 1).toUpperCase() + algo.substring(1);

        Stage secondStage = new Stage();
        secondStage.setTitle(Algo);
        secondStage.setScene(secondscene);
        controller.initialize(algo);
        secondStage.showAndWait();

		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
        //Load le splitter (Si "test it!" a �t� actionn�, r�initialise tous sinon )
        flushall();
        loadFromTeaching();
	}

	private void setThenumberToTheCode() {

		numCod.getChildren().clear();

	    for (int y = 0 ; y < countLines(code) ; y++) {
	    	System.out.println(y);
			Text line = new Text(y+".");  //On cr�e un Object "TEXTE" (un string avec des info sur le font)
			line.setStyle("    -fx-font-size: 12.7;\r\n" +
							"   -fx-fill: #bebfc2;");
	    	numCod.add(line,0,y);

		}
	}

	//ANIMATION AVEC LA GRILLE
	public void updateProcess(int nump,int linep) throws RipException{

		//ArrayList<Blocks> BlockStruct = simulation.getBlockStruct(); // Ici, la strucure que j'ai cr�e. cf le docs ou j'explique se qu'il y a dedans (y'as pas les infos pour l'�tat des processus, juste les info sur comment est le code)

        initGrid();

        processline[nump]=linep;
        int nbperline;

		// La textForProcess c'est l'id de la balise FXML dans laquelle on va mettre l'animation. Pour l'instant c'est un texte
		for (int l = 0; l < countLines(code) ; l++) {
			nbperline = 1;
			for (int i = 0; i < numberOfProcesses; i++) {
				if (l==processline[i]) {

					if(simulation.processIsCrashed(i)) {
						StackPane proc = new StackPane();
						proc.getChildren().addAll(new Circle(10,Color.RED), new Label("P"+i));
						addTooltip(proc,i);
						Animation.add(proc,nbperline,l);
					}
					else if(simulation.processIsDone(i)) {
						StackPane proc = new StackPane();
						proc.getChildren().addAll(new Circle(10,Color.BLUE), new Label("P"+i));
						addTooltip(proc,i);
						Animation.add(proc,nbperline,l);
					}
					else if(i==nump) {
						StackPane proc = new StackPane();
						proc.getChildren().addAll(new Circle(10,Color.GREEN), new Label("P"+i));
						addTooltip(proc,i);
						Animation.add(proc,nbperline,l);
					}
					else {
						StackPane proc = new StackPane();
						proc.getChildren().addAll(new Circle(10,Color.web("#8599ad")), new Label("P"+i));
						addTooltip(proc,i);
						Animation.add(proc,nbperline,l);
					}
					nbperline++;
				}
			}
		}

		Animation.setGridLinesVisible(true);
	}


	public void addTooltip(StackPane proc, int currentProcessId) {

		//On r�cup�res lse donn�e

		Variable[] variableInfo;
		String str = "";

		try {
			variableInfo = simulation.getLocalVariables(currentProcessId);
		} catch (RipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		for(int i=0;i<variableInfo.length;i++)
		{
			if(variableInfo[i] == null)
			{
				break;
			}
			else {
				System.out.println("    " + variableInfo[i].getName() + " " + variableInfo[i].getValue());
				str = str + variableInfo[i].getName() + " = ";
				str = str + variableInfo[i].getValue() + "\n";
			}
		}

		//ON installe le tooltip

		Tooltip tool = new Tooltip(str);
		tool.setShowDelay(new Duration(100));
		Tooltip.install(proc,tool);

	}


	// ANIMATION EN TXT
	public void updateProcessTXT(int nump,int linep) throws RipException{

		//ArrayList<Blocks> BlockStruct = simulation.getBlockStruct(); // Ici, la strucure que j'ai cr�e. cf le docs ou j'explique se qu'il y a dedans (y'as pas les infos pour l'�tat des processus, juste les info sur comment est le code)

        lineProc.getChildren().clear();

        processline[nump]=linep;

		// La textForProcess c'est l'id de la balise FXML dans laquelle on va mettre l'animation. Pour l'instant c'est un texte
		// Il faudra adapter le code ET le FXML pour que � la place d'un texte, on est une grille.

		for (int l = 0; l < countLines(code) ; l++) {
			Text textForProcess2 = new Text(Integer.toString(l)+")");  //On cr�e un Object "TEXTE" (un string avec des info sur le font)
			textForProcess2.setFont(Font.font("System", 18.9));
			textForProcess2.setStyle("-fx-font-weight: normal");
			textForProcess2.setFill(Color.BLACK);
			lineProc.getChildren().add(textForProcess2);
			for (int i = 0; i < numberOfProcesses; i++) {
				if (l==processline[i]) {
					Text textForProcess = new Text("P"+Integer.toString(i)+",");
					textForProcess.setFont(Font.font("System", 18.9));
					textForProcess.setStyle("-fx-font-weight: normal");
					if(simulation.processIsDone(i)) {
						textForProcess.setFill(Color.BLUE);
					}
					else if(simulation.processIsCrashed(i)) {
						textForProcess.setFill(Color.RED);
					}
					else if(i==nump) {
						textForProcess.setStyle("-fx-font-weight: bold");
					}
					else {
						textForProcess.setFill(Color.BLACK);
					}
					lineProc.getChildren().add(textForProcess);
				}
			}

			Text textForProcess = new Text("\n");
			textForProcess.setFont(Font.font("System", 18.9));
			lineProc.getChildren().add(textForProcess); // Et la on met l'object texte que l'on viens de cr�e dans la balise "textForProcess"
		}
	}

}
