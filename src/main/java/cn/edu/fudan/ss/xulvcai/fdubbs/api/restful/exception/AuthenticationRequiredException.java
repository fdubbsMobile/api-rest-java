package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception;

public class AuthenticationRequiredException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5334075937834812014L;

	public AuthenticationRequiredException(String message) {
		super(message);
	}
}
