package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
    
    @GET
    @Path("hello")
    @Produces(MediaType.APPLICATION_JSON)
    public Hello sayHello() {
    	Hello  hello = new Hello();
    	hello.setMessage("Hello, buddy!");
    	return hello;
    }
}
