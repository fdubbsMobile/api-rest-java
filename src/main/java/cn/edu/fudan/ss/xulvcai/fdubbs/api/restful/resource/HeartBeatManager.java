package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.resource;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.annotation.Generated;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonProperty;


@Path("/heartBeat")
public class HeartBeatManager {

	public static long requestCount = 0;
	
	@GET
	@Path("/live")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHeartBeat() {
		requestCount++;

		ServerHeartBeat heartBeat = new ServerHeartBeat()
											.withServerName("jboss-rest-api-for-fdubbs")
											.withRequestCount(requestCount);
		return Response.ok().entity(heartBeat).build();
	}
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "server_name",
    "date",
    "request_count",
    "error_info",
    "local_time_zone",
    "local_hour",
    "shanghai_hour"
})
class ServerHeartBeat {
	@JsonProperty("server_name")
	private String serverName;
	
	@JsonProperty("date")
	private Date date;
	
	@JsonProperty("request_count")
	private long requestCount;
	
	@JsonProperty("error_info")
	private String errorInfo;
	
	@JsonProperty("local_time_zone")
	private String localTimeZone;
	
	@JsonProperty("local_hour")
	private int localHour;
	
	@JsonProperty("shanghai_hour")
	private int shanghaiHour;
	
	private Calendar calendar;
	ServerHeartBeat() {
		date = new Date();
		calendar = Calendar.getInstance();
		localTimeZone = calendar.getTimeZone().getID();
		localHour = calendar.get(Calendar.HOUR_OF_DAY);
		calendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		shanghaiHour = calendar.get(Calendar.HOUR_OF_DAY);
	}
	
	@JsonProperty("error_info")
    public String getErrorInfo() {
        return errorInfo;
    }
	
	@JsonProperty("error_info")
    public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
    }
	
	public ServerHeartBeat withErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
        return this;
    }
	
	@JsonProperty("server_name")
    public String getServerName() {
        return serverName;
    }
	
	@JsonProperty("server_name")
    public void setServerName(String serverName) {
		this.serverName = serverName;
    }
	
	public ServerHeartBeat withServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }
	
	@JsonProperty("date")
    public Date getDate() {
        return date;
    }
	
	@JsonProperty("date")
    public void setDate(Date date) {
        this.date = date;
    }
	
	public ServerHeartBeat withDate(Date date) {
		this.date = date;
        return this;
    }
	
	@JsonProperty("request_count")
    public long getRequestCount() {
        return requestCount;
    }
	
	@JsonProperty("request_count")
    public void setRequestCount(long requestCount) {
		this.requestCount = requestCount;
    }
	
	public ServerHeartBeat withRequestCount(long requestCount) {
        this.requestCount = requestCount;
        return this;
    }
	
	@JsonProperty("local_time_zone")
	public String getLocalTimeZone() {
		return localTimeZone;
	}
	
	@JsonProperty("local_time_zone")
	public void setLocalTimeZone(String localTimeZone) {
		this.localTimeZone = localTimeZone;
	}
	
	public ServerHeartBeat withLocalTimeZone(String localTimeZone) {
		this.localTimeZone = localTimeZone;
        return this;
    }
	
	@JsonProperty("local_hour")
    public int getLocalHour() {
        return localHour;
    }
	
	@JsonProperty("local_hour")
    public void setLocalHour(int localHour) {
		this.localHour = localHour;
    }
	
	public ServerHeartBeat withLocalHour(int localHour) {
		this.localHour = localHour;
        return this;
    }
	
	@JsonProperty("shanghai_hour")
    public int getShanghaiHour() {
        return shanghaiHour;
    }
	
	@JsonProperty("shanghai_hour")
    public void setShanghaiHour(int shanghaiHour) {
		this.shanghaiHour = shanghaiHour;
    }
	
	public ServerHeartBeat withShanghaiHour(int shanghaiHour) {
		this.shanghaiHour = shanghaiHour;
        return this;
    }
	
}
