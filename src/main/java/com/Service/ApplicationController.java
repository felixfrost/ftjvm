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
    public String home (Model model, HttpSession session){
        if(session.getAttribute("currentUser") == null){
            return ("redirect:/login");
        }
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
        if (session.getAttribute("currentUser") == null && userRepo.findByUsernameEquals(username) != null) {
            if (userRepo.findByUsernameEquals(username).getPassword().equals(password)) {
                session.setAttribute("currentUser", userRepo.findByUsernameEquals(username));
                System.out.println("Success logging in...");
                return "home";
            }
        } else { System.out.println("Error logging in..."); }
        return "login";
    }

    @GetMapping("/logout")
    public String logout (HttpSession session) {
        session.setAttribute("currentUser", null);
        System.out.println("Logging out...");
        return "redirect:/";
    }

    @GetMapping("/game")
    //public String apiTest(@RequestParam("limit") int limit, @RequestParam("categories") String categories, Model model) throws JsonProcessingException {
    public String getQuiz(HttpSession session, Model model) {
        model.addAttribute("questions",
                service.getQuestions((int)session.getAttribute("limit"),(String)session.getAttribute("category")));
        return "game";
    }

    @GetMapping("/select")
    public String getSelectionScreen(HttpSession session, Model model) {
      /*  if(session.getAttribute("currentUser") == null){
            return ("redirect:/login");
        }*/
        model.addAttribute("categories", service.getQuizCategories());
        return "gameSelection";
    }

    @PostMapping("/select")
    public String postSelectionScreen(@RequestParam(required = false) String category, @RequestParam(required = false) Integer limit, HttpSession session, Model model) {
        session.setAttribute("limit", limit);
        if (limit != null) return "redirect:/getQuiz";

        session.setAttribute("category", category);
        model.addAttribute("limit", service.getQuizLimits());
        return "gameSelection";
    }
}
