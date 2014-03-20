package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http;


import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.resource.SectionManager;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.pool.ReusableHttpClientFactory;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.pool.ReusableHttpClientPool;

public /*enum*/ class HttpClientManager {

	private static Logger logger = LoggerFactory.getLogger(HttpClientManager.class);
	
	/*SINGLE_INSTANCE;*/
	private static final HttpClientManager SINGLE_INSTANCE =  new HttpClientManager();
	
	private final int POOL_CAPACITY = 50;
	private final ReusableHttpClientPool anonymousClientPool;
	private final ReusableHttpClientFactory httpClientFactory;
	private final ConcurrentHashMap<String, ReusableHttpClient> httpClientCache;

	
	
	private HttpClientManager() {
		httpClientFactory = new ReusableHttpClientFactory();
		anonymousClientPool = new ReusableHttpClientPool(httpClientFactory, POOL_CAPACITY);
		httpClientCache = new ConcurrentHashMap<String, ReusableHttpClient>();
	}
	
	public static HttpClientManager getInstance() {
		return SINGLE_INSTANCE;
	}
	
	public ReusableHttpClient getAnonymousClient() {
		return anonymousClientPool.getResource();
	}
	
	public void releaseReusableHttpClient(ReusableHttpClient httpClient) {
		
		if(httpClient == null || httpClient.isExclusive())
			return;
		
		anonymousClientPool.returnResource(httpClient);
	}
	
	
	public ReusableHttpClient getAuthClient(String authCode) {
		if(authCode != null && httpClientCache.containsKey(authCode)) {
			return httpClientCache.get(authCode);
		}
		
		return null;
	}
	
	public void markClientAsAuth(String authCode, ReusableHttpClient httpClient) {
		httpClient.markAsExclusive();
		httpClientCache.put(authCode, httpClient);
		logger.info("Add client for authCode<"+authCode+"> ...");
		anonymousClientPool.returnResource(httpClient);
	}
	
	public void disableClientForAuthCode(String authCode) {
		if(authCode != null && httpClientCache.containsKey(authCode)) {
			httpClientCache.remove(authCode);
			logger.info("Remove client for authCode<"+authCode+"> ...");
		}
	}
	
}
