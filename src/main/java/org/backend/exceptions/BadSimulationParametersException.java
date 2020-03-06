package org.backend.exceptions;

/**
 * Denotes that a new simulation has missing, incorrect or incompatible parameters.
 * @author Hugo
 *
 */
public class BadSimulationParametersException extends BackEndException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BadSimulationParametersException(String errorMessage) {
		super(errorMessage);
	}
}
