package com.Service;

import com.Model.Question;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
public class ApplicationController {

    @Autowired
    ApplicationService service;

    @Autowired
    RestTemplate resttemplate;

    @GetMapping("/")
    public String home (Model model){
        return "home";
    }


    @GetMapping("/getQuiz")
    public String apiTest(@RequestParam("amount") int amount, @RequestParam("category") int category,@RequestParam("difficulty") String difficulty, Model model) throws JsonProcessingException {

        String response = resttemplate.getForObject("https://opentdb.com/api.php?amount=" + amount + "&category=" + category + "&difficulty=" + difficulty, String.class);
        String qoute = String.valueOf('\u201d');

        String json = response.substring(29,response.length()-1);
        json = json.replaceAll("&quot;", qoute);
        json = json.replaceAll("&#039;", "´");
        json = json.replaceAll("&ldquo;", "");
        json = json.replaceAll("&rsquo;s", "");
        json = json.replaceAll("&hellip;", "´");
        json = json.replaceAll("&rdquo;", qoute);
        /*
        &oacute;
        &ldquo;
        &rsquo;s
        &hellip;
        &rdquo;
        */

        ObjectMapper objectMapper = new ObjectMapper();
        List<Question> questions = Arrays.asList(objectMapper.readValue(json, Question[].class));
        questions.stream().forEach(q -> System.out.println(q.getQuestion()));
        //questions.forEach(System.out::println);

        // List<Student> participantJsonList = mapper.readValue(jsonString, new TypeReference<List<Student>>(){})
        //List<Question> questions = response.get(1).toString();
        //questions.stream().forEach(System.out::println);
        //service.apiConnect(amount, category , difficulty);
        model.addAttribute("questions", questions);

        return "apiTest";
    }

    // https://opentdb.com/api.php?amount=20&category=13
    // https://opentdb.com/api_category.php (get all categories and their id in an array)
    // https://opentdb.com/api.php?amount=10&category=14&difficulty=medium&type=boolean
    // https://opentdb.com/api.php?amount=10&category=14&difficulty=medium&type=multiple
}
