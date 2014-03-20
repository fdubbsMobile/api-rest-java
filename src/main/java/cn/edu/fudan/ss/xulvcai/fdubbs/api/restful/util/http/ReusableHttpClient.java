package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http;


import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class ReusableHttpClient {

	private static long EXPIRE_INTERVAL = 15 * 60 * 1000; // 15 mins
	private CloseableHttpClient httpclient;
	private long lastUsedTimestamp;
	private int usedCount;
	private boolean isExclusive;
	
	public ReusableHttpClient() {
		httpclient = HttpClients.createDefault();
		lastUsedTimestamp = System.currentTimeMillis();
		usedCount = 0;
		isExclusive = false;
	}
	
	public CloseableHttpResponse executePost(HttpPost postRequest, HttpClientContext context) 
			throws ClientProtocolException, IOException {
		
		touch();
		return httpclient.execute(postRequest, context);
	}
	
	public CloseableHttpResponse excuteGet(HttpGet getRequest) 
			throws ClientProtocolException, IOException {
		
		touch();
		return httpclient.execute(getRequest);
	}
	
	public boolean isExpired() {
		long currentTime = System.currentTimeMillis();
		return currentTime > lastUsedTimestamp + EXPIRE_INTERVAL;
	}
	
	public void markAsExclusive() {
		isExclusive = true;
		touch();
	}
	
	public boolean isExclusive() {
		return isExclusive;
	}
	
	public int getTotalUsedCount() {
		return usedCount;
	}
	
	private void touch() {
		lastUsedTimestamp = System.currentTimeMillis();
		usedCount++;
	}
}
