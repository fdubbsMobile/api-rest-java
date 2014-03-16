package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.CookieKeyValuePair;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.LoginResponse;

@Path("user")
public class LoginSession {

	
	private static Logger logger = LoggerFactory.getLogger(LoginSession.class);
	private static CloseableHttpClient httpclient = HttpClients.createDefault();
	
	private static final String USER_NOT_EXIST_ERROR_MESSAGE = "找不到指定的用户";
	private static final String PASSWD_INCORRECT_ERROR_MESSAGE = "用户名和密码不匹配";
	
	@POST
	@Path("login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public LoginResponse doUserLogin(@FormParam("user_id") String user_id,
			@FormParam("passwd") String passwd) {
	
		logger.info(">>>>>>>>>>>>> Start doUserLogin <<<<<<<<<<<<<<");
		
		
		LoginResponse loginResponse = new LoginResponse();
		
		if(user_id == null || user_id.isEmpty()) {
			loginResponse.setResultCode(LoginResponse.ResultCode.USER_ID_EMPTY);
			loginResponse.setErrorMessage("User Id should not be empty!");
		}
		else if(passwd == null || passwd.isEmpty()) {
			loginResponse.setResultCode(LoginResponse.ResultCode.PASSWD_EMPTY);
			loginResponse.setErrorMessage("Password should not be empty!");
		}
		else {
			try {
				loginResponse = postLoginRequest(user_id, passwd);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				loginResponse.setResultCode(LoginResponse.ResultCode.INTERNAL_ERROR);
				loginResponse.setErrorMessage("Internal Error!");
			}
		}
		
		
		logger.info(">>>>>>>>>>>>> End doUserLogin <<<<<<<<<<<<<<");
		return loginResponse;
	}
	
	private LoginResponse postLoginRequest(String user_id, String passwd) throws IOException {
		LoginResponse result = new LoginResponse();
		
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("id", user_id));
		formparams.add(new BasicNameValuePair("pw", passwd));
		
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
		
		HttpPost httpPost = new HttpPost("http://bbs.fudan.edu.cn/bbs/login");
		httpPost.setEntity(entity);
		
		HttpClientContext context = HttpClientContext.create();
		CloseableHttpResponse postResponse = httpclient.execute(httpPost, context);
		
		boolean loginSuccess = isLoginSuccess(postResponse);
		logger.debug("Login successful : " + loginSuccess);
		
		if(loginSuccess) {
			List<CookieKeyValuePair> cookies = getCookiePairsFromContext(context);
			result.setResultCode(LoginResponse.ResultCode.SUCCESS);
			result.setCookies(cookies);
		}else{
			String errorMessage = getErrorMessageFromResponse(postResponse);
			if(USER_NOT_EXIST_ERROR_MESSAGE.equals(errorMessage)) {
				result.setResultCode(LoginResponse.ResultCode.USER_NOT_EXIST);
				result.setErrorMessage(USER_NOT_EXIST_ERROR_MESSAGE);
			}
			else if(PASSWD_INCORRECT_ERROR_MESSAGE.equals(errorMessage)) {
				result.setResultCode(LoginResponse.ResultCode.PASSWD_INCORRECT);
				result.setErrorMessage(PASSWD_INCORRECT_ERROR_MESSAGE);
			}
			else {
				result.setResultCode(LoginResponse.ResultCode.INTERNAL_ERROR);
				result.setErrorMessage("Internal Error!");
			}
		}
		
		postResponse.close();
		
		return result;
	}
	
	private boolean isLoginSuccess(CloseableHttpResponse response) {
		int status = response.getStatusLine().getStatusCode();
		
		if(HttpStatus.SC_MOVED_TEMPORARILY == status) {
			return true;
		}
		
		/*
		if(HttpStatus.SC_OK == status) {
			return false;
		}
		*/
		return false;
	}
	
	private List<CookieKeyValuePair> getCookiePairsFromContext(HttpClientContext context) {
		List<CookieKeyValuePair> cookiePairs = new ArrayList<CookieKeyValuePair>();
		List<Cookie> cookies = context.getCookieStore().getCookies();
		for(Cookie cookie : cookies) {
			CookieKeyValuePair cookiePair = new CookieKeyValuePair();
			cookiePair.setCookieName(cookie.getName());
			cookiePair.setCookieValue(cookie.getValue());
			cookiePairs.add(cookiePair);
			logger.debug("Name : " + cookie.getName() + "; Value : " + cookie.getValue()); 
		}
		return cookiePairs;
	}
	
	private String getErrorMessageFromResponse(CloseableHttpResponse response) throws ParseException, IOException {
		HttpEntity responseEntity = response.getEntity();
		if(responseEntity != null) {
			String contentAsString = EntityUtils.toString(responseEntity);
			Document doc = Jsoup.parse(contentAsString);
			Element bodyContent = doc.select("body > div").first();
			logger.debug("Body Content : " + bodyContent.text());
			return bodyContent.text();
		}
		return null;
	}
}
