package com.Service;

import com.Model.Question;

import com.Model.QuizCategory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class ApplicationService {

    @Autowired
    RestTemplate resttemplate;
    @Autowired
    UserRepository userRepo;

    List<QuizCategory> quizCategories = List.of(QuizCategory.values());

    public List<Question> getQuestions(int limit, String categories) throws JsonProcessingException {
        String response;
        if (categories.equals(""))
            response = resttemplate.getForObject("https://the-trivia-api.com/questions?limit=" + limit, String.class);
        else
            response = resttemplate.getForObject("https://the-trivia-api.com/questions?categories=" + categories + "&limit=" + limit, String.class);

        System.out.println(response);
        ObjectMapper objectMapper = new ObjectMapper();
        List<Question> questions = Arrays.asList(objectMapper.readValue(response, Question[].class));
        questions.forEach(Question::mixAnswers);

        return questions;
    }

    public void getUsers() {
        List<User> userList = userRepo.findAll();
        System.out.println(userList);
    }

    public List<QuizCategory> getQuizCategories() {
        return quizCategories;
    }
}
