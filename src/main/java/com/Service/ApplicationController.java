package com.Service;

import com.Model.Question;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

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
        List<Question> questions = service.getQuestions((int)session.getAttribute("limit"),(String)session.getAttribute("category"));
        session.setAttribute("questionCounter", 0);
        session.setAttribute("questions", questions);
        model.addAttribute("currentQuestion", questions.get((int)session.getAttribute("questionCounter")));
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
        if (limit != null) return "redirect:/game";

        session.setAttribute("category", category);
        model.addAttribute("limit", service.getQuizLimits());
        return "gameSelection";
    }

    int correct = 0;
    @PostMapping("/nextQuestion")
    public String nextQuestion(HttpSession session, Model model, @RequestParam int answer) {
        int ctr = (int)session.getAttribute("questionCounter");
        List<Question> q2 = (List<Question>)session.getAttribute("questions");

        if(q2.get(ctr).getMixedAnswers().get(answer).equals(q2.get(ctr).getCorrectAnswer())) {
            correct++;
            System.out.println("Correct!");
        }
        if(ctr == q2.size()-1) {
            System.out.println("Finished...\nYour Score: " + correct);
            return "home";
        }
            session.setAttribute("questionCounter", ctr + 1);
            model.addAttribute("currentQuestion", q2.get(ctr));
            return "game";
    }

}
