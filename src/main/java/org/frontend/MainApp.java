package org.frontend;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;


public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }
	
    @Override
    public void start(Stage stage) throws Exception {
    	
    	
    	//Affichage de la fenêtre principale
        Parent root = FXMLLoader.load(getClass().getResource("scene.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        
        stage.setTitle("CALSI");
        stage.setScene(scene);
        stage.sizeToScene();
        System.out.print("main class");
        stage.show();
        
        
        //Affichage de la fenêtre Welcome
        
        Parent secondroot = FXMLLoader.load(getClass().getResource("welcome.fxml"));
        Scene secondscene = new Scene(secondroot);
        
        Stage secondStage = new Stage();
        secondStage.setTitle("Welcome !");
        secondStage.setScene(secondscene);
        secondStage.show();
        
        //Affichage fenetre CrashTest. Mettre en commentaire if not used
        
        Parent crashroot = FXMLLoader.load(getClass().getResource("crashTestSene.fxml"));
        Scene crashscene = new Scene(crashroot);

        Stage crashStage = new Stage();
        crashStage.setTitle("Crash Test !");
        crashStage.setScene(crashscene);
        crashStage.show();
    }


}
