package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.MyResource;

public class MyResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(MyResource.class);
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void testGetIt() {
        final String responseMsg = target().path("myresource").request().get(String.class);

        assertEquals("Hello, My Precious!", responseMsg);
    }
    
    @Test
    public void testHello() {
    	Hello  hello = new Hello();
    	hello.setMessage("Hello, buddy!");
    	final Hello responseMsg = target().path("myresource/hello").request().get(Hello.class);
    	assertTrue("Faill to test Hello", hello.equals(responseMsg));
    	
    }
}
