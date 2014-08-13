package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common;

public class LoginInfo {

	private String userId;
	private String password;
	
	public LoginInfo(String userId, String password) {
		this.userId = userId;
		this.password = password;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public String getPassword() {
		return password;
	}
	
}
