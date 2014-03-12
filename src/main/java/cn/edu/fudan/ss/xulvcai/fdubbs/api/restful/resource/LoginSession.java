package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.LoginResponse;

@Path("user")
public class LoginSession {

	
	
	@POST
	@Path("login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public LoginResponse doUserLogin(@FormParam("user_id") String user_id,
			@FormParam("passwd") String passwd) {
	
		
		LoginResponse response = new LoginResponse();
		response.setResultCode(LoginResponse.ResultCode.SUCCESS);
		
		return response;
	}
}
