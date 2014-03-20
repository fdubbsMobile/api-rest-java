package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http;


import java.util.concurrent.ConcurrentHashMap;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.pool.ReusableHttpClientFactory;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.pool.ReusableHttpClientPool;

public /*enum*/ class HttpClientManager {

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
	
	public void returnAnonymousClient(ReusableHttpClient httpClient) {
		anonymousClientPool.returnResource(httpClient);
	}
	
	
	public ReusableHttpClient getAuthClient(String authCode) {
		if(httpClientCache.containsKey(authCode)) {
			return httpClientCache.get(authCode);
		}
		
		return null;
	}
	
	public void markClientAsAuth(String authCode, ReusableHttpClient httpClient) {
		httpClient.markAsExclusive();
		httpClientCache.put(authCode, httpClient);
		anonymousClientPool.returnResource(httpClient);
	}
	
}
