package org.tools;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Tools implements Cloneable {
	
	public static String getContentOfFile(String fileName) throws IOException,FileNotFoundException {
		StringBuilder contentBuilder = new StringBuilder();
	    try (Stream<String> stream = Files.lines( Paths.get(fileName), StandardCharsets.UTF_8)) 
	    {
	        stream.forEach(s -> contentBuilder.append(s).append("\n"));
	    }
	    return contentBuilder.toString();
	}
	
	// Créaction d'une fenêtre d'alerte
	public void customeAlertTool(String alertText) {

	 Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(alertText);
        alert.setResizable(true);
        alert.getDialogPane().setPrefSize(400, 100);
        alert.showAndWait();
	        
	}
	
	// Permet de vérifier si une ligne de code réfère bien à un certaine object
	public static boolean lineReallyContainObject (String codeLine, String objName) {
		// TODO Vérifier que codeLine contient objName, et que les caractère avant et après ne sont ni un nombre ni une lettre (majucule incluse). 
		int pos = codeLine.indexOf(objName);
		if(pos>0 && (Character.isLetter(codeLine.charAt(pos-1)) || Character.isDigit(codeLine.charAt(pos-1)))) {
			return false;
		}
		if(pos+objName.length()<codeLine.length() && (Character.isLetter(codeLine.charAt(pos+objName.length())) || Character.isDigit(codeLine.charAt(pos+objName.length())))) {
			return false;
		}
		return true;
	}
	
	
}
