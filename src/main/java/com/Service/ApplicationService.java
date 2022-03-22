package com.Service;

import com.Model.Category;
import com.Model.Question;
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

    public List<Question> getQuestions(int amount, int category, String difficulty) throws JsonProcessingException {
        String response = resttemplate.getForObject("https://opentdb.com/api.php?amount=" + amount + "&category=" + category + "&difficulty=" + difficulty, String.class);

        String json = response.substring(29,response.length()-1);
        ObjectMapper objectMapper = new ObjectMapper();
        List<Question> questions = Arrays.asList(objectMapper.readValue(json, Question[].class));
        questions.forEach(Question::htmlCodeStrip);

        return questions;
    }

    public List<Category> getCategories() {
        String response = resttemplate.getForObject("https://opentdb.com/api_category.php", String.class);
        String json = response.substring(21, response.length()-1);
        List<Category> categories = null;
        try {
            categories = Arrays.asList(new ObjectMapper().readValue(json, Category[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //categories.forEach(System.out::println);
        return categories;
    }
}
