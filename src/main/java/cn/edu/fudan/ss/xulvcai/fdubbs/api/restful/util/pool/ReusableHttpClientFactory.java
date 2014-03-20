package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.pool;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;

/**
 * A factory that generates ReusableHttpClient
 * @author hidennis
 */
public class ReusableHttpClientFactory implements ResourceFactory<ReusableHttpClient> {

	@Override
	public ReusableHttpClient createResource() {
		// TODO Auto-generated method stub
		return new ReusableHttpClient();
	}

	@Override
	public boolean validateResource(ReusableHttpClient resource) {
		return !resource.isExclusive();
	}



}
