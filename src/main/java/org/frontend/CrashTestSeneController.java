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
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.geometry.Insets;
import javafx.scene.canvas.GraphicsContext;

/**
 * @author renon
 *
 */
/**
 * @author renon
 *
 */

public class CrashTestSeneController {

    @FXML private Canvas img ;
    
    private GraphicsContext gc ;
    
    @FXML private void drawCanvas(ActionEvent event) {

    }
    
    public void initialize() {
        gc = img.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        gc.fillRect(50, 50, 100, 100);
    }
	
}
