package com.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class ApplicationController {

    @Autowired
    ApplicationService service;

    @Autowired
    UserRepository userRepo;

    @GetMapping("/")
    public String home (Model model){
        //service.getCategories();
        service.getUsers();
        return "home";
    }

    @GetMapping("/settings")
    public String settings () {
        return "settings";
    }

    @GetMapping("/about-us")
    public String aboutUs () {
        return "aboutUs";
    }

    @GetMapping("/trivimania")
    public String game () {
        return "game";
    }

    @GetMapping("/highscore")
    public String highScore () {
        return "highscore";
    }

    @GetMapping("/createuser")
    public String createUser (Model model){
        model.addAttribute("user", new User());
        return "createUser";
    }

    @PostMapping("/createuser")
    public String createUser (@Valid @ModelAttribute User user, BindingResult result) {
        if(result.hasErrors()){
            return "createUser";
        }
        if(userRepo.findByUsernameEquals(user.getUsername()) == null) {
            userRepo.save(user);
            System.out.println(user.getUsername() + user.getFirstname() + user.getLastname());
            return "home";
        }
        else return "login";
    }

    @GetMapping("/login")
    public String login (){
        return "login";
    }

    @PostMapping("/login")
    public String login (HttpSession session, @RequestParam("username") String username, @RequestParam("password") String password) {
        if (session.getAttribute("currentUser") == null){
            if(userRepo.findByUsernameEquals(username).getPassword() == password) {
                session.setAttribute("currentUser", userRepo.findByUsernameEquals(username));
                return "home";
            }
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout (HttpSession session) {
        session.setAttribute("currentUser", null);
        return "redirect:/";
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
