package org.backend.exceptions;

/**
 * Denotes that the source code provided does no have the correct format and thus can't be read by the backend.
 * @author Hugo
 *
 */
public class BadSourceCodeException extends BackEndException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BadSourceCodeException(String errorMessage) {
		super(errorMessage);
	}
}
