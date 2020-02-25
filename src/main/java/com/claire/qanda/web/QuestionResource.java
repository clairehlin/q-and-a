package com.claire.qanda.web;

import com.claire.qanda.model.OpenQuestion;
import com.claire.qanda.model.Question;
import com.claire.qanda.services.QuestionsService;
import com.claire.qanda.web.model.WebOpenQuestion;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/questions")
@Singleton
public class QuestionResource {

    private final QuestionsService questionsService;
    private final ObjectMapper objectMapper;

    public QuestionResource(QuestionsService questionsService, ObjectMapper objectMapper) {
        this.questionsService = questionsService;
        this.objectMapper = objectMapper;
        this.objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        this.objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listQuestions() throws JsonProcessingException {
        final List<Question> questions = questionsService.list();
        return objectMapper.writeValueAsString(questions);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addQuestions(WebOpenQuestion webOpenQuestion) {
        questionsService.addQuestion(
                new OpenQuestion(null, webOpenQuestion.statement, webOpenQuestion.answer)
        );
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getQuestions(@PathParam("id") Integer id) throws JsonProcessingException {
        return objectMapper.writeValueAsString(questionsService.get(id));

    }

    @DELETE
    @Path("{id}")
    public void deleteQuestionWithId(@PathParam("id") Integer id) throws JsonProcessingException {
        questionsService.deleteQuestionWithId(id);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public void updateOpenQuestion(
            @PathParam("id") Integer id,
            WebOpenQuestion webOpenQuestion
    ) {
        questionsService.updateOpenQuestion(
                new OpenQuestion(id, webOpenQuestion.statement, webOpenQuestion.answer)
        );
    }


}
