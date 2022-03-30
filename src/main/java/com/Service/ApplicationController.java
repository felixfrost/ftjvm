package com.Service;

import com.Model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
public class ApplicationController {

    @Autowired
    ApplicationService service;

    @GetMapping("/")
    public String home (Model model, HttpSession session){
        User user = (User) session.getAttribute("currentUser");
        if(user == null){
            return ("redirect:/login");
        }
        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("/settings")
    public String settings (Model model, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if(user == null){
            return ("redirect:/login");
        }
        model.addAttribute("user", user);
        return "settings";
    }

    @GetMapping("settings/{num}")
    public String avatar(HttpSession session, @PathVariable Integer num) {
        User user = (User) session.getAttribute("currentUser");
        user.setAvatarId(num);
        service.saveAvatar(num, user.getUsername());
        return "redirect:/settings";
    }

    @GetMapping("/about-us")
    public String aboutUs (HttpSession session) {
        return "aboutUs";
    }

    @GetMapping("/multiplayer")
    public String multiPlayer (HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if(user == null){
            return ("redirect:/login");
        }
        return "multiplayer";
    }

    @GetMapping("/trivimania")
    public String game () {
        return "game";
    }

    @GetMapping("/score")
    public String score (HttpSession session, Model model){
        model.addAttribute("user", session.getAttribute("currentUser"));
        model.addAttribute("score", session.getAttribute("scoreCounter"));
        return "score";
    }

    @GetMapping("/highscore")
    public String highScore (HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        if(user == null){
            return ("redirect:/login");
        }
        model.addAttribute("userTop", (HighScore)service.findUserTopScore(user.getUsername()));
        model.addAttribute("user", user);
        model.addAttribute("topScores", service.getTopScores());
        model.addAttribute("todayTop", service.getTodayTop());
        model.addAttribute("weekTop", service.getWeekTop());

        System.out.println(service.getWeekTop());
        System.out.println(service.getTodayTop());
        System.out.println(service.getTopScores());
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
        if(service.findUser(user.getUsername()) == null) {
            service.saveUser(user);
            return "redirect:/";
        }
        else return "login";
    }

    @GetMapping("/login")
    public String login (){
        return "login";
    }

    @PostMapping("/login")
    public String login (HttpSession session, @RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        User user = service.findUser(username);
        if (session.getAttribute("currentUser") == null && user != null) {
            if (user.getPassword().equals(password)) {
                session.setAttribute("currentUser", user);
                System.out.println("Success logging in...");
                return "redirect:/";
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
    public String getQuiz(HttpSession session, Model model) {
        List<Question> questions = service.getQuestions((int)session.getAttribute("limit"),(String)session.getAttribute("category"));
        LocalDateTime startTime = LocalDateTime.now();

        session.setAttribute("questionCounter", 0);
        session.setAttribute("scoreCounter", 0);
        session.setAttribute("questions", questions);
        session.setAttribute("startTime", startTime);
        model.addAttribute("currentQuestion", questions.get((int)session.getAttribute("questionCounter")));
        model.addAttribute("user", session.getAttribute("currentUser"));
        model.addAttribute("currentQuestionNumber", 1);
        model.addAttribute("totalNumberOfQuestions", questions.size());
        return "game";
    }

    @GetMapping("/select")
    public String getSelectionScreen(HttpSession session, Model model) {
        if(session.getAttribute("currentUser") == null){
            return ("redirect:/login");
        }
        model.addAttribute("categories", service.getQuizCategories());
        return "gameSelection";
    }

    @PostMapping("/select")
    public String postSelectionScreen(@RequestParam(required = false) String category, @RequestParam(required = false) Integer limit, HttpSession session, Model model) {
        session.setAttribute("limit", limit);
        if (limit != null) return "redirect:/game";

        session.setAttribute("category", category);
        model.addAttribute("limit", service.getQuizLimits());
        model.addAttribute("chosenCategory", service.getFancyCategoryString(category));
        return "gameSelection";
    }


    @PostMapping("/nextQuestion")
    public String nextQuestion(HttpSession session, Model model, @RequestParam(required = false) Integer answer)  {
        int ctr = (int)session.getAttribute("questionCounter");

        List<Question> questionList = (List<Question>)session.getAttribute("questions");


        if (answer != null) {
            if (questionList.get(ctr).getMixedAnswers().get(answer).equals(questionList.get(ctr).getCorrectAnswer())) {
                long setPoints = 10000;
                long timeDiff = ChronoUnit.MILLIS.between((LocalDateTime)session.getAttribute("startTime"), LocalDateTime.now());
                int points = (int) (setPoints - timeDiff);

                session.setAttribute("scoreCounter",(Integer)session.getAttribute("scoreCounter") + points);

                System.out.println(timeDiff);
                System.out.println("Correct!");
            }

        }
        if(ctr == questionList.size()-1) {
            service.saveScore(new HighScore(null, (int)session.getAttribute("scoreCounter"), LocalDate.now(), (User)session.getAttribute("currentUser")));
            return "redirect:/score";
        }
        // updates values of the user
            ctr++;
            session.setAttribute("startTime", LocalDateTime.now());
            session.setAttribute("questionCounter", ctr);
            model.addAttribute("currentQuestion", questionList.get(ctr));
            model.addAttribute("user", session.getAttribute("currentUser"));
            model.addAttribute("currentQuestionNumber", ctr+1);
            model.addAttribute("totalNumberOfQuestions", questionList.size());
            return "game";
    }

    @GetMapping("/secret")
    public String easterEgg() {
        return "easterEgg";
    }

    @GetMapping("/secretGame")
    public String secretGame(HttpSession session, Model model) {
        List<SecretQuestion> questions = service.getSecretQuestions();
        LocalDateTime startTime = LocalDateTime.now();

        System.out.println(questions);

        session.setAttribute("questionCounter", 0);
        session.setAttribute("scoreCounter", 0);
        session.setAttribute("questions", questions);
        session.setAttribute("startTime", startTime);
        model.addAttribute("currentQuestion", questions.get((int)session.getAttribute("questionCounter")));
        model.addAttribute("user", session.getAttribute("currentUser"));
        model.addAttribute("currentQuestionNumber", 1);
        model.addAttribute("totalNumberOfQuestions", questions.size());
        return "secretGame";
    }


}
