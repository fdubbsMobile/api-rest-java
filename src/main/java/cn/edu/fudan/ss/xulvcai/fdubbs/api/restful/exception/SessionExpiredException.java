package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception;

public class SessionExpiredException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -847218894917743286L;

	public SessionExpiredException(String message) {
		super(message);
	}
}
