package org.backend.exceptions;

/**
 * Parent class for our custom backend exceptions.
 * @author Hugo
 *
 */
public class BackEndException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BackEndException(String errorMessage) {
		super(errorMessage);
	}
}
