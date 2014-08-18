package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.handler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.LogoutResponse;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.BBSHostConstant;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginUtils;

public class LogoutResponseHandler implements ResponseHandler<LogoutResponse> {

	@Override
	public LogoutResponse handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		
		LogoutResponse logoutResponse = new LogoutResponse();
		
		boolean logoutSuccess = LoginUtils.isLoginOrLogoutSuccess(response
				.getStatusLine().getStatusCode());

		if (logoutSuccess) {
			logoutResponse.setResultCode(LogoutResponse.ResultCode.SUCCESS);
		} else {
			logoutResponse.setResultCode(LogoutResponse.ResultCode.INTERNAL_ERROR);
		}
		
		return logoutResponse;
	}
	
	public HttpGet getLogoutGetRequest() {
		URI uri = null;
		try {
			uri = new URIBuilder().setScheme("http")
					.setHost(BBSHostConstant.getHostName()).setPath("/bbs/logout")
					.build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (uri == null) {
			return new HttpGet("http://" + BBSHostConstant.getHostName()
					+ "/bbs/logout");
		} else {
			return new HttpGet(uri);
		}
	}

}
