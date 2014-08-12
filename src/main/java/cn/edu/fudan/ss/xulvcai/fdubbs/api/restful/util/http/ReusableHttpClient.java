package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http;


import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReusableHttpClient {

    private static Logger logger = LoggerFactory.getLogger(ReusableHttpClient.class);
    
    private static boolean use_proxy = false;
    
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
	
	public CloseableHttpResponse execute(final HttpHost target, final HttpRequest request,
            final HttpContext context) throws IOException, ClientProtocolException {
		touch();
        return httpclient.execute(target, request, context);
    }
	
	public CloseableHttpResponse execute(
            final HttpUriRequest request,
            final HttpContext context) throws IOException, ClientProtocolException {
		touch();
        return httpclient.execute(request, context);
    }
	
	public CloseableHttpResponse execute(
            final HttpUriRequest request) throws IOException, ClientProtocolException {
		touch();
        return httpclient.execute(request, (HttpContext) null);
    }
	
	public CloseableHttpResponse execute(
            final HttpHost target,
            final HttpRequest request) throws IOException, ClientProtocolException {
		touch();
        return httpclient.execute(target, request, (HttpContext) null);
    }
	
	public <T> T execute(final HttpUriRequest request,
            final ResponseHandler<? extends T> responseHandler) throws IOException,
            ClientProtocolException {
		touch();
        return httpclient.execute(request, responseHandler, null);
    }
	
	public <T> T execute(final HttpUriRequest request,
            final ResponseHandler<? extends T> responseHandler, final HttpContext context)
            throws IOException, ClientProtocolException {
		touch();
        return httpclient.execute(request, responseHandler, context);
    }
	
	
	public <T> T execute(final HttpHost target, final HttpRequest request,
            final ResponseHandler<? extends T> responseHandler) throws IOException,
            ClientProtocolException {
		touch();
        return httpclient.execute(target, request, responseHandler, null);
    }
	
	public <T> T execute(final HttpHost target, final HttpRequest request,
            final ResponseHandler<? extends T> responseHandler, final HttpContext context)
            throws IOException, ClientProtocolException {
		touch();
		return httpclient.execute(target, request, responseHandler, context);
    }
	
	
	public CloseableHttpResponse executePost(HttpPost postRequest, HttpClientContext context) 
			throws ClientProtocolException, IOException {
		
		touch();
		return httpclient.execute(postRequest, context);
	}
	
	public CloseableHttpResponse excuteGet(HttpGet getRequest) 
			throws ClientProtocolException, IOException {
		
		touch();
		if (use_proxy) {
            HttpHost proxy = new HttpHost("10.249.125.35", 8080, "http");
            RequestConfig config = RequestConfig.custom()
                    .setProxy(proxy)
                    .build();
            getRequest.setConfig(config);
            logger.info(">>>>>>>>>>>>> Adding proxy <<<<<<<<<<<<<<");
        }
		return httpclient.execute(getRequest);
	}
	
	public void close() {
		try {
			httpclient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
