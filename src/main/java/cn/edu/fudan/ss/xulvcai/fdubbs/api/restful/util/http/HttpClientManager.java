package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.AuthenticationExpiredException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.exception.AuthenticationRequiredException;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common.LoginInfo;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.pool.ReusableHttpClientFactory;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.pool.ReusableHttpClientPool;

public/* enum */class HttpClientManager {

	private static Logger logger = LoggerFactory
			.getLogger(HttpClientManager.class);

	/* SINGLE_INSTANCE; */
	private static final HttpClientManager SINGLE_INSTANCE = new HttpClientManager();

	private final int POOL_CAPACITY = 10;
	private final int DEFAULT_CHECK_INTERVAL = 5 * 60 * 1000; // 5mins in
																// milliseconds
	private final ReusableHttpClientPool anonymousClientPool;
	private final ReusableHttpClientFactory httpClientFactory;
	private final ConcurrentHashMap<String, ReusableHttpClient> authClientCache;
	private final ConcurrentHashMap<String, LoginInfo> authLoginInfoCache;
	private final IdleClientFinalizer idleClientFinalizer;

	private HttpClientManager() {
		httpClientFactory = new ReusableHttpClientFactory();
		anonymousClientPool = new ReusableHttpClientPool(httpClientFactory,
				POOL_CAPACITY);
		authClientCache = new ConcurrentHashMap<String, ReusableHttpClient>();
		authLoginInfoCache = new ConcurrentHashMap<String, LoginInfo>();

		// create the idle client finalize thread
		idleClientFinalizer = new IdleClientFinalizer(DEFAULT_CHECK_INTERVAL);
		Thread idleClientFinalizationThread = new Thread(idleClientFinalizer);
		idleClientFinalizationThread.start();
	}

	public static HttpClientManager getInstance() {
		return SINGLE_INSTANCE;
	}

	private ReusableHttpClient getAnonymousClient() {
		return anonymousClientPool.getResource();
	}

	public void releaseReusableHttpClient(ReusableHttpClient httpClient) {

		if (httpClient == null || httpClient.isExclusive())
			return;

		anonymousClientPool.returnResource(httpClient);
	}

	public ReusableHttpClient getReusableClient(String authCode,
			boolean allowAnony) {

		ReusableHttpClient reusableClient = null;

		if (authCode != null) {
			reusableClient = HttpClientManager.getInstance().getAuthClient(
					authCode);
		}

		if (reusableClient == null) {
			if (!allowAnony) {
				logger.error("reusableClient is null! You need to login");

				if (authCode == null || authCode.isEmpty()) {
					throw new AuthenticationRequiredException(
							"Auth Code is empty!");
				} else {
					throw new AuthenticationExpiredException("Auth Code<"
							+ authCode + "> has been expired!");
				}
			}
			reusableClient = HttpClientManager.getInstance()
					.getAnonymousClient();
		}

		return reusableClient;
	}

	private ReusableHttpClient getAuthClient(String authCode) {
		if (authCode != null && authClientCache.containsKey(authCode)) {
			return authClientCache.get(authCode);
		}

		return null;
	}

	public LoginInfo getAuthLoginInfo(String authCode) {
		if (authCode != null && authLoginInfoCache.containsKey(authCode)) {
			return authLoginInfoCache.get(authCode);
		}

		return null;
	}

	public void markClientAsAuth(String authCode,
			ReusableHttpClient httpClient, LoginInfo info) {
		httpClient.markAsExclusive();
		authClientCache.put(authCode, httpClient);
		authLoginInfoCache.put(authCode, info);
		logger.info("Add client for authCode<" + authCode + "> ...");
		anonymousClientPool.returnResource(httpClient);

	}

	public void disableClientForAuthCode(String authCode) {
		if (authCode != null && authClientCache.containsKey(authCode)) {
			authClientCache.remove(authCode);
			logger.info("Remove client for authCode<" + authCode + "> ...");
		}
	}

	public void finalizeAuthCilent(String authCode,
			ReusableHttpClient httpClient) {
		disableClientForAuthCode(authCode);
		httpClient.close();
	}

	@Override
	protected void finalize() throws Throwable {
		// Disable the idle client finalize thread
		idleClientFinalizer.disable();

		super.finalize();
	}

	private class IdleClientFinalizer implements Runnable {

		// The number of milliseconds we'll sleep between checks
		private int sleepTimeMS;

		IdleClientFinalizer(int idleClientCheckInterval) {
			this.sleepTimeMS = idleClientCheckInterval;
		}

		private boolean enabled = true;

		public void run() {
			while (enabled) {
				Collection<String> keySet = authClientCache.keySet();

				for (String key : keySet) {
					ReusableHttpClient authClient = authClientCache.get(key);
					if (authClient.isExpired()) {
						logger.info("HttpClient for auth_code" + key
								+ "is EXPIRED !");
						authClientCache.remove(key);
						authClient.close();
					}
				}

				try {
					Thread.sleep(sleepTimeMS);
				} catch (InterruptedException e) {
					// Nothing we can do
				}
			}
		}

		public void disable() {
			enabled = false;
		}
	}

}
