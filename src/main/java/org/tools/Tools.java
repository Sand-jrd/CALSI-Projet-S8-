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
	
	//La fonction qui r?initialise l'execution
	public void customeAlertTool(String alertText) {

	 Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(alertText);
        alert.setResizable(true);
        alert.getDialogPane().setPrefSize(400, 100);
        alert.showAndWait();
	        
	}
	
}
