package org.frontend;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import java.io.*;
import javafx.scene.image.ImageView;
import java.text.DecimalFormat;
import javafx.stage.FileChooser;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
/**
 * @author renon
 *
 */
/**
 * @author renon
 *
 */

public class EditController {
	
	
	@FXML
	private TextArea shed;
	
	
	public void initialize(String ShedString) {
		
		if (ShedString != "") {
			shed.setText(ShedString);
		}
		else {
			shed.setText("Vous n'avez pas chargé de Sheduler.");
		}
		
		
	}
	
	public String getShed() {
		if (shed.getText().toString() == "Vous n'avez pas chargé de Sheduler.") {
			return "";
		}
		else {
			return shed.getText().toString();
		}
		
	}

	
}

