package org.frontend;
 
import javafx.fxml.FXML;
//import javafx.scene.media.Media;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

import javafx.scene.control.Button;

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
	public Button labeldetest;

	@FXML
	public TextArea algoTxt;
	
	//---------------------------- VARIABLES GLOBALES --------------------------------------//

	public String algo;
	
	//---------------------------- Initialisation de la fen�tre --------------------------------------//

    public void initialize(String str) {

        labeldetest.setTooltip(new Tooltip("Tooltip for Button"));

    	algo = str;
    	System.out.print("\n Initialization " + algo+"\n");
    	//Initialise la fen�tre avec la bonne explication.
    	if(algo == "splitter") {
    		algoTxt.setText("Explication du splitter ici !");
    	}
    	else if(algo == "bakery") {
    		algoTxt.setText("Explication du bakery ici !");
    	}
    	
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
