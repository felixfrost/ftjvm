package com.Service;

import com.Model.Question;
import com.Model.QuizCategory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private List<QuizCategory> quizCategories = List.of(QuizCategory.values());
    private List<Integer> quizLimits = List.of(10,25,50);

    public List<Question> getQuestions(int limit, String categories) {
        String response;
        if (categories.equals(""))
            response = resttemplate.getForObject("https://the-trivia-api.com/questions?limit=" + limit, String.class);
        else
            response = resttemplate.getForObject("https://the-trivia-api.com/questions?categories=" + categories + "&limit=" + limit, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        List<Question> questions = null;
        try {
            questions = Arrays.asList(objectMapper.readValue(response, Question[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
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

    public List<Integer> getQuizLimits() {
        return quizLimits;
    }
}
