package org.backend.exceptions;

/**
 * General exception used to denote an application breaking bug in the backend.
 * @author Hugo
 *
 */
public class RipException extends BackEndException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RipException(String errorMessage) {
		super(errorMessage);
	}
}
