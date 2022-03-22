package com.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.HttpSession;

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

    @PostMapping("/")
    public String addUser(@ModelAttribute User user) {

    }


    @GetMapping("/settings")
    public String settings () {
        return "settings";
    }

    @GetMapping("/login")
    public String addUser(){
        return "";
    }

    @GetMapping("/getQuiz")
    public String apiTest(@RequestParam("amount") int amount, @RequestParam("category") int category,@RequestParam("difficulty") String difficulty, Model model) throws JsonProcessingException {

        model.addAttribute("questions", service.getQuestions(amount,category,difficulty));

        return "apiTest";
    }

    // https://opentdb.com/api.php?amount=20&category=13
    // https://opentdb.com/api_category.php (get all categories and their id in an array)
    // https://opentdb.com/api.php?amount=10&category=14&difficulty=medium&type=boolean
    // https://opentdb.com/api.php?amount=10&category=14&difficulty=medium&type=multiple
}
