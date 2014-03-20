package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http;


import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class ReusableHttpClient {

	private CloseableHttpClient httpclient;
	private long lastUsedTimestamp;
	private int userdCount;
	
	public ReusableHttpClient() {
		httpclient = HttpClients.createDefault();
		lastUsedTimestamp = System.currentTimeMillis();
		userdCount = 0;
	}
	
	public void touch() {
		lastUsedTimestamp = System.currentTimeMillis();
		userdCount++;
	}
}
