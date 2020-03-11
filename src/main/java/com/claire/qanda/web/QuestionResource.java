package com.claire.qanda.web;

import com.claire.qanda.model.MultipleChoiceQuestion;
import com.claire.qanda.model.OpenQuestion;
import com.claire.qanda.model.Question;
import com.claire.qanda.model.SimpleTrueOrFalseQuestion;
import com.claire.qanda.services.QuestionsService;
import com.claire.qanda.web.model.WebQuestion;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Path("/questions")
@Singleton
public class QuestionResource {

    private final QuestionsService questionsService;
    private final ObjectMapper objectMapper;
    private final Map<String, Consumer<WebQuestion>> addQuestionOperations;
    private final Map<String, Consumer<WebQuestion>> updateQuestionOperations;

    public QuestionResource(QuestionsService questionsService, ObjectMapper objectMapper) {
        this.questionsService = questionsService;
        this.objectMapper = objectMapper;
        this.objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        this.objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        this.addQuestionOperations = addOperations(questionsService);
        this.updateQuestionOperations = updateOperations(questionsService);
    }

    private Map<String, Consumer<WebQuestion>> addOperations(QuestionsService questionsService) {
        Map<String, Consumer<WebQuestion>> addQuestionOperations = new HashMap<>();
        addQuestionOperations.put("OpenQuestion", new AddOpenQuestion(questionsService));
        addQuestionOperations.put("TrueOrFalseQuestion", new AddTrueOrFalseQuestion(questionsService));
        addQuestionOperations.put("MultipleChoiceQuestion", new AddMultipleChoiceQuestion(questionsService));
        return addQuestionOperations;
    }

    private Map<String, Consumer<WebQuestion>> updateOperations(QuestionsService questionsService) {
        Map<String, Consumer<WebQuestion>> updateQuestionOperations = new HashMap<>();
        updateQuestionOperations.put(OpenQuestion.class.getName(), new UpdateOpenQuestion(questionsService));
        updateQuestionOperations.put(SimpleTrueOrFalseQuestion.class.getName(), new AddTrueOrFalseQuestion(questionsService));
        updateQuestionOperations.put(MultipleChoiceQuestion.class.getName(), new AddMultipleChoiceQuestion(questionsService));
        return updateQuestionOperations;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listQuestions() throws JsonProcessingException {
        final List<Question> questions = questionsService.list();
        return objectMapper.writeValueAsString(questions);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getQuestions(@PathParam("id") Integer id) throws JsonProcessingException {
        return objectMapper.writeValueAsString(questionsService.get(id));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addQuestions(WebQuestion webQuestion, @QueryParam("questionType") String questionType) {
        this.addQuestionOperations.getOrDefault(questionType, new FailedAddQuestion(questionType))
                .accept(webQuestion);
    }

    @DELETE
    @Path("{id}")
    public void deleteQuestionWithId(@PathParam("id") Integer id) {
        questionsService.deleteQuestionWithId(id);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public void updateQuestion(
            @PathParam("id") Integer id,
            WebQuestion webQuestion
    ) {
        String questionType = questionsService.get(id).getClass().getName();
        this.updateQuestionOperations.getOrDefault(questionType, new FailedUpdateQuestion(questionType))
                .accept(webQuestion);
    }
}
