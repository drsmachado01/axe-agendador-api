package br.com.axe.agendadorapi.services.exceptions;

public class ObjectNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -5646519441011260865L;

	public ObjectNotFoundException(String message) {
        super(message);
    }
}

