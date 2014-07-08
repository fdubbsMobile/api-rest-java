package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common;

import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;


public enum RESTErrorStatus implements StatusType {

	REST_SERVER_INTERNAL_ERROR_STATUS(600, "REST Server Internal Error!"),
	REST_SERVER_PAMAMETER_ERROR_STATUS(601, "Wrong Parameter Error!"),
	REST_SERVER_REQUEST_CONTENT_ERROR_STATUS(602, "Bad Request Content!");;
	
	private final int code;
    private final String reason;
    private Family family;



    RESTErrorStatus(final int statusCode, final String reasonPhrase)
    {
       this.code = statusCode;
       this.reason = reasonPhrase;
       switch (code / 100)
       {
          
       }
    }


    /**
     * Get the associated status code
     *
     * @return the status code
     */
    public int getStatusCode()
    {
       return code;
    }

    /**
     * Get the reason phrase
     *
     * @return the reason phrase
     */
    public String getReasonPhrase()
    {
       return toString();
    }

    /**
     * Get the reason phrase
     *
     * @return the reason phrase
     */
    @Override
    public String toString()
    {
       return reason;
    }

    /**
     * Convert a numerical status code into the corresponding Status
     *
     * @param statusCode the numerical status code
     * @return the matching Status or null is no matching Status is defined
     */
    public static RESTErrorStatus fromStatusCode(final int statusCode)
    {
       for (RESTErrorStatus s : RESTErrorStatus.values())
       {
          if (s.code == statusCode)
          {
             return s;
          }
       }
       return null;
    }

	@Override
	public Family getFamily() {
		return this.family;
	}
 }
