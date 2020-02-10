package org.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.*;
import java.math.RoundingMode;

import org.backend.BackEndException;
import org.backend.BadSourceCodeException;
import org.backend.Infos;
import org.backend.RipException;
import org.backend.Simulation;
import org.backend.SimulationBuilder;
import org.backend.VariableInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.text.Text;

import org.tools.Tools;
import bsh.EvalError;

/**
 * This class will saved all the states simulation will come true. Hence, We will be able to StepBack and Save a simulation.
 * @author Sandrine Juillard
 *
 */
public class History {
	
	private	ArrayList<Text> textForProcess;
	
	public History(ArrayList<Text> textForProcess) {
		
		this.textForProcess = textForProcess;
		
	}
	
	
}