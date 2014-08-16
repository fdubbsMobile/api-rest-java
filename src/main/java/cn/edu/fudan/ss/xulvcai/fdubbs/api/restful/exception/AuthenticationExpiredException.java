package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception;

public class AuthenticationExpiredException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -847218894917743286L;

	public AuthenticationExpiredException(String message) {
		super(message);
	}
}
