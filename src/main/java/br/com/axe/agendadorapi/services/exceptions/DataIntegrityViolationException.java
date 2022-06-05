package br.com.axe.agendadorapi.services.exceptions;

public class DataIntegrityViolationException extends RuntimeException {
	private static final long serialVersionUID = 2383324248767440737L;

	public DataIntegrityViolationException(String message) {
		super(message);
	}

}
