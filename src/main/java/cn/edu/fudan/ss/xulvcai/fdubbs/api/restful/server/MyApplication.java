package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.server;

import javax.ws.rs.ApplicationPath;

import org.apache.log4j.PropertyConfigurator;
import org.glassfish.jersey.server.ResourceConfig;


@ApplicationPath("/api/v1/*")
public class MyApplication extends ResourceConfig {

	public MyApplication() {
		ClassLoader classLoader=MyApplication.class.getClassLoader();
		String path = classLoader.getResource("log4j.properties").getPath();
		System.out.println("Path is "+path);
		PropertyConfigurator.configure(path);
		
        setApplicationName("RESTful API for fdubbs");
        packages("cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.resource");
        
    }
}
