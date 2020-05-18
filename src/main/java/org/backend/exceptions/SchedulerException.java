package org.backend.exceptions;


public class SchedulerException extends Exception {

	private static final long serialVersionUID = 1L;

	public SchedulerException(String errorMessage) {
		super(errorMessage);
	}
}