package org.frontend;
 
import javafx.fxml.FXML;
//import javafx.scene.media.Media;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

import javafx.event.ActionEvent;

import java.io.*;


/**
 * @author renon
 *
 */
/**
 * @author renon
 *
 */

public class TeachingController{
    
	//---------------------------- DECLARATION des BOUTTONS --------------------------------//
	
	//@FXML
	//private Media video;
	
	@FXML
	private Button buttonProcessCrash;
	
	@FXML
	public Button closeButton;

	@FXML
	public TextFlow algoTxt;
	
	//---------------------------- VARIABLES GLOBALES --------------------------------------//

	public String algo;
	
	//---------------------------- Initialisation de la fenï¿½tre --------------------------------------//

    public void initialize(String str) {
        System.out.print("\n Initialization " + algo+"\n");


    	algo = str;
    	//Initialise la fenï¿½tre avec la bonne explication.
    	algoTxt.getChildren().clear();
    	Text theTxt = new Text("Error");
    	if(algo == "splitter") {
    		theTxt = new Text("Explication du splitter ici !");
    	}
    	else if(algo == "bakery") {
    		theTxt = new Text("Desciption of the algorithm : \n"
    				+ "Two SWMR safe registers, denoted FLAG[i] and MY_TURN[i], are associated with each   \n"
    				+ "process pi (hence these registers can be read by any process but written only       \n"
    				+ "by pi). \n"
    				+ "My_TURn[i] (initialized to 0 and reste when pi exits the critical section) is used to \n"
    				+ "contain the priority number of pi when it wants to use the critical section. \n"
    				+ "FLAG[i] is a binary control variable whose domain is {down,up}. Inizializd to down, it is set \n"
    				+ "to up by pi while it compute the value of its priority number MY_TURN[i].\n"
    				+ "\n\nPseudo-code : \n"
    				+ "(1) FLAG[i] <- up;\n"
    				+ "(2) MY_TURN[i] <- max(MY_TURN[i],...,MY_TURN[n])+1;\n"
    				+ "(3) FLAG[i] <- down;\n"
    				+ "(4) for each j € {1,...,n}\\{i} do\n"
    				+ "(5)	 wait(FLAG[j]=down);\n"
    				+ "(6)	 wait((MY_TURN[j]=0) V <MY_TURN[i],i> < <MY_TURN[j],j>\n"
    				+ "(7) end for;\n"
    				+ "end operation");
    	}
    	theTxt.setFont(Font.font("Arial", 18.9));
    	algoTxt.getChildren().add(theTxt);
    	//Flush source code
    	sourceCodeFlush();
    	
    }
    
	//---------------------------------------------------------------------------------------------------------------------------//
	//---------------------------- FONCTIONS d'ACTION QUAND ON CLIQUE SUR UN BOUTON  --------------------------------------------//
	//---------------------------------------------------------------------------------------------------------------------------//

	// ---- Teste it ! ---- //
    public void loadCode() {
    	
    	String currentDir = System.getProperty("user.dir");
    	
		String sourcecode = currentDir + "\\src\\main\\resources\\org\\Algorithmes\\source.txt";
		String fichiercode = currentDir + "\\src\\main\\resources\\org\\Algorithmes\\"+algo+".txt";
		
		String code="";
		
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(fichiercode)))) {

			String line;
			while ((line = reader.readLine()) != null)
				code=code+line+"\n";
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try (FileWriter fw = new FileWriter(sourcecode)){
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(code);
			bw.flush();
			bw.close();
			System.out.print("saved\n");
		}catch (IOException e) {
			System.out.print("Oh no :o");
		}

		Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
        
    }
    
    // ---- Let's code it ! ---- //
    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
	public void transferMessage(String string) {
		algo = string;
	}
    
	//---------------------------------------------------------------------------------------------------------------------------//
	//------------------------------------ TOOLS  ----------------------------------------------------//
	//---------------------------------------------------------------------------------------------------------------------------//

	
    private void sourceCodeFlush() {
    	
    	String currentDir = System.getProperty("user.dir");
		String sourcecode = currentDir + "\\src\\main\\resources\\org\\Algorithmes\\source.txt";
		
		String code="Try to implemente "+algo+"\nGood Luck !";

		
		try (FileWriter fw = new FileWriter(sourcecode)){
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(code);
			bw.flush();
			bw.close();
			System.out.print("saved\n");
		}catch (IOException e) {
			System.out.print("Oh no :o");
		}
		
	}
	
}
