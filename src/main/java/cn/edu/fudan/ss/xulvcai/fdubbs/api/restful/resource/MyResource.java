package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.resource;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.Hello;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo.UserAccount;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Hello, My Precious!";
    }
    
    @POST
    @Path("hello")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Hello sayHello(UserAccount user) {
    	if(user == null) {
    		Hello response = new Hello();
    		response.setMessage("Hello,  Anonymous!");
    		return response;
    	}
    	
    	System.out.println("User : "+ user.getName());
    	Hello  hello = new Hello();
    	hello.setMessage("Hello, " + user.getName() + "!");
    	return hello;
    }
}
