package com.Service;

import com.Model.Category;
import com.Model.Question;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    RestTemplate resttemplate;
    @Autowired
    UserRepository userRepo;

    public List<Question> getQuestions(int limit, String categories) throws JsonProcessingException {
        String response = resttemplate.getForObject("https://the-trivia-api.com/questions?categories=" + categories + "&limit=" + limit, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        List<Question> questions = Arrays.asList(objectMapper.readValue(response, Question[].class));
        questions.forEach(Question::mixAnswers);

        return questions;
    }

    public void getUsers() {
        List<User> userList = userRepo.findAll();
        System.out.println(userList);
    }
}
