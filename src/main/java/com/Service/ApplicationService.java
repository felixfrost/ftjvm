package com.Service;

import com.Model.Category;
import com.Model.Question;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
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

    public List<Question> getQuestions(int amount, int category, String difficulty) throws JsonProcessingException {
        String response = resttemplate.getForObject("https://opentdb.com/api.php?amount=" + amount + "&category=" + category + "&difficulty=" + difficulty, String.class);

        String json = response.substring(29,response.length()-1);
        ObjectMapper objectMapper = new ObjectMapper();
        List<Question> questions = Arrays.asList(objectMapper.readValue(json, Question[].class));
        questions.forEach(Question::htmlCodeStrip);

        return questions;
    }

    public List<Category> getCategories() {
        // Get a list with all categories and their id's
        String response = resttemplate.getForObject("https://opentdb.com/api_category.php", String.class);
        String json = response.substring(21, response.length()-1);
        List<Category> categories = null;
        try {
            categories = Arrays.asList(new ObjectMapper().readValue(json, Category[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // Get a list with number of pending, rejected, verified and total sum of questions per category
        // Takes the number of verified question and add it to the corresponding category.
        response = resttemplate.getForObject("https://opentdb.com/api_count_global.php", String.class);
        JSONObject jsonObject = new JSONObject(response);
        JSONObject catNumOfQuestions = jsonObject.getJSONObject("categories");
        for(int i = 0; i< categories.size(); i++ )
            categories.get(i).setNumOfQuestions(
                    catNumOfQuestions.getJSONObject(Integer.toString(i+9))
                    .getInt("total_num_of_verified_questions"));

        //categories.forEach(System.out::println);
        return categories;
    }
}
