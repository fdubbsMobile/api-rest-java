package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http;

import java.util.HashMap;

public class HttpClientManager {

	private HashMap httpClientCache;
	
	private HttpClientManager() {
		
	}
	
	public HttpClientManager getInstance() {
		return new HttpClientManager();
	}
}
