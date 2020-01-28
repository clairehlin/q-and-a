package com.claire.qanda;

import com.claire.qanda.repository.SimpleQuestionRepository;
import com.claire.qanda.services.SimpleQuestionsService;
import com.claire.qanda.web.QuestionResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import static java.util.stream.Collectors.joining;

public class QuestionsApplication {
    public static void main(String... args) throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        Server jettyServer = new Server(8080);
        jettyServer.setHandler(context);

        context.addServlet(
                new ServletHolder(
                        new ServletContainer(
                                new QuestionResourceConfig()
                        )
                ),
                "/*"
        );

        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }
    }

    private static class QuestionResourceConfig extends ResourceConfig {
        QuestionResourceConfig() {
            register(questionResource());
        }
    }

    private static QuestionResource questionResource() {
        return new QuestionResource(
                new SimpleQuestionsService(
                        new SimpleQuestionRepository()
                ),
                new ObjectMapper()
        );
    }
}
