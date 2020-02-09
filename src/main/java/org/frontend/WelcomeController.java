package org.frontend;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import java.text.DecimalFormat;
import java.math.RoundingMode;
import javafx.stage.FileChooser;
import javafx.scene.control.MenuItem;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.text.*;

import org.backend.BackEndException;
import org.backend.BadSourceCodeException;
import org.backend.Infos;
import org.backend.RipException;
import org.backend.Simulation;
import org.backend.SimulationBuilder;
import org.backend.VariableInfo;

import javafx.scene.control.TextArea;
import java.io.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.TextFlow;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * @author renon
 *
 */
/**
 * @author renon
 *
 */

public class WelcomeController {

	@FXML
	private Text Welcometexte;

	@FXML
	private Text Intstructiontexte;
	
	@FXML
	private Text InstructionTitle;
	
	private String code="import java.lang.Math; 	//	All import have to be made without any blank line\r\n" + 
			"\r\n" + 
			"// // Shared variables declaration		//	All declaration have to be made without any blank line\r\n" + 
			"Integer shared1;\r\n" + 
			"Boolean[] shared2;\r\n" + 
			"\r\n" + 
			"// // Shared variables initialization		//	All initialization have to be made without any blank line\r\n" + 
			"flag[0] = false;\r\n" + 
			"flag[1] = false;\r\n" + 
			"\r\n" + 
			"// // Local variables declaration		//	All declaration have to be made without any blank line\r\n" + 
			"int j;\r\n" + 
			"boolean a;\r\n" + 
			"\r\n" + 
			"// //Example algorithm		//	You can write your algorithm using Java Syntax, blank line are allowed\r\n" + 
			"flag[i] = true;\r\n" + 
			"while ( a == true && b == j) {		//	The opening accolade \"{\" have to be in the same line than the condition\r\n" + 
			"	toto=test\r\n" + 
			"}		//	The closing accolade \"}\" have to be alone on the line\r\n" + 
			"// critical section \r\n" + 
			"1\r\n" + 
			"// end of critical section\r\n" + 
			"flag[i] = false;";
	
	public void initialize() {
		Intstructiontexte.setText(code);
	}

	
}
