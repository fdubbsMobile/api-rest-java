package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util;

import javax.ws.rs.core.Response.StatusType;

import org.glassfish.jersey.message.internal.Statuses;


public final class ResponseStatus {

	public static final StatusType SERVER_INTERNAL_ERROR_STATUS = Statuses.from(600, "Server Internal Error!");
	public static final StatusType PAMAMETER_ERROR_STATUS = Statuses.from(601, "Wrong Parameter Error!");
	
	private ResponseStatus() {
		
	}
}
