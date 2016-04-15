package de.clashofcubes.webinterface.servermanagement.serverfiles.exceptions;

public class ServerException extends RuntimeException {

	private static final long serialVersionUID = 3528078479774120301L;

	public ServerException(String msg) {
		super(msg);
	}

	public ServerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServerException(Throwable cause) {
		super(cause);
	}

}