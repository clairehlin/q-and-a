package com.claire.qanda;

import com.claire.qanda.web.QuestionResource;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import java.io.File;

public class QuestionsApplication {
    public static void main(String... args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        Context context = tomcat.addWebapp("", new File(".").getAbsolutePath());
        Tomcat.addServlet(context, "jersey-container-servlet", resources());
        context.addServletMappingDecoded("/*", "jersey-container-servlet");

        tomcat.start();
        tomcat.getServer().await();
    }

    private static ServletContainer resources() {
        return new ServletContainer(new ResourceConfig(QuestionResource.class));
    }
}
